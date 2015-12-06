package com.securingandroid.mustajab.getapk;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mustajab on 11/28/15.
 */
public class AndroidAppsInfo extends Activity{
    private static final String TAG = "MUSTAJAB";


    public void getNumberOfInstalledApps() {
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
    }
    public void getInstalledAndroidApps() {

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
    }


}
