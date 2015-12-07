package com.example.tatsuya.myapplication.HttpConectionTools;

public interface HttpCallBack {
    void onEndHttpCommunication(String result);

    void onErrorHttpCommunication();
}
