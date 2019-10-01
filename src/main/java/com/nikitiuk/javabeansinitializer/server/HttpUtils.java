package com.nikitiuk.javabeansinitializer.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

final public class HttpUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    private HttpUtils() {
    }

    public static Request readRequest(InputStream inputStream) throws IOException {
        //BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        BufferedInputStream bis = new BufferedInputStream(inputStream);

        /*System.out.println(bufferedReader.readLine());
        System.out.println(bufferedReader.readLine());


        byte[] bytes = new byte[100];
        int read = bis.read(bytes);
        System.out.println("Read: " + read);
        System.out.println(String.valueOf(bytes));
        System.out.println(bufferedReader.readLine());*/
        return new RequestDigester().parse(bis);
    }

    public static void sendResponse() {

    }
}
