package com.imadji.smoovies.data.source.remote;

import com.imadji.smoovies.data.model.Movies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by imadji on 7/8/2018.
 */
public interface Endpoint {
    @GET("movie/now_playing")
    Call<Movies> getNowPlayingMovies(@Query("page") int page);

    @GET("movie/{movie_id}/similar")
    Call<Movies> getSimilarMovies(@Path("movie_id") long movieId, @Query("page") int page);

}
