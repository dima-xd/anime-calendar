package com.animecalendar.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.animecalendar.R;
import com.animecalendar.beans.Anime;
import com.animecalendar.beans.AnimeDay;
import com.animecalendar.listeners.AnimeCardListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AnimeListAdapter extends RecyclerView.Adapter<AnimeListAdapter.AnimeListViewHolder> {

    private final List<AnimeDay> animeList;
    private final AnimeCardListener animeCardListener;

    @NonNull
    @Override
    public AnimeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.anime_card, parent, false);
        return new AnimeListViewHolder(view);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull AnimeListViewHolder holder, int position) {
        TextView titleView = holder.title;
        TextView numEpisodesView = holder.numEpisodes;
        TextView ratingBar = holder.rating;
        ImageView imageView = holder.image;

        Anime anime = animeList.get(position).getAnime();
        numEpisodesView.setText("Next episode: " + animeList.get(position).getNextEpisode());
        titleView.setText(anime.getName());
        if (anime.getScore().equals("0.0")) {
            ratingBar.setText("Score: N/A");
        } else {
            ratingBar.setText("Score: " + anime.getScore());
        }
        Picasso.get().load("https://shikimori.one" + anime.getImage().getPreview()).into(imageView);
        imageView.setContentDescription(anime.getName());

        holder.title.setOnClickListener(v -> animeCardListener.onAnimeClick(animeList.get(position)));
        holder.title.setOnLongClickListener(v -> {
            animeCardListener.onAnimeLongClick(animeList.get(position));
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return animeList.size();
    }

    public static class AnimeListViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView numEpisodes;
        private TextView rating;
        private ImageView image;

        public AnimeListViewHolder(@NonNull View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.anime_card_title);
            this.numEpisodes = itemView.findViewById(R.id.anime_card_episodes);
            this.rating = itemView.findViewById(R.id.anime_card_rating);
            this.image = itemView.findViewById(R.id.anime_card_image);
        }
    }

    public AnimeListAdapter(List<AnimeDay> animeList, AnimeCardListener animeCardListener) {
        this.animeList = animeList;
        this.animeCardListener = animeCardListener;
    }

    public List<AnimeDay> getAnimeList() {
        return animeList;
    }

}
