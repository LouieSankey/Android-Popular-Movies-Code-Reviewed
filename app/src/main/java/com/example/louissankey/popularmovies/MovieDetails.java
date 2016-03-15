package com.example.louissankey.popularmovies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.louissankey.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieDetails extends AppCompatActivity{
    @Bind(R.id.title_details)
    TextView titleDetailsTextView;
    @Bind(R.id.overview_details)
    TextView overveiewDetailsTextView;
    @Bind(R.id.movie_details_imageview)
    ImageView movieDetailsImageView;
    @Bind(R.id.votes_details)
    TextView votesDetailsTextView;
    @Bind(R.id.author_name)
    TextView authorNameTextView;
    @Bind(R.id.reviews_textview)
    TextView reviewsTextView;
    @Bind(R.id.review_header_label)
    TextView reviewsHeaderLAbel;
    @Bind(R.id.author_label)
    TextView authorLabel;
    @Bind(R.id.favorite_checkBox)
    CheckBox favoriteCheckbox;


    public static final String TAG = MovieDetails.class.getSimpleName();
    public static final String MOVIE_TRAILER_KEY = "MOVIE_TRAILER_KEY";

    private int mMovieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        //todo remove this code
        SharedPreferences preferences = getSharedPreferences(MainActivity.FAVORITE_MOVIES, MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();

        Bundle extras = getIntent().getExtras();
        mMovieId = extras.getInt(MainActivity.MOVIE_ID);
        final String movieTitle = extras.getString(MainActivity.MOVIE_TITLE);
        final String moviePosterUrl = extras.getString(MainActivity.MOVIE_POSTER_URL);
        final String movieOverview = extras.getString(MainActivity.MOVIE_OVERVIEW);
        final Double moveVoteAverage = extras.getDouble(MainActivity.MOVIE_VOTE_AVERAGE);
        Boolean isChecked = extras.getBoolean("IS_CHECKED");
        if(isChecked){
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    favoriteCheckbox.setChecked(true);
                }
            };
            runnable.run();
        }

        Picasso.with(this)
                .load(getString(R.string.image_path_prefix_url) + moviePosterUrl)
                .into(movieDetailsImageView);


        titleDetailsTextView.setText(movieTitle);
        overveiewDetailsTextView.setText(movieOverview);
        votesDetailsTextView.setText(NumberFormat.getInstance().format(moveVoteAverage));

        favoriteCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                MoviesDatabaseHandler db = new MoviesDatabaseHandler(MovieDetails.this, null, null, 1);

            if(isChecked){
                db.addMovie(new Movie(mMovieId, movieTitle, moviePosterUrl, movieOverview,  moveVoteAverage));

            }else{

                Movie movie = db.getMovie(mMovieId);
                db.deleteMovie(movie.getMovieId());
            }

            }
        });

        showMovieTrailerFragment();
        showReviews();

    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }


    public void showReviews(){
        String apiKey = BuildConfig.POPULAR_MOVIES_API_KEY;

        final Request request = new Request.Builder()
                .url(getString(R.string.themovedb_video_url) + mMovieId + getString(R.string.reviews_url_part_two) + apiKey)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData;
                jsonData = response.body().string();
                Log.v(TAG, jsonData);
                JSONObject movieJson;

                try {

                    movieJson = new JSONObject(jsonData);
                    final JSONArray results = movieJson.getJSONArray(getString(R.string.json_results_key));
                    final JSONObject reviewObject = results.getJSONObject(0);
                    runOnUiThread(new Runnable(){
                        @Override
                        public void run(){

                            try {

                                    reviewsHeaderLAbel.setText("Reviews");
                                    authorLabel.setText("Author: ");
                                    authorNameTextView.setText(reviewObject.getString("author"));
                                    reviewsTextView.setText(reviewObject.getString("content"));


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });

    }



    public void showMovieTrailerFragment() {

        String apiKey = BuildConfig.POPULAR_MOVIES_API_KEY;

        final Request request = new Request.Builder()
                .url(getString(R.string.themovedb_video_url) + mMovieId + getString(R.string.video_url_part_two) + apiKey)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData;
                jsonData = response.body().string();
                Log.v(TAG, jsonData);
                JSONObject movieJson;
                String movieTrailerKey;

                try {

                    movieJson = new JSONObject(jsonData);
                    JSONArray results = movieJson.getJSONArray(getString(R.string.json_results_key));

                    MovieTrailerFragment fragment = new MovieTrailerFragment();
                    Bundle args = new Bundle();

                    //check if trailer is available
                    for(int i = 0; i < results.length(); i++){
                        JSONObject jsonObject = results.getJSONObject(i);
                        if(jsonObject.getString(getString(R.string.type)).equals(getString(R.string.trailer))){
                            movieTrailerKey = jsonObject.getString("key");

                            //if trailer key is available, bundle it and start fragment
                            args.putString(MOVIE_TRAILER_KEY, movieTrailerKey);
                            fragment.setArguments(args);

                            //todo: on slow connection user may navigate back before commit and crash app
                            FragmentManager manager = getSupportFragmentManager();
                            manager.beginTransaction()
                                    .replace(R.id.frame_layout, fragment)
                                    .commit();

                            break;

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });

    }


}
