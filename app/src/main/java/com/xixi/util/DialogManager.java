package com.xixi.util;

import android.app.ProgressDialog;
import android.content.Context;

public class DialogManager {

    private ProgressDialog progressDialog;
    private Context context;

    public DialogManager (Context context) {
        this.context = context;
    }

    public void showProgressDialog(String s) {
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

    public void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
