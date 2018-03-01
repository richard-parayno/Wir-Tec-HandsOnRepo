package com.example.rparayno.handsonrepository.RestaurantFiles;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.rparayno.handsonrepository.R;

import java.util.List;

/**
 * Created by rparayno on 07/02/2018.
 */


public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.MyViewHolder> {

    private List<Restaurant> restaurantList;
    private MyAdapterListener listener;


    public RestaurantAdapter(List<Restaurant> restaurantList, MyAdapterListener listener) {
        this.restaurantList = restaurantList;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, description, weight;
        public Button editBtn;


        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.tv_name);
            description = (TextView) view.findViewById(R.id.tv_description);
            weight = (TextView) view.findViewById(R.id.tv_weight);
            editBtn = (Button) view.findViewById(R.id.editBtn);

            editBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    listener.editButtonOnClick(v, getAdapterPosition());
                }
            });
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.resto_item_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Restaurant restaurant = restaurantList.get(position);
        holder.name.setText(restaurant.getName());
        holder.description.setText(restaurant.getDescription());
        holder.weight.setText(restaurant.getWeight());
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }
}