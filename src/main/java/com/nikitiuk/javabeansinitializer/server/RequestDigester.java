package com.nikitiuk.javabeansinitializer.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class RequestDigester {

    private static final Logger logger = LoggerFactory.getLogger(RequestDigester.class);

    public Request parse(BufferedInputStream bufferedInputStream) throws IOException {
        /*byte[] some = readLine(bufferedInputStream);
        String line = readLineIntoString(bufferedInputStream);
        logger.info("Read in String:" + line);*/
        List<String> headersList = readHeadersTillEmptyLine(bufferedInputStream);
        String firstHeaderLine = headersList.remove(0);
        RequestMethod requestMethod = getRequestMethod(firstHeaderLine);
        String url = getUrl(firstHeaderLine);
        logger.info(requestMethod.toString());
        logger.info(url);

        logger.info(headersList.toString());

        switch (requestMethod) {
            case GET:
            case DELETE:
                return new InfoRequest(url, requestMethod);
            case PUT:
            case POST:
                //return new FileRequest() parseFileRequest(headersList, bufferedInputStream);
            default:
                throw new RuntimeException("Request Method Is Incorrect.");
        }
    }

    private List<String> readHeadersTillEmptyLine(BufferedInputStream bis) throws IOException {
        List<String> headers = new ArrayList<>();
        while (true) {
            String nextLine = readLineIntoString(bis);
            if (nextLine.equals("\r\n")) {
                break;
            }
            headers.add(nextLine);
        }
        return headers;
    }

    private RequestMethod getRequestMethod(String firstHeaderLine) {
        StringTokenizer tokenizer = new StringTokenizer(firstHeaderLine, " ");
        return RequestMethod.valueOf(tokenizer.nextToken());
    }

    private String getUrl(String firstHeaderLine) {
        StringTokenizer tokenizer = new StringTokenizer(firstHeaderLine, " ");
        tokenizer.nextToken();
        return tokenizer.nextToken();
    }

    private Map<String, byte[]> parseFileRequest(List<String> headersList, BufferedInputStream bis) throws IOException {
        String contentType = "", boundary = "";
        int contentLength;
        for(String line : headersList) {
            if(line.startsWith("Content-Type")) {
                StringTokenizer tokenizer = new StringTokenizer(line, " ");
                tokenizer.nextToken();
                contentType = tokenizer.nextToken().replaceAll(";", "").trim();
                boundary = "--" + tokenizer.nextToken().substring(9);   //'boundary=' - 9 chars
            } else if(line.startsWith("Content-Length")) {
                contentLength = Integer.parseInt(line.substring(16));   //'Content-Length: ' - 16 chars
            }
        }
        if(contentType.equals("multipart/form-data") && !boundary.equals("")) {
            return parseMultipartFormData(boundary, bis);
        }
        return null;
    }

    private Map<String, byte[]> parseMultipartFormData(String boundary, BufferedInputStream bis) throws IOException {
        String nextLine = "";
        while (!nextLine.equals(boundary)){
            nextLine = readLineIntoString(bis);
        }
        return parseMultipartBody(boundary,bis);
    }

    private Map<String, byte[]> parseMultipartBody(String boundary, BufferedInputStream bis) throws IOException {
        boolean reachedFinalBoundary = false;
        Map<String, byte[]> body = new HashMap<>();
        String currentPartName;
        String nextLine = "";
        while(!reachedFinalBoundary) {
            while (!nextLine.equals("\r\n")) {
                nextLine = readLineIntoString(bis);
                if(nextLine.startsWith("Content-Disposition")) {
                    String[] contentDisposition = nextLine.split(" ");
                    insertFor:
                        for(String param : contentDisposition) {
                            if(param.startsWith("name") && !param.substring(5, 7).equals("\"\"")) {  //name="";
                                currentPartName = param.substring(6, param.lastIndexOf("\""));
                                break insertFor;
                            }
                        }
                }
            }
        }
        return null;
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