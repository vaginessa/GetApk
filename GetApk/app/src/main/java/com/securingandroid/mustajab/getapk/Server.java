package com.securingandroid.mustajab.getapk;

import android.util.Log;

import java.io.File;
	
import java.io.FileOutputStream;
	
import java.io.IOException;
	
import java.io.ObjectInputStream;
	
import java.net.ServerSocket;
	
import java.net.Socket;
	
	
public class Server {
	
    private ServerSocket serverSocket = null;
	
    private Socket socket = null;
	
    private ObjectInputStream inputStream = null;
	
    private FileEvent fileEvent;
	
    private File dstFile = null;
	
    private FileOutputStream fileOutputStream = null;
	
	
    public Server() {
	
	
    }
	
	
    /**
	
     * Accepts socket connection
	
     */
	
    public void doConnect(int port) {
        try {
            serverSocket = new ServerSocket(port);
            Log.d("Mustajab", "Server is listening at port -->"+port);
            socket = serverSocket.accept();
            Log.d("Mustajab", "Got response from client");
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
	
    }
    
    public void remainConnect() {
        try {
            socket = serverSocket.accept();
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
	
    }
	 public boolean closeConnect() {
         boolean successfullyClosed = true;
         try {
            socket.close();
            inputStream.close();
         } catch(Exception ex) {
             successfullyClosed = false;
         }
         return successfullyClosed;
     }
	
    /**
	
     * Reading the FileEvent object and copying the file to disk.
	
     */
	
    public String downloadFile() {
        String outputFile = "";
	
        try {
	
            fileEvent = (FileEvent) inputStream.readObject();
	
            if (fileEvent.getStatus().equalsIgnoreCase("Error")) {
	
                System.out.println("Error occurred ..So exiting");
	
                //System.exit(0);
	
            }
	
            outputFile = fileEvent.getDestinationDirectory() + fileEvent.getFilename();
	
            if (!new File(fileEvent.getDestinationDirectory()).exists()) {
	
                new File(fileEvent.getDestinationDirectory()).mkdirs();
	
            }
	
            dstFile = new File(outputFile);
	
            fileOutputStream = new FileOutputStream(dstFile);
	
            fileOutputStream.write(fileEvent.getFileData());
	
            fileOutputStream.flush();
	
            fileOutputStream.close();
	
            System.out.println("Output file : " + outputFile + " is successfully saved ");


	
            //Thread.sleep(3000);
	
           // System.exit(0);
	
	
        } catch (IOException e) {
	
            e.printStackTrace();
	
        } catch (ClassNotFoundException e) {
	
            e.printStackTrace();
	
        } /**catch (InterruptedException e) {
	
            e.printStackTrace();
	
        }**/
        return fileEvent.getFilename();
    }
	
	
    public static void main(String[] args) {
    
	        Server server = new Server();
	    	
	        server.doConnect(4445);
	        server.downloadFile();
	        while (true) {
	        	 server.remainConnect();
	        	 server.downloadFile();
	        }
	       
	        
    	}
    }
	
