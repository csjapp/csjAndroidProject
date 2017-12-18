package com.csjcom.csjandroidproject;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.csjcom.csjandroidlibrary.base.CSJActivity;
import com.csjcom.csjandroidlibrary.http.CSJHttpHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alun on 2017/11/30.
 */

public class AddExceptionsDialog extends Dialog implements View.OnClickListener, CSJHttpHelper.CSJHttpPostCallBack {
    View contentView;
    GJ data;
    CSJActivity activity;
    EditText etType, etContent;
    View btnUpload;

    public AddExceptionsDialog(@NonNull CSJActivity activity, GJ data) {
        super(activity, android.support.design.R.style.Base_Theme_AppCompat_Dialog_FixedSize);
        contentView = LayoutInflater.from(activity).inflate(R.layout.dialog_add_exceptions, null);
        setContentView(contentView, new ViewGroup.LayoutParams(500, 800));
        this.data = data;
        this.activity = activity;
        etType = findViewById(R.id.dialog_add_exceptions_et_type);
        etContent = findViewById(R.id.dialog_add_exceptions_et_content);
        btnUpload = findViewById(R.id.dialog_add_exceptions_btn_upload);
        btnUpload.setOnClickListener(this);
    }

    private void upload() {
        JSONObject jBody = new JSONObject();
        try {
            jBody.put("exceptiontype", etType.getText().toString());
            jBody.put("exceptioncontents", etContent.getText().toString());
            jBody.put("storeno", data.getSTORENO());
            CSJHttpHelper.post(activity, "addexceptions", jBody, this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        upload();
    }

    @Override
    public void onHttpPostFailure(String errMsg) {
        Toast.makeText(activity, errMsg, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onHttpPostSucceed(String jRes) {
        Toast.makeText(activity, jRes, Toast.LENGTH_LONG).show();
        dismiss();
    }
}
