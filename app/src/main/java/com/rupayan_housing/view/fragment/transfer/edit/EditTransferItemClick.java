package com.rupayan_housing.view.fragment.transfer.edit;

import com.rupayan_housing.serverResponseModel.SalesRequisitionItems;

public interface EditTransferItemClick {
    void removeBtn(int position);

    void insertQuantity(int position, String quantity, SalesRequisitionItems currentItem);

    void minusQuantity(int position, String quantity, SalesRequisitionItems currentItem);
}
