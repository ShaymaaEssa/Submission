package mal.udacity.android.moviesapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


//Detail Activity will be calling when we are in Mobile mode
//it contains the DetailFragment
public class DetailActivity extends ActionBarActivity {


    public static final String MOVIE_ITEM = "movieitem";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        DetailActivityFragment detailActivityFragment = (DetailActivityFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);
        MovieInfo movieInfo = getIntent().getParcelableExtra(DetailActivity.MOVIE_ITEM);
        detailActivityFragment.setMovieItem(movieInfo);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
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
            Intent settingIntent = new Intent(DetailActivity.this,SettingActivity.class);
            startActivity(settingIntent);
        }

        return super.onOptionsItemSelected(item);
    }
}
