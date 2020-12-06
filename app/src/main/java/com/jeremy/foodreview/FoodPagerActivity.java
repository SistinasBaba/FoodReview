package com.jeremy.foodreview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;
import java.util.UUID;

public class FoodPagerActivity extends AppCompatActivity {
    private static final String EXTRA_FOOD_ID = "com.jeremy.FoodReview.food_id";

    private ViewPager mViewPager;
    private List<Food> mFoods;

    public static Intent newIntent(Context packageContext, UUID foodId) {
        Intent intent = new Intent(packageContext, FoodPagerActivity.class);
        intent.putExtra(EXTRA_FOOD_ID, foodId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_pager);

        UUID foodId = (UUID) getIntent().getSerializableExtra(EXTRA_FOOD_ID);

        mViewPager = (ViewPager) findViewById(R.id.food_view_pager);

        mFoods = FoodLab.get(this).getFoods();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {

            @Override
            public Fragment getItem(int position) {
                Food food = mFoods.get(position);
                return FoodFragment.newInstance(food.getId());
            }

            @Override
            public int getCount() {
                return mFoods.size();
            }
        });

        for (int i = 0; i < mFoods.size(); i++) {
            if (mFoods.get(i).getId().equals(foodId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

}
