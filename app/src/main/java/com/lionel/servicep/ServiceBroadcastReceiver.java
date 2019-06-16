package com.lionel.servicep;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static com.lionel.servicep.Constant.DATA_TYPE;
import static com.lionel.servicep.Constant.DATA_VALUE;

public class ServiceBroadcastReceiver extends BroadcastReceiver {

    private IBroadcastDataListener broadcastDataListener;


    public interface IBroadcastDataListener {
        void onBroadcastDataChanged(int type, int data);
    }

    public void setListener(IBroadcastDataListener broadcastDataListener) {
        this.broadcastDataListener = broadcastDataListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (broadcastDataListener != null) {
            int type = intent.getIntExtra(DATA_TYPE, -1);
            int value = intent.getIntExtra(DATA_VALUE, -2);
            broadcastDataListener.onBroadcastDataChanged(type, value);
        }
    }
}
