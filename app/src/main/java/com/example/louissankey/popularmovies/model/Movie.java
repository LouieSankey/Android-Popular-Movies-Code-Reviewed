package com.example.louissankey.popularmovies.model;

/**
 * Created by louissankey on 2/22/16.
 */
public class Movie {

    private String mTitle;
    private String mPosterUrl;
    private String mOverview;
    private double mVoteAverage;
    private String mReleaseDate;

    public String getTitle() {
        return mTitle;
    }

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
