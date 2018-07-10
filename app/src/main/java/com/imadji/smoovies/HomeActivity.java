package com.imadji.smoovies;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.imadji.smoovies.viewmodel.MovieViewModel;
import com.imadji.smoovies.viewmodel.MovieViewModelFactory;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = HomeActivity.class.getSimpleName();

    private MovieViewModelFactory viewModelFactory;
    private MovieViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        viewModelFactory = new MovieViewModelFactory(MyApplication.getRepository());
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MovieViewModel.class);
        viewModel.getMovies().observe(this, movies -> {
            Log.d(TAG, "movies size " + movies.size());
            if (!movies.isEmpty()) Log.d(TAG, "movie 1 " + movies.get(0).getId());
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        viewModel.loadMore();
    }
}
