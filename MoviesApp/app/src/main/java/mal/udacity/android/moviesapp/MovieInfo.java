package mal.udacity.android.moviesapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by MOSTAFA on 05/08/2016.
 */
public class MovieInfo implements Parcelable {
    private String title;
    private String moviePoster;
    private String filmThumbnail;
    private String releasedDate;
    private String id;
    private String filmDuration;
    private String overview;
    private String trailers;
    private String vote;


    public MovieInfo(String title, String moviePoster, String filmThumbnail, String releasedDate,String id, String filmDuration, String overview, String trailers, String vote) {
        this.title = title;
        this.moviePoster = moviePoster;
        this.filmThumbnail = filmThumbnail;
        this.releasedDate = releasedDate;
        this.id = id;
        this.filmDuration = filmDuration;
        this.overview = overview;
        this.trailers = trailers;
        this.vote = vote;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

    public String getFilmThumbnail() {
        return filmThumbnail;
    }

    public void setFilmThumbnail(String filmThumbnail) {
        this.filmThumbnail = filmThumbnail;
    }

    public String getReleasedDate() {
        return releasedDate;
    }

    public void setReleasedDate(String releasedDate) {
        this.releasedDate = releasedDate;
    }

    public String getFilmDuration() {
        return filmDuration;
    }

    public void setFilmDuration(String filmDuration) {
        this.filmDuration = filmDuration;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getTrailers() {
        return trailers;
    }

    public void setTrailers(String trailers) {
        this.trailers = trailers;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    //for Parcelable, so we can send movie object in the intent
    public MovieInfo(Parcel in) {
        String[] data = new String[7];
        in.readStringArray(data);
        this.title = data[0];
        this.moviePoster = data[1];
        this.filmThumbnail = data[2];
        this.releasedDate = data[3];
        this.overview = data[4];
        this.vote = data[5];
        this.id = data[6];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{this.title,this .moviePoster,this.filmThumbnail, this.releasedDate, this.overview, this.vote,this.id});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public MovieInfo createFromParcel(Parcel in) {
            return new MovieInfo(in);
        }

        @Override
        public MovieInfo[] newArray(int size) {
            return new MovieInfo[size];
        }
    };
}