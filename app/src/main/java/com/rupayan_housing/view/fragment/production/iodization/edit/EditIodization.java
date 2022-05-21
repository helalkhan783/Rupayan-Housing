package com.rupayan_housing.view.fragment.production.iodization.edit;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.FragmentEditIodizationBinding;
import com.rupayan_housing.localDatabase.MyWashingCrushingHelper;
import com.rupayan_housing.serverResponseModel.EditWashingCrushingItem;
import com.rupayan_housing.serverResponseModel.EditWashingCrushingResponse;
import com.rupayan_housing.serverResponseModel.EnterpriseList;
import com.rupayan_housing.serverResponseModel.WashingCrushingModel;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.view.fragment.production.washingAndCrushing.edit.EditWashingCrushing;
import com.rupayan_housing.viewModel.EditWashingCrushingViewModel;
import com.rupayan_housing.viewModel.IodizationViewModel;
import com.rupayan_housing.viewModel.SaleViewModel;
import com.rupayan_housing.viewModel.UpdateMillerViewModel;
import com.rupayan_housing.viewModel.UpdateSaleViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class EditIodization extends BaseFragment implements IodizationEditItemClickHandle {
    public static FragmentEditIodizationBinding binding;
    private UpdateMillerViewModel updateMillerViewModel;
    private MyWashingCrushingHelper myDatabaseHelper;
    private SaleViewModel saleViewModel;
    private UpdateSaleViewModel updateSaleViewModel;
    private EditWashingCrushingViewModel editWashingCrushingViewModel;
    private IodizationViewModel iodizationViewModel;

    List<WashingCrushingModel> itemsList;
    EditIodizationEditAdapter adapter;


    private static boolean finalTotalCount = false;
    private boolean allOk = true;

    public static EditWashingCrushingResponse getPreviousSaleInfoResponse;
    public static List<EditWashingCrushingItem> currentSaleItemList;


    /**
     * updated quantity product list (LAST QUANTITY UPDATED ALL PRODUCT LIST)
     */
    public static List<WashingCrushingModel> updatedQuantityProductList = new ArrayList<>();
    public static List<String> updatedQuantityList = new ArrayList<>();


    private String siId;
    private String selectedEnterprise;
    private String orderId;
    /**
     * For select store
     */
    private List<EnterpriseList> storeResponseList;
    private List<String> storeNameList;
    private String selectedStore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_iodization, container, false);
        myDatabaseHelper = new MyWashingCrushingHelper(getContext());
        updateMillerViewModel = new ViewModelProvider(this).get(UpdateMillerViewModel.class);
        saleViewModel = new ViewModelProvider(this).get(SaleViewModel.class);
        updateSaleViewModel = new ViewModelProvider(this).get(UpdateSaleViewModel.class);
        editWashingCrushingViewModel = new ViewModelProvider(this).get(EditWashingCrushingViewModel.class);
        iodizationViewModel = new ViewModelProvider(this).get(IodizationViewModel.class);
        binding.toolbar.toolbarTitle.setText("Edit Iodization");
        getDataFromPreviousFragment();

        binding.toolbar.setClickHandle(() -> {
            hideKeyboard(getActivity());
            getActivity().onBackPressed();
        });
        /**
         * Now get Sale Data from previous Fragment
         */
        getPreviousIodizationDataFromServer();
        /**
         * now validation and submit current page Data
         */
        binding.setClickHandle(() -> {
            if (selectedStore == null) {
                binding.store.setEnableErrorLabel(true);
                binding.store.setErrorText("Empty");
                return;
            }

            if (itemsList == null || itemsList.isEmpty()) {
                return;
            }
            finalTotalCount = true;
            getFinalAllQuantity();//for count updated quantity object

            updatedQuantityProductList.clear();
            updatedQuantityProductList.addAll(itemsList);


            for (int i = 0; i < updatedQuantityProductList.size(); i++) {
                if (Double.parseDouble(updatedQuantityProductList.get(i).getQuantity()) == 0) {

                    return;
                }
            }

            if (updatedQuantityProductList.isEmpty()) {
                infoMessage(getActivity().getApplication(), "Empty Quantity");
                return;
            }
            if (!isInternetOn(getActivity())){
                infoMessage(requireActivity().getApplication(), "Please Check Your Internet Connection");
                return;
            }
            validationAndSave();
        });

        /**
         * handle selected store
         */
        binding.store.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStore = storeResponseList.get(position).getStoreID();
                /**
                 * now get current recycler item list data from this selected store
                 */
                if (itemsList != null) {
                    setStockQuantityByCurrentItemsSoldFrom(selectedStore);
                }


                binding.store.setEnableErrorLabel(false);
                binding.store.setErrorText("");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return binding.getRoot();
    }

    private void setStockQuantityByCurrentItemsSoldFrom(String selectedStoreId) {

        /**
         * after set store load all product stock data------
         */
        List<String> productIdList = new ArrayList<>();
        List<String> slectedStore = new ArrayList<>();
        List<String> soldFromList = new ArrayList<>();

        productIdList.clear();
        soldFromList.clear();

        try {
            for (int i = 0; i < itemsList.size(); i++) {
                try {
                    productIdList.add(itemsList.get(i).getProductID());
                    slectedStore.add(EditIodization.currentSaleItemList.get(i).getSoldFrom());
                    soldFromList.add(getPreviousSaleInfoResponse.getOrderDetails().getItems().get(i).getSoldFrom());
                } catch (Exception e) {
                    Log.d("ERROR", "" + e.getMessage());
                }

            }
        } catch (Exception e) {
            Log.d("ERROR", "" + e.getMessage());
        }
        saleViewModel.getItemStockList(getActivity(), productIdList, soldFromList)
                .observe(getViewLifecycleOwner(), stockResponse -> {
                    if (stockResponse == null) {
                        errorMessage(getActivity().getApplication(), "Something Wrong");
                        return;
                    }
                    if (stockResponse.getStatus() == 400) {
                        errorMessage(getActivity().getApplication(), "Something Wrong");
                        return;
                    }
                    for (int i = 0; i < stockResponse.getLists().size(); i++) {

                        try {
                            binding.itemListRv.getLayoutManager()
                                    .findViewByPosition(i).findViewById(R.id.stock).setVisibility(View.VISIBLE);
                            ((TextView) binding.itemListRv.getLayoutManager().findViewByPosition(i)
                                    .findViewById(R.id.stock)).setText(stockResponse.getLists().get(i).getStockQty() + " Available");
                        } catch (Exception e) {
                            Log.d("ERROR", "" + e.getMessage());
                        }
                    }

                });
    }

    private void getPreviousIodizationDataFromServer() {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }


        /**
         * set available stock
         * Available Potassium Iodide()
         */
        iodizationViewModel.getEditIodizationStock(getActivity(), orderId)
                .observe(getViewLifecycleOwner(), response -> {
                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "Something Wrong");
                        return;
                    }

                    if (response.getStatus() == 400) {
                        infoMessage(getActivity().getApplication(), "" + response.getStatus());
                        return;
                    }
                    binding.stock.setText("Available Potassium Iodide(KIO3): " + response.getQty());
                });


        iodizationViewModel.getIodizationPageData(getActivity(), siId)
                .observe(getViewLifecycleOwner(), response -> {
                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "Something Wrong");
                        return;
                    }

                    if (response.getStatus() == 400) {
                        infoMessage(getActivity().getApplication(), "" + response.getStatus());
                        return;
                    }
                    getPreviousSaleInfoResponse = response;

                    currentSaleItemList = response.getOrderDetails().getItems();//store current sales items
                    selectedEnterprise = response.getOrder().getStoreID();
                    orderId = response.getOrder().getOrderID();

                    /**
                     * save data
                     */
                    for (int i = 0; i < response.getOrderDetails().getItems().size(); i++) {
                        long rowId = myDatabaseHelper.insertData(
                                response.getOrderDetails().getItems().get(i).getProductID(),
                                response.getOrderDetails().getItems().get(i).getItem(),
                                response.getOrderDetails().getItems().get(i).getQuantity(),
                                response.getOrderDetails().getItems().get(i).getUnitID(),
                                response.getOrderDetails().getItems().get(i).getSoldFrom(),
                                response.getOrderDetails().getItems().get(i).getUnit()
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
                        WashingCrushingModel item = new WashingCrushingModel();
                        item.setProductID(cursor.getString(1));
                        item.setProductTitle(cursor.getString(2));
                        item.setQuantity(cursor.getString(3));
                        item.setUnit(cursor.getString(4));
                        item.setSoldFrom(cursor.getString(5));
                        item.setUnit_name(cursor.getString(6));
                        itemsList.add(item);
                    }
                    adapter = new EditIodizationEditAdapter(getActivity(), itemsList, EditIodization.this);
                    binding.itemListRv.setLayoutManager(new LinearLayoutManager(getActivity()));
                    binding.itemListRv.setAdapter(adapter);

                    /**
                     * now control stock
                     */

                    /**
                     * after set store load all product stock data------
                     */
                    List<String> productIdList = new ArrayList<>();
                    List<String> slectedStore = new ArrayList<>();

                    productIdList.clear();

                    for (int i = 0; i < itemsList.size(); i++) {
                        productIdList.add(itemsList.get(i).getProductID());
                        slectedStore.add(EditIodization.currentSaleItemList.get(i).getSoldFrom());
                    }
                });

        /**
         * Now set store list by selected enterprise id
         */
        saleViewModel.getStoreListByOptionalEnterpriseId(getActivity(), selectedEnterprise)
                .observe(getViewLifecycleOwner(), storeResponse -> {
                    if (storeResponse == null) {
                        errorMessage(getActivity().getApplication(), "Something Wrong");
                        return;
                    }
                    if (storeResponse.getStatus() == 400) {
                        infoMessage(getActivity().getApplication(), "" + "Api ERROR");
                        return;
                    }


                    storeResponseList = new ArrayList<>();
                    storeResponseList.clear();
                    storeNameList = new ArrayList<>();
                    storeNameList.clear();
                    storeResponseList.addAll(storeResponse.getEnterprise());
                    for (int i = 0; i < storeResponse.getEnterprise().size(); i++) {
                        storeNameList.add("" + storeResponse.getEnterprise().get(i).getStoreName());
                    }
                    binding.store.setItem(storeNameList);


                });
    }

    private void getDataFromPreviousFragment() {
        assert getArguments() != null;
        siId = getArguments().getString("sid");
        orderId = getArguments().getString("orderId");
    }

    @Override
    public void removeBtn(int position) {

    }

    @Override
    public void insertQuantity(int position, String quantity, WashingCrushingModel currentItem) {

        allOk = true;//for  handle submit time validation
        Boolean isUpdated = myDatabaseHelper
                .updateData(currentItem.getProductID(), currentItem.getProductTitle(), quantity, currentItem.getUnit(), currentItem.getSoldFrom(), currentItem.getUnit_name());

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

        itemsList = new ArrayList<>();
        itemsList.clear();
        while (cursor.moveToNext()) {
            WashingCrushingModel item = new WashingCrushingModel();
            item.setProductID(cursor.getString(1));
            item.setProductTitle(cursor.getString(2));
            item.setQuantity(cursor.getString(3));
            item.setUnit(cursor.getString(4));
            item.setSoldFrom(cursor.getString(5));
            item.setUnit_name(cursor.getString(6));

            itemsList.add(item);
        }
    }

    @Override
    public void minusQuantity(int position, String quantity, WashingCrushingModel currentItem) {
        allOk = true;//for  handle submit time validation
        Boolean isUpdated = myDatabaseHelper
                .updateData(currentItem.getProductID(), currentItem.getProductTitle(), quantity, currentItem.getUnit(), currentItem.getSoldFrom(), currentItem.getUnit_name());

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

        itemsList = new ArrayList<>();
        itemsList.clear();
        while (cursor.moveToNext()) {
            WashingCrushingModel item = new WashingCrushingModel();

            item.setProductID(cursor.getString(1));
            item.setProductTitle(cursor.getString(2));
            item.setQuantity(cursor.getString(3));
            item.setUnit(cursor.getString(4));
            item.setSoldFrom(cursor.getString(5));
            item.setUnit_name(cursor.getString(6));

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


    private void validationAndSave() {

        /**
         * for set updated productIdList,unitList
         */
        List<String> proDuctIdList = new ArrayList<>();
        List<String> unitList = new ArrayList<>();
        List<String> productTitleList = new ArrayList<>();
        List<String> previousQuantityList = new ArrayList<>();
        List<String> soldFromList = new ArrayList<>();

        proDuctIdList.clear();
        unitList.clear();
        productTitleList.clear();
        soldFromList.clear();
        for (int i = 0; i < EditIodization.updatedQuantityProductList.size(); i++) {
            proDuctIdList.add(EditIodization.updatedQuantityProductList.get(i).getProductID());
            unitList.add(EditIodization.updatedQuantityProductList.get(i).getUnit());
            productTitleList.add(EditIodization.updatedQuantityProductList.get(i).getProductTitle());
            previousQuantityList.add(EditIodization.currentSaleItemList.get(i).getQuantity());
            soldFromList.add(EditIodization.currentSaleItemList.get(i).getSoldFrom());
        }


        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check your Internet Connection");
            return;
        }


        iodizationViewModel.editIodizationStockMessageCheck(
                getActivity(), orderId, siId, proDuctIdList, Collections.singletonList(selectedStore), EditIodization.updatedQuantityList,
                productTitleList, previousQuantityList, soldFromList
        ).observe(getViewLifecycleOwner(), response1 -> {
            if (response1 == null) {
                errorMessage(getActivity().getApplication(), "Something Wrong");
                return;
            }
            if (response1.getStatus() == 400) {
                infoMessage(getActivity().getApplication(), "" + response1.getMessage());
                return;
            }
            try {
                Bundle bundle = new Bundle();
                bundle.putString("selectedEnterprise", selectedEnterprise);
                bundle.putString("selectedStoreId", selectedStore);
                bundle.putString("siId", siId);
                bundle.putString("orderId", orderId);
                bundle.putString("destinationStore", getPreviousSaleInfoResponse.getDestination_store());
                /**
                 * all ok now go to the confirm iodization fragment
                 */
                Navigation.findNavController(getView()).navigate(R.id.action_editIodization_to_confirmEditIodization, bundle);
                selectedStore = null;
                selectedEnterprise = null;
            } catch (Exception e) {
                Log.d("ERROR", "error");
            }
        });


    }
}