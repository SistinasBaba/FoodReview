package com.jeremy.foodreview.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.widget.RatingBar;

import com.jeremy.foodreview.Food;

import java.util.Date;
import java.util.UUID;

public class FoodCursorWrapper extends CursorWrapper {
    public FoodCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public Food getFood() {
        String uuidString   = getString(getColumnIndex(FoodDbSchema.FoodTable.Cols.UUID));
        String title        = getString(getColumnIndex(FoodDbSchema.FoodTable.Cols.TITLE));
        String restaurant   = getString(getColumnIndex(FoodDbSchema.FoodTable.Cols.RESTAURANT));
        String review       = getString(getColumnIndex(FoodDbSchema.FoodTable.Cols.REVIEW));
        float rating        = getFloat(getColumnIndex(FoodDbSchema.FoodTable.Cols.RATING));
        long date           = getLong(getColumnIndex(FoodDbSchema.FoodTable.Cols.DATE));

        Food food = new Food(UUID.fromString(uuidString));
        food.setTitle(title);
        food.setRestaurant(restaurant);
        food.setReview(review);
        food.setRating(rating);
        food.setDate(new Date(date));

        return food;
    }
}
