package model;

import java.io.Serializable;
import java.util.HashMap;

public class Track implements Serializable {
    private static final long id = 1L;
    private String name;
    private String duration;
    private String playCounts;
    private String listeners;
    private String mbId;
    private String url;
    private boolean streamable;
    private HashMap<String,String> imageUrls;
    private Artist artist;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPlayCounts() {
        return playCounts;
    }

    public void setPlayCounts(String playCounts) {
        this.playCounts = playCounts;
    }

    public String getListeners() {
        return listeners;
    }

    public void setListeners(String listeners) {
        this.listeners = listeners;
    }

    public String getMbId() {
        return mbId;
    }

    public void setMbId(String mbId) {
        this.mbId = mbId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isStreamable() {
        return streamable;
    }

    public void setStreamable(boolean streamable) {
        this.streamable = streamable;
    }

    public HashMap<String, String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(HashMap<String, String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }
}
