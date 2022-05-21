package com.rupayan_housing.permission;

import androidx.fragment.app.FragmentActivity;

import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.utils.PermissionUtil;


import es.dmoral.toasty.Toasty;

public  class Permission {
    FragmentActivity context;
    public Permission(FragmentActivity context) {
        this.context = context;
    }

    public boolean checkPermission(Integer permissionId,String profileType){
        if (!profileType.equals("7")) {
            Toasty.info(context, "You don't  have permission for access this portion", Toasty.LENGTH_LONG).show();
            return false; }
        if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(permissionId) ||
                PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context)
                        .getUserCredentials().getPermissions()).contains(1)) {
            return true; }
        Toasty.info(context, "You don't  have permission for access this portion", Toasty.LENGTH_LONG).show();
        return false;
    }
}
