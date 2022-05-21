package com.rupayan_housing.view.fragment.production.washingAndCrushing.edit;

import com.rupayan_housing.serverResponseModel.WashingCrushingModel;

public interface EditWashingCrushingItemClickHandle {
    void removeBtn(int position);
    void insertQuantity(int position, String quantity, WashingCrushingModel currentItem);
    void minusQuantity(int position, String quantity, WashingCrushingModel currentItem);
}
