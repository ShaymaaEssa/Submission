package mal.udacity.android.moviesapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends ActionBarActivity implements GridViewItemClickListener {

    MovieInfo firstMovieInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    //interface method implementation when an item clicked in grid-view
    @Override
    public void movieItemClicked(MovieInfo movieInfoItem) {
        View detailFragmentContainer = findViewById(R.id.detailFragmentContainer);


        //Tablet View
        if (detailFragmentContainer != null) { //means we are in tablet view
            //This code for attach detailFragment to the FrameLayout
            DetailActivityFragment detailFragment = new DetailActivityFragment();
            detailFragment.setMovieItem(movieInfoItem);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.detailFragmentContainer, detailFragment);
            ft.addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        }
        else { //Phone View
            Intent detailIntent = new Intent(MainActivity.this,DetailActivity.class);
                detailIntent.putExtra(DetailActivity.MOVIE_ITEM,movieInfoItem);
                startActivity(detailIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingIntent = new Intent(MainActivity.this,SettingActivity.class);
            startActivity(settingIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void setFirstMovieInfo(MovieInfo firstMovieInfo) {
        this.firstMovieInfo = firstMovieInfo;
    }
}
