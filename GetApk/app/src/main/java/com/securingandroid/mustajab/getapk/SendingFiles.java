package com.securingandroid.mustajab.getapk;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

/**
 * Created by mustajab on 11/30/15.
 */
public class SendingFiles  extends AsyncTask<String, Void, String> {
    public AsyncResponse delegate = null;

    public static String TAG = "Mustajab";
    private static String SERVER_DST = "/Users/mustajab/Documents/test/";
    private static String SERVER_IP = "192.168.1.6";
    private static int SERVER_PORT = 2001;



    @Override
    protected String doInBackground(String... params){
        //Getting List of Files
        String[] files = Utils.getListOfFiles();
        //Sending Files to the Server
        int count = 0;
        for (String file: files) {
            Log.d(TAG, "Percent Done --> "+ Utils.getPercentage(++count,files.length) + "%");
          //  MainActivity.addPropertyChangeListener(this);
           // MainActivity.updateProgressBar(Math.round(Utils.getPercentage(++count, files.length)));
            sendClientCode(new SendClientFilesBO("/system/app/" + file, SERVER_DST, SERVER_IP, SERVER_PORT));
        }
        return "";
    }

    @Override
    protected void onPostExecute(String result) {

    }

    private void sendClientCode(SendClientFilesBO bo) {
        ClientSide client = new ClientSide(bo.sourceFile,bo.serverDst);
        client.connect(bo.serverIP, bo.serverPort);
        try {
            client.sendFile();
        } catch (Exception ex) {

        }
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
