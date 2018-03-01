package com.example.rparayno.handsonrepository.RestaurantFiles;

import android.content.Intent;
import android.graphics.Movie;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.rparayno.handsonrepository.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RestaurantRandomizer extends AppCompatActivity {
    private List<Restaurant> restaurantList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RestaurantAdapter rAdapter;

    private final int
            ADD_REQUEST_CODE = 1,
            EDIT_REQUEST_CODE = 2;

    public static final String
            EDIT_NAME_KEY = "restoName",
            EDIT_DESC_KEY = "restoDesc",
            EDIT_WEIGHT_KEY = "restoWeight",
            EDIT_POSITION_KEY = "editPosition";

    private boolean runOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_randomizer);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        this.rAdapter = new RestaurantAdapter(restaurantList, new MyAdapterListener() {
            @Override
            public void editButtonOnClick(View v, int position) {
                Log.d("TAG", "editButtonOnClick: Item clicked at " + position );
                Intent editIntent = new Intent(RestaurantRandomizer.this, RestaurantAdd.class);
                Bundle editBundle = new Bundle();
                editBundle.putString(EDIT_NAME_KEY, restaurantList.get(position).getName());
                editBundle.putString(EDIT_DESC_KEY, restaurantList.get(position).getDescription());
                editBundle.putString(EDIT_WEIGHT_KEY, restaurantList.get(position).getWeight());
                editBundle.putInt(EDIT_POSITION_KEY, position);
                editIntent.putExtras(editBundle);

                startActivityForResult(editIntent, EDIT_REQUEST_CODE);
                Log.d("TAG", "editButtonOnClick: Activity Started at " + position );

            }


        });

        RecyclerView.LayoutManager rLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(rLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(rAdapter);

        if (runOnce != true) {
            prepareRestaurantData();
        }

        Button surpriseButton = (Button) findViewById(R.id.surpriseButton);
        surpriseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                surprise(v);
            }
        });

        //transfer to add view
        Button addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent restaurantAddIntent = new Intent(RestaurantRandomizer.this, RestaurantAdd.class);
                startActivity(restaurantAddIntent);
            }
        });


        Intent intent = getIntent();

        if (intent != null && intent.getExtras() != null) {
            Restaurant restaurant = new Restaurant(intent.getStringExtra("restoName").toString(), intent.getStringExtra("restoDesc").toString(), intent.getStringExtra("restoWeight").toString());
            restaurantList.add(restaurant);

            rAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == EDIT_REQUEST_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                String editedName = data.getStringExtra(EDIT_NAME_KEY),
                editedDescription = data.getStringExtra(EDIT_DESC_KEY),
                editedWeight = data.getStringExtra(EDIT_WEIGHT_KEY);
                int position = data.getExtras().getInt(EDIT_POSITION_KEY);

                Log.d("TAG", "Data retrieved from intent: Position: " + position + " " + editedName + " " + editedDescription + " " + editedWeight);

                if (editedName != null ) {
                    restaurantList.get(position).setName(editedName.toString());
                }
                if (editedDescription != null) {
                    restaurantList.get(position).setDescription(editedDescription.toString());
                }
                if (editedWeight != null) {
                    restaurantList.get(position).setWeight(editedWeight.toString());
                }

                rAdapter.notifyDataSetChanged();
            }
        }

        if (requestCode == ADD_REQUEST_CODE) {

        }
    }

    private void prepareRestaurantData() {
        Restaurant restaurant = new Restaurant("Noel's", "Barbeque", "200");
        restaurantList.add(restaurant);

        restaurant = new Restaurant("Eric's", "Sisig", "300");
        restaurantList.add(restaurant);

        restaurant = new Restaurant("Dixie's", "Sisig", "150");
        restaurantList.add(restaurant);


        rAdapter.notifyDataSetChanged();

        runOnce = true;

    }


    private void surprise(View view) {
        double total = 0;
        for (Restaurant r : restaurantList) {
            total += Double.parseDouble(r.getWeight());
        }

        Restaurant randomResto = restaurantList.get(new Random().nextInt(restaurantList.size()));

        Snackbar.make(view, randomResto.getName(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }




}
