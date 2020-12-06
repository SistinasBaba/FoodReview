package com.jeremy.foodreview.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jeremy.foodreview.database.FoodDbSchema;
import com.jeremy.foodreview.database.FoodDbSchema.FoodTable;

public class FoodBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "foodBase.db";

    public FoodBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + FoodTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                FoodTable.Cols.UUID         + ", " +
                FoodTable.Cols.TITLE        + ", " +
                FoodTable.Cols.RESTAURANT   + ", " +
                FoodTable.Cols.REVIEW       + ", " +
                FoodTable.Cols.RATING       + ", " +
                FoodTable.Cols.DATE         + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
