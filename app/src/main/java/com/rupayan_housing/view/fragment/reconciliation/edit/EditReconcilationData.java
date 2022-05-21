package com.rupayan_housing.view.fragment.reconciliation.edit;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rupayan_housing.R;
import com.rupayan_housing.adapter.EditReconciliationAdapter;
import com.rupayan_housing.databinding.FragmentEditReconcilationDataBinding;
import com.rupayan_housing.localDatabase.MyDatabaseHelper;
import com.rupayan_housing.serverResponseModel.EditReconcilationPageResponse;
import com.rupayan_housing.serverResponseModel.EditSaleItemsResponse;
import com.rupayan_housing.serverResponseModel.EnterpriseList;
import com.rupayan_housing.serverResponseModel.EnterpriseResponse;
import com.rupayan_housing.serverResponseModel.GetEnterpriseResponse;
import com.rupayan_housing.serverResponseModel.SalesRequisitionItems;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.view.fragment.transfer.addNew.AddNewTransfer;
import com.rupayan_housing.viewModel.ReconcilationViewModel;
import com.rupayan_housing.viewModel.SaleViewModel;
import com.rupayan_housing.viewModel.SalesRequisitionViewModel;
import com.rupayan_housing.viewModel.TransferViewModel;

import java.util.ArrayList;
import java.util.List;

public class EditReconcilationData extends BaseFragment implements EditReconciliationItemClick {

    public static FragmentEditReconcilationDataBinding binding;

    private MyDatabaseHelper myDatabaseHelper;
    private SalesRequisitionViewModel salesRequisitionViewModel;
    private ReconcilationViewModel reconcilationViewModel;
    private SaleViewModel saleViewModel;
    private TransferViewModel transferViewModel;
    /**
     * For get store list
     */
    List<SalesRequisitionItems> itemsList;
    EditReconciliationAdapter adapter;


    /**
     * For store initial product list
     */
    public static List<SalesRequisitionItems> initProductListResponse;
    /**
     * updated quantity product list (LAST QUANTITY UPDATED ALL PRODUCT LIST)
     */
    public static List<SalesRequisitionItems> updatedQuantityProductList = new ArrayList<>();
    public static List<String> updatedQuantityList = new ArrayList<>();

    /**
     * For enterprise
     */
    List<EnterpriseResponse> enterpriseResponseList;
    List<String> enterpriseNameList;
    /**
     * for store
     */
    List<EnterpriseList> storeResponseList;
    List<String> storeNameList;

    private String selectedEnterPrice, selectedStore;
    public  static  String reconciliationType;



    String previousOrderSid, orderSerial;


