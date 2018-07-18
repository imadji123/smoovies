package com.imadji.smoovies.viewmodel;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.imadji.smoovies.repository.MovieRepository;

/**
 * Created by imadji on 7/9/2018.
 */

public class MovieViewModelFactory implements ViewModelProvider.Factory {
    private final MovieRepository repository;

    public MovieViewModelFactory(MovieRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public MovieViewModel create(@NonNull Class modelClass) {
        return new MovieViewModel(repository);
    }

}
