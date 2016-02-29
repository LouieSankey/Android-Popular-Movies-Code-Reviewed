package com.example.louissankey.popularmovies;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.louissankey.popularmovies.model.Movie;
import com.example.louissankey.popularmovies.model.MoviePosterAdapter;
import com.google.android.youtube.player.YouTubeBaseActivity;
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

    public static final String TAG = MovieDetails.class.getSimpleName();
    private int mMovieId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);


        Bundle extras = getIntent().getExtras();
        mMovieId = extras.getInt(MainActivity.MOVIE_ID);

        Picasso.with(this)
                .load(getString(R.string.image_path_prefix_url) + extras.getString(MainActivity.MOVIE_POSTER_URL))
                .into(movieDetailsImageView);


        titleDetailsTextView.setText(extras.getString(MainActivity.MOVIE_TITLE));
        overveiewDetailsTextView.setText(extras.getString(MainActivity.MOVIE_OVERVIEW));
        votesDetailsTextView.setText(NumberFormat.getInstance().format(extras.getDouble(MainActivity.MOVIE_VOTE_AVERAGE)));


    showMovieTrailerFragment();

    }

    public void showMovieTrailerFragment() {

        String apiKey = BuildConfig.POPULAR_MOVIES_API_KEY;

        final Request request = new Request.Builder()
                .url(getString(R.string.themovedb_video_url) + mMovieId + "/videos?api_key=" + apiKey)
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
                String movieTrailerKey = "";

                try {

                    movieJson = new JSONObject(jsonData);
                    JSONArray results = movieJson.getJSONArray(getString(R.string.json_results_key));

                    YoutubeFragment fragment = new YoutubeFragment();
                    Bundle args = new Bundle();

                    //check if trailer is available
                    for(int i = 0; i < results.length(); i++){
                        JSONObject jsonObject = results.getJSONObject(i);
                        if(jsonObject.getString("type").equals("Trailer")){
                            movieTrailerKey = jsonObject.getString("key");

                            //if trailer key is available, bundle it and start fragment
                            args.putString("MOVIE_TRAILER_KEY", movieTrailerKey);
                            fragment.setArguments(args);

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
