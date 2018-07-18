package com.imadji.smoovies.view.util;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.imadji.smoovies.data.model.Movie;

import java.util.List;

/**
 * Created by imadji on 7/11/2018.
 */

public class MovieDiffCallback extends DiffUtil.Callback {
    private final List<Movie> oldMovieList;
    private final List<Movie> newMovieList;

    public MovieDiffCallback(List<Movie> oldMovieList, List<Movie> newMovieList) {
        this.oldMovieList = oldMovieList;
        this.newMovieList = newMovieList;
    }

    @Override
    public int getOldListSize() {
        return oldMovieList.size();
    }

    @Override
    public int getNewListSize() {
        return newMovieList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldMovieList.get(oldItemPosition).getId() == newMovieList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final Movie oldMovie = oldMovieList.get(oldItemPosition);
        final Movie newMovie = newMovieList.get(newItemPosition);
        return oldMovie.getId() == newMovie.getId();
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        // Implement method if you're going to use ItemAnimator
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
