package mal.udacity.android.moviesapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MOSTAFA on 05/08/2016.
 */

//Adapter for the grid view in the main fragment
public class ImageAdapter extends BaseAdapter {
    private Context context;
    List<MovieInfo> movieInfoList = new ArrayList<MovieInfo>();
    int position ;

    public ImageAdapter(Context context,List<MovieInfo> movieInfoList) {
        this.context = context;
        this.movieInfoList = movieInfoList;

    }

    @Override
    public int getCount() {
        return movieInfoList.size();
    }

    @Override
    public MovieInfo getItem(int i) {
        return movieInfoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        if (movieInfoList != null)
            return movieInfoList.get(i).hashCode();
        else return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        View v ;
        if (view == null){
            viewHolder = new ViewHolder();
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.grid_item,viewGroup,false);
            viewHolder.imageView = (ImageView)v.findViewById(R.id.image_view_poster);
            v.setTag(viewHolder);
        }
        else {
            v = view;
            viewHolder = (ViewHolder) v.getTag();
        }

        int width= context.getResources().getDisplayMetrics().widthPixels;

        Picasso.with(context)
                .load(context.getString(R.string.imageURL) + movieInfoList.get(i).getMoviePoster())
                .centerCrop().resize(width/2,4*width/5)
                .placeholder(R.drawable.postimg_error)
                .error(R.drawable.postimg_error)
                .into(viewHolder.imageView);

        position = i;



        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        v.setLayoutParams(new GridView.LayoutParams(params));
        return v;
    }

    private class ViewHolder {
        ImageView imageView;
    }
}


