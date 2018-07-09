package com.imadji.smoovies.data.source.remote;

import com.imadji.smoovies.data.source.remote.response.MoviesResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by imadji on 7/8/2018.
 */

public interface ApiService {
    @GET("movie/now_playing")
    Single<MoviesResponse> getNowPlayingMovies(@Query("page") int page);

    @GET("movie/{movie_id}/similar")
    Single<MoviesResponse> getSimilarMovies(@Path("movie_id") long movieId, @Query("page") int page);
}
