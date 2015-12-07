package com.example.tatsuya.myapplication;

import org.json.JSONException;
import org.json.JSONObject;

public class RestaurantInfo {
    public String name;

    public String imageUrl;

    public String description;

    public String address;

    public RestaurantInfo(JSONObject restaurant) {
        try {
            this.name = restaurant.getString("name");
            this.imageUrl = restaurant.getJSONObject("image_url").getString("shop_image1");

            this.address = restaurant.getString("address");
            StringBuilder builder = new StringBuilder();
            builder.append("住所：").append(restaurant.getString("address")).append("\n");
            builder.append("営業時間：").append(restaurant.getString("opentime")).append("\n");
            builder.append("休業日：").append(restaurant.getString("holiday"));
            this.description = builder.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("名前：").append(name).append("\n");
        builder.append("imageUrl：").append(imageUrl).append("\n");
        builder.append("説明:").append(description);
        return builder.toString();
    }
}
