package com.farjami.mohsen.pinterest.system;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class CustomBroadCastReceiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {
    Log.d("broadcast", "onReceive: "+"context = "+context+"  intent = "+intent);

  }
}
