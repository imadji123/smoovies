package com.imadji.smoovies.repository;

import com.imadji.smoovies.data.model.Movie;
import com.imadji.smoovies.data.source.local.MovieDatabase;
import com.imadji.smoovies.data.source.remote.ApiService;
import com.imadji.smoovies.data.source.remote.response.MoviesResponse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by imadji on 7/8/2018.
 */

public class MovieRepository {
    private static MovieRepository instance;

    private final MovieDatabase database;

    private MovieRepository(MovieDatabase database) {
        this.database = database;
    }

    public static MovieRepository getInstance(final MovieDatabase database) {
        if (instance == null) {
            instance = new MovieRepository(database);
        }
        return instance;
    }

    public Single<List<Movie>> loadMovies(int page) {
        return Single.concat(loadMoviesFromDb(), loadMoviesFromApi(page))
                .filter(movies -> !movies.isEmpty())
                .first(new ArrayList<>());
    }

    public Single<Movie> loadMovieDetails(long movieId) {
        return database.getMovieDao().getMovie(movieId)
                .subscribeOn(Schedulers.io());
    }

    private Single<List<Movie>> loadMoviesFromDb() {
        return database.getMovieDao().getAllMovies()
                .subscribeOn(Schedulers.io());
    }

    private Single<List<Movie>> loadMoviesFromApi(int page) {
        return ApiService.getInstance().getNowPlayingMovies(page)
                .subscribeOn(Schedulers.io())
                .map(MoviesResponse::getResults)
                .doOnSuccess(this::insertMoviesToDb);
    }

    private Single<List<Movie>> getSimilarMovies(long movieId, int page) {
        return ApiService.getInstance().getSimilarMovies(movieId, page)
                .subscribeOn(Schedulers.io())
                .map(MoviesResponse::getResults);
    }

    private void insertMoviesToDb(List<Movie> movies) {
        database.getMovieDao().insertAll(movies);
    }

}
