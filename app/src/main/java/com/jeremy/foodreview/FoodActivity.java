package com.jeremy.foodreview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.UUID;

public class FoodActivity extends SingleFragmentActivity {

    private static final String EXTRA_FOOD_ID = "com.jeremy.FoodReview.food_id";

    public static Intent newIntent(Context packageContext, UUID foodId) {
        Intent intent = new Intent(packageContext, FoodActivity.class);
        intent.putExtra(EXTRA_FOOD_ID, foodId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID foodId = (UUID) getIntent().getSerializableExtra(EXTRA_FOOD_ID);
        return FoodFragment.newInstance(foodId);
    }
}