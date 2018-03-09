package com.example.rparayno.handsonrepository.MusicPlayer;

/**
 * Created by rparayno on 07/03/2018.
 */

public interface PlaySongListener {

    void onPlayRequest(int songIndex);
    void onPlayUpdate(int songIndex);
}
