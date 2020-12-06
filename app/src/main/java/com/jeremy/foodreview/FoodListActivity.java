package com.jeremy.foodreview;

import androidx.fragment.app.Fragment;

public class FoodListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new FoodListFragment();
    }

}