    private static boolean finalTotalCount = false;
    private boolean allOk = true;
    public static EditReconcilationPageResponse previousReconcilationInfoResponse;
    public static List<EditSaleItemsResponse> currentSaleItemList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_reconcilation_data, container, false);
        reconcilationViewModel = new ViewModelProvider(this).get(ReconcilationViewModel.class);
        binding.toolbar.toolbarTitle.setText("Update Reconciliation");
        binding.toolbar.setClickHandle(() -> {
            hideKeyboard(getActivity());
            getActivity().onBackPressed();
        });


        salesRequisitionViewModel = new ViewModelProvider(this).get(SalesRequisitionViewModel.class);
        saleViewModel = new ViewModelProvider(this).get(SaleViewModel.class);
        myDatabaseHelper = new MyDatabaseHelper(getContext());
        transferViewModel = new ViewModelProvider(this).get(TransferViewModel.class);
        initProductListResponse = new ArrayList<>();
        initProductListResponse.clear();

        getLocalDataIfHave();
        getDataFromPreviousFragment();

        binding.toolbar.setClickHandle(() -> {
            hideKeyboard(getActivity());
            getActivity().onBackPressed();
        });
        /**
         * For Submit Data
         */
        binding.totalQuantity.setOnClickListener(v -> {
            if (!(isInternetOn(getActivity()))) {
                infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
                return;
            }

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
          /*  if (selectedStore == null) {
                binding.store.setEnableErrorLabel(true);
                binding.store.setErrorText("Empty");
                return;
            }*/

            initProductListResponse.clear();

            validationAndSubmit();

        });


        /**
         * for set Page Data
         */
        getPageDataFromServer();

        getPreviousReconcilationDataFromServer();

        binding.enterPrice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedEnterPrice = enterpriseResponseList.get(position).getStoreID();
                binding.enterPrice.setEnableErrorLabel(false);
                /***
                 * ok now set store dropdown data by (optional) enterprise id
                 */
                nowSetStoreListByEnterPriseId();
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.store.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (selectedEnterPrice == null) {
                    infoMessage(getActivity().getApplication(), "Select Enterprise First !!");
                    return;
                }
                selectedStore = storeResponseList.get(position).getStoreID();
                binding.store.setErrorText("");
                binding.store.setEnableErrorLabel(false);
                allOk = true;

                /**
                 * Now handle stock  if have selected store
                 */

                /**
                 * after set store load all product stock data------
                 */
                List<String> productIdList = new ArrayList<>();
                /**
                 * for handle stock
                 */
                if (selectedStore != null) {
                    productIdList.clear();
                    if (itemsList == null) {
                        return;
                    }
                    List<String> soldFromList = new ArrayList<>();
                    List<String> oldSoldFromList = new ArrayList<>();
                    List<String> productTitleList = new ArrayList<>();
                    List<String> quantityList = new ArrayList<>();
                    List<String> previousQuantityList = new ArrayList<>();
                    productIdList.clear();
                    soldFromList.clear();
                    productTitleList.clear();
                    quantityList.clear();
                    previousQuantityList.clear();
                    oldSoldFromList.clear();

                    for (int i = 0; i < itemsList.size(); i++) {
                        try {
                            productIdList.add(itemsList.get(i).getProductID());
                            soldFromList.add(currentSaleItemList.get(i).getSoldFrom());
                            productTitleList.add(currentSaleItemList.get(i).getProduct_title());
                            quantityList.add(currentSaleItemList.get(i).getQuantity());
                            previousQuantityList.add(previousReconcilationInfoResponse.getItems().get(i).getQuantity());
                            oldSoldFromList.add(previousReconcilationInfoResponse.getItems().get(i).getSoldFrom());
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

                                if (stockResponse.getStatus() == 400) {
                                    infoMessage(getActivity().getApplication(), " ");
                                    return;
                                }

                                for (int i = 0; i < stockResponse.getLists().size(); i++) {
                                    try {
                                        binding.productList.getLayoutManager()
                                                .findViewByPosition(i).findViewById(R.id.stock).setVisibility(View.VISIBLE);
                                        ((TextView) binding.productList.getLayoutManager().findViewByPosition(i)
                                                .findViewById(R.id.stock)).setText(stockResponse.getLists().get(i).getStockQty() + "");
                                    } catch (Exception e) {
                                        Log.d("ERROR", "" + e.getMessage());
                                    }
                                }

                            });
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return binding.getRoot();
    }

    private void getDataFromPreviousFragment() {
        assert getArguments() != null;
        previousOrderSid = getArguments().getString("id");
        //orderSerial = getArguments().getString("orderSerial");
    }

    private void getLocalDataIfHave() {
        /**
         * now get all local data from db
         */
        Cursor cursor = myDatabaseHelper.displayAllData();
        if (cursor.getCount() == 0) {//means didn't have any data
            // Toast.makeText(getContext(), "There Is No Data", Toast.LENGTH_SHORT).show();
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
        adapter = new EditReconciliationAdapter(getActivity(), itemsList, null, this);
        binding.productList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.productList.setAdapter(adapter);
    }

    private void getPageDataFromServer() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        salesRequisitionViewModel.getEnterpriseResponse(getActivity())
                .observe(getViewLifecycleOwner(), response -> {
                    progressDialog.dismiss();
                    try {
                        if (response == null) {
                            errorMessage(getActivity().getApplication(), "Something Wrong");
                            return;
                        }
                        if (response.getStatus() == 400) {
                            infoMessage(getActivity().getApplication(), "" + response.getMessage());
                            return;
                        }
                    } catch (Exception e) {
                        Log.d("ERROR", "" + e.getMessage());
                    }
                    /**
                     * all Ok now set data to view
                     */
                    setPageData(response);
                });
    }

    private void setPageData(GetEnterpriseResponse response) {

        enterpriseResponseList = new ArrayList<>();
        enterpriseResponseList.clear();
        enterpriseNameList = new ArrayList<>();
        enterpriseNameList.clear();
        enterpriseResponseList.addAll(response.getEnterprise());

        for (int i = 0; i < response.getEnterprise().size(); i++) {
            enterpriseNameList.add("" + response.getEnterprise().get(i).getStoreName());
        }
        binding.enterPrice.setItem(enterpriseNameList);
        if (enterpriseResponseList.size() == 1) {
            binding.enterPrice.setSelection(0);
            selectedEnterPrice = enterpriseResponseList.get(0).getStoreID();
        }
    }

    public static List<String> getAllQuantity() {
        List<String> paymentLimitAmount = new ArrayList<>();
        try {
            for (int i = 0; i < binding.productList.getChildCount(); i++) {
                try {
                    paymentLimitAmount.add(((EditText) binding.productList.getLayoutManager().findViewByPosition(i).findViewById(R.id.quantityEt)).getText().toString());
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
        for (int i = 0; i < binding.productList.getChildCount(); i++) {
            try {
                /**
                 * for handle updated quantity object
                 */
                if (finalTotalCount) {
                    String currentQuantity = ((EditText) binding.productList.getLayoutManager().findViewByPosition(i).findViewById(R.id.quantityEt)).getText().toString();
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
            binding.productList.getAdapter().notifyItemRemoved(position);
            /**
             * now manage loading time total quantity
             */
            double totalQuantity = 0;
            for (int i = 0; i < itemsList.size(); i++) {
                totalQuantity += Double.parseDouble(itemsList.get(i).getQuantity());
            }
            AddNewTransfer.binding.totalQuantity.setText("Total Quantity: " + String.valueOf(totalQuantity));
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


    private void nowSetStoreListByEnterPriseId() {
        saleViewModel.getStoreListByOptionalEnterpriseId(getActivity(), selectedEnterPrice)
                .observe(getViewLifecycleOwner(), response -> {
                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "Something Wrong");
                        return;
                    }
                    if (response.getStatus() != 200) {
                        errorMessage(getActivity().getApplication(), "Something Wrong");
                        return;
                    }

                    storeResponseList = new ArrayList<>();
                    storeResponseList.clear();
                    storeNameList = new ArrayList<>();
                    storeNameList.clear();

                    storeResponseList.addAll(response.getEnterprise());

                    for (int i = 0; i < response.getEnterprise().size(); i++) {
                        storeNameList.add(response.getEnterprise().get(i).getStoreName());
                        if (selectedStore !=null){
                            if (selectedStore.equals(response.getEnterprise().get(i).getStoreID())){
                                binding.store.setSelection(i);
                            }
                        }
                    }
                    binding.store.setItem(storeNameList);


//                    for (int i = 0; i < storeResponseList.size(); i++) {
//                        if (storeResponseList.get(i).getStoreID().equals(previousReconcilationInfoResponse.getOrderInfo().getStoreID())) {
//                            binding.store.setSelection(i);
//                            break;
//                        }
//                    }
                    if (storeResponseList.size() == 1) {
                        binding.store.setSelection(0);
                        selectedStore = storeResponseList.get(0).getStoreID();
                    }
                });
    }

    private void getPreviousReconcilationDataFromServer() {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }


        reconcilationViewModel.getEditReconcilationPageData(getActivity(), previousOrderSid)
                .observe(getViewLifecycleOwner(), response -> {
                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "Something Wrong");
                        return;
                    }

                    if (response.getStatus() == 400) {
                        infoMessage(getActivity().getApplication(), "Backend ERROR");
                        return;
                    }

                    previousReconcilationInfoResponse = response;//store current page whole data

                    currentSaleItemList = response.getItems();
                    if (selectedEnterPrice == null){
                        selectedEnterPrice = response.getOrderInfo().getStoreID();
                    }



                    reconciliationType = response.getReconciliationType();
                    selectedStore = response.getItems().get(0).getSoldFrom();

                    /**
                     * now set previous selected store
                     */
                    // selectedStore = response.getOrder().getStoreID();

                    //orderId = response.getOrder().getOrderID();
                    /**
                     * save data
                     */
                    for (int i = 0; i < response.getItems().size(); i++) {
                        long rowId = myDatabaseHelper.insertData(
                                response.getItems().get(i).getProductID(),
                                response.getItems().get(i).getProduct_title(),
                                response.getItems().get(i).getQuantity(),
                                response.getItems().get(i).getUnit(),
                                ""
                        );

                        if (rowId == -1) {
                            // Toast.makeText(getContext(), "Failed Inserted ", Toast.LENGTH_SHORT).show();
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
                    adapter = new EditReconciliationAdapter(getActivity(), itemsList, null, EditReconcilationData.this);
                    binding.productList.setLayoutManager(new LinearLayoutManager(getActivity()));
                    binding.productList.setAdapter(adapter);


                    /**
                     * now control stock
                     */

                    /**
                     * after set store load all product stock data------
                     */
                    List<String> productIdList = new ArrayList<>();
                    List<String> soldFromList = new ArrayList<>();
                    List<String> oldSoldFromList = new ArrayList<>();
                    List<String> productTitleList = new ArrayList<>();
                    List<String> quantityList = new ArrayList<>();
                    List<String> previousQuantityList = new ArrayList<>();
                    productIdList.clear();
                    soldFromList.clear();
                    productTitleList.clear();
                    quantityList.clear();
                    previousQuantityList.clear();
                    oldSoldFromList.clear();

                    for (int i = 0; i < itemsList.size(); i++) {
                        try {
                            productIdList.add(itemsList.get(i).getProductID());
                            soldFromList.add(currentSaleItemList.get(i).getSoldFrom());
                            productTitleList.add(itemsList.get(i).getProductTitle());
                            quantityList.add(itemsList.get(i).getQuantity());
                            previousQuantityList.add(previousReconcilationInfoResponse.getItems().get(i).getQuantity());
                            oldSoldFromList.add(previousReconcilationInfoResponse.getItems().get(i).getSoldFrom());
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

                                if (stockResponse.getStatus() == 400) {
                                    infoMessage(getActivity().getApplication(), " ");
                                    return;
                                }

                                for (int i = 0; i < stockResponse.getLists().size(); i++) {
                                    try {
                                        binding.productList.getLayoutManager()
                                                .findViewByPosition(i).findViewById(R.id.stock).setVisibility(View.VISIBLE);
                                        ((TextView) binding.productList.getLayoutManager().findViewByPosition(i)
                                                .findViewById(R.id.stock)).setText(stockResponse.getLists().get(i).getStockQty() + "");
                                    } catch (Exception e) {
                                        Log.d("ERROR", "" + e.getMessage());
                                    }
                                }

                            });

                });


    }

    private void validationAndSubmit() {
        if (selectedEnterPrice == null) {
            infoMessage(getActivity().getApplication(), "Please select enterprise");
            return;
        }
        if (selectedStore == null) {
            infoMessage(getActivity().getApplication(), "Please select store");
            return;
        }

        List<String> productIdList = new ArrayList<>();
        List<String> soldFromList = new ArrayList<>();
        List<String> oldSoldFromList = new ArrayList<>();
        List<String> productTitleList = new ArrayList<>();
        List<String> quantityList = new ArrayList<>();
        List<String> previousQuantityList = new ArrayList<>();
        productIdList.clear();
        soldFromList.clear();
        productTitleList.clear();
        quantityList.clear();
        previousQuantityList.clear();
        oldSoldFromList.clear();

        for (int i = 0; i < itemsList.size(); i++) {
            try {
                productIdList.add(itemsList.get(i).getProductID());
                soldFromList.add(currentSaleItemList.get(i).getSoldFrom());
                productTitleList.add(itemsList.get(i).getProductTitle());
                quantityList.add(itemsList.get(i).getQuantity());
                previousQuantityList.add(previousReconcilationInfoResponse.getItems().get(i).getQuantity());
                oldSoldFromList.add(previousReconcilationInfoResponse.getItems().get(i).getSoldFrom());
            } catch (Exception e) {
                Log.d("Error", "" + e.getMessage());
            }
        }
        hideKeyboard(getActivity());


        saleViewModel.getProductStockDataByProductId(getActivity(), productIdList, selectedStore)
                .observe(getViewLifecycleOwner(), response -> {
                    for (int i = 0; i < response.getLists().size(); i++) {
                        if (response.getLists().get(i).getStockQty() == 0) {
                            if (Double.parseDouble(updatedQuantityProductList.get(i).getQuantity()) == 0) {

                            } else {
                                if (Double.parseDouble(updatedQuantityProductList.get(i).getQuantity()) > 0) {
                                    (binding.productList.getLayoutManager().findViewByPosition(i).findViewById(R.id.stock)).setVisibility(View.VISIBLE);
                                    ((TextView) binding.productList.getLayoutManager()
                                            .findViewByPosition(i).findViewById(R.id.stock)).setText(response.getLists().get(i).getStockQty() + "");
                                    allOk = false;
                                    return;
                                }
                            }

                        }
                        if (Double.parseDouble(updatedQuantityProductList.get(i).getQuantity()) > response.getLists().get(i).getStockQty()) {
                            (binding.productList.getLayoutManager().findViewByPosition(i).findViewById(R.id.stock)).setVisibility(View.VISIBLE);
                            ((TextView) binding.productList.getLayoutManager().findViewByPosition(i).findViewById(R.id.stock)).setText(response.getLists().get(i).getStockQty() + "");
                            allOk = false;
                            return;
                        }
                        allOk = true;
                    }
                    if (allOk) {//now all ok
                        try {
                            Bundle bundle = new Bundle();
                            if (selectedEnterPrice == null) {
                                bundle.putString("selectedEnterpriseId", "");
                            } else {
                                bundle.putString("selectedEnterpriseId", selectedEnterPrice);
                            }
                            if (selectedStore == null) {
                                bundle.putString("selectedStoreId", "");
                            } else {
                                bundle.putString("selectedStoreId", selectedStore);
                            }
                            bundle.putString("id", previousOrderSid);
                            //bundle.getString("orderSerial", orderSerial);
                            hideKeyboard(getActivity());
                            Navigation.findNavController(getView()).navigate(R.id.action_editReconcilationData_to_confirmEditReconcilation, bundle);
                        } catch (Exception e) {
                            Log.d("ERROR", "error");
                        }
                    }
                });
    }

}