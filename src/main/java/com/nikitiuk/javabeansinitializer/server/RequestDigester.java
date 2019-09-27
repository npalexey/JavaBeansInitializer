package com.nikitiuk.javabeansinitializer.server;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class RequestDigester {

    private static final Logger logger = LoggerFactory.getLogger(RequestDigester.class);

    public Request parse(BufferedInputStream bufferedInputStream) throws IOException {
        byte[] some = readLine(bufferedInputStream);
        String line = readLineIntoString(bufferedInputStream);
        logger.info("Read in String:" + line);
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
        return stringBuilder.toString();
        /*byte[] line = readLine(bis);
        StringBuilder stringBuilder = new StringBuilder();
        for (int b : line) {
            stringBuilder.append((char) b);
        }
        return stringBuilder.toString();*/
    }
}