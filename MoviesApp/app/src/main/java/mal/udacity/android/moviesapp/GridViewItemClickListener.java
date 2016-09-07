package mal.udacity.android.moviesapp;

/**
 * Created by MOSTAFA on 13/08/2016.
 */

    //To enhance tablet view so when image item clicked in list_fragment, we want it to tell the the main activity
    //which item has been clicked
public interface GridViewItemClickListener {
    void movieItemClicked(MovieInfo movieInfoItem);
}
