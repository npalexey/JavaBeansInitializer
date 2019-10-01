package com.nikitiuk.javabeansinitializer.server;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;

public class RequestDigester {

    private static final Logger logger = LoggerFactory.getLogger(RequestDigester.class);

    public Request parse(BufferedInputStream bufferedInputStream) throws IOException {
        List<String> headersList = readHeadersTillEmptyLine(bufferedInputStream);
        String firstHeaderLine = headersList.remove(0);
        Map<String, String> headersMap = putHeadersIntoMap(headersList);
        RequestMethod requestMethod = getRequestMethod(firstHeaderLine);
        String url = getUrl(firstHeaderLine);

        switch (requestMethod) {
            case GET:
            case DELETE:
                return new InfoRequest(url, requestMethod, headersMap);
            case PUT:
            case POST:
                return new ContentRequest(url, requestMethod, headersMap, parseContentRequest(headersList, bufferedInputStream));
            default:
                throw new RuntimeException("Such Request Method Is Not Implemented Yet.");
        }
    }

    private List<String> readHeadersTillEmptyLine(BufferedInputStream bis) throws IOException {
        List<String> headers = new ArrayList<>();
        while (true) {
            String nextLine = readLineIntoString(bis);
            logger.info(nextLine.replaceAll("\r\n", ""));
            if (nextLine.equals("\r\n")) {
                break;
            }
            headers.add(nextLine);
        }
        return headers;
    }

    private RequestMethod getRequestMethod(String firstHeaderLine) {
        /*StringTokenizer tokenizer = new StringTokenizer(firstHeaderLine, " ");
        return RequestMethod.valueOf(tokenizer.nextToken());*/
        return RequestMethod.valueOf(firstHeaderLine.split(" ", 2)[0]);
    }

    private String getUrl(String firstHeaderLine) {
        /*StringTokenizer tokenizer = new StringTokenizer(firstHeaderLine, " ");
        tokenizer.nextToken();
        return tokenizer.nextToken();*/
        return firstHeaderLine.split(" ", 3)[1];
    }

    private Map<String, byte[]> parseContentRequest(List<String> headersList, BufferedInputStream bis) throws IOException {
        String contentType = "", boundary = "";
        int contentLength = 0;
        for (String line : headersList) {
            if (line.startsWith("Content-Type")) {
                StringTokenizer tokenizer = new StringTokenizer(line, " ");
                tokenizer.nextToken();
                contentType = tokenizer.nextToken().replaceAll(";", "").trim();
                if (contentType.equals(MimeType.MULTIPART_FORM_DATA.mimeTypeName())) {
                    boundary = "--" + tokenizer.nextToken().substring(9).replaceAll("\r\n", "");   //'boundary=' - 9 chars
                } else if (!contentType.equals(MimeType.APPLICATION_JSON.mimeTypeName())){
                    throw new RuntimeException("Cannot parse other types of body yet");
                }
            } else if (line.startsWith("Content-Length")) {
                contentLength = Integer.parseInt(line.substring(16).replaceAll("\r\n", ""));   //'Content-Length: ' - 16 chars
            }
        }
        if (contentType.equals(MimeType.MULTIPART_FORM_DATA.mimeTypeName()) && !boundary.equals("")) {
            return parseMultipartFormData(boundary, bis);
        } else if (contentType.equals(MimeType.APPLICATION_JSON.mimeTypeName())){
            return parseJsonBody(contentLength, bis);
        } else {
            throw new RuntimeException("Cannot parse other types of body yet");
        }
    }

    private Map<String, byte[]> parseContentRequestMAP(Map<String, String> headersMap, BufferedInputStream bis) throws IOException {
        String contentType, boundary = "";
        String contentTypeEntry = headersMap.get("Content-Type");
        if(contentTypeEntry.contains(";")) {
            int indexOfSemicolon = contentTypeEntry.indexOf(";");
            contentType = contentTypeEntry.substring(0, indexOfSemicolon);
            boundary = "--" + contentTypeEntry.substring(indexOfSemicolon + 11).trim();
        } else {
            contentType = contentTypeEntry.trim();
        }
        int contentLength = Integer.parseInt(headersMap.get("Content-Length"));
        if (contentType.equals(MimeType.MULTIPART_FORM_DATA.mimeTypeName()) && StringUtils.isNotBlank(boundary)) {
            return parseMultipartFormData(boundary, bis);
        } else if (contentType.equals(MimeType.APPLICATION_JSON.mimeTypeName())){
            return parseJsonBody(contentLength, bis);
        } else {
            throw new RuntimeException("Cannot parse other types of body yet");
        }
    }

