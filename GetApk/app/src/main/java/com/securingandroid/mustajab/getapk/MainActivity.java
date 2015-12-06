package com.securingandroid.mustajab.getapk;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements AsyncResponse{
    private static final String TAG = "MUSTAJAB";
    private static String IP = "";
    AsyncUtils asyncTask = new AsyncUtils();

    ReceivingFiles receivingFiles = new ReceivingFiles();
    public ProgressBar progressBar2 = null;
    TextView progressTextView = null;
    TextView ipAdrs = null;
    TextView publicIP = null;
    TextView filesReceived = null;
    EditText serverIp = null;
    EditText port = null;

    TextView rARespTextView = null;
    TextView wARespTextView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //this to set delegate/listener back to this class

        asyncTask.delegate = this;

        //execute the async task
        asyncTask.execute();


        setContentView(R.layout.activity_main);
        TextView numOfInstalledApps = (TextView)findViewById(R.id.installedApps);
        numOfInstalledApps.setText("" + getNumOfInstalledApps());
        // getListOfFiles
        TextView preInstalledApps = (TextView)findViewById(R.id.preInstalledApps);
        preInstalledApps.setText("" + Utils.getListOfFiles().length);

        Button receiveApks = (Button) findViewById(R.id.recBtn);
        receiveApks.setEnabled(true);
/**
        rARespTextView = (TextView) findViewById(R.id.rARespTextView);
        wARespTextView = (TextView) findViewById(R.id.wARespTextView);

        Boolean[] readWriteAccess = Utils.getReadWriteAccessAppsInstalledFolder();
        if(readWriteAccess[0]) {
            rARespTextView.setText("Yes");
        } else {
            rARespTextView.setText("No");
        }

        if(readWriteAccess[1]) {
            wARespTextView.setText("Yes");
        } else {
            wARespTextView.setText("No");
        }

 **/

        //wARespTextView


        //Ip Address of Device
        ipAdrs = (TextView)findViewById(R.id.ipAdrs);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        progressTextView = (TextView) findViewById(R.id.progressTextView);



        publicIP = (TextView)findViewById(R.id.publicIP);
        filesReceived = (TextView) findViewById(R.id.fileReceived);

        serverIp = (EditText)findViewById(R.id.serverIP);
        port = (EditText)findViewById(R.id.port);
        //ipAdrs


/**
 ClientSide client = new ClientSide();
        client.connect();
        client.sendFile(); **/
/**
        try {GetIpAddress.GetIp();}
        catch (IOException ex) {
            Log.d(TAG, "Exception" + ex);
        } **/


/**


          File appsDir = null;
        try {
            Log.d(TAG, "Trying to access the Directory");
            //appsDir = new File("/data/app");//Installed Applications
            appsDir = new File("/system/app");// Pre-installed Applications
        } catch (Exception ex) {
            Log.d(TAG, "Unable to find the Directory");
        }
        Log.d(TAG, "Can read directory --> "+ appsDir.canRead());
        Log.d(TAG, "Can write directory --> "+ appsDir.canWrite());
        Log.d(TAG, "Is Directory --> "+ appsDir.isDirectory());

        String[] files = appsDir.list();

        Log.d(TAG, "list of files " + files);

        if (files != null) {
            Log.d(TAG, "list of files " + files.length);

            for (int i = 0 ; i < files.length ; i++ ) {
                Log.d(TAG, "File: " + files[i]);
            }
        }




        List<String> pkg_path = new ArrayList<String>();
        PackageManager  pm = getPackageManager();
        List<PackageInfo> pkginfo_list = pm.getInstalledPackages(PackageManager.GET_ACTIVITIES);
        List<ApplicationInfo> appinfo_list = pm.getInstalledApplications(0);
        for (int x=0; x < pkginfo_list.size(); x++){
            PackageInfo pkginfo = pkginfo_list.get(x);
            pkg_path.add( appinfo_list.get(x).publicSourceDir);  //store package path in array
        }

        Log.d(TAG, "Pakages path--> " + pkg_path);

        Log.d(TAG, "No. of installed apps--> " + pkg_path.size());
**/

        ///////////////////
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /** Called when the user touches the button */
    public void downloadApp(String apkName) throws Exception{
        Log.d(TAG, "IP is :"+ IP);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/" + apkName)), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void sendApks(View view) throws Exception{
        SendingFiles sendingFiles = new SendingFiles();
        sendingFiles.execute();
        view.setEnabled(false);
        Button receiveApks = (Button) findViewById(R.id.recBtn);
        receiveApks.setEnabled(true);
    }

    public void receiveApks(View view) throws Exception {

        receivingFiles.execute();
        view.setEnabled(false);

        Button sendApks = (Button) findViewById(R.id.sendApks);
        sendApks.setEnabled(false);

        Button stopRecBtn = (Button) findViewById(R.id.stopRecBtn);
        stopRecBtn.setEnabled(true);
    }

    public void stopRecBtn(View view) throws Exception {
        Button sendApks = (Button) findViewById(R.id.sendApks);
        sendApks.setEnabled(true);


        receivingFiles.cancel(true);
        view.setEnabled(false);
    }
//progressBar2





    //this override the implemented method from asyncTask

    public void processFinish(AsyncUtilsBO output){
        Log.d(TAG, "Got IP from ProcessFinish: " + output.getIp());
        ipAdrs.setText(output.getIp());
        publicIP.setText(output.getPublicIP());
    }

    /**
     *
     * @return
     */
    public int getNumOfInstalledApps() {
        List<String> pkg_path = new ArrayList<String>();
        PackageManager  pm = getPackageManager();
        List<PackageInfo> pkginfo_list = pm.getInstalledPackages(PackageManager.GET_ACTIVITIES);
        List<ApplicationInfo> appinfo_list = pm.getInstalledApplications(0);
        for (int x=0; x < pkginfo_list.size(); x++){
            PackageInfo pkginfo = pkginfo_list.get(x);
            pkg_path.add( appinfo_list.get(x).publicSourceDir);  //store package path in array
        }

        //Log.d(TAG, "Pakages path--> " + pkg_path);

        //Log.d(TAG, "No. of installed apps--> " + pkg_path.size());
        return pkg_path.size();
    }


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

    /**
     *
     */
    public class ReceivingFiles  extends AsyncTask<String, String, String> {
        public String TAG = "Mustajab";
        private  int SERVER_PORT = 2001;
        Server server = new Server();
        String donwnloadedApk = "";

        @Override
        protected String doInBackground(String... params) {

            publishProgress("1");

            server.doConnect(SERVER_PORT);
            donwnloadedApk = server.downloadFile();
            publishProgress(donwnloadedApk);
            //filesReceived.setText(server.downloadFile());
            while (true) {
                if (this.isCancelled()) {
                    server.closeConnect();
                    break;


                } else {
                    try {
                        downloadApp(donwnloadedApk);
                    } catch (Exception ex) {
                        
                    }

                    server.remainConnect();
                    donwnloadedApk = server.downloadFile();
                    publishProgress(donwnloadedApk);
                }


            }
            return "connClosed";
        }

        @Override
        protected void onPostExecute(String result) {
                Button receiveApks = (Button) findViewById(R.id.recBtn);
                receiveApks.setEnabled(true);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... values) {
            if(values[0] == "1") {
                filesReceived.setText("Listening at port "+SERVER_PORT);

            } else if(values[0] == "-999") {
                filesReceived.setTextColor(Color.RED);
                filesReceived.setText("Stopped Listening at port "+SERVER_PORT);
            } else {
                filesReceived.setText(filesReceived.getText() + ", " + values[0]);
            }
        }
    }


    /**
     *
     */
    public class SendingFiles  extends AsyncTask<String, Integer, Boolean> {
        public AsyncResponse delegate = null;
        public String TAG = "Mustajab";
        private String SERVER_DST = "/Users/mustajab/Documents/test/";
        private String SERVER_IP = "192.168.1.6";////serverIP; serverIp.getText().toString();
        //private  int SERVER_PORT = 2001;
        private int SERVER_PORT = 4445;///Integer.getInteger(port.getText().toString());//port
        private boolean stopExecution = false;

        @Override
        protected Boolean doInBackground(String... params) {
            boolean connectedtoServer = false;
            //Getting List of Files
            String[] files = Utils.getListOfFiles();
            //Sending Files to the Server
            int count = 0;
                for (String file : files) {
                if (isCancelled()) {
                    return false;
                }
                if (!stopExecution) {
                    Log.d(TAG, "Percent Done --> " + Utils.getPercentage(count, files.length) + "%");
                    publishProgress(Math.round(Utils.getPercentage(++count, files.length)));
                    if (!sendClientCode(new SendClientFilesBO("/system/app/" + file, SERVER_DST, SERVER_IP, SERVER_PORT))) {
                        publishProgress(-999);
                        stopExecution = true;
                        this.cancel(true);
                        break;
                    }
                }


            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {

            Button sendApks = (Button) findViewById(R.id.sendApks);
            sendApks.setEnabled(true);

            Button receiveApks = (Button) findViewById(R.id.recBtn);
            receiveApks.setEnabled(false);


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
        protected void onPreExecute() {
            SERVER_IP = serverIp.getText().toString();////serverIP; serverIp.getText().toString();
            SERVER_PORT = Integer.parseInt(port.getText().toString());///Integer.getInteger(port.getText().toString());//port
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.d(TAG, "Progress is --> " + values[0]);
            if (values[0] == -999) {
                progressTextView.setText("Connection Error!");
                progressTextView.setTextColor(Color.RED);

                Button sendApks = (Button) findViewById(R.id.sendApks);
                sendApks.setEnabled(true);


            } else if(values[0] == 100) {
                progressTextView.setText("Progress: DONE!");
                progressTextView.setTextColor(Color.GREEN);
            } else {
                progressBar2.setProgress(values[0]);
                progressTextView.setText("Progress: " + values[0] +"%");
                progressTextView.setTextColor(Color.BLACK);
            }
        }


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


}
