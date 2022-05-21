package com.rupayan_housing.utils;

import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.rupayan_housing.view.fragment.BaseFragment;

import lombok.AllArgsConstructor;


public class InternetCheckerRecyclerBuddy extends BaseFragment {
    FragmentActivity fragmentActivity;

    public InternetCheckerRecyclerBuddy(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
    }

    public boolean isInternetAvailableHere(RecyclerView recyclerView, TextView dataNotFound) {
        if (!isInternetOn(this.fragmentActivity)) {
            recyclerView.setVisibility(View.GONE);
            dataNotFound.setVisibility(View.VISIBLE);
            dataNotFound.setText("Please Check Your Internet Connection");
            return false;
        }
        return true;
    }

}
