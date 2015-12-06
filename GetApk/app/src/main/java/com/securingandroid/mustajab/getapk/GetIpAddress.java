package com.securingandroid.mustajab.getapk;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * Created by mustajab on 11/28/15.
 */
public class GetIpAddress {


    private static final String TAG = "MUSTAJAB";

    public static void GetIp() throws IOException {
        InetAddress ip;
        try {

            ip = InetAddress.getLocalHost();
            Log.d(TAG, "Current IP address : " + ip.getHostAddress());


            NetworkInterface network = NetworkInterface.getByInetAddress(ip);

            byte[] mac = network.getHardwareAddress();

            Log.d(TAG, "Current MAC address : ");

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            Log.d(TAG, sb.toString());

        } catch (UnknownHostException e) {
            Log.d(TAG, "Exception"+e);
        } catch (SocketException e){
            Log.d(TAG, "Exception" + e);
        }


        /*********** Getting IP Address **********/
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(
                whatismyip.openStream()));

        String ip2 = in.readLine();
        Log.d(TAG, "Public IP address: "+ip2);
    }
}
