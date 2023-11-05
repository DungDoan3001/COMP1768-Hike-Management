package com.dungdoan.hike;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class hike_details extends AppCompatActivity {

    private ImageView imageView;
    private TextView hikeName, location, date, parking, length, level, description, createdAt, updatedAt;
    private Button addObservationBtn;
    private RecyclerView observationRV;
    private ActionBar actionBar;
    private String hikeID;
    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike_details);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Hike Details");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        hikeID = intent.getStringExtra("HIKE_ID");

        addObservationBtn = findViewById(R.id.add_observation_btn);
        dbHelper = new DbHelper(this);

        imageView = findViewById(R.id.hike_image);
        hikeName = findViewById(R.id.hike_name);
        location = findViewById(R.id.hike_location);
        date = findViewById(R.id.hike_date);
        parking = findViewById(R.id.hike_parking);
        length = findViewById(R.id.hike_length);
        level = findViewById(R.id.hike_level);
        description = findViewById(R.id.hike_description);
        createdAt = findViewById(R.id.hike_created_date);
        updatedAt = findViewById(R.id.hike_updated_at);

        showHikeDetails();

        addObservationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(hike_details.this, observation_add_and_update.class);
                intent.putExtra("IS_EDIT_MODE", false);
                intent.putExtra("HIKE_ID", hikeID);
                startActivity(intent);
            }
        });
    }

    private void showHikeDetails() {
        String selectQuery = String.format("SELECT * FROM %s WHERE %s = \"%s\"", HikeConstants.TABLE_NAME, HikeConstants.HIKE_ID, hikeID);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        observationRV = findViewById(R.id.hike_observation_recycler_view);

        if(cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int idColumnIndex = cursor.getColumnIndex(HikeConstants.HIKE_ID);
                @SuppressLint("Range") int nameColumnIndex = cursor.getColumnIndex(HikeConstants.HIKE_NAME);
                @SuppressLint("Range") int locationColumnIndex = cursor.getColumnIndex(HikeConstants.HIKE_LOCATION);
                @SuppressLint("Range") int dateColumnIndex = cursor.getColumnIndex(HikeConstants.HIKE_DATE);
                @SuppressLint("Range") int lengthColumnIndex = cursor.getColumnIndex(HikeConstants.HIKE_LENGTH);
                @SuppressLint("Range") int parkingColumnIndex = cursor.getColumnIndex(HikeConstants.HIKE_PARKING);
                @SuppressLint("Range") int levelColumnIndex = cursor.getColumnIndex(HikeConstants.HIKE_LEVEL);
                @SuppressLint("Range") int descriptionColumnIndex = cursor.getColumnIndex(HikeConstants.HIKE_DESCRIPTION);
                @SuppressLint("Range") int imageColumnIndex = cursor.getColumnIndex(HikeConstants.HIKE_IMAGE);
                @SuppressLint("Range") int createdAtColumnIndex = cursor.getColumnIndex(HikeConstants.HIKE_CREATED_AT);
                @SuppressLint("Range") int updatedAtColumnIndex = cursor.getColumnIndex(HikeConstants.HIKE_UPDATED_AT);

                String id = "" + cursor.getInt(idColumnIndex);
                String name = cursor.getString(nameColumnIndex);
                String location = cursor.getString(locationColumnIndex);
                String date = cursor.getString(dateColumnIndex);
                String length = cursor.getString(lengthColumnIndex);
                String parking = cursor.getString(parkingColumnIndex);
                String level = cursor.getString(levelColumnIndex);
                String description = cursor.getString(descriptionColumnIndex);
                String image = cursor.getString(imageColumnIndex);
                String created_at = cursor.getString(createdAtColumnIndex);
                String updated_at = cursor.getString(updatedAtColumnIndex);

                Calendar createdAtCalendar = Calendar.getInstance(Locale.getDefault());
                createdAtCalendar.setTimeInMillis(Long.parseLong(created_at));
                SimpleDateFormat createdAtFormat = new SimpleDateFormat("HH:mm, dd/MM/yyyy", Locale.getDefault());
                String createdAtTime =  createdAtFormat.format(createdAtCalendar.getTime());

                Calendar updatedAtCalendar = Calendar.getInstance(Locale.getDefault());
                updatedAtCalendar.setTimeInMillis(Long.parseLong(updated_at));
                SimpleDateFormat updatedAtFormat = new SimpleDateFormat("HH:mm, dd/MM/yyyy", Locale.getDefault());
                String updatedAtTime = updatedAtFormat.format(updatedAtCalendar.getTime());

                hikeName.setText(name);
                this.location.setText(location);
                this.date.setText(date);
                this.parking.setText(parking);
                this.length.setText(length);
                this.description.setText(description);
                this.createdAt.setText(createdAtTime);
                this.updatedAt.setText(updatedAtTime);
                this.level.setText(level);

                if(level.trim().toLowerCase().equals("easy")) {
                    this.level.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.green));
                } else if(level.trim().toLowerCase().equals("intermediate")) {
                    this.level.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.yellow));
                } else if(level.trim().toLowerCase().equals("hard")) {
                    this.level.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.red));
                }

                if(image.equals("null")) {
                    this.imageView.setImageResource(R.drawable.image_vector);
                } else {
                    this.imageView.setImageURI(Uri.parse(image));
                }
            } while (cursor.moveToNext());
        }

        database.close();
        loadObservation(hikeID);
    }

    private void loadObservation(String hikeID) {
        ObservationAdapter observationAdapter = new ObservationAdapter(hike_details.this,
                dbHelper.getObservationsByHikeID(hikeID));

        observationRV.setAdapter(observationAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadObservation(hikeID);
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return super.onSupportNavigateUp();
    }
}