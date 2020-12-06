package com.jeremy.foodreview;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class FoodFragment extends Fragment {

    private static final String ARG_FOOD_ID = "food_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_PHOTO = 2;


    private Food mFood;
    private File mPhotoFile;
    private EditText mTitleField;
    private EditText mRestaurantField;
    private EditText mReview;
    private Button mDateButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;


    public static FoodFragment newInstance(UUID foodId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_FOOD_ID, foodId);

        FoodFragment fragment = new FoodFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID foodId = (UUID) getArguments().getSerializable(ARG_FOOD_ID);
        mFood = FoodLab.get(getActivity()).getFood(foodId);
        mPhotoFile = FoodLab.get(getActivity()).getPhotoFile(mFood);
    }

    @Override
    public void onPause() {
        super.onPause();

        FoodLab.get(getActivity()).updateFood(mFood);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_food, container, false);

        mTitleField = (EditText) v.findViewById(R.id.food_title);
        mTitleField.setText(mFood.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mFood.setTitle(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mDateButton = (Button) v.findViewById(R.id.food_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mFood.getDate());
                dialog.setTargetFragment(FoodFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });


        mRestaurantField = (EditText) v.findViewById(R.id.restaurant_title);
        mRestaurantField.setText(mFood.getRestaurant());
        mRestaurantField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence r, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence r, int start, int before, int count) {
                mFood.setRestaurant(r.toString());
            }

            @Override
            public void afterTextChanged(Editable r) {

            }
        });

        mReview = (EditText) v.findViewById(R.id.review_description);
        mReview.setText(mFood.getReview());
        mReview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence v, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence v, int start, int before, int count) {
                mFood.setReview(v.toString());
            }

            @Override
            public void afterTextChanged(Editable v) {

            }
        });

        mPhotoButton = (ImageButton) v.findViewById(R.id.food_camera);
        PackageManager packageManager = getActivity().getPackageManager();
        final Intent captureImage = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Uri uri = FileProvider.getUriForFile(getActivity(), "com.jeremy.foodreview.fileprovider", mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                List<ResolveInfo> cameraActivities = getActivity().getPackageManager().queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo activity : cameraActivities) {
                    getActivity().grantUriPermission(activity.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });



        mPhotoView = (ImageView) v.findViewById(R.id.food_photo);
        updatePhotoView();

        return v;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mFood.setDate(date);
            updateDate();


        } else if (requestCode == REQUEST_PHOTO) {
            Uri uri = FileProvider.getUriForFile(getActivity(), "com.jeremy.foodreview.fileprovider", mPhotoFile);

            getActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            updatePhotoView();
        }
    }

    private void updateDate() {
        mDateButton.setText(mFood.getDate().toString());
    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }


}
