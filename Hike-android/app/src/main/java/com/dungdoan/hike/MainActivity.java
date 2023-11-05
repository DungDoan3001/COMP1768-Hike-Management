package com.dungdoan.hike;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton addHikeBtn;
    private RecyclerView hikeRV;

    private DbHelper dbHelper;

    ActionBar actionBar;

    String orderByNewest = HikeConstants.HIKE_CREATED_AT + " DESC";
    String orderByOldest = HikeConstants.HIKE_CREATED_AT + " ASC";
    String orderByNewestUpdated = HikeConstants.HIKE_UPDATED_AT + " DESC";
    String orderByOldestUpdated = HikeConstants.HIKE_UPDATED_AT + " ASC";
    String orderByNameAsc = HikeConstants.HIKE_NAME + " ASC";
    String orderByNameDesc = HikeConstants.HIKE_NAME + " DESC";
    String currentSortOption = orderByNewest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar = getSupportActionBar();

        addHikeBtn = findViewById(R.id.add_btn);
        hikeRV = findViewById(R.id.hike_recycler_view);

        dbHelper = new DbHelper(this);

        loadHikes(orderByNewest);

        addHikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, hike_add_and_update.class);
                intent.putExtra("isEditMode", false);
                startActivity(intent);
            }
        });
    }

    private void loadHikes(String orderBy) {
        currentSortOption = orderBy;
        HikeAdapter hikeAdapter = new HikeAdapter(MainActivity.this, dbHelper.getAllHikes(orderBy));
        hikeRV.setAdapter(hikeAdapter);

        actionBar.setSubtitle("Total: " + dbHelper.getHikesCount());
    }

    private void searchHike(String query) {
        HikeAdapter hikeAdapter = new HikeAdapter(MainActivity.this, dbHelper.searchHike(query));
        hikeRV.setAdapter(hikeAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadHikes(currentSortOption);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.application_menu, menu);
        MenuItem item = menu.findItem(R.id.search_action);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchHike(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchHike(newText);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.sort_action) {
            sortOptionDialog();
        } else if (id == R.id.delete_action) {
            dbHelper.deleteAllHikes();
            Toast.makeText(this, "Delete All Hikes Successfully!", Toast.LENGTH_SHORT).show();
            loadHikes(currentSortOption);
        }
        return super.onOptionsItemSelected(item);
    }

    private void sortOptionDialog() {
        String[] options = {"A-Z", "Z-A", "Newest", "Oldest", "Latest updated", "Oldest updated"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sort By").setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){
                    //A-Z
                    loadHikes(orderByNameAsc);
                }
                else if(which == 1){
                    //Z-A
                    loadHikes(orderByNameDesc);
                }
                else if(which == 2){
                    //Newest
                    loadHikes(orderByNewest);
                }
                else if(which == 3){
                    //Oldest
                    loadHikes(orderByOldest);
                }
                else if(which == 4){
                    //Latest updated
                    loadHikes(orderByNewestUpdated);
                }
                else if(which == 5){
                    //Oldest updated
                    loadHikes(orderByOldestUpdated);
                }
            }
        }).create().show();
    }
}