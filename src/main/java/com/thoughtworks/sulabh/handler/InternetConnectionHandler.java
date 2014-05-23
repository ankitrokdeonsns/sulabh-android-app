package com.thoughtworks.sulabh.handler;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public abstract class InternetConnectionHandler {

    private final Context context;

    public InternetConnectionHandler(Context context) {
        this.context = context;
    }

    public abstract void executeIfConnectedToInternet();

    public abstract void executeIfNotConnectedToInternet();

    public void checkForConnectivity() {
        if(hasConnection()) {
          executeIfConnectedToInternet();
        } else {
          executeIfNotConnectedToInternet();
        };
    }

    private boolean hasConnection() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }

        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            return true;
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        }

        return false;
    }

}
