package com.imadji.smoovies.view;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.imadji.smoovies.MyApplication;
import com.imadji.smoovies.R;
import com.imadji.smoovies.data.model.Movie;
import com.imadji.smoovies.viewmodel.MovieViewModel;
import com.imadji.smoovies.viewmodel.MovieViewModelFactory;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by imadji on 7/10/2018.
 */

public class MovieDetailsActivity extends AppCompatActivity {
    private static final String TAG = MovieDetailsActivity.class.getSimpleName();

    private MovieViewModelFactory viewModelFactory;
    private MovieViewModel viewModel;

    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        setupToolbar();
        setupViews();

        viewModelFactory = new MovieViewModelFactory(MyApplication.getRepository());
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MovieViewModel.class);
        viewModel.getSimilarMovies(movie).observe(this, movies -> {
            Log.d(TAG, "movies size " + movies.size());
        });

    }

    private void setupToolbar() {

    }

    private void setupViews() {
        if (getIntent() != null ) {
            Bundle data = getIntent().getExtras();
            movie = data.getParcelable("movie");
        }

    }
}
