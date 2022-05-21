package com.rupayan_housing.permission;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;

import com.rupayan_housing.view.fragment.store.StoreListFragment;

public class HelperClass {
FragmentActivity context;

    public HelperClass(FragmentActivity context) {
        this.context = context;
    }

    public void navigate(String porson, View root, int destinationPath) {
        Bundle bundle = new Bundle();
        bundle.putString("porson", porson);
        bundle.putString("pageName", "Stock List");
        StoreListFragment.manage = 0;
        StoreListFragment.endScroll = false;
        StoreListFragment.pageNumber = 1;
        StoreListFragment.isFirstLoad = 0;
        Navigation.findNavController(root).navigate(destinationPath, bundle);
    }
}
