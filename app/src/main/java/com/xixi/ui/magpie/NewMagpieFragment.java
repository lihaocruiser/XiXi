package com.xixi.ui.magpie;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xixi.R;

public class NewMagpieFragment extends Fragment {

    EditText etTitle;
    EditText etBasic;
    EditText etHobby;
    EditText etCondition;

    Button btnAddPhoto;

    ImageView[] imSelected = new ImageView[3];

    private OnAddPhotoListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_new_magpie, container, false);

        etTitle = (EditText) rootView.findViewById(R.id.et_title);
        etBasic = (EditText) rootView.findViewById(R.id.et_basic);
        etHobby = (EditText) rootView.findViewById(R.id.et_hobby);
        etCondition = (EditText) rootView.findViewById(R.id.et_condition);

        btnAddPhoto = (Button) rootView.findViewById(R.id.btn_add_photo);

        imSelected[0] = (ImageView) rootView.findViewById(R.id.im1);
        imSelected[1] = (ImageView) rootView.findViewById(R.id.im3);
        imSelected[2] = (ImageView) rootView.findViewById(R.id.im2);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onAddPhoto();
                }
            }
        });
    }


    public interface OnAddPhotoListener {
        public void onAddPhoto();
    }

    public void setOnAddPhotoListener(OnAddPhotoListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
        }
        return true;
    }

}