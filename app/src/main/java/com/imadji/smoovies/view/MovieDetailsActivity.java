package com.imadji.smoovies.view;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Typeface;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v7.widget.CardView;
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

import static com.imadji.smoovies.view.MovieListActivity.EXTRA_MOVIE;

/**
 * Created by imadji on 7/10/2018.
 */

public class MovieDetailsActivity extends BaseActivity implements AppBarLayout.OnOffsetChangedListener {
    private static final String TAG = MovieDetailsActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.backdrop)
    ImageView imageBackdrop;
    @BindView(R.id.card_poster)
    CardView cardPoster;
    @BindView(R.id.poster)
    ImageView imagePoster;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.title)
    TextView textTitle;
    @BindView(R.id.overview)
    TextView textOverview;

    private MovieAdapter adapter;
    private MovieViewModelFactory viewModelFactory;
    private MovieViewModel viewModel;
    private Movie movie;

    private int scrollRange = 0;
    boolean isPosterShow = true;
    boolean isTitleShow = false;

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

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (scrollRange == 0) {
            scrollRange = appBarLayout.getTotalScrollRange();
        }

        updateMoviePosterView(scrollRange, verticalOffset);
        updateToolbarView(scrollRange, verticalOffset);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        appBarLayout.addOnOffsetChangedListener(this);
    }

    private void updateMoviePosterView(int scrollRange, int verticalOffset) {
        int percentageToAnimatePoster = 20;
        int percentage = (Math.abs(verticalOffset)) * 100 / scrollRange;

        if (percentage >= percentageToAnimatePoster && isPosterShow) {
            cardPoster.animate()
                    .scaleY(0).scaleX(0)
                    .setDuration(400)
                    .start();
            isPosterShow = false;
        }

        if (percentage <= percentageToAnimatePoster && !isPosterShow) {
            cardPoster.animate()
                    .scaleY(1).scaleX(1)
                    .start();
            isPosterShow = true;
        }
    }

    private void updateToolbarView(int scrollRange, int verticalOffset) {
        if (scrollRange + verticalOffset < 30) {
            collapsingToolbarLayout.setTitle(getString(R.string.movie_details));
            collapsingToolbarLayout.setCollapsedTitleTypeface(Typeface.DEFAULT);
            isTitleShow = true;
        } else if (isTitleShow) {
            collapsingToolbarLayout.setTitle(" ");
            isTitleShow = false;
        }
    }

    private void setupView() {
        if (getIntent() != null ) {
            Bundle data = getIntent().getExtras();
            movie = data.getParcelable(EXTRA_MOVIE);
            String titleAndReleaseYear = String.format(getString(R.string.movie_title_and_year),
                    movie.getOriginalTitle(), movie.getReleaseDate().split("-")[0]);
            textTitle.setText(titleAndReleaseYear);
            textOverview.setText(movie.getOverview());
            Glide.with(this).load(BuildConfig.TMDB_IMAGE_URL + "w780/" + movie.getBackdropPath())
                    .into(imageBackdrop);
            Glide.with(this).load(BuildConfig.TMDB_IMAGE_URL + "w342/" + movie.getPosterPath())
                    .into(imagePoster);
        }

        adapter = new MovieAdapter(1);

        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
    }
}
