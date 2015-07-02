package com.xixi.util.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.xixi.R;

public class TextAlertDialogManager {

    Context context;
    LayoutInflater inflater;
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;

    EditText etDialog;
    LinearLayout ll;

    public TextAlertDialogManager(Context context) {
        this.context = context;
        builder = new AlertDialog.Builder(context);
        inflater = LayoutInflater.from(context);
    }


    public void show(DialogInterface.OnClickListener listener) {
        this.show(listener, InputType.TYPE_CLASS_TEXT);
    }

    public void show(DialogInterface.OnClickListener listener, int inputType) {
        ll = (LinearLayout) inflater.inflate(R.layout.dialog_edit_text, null);
        etDialog = (EditText) ll.findViewById(R.id.et_dialog);
        etDialog.setInputType(inputType);
        builder.setView(ll);
        builder.setPositiveButton(R.string.btn_confirm, listener);
        builder.setNegativeButton(R.string.btn_cancel, null);
        alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }


    public String getText() {
        return etDialog.getText().toString();
    }

}
