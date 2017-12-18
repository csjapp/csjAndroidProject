package com.csjcom.csjandroidlibrary.base;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by alun on 2017/11/29.
 */

public class CSJActivity extends AppCompatActivity {

    private CSJApplication getCSJApplication(){
        return (CSJApplication) getApplication();
    }


    public final String getCSJSecret() {
        return getCSJApplication().getCSJSecret();
    }

    public final String getCSJApiUrl(){
        return getCSJApplication().getCSJApiUrl();
    }
}
