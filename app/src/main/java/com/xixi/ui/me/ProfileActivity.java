package com.xixi.ui.me;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.xixi.R;
import com.xixi.widget.CircleImageView;

public class ProfileActivity extends ActionBarActivity implements View.OnClickListener {

    CircleImageView imHeader;

    LinearLayout llNickname;
    LinearLayout llAge;
    LinearLayout llSchool;
    LinearLayout llLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imHeader = (CircleImageView) findViewById(R.id.im_header);
        llNickname = (LinearLayout) findViewById(R.id.ll_nickname);
        llAge = (LinearLayout) findViewById(R.id.ll_age);
        llSchool = (LinearLayout) findViewById(R.id.ll_school);
        llLabel = (LinearLayout) findViewById(R.id.ll_label);

        imHeader.setOnClickListener(this);
        llNickname.setOnClickListener(this);
        llAge.setOnClickListener(this);
        llSchool.setOnClickListener(this);
        llLabel.setOnClickListener(this);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
