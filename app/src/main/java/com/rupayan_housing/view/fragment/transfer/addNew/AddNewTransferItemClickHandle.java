package com.rupayan_housing.view.fragment.transfer.addNew;

import com.rupayan_housing.serverResponseModel.SalesRequisitionItems;

public interface
AddNewTransferItemClickHandle {
    void removeBtn(int position);
    void insertQuantity(int position, String quantity, SalesRequisitionItems currentItem);
    void minusQuantity(int position, String quantity, SalesRequisitionItems currentItem);

}
