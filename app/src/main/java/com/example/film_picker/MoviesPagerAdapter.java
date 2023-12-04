package com.example.film_picker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MoviesPagerAdapter extends RecyclerView.Adapter<MoviesPagerAdapter.ViewHolder> {

    private final ArrayList<Movie> movies;
    private int currentMovieIndex;


    public MoviesPagerAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
        this.currentMovieIndex = 0;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // A lapozáshoz mindig az aktuális film címét jelenítjük meg.
        Movie currentMovie = movies.get(currentMovieIndex);
        holder.textView.setText(currentMovie.getTitle());
    }

    @Override
    public int getItemCount() {
        return movies.isEmpty() ? 0 : 1;
    }

    public void onNextMovie() {
        currentMovieIndex = (currentMovieIndex + 1) % movies.size();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.textViewMovieTitle);
        }
    }
    public void backToSearch(View view) {
        // Bezárom ezt az Activity-t és visszatérek a MainActivity-hez
        finish();
    }

    private void finish() {
    }
}
