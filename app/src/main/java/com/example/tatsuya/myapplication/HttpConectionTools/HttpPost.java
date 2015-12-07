package com.example.tatsuya.myapplication.HttpConectionTools;

import android.content.Context;
import android.os.AsyncTask;

import java.net.MalformedURLException;

public class HttpPost extends AsyncTask<Void, Void, String> {
    private final String TAG = HttpPost.class.getSimpleName();
    HttpCommunication httpCommunication;
    private Context context;
    private boolean isFailed;
    private HttpCallBack callBack;

    public HttpPost(Context context) {
        this.context = context;
        httpCommunication = new HttpCommunication();
        callBack = null;
    }

    public void setCallBack(HttpCallBack callBack) {
        this.callBack = callBack;
    }

    public void setUrl(String url) throws MalformedURLException {
        httpCommunication.setUrl(url);
    }

    public void setPostData(String data) {
        httpCommunication.setPostData(data);
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
            return httpCommunication.getPostResult();
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
