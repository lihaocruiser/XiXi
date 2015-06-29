package com.xixi.ui.me;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.xixi.R;
import com.xixi.util.dialog.ProgressDialogManager;
import com.xixi.widget.CircleImageView;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    CircleImageView imHeader;

    LinearLayout llNickname;
    LinearLayout llAge;
    LinearLayout llSchool;
    LinearLayout llLabel;

    ProgressDialogManager progressDialogManager;

    OnModifyProfileListener onModifyProfileListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);

        imHeader = (CircleImageView) rootView.findViewById(R.id.im_header);
        llNickname = (LinearLayout) rootView.findViewById(R.id.ll_nickname);
        llAge = (LinearLayout) rootView.findViewById(R.id.ll_age);
        llSchool = (LinearLayout) rootView.findViewById(R.id.ll_school);
        llLabel = (LinearLayout) rootView.findViewById(R.id.ll_label);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        progressDialogManager = new ProgressDialogManager(getActivity());

        imHeader.setOnClickListener(this);
        llNickname.setOnClickListener(this);
        llAge.setOnClickListener(this);
        llSchool.setOnClickListener(this);
        llLabel.setOnClickListener(this);
    }


    public interface OnModifyProfileListener {
        public void onModifyProfile();
    }

    public void setOnModifyProfileListener(OnModifyProfileListener listener) {
        onModifyProfileListener = listener;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll_nickname:
                break;
            case R.id.ll_age:
                break;
            case R.id.ll_school:
                break;
            case R.id.ll_label:
                break;
        }
    }
}
