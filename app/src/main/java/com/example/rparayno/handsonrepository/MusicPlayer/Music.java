package com.example.rparayno.handsonrepository.MusicPlayer;

/**
 * Created by rparayno on 28/02/2018.
 */

public class Music {

    private String artist;
    private String song;
    private long id;

    public Music(String artist, String song, long id) {
        this.artist = artist;
        this.song = song;
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public long getId() {
        return id;
    }
}
