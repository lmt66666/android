package com.example.myapplication.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;


import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import com.example.myapplication.adapter.SongAdapter;
import com.example.myapplication.bean.MyPlayer;
import com.example.myapplication.bean.Song;
import com.example.myapplication.service.MyService;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<Song> songs = new ArrayList<Song>();
    private ListView music_lv;
    private ImageView cp_img;
    private TextView song_tv;
    private TextView singer_tv;
    private ImageView more_img;
    private ImageView next_img;
    private ImageView play_img;
    //当前歌曲的序号
    private int current = 0;
    //当前状态,0x11没有播放,ox12正在播放,ox13播放暂停
    int status = 0x11;

    private MyPlayer myPlayer = new MyPlayer();
    //定义intentfilter（频率）
    public static final String CONTROL = "hbgyzy.edu.mymp3play.CONTROL";// 控制播放、暂停
    public static final String UPDATE = "hbgyzy.edu.mymp3play.UPDATE";// 更新界面显示
    public static final String PROGRESS = "iet.jxufe.cn.android.PROGRESS";// 更新精度条
    //定义广播接收器，接收service发来的广播包
    private  AcBroadCastReveiver acReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.music_layout);
        initView();
        getSong();
        //初始化播放器界面，显示list中第一首歌曲的信息
        setPlayer(current);
        //注册广播接收器
        regBroadcast();


        // filter.addAction();
    }

    private void regBroadcast() {
        //定义频率（意图过滤器）
        IntentFilter intentfilter = new IntentFilter(MainActivity.UPDATE);
        //实例化BroadcastReceiver
        acReceiver = new AcBroadCastReveiver();
        //注册BroadcastReceiver到service
        registerReceiver(acReceiver, intentfilter);
    }

    private void initView() {
        music_lv = (ListView) findViewById(R.id.music_lv);
//       添加单击事件单击某一首歌曲的时候，将歌曲的信息加载到播放控制界面上
        music_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setPlayer(position);
                //单击歌曲时启动service同时传递当前歌曲的文件路径（URI）
                Intent intent = new Intent(MainActivity.this, MyService.class);
                startService(intent);
                /*
                //如果当前播放器有歌曲在播放，要让播放器复位
                if (status != 0x11) {
                status = 0x11;
                setPlayer(position);
            }
            //Intent intent = new Intent(MainActivity.this, MediaPlayService.class);
            //startService(intent);*/
        }
        });
        cp_img = (ImageView) findViewById(R.id.cp_img);
        song_tv = (TextView) findViewById(R.id.song_tv);
        singer_tv = (TextView) findViewById(R.id.singer_tv);
        more_img = (ImageView) findViewById(R.id.more_img);
        next_img = (ImageView) findViewById(R.id.next_img);
        play_img = (ImageView) findViewById(R.id.play_img);
        play_img.setOnClickListener(this);
        next_img.setOnClickListener(this);
    }


    private void getSong() {
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        while (cursor.moveToNext()) {
            String titleStr, artistStr, filePathStr;
            long durationLon, sizeLong;
            int isMusicInt;
            //歌曲名称
            titleStr = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
            //歌曲演唱者
            artistStr = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
            //歌曲时长
            durationLon = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
            //歌曲文件路径
            filePathStr = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
            //歌曲文件的大小
            sizeLong = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
            //文件是否为音乐
            isMusicInt = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.IS_MUSIC));

            //Song(String songNameStr, String songerNameStr, String songTimeStr, String fileSizeStr,String filePathStr )
            //对歌曲进行筛选
            if ((isMusicInt != 0) && (sizeLong > 0)) {
                Song song = new Song(titleStr, artistStr, String.valueOf(durationLon), String.valueOf(sizeLong), filePathStr);
                songs.add(song);
            }
        }
        SongAdapter songAdapter = new SongAdapter(this, songs);
        music_lv.setAdapter(songAdapter);
    }


    /**
     * 把歌曲时间变为分秒的形式
     */
    private String ShowTime(long time) {
        time /= 1000;
        long minute = time / 60;
        long hour = minute / 60;
        long second = time % 60;
        minute %= 60;
        //二进制数字
        return String.format("%02d:%02d", minute, second);
    }

    /**
     * 功能描述：设置播放按钮的界面（歌曲歌手）
     * 参    数：
     * 返 回 值：
     **/
    private void setPlayer(int position) {
        //song_tv   singer_tv
        song_tv.setText(songs.get(position).getSongNameStr());
        singer_tv.setText(songs.get(position).getSingerNameStr());
        current = position;//保存当前要播放第几首歌曲
        if (status == 0x11) {
                myPlayer.stop();
            play_img.setImageDrawable(getResources().getDrawable(R.drawable.play));
        }

    }


    //当前状态,0x11没有播放,ox12正在播放,ox13播放暂停
    private void setPlayImg(int status) {
        if (status == 0x11) {
            play_img.setImageDrawable(getResources().getDrawable(R.drawable.pause));
        } else if (status == 0x12) {
            play_img.setImageDrawable(getResources().getDrawable(R.drawable.play));
        } else {
            play_img.setImageDrawable(getResources().getDrawable(R.drawable.pause));
        }

    }

    /**
     * 功能描述：单击按钮是发送广播
     * 参    数：
     * 返 回 值：
     **/
    public void onClick(View v) {
        //建立intent并设置“频率”
        Intent intent = new Intent(CONTROL);
        switch (v.getId()) {
            case R.id.play_img:
                //给intent添加值
                intent.putExtra("url", songs.get(current).getFilePathStr());
                intent.putExtra("status", status);
                sendBroadcast(intent);
                break;
            case R.id.next_img:
                if (current < (songs.size() - 2)) {
                    current++;
                    status = 0x11;
                    setPlayer(current);
                }
                break;
            case R.id.more_img:
                break;
        }
    }



    /**
     * 功能描述：用来实现音乐的播放功能
     * 参    数：
     * 返 回 值：
     **/
   // @Override

    /*
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_img:
                if (status == 0x11) {
                    myPlayer.pareAndPlay(songs.get(current).getFilePathStr());
                    setPlayImg(0x11);
                    status = 0x12;
                } else if (status == 0x12) {
                    myPlayer.pause();
                    setPlayImg(0x12);
                    status = 0x13;
                } else if (status == 0x13) {
                    myPlayer.play();
                    setPlayImg(0x13);
                    status = 0x12;
                }
                break;
            case R.id.next_img:
                if (current < (songs.size() - 2)) {
                    current++;
                    status=0x11;
                    setPlayer(current);
                }
                break;
            case R.id.more_img:


                break;
        }
    }*/

    public class AcBroadCastReveiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
//           //接收service送来的状态值
            int update= intent.getIntExtra("status", -1);
            if (update == 0x11) {
                setPlayImg(0x11);
                status = 0x11;
            } else if (update == 0x12) {
                setPlayImg(0x12);
                status = 0x12;
            } else if (update == 0x13) {
                setPlayImg(0x13);
                status = 0x13;
            }
        }
    }

    @Override
    protected void onDestroy() {
        myPlayer.closePlayer();
        super.onDestroy();
    }
}
