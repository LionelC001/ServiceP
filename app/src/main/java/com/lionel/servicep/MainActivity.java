package com.lionel.servicep;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lionel.servicep.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

import static com.lionel.servicep.Constant.ACTION_BROADCAST_RECEIVER;
import static com.lionel.servicep.Constant.TYPE_BACKGROUND_DATA;
import static com.lionel.servicep.Constant.TYPE_FOREGROUND_DATA;

public class MainActivity extends AppCompatActivity implements ServiceBroadcastReceiver.IBroadcastDataListener {

    private ServiceConnection dummyServiceConnection;
    private ServiceBroadcastReceiver broadcastReceiver;
    private DummyService dummyService;
    private ActivityMainBinding dataBinding;
    private List<Integer> currentData = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initReceiver();
    }

    private void initReceiver() {
        IntentFilter intentFilter = new IntentFilter(ACTION_BROADCAST_RECEIVER);
        broadcastReceiver = new ServiceBroadcastReceiver();
        registerReceiver(broadcastReceiver, intentFilter);
        broadcastReceiver.setListener(this);
    }

    @Override
    public void onBroadcastDataChanged(int type, int data) {
        if (type == TYPE_BACKGROUND_DATA) {
            dataBinding.setBackgroundStateData(data);
        }

        if (type == TYPE_FOREGROUND_DATA) {
            currentData.add(data);
            dataBinding.setForegroundStateData(currentData);
        }
    }


    @Override
    protected void onDestroy() {
        unbindService(dummyServiceConnection);
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    public void onBindServiceBtnCLicked(View view) {
        initService();
    }

    private void initService() {
        dummyServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                DummyService.DummyBinder binder = (DummyService.DummyBinder) service;
                dummyService = binder.getDummyService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        };

        Intent intent = new Intent(MainActivity.this, DummyService.class);
        bindService(intent, dummyServiceConnection, BIND_AUTO_CREATE);
    }

    public void onUnbindServiceBtnCLicked(View view) {
        if (isMyServiceRunning(DummyService.class)) {
            currentData.clear();
            unbindService(dummyServiceConnection);
        }
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
