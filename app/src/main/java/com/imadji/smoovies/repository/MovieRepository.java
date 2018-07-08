package com.imadji.smoovies.repository;

import com.imadji.smoovies.data.model.Movies;
import com.imadji.smoovies.data.source.MovieDataSource;
import com.imadji.smoovies.data.source.remote.ApiService;

import io.reactivex.Flowable;

/**
 * Created by imadji on 7/8/2018.
 */
public class MovieRepository implements MovieDataSource {
    private static MovieRepository instance;

    public static MovieRepository getInstance() {
        if (instance == null) {
            instance = new MovieRepository();
        }
        return instance;
    }

    @Override
    public Flowable<Movies> getNowPlayingMovies(int page) {
        return ApiService.getInstance().getNowPlayingMovies(page);
    }

    @Override
    public Flowable<Movies> getSimilarMovies(long movieId, int page) {
        return ApiService.getInstance().getSimilarMovies(movieId, page);
    }

}
