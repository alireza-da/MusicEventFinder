package com.example.musiceventfinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import Utils.AppController;
import Utils.LastFMAccDetail;
import Utils.Prefs;
import data.ArtistListViewAdaptor;
import data.TrackListViewAdaptor;
import model.Artist;
import model.Track;

public class MainActivity extends AppCompatActivity {
    private String url = "https://ws.audioscrobbler.com/2.0/?method=";
    private ListView listView;
    private ArtistListViewAdaptor artistListViewAdaptor;
    private TrackListViewAdaptor trackListViewAdaptor;
    private ArrayList<Track> tracksArrayList;
    private ArrayList<Artist> artistsArrayList;
    private TextView chartFilterText;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chartFilterText = findViewById(R.id.chart_filter_text);
        artistsArrayList = new ArrayList<>();
        tracksArrayList = new ArrayList<>();
        listView = findViewById(R.id.chart_list);
        Prefs prefs = new Prefs(MainActivity.this);
        chartFilterText.setText("Chart Filter: "+prefs.getChartType());
        showChart(prefs.getChartType());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.change_chart_filter){
            changeChartFilter();
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeChartFilter(){

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        RadioButton artistFilter = new RadioButton(MainActivity.this);
        RadioButton trackFilter = new RadioButton(MainActivity.this);
        artistFilter.setText("Artists");
        trackFilter.setText("Tracks");
        final RadioGroup radioGroup = new RadioGroup(MainActivity.this);
        radioGroup.addView(artistFilter);
        radioGroup.addView(trackFilter);
        alertDialog.setView(radioGroup);
        alertDialog.setTitle("Change Chart Filter");
        alertDialog.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Prefs filterPreference = new Prefs(MainActivity.this);
                if (radioGroup.getCheckedRadioButtonId() == 1){
                    filterPreference.setChartType("Artist");
                    showChart("Artist");
                }else{
                    filterPreference.setChartType("Tracks");
                    showChart("Tracks");
                }
                chartFilterText.setText("Chart Filter: "+filterPreference.getChartType());
            }
        });
        alertDialog.show();
    }

    private void getTracks(){
        trackListViewAdaptor = new TrackListViewAdaptor(MainActivity.this, R.layout.list_row, tracksArrayList);
        listView.setAdapter(trackListViewAdaptor);
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading Data ...");
        progressDialog.show();

        tracksArrayList.clear();
        String getTracksUrl = url + "chart.gettoptracks" + "&api_key=" + LastFMAccDetail.API_KEY + "&format=json";
        JsonObjectRequest chartRequest = new JsonObjectRequest(Request.Method.GET, getTracksUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.cancel();
                progressDialog = null;
                try {
                    JSONObject chart = response.getJSONObject("tracks");
                    JSONArray tracks = chart.getJSONArray("track");
                    for (int i = 0; i< tracks.length();i++){
                        JSONObject trackObject = tracks.getJSONObject(i);
                        String trackName = trackObject.getString("name");
                        String duration = trackObject.getString("duration");
                        String playCount = trackObject.getString("playcount");
                        String listeners = trackObject.getString("listeners");
                        String mbId = trackObject.getString("mbid");
                        String url = trackObject.getString("url");
                        String streamable = trackObject.getJSONObject("streamable").getString("fulltrack");
                        JSONObject artistObject = trackObject.getJSONObject("artist");
                        String artistName = artistObject.getString("name");
                        String artistUrl = artistObject.getString("url");

                        //get image urls
                        JSONArray imageList = trackObject.getJSONArray("image");
                        HashMap<String , String> imageAlbum = new HashMap<>();
                        for (int j = 0 ; j < imageList.length(); j++){
                            JSONObject image = imageList.getJSONObject(j);
                            imageAlbum.put(image.getString("size"), image.getString("#text"));
                        }

                        Track track = new Track();
                        track.setArtist(new Artist());
                        track.getArtist().setName(artistName);
                        track.getArtist().setUrl(artistUrl);
                        track.setDuration(duration);
                        track.setImageUrls(imageAlbum);
                        track.setListeners(listeners);
                        track.setMbId(mbId);
                        track.setName(trackName);
                        track.setPlayCounts(playCount);
                        if (streamable.equals("0")){
                            track.setStreamable(false);
                        }else track.setStreamable(true);
                        track.setUrl(url);
                        tracksArrayList.add(track);
                        trackListViewAdaptor.notifyDataSetChanged();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        AppController.getInstance().addToRequestQueue(chartRequest);
    }

    public void getArtists(){
        artistListViewAdaptor = new ArtistListViewAdaptor(MainActivity.this, R.layout.list_row, artistsArrayList);
        listView.setAdapter(artistListViewAdaptor);
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading Data ...");
        progressDialog.show();
        artistsArrayList.clear();
        String getTracksUrl = url + "chart.gettopartists" + "&api_key=" + LastFMAccDetail.API_KEY + "&format=json";
        JsonObjectRequest chartRequest = new JsonObjectRequest(Request.Method.GET, getTracksUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.cancel();
                progressDialog = null;
                try {
                    JSONObject chart = response.getJSONObject("artists");
                    JSONArray artistsArray = chart.getJSONArray("artist");
                    for (int i =0; i<artistsArray.length();i++){
                        JSONObject artistObject = artistsArray.getJSONObject(i);
                        String artistName =  artistObject.getString("name");
                        String playCount = artistObject.getString("playcount");
                        String listeners = artistObject.getString("listeners");
                        String mbId = artistObject.getString("mbid");
                        String url = artistObject.getString("url");

                        JSONArray imageList = artistObject.getJSONArray("image");
                        HashMap<String , String> imageAlbum = new HashMap<>();
                        for (int j = 0 ; j < imageList.length(); j++){
                            JSONObject image = imageList.getJSONObject(j);
                            imageAlbum.put(image.getString("size"), image.getString("#text"));
                        }

                        Artist artist = new Artist();
                        artist.setUrl(url);
                        artist.setName(artistName);
                        artist.setMbId(mbId);
                        artist.setPlayCounts(playCount);
                        artist.setListeners(listeners);
                        artist.setImageUrls(imageAlbum);

                        artistsArrayList.add(artist);
                        artistListViewAdaptor.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        AppController.getInstance().addToRequestQueue(chartRequest);
    }
    public void showChart(String filter){
        if (filter.equals("Artist")){
            getArtists();
        }
        else getTracks();
    }
}