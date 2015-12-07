package com.example.tatsuya.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tatsuya.myapplication.HttpConectionTools.HttpGetImage;
import com.example.tatsuya.myapplication.HttpConectionTools.HttpImageCallBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends Activity {
    private static String TAG = MainActivity.class.getSimpleName();
    private LocationManager locationManager;
    private List<RestaurantInfo> restaurantInfoList = null;
    private Random random = new Random();
    private Location currentLocation;
    private Integer showedRestaurantIndex = null;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Button button = (Button) findViewById(R.id.search_button);
        button.setOnClickListener(searchButtonClickListener);
        button = (Button) findViewById(R.id.deside_button);
        button.setOnClickListener(decideButtonClickListener);
        imageView = (ImageView) findViewById(R.id.image);
    }

    private View.OnClickListener searchButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (restaurantInfoList == null) {
                getLocationAndSetRestaurantInfoList();
            } else {
                setShowedRestaurantIndex();
                updateViews();
            }
        }
    };

    private View.OnClickListener decideButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (restaurantInfoList != null && showedRestaurantIndex != null) {
                startNavigation(currentLocation, restaurantInfoList.get(showedRestaurantIndex).address);
            }
        }
    };

    private void getLocationAndSetRestaurantInfoList() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                0,
                0,
                locationListener
        );
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.removeUpdates(this);
            currentLocation = location;
            setRestaurantInfo(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private List<RestaurantInfo> setRestaurantInfo(Location location) {
        GnaviAPI gnaviAPI = new GnaviAPI(getApplicationContext());
        Map<String, String> params = new HashMap<>();
        params.put("format", "json");
        params.put("latitude", String.valueOf(location.getLatitude()));
        params.put("longitude", String.valueOf(location.getLongitude()));
        params.put("range", "3");
        params.put("hit_per_page", "100");
        gnaviAPI.setParams(params);
        gnaviAPI.setCallBack(gnaviCallBack);
        gnaviAPI.startSearch();
        return new ArrayList<>();
    }

    private void setShowedRestaurantIndex() {
        this.showedRestaurantIndex = random.nextInt(restaurantInfoList.size());
    }

    private void updateViews() {
        if (showedRestaurantIndex == null || showedRestaurantIndex >= restaurantInfoList.size()) {
            return;
        }
        RestaurantInfo restaurantInfo = restaurantInfoList.get(showedRestaurantIndex);
        TextView titleTextView = (TextView) findViewById(R.id.title);
        TextView descriptionTextView = (TextView) findViewById(R.id.description);
        titleTextView.setText(restaurantInfo.name);
        descriptionTextView.setText(restaurantInfo.description);
        if (!restaurantInfo.imageUrl.equals("{}")) {
            HttpGetImage httpGetImage = new HttpGetImage(getApplicationContext());
            httpGetImage.setUrl(restaurantInfo.imageUrl);
            httpGetImage.setCallBack(imageCallBack);
            httpGetImage.execute();
        } else {
            imageView.setImageResource(R.drawable.noimage);
        }
    }

    private void startNavigation(Location startLocation, String endAddr) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        intent.setData(Uri.parse("http://maps.google.com/maps?saddr=" + startLocation.getLatitude() + "," + startLocation.getLongitude() + "&daddr=" + endAddr + "&dirflg=w"));
        startActivity(intent);
    }

    private GnaviCallBack gnaviCallBack = new GnaviCallBack() {
        @Override
        public void onEndGetData(List<RestaurantInfo> restaurantInfoList) {
            MainActivity.this.restaurantInfoList = restaurantInfoList;
            setShowedRestaurantIndex();
            updateViews();
        }
    };

    private HttpImageCallBack imageCallBack = new HttpImageCallBack() {
        @Override
        public void onEndHttpCommunication(Bitmap result) {
            imageView.setImageBitmap(result);
        }

        @Override
        public void onErrorHttpCommunication() {

        }
    };
}
