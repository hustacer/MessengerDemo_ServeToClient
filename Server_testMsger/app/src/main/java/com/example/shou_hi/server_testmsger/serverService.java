package com.example.shou_hi.server_testmsger;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class serverService extends Service {
    private static final int MSG_WHAT = 1;
    private static final int REQUEST_KEYEVENT = 1;
    private static final int MSG_TOCLIENT = 2;
    private static final int REQUEST_KEYEVENT_TOCLIENT = 2;
    private Messenger mMessenger_from_client = null;

    public serverService() {
    }

    private Messenger mMessager = new Messenger(new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d("Server MSG", "handleMessage()");
            super.handleMessage(msg);

            mMessenger_from_client = (Messenger)msg.obj;
            Message message = msg.obtain(msg);
            Log.d("Server MSG", message.toString());
            switch (message.what) {
                case MSG_WHAT:
                    Message msgToClient = new Message();
                    msgToClient.what=MSG_TOCLIENT;
                    msgToClient.arg1=REQUEST_KEYEVENT_TOCLIENT;
                    try {
                        Log.d("Server MSG", "sending message....");
                        mMessenger_from_client.send(msgToClient);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }

    });

    @Override
    public IBinder onBind(Intent intent) {
        return mMessager.getBinder();
    }
}
