package com.rupayan_housing.view.fragment.production.packating;

import com.rupayan_housing.serverResponseModel.PackatingModel;
import com.rupayan_housing.serverResponseModel.PacketingList;

public interface AddNewPackatingRecyclerClickHandle {
    void selectItemName(int holderPosition, PacketingList selectedItemMainId, PackatingModel model);
    void changeQuantity(String currentQuantity,int holderPosition, PackatingModel model);
    void delete(int position);
}
