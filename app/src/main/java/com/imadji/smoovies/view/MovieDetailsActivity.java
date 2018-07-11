package com.imadji.smoovies.view;

import android.arch.lifecycle.ViewModelProviders;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.imadji.smoovies.BuildConfig;
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

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.backdrop)
    ImageView imageBackdrop;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.title)
    TextView textTitle;
    @BindView(R.id.release_year)
    TextView textReleaseYear;
    @BindView(R.id.description)
    TextView textDescription;

    private MovieAdapter adapter;
    private MovieViewModelFactory viewModelFactory;
    private MovieViewModel viewModel;

    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        setupToolbar();
        setupView();

        viewModelFactory = new MovieViewModelFactory(MyApplication.getRepository());
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MovieViewModel.class);
        viewModel.getSimilarMovies(movie).observe(this, movies -> {
            Log.d(TAG, "movies size " + movies.size());
            adapter.setItems(movies);
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(getString(R.string.movie_details));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    private void setupView() {
        if (getIntent() != null ) {
            Bundle data = getIntent().getExtras();
            movie = data.getParcelable("movie");
            textTitle.setText(movie.getOriginalTitle());
            textReleaseYear.setText(movie.getReleaseDate().split("-")[0]);
            textDescription.setText(movie.getOverview());
            Glide.with(this).load(BuildConfig.TMDB_IMAGE_URL + "w780/" +movie.getBackdropPath())
                    .into(imageBackdrop);
        }

        adapter = new MovieAdapter(1);

        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
    }
}
