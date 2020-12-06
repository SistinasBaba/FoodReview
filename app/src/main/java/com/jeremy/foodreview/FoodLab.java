package com.jeremy.foodreview;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jeremy.foodreview.database.FoodCursorWrapper;
import com.jeremy.foodreview.database.FoodDbSchema;
import com.jeremy.foodreview.database.FoodBaseHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FoodLab {
    private static FoodLab sFoodLab;


    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static FoodLab get(Context context) {
        if (sFoodLab == null) {
            sFoodLab = new FoodLab(context);
        }
        return sFoodLab;
    }

    private FoodLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new FoodBaseHelper(mContext).getWritableDatabase();

    }

    public void addFood(Food f) {
        ContentValues values = getContentValues(f);

        mDatabase.insert(FoodDbSchema.FoodTable.NAME, null, values);
    }


    public List<Food> getFoods() {
        List<Food> foods = new ArrayList<>();

        FoodCursorWrapper cursor = queryFoods(null,null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                foods.add(cursor.getFood());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return foods;
    }

    public Food getFood(UUID id) {
        FoodCursorWrapper cursor = queryFoods(
                FoodDbSchema.FoodTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getFood();
        } finally {
            cursor.close();
        }
    }

    public File getPhotoFile (Food food) {
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, food.getPhotoFilename());
    }

    public void updateFood(Food food) {
        String uuidString = food.getId().toString();
        ContentValues values = getContentValues(food);

        mDatabase.update(FoodDbSchema.FoodTable.NAME, values, FoodDbSchema.FoodTable.Cols.UUID + " = ?", new String[] {uuidString});
    }

    private FoodCursorWrapper queryFoods(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                FoodDbSchema.FoodTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new FoodCursorWrapper(cursor);
    }

    public static ContentValues getContentValues(Food food){
        ContentValues values = new ContentValues();
        values.put(FoodDbSchema.FoodTable.Cols.UUID, food.getId().toString());
        values.put(FoodDbSchema.FoodTable.Cols.TITLE, food.getTitle());
        values.put(FoodDbSchema.FoodTable.Cols.RESTAURANT, food.getRestaurant());
        values.put(FoodDbSchema.FoodTable.Cols.REVIEW, food.getReview());
        values.put(FoodDbSchema.FoodTable.Cols.RATING, food.getRating());
        values.put(FoodDbSchema.FoodTable.Cols.DATE,food.getDate().getTime());

        return values;
    }



}
