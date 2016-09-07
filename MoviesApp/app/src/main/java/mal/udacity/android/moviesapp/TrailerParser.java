package mal.udacity.android.moviesapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MOSTAFA on 23/08/2016.
 */
public class TrailerParser extends JSONParser {
    final String name = "name";
    final String key = "key";
    List<TrailerDataModel> trailersList = new ArrayList<TrailerDataModel>();
    @Override
    public List dataParsing(String trailerJsonStr) throws JSONException {
        JSONObject jsonObject = new JSONObject(trailerJsonStr);
        JSONArray jsonArray = jsonObject.getJSONArray("results");
        for (int i = 0 ;i<jsonArray.length();i++){
            JSONObject object = jsonArray.getJSONObject(i);
            TrailerDataModel trailerItem = new TrailerDataModel(object.getString(name),
                    object.getString(key));
            trailersList.add(trailerItem);

        }
        return trailersList;
    }
}
