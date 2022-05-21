package com.rupayan_housing.permission;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.fragment.app.FragmentActivity;

public class SharedPreferenceForZoneReprt {
    FragmentActivity context;

    public SharedPreferenceForZoneReprt(FragmentActivity context) {
        this.context = context;
    }

    public void saveZone(String zoneId) {//for save store Id
        SharedPreferences preferences = context.getSharedPreferences("ZONE_ID", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("zoneId", zoneId);
        editor.apply();
    }

    public String getZoneId() {
        SharedPreferences prfs = context.getSharedPreferences("ZONE_ID", Context.MODE_PRIVATE);
        String storeId = prfs.getString("zoneId", "");
        return storeId;
    }

    public void deleteZoneData() {
        SharedPreferences preferences = context.getSharedPreferences("ZONE_ID", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }
}
