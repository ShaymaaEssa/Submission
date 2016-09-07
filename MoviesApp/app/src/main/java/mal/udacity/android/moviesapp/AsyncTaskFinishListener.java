package mal.udacity.android.moviesapp;

import java.util.List;

/**
 * Created by MOSTAFA on 03/08/2016.
 */

//observer that will be calling when asynctask finish getting data
public interface AsyncTaskFinishListener {
    public void notifyUpdateData(List list);
}
