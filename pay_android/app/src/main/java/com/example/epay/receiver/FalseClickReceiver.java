package com.example.epay.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class FalseClickReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
       Intent intent1= intent.getParcelableExtra("msg");
        if(intent1!=null) {

        }

    }
}
