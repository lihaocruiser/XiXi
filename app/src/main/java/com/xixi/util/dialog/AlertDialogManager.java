package com.xixi.util.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertDialogManager {

    private Context context;
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;

    public AlertDialogManager(Context context) {
        this.context = context;
        builder = new AlertDialog.Builder(context);
    }

    public void show(String[] items, DialogInterface.OnClickListener listener) {
        builder.setItems(items, listener);
        builder.create();
        builder.show();
    }

}
