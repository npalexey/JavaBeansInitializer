package com.nikitiuk.javabeansinitializer.server.request;

import com.nikitiuk.javabeansinitializer.server.utils.MimeType;
import com.nikitiuk.javabeansinitializer.server.request.types.ContentRequest;
import com.nikitiuk.javabeansinitializer.server.request.types.InfoRequest;
import com.nikitiuk.javabeansinitializer.server.request.types.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
                return new ContentRequest(url, requestMethod, headersMap, parseContentRequestBody(headersMap, bufferedInputStream));
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
        return RequestMethod.valueOf(firstHeaderLine.split(" ", 2)[0]);
    }

    private String getUrl(String firstHeaderLine) {
        return firstHeaderLine.split(" ", 3)[1];
    }

    private Map<String, byte[]> parseContentRequestBody(Map<String, String> headersMap, BufferedInputStream bis) throws IOException {
        List<String> contentTypeAndBoundary = getContentTypeAndBoundaryFromHeader(headersMap.get("Content-Type"));
        int contentLength = Integer.parseInt(headersMap.get("Content-Length"));
        if (contentTypeAndBoundary.get(0).equals(MimeType.MULTIPART_FORM_DATA) && contentTypeAndBoundary.size() == 2) {
            return parseMultipartBody(contentTypeAndBoundary.get(1), bis);
        } else if (contentTypeAndBoundary.get(0).equals(MimeType.APPLICATION_JSON)){
            return parseJsonBody(contentLength, bis);
        } else {
            throw new RuntimeException("Cannot parse other types of body yet");
        }
    }

    private List<String> getContentTypeAndBoundaryFromHeader (String contentTypeEntry) {
        List<String> contentTypeAndBoundary = new ArrayList<>();
        if(contentTypeEntry.contains(";")) {
            int indexOfSemicolon = contentTypeEntry.indexOf(";");
            contentTypeAndBoundary.add(contentTypeEntry.substring(0, indexOfSemicolon));
            contentTypeAndBoundary.add("--" + contentTypeEntry.substring(indexOfSemicolon + 11).trim());
        } else {
            contentTypeAndBoundary.add(contentTypeEntry.trim());
        }
        return contentTypeAndBoundary;
    }

    private Map<String, byte[]> parseMultipartBody(String boundary, BufferedInputStream bis) throws IOException {
        readRequestStreamTillCertainBoundary(boundary, bis);
        return parseMultipartTrimmedBody(boundary, bis);
    }

    private Map<String, byte[]> parseMultipartTrimmedBody(String boundary, BufferedInputStream bis) throws IOException {
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
                            if(contentDisposition.length < 4) {
                                currentPartName = param.substring(6, param.lastIndexOf("\""));
                            } else {
                                currentPartName = "file:" + param.substring(6, param.lastIndexOf("\""));
                            }
                            break;
                        } else if (param.startsWith("filename")) {
                            currentPartName = "file:" + param.substring(10, param.lastIndexOf("\""));
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
                if (Arrays.equals(nextByteLine, byteBoundary)) {
                    break;
                } else if (Arrays.equals(nextByteLine, byteFinalBoundary)) {
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
        int bytesRead = bis.read(buffer);
        logger.info(String.format("%d bytes of request's json body were read.", bytesRead));
        byteArrayOutputStream.write("{\n".getBytes());
        byteArrayOutputStream.write(buffer);
        Map<String, byte[]> body = new HashMap<>();
        body.put("JsonArray", byteArrayOutputStream.toByteArray());
        byteArrayOutputStream.close();
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
        while ((nextByte = bis.read()) != 10) {      // !='\n'
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
        while ((nextByte = bis.read()) != 10) {
            stringBuilder.append((char) nextByte);
        }
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }
}