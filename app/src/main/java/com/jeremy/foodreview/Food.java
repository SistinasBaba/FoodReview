package com.jeremy.foodreview;

import android.widget.RatingBar;

import java.util.Date;
import java.util.UUID;

public class Food {

    private UUID mId;
    private String mTitle;
    private String mRestaurant;
    private String mReview;
    private float mRating;
    private Date mDate;

    public Food() {
        this(UUID.randomUUID());

    }

    public Food(UUID id){
        mId = id;
        mDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }
    public void setTitle(String title) {
        mTitle = title;
    }

    public String getRestaurant() {
        return mRestaurant;
    }
    public void setRestaurant(String restaurant) {
        mRestaurant = restaurant;
    }

    public String getReview() {
        return mReview;
    }
    public void setReview(String review) {
        mReview = review;
    }

    public float getRating() {
        return mRating;
    }
    public void setRating(float rating) {
        mRating = rating;
    }

    public Date getDate() {
        return mDate;
    }
    public void setDate(Date date) {
        mDate = date;
    }

    public String getPhotoFilename() { return "IMG_" + getId().toString() + ".jpg";
    }
}
