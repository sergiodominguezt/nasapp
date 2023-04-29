package com.example.nasapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<AsteroidModel> asteroidModels;
    private SelectListener listener;

    private AdapterView.OnItemClickListener onItemClickListener;

    public MyAdapter(ArrayList<AsteroidModel> asteroidModels, Context context, SelectListener listener) {
        this.context = context;
        this.asteroidModels = asteroidModels;
        this.listener =listener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.asteroidentry,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AsteroidModel asteroidModel = asteroidModels.get(position);
        holder.name.setText(asteroidModel.getName());
        holder.hazardousAsteroid.setText(asteroidModel.getName());
        holder.neoReferenceId.setText(asteroidModel.getName());
        holder.nasaJplUrl.setText(asteroidModel.getName());
        holder.isSentryObject.setText(asteroidModel.getName());
        holder.absoluteMagnitude.setText(asteroidModel.getName());

        holder.cardView.setOnClickListener(v -> listener.onItemClicked(asteroidModel));



    }

    @Override
    public int getItemCount() {
        return asteroidModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        TextView name, referenceId, hazardousAsteroid, neoReferenceId, nasaJplUrl, isSentryObject, absoluteMagnitude;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.asteroidName);
            referenceId = itemView.findViewById(R.id.referenceId);
            hazardousAsteroid = itemView.findViewById(R.id.dangerousAsteroid);
            neoReferenceId = itemView.findViewById(R.id.neoReferenceId);
            nasaJplUrl = itemView.findViewById(R.id.nasaJplUrl);
            isSentryObject = itemView.findViewById(R.id.isSentryObject);
            absoluteMagnitude = itemView.findViewById(R.id.absoluteMagnitude);

            cardView = itemView.findViewById(R.id.cardAsteroidEntry);
        }
    }


}
