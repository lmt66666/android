package com.example.myapplication.bean;
public class Song {
    private  String songNameStr;
    private String singerNameStr;
    private String songTimeStr;
    private String filePathStr;
    private String  fileSizeStr;



    public Song(String songNameStr, String songerNameStr, String songTimeStr, String fileSizeStr,String filePathStr ) {
        this.songNameStr = songNameStr;
        this.singerNameStr = songerNameStr;
        this.songTimeStr = songTimeStr;
        this.filePathStr = filePathStr;
        this.fileSizeStr=fileSizeStr;
    }

    public String getSongNameStr() {
        return songNameStr;
    }

    public void setSongNameStr(String songNameStr) {
        this.songNameStr = songNameStr;
    }

    public String getSingerNameStr() {
        return singerNameStr;
    }

    public void setSingerNameStr(String singerNameStr) {
        this.singerNameStr = singerNameStr;
    }

    public String getSongTimeStr() {
        return songTimeStr;
    }

    public void setSongTimeStr(String songTimeStr) {
        this.songTimeStr = songTimeStr;
    }

    public String getFilePathStr() {
        return filePathStr;
    }

    public void setFilePathStr(String filePathStr) {
        this.filePathStr = filePathStr;
    }

    public String getFileSizeStr() {
        return fileSizeStr;
    }

    public void setFileSizeStr(String fileSizeStr) {
        this.fileSizeStr = fileSizeStr;
    }
}
