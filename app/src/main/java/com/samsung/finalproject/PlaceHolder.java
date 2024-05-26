package com.samsung.finalproject;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PlaceHolder extends RecyclerView.ViewHolder {
    TextView cityView;
    TextView timeView;
    TextView longitudeView;
    TextView latitudeView;

    public PlaceHolder(@NonNull View itemView) {
        super(itemView);
        cityView = itemView.findViewById(R.id.city);
        timeView = itemView.findViewById(R.id.time);
        longitudeView = itemView.findViewById(R.id.longitude);
        latitudeView = itemView.findViewById(R.id.latitude);
    }
}

