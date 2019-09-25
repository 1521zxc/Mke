package com.example.epay.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


/**
 * 添加银行卡
 */
public class ClickReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent2=intent.getParcelableExtra("realIntent");
        context.startActivity(intent2);
        Intent intent1= intent.getParcelableExtra("msg");
        if(intent1!=null) {

        }
    }
}
