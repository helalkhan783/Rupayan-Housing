package com.rupayan_housing.view.fragment.purchase.editPurchase;

import com.rupayan_housing.serverResponseModel.SalesRequisitionItems;

public interface EditEditPurchaseDataClickHandle {

    void removeBtn(int position);
    void insertQuantity(int position, String quantity, SalesRequisitionItems currentItem);
    void minusQuantity(int position, String quantity, SalesRequisitionItems currentItem);

}
