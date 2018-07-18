package com.imadji.smoovies.data.source.remote.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.imadji.smoovies.data.model.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by imadji on 7/8/2018.
 */

public class MoviesResponse {
    @SerializedName("results")
    @Expose
    private List<Movie> results;

    @SerializedName("page")
    @Expose
    private long page;

    @SerializedName("total_results")
    @Expose
    private long totalResults;

    @SerializedName("total_pages")
    @Expose
    private long totalPages;

    public List<Movie> getResults() {
        if (results == null) {
            results = new ArrayList<>();
        }

        return results;
    }

    public long getPage() {
        return page;
    }

    public long getTotalResults() {
        return totalResults;
    }

    public long getTotalPages() {
        return totalPages;
    }

}
