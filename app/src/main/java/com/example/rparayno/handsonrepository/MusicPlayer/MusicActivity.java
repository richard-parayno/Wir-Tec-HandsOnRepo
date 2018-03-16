package com.example.rparayno.handsonrepository.MusicPlayer;

import android.Manifest;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.rparayno.handsonrepository.MusicPlayer.MusicService.MusicBinder;

import com.example.rparayno.handsonrepository.R;

import java.util.ArrayList;

public class MusicActivity extends AppCompatActivity implements MusicController.MediaPlayerControl{
    private ArrayList<Music> musicList;
    private RecyclerView recyclerView;
    private MusicAdapter mAdapter;
    private final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 1;
    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound=false;
    private ServiceConnection musicConnection;
    private PlaySongListener songListener;

    private MusicController musicController;

    private boolean paused = false, playbackPaused = false;

    private LinearLayout musicControlLayout;

    private TextView title;
    private TextView artist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        setupMusicService();
        initResources();

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                showExplanation("Need Permission", "We need access to your storage so I can play your music!");

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_READ_EXTERNAL_STORAGE);

            }

        } else {
            // Permission has already been granted
        }
        loadSongsFromStorage();
    }

    //connect to the service
    private void setupMusicService(){
        this.musicConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                MusicBinder binder = (MusicBinder)service;
                //get service
                musicSrv = binder.getService();
                //pass list
                musicSrv.setList(musicList, songListener);
                musicBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                musicBound = false;
            }
        };
    }


    @Override
    protected void onDestroy() {
        this.stopService(playIntent);
        this.unbindService(this.musicConnection);

        if (this.musicController != null) {
            this.musicController.hide();
            this.musicSrv = null;
            this.musicController = null;
            Log.d("TAG", "Music service stopped");
        }
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //menu item selected

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CODE_READ_EXTERNAL_STORAGE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //loadSongsFromStorage();
                    //initResources();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    this.finish();
                }
                return;
        }
    }

    private void showExplanation(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(MusicActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_CODE_READ_EXTERNAL_STORAGE);
                    }
                });
        builder.create().show();
    }

    private void loadSongsFromStorage() {
        ContentResolver musicResolver = this.getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            int titleColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.ARTIST);

            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                musicList.add(new Music(thisArtist, thisTitle, thisId));
                //mAdapter.notifyDataSetChanged();
            } while (musicCursor.moveToNext());
        }
    }

    private void initResources() {
        this.title = (TextView) findViewById(R.id.tv_song);
        this.artist = (TextView) findViewById(R.id.tv_artist);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        this.musicList = new ArrayList<>();
        this.songListener = new PlaySongListener() {

            @Override
            public void onPlayRequest(int songIndex) {
                if(musicSrv != null) {
                    Log.d("TAG", "onPlayRequest");
                    musicSrv.setSong(songIndex);
                    musicSrv.playSong();
                    if (playbackPaused) {
                        setMusicController();
                        playbackPaused = false;
                    }
                } else {
                    Log.e("TAG", "Music service is not properly setup");
                }
            }

            @Override
            public void onPlayUpdate(int songIndex) {
                Music song = musicList.get(songIndex);
                title.setText(song.getSong());
                artist.setText(song.getArtist());
            }
        };

        this.mAdapter = new MusicAdapter(this.musicList, this.songListener);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        musicControlLayout = (LinearLayout) findViewById(R.id.media_control_layout);

        ViewTreeObserver vto = musicControlLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setMusicController();
            }
        });
    }

    private void setMusicController() {
        if (this.musicController != null) {
            this.musicController.hide();
            this.musicController = null;
        }

        musicController = new MusicController(this);
        musicController.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }
        });

        musicController.setMediaPlayer(this);
        musicController.setAnchorView(findViewById(R.id.media_control_layout));
        musicController.setEnabled(true);
        musicController.show(0);
    }

    @Override
    public void start() {
        if (this.musicSrv != null)
            musicSrv.go();
    }

    @Override
    public void pause() {
        if (this.musicSrv != null) {
            playbackPaused = true;
            musicSrv.pausePlayer();
        }
    }

    @Override
    public int getDuration() {
        if (this.musicSrv != null && this.musicBound && this.musicSrv.isPng())
            return this.musicSrv.getDur();
        else
            return 0;
    }

    @Override
    public int getCurrentPosition() {
        if (this.musicSrv != null && this.musicBound && this.musicSrv.isPng())
            return this.musicSrv.getPosn();
        else
            return 0;
    }

    @Override
    public void seekTo(int pos) {
        if (musicSrv != null)
            musicSrv.seek(pos);

    }

    @Override
    public boolean isPlaying() {
        if (this.musicSrv != null && this.musicBound)
            return this.musicSrv.isPng();
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    private void playNext() {
        musicSrv.playNext();
        if (playbackPaused) {
            setMusicController();
            playbackPaused = false;
        }
        musicController.show(0);
    }

    private void playPrev() {
        musicSrv.playPrev();
        if (playbackPaused) {
            setMusicController();
            playbackPaused = false;
        }
        musicController.show(0);
    }

    @Override
    protected void onPause() {
        this.paused = true;

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (this.paused) {
            setMusicController();
            paused = false;
        }
    }

    @Override
    protected void onStop() {
        musicController.hide();

        super.onStop();
    }


}
