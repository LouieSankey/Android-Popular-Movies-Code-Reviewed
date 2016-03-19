package com.example.louissankey.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by louissankey on 2/22/16.
 */
public class Movie implements Parcelable{

    private String mTitle;
    private String mPosterUrl;
    private String mOverview;
    private String mReleaseDate;
    private int mMovieId;
    private double mVoteAverage;


    public Movie(Parcel in) {
        mMovieId = in.readInt();
        mTitle = in.readString();
        mPosterUrl = in.readString();
        mOverview = in.readString();
        mReleaseDate = in.readString();
        mVoteAverage = in.readDouble();
        mReleaseDate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mMovieId);
        dest.writeString(mTitle);
        dest.writeString(mPosterUrl);
        dest.writeString(mOverview);
        dest.writeString(mReleaseDate);
        dest.writeDouble(mVoteAverage);
        dest.writeString(mReleaseDate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public Movie(){

    }

    public Movie(int movieId, String title, String posterUrl, String overview, String releaseDate, double voteAverage){
        mMovieId = movieId;
        mTitle = title;
        mOverview = overview;
        mPosterUrl = posterUrl;
        mReleaseDate = releaseDate;
        mVoteAverage = voteAverage;
    }

    public int getMovieId() {return mMovieId;}

    public void setMovieId(int movieId) {mMovieId = movieId;}

    public String getTitle() { return mTitle;}

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getPosterUrl() {
        return mPosterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        mPosterUrl = posterUrl;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String overview) {
        mOverview = overview;
    }

    public double getVoteAverage() {
        return mVoteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        mVoteAverage = voteAverage;
    }

    public String getReleaseDate() { return mReleaseDate; }

    public void setReleaseDate(String releaseDate) { mReleaseDate = releaseDate; }

}
