package com.rupayan_housing.view.fragment.production.iodization.edit;

import com.rupayan_housing.serverResponseModel.WashingCrushingModel;

public interface IodizationEditItemClickHandle {
    void removeBtn(int position);
    void insertQuantity(int position, String quantity, WashingCrushingModel currentItem);
    void minusQuantity(int position, String quantity, WashingCrushingModel currentItem);
}
