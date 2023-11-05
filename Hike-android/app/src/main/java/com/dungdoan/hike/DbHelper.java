package com.dungdoan.hike;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import kotlin.Suppress;

public class DbHelper extends SQLiteOpenHelper {
    private SQLiteDatabase database;

    public DbHelper(@Nullable Context context) {
        super(context, DatabaseConstants.DB_NAME, null, DatabaseConstants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(HikeConstants.CREATE_TABLE_QUERY); // Create table hike
        db.execSQL(ObservationConstants.CREATE_TABLE_QUERY); // Create table observation
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Override the old tables
        db.execSQL(HikeConstants.DROP_TABLE_QUERY);
        Log.w(this.getClass().getName(), HikeConstants.TABLE_NAME + " database upgrade to version " + newVersion + " - old data lost");

        db.execSQL(ObservationConstants.DROP_TABLE_QUERY);
        Log.w(this.getClass().getName(), ObservationConstants.TABLE_NAME + " database upgrade to version " + newVersion + " - old data lost");

        // Create new table
        onCreate(db);
    }

    public long insertHike(String name,
                           String location,
                           String date,
                           String length,
                           String parking,
                           String level,
                           String description,
                           String image,
                           String created_At,
                           String updated_At) {
        database = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(HikeConstants.HIKE_NAME, name);
        values.put(HikeConstants.HIKE_LOCATION, location);
        values.put(HikeConstants.HIKE_DATE, date);
        values.put(HikeConstants.HIKE_LENGTH, length);
        values.put(HikeConstants.HIKE_PARKING, parking);
        values.put(HikeConstants.HIKE_LEVEL, level);
        values.put(HikeConstants.HIKE_DESCRIPTION, description);
        values.put(HikeConstants.HIKE_IMAGE, image);
        values.put(HikeConstants.HIKE_CREATED_AT, created_At);
        values.put(HikeConstants.HIKE_UPDATED_AT, updated_At);

        long result =  database.insertOrThrow(HikeConstants.TABLE_NAME, null, values);
        database.close();

        return result;
    }

    public void updateHike(String ID,
                           String name,
                           String location,
                           String date,
                           String length,
                           String parking,
                           String level,
                           String description,
                           String image,
                           String created_At,
                           String updated_At) {
        database = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(HikeConstants.HIKE_NAME, name);
        values.put(HikeConstants.HIKE_LOCATION, location);
        values.put(HikeConstants.HIKE_DATE, date);
        values.put(HikeConstants.HIKE_LENGTH, length);
        values.put(HikeConstants.HIKE_PARKING, parking);
        values.put(HikeConstants.HIKE_LEVEL, level);
        values.put(HikeConstants.HIKE_DESCRIPTION, description);
        values.put(HikeConstants.HIKE_IMAGE, image);
        values.put(HikeConstants.HIKE_CREATED_AT, created_At);
        values.put(HikeConstants.HIKE_UPDATED_AT, updated_At);

        database.update(HikeConstants.TABLE_NAME, values, HikeConstants.HIKE_ID + " = ?", new String[]{ID});
        database.close();
    }

    public ArrayList<HikeModel> getAllHikes(String orderBy) {
        database = getReadableDatabase();

        ArrayList<HikeModel> hikeArrayList = new ArrayList<HikeModel>();

        Cursor cursor = database.rawQuery("SELECT * FROM " + HikeConstants.TABLE_NAME + " ORDER BY " + orderBy, null);

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

                HikeModel hikeModel = new HikeModel(
                        ""+cursor.getInt(idColumnIndex),
                        cursor.getString(nameColumnIndex),
                        cursor.getString(locationColumnIndex),
                        cursor.getString(dateColumnIndex),
                        cursor.getString(lengthColumnIndex),
                        cursor.getString(parkingColumnIndex),
                        cursor.getString(levelColumnIndex),
                        cursor.getString(descriptionColumnIndex),
                        cursor.getString(imageColumnIndex),
                        cursor.getString(createdAtColumnIndex),
                        cursor.getString(updatedAtColumnIndex));
                hikeArrayList.add(hikeModel);

            } while (cursor.moveToNext());
        }
        database.close();
        return hikeArrayList;
    }

    public ArrayList<HikeModel> searchHike(String query) {
        database = getReadableDatabase();
        ArrayList<HikeModel> hikeArrayList = new ArrayList<HikeModel>();

        String selectQuery = "SELECT * FROM " + HikeConstants.TABLE_NAME + " WHERE " +
                HikeConstants.HIKE_NAME + " LIKE '%" + query + "%' OR " +
                HikeConstants.HIKE_DATE + " LIKE '%" + query + "%' OR " +
                HikeConstants.HIKE_LENGTH + " LIKE '%" + query + "%' OR " +
                HikeConstants.HIKE_LEVEL + " LIKE '%" + query + "%' OR " +
                HikeConstants.HIKE_LOCATION + " LIKE '%" + query + "%'";

        Cursor cursor = database.rawQuery(selectQuery, null);

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

                HikeModel hikeModel = new HikeModel(
                        ""+cursor.getInt(idColumnIndex),
                        cursor.getString(nameColumnIndex),
                        cursor.getString(locationColumnIndex),
                        cursor.getString(dateColumnIndex),
                        cursor.getString(lengthColumnIndex),
                        cursor.getString(parkingColumnIndex),
                        cursor.getString(levelColumnIndex),
                        cursor.getString(descriptionColumnIndex),
                        cursor.getString(imageColumnIndex),
                        cursor.getString(createdAtColumnIndex),
                        cursor.getString(updatedAtColumnIndex));
                hikeArrayList.add(hikeModel);
            } while (cursor.moveToNext());
        }
        database.close();
        return hikeArrayList;
    }

    public int getHikesCount() {
        database = getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM " + HikeConstants.TABLE_NAME, null);

        int count = cursor.getCount();

        cursor.close();

        return count;
    }

    public void deleteHike(String ID) {
        database = getWritableDatabase();
        database.delete(ObservationConstants.TABLE_NAME, ObservationConstants.OBSERVATION_HIKE_ID + " = ?", new String[]{ID});
        database.delete(HikeConstants.TABLE_NAME, HikeConstants.HIKE_ID + " = ?", new String[]{ID});
        database.close();
    }

    public void deleteAllHikes() {
        database = getWritableDatabase();
        database.execSQL("DELETE FROM " + HikeConstants.TABLE_NAME);
        database.close();
    }

    public long insertObservation(String name,
                                  String time,
                                  String comment,
                                  String image,
                                  String created_At,
                                  String updated_At,
                                  String hikeID) {
        database = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ObservationConstants.OBSERVATION_NAME, name);
        values.put(ObservationConstants.OBSERVATION_TIME, time);
        values.put(ObservationConstants.OBSERVATION_COMMENT, comment);
        values.put(ObservationConstants.OBSERVATION_IMAGE, image);
        values.put(ObservationConstants.OBSERVATION_CREATED_AT, created_At);
        values.put(ObservationConstants.OBSERVATION_UPDATED_AT, updated_At);
        values.put(ObservationConstants.OBSERVATION_HIKE_ID, hikeID);

        long result = database.insertOrThrow(ObservationConstants.TABLE_NAME, null, values);
        database.close();

        return result;
    }

    public void updateObservation(String ID,
                                  String name,
                                  String time,
                                  String comment,
                                  String image,
                                  String created_At,
                                  String updated_At,
                                  String hikeID) {
        database = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ObservationConstants.OBSERVATION_NAME, name);
        values.put(ObservationConstants.OBSERVATION_TIME, time);
        values.put(ObservationConstants.OBSERVATION_COMMENT, comment);
        values.put(ObservationConstants.OBSERVATION_IMAGE, image);
        values.put(ObservationConstants.OBSERVATION_CREATED_AT, created_At);
        values.put(ObservationConstants.OBSERVATION_UPDATED_AT, updated_At);
        values.put(ObservationConstants.OBSERVATION_HIKE_ID, hikeID);

        database.update(ObservationConstants.TABLE_NAME, values, ObservationConstants.OBSERVATION_ID + " = ?", new String[]{ID});
        database.close();
    }

    public ArrayList<ObservationModel> getObservationsByHikeID(String hikeID) {
        database = getReadableDatabase();
        ArrayList<ObservationModel> observationArrayList = new ArrayList<ObservationModel>();

        Cursor cursor = database.rawQuery("SELECT * FROM " + ObservationConstants.TABLE_NAME +
                " WHERE " + ObservationConstants.OBSERVATION_HIKE_ID + "= '" + hikeID + "' " +
                "ORDER BY " + ObservationConstants.OBSERVATION_CREATED_AT + " DESC", null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int idColumnIndex = cursor.getColumnIndex(ObservationConstants.OBSERVATION_ID);
                @SuppressLint("Range") int nameColumnIndex = cursor.getColumnIndex(ObservationConstants.OBSERVATION_NAME);
                @SuppressLint("Range") int timeColumnIndex = cursor.getColumnIndex(ObservationConstants.OBSERVATION_TIME);
                @SuppressLint("Range") int imageColumnIndex = cursor.getColumnIndex(ObservationConstants.OBSERVATION_IMAGE);
                @SuppressLint("Range") int commentColumnIndex = cursor.getColumnIndex(ObservationConstants.OBSERVATION_COMMENT);
                @SuppressLint("Range") int createdAtColumnIndex = cursor.getColumnIndex(ObservationConstants.OBSERVATION_CREATED_AT);
                @SuppressLint("Range") int updatedAtColumnIndex = cursor.getColumnIndex(ObservationConstants.OBSERVATION_UPDATED_AT);
                @SuppressLint("Range") int hikeIDColumnIndex = cursor.getColumnIndex(ObservationConstants.OBSERVATION_HIKE_ID);

                ObservationModel observationModel = new ObservationModel(
                        "" + cursor.getInt(idColumnIndex),
                        cursor.getString(nameColumnIndex),
                        cursor.getString(timeColumnIndex),
                        cursor.getString(imageColumnIndex),
                        cursor.getString(commentColumnIndex),
                        cursor.getString(createdAtColumnIndex),
                        cursor.getString(updatedAtColumnIndex),
                        cursor.getString(hikeIDColumnIndex));

                observationArrayList.add(observationModel);
            } while (cursor.moveToNext());
        }
        database.close();
        return observationArrayList;
    }

    public void deleteObservation(String ID) {
        database = getWritableDatabase();
        database.delete(ObservationConstants.TABLE_NAME, ObservationConstants.OBSERVATION_ID + " = ?", new String[]{ID});
        database.close();
    }

    public void deleteAllTables() {
        Cursor cursor = database.rawQuery("SELECT * FROM sqlite_master WHERE type='table'", null);

        if(cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String tableName = cursor.getString(0);

                if(!tableName.equals("android_metadata") && !tableName.equals("sqlite_sequence")) {
                    database.execSQL("DROP TABLE IF EXISTS " + tableName);
                }

                cursor.moveToNext();
            }
        }
        cursor.close();
    }
}
