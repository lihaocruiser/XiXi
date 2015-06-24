package com.xixi.ui.main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.xixi.R;

public class FragmentDiscover extends Fragment implements View.OnClickListener {

    LinearLayout llCircle;
    LinearLayout llWeekend;
    LinearLayout llHelp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_discover, container, false);
        llCircle = (LinearLayout) rootView.findViewById(R.id.ll_circle);
        llWeekend = (LinearLayout) rootView.findViewById(R.id.ll_weekend);
        llHelp = (LinearLayout) rootView.findViewById(R.id.ll_help);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        llCircle.setOnClickListener(this);
        llWeekend.setOnClickListener(this);
        llHelp.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        //TODO StartActivity

        switch (id) {
            case R.id.ll_circle:
                break;
            case R.id.ll_weekend:
                break;
            case R.id.ll_help:
                break;
        }
    }
}
