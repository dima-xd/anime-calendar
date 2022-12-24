package com.animecalendar.beans;

public class Anime {
    private String id;
    private String name;
    private String russian;
    private Image image;
    private String url;
    private String kind;
    private String score;
    private String status;
    private String episodes;
    private String episodesAired;
    private String airedOn;
    private String releasedOn;

    public Anime(String id, String name, String russian, Image image, String url, String kind, String score, String status, String episodes, String episodesAired, String airedOn, String releasedOn) {
        this.id = id;
        this.name = name;
        this.russian = russian;
        this.image = image;
        this.url = url;
        this.kind = kind;
        this.score = score;
        this.status = status;
        this.episodes = episodes;
        this.episodesAired = episodesAired;
        this.airedOn = airedOn;
        this.releasedOn = releasedOn;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRussian() {
        return russian;
    }

    public Image getImage() {
        return image;
    }

    public String getUrl() {
        return url;
    }

    public String getKind() {
        return kind;
    }

    public String getScore() {
        return score;
    }

    public String getStatus() {
        return status;
    }

    public String getEpisodes() {
        return episodes;
    }

    public String getEpisodesAired() {
        return episodesAired;
    }

    public String getAiredOn() {
        return airedOn;
    }

    public String getReleasedOn() {
        return releasedOn;
    }
}
