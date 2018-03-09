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
import android.widget.TextView;

import com.example.rparayno.handsonrepository.MusicPlayer.MusicService.MusicBinder;

import com.example.rparayno.handsonrepository.R;

import java.util.ArrayList;

public class MusicActivity extends AppCompatActivity {
    private ArrayList<Music> musicList;
    private RecyclerView recyclerView;
    private MusicAdapter mAdapter;
    private final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 1;
    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound=false;
    private ServiceConnection musicConnection;
    private PlaySongListener songListener;

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
        stopService(playIntent);
        unbindService(musicConnection);

        this.musicSrv=null;
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //menu item selected
        switch (item.getItemId()) {
            case R.id.btn_end:
                stopService(playIntent);
                musicSrv=null;
                System.exit(0);
                break;
        }
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




    }
}
