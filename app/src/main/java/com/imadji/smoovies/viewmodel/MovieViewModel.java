package com.imadji.smoovies.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.imadji.smoovies.data.model.Movie;
import com.imadji.smoovies.repository.MovieRepository;

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
    private int page;
    private boolean loading;

    public MovieViewModel(final MovieRepository repository) {
        Log.d(TAG, "Instantiate MovieViewModel");
        this.repository = repository;
        this.page = 1;
        this.loading = false;
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
            movies = new MutableLiveData<>();
            loadMovies();
        }
        return movies;
    }

    public void loadMore() {
        Log.d(TAG, "loadMore()");
        if (loading) return;
        page++;
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

    private void onSuccess(List<Movie> movies) {
        Log.d(TAG, "onSuccess()");
        loading = false;
        this.movies.postValue(movies);
    }

    private void onError(Throwable throwable) {
        Log.d(TAG, "onError() " + throwable.getMessage());
        loading = false;
    }

}
