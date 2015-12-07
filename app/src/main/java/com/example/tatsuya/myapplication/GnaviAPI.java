package com.example.tatsuya.myapplication;

import android.content.Context;
import android.util.Log;

import com.example.tatsuya.myapplication.HttpConectionTools.HttpCallBack;
import com.example.tatsuya.myapplication.HttpConectionTools.HttpGet;
import com.example.tatsuya.myapplication.HttpConectionTools.UrlData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GnaviAPI {
    private static final String TAG = GnaviAPI.class.getSimpleName();
    private static final String AccessKey = GnaviAccessData.ACCESS_KEY;
    private Context context;
    private Map<String, String> params;
    private GnaviCallBack gnaviCallBack;

    public GnaviAPI(Context context) {
        this.context = context;
    }

    public void setParams(Map<String, String > params) {
        this.params = params;
    }

    public void startSearch() {
        StringBuilder builder = new StringBuilder();
        builder.append(UrlData.URL_TOP).append("?");
        builder.append("keyid=").append(AccessKey);
        for(String key : params.keySet()){
            builder.append("&").append(key).append("=").append(params.get(key));
        }
        HttpGet httpGet = new HttpGet(context);
        try {
            httpGet.setUrl(builder.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return;
        }
        httpGet.setCallBack(callBack);
        httpGet.execute();
    }

    public void setCallBack(GnaviCallBack callBack) {
        this.gnaviCallBack = callBack;
    }

    private HttpCallBack callBack = new HttpCallBack() {
        @Override
        public void onEndHttpCommunication(String result) {
            try {
                JSONObject json = new JSONObject(result);
                JSONArray restaurantList = json.getJSONArray("rest");
                Log.d(TAG, restaurantList.toString());
                List<RestaurantInfo> restaurantInfoList = new ArrayList<>();
                for(int i = 0; i < restaurantList.length(); i++) {
                    JSONObject restaurant = restaurantList.getJSONObject(i);
                    restaurantInfoList.add(new RestaurantInfo(restaurant));
                    Log.d(TAG, restaurantInfoList.get(i).toString());
                }
                gnaviCallBack.onEndGetData(restaurantInfoList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onErrorHttpCommunication() {

        }
    };
}
