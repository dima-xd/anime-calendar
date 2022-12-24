package com.animecalendar.beans;

import android.annotation.SuppressLint;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AnimeDay {
    private String nextEpisode;
    private Date nextEpisodeAt;
    private String duration;
    private Anime anime;

    @SuppressLint("SimpleDateFormat")
    public AnimeDay(String nextEpisode, String nextEpisodeAt, String duration, String anime) throws ParseException, JSONException {
        this.nextEpisode = nextEpisode;
        this.nextEpisodeAt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(nextEpisodeAt);
        this.duration = duration;
        JSONObject jsonObject = new JSONObject(anime);
        this.anime = new Anime(
                jsonObject.getString("id"),
                jsonObject.getString("name"),
                jsonObject.getString("russian"),
                new Image(jsonObject.getString("image")),
                jsonObject.getString("url"),
                jsonObject.getString("kind"),
                jsonObject.getString("score"),
                jsonObject.getString("status"),
                jsonObject.getString("episodes"),
                jsonObject.getString("episodes_aired"),
                jsonObject.getString("aired_on"),
                jsonObject.getString("released_on")
        );
    }

    public String getNextEpisode() {
        return nextEpisode;
    }

    public Date getNextEpisodeAt() {
        return nextEpisodeAt;
    }

    public String getDuration() {
        return duration;
    }

    public Anime getAnime() {
        return anime;
    }
}
