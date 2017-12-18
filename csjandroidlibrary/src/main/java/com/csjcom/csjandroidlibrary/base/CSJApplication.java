package com.csjcom.csjandroidlibrary.base;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * Created by alun on 2017/11/29.
 */

public class CSJApplication extends Application {
    private String csjSecret;
    private String csjApiUrl;

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationInfo appInfo = null;
        try {
            appInfo = getPackageManager().getApplicationInfo(getPackageName(),
                    PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        csjSecret = appInfo.metaData.getString("csj.app.secret");
        csjApiUrl = appInfo.metaData.getString("csj.app.api");
    }

    public final String getCSJSecret() {
        return csjSecret;
    }

    public final String getCSJApiUrl() {
        return csjApiUrl;
    }
}
