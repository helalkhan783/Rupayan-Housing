package com.rupayan_housing.view.fragment.production.iodization.addNew;

import com.rupayan_housing.serverResponseModel.SalesRequisitionItems;

public interface IodizationRecyclerItemClickHandle {
    void removeBtn(int position);
    void insertQuantity(int position, String quantity, SalesRequisitionItems currentItem);
    void minusQuantity(int position, String quantity, SalesRequisitionItems currentItem);
}
