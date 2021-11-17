package com.example.musiceventfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.w3c.dom.Text;

import Utils.AppController;
import model.Track;

public class TrackDetailsActivity extends AppCompatActivity {
    private Track track;
    private TextView name;
    private TextView playCount;
    private TextView listeners;
    private NetworkImageView image;
    private TextView artistName;
    private ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private TextView duration;
    private Button urlListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_details);
        track = (Track) getIntent().getSerializableExtra("track_object");
        name = findViewById(R.id.name_text);
        playCount = findViewById(R.id.playcounts);
        listeners = findViewById(R.id.listeners_detail);
        image = findViewById(R.id.artist_or_cover_image_detail);
        artistName = findViewById(R.id.artist_name);
        duration = findViewById(R.id.duration);
        urlListener = findViewById(R.id.url_text);

        name.setText(track.getName());
        playCount.setText("Play count: "+track.getPlayCounts());
        listeners.setText("Listeners: "+track.getListeners());
        duration.setText("Duration: "+track.getDuration());
        artistName.setText("Artist: "+track.getArtist().getName());
        image.setImageUrl(track.getImageUrls().get("extralarge"),imageLoader);
        urlListener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(track.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }
}