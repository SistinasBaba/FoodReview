package com.jeremy.foodreview;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FoodListFragment extends Fragment {

    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    private RecyclerView mFoodRecyclerView;
    private FoodAdapter mAdapter;
    private boolean mSubtitleVisible;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_list, container, false);

        mFoodRecyclerView = (RecyclerView) view.findViewById(R.id.food_recycler_view);
        mFoodRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_food_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_food:
                Food food = new Food();
                FoodLab.get(getActivity()).addFood(food);
                Intent intent = FoodPagerActivity.newIntent(getActivity(), food.getId());
                startActivity(intent);
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle() {
        FoodLab foodLab = FoodLab.get(getActivity());
        int foodCount = foodLab.getFoods().size();
        String subtitle = getString(R.string.subtitle_format, foodCount);

        if (!mSubtitleVisible) {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    private void updateUI() {
        FoodLab foodLab = FoodLab.get(getActivity());
        List<Food> foods = foodLab.getFoods();

        if (mAdapter == null) {
            mAdapter = new FoodAdapter(foods);
            mFoodRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setFoods(foods);
            mAdapter.notifyDataSetChanged();
        }

        updateSubtitle();
    }


    private class FoodHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Food mFood;
        private TextView mTitleTextView;
        private TextView mDateTextView;

        public FoodHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_food, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.food_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.food_date);
        }

        public void bind(Food food) {
            mFood = food;
            mTitleTextView.setText(mFood.getTitle());
            mDateTextView.setText(mFood.getDate().toString());
        }

        @Override
        public void onClick(View view) {
            Intent intent = FoodPagerActivity.newIntent(getActivity(), mFood.getId());
            startActivity(intent);
        }
    }

    //Adapter
    private class FoodAdapter extends RecyclerView.Adapter<FoodHolder> {

        private List<Food> mFoods;

        public FoodAdapter(List<Food> foods) {
            mFoods = foods;
        }

        @Override
        public FoodHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new FoodHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(FoodHolder holder, int position) {
            Food food = mFoods.get(position);
            holder.bind(food);
        }

        @Override
        public int getItemCount() {
            return mFoods.size();
        }

        public void setFoods (List<Food> foods) {
            mFoods = foods;
        }
    }

}
