package com.example.myapplication.adapter;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.bean.Song;

import java.util.ArrayList;

/**
 * Created by zh on 2017/3/27.
 */

public class SongAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater myLi;
    private ArrayList<Song> songs = new ArrayList<Song>();

    public SongAdapter(Context context, ArrayList<Song> songs) {
        this.context = context;
        this.songs = songs;
        this.myLi = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int position) {
        return songs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Song item = songs.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = myLi.inflate(R.layout.item_song, null);
            holder = new ViewHolder();
            holder.songNameTv = (TextView) convertView.findViewById(R.id.song_name_tv);
            holder.singerNameTv = (TextView) convertView.findViewById(R.id.singer_name_tv);
            holder.songTimeTv = (TextView) convertView.findViewById(R.id.time_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.songNameTv.setText(item.getSongNameStr());
        holder.singerNameTv.setText(item.getSingerNameStr());
        holder.songTimeTv.setText(item.getSongTimeStr());
        return convertView;
    }

    public static class ViewHolder {
        public TextView songNameTv, singerNameTv, songTimeTv;
    }
    /**
     * 把歌曲时间变为分秒的形式
     */
    private String ShowTime(int time) {
        time /= 1000;
        int minute = time / 60;
        int hour = minute / 60;
        int second = time % 60;
        minute %= 60;
        //二进制数字
        return String.format("%02d:%02d", minute, second);
    }
}
