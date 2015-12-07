package com.example.tatsuya.myapplication.HttpConectionTools;

import android.graphics.Bitmap;

public interface HttpImageCallBack {
    void onEndHttpCommunication(Bitmap result);

    void onErrorHttpCommunication();
}
