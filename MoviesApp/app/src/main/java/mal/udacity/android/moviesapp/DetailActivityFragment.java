package mal.udacity.android.moviesapp;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;

import mal.udacity.android.moviesapp.data.MovieDataBaseHelper;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment implements ErrorHandling{


    MovieInfo movieItem ; //object of movie that will display in the detail fragment

    View view;
    View header;

    //Header Componants
    TextView title;
    ImageView filmThumbnail ;
    TextView releasedDate ;
    TextView overview ;
    TextView vote ;
    ImageButton fav;
    ImageButton unfav;

    //ListView for Trailer and Review
    ListView trailerReviewListView;
    TrailerReviewAdapter trailerReviewAdapter;

    //reviews_part
    List<ReviewDataModel> reviewsData;
    final String Movie_BASE_URL =
            "http://api.themoviedb.org/3/movie/";
    final String API_PARAM = "api_key";

    //trailer_part
    List<TrailerDataModel>trailerData;

    //List which will contain the both data for trailers and reviews
    List<Object> allData;



    //flags to know that Reviews and Trailers allData is ready
    boolean trailerFlag = false;
    boolean reviewFlag = false;

    static final String MOVIE_ITEM = "movieItem";

    public void setMovieItem(MovieInfo movieItem) {
        this.movieItem = movieItem;
    }

    public DetailActivityFragment() {
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null){
            movieItem = savedInstanceState.getParcelable(MOVIE_ITEM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_detail, container, false);
        header = getActivity().getLayoutInflater().inflate(R.layout.detail_header, null); //listview header layout
        title = (TextView)header.findViewById(R.id.txt_detail_title);
        filmThumbnail = (ImageView)header.findViewById(R.id.img_detail_moviethumbnail);
        releasedDate = (TextView)header.findViewById(R.id.txt_detail_releaseddate);
        overview = (TextView)header.findViewById(R.id.txt_detail_overview);
        vote = (TextView)header.findViewById(R.id.txt_detail_rate);
        fav = (ImageButton)header.findViewById(R.id.btn_detail_fav);
        unfav = (ImageButton)header.findViewById(R.id.btn_detail_unfav);
        trailerReviewListView = (ListView)view.findViewById(R.id.listview_detail_trailerreview);


        //add header to the listview
        trailerReviewListView.addHeaderView(header);



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        title.setText(movieItem.getTitle());
        Picasso.with(getActivity())
                .load(getActivity().getString(R.string.imageURL) + movieItem.getFilmThumbnail())
                .placeholder(R.drawable.postimg_error)
                .error(R.drawable.postimg_error)
                .into(filmThumbnail);
        releasedDate.setText(movieItem.getReleasedDate());
        overview.setText(movieItem.getOverview());
        vote.setText(movieItem.getVote());

        //favourite SQLite DB implementation part
        boolean movieAdded = movieInDB(movieItem.getId());
        if (movieAdded){ //if the movie in the Database
            fav.setVisibility(View.VISIBLE);
            unfav.setVisibility(View.INVISIBLE);
        }
        else {
            fav.setVisibility(View.INVISIBLE);
            unfav.setVisibility(View.VISIBLE);
        }

        //Listener to add the movie to the Database
        unfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMovieToDB();
                fav.setVisibility(View.VISIBLE);
                unfav.setVisibility(View.INVISIBLE);
            }
        });

        //Listener to remove the movie from the database
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteMovieFromDB();
                fav.setVisibility(View.INVISIBLE);
                unfav.setVisibility(View.VISIBLE);
            }
        });

        //Reviews Code
        reviewsData = new ArrayList<ReviewDataModel>();
        Uri reviewUri = Uri.parse(Movie_BASE_URL + movieItem.getId()+"/reviews" +"?")
                .buildUpon()
                .appendQueryParameter(API_PARAM, BuildConfig.MOVIEAPIKEY)
                .build();
        URLConnector urlConnector = new URLConnector(new ReviewParser(), new AsyncTaskFinishListener() {
            @Override
            public void notifyUpdateData(List list) {
                reviewsData = list;
                reviewFlag = true;
                setTrailerReviewAdapter();
            }
        }, this);
        urlConnector.executeURL(reviewUri.toString());

        //Trailers Code
        trailerData = new ArrayList<TrailerDataModel>();
        Uri trailerUri = Uri.parse(Movie_BASE_URL + movieItem.getId()+"/videos" +"?")
                .buildUpon()
                .appendQueryParameter(API_PARAM, BuildConfig.MOVIEAPIKEY)
                .build();
        URLConnector urlConnector2 = new URLConnector(new TrailerParser(), new AsyncTaskFinishListener() {
            @Override
            public void notifyUpdateData(List list) {
                trailerData = list;
                trailerFlag = true;
                setTrailerReviewAdapter();

            }
        }, this);
        urlConnector2.executeURL(trailerUri.toString());

        //to handle click on trailers items
        trailerReviewListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //check if this item is trailer item
                if (position > 1 && position <= trailerData.size() + 1)
                    itemClicked((TrailerDataModel) adapterView.getItemAtPosition(position));
            }
        });

    }


    //set the adapter to the TrailerReview ListView
    private void setTrailerReviewAdapter() {
        allData = new ArrayList<Object>();
        if (trailerFlag && reviewFlag){
            allData.addAll(trailerData);
            allData.addAll(reviewsData);

            int trailerListSize = trailerData.size();
            trailerReviewAdapter = new TrailerReviewAdapter(getActivity(),R.layout.review_item, allData,trailerListSize);
            trailerReviewListView.setAdapter(trailerReviewAdapter);
        }
    }

    private void deleteMovieFromDB() {
        SQLiteDatabase db;
        int rowCount;
        try{
            SQLiteOpenHelper movieDBHelper = new MovieDataBaseHelper(getActivity());
            db = movieDBHelper.getWritableDatabase();
            rowCount = db.delete("Movie", "MovieID = ?", new String[]{movieItem.getId()});
            if (rowCount == -1)
                Toast.makeText(getActivity(), "Error in deleting Movie to Favourite DB", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getActivity(), "Movie Removed from Fav", Toast.LENGTH_SHORT).show();
        }catch (SQLiteException exception){
            Toast.makeText(getActivity(), "Database unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    private void addMovieToDB() {

            SQLiteDatabase db;
            long insertId;
            try{
                ContentValues movieValues = new ContentValues();
                movieValues.put("Title",movieItem.getTitle());
                movieValues.put("MoviePoster",movieItem.getMoviePoster());
                movieValues.put("FilmThumbnail",movieItem.getFilmThumbnail());
                movieValues.put("ReleasedDate",movieItem.getReleasedDate());
                movieValues.put("MovieID",movieItem.getId());
                movieValues.put("MovieDuration",movieItem.getFilmDuration());
                movieValues.put("OverView",movieItem.getOverview());
                movieValues.put("Rate",movieItem.getVote());

                SQLiteOpenHelper movieDBHelper = new MovieDataBaseHelper(getActivity());
                db = movieDBHelper.getWritableDatabase();
                insertId = db.insert("Movie", null, movieValues);
                if (insertId == -1)
                    Toast.makeText(getActivity(), "Error in Adding Movie to Favourite DB", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getActivity(), "Movie Added to Fav", Toast.LENGTH_SHORT).show();
            }catch (SQLiteException exception){
                Toast.makeText(getActivity(), "Database unavailable", Toast.LENGTH_SHORT).show();
            }
    }

    //to check if the movie in the fav or not
    private boolean movieInDB(String movieId) {
        Cursor cursor;
        SQLiteDatabase db;
        boolean inDB;
        try{
            SQLiteOpenHelper movieDBHelper = new MovieDataBaseHelper(getActivity());
             db = movieDBHelper.getReadableDatabase();
             cursor = db.query("Movie",new String[] {"_id"},"MovieID = ?", new String [] {movieId},null,null,null);
            if (cursor.moveToFirst()) inDB= true;
            else inDB= false;
            cursor.close();
            db.close();
            return inDB;
        }catch (SQLiteException exception){
           Toast.makeText(getActivity(), "Database unavailable", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    //handle the click on the trailer item on the listview
    private void itemClicked(TrailerDataModel trailerItem) {
       try{
           startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + trailerItem.getKey())));
       }catch (ActivityNotFoundException ex){
           Intent intent = new Intent(Intent.ACTION_VIEW,
                   Uri.parse("http://www.youtube.com/watch?v=" + trailerItem.getKey()));
           startActivity(intent);
       }

    }

    //to handle rotation of the device
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(MOVIE_ITEM,movieItem);
    }



    @Override
    public void errorExceptionHandling(Exception mException) {

        if (mException instanceof MalformedURLException)
            Toast.makeText(getActivity(), "There is Malformed URL Exception", Toast.LENGTH_SHORT).show();
        else if (mException instanceof ProtocolException)
            Toast.makeText(getActivity(),"There is Protocol Exception in the connection",Toast.LENGTH_SHORT).show();
        else if (mException instanceof IOException)
            Toast.makeText(getActivity(),"There is IO Exception",Toast.LENGTH_SHORT).show();
        else if (mException instanceof JSONException)
            Toast.makeText(getActivity(),"Error in handling Json Data",Toast.LENGTH_SHORT).show();
    }


}
