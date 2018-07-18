package com.imadji.smoovies.data.source.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.imadji.smoovies.data.model.Movie;

/**
 * Created by imadji on 7/9/2018.
 */

@Database(entities = { Movie.class }, version = 1)
public abstract class MovieDatabase extends RoomDatabase {
    private static final String DB_NAME = "smooviesDatabase.db";

    private static MovieDatabase instance;

    public static MovieDatabase getInstance(final Context context) {
        if (instance == null) {
            instance = createDatabase(context);
        }
        return instance;
    }

    private static MovieDatabase createDatabase(final Context context) {
        return Room.databaseBuilder(context, MovieDatabase.class, DB_NAME).build();
    }

    public abstract MovieDao getMovieDao();

}
