package com.rupayan_housing.view.fragment.purchase.editPurchase;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.rupayan_housing.R;
import com.rupayan_housing.adapter.EditPurchaseAdapter;
import com.rupayan_housing.databinding.FragmentEditPurchaseDataBinding;
import com.rupayan_housing.localDatabase.MyDatabaseHelper;
import com.rupayan_housing.serverResponseModel.DuePaymentResponse;
import com.rupayan_housing.serverResponseModel.EditSaleItemsResponse;
import com.rupayan_housing.serverResponseModel.GetPreviousSaleInfoResponse;
import com.rupayan_housing.serverResponseModel.SalesRequisitionItems;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.view.fragment.sale.editSale.EditSaleData;
import com.rupayan_housing.view.fragment.sale.newSale.AddNewSale;
import com.rupayan_housing.viewModel.SaleViewModel;
import com.rupayan_housing.viewModel.UpdateMillerViewModel;
import com.rupayan_housing.viewModel.UpdateSaleViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class
EditPurchaseData extends BaseFragment implements EditEditPurchaseDataClickHandle {
    public static FragmentEditPurchaseDataBinding binding;
    public static String  customerName,companyName,address, customerPhone,customerId;


    private UpdateMillerViewModel updateMillerViewModel;
    private MyDatabaseHelper myDatabaseHelper;
    private SaleViewModel saleViewModel;
    private UpdateSaleViewModel updateSaleViewModel;

    private String NO_DATA_FOUND = "No Data Found";
    private boolean isDataFetching = false; // variable to detect whether server data fetch is loading or not

    List<SalesRequisitionItems> itemsList;
    EditPurchaseAdapter adapter;


    private static boolean finalTotalCount = false;
    private boolean allOk = true;

    private GetPreviousSaleInfoResponse getPreviousSaleInfoResponse;
    public static List<EditSaleItemsResponse> currentSaleItemList;


    /**
     * updated quantity product list (LAST QUANTITY UPDATED ALL PRODUCT LIST)
     */
    public static List<SalesRequisitionItems> updatedQuantityProductList = new ArrayList<>();
    public static List<String> updatedQuantityList = new ArrayList<>();


    private String siId;
    private String selectedStore;
    private String orderId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_purchase_data, container, false);


        myDatabaseHelper = new MyDatabaseHelper(getContext());
        updateMillerViewModel = new ViewModelProvider(this).get(UpdateMillerViewModel.class);
        saleViewModel = new ViewModelProvider(this).get(SaleViewModel.class);
        updateSaleViewModel = new ViewModelProvider(this).get(UpdateSaleViewModel.class);
        getDataFromPreviousFragment();
        binding.toolbar.setClickHandle(() -> {
            hideKeyboard(getActivity());
            getActivity().onBackPressed();
        });
        /**
         * Now get Sale Data from previous Fragment
         */
        getSaleDataFromServer();

        /**
         * now submit edit ProductInfo
         */
        binding.setClickHandle(() -> {
       goToConfirmPage();
        });

        binding.confirmBtn.setOnClickListener(v -> goToConfirmPage());
        return binding.getRoot();
    }

    private void goToConfirmPage() {
        if (itemsList == null || itemsList.isEmpty()) {
            return;
        }
        finalTotalCount = true;
        getFinalAllQuantity();//for count updated quantity object

        updatedQuantityProductList.clear();
        updatedQuantityProductList.addAll(itemsList);
        if (updatedQuantityProductList.isEmpty()) {
            infoMessage(getActivity().getApplication(), "Empty Quantity");
            return;
        }

        for (int i = 0; i < updatedQuantityProductList.size(); i++) {
            if (Double.parseDouble(updatedQuantityProductList.get(i).getQuantity()) == 0) {

                return;
            }
        }
        /**
         * validation and save
         */
        validationRecyclerData();
    }

    private void getDataFromPreviousFragment() {
        assert getArguments() != null;
        siId = getArguments().getString("sid");
        binding.toolbar.toolbarTitle.setText(""+getArguments().getString("pageName"));
    }

    private void getSaleDataFromServer() {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }
        updateMillerViewModel.getEditPurchasePageInfo(getActivity(), siId)
                .observe(getViewLifecycleOwner(), response -> {
                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "Something Wrong");
                        return;
                    }
                    if (response.getPaymentsCount() > 1) {//this is one kind of purchase edit permission
                        binding.mainPortion.setVisibility(View.GONE);
                        binding.noDataFound.setVisibility(View.VISIBLE);
                        binding.noDataFound.setText(PermissionUtil.permissionMessage);
                        return;
                    }

                    getPreviousSaleInfoResponse = response;//store current page whole data
                    currentSaleItemList = response.getOrderDetails().getItems();//store current sales items

                    /**
                     * now set previous selected store
                     */
                    selectedStore = response.getOrder().getStoreID();////////////////////////ERROR
                    orderId = response.getOrder().getOrderID();
                    customerName = response.getOrderDetails().getCustomer().getCustomerFname();
                    companyName = response.getOrderDetails().getCustomer().getCompanyName();
                    address = response.getOrderDetails().getCustomer().getAddress();
                    customerPhone = response.getOrderDetails().getCustomer().getPhone();
                    customerId = response.getOrder().getCustomerID();
                    /**
                     * save data
                     */
                    for (int i = 0; i < response.getOrderDetails().getItems().size(); i++) {
                        long rowId = myDatabaseHelper.insertData(
                                response.getOrderDetails().getItems().get(i).getProductID(),
                                response.getOrderDetails().getItems().get(i).getItem(),
                                response.getOrderDetails().getItems().get(i).getQuantity(),
                                response.getOrderDetails().getItems().get(i).getUnit(),
                                ""
                        );

                        if (rowId == -1) {
                            //   Toast.makeText(getContext(), "Failed Inserted ", Toast.LENGTH_SHORT).show();
                        } else {
                            // Toast.makeText(getContext(), "Successfully Inserted in Row = " + rowId, Toast.LENGTH_SHORT).show();
                        }
                    }
                    /**
                     * now get all local data from db
                     */
                    Cursor cursor = myDatabaseHelper.displayAllData();
                    if (cursor.getCount() == 0) {//means didn't have any data
                        Toast.makeText(getContext(), "There Is No Data", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    itemsList = new ArrayList<>();
                    itemsList.clear();
                    while (cursor.moveToNext()) {
                        SalesRequisitionItems item = new SalesRequisitionItems();
                        item.setProductID(cursor.getString(1));
                        item.setProductTitle(cursor.getString(2));
                        item.setQuantity(cursor.getString(3));
                        item.setUnit(cursor.getString(4));
                        item.setUnit_name(cursor.getString(5));
                        itemsList.add(item);
                    }
                    adapter = new EditPurchaseAdapter(getActivity(), itemsList, EditPurchaseData.this);
                    binding.itemListRv.setLayoutManager(new LinearLayoutManager(getActivity()));
                    binding.itemListRv.setAdapter(adapter);


                    /**
                     * now control stock
                     */

                    /**
                     * after set store load all product stock data------
                     */
                    List<String> productIdList = new ArrayList<>();
                    List<String> soldFromList = new ArrayList<>();
                    productIdList.clear();
                    soldFromList.clear();


                    for (int i = 0; i < itemsList.size(); i++) {
                        try {
                            productIdList.add(itemsList.get(i).getProductID());
                            soldFromList.add(currentSaleItemList.get(i).getSoldFrom());
                        } catch (Exception e) {
                            Log.d("Error", "" + e.getMessage());
                        }
                    }
                    saleViewModel.getItemStockList(getActivity(), productIdList, soldFromList)
                            .observe(getViewLifecycleOwner(), stockResponse -> {
                                if (stockResponse == null) {
                                    errorMessage(getActivity().getApplication(), "Something Wrong");
                                    return;
                                }

                                if (response.getStatus() == 400) {
                                    infoMessage(getActivity().getApplication(), " ");
                                    return;
                                }

                                for (int i = 0; i < stockResponse.getLists().size(); i++) {
                                    try {
                                        binding.itemListRv.getLayoutManager()
                                                .findViewByPosition(i).findViewById(R.id.stock).setVisibility(View.VISIBLE);
                                        ((EditText) binding.itemListRv.getLayoutManager().findViewByPosition(i)
                                                .findViewById(R.id.stock)).setText(stockResponse.getLists().get(i).getStockQty() + " Available");
                                    } catch (Exception e) {
                                        Log.d("ERROR", "" + e.getMessage());
                                    }
                                }

                            });

                });
    }

    @Override
    public void removeBtn(int position) {
        allOk = true;//for  handle submit time validation

        if (position > itemsList.size() - 1) {
            adapter.notifyItemRangeRemoved(0, itemsList.size());
            return;
        }
        int value = myDatabaseHelper.deleteData(itemsList.get(position).getProductID());
        if (value > 0) {
            itemsList.remove(position);
            binding.itemListRv.getAdapter().notifyItemRemoved(position);
//            /**
//             * now manage loading time total quantity
//             */
//            int totalQuantity = 0;
//            for (int i = 0; i < itemsList.size(); i++) {
//                totalQuantity += Integer.parseInt(itemsList.get(i).getQuantity());
//            }
//            EditPurchaseData.binding.totalQuantity.setText("Total Quantity: " + String.valueOf(totalQuantity));
            return;
        }
        Toast.makeText(getContext(), "delete failed ", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void insertQuantity(int position, String quantity, SalesRequisitionItems currentItem) {

        allOk = true;//for  handle submit time validation
        Boolean isUpdated = myDatabaseHelper
                .updateData(currentItem.getProductID(), currentItem.getProductTitle(), quantity, currentItem.getUnit(), currentItem.getUnit_name());

        if (isUpdated) {
            //Toast.makeText(getContext(), "Updated ", Toast.LENGTH_SHORT).show();
        } else {
            // Toast.makeText(getContext(), "failed update ", Toast.LENGTH_SHORT).show();
        }


        /**
         * now get all local data from db
         */
        Cursor cursor = myDatabaseHelper.displayAllData();
        if (cursor.getCount() == 0) {//means didn't have any data
            // Toast.makeText(getContext(), "There Is No Data", Toast.LENGTH_SHORT).show();
            return;
        }

        itemsList.clear();
        while (cursor.moveToNext()) {
            SalesRequisitionItems item = new SalesRequisitionItems();
            item.setProductID(cursor.getString(1));
            item.setProductTitle(cursor.getString(2));
            item.setQuantity(cursor.getString(3));
            item.setUnit(cursor.getString(4));
            item.setUnit_name(cursor.getString(5));
            itemsList.add(item);
        }

    }

    @Override
    public void minusQuantity(int position, String quantity, SalesRequisitionItems currentItem) {
        allOk = true;//for  handle submit time validation
        Boolean isUpdated = myDatabaseHelper
                .updateData(currentItem.getProductID(), currentItem.getProductTitle(), quantity, currentItem.getUnit(), currentItem.getUnit_name());

        if (isUpdated) {
            //Toast.makeText(getContext(), "Updated ", Toast.LENGTH_SHORT).show();
        } else {
            // Toast.makeText(getContext(), "failed update ", Toast.LENGTH_SHORT).show();
        }


        /**
         * now get all local data from db
         */
        Cursor cursor = myDatabaseHelper.displayAllData();
        if (cursor.getCount() == 0) {//means didn't have any data
            // Toast.makeText(getContext(), "There Is No Data", Toast.LENGTH_SHORT).show();
            return;
        }

        itemsList.clear();
        while (cursor.moveToNext()) {
            SalesRequisitionItems item = new SalesRequisitionItems();
            item.setProductID(cursor.getString(1));
            item.setProductTitle(cursor.getString(2));
            item.setQuantity(cursor.getString(3));
            item.setUnit(cursor.getString(4));
            item.setUnit_name(cursor.getString(5));
            itemsList.add(item);
        }
    }

    public static List<String> getAllQuantity() {
        List<String> paymentLimitAmount = new ArrayList<>();
        try {
            for (int i = 0; i < binding.itemListRv.getChildCount(); i++) {
                try {
                    paymentLimitAmount.add(((EditText) binding.itemListRv.getLayoutManager().findViewByPosition(i).findViewById(R.id.quantityEt)).getText().toString());
                } catch (Exception e) {
                    Log.d("ERROR", e.getMessage());
                }

            }
        } catch (Exception e) {
            Log.d("ERROR", e.getMessage());
        }
        return paymentLimitAmount;
    }

    public List<String> getFinalAllQuantity() {
        List<String> paymentLimitAmount = new ArrayList<>();
        updatedQuantityProductList.clear();
        updatedQuantityList.clear();
        for (int i = 0; i < binding.itemListRv.getChildCount(); i++) {
            try {
                /**
                 * for handle updated quantity object
                 */
                if (finalTotalCount) {
                    String currentQuantity = ((EditText) binding.itemListRv.getLayoutManager().findViewByPosition(i).findViewById(R.id.quantityEt)).getText().toString();
                    if (!currentQuantity.equals("0")) {
                        updatedQuantityProductList.add(itemsList.get(i));//set updated product quantity object
                        updatedQuantityList.add(currentQuantity);
                    }
//                    finalTotalCount= false;
                }
            } catch (Exception e) {
                Log.d("ERROR", e.getMessage());
            }
        }
        return paymentLimitAmount;
    }


    private void validationRecyclerData() {
        hideKeyboard(getActivity());
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }

        try {
            Bundle bundle = new Bundle();
            if (selectedStore == null) {
                bundle.putString("selectedStoreId", "");
            } else {
                bundle.putString("selectedStoreId", selectedStore);
            }
            bundle.putString("siId", siId);
            bundle.putString("orderId", orderId);
            Navigation.findNavController(getView()).navigate(R.id.action_editPurchaseData_to_confirmEditPurchase, bundle);
        } catch (Exception e) {
            Log.d("ERROR", "error");
        }

    }

}