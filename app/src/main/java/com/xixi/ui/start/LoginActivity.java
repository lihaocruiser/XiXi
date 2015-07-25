package com.xixi.ui.start;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xixi.R;
import com.xixi.bean.ApplicationContext;
import com.xixi.net.start.LoginJSONTask;
import com.xixi.ui.main.MainActivity;
import com.xixi.net.base.JSONReceiver;
import com.xixi.util.SafeJSON;

public class LoginActivity extends Activity implements OnClickListener {

	EditText etEmail;
	EditText etPassword;
	Button btnLogin;
	Button btnRegister;
	
	String email;
	String password;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		init();
	}
	
	private void initView() {
		setContentView(R.layout.activity_login);
		etEmail = (EditText) findViewById(R.id.email);
		etPassword = (EditText) findViewById(R.id.password);
		btnLogin = (Button) findViewById(R.id.login);
		btnRegister = (Button) findViewById(R.id.register);
		btnLogin.setOnClickListener(this);
		btnRegister.setOnClickListener(this);
	}
	
	private void init() {
		ApplicationContext ac = ApplicationContext.getInstance();
		ac.setContext(getApplicationContext());
		int checked = ac.getChecked();
		if (checked == 0) {
			email = ac.getEmail();
			password = ac.getPassword();
			login(email, password);
		}
	}

	private void login(String email, String password) {
		new LoginJSONTask(email, password, jsonReceiver).execute();
	}

    private JSONReceiver jsonReceiver = new JSONReceiver() {
        @Override
        public void onFailure(JSONObject obj) {
            Toast.makeText(LoginActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onSuccess(JSONObject obj) {
            switch (SafeJSON.getInt(obj, "checked", -1)) {
                case 0:        // 登陆成功
                    ApplicationContext ac = ApplicationContext.getInstance();
                    ac.saveUserInfo(obj);
                    ac.setEmail(email);
                    ac.setPassword(password);
                    ac.setChecked(0);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                    break;
                case 1:
                    Toast.makeText(LoginActivity.this, "邮箱或密码错误", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(LoginActivity.this, "账号已被停用", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(LoginActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
            }
        }
    };

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login:
			email = etEmail.getText().toString();
			password = etPassword.getText().toString();
			login(email, password);
			break;
		case R.id.register:
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
			break;
		}
	}

}
