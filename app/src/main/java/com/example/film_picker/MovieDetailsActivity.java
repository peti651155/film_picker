package com.example.film_picker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MovieDetailsActivity extends AppCompatActivity {

    private TextView movieTitleTextView;
    private Button backButton;
    private Button nextButton;
    private ArrayList<Movie> movies;
    private int currentMovieIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details); // Bizonyosodj meg róla, hogy létrehozod az activity_movie_details.xml layout fájlt

        movieTitleTextView = findViewById(R.id.movieTitleTextView); // Azonosítók helyesek legyenek az XML layoutban
        backButton = findViewById(R.id.backButton);
        nextButton = findViewById(R.id.nextButton);

        // Feltételezve, hogy a filmek listáját egy Intent-ből kapjuk meg
        movies = (ArrayList<Movie>) getIntent().getSerializableExtra("MOVIES_LIST");
        if (movies != null && !movies.isEmpty()) {
            displayMovie(); // Megjeleníti az első filmet
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Bezárja ezt az Activity-t és visszatér az előzőre
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentMovieIndex < movies.size() - 1) {
                    currentMovieIndex++;
                    displayMovie();
                } else {
                    // Itt jelezheted, ha nincs több film
                    nextButton.setEnabled(false); // Letiltjuk a gombot, ha nincs több film
                }
            }
        });
    }

    private void displayMovie() {
        if (currentMovieIndex < movies.size()) {
            Movie currentMovie = movies.get(currentMovieIndex);
            movieTitleTextView.setText(currentMovie.getTitle());
        }
    }
}
