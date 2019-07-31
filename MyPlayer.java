package com.example.myapplication.bean;

import android.media.MediaPlayer;

import java.io.IOException;

/**
 * 定义播放器类实现播放功能
 */

public class MyPlayer {
    private MediaPlayer mediaPlayer = new MediaPlayer();

    //当前状态,0x11没有播放,ox12正在播放,ox13播放暂停
    int status = 0x11;
    //当前第几首歌曲
    int current = 0;

    /**
     * 功能描述：首次播放歌曲时执行
     * 参    数：String filePathStr-歌曲的路径
     * 返 回 值：
     **/
    public void pareAndPlay(String filePathStr) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(filePathStr);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 功能描述：暂停后再开始播放歌曲
     * 参    数：
     * 返 回 值：
     **/
    public void play() {
        mediaPlayer.start();
    }
    /**

     * 功能描述：暂停播放
     * 参    数：
     * 返 回 值：
     **/
    public void pause() {
        mediaPlayer.pause();
    }

    /**
     * 功能描述：播放下一首歌曲
     * 参    数：
     * 返 回 值：
     **/
    public void nextMsuci(String filePathStr) {
        mediaPlayer.stop();
        pareAndPlay(filePathStr);
    }
    /**
     * 作    者：zhangming
     * 创建时间：2017/8/14
     * 功能描述：停止播放歌曲
     * 参    数：
     * 返 回 值：
     **/
    public void stop() {
        mediaPlayer.stop();
    }
    /**
     * 功能描述：连续播放歌曲
     * 参    数：
     * 返 回 值：
     **/
    public void lianMusic() {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //当上一首歌曲播放完毕，继续播放下一首，发送广播给actiity更新UI界面
            }
        });
    }
    /**
     * 功能描述：当actiivty或者service销毁时需要释放播放器所占用的资源
     * 参    数：
     * 返 回 值：
     **/
    public void closePlayer() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
