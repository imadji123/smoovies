package com.imadji.smoovies.data.source;

import com.imadji.smoovies.data.model.Movies;

import io.reactivex.Flowable;

/**
 * Created by imadji on 7/8/2018.
 */
public interface MovieDataSource {
    Flowable<Movies> getNowPlayingMovies(int page);
    Flowable<Movies> getSimilarMovies(long movieId, int page);
}
