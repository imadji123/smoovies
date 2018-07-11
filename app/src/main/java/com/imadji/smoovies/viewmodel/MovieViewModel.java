package com.imadji.smoovies.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.imadji.smoovies.data.model.Movie;
import com.imadji.smoovies.repository.MovieRepository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by imadji on 7/9/2018.
 */

public class MovieViewModel extends ViewModel {
    private static final String TAG = MovieViewModel.class.getSimpleName();

    private final MovieRepository repository;

    private Disposable disposable;
    private MutableLiveData<List<Movie>> movies;
    private List<Movie> currentMovies;
    private int page;
    private boolean loading;
    private boolean success;

    public MovieViewModel(final MovieRepository repository) {
        Log.d(TAG, "Instantiate MovieViewModel");
        this.repository = repository;
        this.page = 1;
        this.loading = false;
        this.success = false;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "onCleared()");
        if (disposable != null) disposable.dispose();
    }

    public LiveData<List<Movie>> getMovies() {
        Log.d(TAG, "getMovies()");
        if (movies == null) {
            this.movies = new MutableLiveData<>();
            this.currentMovies = new ArrayList<>();
        }
        if (!success) loadMovies();

        return movies;
    }

    public LiveData<List<Movie>> getSimilarMovies(Movie movie) {
        Log.d(TAG, "getSimilarMovies() id " + movie.getId());
        if (movies == null) {
            this.movies = new MutableLiveData<>();
            this.currentMovies = new ArrayList<>();
        }
        if (!success) loadSimilarMovies(movie.getId());
        return movies;
    }

    public void loadMore() {
        Log.d(TAG, "loadMore()");
        if (loading) return; // need to handle page == totalPage
        if (success) page++;
        loadMovies();
    }

    private void loadMovies() {
        Log.d(TAG, "loadMovies() page " + page);
        if (loading) return;
        loading = true;
        disposable = repository.loadMovies(page)
                .subscribeOn(Schedulers.io())
                .subscribe(this::onSuccess, this::onError);
    }

    private void loadSimilarMovies(long movieId) {
        Log.d(TAG, "loadMovies() page " + page);
        if (loading) return;
        loading = true;
        disposable = repository.loadSimilarMovies(movieId, page)
                .subscribeOn(Schedulers.io())
                .subscribe(this::onSuccess, this::onError);
    }

    private void onSuccess(List<Movie> movies) {
        Log.d(TAG, "onSuccess()");
        loading = false;
        success = true;
        this.currentMovies.addAll(movies);
        this.movies.postValue(currentMovies);
    }

    private void onError(Throwable throwable) {
        Log.d(TAG, "onError() " + throwable.getMessage());
        loading = false;
        success = false;
    }

}
