package com.example.nasapp.activities;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.nasapp.R;
import com.example.nasapp.adapters.HomeAdapter;
import com.example.nasapp.database.DBAsteroidHelper;
import com.example.nasapp.database.DBUsersHelper;
import com.example.nasapp.interfaces.SelectListener;
import com.example.nasapp.models.AsteroidModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.Iterator;


public class HomeActivity extends AppCompatActivity implements SelectListener {
    RequestQueue requestQueue;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String EMAIL_KEY = "email_key";
    public static final String ID_KEY = "id_key";
    public static final String FIRST_NAME_KEY = "first_name_key";
    public static final String LAST_NAME_KEY = "last_name_key";

    SharedPreferences sharedPreferences;
    String email, fistName, lastName;
    String url;
    RecyclerView recyclerView;
    HomeAdapter adapter;
    int userId;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BasicNetwork basicNetwork = new BasicNetwork(new HurlStack());
        DiskBasedCache cacheDir = new DiskBasedCache(getCacheDir(), 1024 * 1024);

        requestQueue = new RequestQueue(cacheDir, basicNetwork);
        requestQueue.start();

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        email = sharedPreferences.getString(EMAIL_KEY, null);
        fistName = sharedPreferences.getString(FIRST_NAME_KEY, null);
        lastName = sharedPreferences.getString(LAST_NAME_KEY, null);

        TextView welcome = findViewById(R.id.idWelcome);
        url = "https://api.nasa.gov/neo/rest/v1/feed?start_date=2023-04-26&end_date=2023-04-30&api_key=R8LmGPZJGAirleebrNnMmuH3XtidhC7XmiE0oKtu";
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        welcome.setText("Welcome \n" + fistName + " " + lastName);

        Button logoutBtn = findViewById(R.id.idBtnLogout);
        progressBar = findViewById(R.id.idProgressBar);

        displayData();
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
                progressBar.setVisibility(View.VISIBLE);
                volleyGet();
            }
        });
    }
    public void volleyGet() {
        int userId = sharedPreferences.getInt(ID_KEY, 0);
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
                displayData();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error getting data" + error.toString());
                progressBar.setVisibility(View.GONE);
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(500000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);

    }
    private void displayData() {
        DBAsteroidHelper dbAsteroidHelper = new DBAsteroidHelper(this, userId);
        DBUsersHelper dbUsersHelper = new DBUsersHelper(this);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt(ID_KEY, 0);

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new HomeAdapter(dbAsteroidHelper.getAllAsteroids(userId),HomeActivity.this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        progressBar.setVisibility(View.GONE);
    }
    @Override
    public void onItemClicked(AsteroidModel asteroidModel) {

        Intent intent = new Intent(HomeActivity.this, AsteroidDetailActivity.class);
        intent.putExtra("name", asteroidModel.getName());
        intent.putExtra("nasaJplUrl", asteroidModel.getNasaJplUrl());
        intent.putExtra("hazardousAsteroid", asteroidModel.isHazardousAsteroid() ? "Yes" : "No");
        intent.putExtra("isSentryObject", asteroidModel.isSentryObject() ? "Yes" : "No");
        intent.putExtra("absoluteMagnitude", asteroidModel.getAbsoluteMagnitude());
        intent.putExtra("neoReferenceId", asteroidModel.getNeoReferenceId());
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
