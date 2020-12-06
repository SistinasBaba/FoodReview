package com.jeremy.foodreview.database;

import android.widget.RatingBar;

public class FoodDbSchema {
    public static final class FoodTable {
        public static final String NAME = "foods";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE ="title";
            public static final String RESTAURANT = "restaurant";
            public static final String REVIEW = "review";
            public static final String RATING = "rating";
            public static final String DATE = "date";
        }
    }
}
