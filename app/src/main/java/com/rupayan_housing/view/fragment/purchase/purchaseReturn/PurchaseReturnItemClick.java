package com.rupayan_housing.view.fragment.purchase.purchaseReturn;

import com.rupayan_housing.serverResponseModel.PurchaseItems;

public interface PurchaseReturnItemClick {
    void insertQuantity(int position, String quantity, PurchaseItems currentItem);
}
