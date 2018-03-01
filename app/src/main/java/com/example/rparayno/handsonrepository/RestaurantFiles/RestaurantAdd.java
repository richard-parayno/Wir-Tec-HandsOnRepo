package com.example.rparayno.handsonrepository.RestaurantFiles;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.rparayno.handsonrepository.R;

public class RestaurantAdd extends AppCompatActivity {


    public static final String RESTO_NAME_KEY = "restoName", DESCRIPTION_KEY = "restoDesc", WEIGHT_KEY = "restoWeight";

    private EditText restoName, restoDesc, restoWeight;
    private TextView restoNameTV, restoDescTV, restoWeightTV;
    private int mode = 0;
    private int position;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_add);

        initAdditionalResources();

        Button addResto = (Button) findViewById(R.id.addResto);


        final Intent restoIntent = getIntent();
        if (restoIntent.getExtras() != null) {
            Bundle bundle = restoIntent.getExtras();
            //Get resto to edit
            restoName.setText(bundle.getString(RestaurantRandomizer.EDIT_NAME_KEY));
            restoDesc.setText(bundle.getString(RestaurantRandomizer.EDIT_DESC_KEY));
            restoWeight.setText(bundle.getString(RestaurantRandomizer.EDIT_WEIGHT_KEY));
            //set flag to edit mode
            mode = 1;
            position = restoIntent.getExtras().getInt(RestaurantRandomizer.EDIT_POSITION_KEY);

            addResto.setText("Edit");
            addResto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent editedIntent = new Intent();
                    Bundle editedBundle = new Bundle();
                    editedBundle.putString(RESTO_NAME_KEY, restoNameTV.getText().toString());
                    editedBundle.putString(DESCRIPTION_KEY, restoDescTV.getText().toString());
                    editedBundle.putString(WEIGHT_KEY, restoWeightTV.getText().toString());
                    editedBundle.putInt(RestaurantRandomizer.EDIT_POSITION_KEY, position);
                    editedIntent.putExtras(editedBundle);

                    setResult(RestaurantRandomizer.RESULT_OK, editedIntent);
                    finish();
                }
            });


        } else {

            //add logic
            addResto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent getIntent = new Intent(RestaurantAdd.this, RestaurantRandomizer.class);
                    getIntent.putExtra(RESTO_NAME_KEY, restoNameTV.getText().toString());
                    getIntent.putExtra(DESCRIPTION_KEY, restoDescTV.getText().toString());
                    getIntent.putExtra(WEIGHT_KEY, restoWeightTV.getText().toString());


                    startActivity(getIntent);
                }
            });
        }
    }

    private void initAdditionalResources() {
        restoName = (EditText) findViewById(R.id.restoName);
        restoWeight = (EditText) findViewById(R.id.restoWeight);
        restoDesc = (EditText) findViewById(R.id.restoDesc);

        restoNameTV = (TextView) findViewById(R.id.restoName);
        restoDescTV = (TextView) findViewById(R.id.restoDesc);
        restoWeightTV = (TextView) findViewById(R.id.restoWeight);
    }


}
