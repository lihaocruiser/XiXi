package com.xixi.ui.start;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.xixi.R;

public class RegisterFragment extends Fragment {

    ImageView imHeader;
    RadioButton rbBoy;
    RadioButton rbGirl;
    EditText etNickname;
    EditText etAge;
    EditText etEmail;
    EditText etPassword;
    EditText etPasswordConfirm;
    EditText etSchool;
    Button btnRegister;
    Button btnLogin;

    private OnAddPhotoListener onAddPhotoListener;
    private OnRegisterListener onRegisterListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_register, container, false);

        imHeader = (ImageView) rootView.findViewById(R.id.im_header);
        rbBoy = (RadioButton) rootView.findViewById(R.id.rb_boy);
        rbGirl = (RadioButton) rootView.findViewById(R.id.rb_girl);
        etNickname = (EditText) rootView.findViewById(R.id.et_nickname);
        etAge = (EditText) rootView.findViewById(R.id.et_age);
        etEmail = (EditText) rootView.findViewById(R.id.et_email);
        etPassword = (EditText) rootView.findViewById(R.id.et_password);
        etPasswordConfirm = (EditText) rootView.findViewById(R.id.et_password_confirm);
        etSchool = (EditText) rootView.findViewById(R.id.et_school);
        btnRegister = (Button) rootView.findViewById(R.id.btn_register);
        btnLogin = (Button) rootView.findViewById(R.id.btn_login);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        imHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onAddPhotoListener != null) {
                    onAddPhotoListener.onAddPhoto();
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRegisterListener != null) {
                    onRegisterListener.onRegister();
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }


    public interface OnAddPhotoListener {
        public void onAddPhoto();
    }

    public void setOnAddPhotoListener(OnAddPhotoListener listener) {
        onAddPhotoListener = listener;
    }

    public interface OnRegisterListener {
        public void onRegister();
    }

    public void setOnRegisterListener(OnRegisterListener listener) {
        onRegisterListener = listener;
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
