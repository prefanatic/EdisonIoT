package com.prefanatic.edisoniot;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by codygoldberg on 12/26/16.
 */

public class MainActivity extends FragmentActivity {

    private SocketServer server;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startServer();
    }

    private void startServer() {
        InetAddress address = getInetAddress();
        if (address == null) {
            return;
        }

        Log.d("Edison", "startServer: Host is " + address.getHostAddress());

        server = new SocketServer(new InetSocketAddress(
                address.getHostAddress(), 12345
        ));
        server.start();
    }

    private InetAddress getInetAddress() {
        try {
            for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface networkInterface = (NetworkInterface) en.nextElement();

                for (Enumeration enumIpAddr = networkInterface.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();

                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
            //Log.e(TAG, "Error getting the network interface information");
        }

        return null;
    }


}
