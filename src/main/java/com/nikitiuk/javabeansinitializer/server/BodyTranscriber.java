package com.nikitiuk.javabeansinitializer.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class BodyTranscriber {

    private static final Logger logger = LoggerFactory.getLogger(BodyTranscriber.class);
    private BufferedInputStream bufferedInputStream;
    private BufferedReader bufferedReader;
    private DataInputStream dataInputStream;
    private String boundary;
    private String finalBoundary;
    private int sizeOfFinalBoundaryInBytes;

    public BodyTranscriber(BufferedInputStream bufferedInputStream, String boundary) {
        this.bufferedInputStream = bufferedInputStream;
        this.bufferedReader = new BufferedReader(new InputStreamReader(bufferedInputStream));
        this.dataInputStream = new DataInputStream(bufferedInputStream);
        this.boundary = "--" + boundary;
        this.finalBoundary = this.boundary + "--";
        this.sizeOfFinalBoundaryInBytes = this.finalBoundary.getBytes().length;
    }

    public void transcribeBodyIntoData() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(bufferedReader.readLine());
        if(bufferedReader.readLine().equals(boundary)) {
            stringBuilder.append(bufferedReader.readLine());
            stringBuilder.append(bufferedReader.readLine());
            stringBuilder.append(bufferedReader.readLine());
            /*bufferedReader.mark(50000);
            boolean notFinal = true;
            while(notFinal) {
                String nextLine = bufferedReader.readLine();
                stringBuilder.append(nextLine);
                if(nextLine.equals(finalBoundary)) {
                    notFinal = false;
                    bufferedReader.reset();
                }
            }*/
            logger.info(stringBuilder.toString());
            File targetFile = new File("/home/npalexey/workenv/sometext.txt");
            OutputStream outStream = new FileOutputStream(targetFile);
            int chunks = dataInputStream.available()/sizeOfFinalBoundaryInBytes;
            byte[] buffer = new byte[8 * 1024];
            int bytesRead;
            for(int i = 0; i < chunks -1; i++)
            while (dataInputStream.available() != sizeOfFinalBoundaryInBytes) {
                if((bytesRead = dataInputStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, bytesRead);
                }
                /*outStream.write(dataInputStream., 0, dataInputStream.available() - sizeOfFinalBoundaryInBytes);*/
            }
        }

    }

    private boolean reachedFinal() throws IOException {
        bufferedReader.mark(400);
        if(bufferedReader.readLine().equals(finalBoundary)){
            return true;
        }
        bufferedReader.reset();
        return false;
    }
}
