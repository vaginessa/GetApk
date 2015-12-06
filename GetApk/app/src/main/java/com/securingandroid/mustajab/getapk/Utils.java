package com.securingandroid.mustajab.getapk;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

/**
 * Created by mustajab on 11/28/15.
 */

    import java.io.*;
    import java.net.*;
    import java.util.*;
//import org.apache.http.conn.util.InetAddressUtils;

    public class Utils{
        private static final String TAG = "MUSTAJAB";
        /**
         * Convert byte array to hex string
         * @param bytes
         * @return
         */
        public static String bytesToHex(byte[] bytes) {
            StringBuilder sbuf = new StringBuilder();
            for(int idx=0; idx < bytes.length; idx++) {
                int intVal = bytes[idx] & 0xff;
                if (intVal < 0x10) sbuf.append("0");
                sbuf.append(Integer.toHexString(intVal).toUpperCase());
            }
            return sbuf.toString();
        }

        /**
         * Get utf8 byte array.
         * @param str
         * @return  array of NULL if error was found
         */
        public static byte[] getUTF8Bytes(String str) {
            try { return str.getBytes("UTF-8"); } catch (Exception ex) { return null; }
        }

        /**
         * Load UTF8withBOM or any ansi text file.
         * @param filename
         * @return
         * @throws java.io.IOException
         */
        public static String loadFileAsString(String filename) throws java.io.IOException {
            final int BUFLEN=1024;
            BufferedInputStream is = new BufferedInputStream(new FileInputStream(filename), BUFLEN);
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFLEN);
                byte[] bytes = new byte[BUFLEN];
                boolean isUTF8=false;
                int read,count=0;
                while((read=is.read(bytes)) != -1) {
                    if (count==0 && bytes[0]==(byte)0xEF && bytes[1]==(byte)0xBB && bytes[2]==(byte)0xBF ) {
                        isUTF8=true;
                        baos.write(bytes, 3, read-3); // drop UTF8 bom marker
                    } else {
                        baos.write(bytes, 0, read);
                    }
                    count+=read;
                }
                return isUTF8 ? new String(baos.toByteArray(), "UTF-8") : new String(baos.toByteArray());
            } finally {
                try{ is.close(); } catch(Exception ex){}
            }
        }

        /**
         * Returns MAC address of the given interface name.
         * @param interfaceName eth0, wlan0 or NULL=use first interface
         * @return  mac address or empty string
         */
        public static String getMACAddress(String interfaceName) {
            try {
                List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
                for (NetworkInterface intf : interfaces) {
                    if (interfaceName != null) {
                        if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
                    }
                    byte[] mac = intf.getHardwareAddress();
                    if (mac==null) return "";
                    StringBuilder buf = new StringBuilder();
                    for (int idx=0; idx<mac.length; idx++)
                        buf.append(String.format("%02X:", mac[idx]));
                    if (buf.length()>0) buf.deleteCharAt(buf.length()-1);
                    return buf.toString();
                }
            } catch (Exception ex) { } // for now eat exceptions
            return "";
        /*try {
            // this is so Linux hack
            return loadFileAsString("/sys/class/net/" +interfaceName + "/address").toUpperCase().trim();
        } catch (IOException ex) {
            return null;
        }*/
        }

        public static String getExternalIpOfAndroid() {
            Scanner s = null;
            String address = "nothing";
            try {
                URL url = new URL("http://checkip.amazonaws.com");
                URLConnection connection = url.openConnection();
                connection.addRequestProperty("Protocol", "Http/1.1");
                connection.addRequestProperty("Connection", "keep-alive");
                connection.addRequestProperty("Keep-Alive", "1000");
                connection.addRequestProperty("User-Agent", "Web-Agent");

                s = new Scanner(connection.getInputStream());
                address =  InetAddress.getByName(s.nextLine()).getHostAddress();
                Log.d(TAG, address);

            } catch(Exception ex) {
                Log.d(TAG,"Exception here"+ex);
            }
            finally {
                s.close();
            }
            return address;
        }


        /**
         * Get IP address from first non-localhost interface
         * @param ipv4  true=return ipv4, false=return ipv6
         * @return  address or empty string
         */
        public static String getIPAddress(boolean useIPv4) {
            try {
                List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
                for (NetworkInterface intf : interfaces) {
                    List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                    for (InetAddress addr : addrs) {
                        if (!addr.isLoopbackAddress()) {
                            String sAddr = addr.getHostAddress();
                            //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                            boolean isIPv4 = sAddr.indexOf(':')<0;

                            if (useIPv4) {
                                if (isIPv4)
                                    return sAddr;
                            } else {
                                if (!isIPv4) {
                                    int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                    return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                                }
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                Log.d(TAG,"Exception:"+ex);
            } // for now eat exceptions
            return "";
        }


        public static InetAddress getExternalIp() throws IOException {
            URL url = new URL("http://checkip.amazonaws.com");
            URLConnection connection = url.openConnection();
            connection.addRequestProperty("Protocol", "Http/1.1");
            connection.addRequestProperty("Connection", "keep-alive");
            connection.addRequestProperty("Keep-Alive", "1000");
            connection.addRequestProperty("User-Agent", "Web-Agent");

            Scanner s = new Scanner(connection.getInputStream());
            try {
                return InetAddress.getByName(s.nextLine());
            } finally {
                s.close();
            }
        }

        public static Boolean[] getReadWriteAccessAppsInstalledFolder() {
            File appsDir = null;
            Boolean[] readWrite = new Boolean[2];
            try {
                Log.d(TAG, "Trying to access the Directory");
                appsDir = new File("/data/app");//Installed Applications
            } catch (Exception ex) {
                Log.d(TAG, "Unable to find the Directory");
            }

            Log.d(TAG, "Can read directory --> " + appsDir.canRead());
            Log.d(TAG, "Can write directory --> " + appsDir.canWrite());
            Log.d(TAG, "Is Directory --> " + appsDir.isDirectory());

            readWrite[0] = appsDir.canRead();
            readWrite[1] = appsDir.canWrite();

            return readWrite;
        }

        public static String[] getListOfFiles() {
            File appsDir = null;
            try {
                Log.d(TAG, "Trying to access the Directory");
                //appsDir = new File("/data/app");//Installed Applications
                appsDir = new File("/system/app");// Pre-installed Applications
            } catch (Exception ex) {
                Log.d(TAG, "Unable to find the Directory");
            }
            Log.d(TAG, "Can read directory --> " + appsDir.canRead());
            Log.d(TAG, "Can write directory --> " + appsDir.canWrite());
            Log.d(TAG, "Is Directory --> " + appsDir.isDirectory());

            String[] files = appsDir.list();

            Log.d(TAG, "list of files " + files);

            if (files != null) {
                Log.d(TAG, "list of files " + files.length);

                for (int i = 0 ; i < files.length ; i++ ) {
                    Log.d(TAG, "File: " + files[i]);
                }
            }
            return files;
        }


        public static float getPercentage(int n, int total) {
            float percent = (n * 100.0f) / total;
            return percent;
        }






        /**
         *
         public static String getPublicIp() throws Exception{
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
            String ip2 = in.readLine(); //you get the IP as a String
            return ip2;
        } **/

    }

