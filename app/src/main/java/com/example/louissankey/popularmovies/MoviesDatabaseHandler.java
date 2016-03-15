package com.example.louissankey.popularmovies;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.louissankey.popularmovies.model.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by louissankey on 3/11/16.
 */

//first I followed a tutorial here on how to create the database:
//http://www.androidhive.info/2011/11/android-sqlite-database-tutorial/


public class MoviesDatabaseHandler extends SQLiteOpenHelper {

    private ContentResolver mContentResolver;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "favoriteMovies.db";
    public static final String KEY_MOVIE_ID = "movieId";
    public static final String TABLE_MOVIE = "movies";
    public static final String KEY_TITLE = "title";
    public static final String KEY_POSTER_URL = "posterUrl";
    public static final String KEY_OVERVIEW = "overview";
    //public static final String KEY_RELEASE_DATE = "releaseDate";
    public static final String KEY_AVERAGE = "voteAverage";


    public MoviesDatabaseHandler(Context context, String name,
                                 SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MOVIES_TABLE = "CREATE TABLE "
                + TABLE_MOVIE + "(" + KEY_MOVIE_ID + " INTEGER_PRIMARY_KEY, "
                + KEY_TITLE + " TEXT, "
                + KEY_POSTER_URL + " TEXT, "
                + KEY_OVERVIEW + " TEXT, "
                //+ KEY_RELEASE_DATE + " TEXT, "
               // + KEY_MOVIE_ID + " TEXT, "
                + KEY_AVERAGE + " TEXT" + ")";

        db.execSQL(CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIE);

        onCreate(db);

    }

    public void addMovie(Movie movie) {

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, movie.getTitle());
        values.put(KEY_POSTER_URL, movie.getPosterUrl());
        values.put(KEY_OVERVIEW, movie.getOverview());
        //values.put(KEY_RELEASE_DATE, movie.getReleaseDate());
        values.put(KEY_MOVIE_ID, movie.getMovieId());
        values.put(KEY_AVERAGE, movie.getVoteAverage());

        mContentResolver.insert(MoviesProvider.CONTENT_URI, values);
    }

    Movie getMovie(int movieId) {
        String[] projection = {KEY_MOVIE_ID,
                KEY_TITLE, KEY_POSTER_URL, KEY_OVERVIEW, KEY_AVERAGE };

        String selection = "movieId = \"" + movieId + "\"";

        Cursor cursor = mContentResolver.query(MoviesProvider.CONTENT_URI,
                projection, selection, null,
                null);

        if (cursor != null)
            cursor.moveToFirst();

        Movie movie = new Movie(cursor.getInt(0),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4));

        return movie;
    }


    public void deleteMovie(int movieId) {

        String selection = "movieId = \"" + movieId + "\"";

        mContentResolver.delete(MoviesProvider.CONTENT_URI,
                selection, null);
    }



    public List<Movie> getAllMovies() {
        List<Movie> contactList = new ArrayList<>();

        Log.v("list", "loaded");
        Cursor cursor = mContentResolver.query(MoviesProvider.CONTENT_URI, null, null, null, null);


        if (cursor.moveToFirst()) {
            do {
                Movie movie = new Movie();
                //movie.set_id(Integer.parseInt(cursor.getString(0)));
                movie.setMovieId(cursor.getInt(0));
                movie.setTitle(cursor.getString(1));
                movie.setPosterUrl(cursor.getString(2));
                movie.setOverview(cursor.getString(3));
                //movie.setReleaseDate(cursor.getString(4));

                movie.setVoteAverage(cursor.getInt(4));

                contactList.add(movie);
            } while (cursor.moveToNext());
        }

        return contactList;
    }




}
