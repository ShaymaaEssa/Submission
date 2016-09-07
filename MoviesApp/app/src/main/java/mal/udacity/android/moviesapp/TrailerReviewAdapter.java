package mal.udacity.android.moviesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MOSTAFA on 23/08/2016.
 */

//adapter class for the listview which in DetailFragment
public class TrailerReviewAdapter extends ArrayAdapter<Object> {
    List<Object> dataList ;
    Context context;
    int trailerListSize ; //size of trailer list

    public TrailerReviewAdapter(Context context, int itemResourceId, List<Object> list , int trailerListSize){
        super(context,itemResourceId, list);

        this.dataList = list;
        this.context = context;
        this.trailerListSize = trailerListSize;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v ;
        //trailer header
        if (position == 0){
            TextView txt_title;
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.trailer_review_title, parent, false);
            txt_title = (TextView)v.findViewById(R.id.txt_detail_trailerreview);
            txt_title.setText("Trailers: ");
            return v;
        }

        //trailer data list
        else if(position>0 && position<=trailerListSize ) { //dataList.size()>position && dataList.get(position-1) instanceof TrailerDataModel
            TrailerViewHolder viewHolder;
            if (convertView != ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.trailer_item, parent, false)) {
                viewHolder = new TrailerViewHolder();
                LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.trailer_item, parent, false);
                viewHolder.name = (TextView) v.findViewById(R.id.txt_trailer_name);
                v.setTag(viewHolder);
            } else {
                v = convertView;
                viewHolder = (TrailerViewHolder) v.getTag();
            }

            TrailerDataModel trailerItem = (TrailerDataModel)dataList.get(position-1);
            if (trailerItem != null) {


                if (viewHolder.name != null)
                    viewHolder.name.setText(trailerItem.getName());

            }

            return v;
        }
        //review header
        else if (position == trailerListSize+1){//(dataList.get(position-2) instanceof TrailerDataModel) && (dataList.get(position-1) instanceof ReviewDataModel)
            TextView txt_title;
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.trailer_review_title, parent, false);
            txt_title = (TextView)v.findViewById(R.id.txt_detail_trailerreview);
            txt_title.setText("Reviews: ");
            return v;
        }

        //review data list
        else { //if (dataList.get(position) instanceof ReviewDataModel)
            ReviewViewHolder viewHolder;
            if (convertView != ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.review_item,parent,false)){
                viewHolder = new ReviewViewHolder();
                LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.review_item,parent,false);
                viewHolder.author = (TextView) v.findViewById(R.id.txt_reviewitem_author);
                viewHolder.reviewTxt = (TextView) v.findViewById(R.id.txt_reviewitem_reviewtxt);

                v.setTag(viewHolder);
            }
            else {
                v = convertView;
                viewHolder = (ReviewViewHolder) v.getTag();
            }

            ReviewDataModel reviewItem = (ReviewDataModel)dataList.get(position-2);
            if (reviewItem != null){


                if (viewHolder.author != null)
                    viewHolder.author.setText(reviewItem.getAuthor());
                if (viewHolder.reviewTxt != null)
                    viewHolder.reviewTxt.setText(reviewItem.getReview());
            }

            return v;
        }


    }

    @Override
    public int getCount() {
        if (dataList == null )
            return 0;
        else return dataList.size()+2;
    }



        @Override
    public Object getItem(int position) {
        if (dataList != null){
            if (position>0 && position<= trailerListSize) //trailer data
                return dataList.get(position-1);
            else if (position >= trailerListSize+2)  //review data
                return dataList.get(position-2);
            else return null;
        }

        else return null;
    }

    @Override
    public long getItemId(int position) {
        if (dataList != null){
            if (position>0 && position<= trailerListSize) //trailer data
                return dataList.get(position-1).hashCode();
            else if (position >= trailerListSize+2)  //review data
                return dataList.get(position-2).hashCode();
            else return 0;
        }

        else return 0;
    }

    public List<Object> getItemList(){
        return dataList;
    }

    public void setItemList (List<Object> trailerList){
        this.dataList = trailerList;
    }

    class TrailerViewHolder {
        TextView name;

    }
    class ReviewViewHolder {
        TextView author;
        TextView reviewTxt;
    }
}
