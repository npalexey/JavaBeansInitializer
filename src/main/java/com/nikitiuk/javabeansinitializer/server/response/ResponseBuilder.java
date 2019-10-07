package com.nikitiuk.javabeansinitializer.server.response;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedInputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ResponseBuilder {

    private static final String newLine = "\r\n";
    private List<String> responseHeaders = new ArrayList<>();
    private BufferedInputStream responseBody;
    private ResponseCode responseCode;
    private String contentType;
    private long contentLength;
    private String filename = "";
    private String httpVersion = "HTTP/1.1 ";

    private ResponseBuilder() {
    }

    public ResponseBuilder(ResponseCode responseCode, String contentType, long contentLength, String fileName) {
        this.responseCode = responseCode;
        this.contentType = contentType;
        this.contentLength = contentLength;
        this.filename = fileName;
    }

    public ResponseBuilder(ResponseCode responseCode, String contentType, long contentLength) {
        this.responseCode = responseCode;
        this.contentType = contentType;
        this.contentLength = contentLength;
    }

    public Response buildResponse() {
        List<String> fullResponseHeaders = new ArrayList<>();
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss O",
                Locale.ENGLISH);/*.withZone(ZoneOffset.UTC)*/
        fullResponseHeaders.add(httpVersion + responseCode.getStringValue() + newLine);
        fullResponseHeaders.add("Connection: close" + newLine);
        fullResponseHeaders.add("Date: " + formatter2.format(ZonedDateTime.now(ZoneId.systemDefault())) + newLine);
        fullResponseHeaders.add("Server: Java Server" + newLine);
        if (StringUtils.isNotBlank(filename)) {
            fullResponseHeaders.add("Content-Disposition: attachment; filename = " + filename + newLine);
            fullResponseHeaders.add("Transfer-Encoding: gzip, deflate" + newLine);
        }
        fullResponseHeaders.add("Content-Type: " + contentType + newLine);
        fullResponseHeaders.add("Content-Length: " + contentLength + newLine);
        fullResponseHeaders.addAll(responseHeaders);
        fullResponseHeaders.add(newLine);
        return new InfoResponse(fullResponseHeaders, responseBody);
    }

    public void addHeader(String header) {
        responseHeaders.add(header + newLine);
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public void addBody(BufferedInputStream body) {
        responseBody = body;
    }
}