package mal.udacity.android.moviesapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MOSTAFA on 05/08/2016.
 */
public class MovieParser extends JSONParser {
    final String title = "title";
    final String moviePoster = "poster_path";
    final String filmThumbnail="backdrop_path";
    final String releasedDate = "release_date";
    final String filmDuration = "";
    final String overview = "overview";
    final String trailers ="";
    final String vote = "vote_average";
    final String id = "id";


    List<MovieInfo> movieList = new ArrayList<MovieInfo>();
    @Override
    public List dataParsing(String movieJsonStr) throws JSONException {
        JSONObject jsonObject = new JSONObject(movieJsonStr);
        JSONArray jsonArray = jsonObject.getJSONArray("results");
        for (int i = 0 ;i<jsonArray.length();i++){
            JSONObject object = jsonArray.getJSONObject(i);
            MovieInfo movieInfo = new MovieInfo(object.getString(title),
                    object.getString(moviePoster),
                    object.getString(filmThumbnail),
                    object.getString(releasedDate),
                    object.getString(id),
                    "",
                    object.getString(overview),
                    "",
                    object.getString(vote));
            movieList.add(movieInfo);

        }
        return movieList;

    }
}
