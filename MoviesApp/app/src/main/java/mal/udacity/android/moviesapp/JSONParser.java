package mal.udacity.android.moviesapp;

import org.json.JSONException;

import java.util.List;

/**
 * Created by MOSTAFA on 01/08/2016.
 */
public abstract class JSONParser {
    public abstract List dataParsing(String postJsonStr) throws JSONException;
}
