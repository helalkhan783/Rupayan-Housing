package com.rupayan_housing.view.fragment.production.washingAndCrushing.addNew;

import com.rupayan_housing.serverResponseModel.SalesRequisitionItems;

public interface WashingAndCrushingAdapterItemClick {
    void removeBtn(int position);

    void insertQuantity(int position, String quantity, SalesRequisitionItems currentItem);

    void minusQuantity(int position, String quantity, SalesRequisitionItems currentItem);
}
