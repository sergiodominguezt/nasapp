package com.example.nasapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.example.nasapp.R;
import com.example.nasapp.interfaces.SelectListener;
import com.example.nasapp.models.AsteroidModel;

import java.util.ArrayList;


public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<AsteroidModel> asteroidModels;
    private SelectListener listener;

    private AdapterView.OnItemClickListener onItemClickListener;

    public HomeAdapter(ArrayList<AsteroidModel> asteroidModels, Context context, SelectListener listener) {
        this.context = context;
        this.asteroidModels = asteroidModels;
        this.listener = listener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.asteroid_entry,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AsteroidModel asteroidModel = asteroidModels.get(position);
        holder.name.setText(asteroidModel.getName());
        holder.hazardousAsteroid.setText(asteroidModel.isHazardousAsteroid() ? "Yes" : "No");


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(asteroidModel);
            }
        });

    }

    @Override
    public int getItemCount() {
        return asteroidModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        TextView name, hazardousAsteroid;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.asteroidName);
            hazardousAsteroid = itemView.findViewById(R.id.dangerousAsteroid);


            cardView = itemView.findViewById(R.id.cardAsteroidEntry);
        }
    }


}
