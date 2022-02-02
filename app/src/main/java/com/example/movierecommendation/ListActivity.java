package com.example.movierecommendation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity implements MovieAdapter.onItemRecommended{

    private Button logout;
    ToggleButton toggleButton;
    private RecyclerView recycler_view;
    private MovieAdapter movieAdapter;
    List<Movies> RecommendedList;
    ProgressDialog spinner;
    public static final String ALL = "All movies";
    public static final String RECOMMENDED = "Recommended Movies";
    SharedPreferences recommended;
    public final String MyRECOMMENDED = "Like";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        logout = findViewById(R.id.logout);
        toggleButton = findViewById(R.id.changelist);

        recommended = getSharedPreferences(MyRECOMMENDED,MODE_PRIVATE);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyCREDENTIALS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.commit();

                Intent logoutIntent = new Intent(ListActivity.this, MainActivity.class);
                startActivity(logoutIntent);
                finish();
            }
        });

        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(toggleButton.getText().equals(ALL)) {
                    new MovieAsyncTask().execute("https://api.rss2json.com/v1/api.json?rss_url=https%3A%2F%2Fwww.filmfestivals.com%2Ftaxonomy%2Fterm%2F30%252B31%252B32%252B33%252B34%252B35%252B36%252B6774%252B590757%2F0%2Ffeed");
                }
                else {
                    if(recommended.contains(RECOMMENDED) && (!RecommendedList.isEmpty())){
                        movieAdapter.updateMovieList(RecommendedList);
                    }
                    else{
                        new MovieAsyncTask().execute("https://api.rss2json.com/v1/api.json?rss_url=https%3A%2F%2Fwww.filmfestivals.com%2Fchannel%2Ffilm%2Fhollywood%2Ffeed");
                    }
                }
            }
        });

        new MovieAsyncTask().execute("https://api.rss2json.com/v1/api.json?rss_url=https%3A%2F%2Fwww.filmfestivals.com%2Ftaxonomy%2Fterm%2F30%252B31%252B32%252B33%252B34%252B35%252B36%252B6774%252B590757%2F0%2Ffeed");

    }

    @Override
    public void onItemRecommend(int check, Movies movies) {
        if(check == 1){
            RecommendedList.add(movies);
            SharedPreferences.Editor editor = recommended.edit();
            editor.putString(MyRECOMMENDED,"true").apply();
        }
        else if(check == 0){
            if(RecommendedList.contains(movies)){
                RecommendedList.remove(movies);
            }
        }
    }

    private class MovieAsyncTask extends AsyncTask<String, String, List<Movies>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            spinner = new ProgressDialog(ListActivity.this);
            spinner.setMessage("Please wait...It is downloading");
            spinner.setIndeterminate(false);
            spinner.setCancelable(false);
            spinner.show();
        }

        @Override
        protected List<Movies> doInBackground(String... strings) {
            HttpURLConnection urlConnection = null;
            List<Movies> list = new ArrayList<>();
            try {
                URL url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");

                if (urlConnection.getResponseCode() != 200) {
                    throw new IOException("Invalid response code from server: " + urlConnection.getResponseCode());
                }

                BufferedReader rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder json = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null) {
                    json.append(line);
                }

                JSONObject jsonObject = new JSONObject(json.toString());
                JSONArray itemsArray = jsonObject.getJSONArray("items");
                int count = 0;
                String title, category, imageLink;

                while (count < itemsArray.length()) {
                    JSONObject movieJson = itemsArray.getJSONObject(count);
                    title = movieJson.getString("title").replaceAll("<.*?>","");
                    category = movieJson.getString("categories").replaceAll("<.*?>","");
                    imageLink = movieJson.getString("thumbnail");
                    Movies movies = new Movies(title, category, imageLink);
                    list.add(movies);
                    count++;
                }

            } catch (Exception e) {
                e. printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return list;
        }

        @Override
        protected void onPostExecute(List<Movies> list) {
            super.onPostExecute(list);
            if(list != null) {
                spinner.hide();
                setUpList(list);
            }else {
                spinner.show();
            }

        }
    }

    private void setUpList(List<Movies> moviesList) {
        recycler_view = findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_view.setNestedScrollingEnabled(false);
        movieAdapter = new MovieAdapter(moviesList);
        recycler_view.setAdapter(movieAdapter);
        recycler_view.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }
}