package com.example.tatsuya.myapplication.HttpConectionTools;

import android.content.Context;
import android.os.AsyncTask;

import java.net.MalformedURLException;

public class HttpGet extends AsyncTask<Void, Void, String> {
    private Context context;
    private final String TAG = HttpGet.class.getSimpleName();
    private HttpCommunication httpCommunication;
    private boolean isFailed;
    private HttpCallBack callBack;

    public HttpGet(Context context) {
        this.context = context;
        httpCommunication = new HttpCommunication();
        callBack = null;
    }

    public void setUrl(String url) throws MalformedURLException {
        httpCommunication.setUrl(url);
    }

    public void setCallBack(HttpCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    protected void onPreExecute() {
        isFailed = false;
    }

    @Override
    protected String doInBackground(Void... params) {
        if (!httpCommunication.checkNetWork(context)) {
            isFailed = true;
            return "";
        }
        try {
            httpCommunication.connection();
            return httpCommunication.getGetResult();
        } catch (Exception e) {
            e.printStackTrace();
            isFailed = true;
            return "";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (callBack == null) {
            return;
        }
        if (isFailed) {
            callBack.onErrorHttpCommunication();
        } else {
            callBack.onEndHttpCommunication(result);
        }
    }
}
