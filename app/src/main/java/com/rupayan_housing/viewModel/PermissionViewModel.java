package com.rupayan_housing.viewModel;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.utils.PermissionUtil;

import java.util.List;

public class PermissionViewModel extends ViewModel {
    MutableLiveData<List<Integer>> accountPermissionList;

    public PermissionViewModel() {
        accountPermissionList = new MutableLiveData<>();
    }

    public MutableLiveData<List<Integer>> getAccountPermission(FragmentActivity context) {
        String permission = PreferenceManager.getInstance(context).getUserPermissions();
        List<Integer> permissionList = PermissionUtil.currentUserPermissionList(permission);
        accountPermissionList.postValue(permissionList);
        return accountPermissionList;
    }
}
