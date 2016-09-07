package mal.udacity.android.moviesapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MOSTAFA on 22/08/2016.
 */
public class ReviewParser extends JSONParser {
    final String author = "author";
    final String review = "content";
    List<ReviewDataModel> reviewsList = new ArrayList<ReviewDataModel>();
    @Override
    public List dataParsing(String reviewJsonStr) throws JSONException {
        JSONObject jsonObject = new JSONObject(reviewJsonStr);
        JSONArray jsonArray = jsonObject.getJSONArray("results");
        for (int i = 0 ;i<jsonArray.length();i++){
            JSONObject object = jsonArray.getJSONObject(i);
            ReviewDataModel reviewData = new ReviewDataModel(object.getString(author),
                    object.getString(review));
            reviewsList.add(reviewData);

        }
        return reviewsList;
    }
}
