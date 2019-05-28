package com.example.shou_hi.client_testmsger;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private Messenger mMessenger = null;
    private Messenger mServiceMessenger = null;
    private Intent serviceIntent;
    private static final int MSG_WHAT = 1;
    private static final int REQUEST_KEYEVENT = 1;
    private static final int MSG_TOCLIENT = 2;
    private static final int REQUEST_KEYEVENT_TOCLIENT = 2;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_TOCLIENT:
                    Log.e("Client MSG","got Message from server");
                    Log.e("Client MSG",msg.toString());
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serviceIntent = new Intent();
        serviceIntent.setComponent(new ComponentName("com.example.shou_hi.server_testmsger","com.example.shou_hi.server_testmsger.serverService"));
        bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);
        mMessenger = new Messenger(mHandler);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("MSG","MainActivity Start");
            Log.d("Client MSG", "onServiceConnected()");
            if (service == null) {
                mServiceMessenger = null;
            } else {
                mServiceMessenger = new Messenger(service);
                Message message = new Message();
                message.what = MSG_WHAT;
                message.arg1 = REQUEST_KEYEVENT;
                message.obj = mMessenger;
                try {
                    Log.d("Client MSG", "Message send()");
                    mServiceMessenger.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("Client MSG", "onServiceDisconnected()");
            mServiceMessenger = null;
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
    }
}
