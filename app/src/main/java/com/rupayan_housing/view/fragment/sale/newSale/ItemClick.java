package com.rupayan_housing.view.fragment.sale.newSale;

import com.rupayan_housing.serverResponseModel.SalesRequisitionItems;

public interface ItemClick {
    void removeBtn(int position);
    void insertQuantity(int position, String quantity, SalesRequisitionItems currentItem);
    void minusQuantity(int position, String quantity, SalesRequisitionItems currentItem);
}
