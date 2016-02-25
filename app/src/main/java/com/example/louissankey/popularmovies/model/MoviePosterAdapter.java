package com.example.louissankey.popularmovies.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.louissankey.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by louissankey on 2/22/16.
 */
public class MoviePosterAdapter extends BaseAdapter {
    private Context mContext;
    private List<Movie> mMovies;

    public MoviePosterAdapter(Context c, List<Movie> movies) {
        mContext = c;
        mMovies = movies;
    }

    @Override
    public int getCount() {return mMovies.size();}

    @Override
    public Object getItem(int position) {return mMovies.get(position);}

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.movie_poster_item, null);

            viewHolder = new ViewHolder();
            viewHolder.moviePosterImage = (ImageView) convertView.findViewById(R.id.movie_poster_imageview);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Movie movie = mMovies.get(position);
        Picasso.with(mContext)
                .load(mContext.getString(R.string.image_path_prefix_url) + movie.getPosterUrl())
                .into(viewHolder.moviePosterImage);

        return convertView;
    }
    public class ViewHolder { ImageView moviePosterImage; }

}
