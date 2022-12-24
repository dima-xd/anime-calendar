package com.animecalendar.listeners;

import com.animecalendar.beans.AnimeDay;

public interface AnimeCardListener {
    void onAnimeClick(AnimeDay animeDay);
    void onAnimeLongClick(AnimeDay animeDay);
}
