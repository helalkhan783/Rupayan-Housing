package com.rupayan_housing.view.fragment.user;

public interface PermissionCheckedHandle {
    /**
     * For handle Checked permission operations
     */
    void checkedPermission(int permissionId);

    /**
     * For Handle unChecked Permission operations
     */
    void unCheckedPermission(int unCheckedPermissionId);
}
