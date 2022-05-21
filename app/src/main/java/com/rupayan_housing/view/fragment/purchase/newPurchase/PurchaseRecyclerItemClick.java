package com.rupayan_housing.view.fragment.purchase.newPurchase;

import com.rupayan_housing.serverResponseModel.SalesRequisitionItems;

public interface PurchaseRecyclerItemClick {
    void removeBtn(int position);
    void insertQuantity(int position, String quantity, SalesRequisitionItems currentItem);
    void minusQuantity(int position, String quantity, SalesRequisitionItems currentItem);
}
