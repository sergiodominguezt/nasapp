package com.example.nasapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.nasapp.database.DBAsteroidHelper;
import com.example.nasapp.database.DBUsersHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Iterator;


public class HomeActivity extends AppCompatActivity implements SelectListener{
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String EMAIL_KEY = "email_key";
    SharedPreferences sharedPreferences;
    String email;
    String password;
    String url;
    RecyclerView recyclerView;
    MyAdapter adapter;
    int userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        email = sharedPreferences.getString(EMAIL_KEY, null);
        TextView welcome = findViewById(R.id.idWelcome);
        url = "https://api.nasa.gov/neo/rest/v1/feed?start_date=2023-04-26&end_date=2023-04-30&api_key=R8LmGPZJGAirleebrNnMmuH3XtidhC7XmiE0oKtu";
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        TextView bol = findViewById(R.id.idBoolean);
        bol.setText("false");


        welcome.setText("Welcome \n" + email);
        displayData();
        Button logoutBtn = findViewById(R.id.idBtnLogout);
        asteroidDetailsLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        adapter.notifyDataSetChanged();
                    }

                });
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        Button downloadBtn = findViewById(R.id.idDownloadData);
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    volleyGet(userId);
            }
        });
    }
    public void volleyGet(int userId) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest requestTest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    System.out.println("Response: " + response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("Response: " + response.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error: " + error.toString());
            }
        });
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject nearEarthObjects = response.getJSONObject("near_earth_objects");
                    Iterator<String> keys = nearEarthObjects.keys();
                    while (keys.hasNext()) {
                        String date = keys.next();
                        JSONArray asteroids = nearEarthObjects.getJSONArray(date);
                        for (int i = 0; i < asteroids.length(); i++) {
                            JSONObject asteroid = asteroids.getJSONObject(i);
                            String name = asteroid.getString("name");
                            String id = asteroid.getString("id");
                            boolean hazardous = asteroid.getBoolean("is_potentially_hazardous_asteroid");
                            String neoReferenceId = asteroid.getString("neo_reference_id");
                            String nasaJplUrl = asteroid.getString("nasa_jpl_url");
                            boolean isSentryObject = asteroid.getBoolean("is_sentry_object");
                            String absoluteMagnitude = asteroid.getString("absolute_magnitude_h");

                            DBAsteroidHelper dbAsteroidHelper = new DBAsteroidHelper(HomeActivity.this, userId);
                            dbAsteroidHelper.insertAsteroids(name, hazardous, neoReferenceId, nasaJplUrl, isSentryObject, absoluteMagnitude, userId);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error: " + error.toString());
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        displayData();
    }
    private void displayData() {
        DBAsteroidHelper dbAsteroidHelper = new DBAsteroidHelper(this, userId);
        DBUsersHelper dbUsersHelper = new DBUsersHelper(this);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        email = sharedPreferences.getString(EMAIL_KEY, null);
        userId = dbUsersHelper.getUserById(email);

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new MyAdapter(dbAsteroidHelper.getAllAsteroids(userId),HomeActivity.this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onItemClicked(AsteroidModel asteroidModel) {

        Intent intent = new Intent(HomeActivity.this, AsteroidDetailActivity.class);
        intent.putExtra("name", asteroidModel.getName());
        intent.putExtra("nasaJplUrl", asteroidModel.getNasaJplUrl());
        intent.putExtra("hazardousAsteroid", asteroidModel.isHazardousAsteroid() ? "Yes" : "No");
        intent.putExtra("isSentryObject", asteroidModel.isSentryObject() ? "Yes" : "No");
        intent.putExtra("absoluteMagnitude", asteroidModel.getAbsoluteMagnitude());
        startActivity(intent);
    }

    private ActivityResultLauncher<Intent> asteroidDetailsLauncher;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
