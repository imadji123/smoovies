package com.imadji.smoovies.view;

import android.content.Intent;
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

import static com.imadji.smoovies.view.MovieListActivity.EXTRA_MOVIE;

/**
 * Created by imadji on 7/10/2018.
 */

public class MovieAdapter extends RecyclerView.Adapter {

    private final int itemType;
    private List<Movie> movieList;

    // 0: MovieViewHolder; 1: PosterViewHolder
    public MovieAdapter(int itemType) {
        this.itemType = itemType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (this.itemType == 0) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie,
                    parent, false);
            return new MovieViewHolder(itemView);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_poster,
                    parent, false);
            return new PosterViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Movie movie = movieList.get(position);

        if (this.itemType == 0) {
            ((MovieViewHolder) holder).setItem(movie);
        } else {
            ((PosterViewHolder) holder).setItem(movie);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), MovieDetailsActivity.class);
            intent.putExtra(EXTRA_MOVIE, movie);
            holder.itemView.getContext().startActivity(intent);
        });
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
            MovieDiffCallback diffCallback = new MovieDiffCallback(movieList, movies);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
            movieList.clear();
            movieList.addAll(movies);
            diffResult.dispatchUpdatesTo(this);
            notifyDataSetChanged();
        }
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.poster)
        public ImageView imagePoster;
        @BindView(R.id.title)
        public TextView textTitle;
        @BindView(R.id.vote)
        public TextView textVote;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setItem(Movie movie) {
            Glide.with(itemView.getContext())
                    .load(BuildConfig.TMDB_IMAGE_URL + "w342/" + movie.getPosterPath())
                    .into(imagePoster);
            textTitle.setText(movie.getTitle());
            textVote.setText(String.valueOf(movie.getVoteAverage()));
        }
    }

    public static class PosterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.poster)
        public ImageView imagePoster;

        public PosterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setItem(Movie movie) {
            Glide.with(itemView.getContext())
                    .load(BuildConfig.TMDB_IMAGE_URL + "w185/" + movie.getPosterPath())
                    .into(imagePoster);
        }
    }
}
