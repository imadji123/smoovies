package com.imadji.smoovies.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;

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

public class MovieListActivity extends AppCompatActivity {
    private static final String TAG = MovieListActivity.class.getSimpleName();

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private MovieAdapter adapter;
    private MovieViewModelFactory viewModelFactory;
    private MovieViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        ButterKnife.bind(this);

        setupViews();

        viewModelFactory = new MovieViewModelFactory(MyApplication.getRepository());
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MovieViewModel.class);
        viewModel.getMovies().observe(this, movies -> {
            Log.d(TAG, "movies size " + movies.size());
            adapter.setItems(movies);
        });
    }

    private void setupViews() {
        adapter = new MovieAdapter();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(4, dpToPx(10), true));
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
