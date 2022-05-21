package com.rupayan_housing.view.fragment.sale.salesReurn;

import com.rupayan_housing.serverResponseModel.PurchaseItems;

public interface SalesReturnItemClick {
    void insertQuantity(int position, String quantity, PurchaseItems currentItem);
}
