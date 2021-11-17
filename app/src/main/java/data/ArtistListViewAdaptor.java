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


import java.util.ArrayList;

import Utils.AppController;
import model.Artist;

public class ArtistListViewAdaptor extends ArrayAdapter<Artist> {
    private LayoutInflater layoutInflater;
    private ArrayList<Artist> data;
    private Activity activityContext;
    private int layoutResourceId;
    private ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    public ArtistListViewAdaptor(Activity context, int resource, ArrayList<Artist> objects) {
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
    public Artist getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getPosition(@Nullable Artist item) {
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
            viewHolder.artistImage = view.findViewById(R.id.artist_or_cover_image_detail);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.artist = data.get(position);
        // Display Data
        viewHolder.name.setText("Artist: "+viewHolder.artist.getName());
        viewHolder.playCounts.setText("Play counts: "+viewHolder.artist.getPlayCounts());
        viewHolder.listeners.setText("Listeners: "+viewHolder.artist.getListeners());
        viewHolder.url.setText(R.string.artist_lastFM_page);
        viewHolder.url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(viewHolder.artist.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                activityContext.startActivity(intent);
            }
        });
        viewHolder.artistImage.setImageUrl(viewHolder.artist.getImageUrls().get("large"),imageLoader);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activityContext, ArtistDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("artist_object", viewHolder.artist);
                intent.putExtras(bundle);
                activityContext.startActivity(intent);
            }
        });
        return view;
    }

    private class ViewHolder{
        Artist artist;
        TextView name;
        TextView playCounts;
        TextView listeners;
        TextView url;
        NetworkImageView artistImage;

    }
}
