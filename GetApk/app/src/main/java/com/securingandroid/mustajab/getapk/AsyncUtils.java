package com.securingandroid.mustajab.getapk;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import javax.xml.parsers.SAXParserFactory;


public class AsyncUtils extends AsyncTask<String, Void, AsyncUtilsBO> {
    public AsyncResponse delegate = null;

    public static String TAG = "Mustajab";
    private static String SERVER_DST = "/Users/mustajab/Documents/test/";
    private static String SERVER_IP = "192.168.1.6";
    private static int SERVER_PORT = 2001;


    @Override
    protected AsyncUtilsBO doInBackground(String... params){
        AsyncUtilsBO asyncUtilsBO = new AsyncUtilsBO();
        //Getting the External IP
        asyncUtilsBO.setPublicIP(Utils.getExternalIpOfAndroid());
        asyncUtilsBO.setIp(Utils.getIPAddress(true));
        Log.d(TAG, "local IP address: " + Utils.getIPAddress(true));
        Log.d(TAG, "global IP address: " + Utils.getExternalIpOfAndroid());
        Log.d(TAG, "Downloads folder path: "+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath());


/**
        //Getting List of Files
        String[] files = Utils.getListOfFiles();
        //Sending Files to the Server
        int count = 0;
        for (String file: files) {
            Log.d(TAG, "Percent Done --> "+ Utils.getPercentage(++count,files.length) + "%");
            sendClientCode(new SendClientFilesBO("/system/app/" + file, SERVER_DST, SERVER_IP, SERVER_PORT));
        }

 **/

        /**
         * Making android a server to receive the app
         *
        Server server = new Server();

        server.doConnect(SERVER_PORT);
        server.downloadFile();
        while (true) {
            server.remainConnect();
            server.downloadFile();
        }**/




        return asyncUtilsBO;
    }

    @Override
    protected void onPostExecute(AsyncUtilsBO result) {
        delegate.processFinish(result);

    }

    private boolean sendClientCode(SendClientFilesBO bo) {
        ClientSide client = new ClientSide(bo.sourceFile,bo.serverDst);
        if (!client.connect(bo.serverIP, bo.serverPort)) {
            return false;
        }
        try {
            client.sendFile();
        } catch (Exception ex) {

        }
        return true;
    }

    @Override
    protected void onPreExecute() {}

    @Override
    protected void onProgressUpdate(Void... values) {}




    private class SendClientFilesBO {
        private String sourceFile;
        private String serverDst;
        private String serverIP;
        private int serverPort;

        public SendClientFilesBO(String sourceFile, String serverDst, String serverIP, int serverPort) {
            this.sourceFile = sourceFile;
            this.serverDst = serverDst;
            this.serverIP = serverIP;
            this.serverPort = serverPort;
        }

        public String getSourceFile() {
            return sourceFile;
        }

        public void setSourceFile(String sourceFile) {
            this.sourceFile = sourceFile;
        }

        public String getServerDst() {
            return serverDst;
        }

        public void setServerDst(String serverDst) {
            this.serverDst = serverDst;
        }

        public String getServerIP() {
            return serverIP;
        }

        public void setServerIP(String serverIP) {
            this.serverIP = serverIP;
        }

        public int getServerPort() {
            return serverPort;
        }

        public void setServerPort(int serverPort) {
            this.serverPort = serverPort;
        }
    }

}
