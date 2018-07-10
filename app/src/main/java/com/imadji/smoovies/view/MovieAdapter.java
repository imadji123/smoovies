package com.imadji.smoovies.view;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.imadji.smoovies.BuildConfig;
import com.imadji.smoovies.R;
import com.imadji.smoovies.data.model.Movie;
import com.imadji.smoovies.view.util.MovieDiffCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by imadji on 7/10/2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private List<Movie> movieList;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_movie,
                parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.setItem(movie);
    }

    @Override
    public int getItemCount() {
        return movieList == null ? 0 : movieList.size();
    }

    public void setItems(final List<Movie> movies) {
        if (movieList == null) {
            movieList = new ArrayList<>();
            movieList.addAll(movies);
            notifyDataSetChanged();
        } else {
            final MovieDiffCallback diffCallback = new MovieDiffCallback(movieList, movies);
            final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
            movieList.clear();
            movieList.addAll(movies);
            diffResult.dispatchUpdatesTo(this);
            notifyDataSetChanged();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.poster)
        public ImageView imagePoster;
        @BindView(R.id.title)
        public TextView textTitle;
        @BindView(R.id.vote)
        public TextView textVote;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setItem(Movie movie) {
            Glide.with(itemView.getContext())
                    .load(BuildConfig.TMDB_IMAGE_URL + movie.getPosterPath())
                    .into(imagePoster);
            textTitle.setText(movie.getTitle());
            textVote.setText(String.valueOf(movie.getVoteAverage()));
        }
    }
}
