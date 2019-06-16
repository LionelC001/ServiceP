package com.lionel.servicep;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import static com.lionel.servicep.Constant.ACTION_BROADCAST_RECEIVER;
import static com.lionel.servicep.Constant.DATA_TYPE;
import static com.lionel.servicep.Constant.DATA_VALUE;
import static com.lionel.servicep.Constant.TYPE_BACKGROUND_DATA;
import static com.lionel.servicep.Constant.TYPE_FOREGROUND_DATA;

public class DummyService extends Service {

    private Thread dummyWebSocketThread;
    private boolean isDummyWebSocketRun = true;

    @Override
    public void onCreate() {
        super.onCreate();
        initDummyWebSocket();
    }

    private void initDummyWebSocket() {
        dummyWebSocketThread = new Thread(new Runnable() {
            @Override
            public void run() {

                while (isDummyWebSocketRun) {
                    int dataNum = countRandomNum();
                    broadcastData(TYPE_BACKGROUND_DATA, dataNum);
                    if (dataNum > 5) {
                        broadcastData(TYPE_FOREGROUND_DATA, dataNum);
                    }

                    Log.d("<>", "" + dataNum);

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        dummyWebSocketThread.start();
    }

    private int countRandomNum() {
        return (int) (Math.random() * 10);
    }

    private void broadcastData(int type, int data) {
        Intent intent = new Intent(ACTION_BROADCAST_RECEIVER);
        intent.putExtra(DATA_TYPE, type);
        intent.putExtra(DATA_VALUE, data);
        sendBroadcast(intent);
    }

    public class DummyBinder extends Binder {
        public DummyService getDummyService() {
            return DummyService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;    // if return null, will not call ServiceConnection
    }

    @Override
    public boolean onUnbind(Intent intent) {
        isDummyWebSocketRun = false;
        Log.d("<>", "Unbind");
        Log.d("<>", "is dummyWebSocketThread live? " + (dummyWebSocketThread != null));
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
