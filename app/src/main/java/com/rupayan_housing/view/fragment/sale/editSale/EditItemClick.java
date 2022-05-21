package com.rupayan_housing.view.fragment.sale.editSale;

import com.rupayan_housing.serverResponseModel.SalesRequisitionItems;

public interface EditItemClick {
    void removeBtn(int position);
    void insertQuantity(int position, String quantity, SalesRequisitionItems currentItem);
    void minusQuantity(int position, String quantity, SalesRequisitionItems currentItem);

}
