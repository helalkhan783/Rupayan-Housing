package com.rupayan_housing.view.fragment.customers;

public interface CustomerInterface {
    void getPosition(int position, String id);

    void getSupplierPosition(int position, String id);

    void getDataForEditStore(int position, String enterPriseId, String storeFullName, String storeShortname, String storeAddress,String storeId);
}
