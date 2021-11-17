package data;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.musiceventfinder.ArtistDetailsActivity;
import com.example.musiceventfinder.R;
import com.example.musiceventfinder.TrackDetailsActivity;


import java.util.ArrayList;

import Utils.AppController;
import model.Track;


public class TrackListViewAdaptor extends ArrayAdapter<Track> {
    private LayoutInflater layoutInflater;
    private ArrayList<Track> data;
    private Activity activityContext;
    private int layoutResourceId;
    private ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    public TrackListViewAdaptor(Activity context, int resource, ArrayList<Track> objects) {
        super(context, resource, resource, objects);
        data = objects;
        activityContext = context;
        layoutResourceId = resource;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Nullable
    @Override
    public Track getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getPosition(@Nullable Track item) {
        return super.getPosition(item);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        final ViewHolder viewHolder;
        if (view == null){
            layoutInflater = LayoutInflater.from(activityContext);
            view = layoutInflater.inflate(layoutResourceId, parent, false);
            viewHolder = new ViewHolder();
            //get references to our views
            viewHolder.name = view.findViewById(R.id.name_text);
            viewHolder.playCounts = view.findViewById(R.id.playcounts);
            viewHolder.listeners = view.findViewById(R.id.listeners_detail);
            viewHolder.url = view.findViewById(R.id.url_text);
            viewHolder.trackImage = view.findViewById(R.id.artist_or_cover_image_detail);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.track = data.get(position);
        // Display Data
        viewHolder.name.setText("Track: "+viewHolder.track.getName());
        viewHolder.playCounts.setText("Play count: "+viewHolder.track.getPlayCounts());
        viewHolder.listeners.setText("Listeners: "+viewHolder.track.getListeners());
        viewHolder.url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(viewHolder.track.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                activityContext.startActivity(intent);
            }
        });
        viewHolder.trackImage.setImageUrl(viewHolder.track.getImageUrls().get("large"),imageLoader);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activityContext, TrackDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("track_object", viewHolder.track);
                intent.putExtras(bundle);
                activityContext.startActivity(intent);
            }
        });
        return view;
    }

    private class ViewHolder{
        Track track;
        TextView name;
        TextView playCounts;
        TextView listeners;
        TextView url;
        TextView duration;
        TextView artistName;
        NetworkImageView trackImage;

    }
}
