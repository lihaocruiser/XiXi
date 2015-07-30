package com.xixi.util.dialog;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

/**
 * Created on 2015-7-29.
 */
public class ProgressDialog {

    private Context context;
    private MaterialDialog materialDialog;

    public ProgressDialog(Context context) {
        this.context = context;
    }

    public void show(int resId) {
        materialDialog = new MaterialDialog
                .Builder(context)
                .theme(Theme.LIGHT)
                .content(resId)
                .progress(true, 0)
                .show();
    }

    public void show(String content) {
        materialDialog = new MaterialDialog
                .Builder(context)
                .theme(Theme.LIGHT)
                .content(content)
                .progress(true, 0)
                .show();
    }

    public void dismiss() {
        materialDialog.dismiss();
    }

}
