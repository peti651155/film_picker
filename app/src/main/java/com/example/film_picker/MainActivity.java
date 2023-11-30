package com.example.film_picker;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private NumberPicker yearPicker;
    private RatingBar ratingBar;
    private Button searchButton;
    private ListView listView;
    private Spinner genreSpinner;
    private ArrayAdapter<String> movieTitlesAdapter;
    private ArrayList<String> movieTitles = new ArrayList<>();
    private Map<String, Integer> genreMap = new HashMap<>();
    private ArrayList<String> genreNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(20, 50, 20, 20);
        mainLayout.setBackgroundColor(Color.parseColor("#5cb8ff"));

        TextView titleTextView = new TextView(this);
        titleTextView.setText("Random Movie Picker");
        titleTextView.setBackgroundColor(Color.parseColor("#5cb8ff"));
        titleTextView.setPadding(20, 20, 20, 20);
        mainLayout.addView(titleTextView);

        // Feldolgozzuk a műfajokat és feltöltjük a Spinner-t
        processGenres("{\"genres\":[{\"id\":28,\"name\":\"Akció\"},{\"id\":12,\"name\":\"Kaland\"},{\"id\":16,\"name\":\"Animáció\"},{\"id\":35,\"name\":\"Vígjáték\"},{\"id\":80,\"name\":\"Bűnügyi\"},{\"id\":99,\"name\":\"Dokumentumfilm\"},{\"id\":18,\"name\":\"Dráma\"},{\"id\":10751,\"name\":\"Családi\"},{\"id\":14,\"name\":\"Fantasy\"},{\"id\":36,\"name\":\"Történelmi\"},{\"id\":27,\"name\":\"Horror\"},{\"id\":10402,\"name\":\"Zene\"},{\"id\":9648,\"name\":\"Misztikus\"},{\"id\":10749,\"name\":\"Romantikus\"},{\"id\":878,\"name\":\"Sci-Fi\"},{\"id\":10770,\"name\":\"TV film\"},{\"id\":53,\"name\":\"Thriller\"},{\"id\":10752,\"name\":\"Háborús\"},{\"id\":37,\"name\":\"Western\"}]}");
        genreSpinner = new Spinner(this);
        ArrayAdapter<String> genreAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genreNames);
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genreSpinner.setAdapter(genreAdapter);
        mainLayout.addView(genreSpinner);

        yearPicker = new NumberPicker(this);
        yearPicker.setMinValue(1990);
        yearPicker.setMaxValue(2023);
        mainLayout.addView(yearPicker);

        ratingBar = new RatingBar(this);
        ratingBar.setNumStars(8);
        ratingBar.setStepSize(0.50f);
        mainLayout.addView(ratingBar);

        searchButton = new Button(this);
        searchButton.setText("Filmek listázása");
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Itt hajtsd végre az API hívást a kiválasztott műfaj azonosítója alapján
                searchMovies();
            }
        });
        mainLayout.addView(searchButton);

        listView = new ListView(this);
        movieTitlesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, movieTitles);
        listView.setAdapter(movieTitlesAdapter);
        mainLayout.addView(listView);

        setContentView(mainLayout);
    }

    private void processGenres(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray genresArray = jsonObject.getJSONArray("genres");
            for (int i = 0; i < genresArray.length(); i++) {
                JSONObject genreObject = genresArray.getJSONObject(i);
                int id = genreObject.getInt("id");
                String name = genreObject.getString("name");
                genreMap.put(name, id);
                genreNames.add(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchMovies() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String selectedGenreName = genreSpinner.getSelectedItem().toString();
                    Integer selectedGenreId = genreMap.get(selectedGenreName);
                    if (selectedGenreId == null) {
                        return;
                    }

                    int selectedYear = yearPicker.getValue();
                    final float selectedRating = ratingBar.getRating();

                    String response = ApiManager.performApiCall(selectedYear, selectedGenreId.toString(), selectedRating);

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray resultsArray = jsonObject.getJSONArray("results");
                    movieTitles.clear();

                    for (int i = 0; i < resultsArray.length(); i++) {
                        JSONObject movieObject = resultsArray.getJSONObject(i);
                        float movieRating = (float) movieObject.getDouble("vote_average");
                        if (movieRating >= selectedRating) {
                            String title = movieObject.getString("title");
                            movieTitles.add(title);
                        }
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            movieTitlesAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


}
