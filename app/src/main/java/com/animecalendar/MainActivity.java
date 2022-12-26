package com.animecalendar;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.animecalendar.adapters.AnimeListAdapter;
import com.animecalendar.beans.Anime;
import com.animecalendar.beans.AnimeDay;
import com.animecalendar.databinding.ActivityMainBinding;
import com.animecalendar.listeners.AnimeCardListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private final Context context = this;

    private final List<AnimeDay> animeDays = new ArrayList<>();
    private final List<AnimeDay> chosenDayAnime = new ArrayList<>();

    private final OkHttpClient client = new OkHttpClient();

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadCalendar();

        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        CalendarView calendar = (CalendarView) findViewById(R.id.calendar);
        calendar.setMinDate(Calendar.getInstance().getTime().getTime());

        RecyclerView recyclerView = binding.recycler;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        AnimeListAdapter adapter = new AnimeListAdapter(chosenDayAnime, new AnimeCardListener() {
            @Override
            public void onAnimeClick(AnimeDay animeDay) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://myanimelist.net/anime/" + animeDay.getAnime().getId()));
                startActivity(browserIntent);
            }

            @Override
            public void onAnimeLongClick(AnimeDay animeDay) {
                Anime anime = animeDay.getAnime();
                Calendar beginTime = Calendar.getInstance();
                beginTime.setTime(animeDay.getNextEpisodeAt());

                Intent intent = new Intent(Intent.ACTION_INSERT)
                        .setData(Events.CONTENT_URI)
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                        .putExtra(Events.TITLE, anime.getName() + " episode " + animeDay.getNextEpisode() + " airs");
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

        calendar.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            chosenDayAnime.clear();

            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (AnimeDay animeDay : animeDays) {
                try {
                    if (isSameDay(animeDay.getNextEpisodeAt(), sdf.parse(year + "-" + (month + 1) + "-" + dayOfMonth))) {
                        chosenDayAnime.add(animeDay);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            adapter.notifyDataSetChanged();
        });
    }

    private void loadCalendar() {
        final String URL = "https://shikimori.one/api/calendar";

        Request request = new Request.Builder()
                .url(URL)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String bodyString = Objects.requireNonNull(response.body()).string();
                    try {
                        JSONArray jsonArray = new JSONArray(bodyString);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            animeDays.add(new AnimeDay(
                                    jsonObject.getString("next_episode"),
                                    jsonObject.getString("next_episode_at"),
                                    jsonObject.getString("duration"),
                                    jsonObject.getString("anime")
                            ));
                        }

                        CalendarView calendar = (CalendarView) findViewById(R.id.calendar);
                        Date maxDate = animeDays.get(animeDays.size() - 1).getNextEpisodeAt();
                        calendar.setMaxDate(maxDate.getTime());
                    } catch (JSONException | ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                PackageManager packageManager = context.getPackageManager();
                Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
                ComponentName componentName = intent.getComponent();
                Intent mainIntent = Intent.makeRestartActivityTask(componentName);
                context.startActivity(mainIntent);
                Runtime.getRuntime().exit(0);
            }
        });
    }

    private boolean isSameDay(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
                && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
                && calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH);
    }

}