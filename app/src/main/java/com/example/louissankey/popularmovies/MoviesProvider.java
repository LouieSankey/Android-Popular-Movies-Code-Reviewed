package com.example.louissankey.popularmovies;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Created by louissankey on 3/14/16.
 */

//I followed a tutorial here on how to create the content provider and update the database handler to use it:
// http://www.techotopia.com/index.php/An_Android_Content_Provider_Tutorial
public class MoviesProvider extends ContentProvider {

    public MoviesDatabaseHandler mMoviesDatabase;

    private static final String AUTHORITY = "com.example.louissankey.popularmovies.MoviesProvider";
    private static final String MOVIES_TABLE = "movies";
    public static final android.net.Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + MOVIES_TABLE);

    public static final int ALL_MOVIES = 1;
    public static final int SINGLE_MOVIE = 2;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, MOVIES_TABLE, ALL_MOVIES);
        sUriMatcher.addURI(AUTHORITY, MOVIES_TABLE + "/#", SINGLE_MOVIE);
    }

    @Override
    public boolean onCreate() {

        mMoviesDatabase = new MoviesDatabaseHandler(getContext(), null, null, 1);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(MoviesDatabaseHandler.TABLE_MOVIE);

        int uriType = sUriMatcher.match(uri);

        switch (uriType){
            case SINGLE_MOVIE:
                queryBuilder.appendWhere(MoviesDatabaseHandler.KEY_MOVIE_ID
                        + "=" + uri.getLastPathSegment());
                break;
            case ALL_MOVIES:
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }


        Cursor cursor = queryBuilder.query(mMoviesDatabase.getReadableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        switch (sUriMatcher.match(uri)) {
            case ALL_MOVIES:
                return "vnd.android.cursor.dir/" + AUTHORITY;
            case SINGLE_MOVIE:
                return "vnd.android.cursor.item/" + AUTHORITY;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sUriMatcher.match(uri);

        SQLiteDatabase mMoviesWritableDatabase = mMoviesDatabase.getWritableDatabase();

        long id = 0;
        switch (uriType) {
            case ALL_MOVIES:
                id = mMoviesWritableDatabase.insert(MoviesDatabaseHandler.TABLE_MOVIE,
                        null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(MOVIES_TABLE + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sUriMatcher.match(uri);
        SQLiteDatabase sqlDB = mMoviesDatabase.getWritableDatabase();
        int rowsDeleted = 0;

        switch (uriType) {
            case ALL_MOVIES:
                rowsDeleted = sqlDB.delete(MoviesDatabaseHandler.TABLE_MOVIE,
                        selection,
                        selectionArgs);
                break;

            case SINGLE_MOVIE:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(MoviesDatabaseHandler.TABLE_MOVIE,
                            MoviesDatabaseHandler.KEY_MOVIE_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(MoviesDatabaseHandler.TABLE_MOVIE,
                            MoviesDatabaseHandler.KEY_MOVIE_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = sUriMatcher.match(uri);
        SQLiteDatabase sqlDB = mMoviesDatabase.getWritableDatabase();
        int rowsUpdated = 0;

        switch (uriType) {
            case ALL_MOVIES:
                rowsUpdated = sqlDB.update(MoviesDatabaseHandler.TABLE_MOVIE,
                        values,
                        selection,
                        selectionArgs);
                break;
            case SINGLE_MOVIE:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated =
                            sqlDB.update(MoviesDatabaseHandler.TABLE_MOVIE,
                                    values,
                                    MoviesDatabaseHandler.KEY_MOVIE_ID + "=" + id,
                                    null);
                } else {
                    rowsUpdated =
                            sqlDB.update(MoviesDatabaseHandler.TABLE_MOVIE,
                                    values,
                                    MoviesDatabaseHandler.KEY_MOVIE_ID + "=" + id
                                            + " and "
                                            + selection,
                                    selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }


}
