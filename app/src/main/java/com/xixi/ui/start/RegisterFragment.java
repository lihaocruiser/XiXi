package com.xixi.ui.start;

import android.content.DialogInterface;
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
import android.widget.Toast;

import com.xixi.R;
import com.xixi.net.JSONReceiver;
import com.xixi.net.start.SchoolListTask;
import com.xixi.util.dialog.AlertDialogManager;
import com.xixi.util.dialog.ProgressDialogManager;

import org.json.JSONArray;
import org.json.JSONObject;

public class RegisterFragment extends Fragment {

    ImageView imHeader;
    RadioButton rbBoy;
    RadioButton rbGirl;
    Button btnSchool;
    EditText etNickname;
    EditText etAge;
    EditText etEmail;
    EditText etPassword;
    EditText etPasswordConfirm;
    Button btnRegister;
    Button btnLogin;

    private OnAddPhotoListener onAddPhotoListener;
    private OnRegisterListener onRegisterListener;

    ProgressDialogManager progressDialogManager;
    AlertDialogManager alertDialogManager;

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
        btnSchool = (Button) rootView.findViewById(R.id.btn_school);
        btnRegister = (Button) rootView.findViewById(R.id.btn_register);
        btnLogin = (Button) rootView.findViewById(R.id.btn_login);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        progressDialogManager = new ProgressDialogManager(getActivity());
        alertDialogManager = new AlertDialogManager(getActivity());

        imHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onAddPhotoListener != null) {
                    onAddPhotoListener.onAddPhoto();
                }
            }
        });

        btnSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialogManager.show("loading school list");
                new SchoolListTask(null, new JSONReceiver() {
                    @Override
                    public void onFailure(JSONObject obj) {
                        String[] s = new String[]{"中国科学技术大学","b"}; // for text only
                        selectSchool(s);    // for test only
                        progressDialogManager.dismiss();
                        Toast.makeText(getActivity(), "request school list fail", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(JSONObject obj) {
                        JSONArray array = obj.optJSONArray("list");
                        int length = array.length();
                        String[] schools = new String[length];
                        String school;
                        for (int i = 0; i < length; i++) {
                            school = array.optString(i);
                            schools[i] = school;
                        }
                        selectSchool(schools);
                        progressDialogManager.dismiss();
                    }
                }).execute();
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

    private void selectSchool(final String[] schools) {
        alertDialogManager.show(schools, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                btnSchool.setText(schools[which]);
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
