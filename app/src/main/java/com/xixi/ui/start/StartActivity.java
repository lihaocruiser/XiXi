package com.xixi.ui.start;

import com.xixi.bean.ApplicationContext;
import com.xixi.R;
import com.xixi.ui.main.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class StartActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);

		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				if (isFirstUse()) {
					Intent intent = new Intent(StartActivity.this, MainActivity.class);
					startActivity(intent);
				}else {
					Intent intent = new Intent(StartActivity.this, MainActivity.class);
					startActivity(intent);
				}
				StartActivity.this.finish();
			}
		}, 2000);
	}
	
	private boolean isFirstUse() {
		ApplicationContext.getInstance().setContext(getApplicationContext());
		int checked = ApplicationContext.getInstance().getChecked();
		return (checked == -1);
	}
}
