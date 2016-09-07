package mal.udacity.android.moviesapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created on 01/09/2016.
 */
public class MovieDataBaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "MovieApp";
    private static final int DB_VERSION = 1;


    //to save the fav movie items in SQLite DataBase
    public MovieDataBaseHelper (Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //Movie Table which contains all the movie items data
        sqLiteDatabase.execSQL("Create table Movie ( "+
        "_id Integer Primary key AutoIncrement, "+
        "Title Text, "+
        "MoviePoster Text, "+
        "FilmThumbnail Text, "+
        "ReleasedDate Text, "+
        "MovieID Text, "+
        "MovieDuration Text, "+
        "OverView Text, "+
        "Rate Text); ");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
