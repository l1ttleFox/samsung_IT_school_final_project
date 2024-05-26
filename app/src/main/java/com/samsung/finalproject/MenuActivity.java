package com.samsung.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {
    private RecyclerView placesView;
    private Button backButton;
    private PlaceAdapter placeAdapter;
    private DBHandler db;
    public static ArrayList<PlaceItem> places = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        db = DBHandler.getDB(MenuActivity.this.getApplicationContext());

        places.addAll(db.getAllPlaces());

        this.backButton = (Button) findViewById(R.id.backBtn);
        placeAdapter = new PlaceAdapter(getApplicationContext(), places);

        placesView = findViewById(R.id.placesList);
        placesView.setLayoutManager(new LinearLayoutManager(this));
        placesView.setAdapter(placeAdapter);
        placesView.addOnItemTouchListener(new PlacesTouchListener(this, placesView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent mapsActivity = new Intent(MenuActivity.this, MapsActivity.class);
                mapsActivity.putExtra("name", places.get(position).getCity());
                mapsActivity.putExtra("latitude", places.get(position).getLatitude());
                mapsActivity.putExtra("longitude", places.get(position).getLongitude());
                mapsActivity.putExtra("id", places.get(position).getId());
                startActivity(mapsActivity);
            }

            @Override
            public void onLongClick(View view, int position) {
                if (position != 0) {
                    showPopupMenu(view, position);
                }
            }
        }));
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showPopupMenu(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(this, view);

        popupMenu.inflate(R.menu.place_item_popup_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.popup_delete) {
                    db.deletePlace(places.get(position).getId());
                    places = db.getAllPlaces();
                    placeAdapter.deleteItem(position);

                    return true;
                } else {
                    return false;
                }
            }
        });
        popupMenu.show();
    }
}