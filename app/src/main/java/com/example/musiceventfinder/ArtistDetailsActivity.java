package com.example.musiceventfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import Utils.AppController;
import model.Artist;
import model.Track;

public class ArtistDetailsActivity extends AppCompatActivity {
    private Artist artist;
    private TextView name;
    private TextView playCount;
    private TextView listeners;
    private NetworkImageView image;
    private TextView artistName;
    private ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private TextView duration;
    private TextView urlListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_details);
        urlListener = findViewById(R.id.url_text);
        artist = (Artist) getIntent().getSerializableExtra("artist_object");
        name = findViewById(R.id.name_text);
        playCount = findViewById(R.id.playcounts);
        listeners = findViewById(R.id.listeners_detail);
        image = findViewById(R.id.artist_or_cover_image_detail);
        name.setText(artist.getName());
        playCount.setText("Play count: "+artist.getPlayCounts());
        listeners.setText("Listeners: "+artist.getListeners());
        image.setImageUrl(artist.getImageUrls().get("extralarge"), imageLoader);
        urlListener.setText("Artist LastFM Page");
        urlListener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(artist.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }
}