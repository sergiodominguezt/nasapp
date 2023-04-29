package com.example.nasapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nasapp.database.DBAsteroidHelper;

public class AsteroidList extends AppCompatActivity implements SelectListener{
    RecyclerView recyclerView;
    MyAdapter adapter;
    int userId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asteroidlist);
        recyclerView = findViewById(R.id.recyclerView);
        Button btn = findViewById(R.id.buttonDownload);

        btn.setOnClickListener(v -> displayData());
    }
    private void displayData() {
        DBAsteroidHelper dbAsteroidHelper = new DBAsteroidHelper(this, userId);
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new MyAdapter(dbAsteroidHelper.getAllAsteroids(),AsteroidList.this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onItemClicked(AsteroidModel asteroidModel) {
        Toast.makeText(this, asteroidModel.getName(), Toast.LENGTH_SHORT).show();
    }
}
