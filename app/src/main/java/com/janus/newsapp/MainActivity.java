package com.janus.newsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String API_KEY = "c7ec08716ac848c39ad496c9f6f8e627";
    String API_URL = "https://newsapi.org/v2/top-headlines?country=in&apiKey="+API_KEY;

    RecyclerView newsRecycler;
    FloatingActionButton btn;
    private ProgressBar pBar;

    Switch DSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*Setting Theme for day and Night Mode*/
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }
        /*Ends Here*/

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newsRecycler = findViewById(R.id.recyclerNews);

        btn = findViewById(R.id.floatingActionButton);

        pBar = findViewById(R.id.progressBar);

        /*Setting Switch Action for ON and OFF*/
        DSwitch = findViewById(R.id.dayNight);

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            DSwitch.setChecked(true);
        } else{
            DSwitch.setChecked(false);
        }

        /*To enable Night Mode on key press*/
        DSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    restartApp();
                }else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    restartApp();
                }
            }
        });

        /*To refresh the page for new Feeds*/
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                finish();
                overridePendingTransition(0,0);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });

        /*To display the progress for when loading data from NEWS API*/
        pBar.setVisibility(View.VISIBLE);

        /* Get data from News API*/
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, API_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show(); //For Testing
                pBar.setVisibility(View.GONE);
                handleResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(MainActivity.this, error.toString(),Toast.LENGTH_SHORT).show(); //For Testing
                pBar.setVisibility(View.GONE);
                Log.e("ERROR", error.toString());
            }
        });

        MySingleTon.getInstance(getApplicationContext()).addToRequestQue(request);
    }

    /*For painting the application with Dark or Light Mode Refresh function*/
    public void restartApp(){
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

    /*
     * To accept values from JSON file, display it in each RecyclerView
    */
    private void handleResponse(JSONObject response){

        ArrayList<NewsFormat> newsArray = new ArrayList<>();

        try {
            JSONArray articleArray = response.getJSONArray("articles");

            for (int i = 0; i < articleArray.length(); i++) {

                JSONObject newsObject = (JSONObject) articleArray.get(i);

                String newsTitle = newsObject.getString("title");
                String newsDesc = newsObject.getString("description");
                String newsURL = newsObject.getString("url");
                String newsImage = newsObject.getString("urlToImage");

                NewsFormat newItem = new NewsFormat(newsTitle,newsDesc,newsImage,newsURL);
                newsArray.add(newItem);

            }

            newsRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL, false));
            newsRecycler.setAdapter(new AdapterNews(getApplicationContext(), newsArray));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}