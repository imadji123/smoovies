package com.imadji.smoovies.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ProgressBar;

import com.imadji.smoovies.MyApplication;
import com.imadji.smoovies.R;
import com.imadji.smoovies.view.util.GridSpacingItemDecoration;
import com.imadji.smoovies.viewmodel.MovieViewModel;
import com.imadji.smoovies.viewmodel.MovieViewModelFactory;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by imadji on 7/10/2018.
 */

public class MovieListActivity extends BaseActivity {
    private static final String TAG = MovieListActivity.class.getSimpleName();
    protected static final String EXTRA_MOVIE = "com.imadji.smoovies.extras.EXTRA_MOVIE";

    @BindView(R.id.coordinator)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private MovieAdapter adapter;
    private MovieViewModelFactory viewModelFactory;
    private MovieViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        ButterKnife.bind(this);

        setupToolbar();
        setupView();

        viewModelFactory = new MovieViewModelFactory(MyApplication.getRepository());
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MovieViewModel.class);
        viewModel.getMovies().observe(this, movies -> {
            Log.d(TAG, "movies size " + movies.size());
            adapter.setItems(movies);
        });
        viewModel.getConnectionStatus().observe(this, status -> {
            Log.d(TAG, "connection status " + status.isSuccess());
            hideProgress();
            if (!status.isSuccess()) {
                showConnectionErrorMessage();
                return;
            }
            recyclerView.setVisibility(View.VISIBLE);
        });
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
    }

    private void setupView() {
        adapter = new MovieAdapter(0);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(5), true));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(4, dpToPx(5), true));
        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(scrollListener);
    }

    private int dpToPx(int dp) {
        Resources resources = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, resources.getDisplayMetrics()));
    }

    private void showConnectionErrorMessage() {
        Snackbar.make(coordinatorLayout, getString(R.string.connection_error), Snackbar.LENGTH_LONG)
                .setAction(R.string.action_retry, view -> {
                    viewModel.loadMore();
                })
                .setActionTextColor(getResources().getColor(R.color.white))
                .show();
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            if (linearLayoutManager.getItemCount() == (linearLayoutManager.findLastVisibleItemPosition() + 1)) {
                viewModel.loadMore();
            }
        }
    };

}
