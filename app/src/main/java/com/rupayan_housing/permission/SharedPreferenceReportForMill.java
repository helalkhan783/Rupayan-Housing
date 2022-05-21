package com.rupayan_housing.permission;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.fragment.app.FragmentActivity;

public class SharedPreferenceReportForMill {
    FragmentActivity context;

    public SharedPreferenceReportForMill(FragmentActivity context) {
        this.context = context;
    }

    public void saveMill(String customerId) {//for save store Id
        SharedPreferences preferences = context.getSharedPreferences("MILL_ID", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("customerId", customerId);
        editor.apply();
    }

    public String getMillId() {
        SharedPreferences prfs = context.getSharedPreferences("MILL_ID", Context.MODE_PRIVATE);
        String storeId = prfs.getString("customerId", "");
        return storeId;
    }

    public void deleteMillData() {
        SharedPreferences preferences = context.getSharedPreferences("MILL_ID", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

}
