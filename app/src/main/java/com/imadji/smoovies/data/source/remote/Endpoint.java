package com.imadji.smoovies.data.source.remote;

import com.imadji.smoovies.data.model.Movies;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by imadji on 7/8/2018.
 */
public interface Endpoint {
    @GET("movie/now_playing")
    Flowable<Movies> getNowPlayingMovies(@Query("page") int page);

    @GET("movie/{movie_id}/similar")
    Flowable<Movies> getSimilarMovies(@Path("movie_id") long movieId, @Query("page") int page);

}
