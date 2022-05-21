package com.rupayan_housing.validation_before_create_order;

import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.FragmentAddNewReconcilationBinding;
import com.rupayan_housing.serverResponseModel.SalesRequisitionItems;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class ValidationBeforeOrderCreate {
    FragmentActivity context;
    String selectedStore, whereComing, selectedEnterPrice;
    List<SalesRequisitionItems> itemsList;
    RecyclerView productList;
    private static boolean finalTotalCount = false;


    public ValidationBeforeOrderCreate(FragmentActivity context, String store, String selectedEnterPrice, List<SalesRequisitionItems> itemsList, RecyclerView productList, String whereComing) {
        this.context = context;
        this.selectedStore = store;
        this.selectedEnterPrice = selectedEnterPrice;
        this.itemsList = itemsList;
        this.productList = productList;
        this.whereComing = whereComing;
    }

    public void validation() {
        try {

            if (selectedEnterPrice == null) {
                Toasty.info(context, "Please select enterpeise", Toasty.LENGTH_LONG).show();
                return;
            }
            if (selectedStore == null) {
                Toasty.info(context, "Please select store", Toasty.LENGTH_LONG).show();

                return;
            }

            if (itemsList == null || itemsList.isEmpty()) {
                Toasty.info(context, "Please search item", Toasty.LENGTH_LONG).show();
                return;
            }


        } catch (Exception e) {
        }
    }

    public boolean ok() {
        try {
            for (int i = 0; i < itemsList.size(); i++) {
                String stock = ((TextView) productList.getLayoutManager().findViewByPosition(i).findViewById(R.id.stock)).getText().toString();
                String itemName = ((TextView) productList.getLayoutManager().findViewByPosition(i).findViewById(R.id.productName)).getText().toString();
                if (!whereComing.equals("AddNewPurchase")) {
                    if (Double.parseDouble(stock) == 0) {
                        Toasty.info(context, itemName + "'s " + " stock unavailable", Toasty.LENGTH_LONG).show();
                        return false;
                    }
                }
                if (Integer.parseInt(itemsList.get(i).getQuantity()) == 0) {
                    Toasty.info(context, "Enter quantity of " + itemName, Toasty.LENGTH_LONG).show();
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            Toasty.info(context, "Something Wrong", Toasty.LENGTH_LONG).show();
            return false;
        }

    }

}
