package com.example.tatsuya.myapplication.HttpConectionTools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;

public class HttpGetImage extends AsyncTask<Void, Void, Bitmap> {
    private Context context;
    private final String TAG = HttpGet.class.getSimpleName();
    private HttpCommunication httpCommunication;
    private boolean isFailed;
    private HttpImageCallBack callBack;
    private String url;

    public HttpGetImage(Context context) {
        this.context = context;
        httpCommunication = new HttpCommunication();
        callBack = null;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setCallBack(HttpImageCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    protected void onPreExecute() {
        isFailed = false;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        if (!httpCommunication.checkNetWork(context)) {
            isFailed = true;
            return null;
        }
        Bitmap image = null;
        try {
            InputStream in = new java.net.URL(url).openStream();
            image = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            isFailed = true;
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return image;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
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
