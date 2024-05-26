package com.samsung.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.samsung.finalproject.databinding.ActivityMapsBinding;

import java.util.ArrayList;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMapClickListener {

    private SupportMapFragment mapFragment;
    private Button mainBtn;
    private Button okBtn;
    private Button cancelBtn;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private TextView timeView;
    private final Marker[] userPoint = new Marker[1];
    private EditText newPlace;
    private TimeThread timeThread;
    private DBHandler db;


    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkPermissions();

        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        timeView = findViewById(R.id.time);
        newPlace = findViewById(R.id.new_place);
        mainBtn = findViewById(R.id.main_button);
        okBtn = findViewById(R.id.ok_button);
        cancelBtn = findViewById(R.id.cancel_btn);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        db = DBHandler.getDB(this.getApplicationContext());

        timeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuActivity.places.clear();
                MenuActivity.places.add(
                        new PlaceItem(
                                "Ваше местоположение",
                                new SolarTime(currentLocation.getLongitude()).toString(),
                                currentLocation.getLatitude(),
                                currentLocation.getLongitude(),
                                null
                        )
                );
                Intent menu = new Intent(MapsActivity.this, MenuActivity.class);
                startActivity(menu);
            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        mapFragment.getMapAsync(this);

        timeView.setText("Loading");
        mainBtn.setOnClickListener(this);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPlace.setVisibility(View.GONE);
                okBtn.setVisibility(View.GONE);
                cancelBtn.setVisibility(View.GONE);
                timeView.setVisibility(View.VISIBLE);

                db.addNewPlace(newPlace.getText().toString(), userPoint[0].getPosition().latitude + "", userPoint[0].getPosition().longitude + "");

                hideKeyboard(okBtn);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPlace.setVisibility(View.GONE);
                okBtn.setVisibility(View.GONE);
                cancelBtn.setVisibility(View.GONE);
                timeView.setVisibility(View.VISIBLE);

                hideKeyboard(cancelBtn);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        getLastLocation();
        LatLng loc = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

        double longitude = currentLocation.getLongitude();
        timeThread = new TimeThread(new SolarTime(longitude), this.timeView);
        timeThread.start();


        userPoint[0] = mMap.addMarker(new MarkerOptions().position(loc).title("Вы здесь"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        mMap.setOnMapClickListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            PlaceItem place = new PlaceItem(extras.getString("name"), "12:00:00", extras.getDouble("latitude"), extras.getDouble("longitude"), extras.getLong("id", 0));
            chooseLocation(place);
        }
    }

    private void getLastLocation() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.NETWORK_PROVIDER;
        @SuppressLint("MissingPermission") android.location.Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        currentLocation = lastKnownLocation;
    }

    @Override
    public void onClick(View v) {
        timeView.setVisibility(View.INVISIBLE);
        newPlace.setVisibility(View.VISIBLE);
        okBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        if (userPoint[0] != null) {
            userPoint[0].remove();
        }
        userPoint[0] = mMap.addMarker(new MarkerOptions().position(latLng).title("Новая локация"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        rebootTimeThread(latLng.longitude);
    }

    public void rebootTimeThread(double longitude) {
        timeThread.running = false;
        timeThread = new TimeThread(new SolarTime(longitude), this.timeView);
        timeThread.start();
    }

    public void chooseLocation(PlaceItem place) {
        if (userPoint[0] != null) {
            userPoint[0].remove();
        }

        LatLng loc = new LatLng(place.getLatitude(), place.getLongitude());
        userPoint[0] = mMap.addMarker(new MarkerOptions().position(loc).title(place.city));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        rebootTimeThread(place.getLongitude());
    }

    public void hideKeyboard(Button btn) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(btn.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    public void checkPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }
}