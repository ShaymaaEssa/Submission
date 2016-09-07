package mal.udacity.android.moviesapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import mal.udacity.android.moviesapp.data.MovieDataBaseHelper;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements AsyncTaskFinishListener,ErrorHandling {



    GridView gridView;
    final String Movie_BASE_URL =
            "http://api.themoviedb.org/3/movie/";
    final String API_PARAM = "api_key";
    String sortPref;
    ImageAdapter adapter;
    GridViewItemClickListener itemClickListener;
    View view;
    int clickedItemPosition =0 ;

    List<MovieInfo> movieList;

    public MainActivityFragment() {
    }

    //to register main activity with listener in the fragment.
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        itemClickListener = (GridViewItemClickListener)activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        if (savedInstanceState != null){
            clickedItemPosition = savedInstanceState.getInt("ClickedItem");
        }
        view = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView)view.findViewById(R.id.gridview_main);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                if (itemClickListener != null) {
                    itemClickListener.movieItemClicked(adapter.getItem(position));
                    clickedItemPosition = position;

                }
            }
        });

        updateMovieList();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovieList();

    }

    public void updateMovieList(){


        sortPref = getSortbyPref();
        if (!sortPref.equals("favorites")){
        Uri builtUri = Uri.parse(Movie_BASE_URL + sortPref +"?")
                .buildUpon()
                .appendQueryParameter(API_PARAM, BuildConfig.MOVIEAPIKEY)
                .build();
        URLConnector urlConnector = new URLConnector(new MovieParser(),this,this);
        urlConnector.executeURL(builtUri.toString());
        }

        //get the movie items which stored in the database as fav movies
        else if (sortPref.equals("favorites")){
           movieList = new ArrayList<MovieInfo>();
            try {
                SQLiteOpenHelper movieDatabaseHelper = new MovieDataBaseHelper(getActivity());
                SQLiteDatabase db = movieDatabaseHelper.getReadableDatabase();
                Cursor cursor = db.query("Movie",new String[]{"Title","MoviePoster","FilmThumbnail","ReleasedDate"
                        ,"MovieID",
                        "MovieDuration",
                        "OverView",
                        "Rate"},null,null,null,null,null);
                if (cursor.moveToFirst()){
                    while (cursor.isAfterLast() == false){ //cursor.isAfterLast() returns true when cursor is at last row position.
                        MovieInfo movieObject = new MovieInfo(cursor.getString(cursor.getColumnIndex("Title")),
                                cursor.getString(cursor.getColumnIndex("MoviePoster")),
                                cursor.getString(cursor.getColumnIndex("FilmThumbnail")),
                                cursor.getString(cursor.getColumnIndex("ReleasedDate")),
                                cursor.getString(cursor.getColumnIndex("MovieID")),
                                cursor.getString(cursor.getColumnIndex("MovieDuration")),
                                cursor.getString(cursor.getColumnIndex("OverView")),
                                null,
                                cursor.getString(cursor.getColumnIndex("Rate")));

                        movieList.add(movieObject);
                        cursor.moveToNext();
                    }
                }
                cursor.close();
                db.close();
                if (movieList == null){
                    Toast.makeText(getActivity(), "No Favourites to show", Toast.LENGTH_SHORT).show();
                }
                else {
                //notifyUpdateData(movieList);
                    adapter = new ImageAdapter(getActivity(),movieList);
                    gridView.setAdapter(adapter);
                }

            }catch (SQLiteException exception){
                Toast.makeText(getActivity(), "Database unavailable", Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    public void notifyUpdateData(List list) {

        adapter = new ImageAdapter(getActivity(),list);
        gridView.setAdapter(adapter);

       // to display first item of the gridview by default.
        if (getActivity().findViewById(R.id.detailFragmentContainer) != null)
        gridView.performItemClick(gridView.getChildAt(clickedItemPosition),clickedItemPosition,adapter.getItemId(clickedItemPosition));

    }

    //to handle rotation of the device
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("ClickedItem",clickedItemPosition);
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
            Toast.makeText(getActivity(),"Error in handling Json allData",Toast.LENGTH_SHORT).show();
    }

    //to get the value of shared preference "most popular or top rated"
    private String getSortbyPref() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String prefValue = preferences.getString(getString(R.string.listpref_prefgeneral_sortlistkey),getString(R.string.pref_sortlist_defaultvalue));
        return prefValue;
    }
}
