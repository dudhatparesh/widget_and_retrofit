package com.enggdream.widgetapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Paresh on 4/13/2017.
 */

public class Data {
    @SerializedName("hashRate")
    @Expose
    private String hashRate;

    public String getHashRate() {
        return hashRate;
    }

    public void setHashRate(String hashRate) {
        this.hashRate = hashRate;
    }
}
