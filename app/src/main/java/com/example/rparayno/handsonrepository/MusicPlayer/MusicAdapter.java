package com.example.rparayno.handsonrepository.MusicPlayer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.rparayno.handsonrepository.R;
import com.example.rparayno.handsonrepository.RestaurantFiles.Restaurant;

import java.util.List;

/**
 * Created by rparayno on 28/02/2018.
 */

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder>{

    private List<Music> musicList;
    private PlaySongListener listener;

    public MusicAdapter(List<Music> musicList, PlaySongListener listener) {
        this.musicList = musicList;
        this.listener = listener;
    }

    public class MusicViewHolder extends RecyclerView.ViewHolder{
        public TextView song, artist;


        public MusicViewHolder(View view) {
            super(view);
            song = (TextView) view.findViewById(R.id.tv_song);
            artist = (TextView) view.findViewById(R.id.tv_artist);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onPlayRequest(getAdapterPosition());
                    listener.onPlayUpdate(getAdapterPosition());
                }
            });

        }
    }

    @Override
    public MusicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.music_item_layout, parent, false);

        return new MusicViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MusicViewHolder holder, int position) {
        Music music = musicList.get(position);
        holder.artist.setText(music.getArtist());
        holder.song.setText(music.getSong());
    }


    @Override
    public int getItemCount() {
        return musicList.size();
    }
}
