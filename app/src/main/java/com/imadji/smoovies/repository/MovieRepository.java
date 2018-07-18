package com.imadji.smoovies.repository;

import com.imadji.smoovies.data.model.Movie;
import com.imadji.smoovies.data.source.local.MovieDatabase;
import com.imadji.smoovies.data.source.remote.ApiService;
import com.imadji.smoovies.data.source.remote.response.MoviesResponse;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by imadji on 7/8/2018.
 */

public class MovieRepository {
    private static MovieRepository instance;

    private final ApiService apiService;
    private final MovieDatabase database;

    private MovieRepository(final ApiService apiService, final MovieDatabase database) {
        this.apiService = apiService;
        this.database = database;
    }

    public static MovieRepository getInstance(final ApiService apiService,
                                              final MovieDatabase database) {
        if (instance == null) {
            instance = new MovieRepository(apiService, database);
        }
        return instance;
    }

    public Single<List<Movie>> loadMovies(int page) {
//        return Single.concat(loadMoviesFromDb(), loadMoviesFromApi(page))
//                .filter(movies -> !movies.isEmpty())
//                .first(new ArrayList<>());
        return loadMoviesFromApi(page);
    }

    public Single<List<Movie>> loadSimilarMovies(long movieId, int page) {
        return loadSimilarMoviesFromApi(movieId, page);
    }

    private Single<List<Movie>> loadMoviesFromApi(int page) {
        return apiService.getNowPlayingMovies(page)
                .subscribeOn(Schedulers.io())
                .map(MoviesResponse::getResults);
    }

    private Single<List<Movie>> loadSimilarMoviesFromApi(long movieId, int page) {
        return apiService.getSimilarMovies(movieId, page)
                .subscribeOn(Schedulers.io())
                .map(MoviesResponse::getResults);
    }

    private Single<List<Movie>> loadMoviesFromDb() {
        return database.getMovieDao().getAllMovies();
    }

    private void insertMoviesToDb(List<Movie> movies) {
        database.getMovieDao().insertAll(movies);
    }

}
