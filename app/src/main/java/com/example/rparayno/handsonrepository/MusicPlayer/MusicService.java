package com.example.rparayno.handsonrepository.MusicPlayer;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import java.util.ArrayList;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener{
    //media player
    private MediaPlayer player;
    //song list
    private ArrayList<Music> songs;
    //current position
    private int songPosition;
    private MusicBinder musicBind = new MusicBinder();

    private PlaySongListener listener;


    public MusicService() {

    }

    public void onCreate() {
        super.onCreate();
        this.songPosition = 0;
        this.player = new MediaPlayer();

        initMusicPlayer();
    }

    public void initMusicPlayer() {
        //set player properties
        this.player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        this.player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        this.player.setOnPreparedListener(this);
        this.player.setOnCompletionListener(this);
        this.player.setOnErrorListener(this);
    }

    public void setList(ArrayList<Music> songs, PlaySongListener listener){
        this.songs = songs;
        this.listener = listener;
    }

    // TODO: Transfer this to a new class
    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    public void playSong() {
        this.player.reset();
        //get song
        Music playSong = songs.get(songPosition);
        //get id
        long currentSong = playSong.getId();
        //set uri
        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currentSong);

        try{
            this.player.setDataSource(getApplicationContext(), trackUri);
            this.player.prepareAsync();
            this.listener.onPlayUpdate(this.songPosition);
        }
        catch(Exception e){
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }

    }

    public void setSong(int songIndex) {
        this.songPosition = songIndex;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return this.musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent){
        this.player.stop();
        this.player.release();
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    public int getPosn() {
        return player.getCurrentPosition();
    }

    public int getDur() {
        return player.getDuration();
    }

    public boolean isPng() {
        return player.isPlaying();
    }

    public void pausePlayer() {
        player.pause();
    }

    public void seek(int posn) {
        player.seekTo(posn);
    }

    public void go() {
        player.start();
    }

    public void playPrev() {
        Log.d("TAG", "playPrev: ");
        songPosition--;
        if (songPosition < 0)
            songPosition = songs.size()-1;
        playSong();
    }

    public void playNext() {
        Log.d("TAG", "playNext: ");
        songPosition++;
        if (songPosition >= songs.size())
            songPosition = 0;
        playSong();
    }

    public ArrayList<Music> getSongs() {
        return songs;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }
}
