package com.xixi.util.dialog;

import android.app.ProgressDialog;
import android.content.Context;

import com.xixi.R;

public class ProgressDialogManager {

    private ProgressDialog progressDialog;
    private Context context;

    public ProgressDialogManager(Context context) {
        this.context = context;
    }

    public void show(int res) {
        String s = context.getResources().getString(res);
        show(s);
    }

    public void show(String s) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
        }
        if (!progressDialog.isShowing()) {
            progressDialog.setMessage(s);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }
    }

    public void dismiss() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
