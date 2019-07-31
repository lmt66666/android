package com.example.myapplication.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.myapplication.R;
import com.example.myapplication.activity.MainActivity;
import com.example.myapplication.bean.MyPlayer;

import static com.example.myapplication.activity.MainActivity.UPDATE;

/**
 * 创建类MyService继承service，实现后台播放功能.
 */

public class MyService extends Service {
    private MyPlayer myPlayer = new MyPlayer();

    //定义广播发接收器
    private SerBroadcastReceiver mySerReceivrt;
    private String TAG="MyService";

    @Nullable

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    /**
     * 功能描述：在android系统出现/Nofication提醒图标
     * 参    数：
     * 返 回 值：
     **/
    @Override
    public void onCreate() {
        super.onCreate();
        Notification.Builder builder=new Notification.Builder(this);
        builder.setContentText("title").setContentText("zhengzai bofang ").setSmallIcon(R.mipmap.ic_launcher);
        Intent notifyIntent = new Intent(this, MyService.class);

        PendingIntent notifyPendingIntent = PendingIntent.getActivity(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(notifyPendingIntent);

        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //manager.notify(1, notification);

        startForeground(1, notification);

        Log.d(TAG, "onCreate() executed");
    }
    /**
     * 功能描述：service销毁时释放mediaPlayer资源
     * 参    数：
     * 返 回 值：
     **/
    @Override
    public void onDestroy() {
        myPlayer.closePlayer();
        super.onDestroy();
    }

    /**
     * 功能描述：设置intentfilter（频率），注册broadcastReceiver
     * 参    数：
     * 返 回 值：
     **/
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //定义频率（意图过滤器）
        IntentFilter intentfilter = new IntentFilter(MainActivity.CONTROL);
        //实例化BroadcastReceiver
        mySerReceivrt = new SerBroadcastReceiver();
        //注册BroadcastReceiver到service
        registerReceiver(mySerReceivrt, intentfilter);

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 功能描述：新建一个SerBroadcastReceiver类实现对广播的接收
     * onReceiver方法用来处理接收到的广播中的信息
     * 参    数：
     * 返 回 值：
     **/
    public class SerBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int status = 0x11;
            int control = intent.getIntExtra("status", -1);
            if (control == 0x11) {
                String url = intent.getStringExtra("url");
                myPlayer.pareAndPlay(url);
                status = 0x12;
            } else if (control == 0x12) {
                myPlayer.pause();
                status = 0x13;
            } else if (control == 0x13) {
                myPlayer.play();
                status = 0x12;
            }
            //将status的状态值返还给Activity，由Activity根据值更新UI
            Intent acintnet = new Intent(UPDATE);
            acintnet.putExtra("status", status);
            sendBroadcast(acintnet);

        }
    }

}

