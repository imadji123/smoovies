package com.imadji.smoovies.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.imadji.smoovies.data.model.Movie;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by imadji on 7/9/2018.
 */

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movies")
    Single<List<Movie>> getAllMovies();

    @Query("SELECT * FROM movies WHERE id = :movieId")
    Single<Movie> getMovie(long movieId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Movie> movies);

}
