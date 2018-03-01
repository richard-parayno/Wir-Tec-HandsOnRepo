package com.example.rparayno.handsonrepository.MusicPlayer;

/**
 * Created by rparayno on 28/02/2018.
 */

public class Music {

    private String artist;
    private String song;

    public Music(String artist, String song) {
        this.artist = artist;
        this.song = song;
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
}
