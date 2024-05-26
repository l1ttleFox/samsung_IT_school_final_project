package com.samsung.finalproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceHolder> {
    Context context;
    private ArrayList<TimeThread> timeThreads = new ArrayList<>();

    public PlaceAdapter(Context context, ArrayList<PlaceItem> places) {
        this.context = context;
        this.places = places;
    }

    ArrayList<PlaceItem> places;

    @NonNull
    @Override
    public PlaceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PlaceHolder(LayoutInflater.from(context).inflate(R.layout.place_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceHolder holder, int position) {
        String name = places.get(position).getCity();
        double latitude = places.get(position).getLatitude();
        double longitude = places.get(position).getLongitude();

        holder.cityView.setText(name);
        holder.timeView.setText(places.get(position).getTime());
        holder.latitudeView.setText("" + latitude);
        holder.longitudeView.setText("" + longitude);

        TimeThread timeThread = new TimeThread(new SolarTime(places.get(position).getLongitude()), holder.timeView);
        this.timeThreads.add(timeThread);
        timeThread.start();
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        for (int i = 0; i < this.timeThreads.size(); i++) {
            this.timeThreads.get(i).running = false;
        }
    }

    public void deleteItem(int position) {
        places.remove(position);
        notifyItemRemoved(position);
    }
}
