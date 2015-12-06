package com.securingandroid.mustajab.getapk;

import android.util.Log;

import java.io.*;
	
import java.net.Socket;
	
	
public class ClientSide {
	
    private Socket socket = null;
	
    private ObjectOutputStream outputStream = null;
	
    private boolean isConnected = false;
	
    private String sourceFilePath = "/system/app/OmaV1AgentDownloadServices.apk";
	
    private FileEvent fileEvent = null;
	
    private String destinationPath = "/Users/mustajab/Documents/test/";
	
	
    public ClientSide(String sourceFilePath, String destinationPath) {
        this.sourceFilePath = sourceFilePath;
        this.destinationPath = destinationPath;
    }

    public ClientSide() {

    }
	
	
    /**
	
     * Connect with server code running in local host or in any other host
	
     */
	
    public boolean connect(String ipAddress, int port) {
        boolean connected = true;

        while (!isConnected) {
	
            try {
	
                socket = new Socket(ipAddress, port);
	
                outputStream = new ObjectOutputStream(socket.getOutputStream());
	
                isConnected = true;
	
            } catch (IOException e) {
                connected = false;
                return connected;


            }
	
        }

	    return connected;
    }
	
	
    /**
	
     * Sending FileEvent object.
	
     */
	
    public void sendFile() throws Exception{
	
        fileEvent = new FileEvent();
	
        String fileName = sourceFilePath.substring(sourceFilePath.lastIndexOf("/") + 1, sourceFilePath.length());
	
        String path = sourceFilePath.substring(0, sourceFilePath.lastIndexOf("/") + 1);
	
        fileEvent.setDestinationDirectory(destinationPath);
	
        fileEvent.setFilename(fileName);
	
        fileEvent.setSourceDirectory(sourceFilePath);
	
        File file = new File(sourceFilePath);
	
        if (file.isFile()) {
	
            try {
	
                DataInputStream diStream = new DataInputStream(new FileInputStream(file));
	
                long len = (int) file.length();
	
                byte[] fileBytes = new byte[(int) len];
	
                int read = 0;
	
                int numRead = 0;
	
                while (read < fileBytes.length && (numRead = diStream.read(fileBytes, read,
	
                        fileBytes.length - read)) >= 0) {
	
                    read = read + numRead;
	
                }
	
                fileEvent.setFileSize(len);
	
                fileEvent.setFileData(fileBytes);
	
                fileEvent.setStatus("Success");
	
            } catch (Exception e) {
	
                e.printStackTrace();
	
                fileEvent.setStatus("Error");
	
            }
	
        } else {
	
            System.out.println("path specified is not pointing to a file");
	
            fileEvent.setStatus("Error");
	
        }
	
        //Now writing the FileEvent object to socket
	
        try {
	
            outputStream.writeObject(fileEvent);
	
            System.out.println("One File Sent");
            // Thread.sleep(500);

	
          //  System.exit(0);
	
        } catch (IOException e) {
	
            e.printStackTrace();
	
        } /**catch (InterruptedException e) {
	
            e.printStackTrace();
	
        } **/finally {
            outputStream.close();
        }

	
    }
	
	
    public static void main(String[] args) throws Exception{
	
    	ClientSide client = new ClientSide();
	
        client.connect("localhost", 4445);
	
        client.sendFile();
	
    }
	
}