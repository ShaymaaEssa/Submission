package mal.udacity.android.moviesapp;

/**
 * Created by MOSTAFA on 23/08/2016.
 */

//Trailer DataModel class for the Trailers data
public class TrailerDataModel {
    String name;
    String key;

    public TrailerDataModel(String name, String key) {
        this.name = name;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


}
