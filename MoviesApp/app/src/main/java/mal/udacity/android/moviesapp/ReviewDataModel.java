package mal.udacity.android.moviesapp;

/**
 * Created by MOSTAFA on 22/08/2016.
 */

//Review DataModel class for the review data
public class ReviewDataModel {
    String author;
    String review;

    public ReviewDataModel(String author, String review) {
        this.author = author;
        this.review = review;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }


}
