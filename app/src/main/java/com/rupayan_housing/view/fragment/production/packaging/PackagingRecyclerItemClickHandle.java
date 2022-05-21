package com.rupayan_housing.view.fragment.production.packaging;

import com.rupayan_housing.localDatabase.PackagingDatabaseModel;
import com.rupayan_housing.serverResponseModel.PackagingOriginItemList;

public interface PackagingRecyclerItemClickHandle {
    void selectItemName(int position, int adapterPosition, PackagingDatabaseModel model);

    void setPreviousSelectedItemName(int adapterPosition, String previousItemNameId, String currentSelectedPackedId);

    void selectPackedName(int position, int adapterPosition, PackagingDatabaseModel model);
    /**
     * For Handle realtime quantity change
     */
    void changeQuantity(String currentQuantity,int adapterPosition, PackagingDatabaseModel packagingDatabaseModel);
    /**
     * For Handle realtime note
     */
    void changeNote(String currentNote,String currentQuantity,int adapterPosition, PackagingDatabaseModel packagingDatabaseModel);

    void removeItem(int position);

}
