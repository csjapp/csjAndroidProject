package com.csjcom.csjandroidlibrary.http;

import com.csjcom.csjandroidlibrary.base.CSJActivity;
import com.csjcom.csjandroidlibrary.utils.CSJEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by alun on 2017/11/29.
 */

public class CSJHttpHelper {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static void post(CSJActivity activity, String method, JSONObject jBody, CSJHttpPostCallBack callBack) throws JSONException {
        post(activity, method, jBody, null, callBack);
    }

    public static void post(final CSJActivity activity, String method, JSONObject jBody, String ticket, final CSJHttpPostCallBack callBack) throws JSONException {
        OkHttpClient client = new OkHttpClient();
        JSONObject jParams = new JSONObject();
        jParams.put("method", method);
        jParams.put("timestamp", System.currentTimeMillis() / 1000);
        jParams.put("nonce", Math.random());
        if (ticket != null) {
            jParams.put("ticket", ticket);
        }
        jParams.put("body", jBody);
        jParams.put("signature", sign(activity, jParams));
        RequestBody requestBody = RequestBody.create(JSON, jBody.toString());
        String url = activity.getCSJApiUrl() + "?method=" + jParams.getString("method");
        url += "&timestamp=" + jParams.getString("timestamp");
        url += "&nonce=" + jParams.getString("nonce");
        if (jParams.has("ticket"))
            url += "&ticket=" + jParams.getString("ticket");
        url += "&signature=" + jParams.getString("signature");
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                if (callBack != null)
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onHttpPostFailure(e.getMessage());
                        }
                    });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                if (callBack!=null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onHttpPostSucceed(res);
                        }
                    });
                }
            }
        });
    }

    private static String sign(CSJActivity activity, JSONObject jParams) throws JSONException {
        String unSign = "";
        unSign += "method=" + jParams.getString("method");
        unSign += "&timestamp=" + jParams.getString("timestamp");
        unSign += "&nonce=" + jParams.getString("nonce");
        if (jParams.has("ticket"))
            unSign += "&ticket=" + jParams.getString("ticket");
        unSign += "&secret=" + activity.getCSJSecret();
        unSign += "&body=" + jParams.getString("body");
        return CSJEncoder.md5(unSign);
    }

    public interface CSJHttpPostCallBack {
        void onHttpPostFailure(String errMsg);

        void onHttpPostSucceed(String jRes);
    }
}