    private String[] getContentTypeAndBoundaryIfExists(String contentTypeEntry) {
        return null;
    }

    private Map<String, byte[]> parseMultipartFormData(String boundary, BufferedInputStream bis) throws IOException {
        readRequestStreamTillCertainBoundary(boundary, bis);
        return parseMultipartBody(boundary, bis);
    }

    private Map<String, byte[]> parseMultipartBody(String boundary, BufferedInputStream bis) throws IOException {
        boolean reachedFinalBoundary = false;
        Map<String, byte[]> body = new HashMap<>();
        String finalBoundary = boundary + "--\r\n";
        boundary = boundary + "\r\n";
        byte[] byteBoundary = boundary.getBytes();
        byte[] byteFinalBoundary = finalBoundary.getBytes();
        String currentPartName = "";
        String nextLine = "";
        while (!reachedFinalBoundary) {
            while (!nextLine.equals("\r\n")) {
                nextLine = readLineIntoString(bis);
                if (nextLine.startsWith("Content-Disposition")) {
                    String[] contentDisposition = nextLine.split(" ");
                    for (String param : contentDisposition) {
                        if (param.startsWith("name") && !param.substring(5, 7).equals("\"\"")) {  //name="";
                            currentPartName = param.substring(6, param.lastIndexOf("\""));
                            break;
                        } else if (param.startsWith("filename")) {
                            currentPartName = param.substring(10, param.lastIndexOf("\""));
                            break;
                        }
                    }
                }
            }
            if (currentPartName.equals("")) {
                throw new RuntimeException("Cannot parse name of file.");
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            while (true) {
                byte[] nextByteLine = readLine(bis);
                nextLine = new String(nextByteLine);
                if (Arrays.equals(nextByteLine, byteBoundary)/*nextLine.equals(boundary)*/) {
                    break;
                } else if (Arrays.equals(nextByteLine, byteFinalBoundary)/*nextLine.equals(finalBoundary)*/) {
                    reachedFinalBoundary = true;
                    break;
                }
                byteArrayOutputStream.write(nextByteLine);
            }
            body.put(currentPartName, byteArrayOutputStream.toByteArray());
            byteArrayOutputStream.close();
        }
        return body;
    }

    private Map<String, byte[]> parseJsonBody(int contentLength, BufferedInputStream bis) throws IOException {
        readRequestStreamTillCertainBoundary("{\n", bis);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[contentLength/*8 * 1024*/];
        int bytesRead;
        bis.read(buffer);
        byteArrayOutputStream.write("{\n".getBytes());
        byteArrayOutputStream.write(buffer);
        /*while ((bytesRead = bis.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }*/
        Map<String, byte[]> body = new HashMap<>();
        body.put("JsonArray", byteArrayOutputStream.toByteArray());
        return body;
    }

    private Map<String, String> putHeadersIntoMap(List<String> headersList) {
        Map<String, String> map = new HashMap<>();
        for(String header : headersList) {
            String[] splitHeader = header.split(":", 2);
            map.put(splitHeader[0], splitHeader[1].trim());
        }
        logger.info("Headers map: " + map.toString());
        return map;
    }

    private void readRequestStreamTillCertainBoundary(String boundary, BufferedInputStream bis) throws IOException {
        String nextLine = "";
        while (!nextLine.equals(boundary)) {
            nextLine = readLineIntoString(bis).replaceAll("\r\n", "");
        }
    }

    private byte[] readLine(BufferedInputStream bis) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int nextByte;
        StringBuilder stringBuilder = new StringBuilder();
        while ((nextByte = bis.read()) != 10 /*read = (char)nextByte) != '\n'*/) {
            stringBuilder.append((char) nextByte);
            byteArrayOutputStream.write(nextByte);
        }
        byteArrayOutputStream.write(10);
        logger.info(stringBuilder.toString());
        return byteArrayOutputStream.toByteArray();
    }

    private String readLineIntoString(BufferedInputStream bis) throws IOException {
        int nextByte;
        StringBuilder stringBuilder = new StringBuilder();
        while ((nextByte = bis.read()) != 10 /*read = (char)nextByte) != '\n'*/) {
            stringBuilder.append((char) nextByte);
        }
        stringBuilder.append("\n");
        return stringBuilder.toString();
        /*byte[] line = readLine(bis);
        StringBuilder stringBuilder = new StringBuilder();
        for (int b : line) {
            stringBuilder.append((char) b);
        }
        return stringBuilder.toString();*/
    }
}