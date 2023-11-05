package com.dungdoan.hike;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class observation_details extends AppCompatActivity {

    private ImageView image;
    private TextView name, time, comment, createdAt, updatedAt;
    private ActionBar actionBar;
    private String observationId;
    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation_details);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Observation details");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        observationId = intent.getStringExtra("OBSERVATION_ID");

        dbHelper = new DbHelper(this);
        image = findViewById(R.id.observation_image);
        name = findViewById(R.id.observation_name);
        time = findViewById(R.id.observation_time);
        comment = findViewById(R.id.observation_comment);
        createdAt = findViewById(R.id.observation_created_at);
        updatedAt = findViewById(R.id.observation_last_updated_at);

        showObservationDetails();
    }

    private void showObservationDetails() {
        String selectQuery = String.format("SELECT * FROM %s WHERE %s = \"%s\"", ObservationConstants.TABLE_NAME, ObservationConstants.OBSERVATION_ID, observationId);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int idColumnIndex = cursor.getColumnIndex(ObservationConstants.OBSERVATION_ID);
                @SuppressLint("Range") int nameColumnIndex = cursor.getColumnIndex(ObservationConstants.OBSERVATION_NAME);
                @SuppressLint("Range") int imageColumnIndex = cursor.getColumnIndex(ObservationConstants.OBSERVATION_IMAGE);
                @SuppressLint("Range") int timeColumnIndex = cursor.getColumnIndex(ObservationConstants.OBSERVATION_TIME);
                @SuppressLint("Range") int commentColumnIndex = cursor.getColumnIndex(ObservationConstants.OBSERVATION_COMMENT);
                @SuppressLint("Range") int createdAtColumnIndex = cursor.getColumnIndex(ObservationConstants.OBSERVATION_CREATED_AT);
                @SuppressLint("Range") int updatedAtColumnIndex = cursor.getColumnIndex(ObservationConstants.OBSERVATION_UPDATED_AT);

                String id = String.valueOf(cursor.getInt(idColumnIndex));
                String observationName = cursor.getString(nameColumnIndex);
                String observationComment = cursor.getString(commentColumnIndex);
                String observationImage = cursor.getString(imageColumnIndex);
                String observationCreatedAt = cursor.getString(createdAtColumnIndex);
                String observationUpdatedAt = cursor.getString(updatedAtColumnIndex);
                String observationTime = cursor.getString(timeColumnIndex);

                Calendar createdAtCalendar = Calendar.getInstance(Locale.getDefault());
                createdAtCalendar.setTimeInMillis(Long.parseLong(observationCreatedAt));
                SimpleDateFormat createdAtFormat = new SimpleDateFormat("HH:mm, dd/MM/yyyy", Locale.getDefault());
                String createdAtTime =  createdAtFormat.format(createdAtCalendar.getTime());

                Calendar updatedAtCalendar = Calendar.getInstance(Locale.getDefault());
                updatedAtCalendar.setTimeInMillis(Long.parseLong(observationUpdatedAt));
                SimpleDateFormat updatedAtFormat = new SimpleDateFormat("HH:mm, dd/MM/yyyy", Locale.getDefault());
                String updatedAtTime = updatedAtFormat.format(updatedAtCalendar.getTime());

                name.setText(observationName);
                comment.setText(observationComment);
                createdAt.setText(createdAtTime);
                updatedAt.setText(updatedAtTime);
                time.setText(observationTime);

                if(observationImage.equals("null")) {
                    image.setImageResource(R.drawable.image_vector);
                } else {
                    image.setImageURI(Uri.parse(observationImage));
                }
            } while (cursor.moveToNext());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return super.onSupportNavigateUp();
    }
}