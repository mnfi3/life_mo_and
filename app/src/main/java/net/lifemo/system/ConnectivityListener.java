package net.lifemo.system;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.view.View;

public class ConnectivityListener extends BroadcastReceiver{

  private Snackbar snackbar;
  private View view;
  public ConnectivityListener(View view, Snackbar snackbar) {
    this.view = view;
    this.snackbar = snackbar;
  }
  @Override
  public void onReceive(Context context, Intent intent) {
    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
    boolean isConnected = networkInfo!=null && networkInfo.isConnected();

    if(isConnected){
      if(snackbar != null) {
        snackbar.dismiss();
      }
    }else{
      snackbar = Snackbar.make(view, "Connection Failed . Check Your Network", Snackbar.LENGTH_INDEFINITE);
      snackbar.show();
    }
  }
  
}
