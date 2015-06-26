package com.xixi.ui.main;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.xixi.R;
import com.xixi.net.image.ImageTask;
import com.xixi.net.BitmapReceiver;
import com.xixi.ui.me.ProfileActivity;
import com.xixi.widget.CircleImageView;

public class FragmentMe extends Fragment implements View.OnClickListener {

    CircleImageView imHeader;
    LinearLayout llProfile;
    LinearLayout llMessages;
    LinearLayout llSettings;

    String headPic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_me, container, false);
        imHeader = (CircleImageView) rootView.findViewById(R.id.header);
        llProfile = (LinearLayout) rootView.findViewById(R.id.ll_profile);
        llMessages = (LinearLayout) rootView.findViewById(R.id.ll_messages);
        llSettings = (LinearLayout) rootView.findViewById(R.id.ll_settings);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        llProfile.setOnClickListener(this);
        llMessages.setOnClickListener(this);
        llSettings.setOnClickListener(this);

        //headPic = ApplicationContext.getInstance().getHeadPic();

        if (headPic != null) {
            new ImageTask(headPic, new BitmapReceiver() {
                @Override
                public void onFailure() {
                    Toast.makeText(getActivity(), "getHeadPic fail", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(String url, Bitmap bitmap) {
                    imHeader.setImageBitmap(bitmap);
                }
            }).execute();
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        //TODO StartActivity

        switch (id) {
            case R.id.ll_profile:
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_messages:
                break;
            case R.id.ll_settings:
                break;
        }
    }
}
