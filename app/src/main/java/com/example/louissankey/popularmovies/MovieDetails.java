package com.example.louissankey.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieDetails extends AppCompatActivity {
    @Bind(R.id.title_details)
    TextView titleDetailsTextView;
    @Bind(R.id.overview_details)
    TextView overveiewDetailsTextView;
    @Bind(R.id.movie_details_imageview)
    ImageView movieDetailsImageView;
    @Bind(R.id.votes_details)
    TextView votesDetailsTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();

        Picasso.with(this)
                .load(getString(R.string.image_path_prefix_url) + extras.getString(MainActivity.MOVIE_POSTER_URL))
                .into(movieDetailsImageView);


        titleDetailsTextView.setText(extras.getString(MainActivity.MOVIE_TITLE));
        overveiewDetailsTextView.setText(extras.getString(MainActivity.MOVIE_OVERVIEW));

        votesDetailsTextView.setText(NumberFormat.getInstance().format(extras.getDouble(MainActivity.MOVIE_VOTE_AVERAGE)));

    }

}
