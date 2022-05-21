package com.rupayan_housing.view.fragment.purchase.purchaseReturn;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.rupayan_housing.R;
import com.rupayan_housing.adapter.PurchaseReturnAdapter;
import com.rupayan_housing.databinding.FragmentPurchaseReturnBinding;
import com.rupayan_housing.localDatabase.MyDatabaseHelper;
import com.rupayan_housing.serverResponseModel.GetPurchaseReturnResponse;
import com.rupayan_housing.serverResponseModel.PurchaseItems;
import com.rupayan_housing.serverResponseModel.SalesRequisitionItems;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.viewModel.PurchaseReturnViewModel;

import java.util.ArrayList;
import java.util.List;


public class PurchaseReturnFragment extends BaseFragment implements PurchaseReturnItemClick {
    public static FragmentPurchaseReturnBinding binding;
    private PurchaseReturnViewModel purchaseReturnViewModel;
    private MyDatabaseHelper myDatabaseHelper;
    private String NO_DATA_FOUND = "No Data Found";
    private boolean isDataFetching = false; // variable to detect whether server data fetch is loading or not
    /**
     * For items
     */
    private GetPurchaseReturnResponse currentResponse;
    private List<String> itemNameList;

    private ArrayAdapter<String> itemArrayAdapter;
    private ArrayList<SalesRequisitionItems> itemsList;
    private PurchaseReturnAdapter adapter;


    private static boolean finalTotalCount = false;
    private boolean allOk = true;
    /**
     * updated quantity product list (LAST QUANTITY UPDATED ALL PRODUCT LIST)
     */
    public static List<SalesRequisitionItems> updatedQuantityProductList = new ArrayList<>();
    public static List<String> updatedQuantityList = new ArrayList<>();
    List<String> currentQuantity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_purchase_return, container, false);
        purchaseReturnViewModel = new ViewModelProvider(this).get(PurchaseReturnViewModel.class);
        myDatabaseHelper = new MyDatabaseHelper(getContext());

        binding.toolbar.toolbarTitle.setText("Purchase Return");
        binding.toolbar.setClickHandle(() -> {
            hideKeyboard(getActivity());
            getActivity().onBackPressed();
        });
        /**
         * After Search
         */
        binding.search.setOnClickListener(v -> {

            if (binding.itemSearchEt.getText().toString().isEmpty()) {
                return;
            }

            getProductBySearchKey(binding.itemSearchEt.getText().toString());
        });

        /**
         * For Submit orders
         */
        binding.submit.setOnClickListener(v -> {
            if (!(isInternetOn(getActivity()))) {
                infoMessage(getActivity().getApplication(), "Please Check your Internet Connection");
                return;
            }
            if (currentResponse == null) {
                infoMessage(getActivity().getApplication(), "Select Order First");
                return;
            }
            hideKeyboard(getActivity());
            confirmDialog();
        });
        /**
         * For cancel whole order
         */
        binding.cancelOrders.setOnClickListener(v -> {
            if (!(isInternetOn(getActivity()))) {
                infoMessage(getActivity().getApplication(), "Please Check your Internet Connection");
                return;
            }
            if (currentResponse == null) {
                infoMessage(getActivity().getApplication(), "Select Order First");
                return;
            }

            String orderId = currentResponse.getOrder().getOrderID();
            if (orderId == null) {
                infoMessage(getActivity().getApplication(), "select Orders");
                return;
            }
            hideKeyboard(getActivity());
            confirmDialogForWholeOrderCancel();
        });

        return binding.getRoot();
    }

    private void cancelWholePurchaseOrders() {
        hideKeyboard(getActivity());
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        purchaseReturnViewModel.purchaseWholeOrderCancel(getActivity(), currentResponse.getOrder().getOrderID())
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
                        successMessage(getActivity().getApplication(), "" + response.getMessage());
                        getActivity().onBackPressed();
                    } catch (Exception e) {
                        Log.d("ERROR", "error");
                    }
                });

    }

    private void getProductBySearchKey(String currentText) {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check your Internet Connection");
            return;
        }
        if (currentText.isEmpty()) {
            return;
        }
        hideKeyboard(getActivity());

        purchaseReturnViewModel.getPurchaseReturnPageData(getActivity(), currentText)
                .observe(getViewLifecycleOwner(), response -> {
                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "Something Wrong");
                        return;
                    }
                    if (response.getStatus() == 400) {
                        infoMessage(getActivity().getApplication(), "" + response.getMessage());
                        return;
                    }
                    currentResponse = response;
                    adapter = new PurchaseReturnAdapter(getActivity(), response.getOrderDetails().getItems(), PurchaseReturnFragment.this);
                    binding.productList.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.productList.setAdapter(adapter);
                    /**
                     * now set Current quantity
                     */
                    currentQuantity = new ArrayList<>();
                    currentQuantity.clear();
                    for (int i = 0; i < response.getOrderDetails().getItems().size(); i++) {
                        currentQuantity.add("0");
                    }

                });


    }

    @Override
    public void insertQuantity(int position, String quantity, PurchaseItems currentItem) {
        try {
            String currentMainQuantity = quantity;
            if (quantity.isEmpty()) {
                currentMainQuantity = "0";
            }
            currentQuantity.remove(position);
            currentQuantity.add(position, currentMainQuantity);

        } catch (Exception e) {
            Log.d("ERROR", "" + e.getMessage());
        }
    }

    private void validationAndSubmit() {


        List<String> orderDetailsIdList = new ArrayList<>();
        List<Integer> productIdList = new ArrayList<>();
        List<String> buyingPriceList = new ArrayList<>();
        List<String> productTitleList = new ArrayList<>();
        List<String> productUnitList = new ArrayList<>();
        List<String> soldFromList = new ArrayList<>();
        orderDetailsIdList.clear();
        productIdList.clear();
        buyingPriceList.clear();
        productTitleList.clear();
        productUnitList.clear();
        soldFromList.clear();

        Double currentTotal = 0.0;

        try {
            for (int i = 0; i < currentResponse.getOrderDetails().getItems().size(); i++) {
                try {
                    orderDetailsIdList.add(currentResponse.getOrderDetails().getItems().get(i).getOrder_detailsID());
                    productIdList.add(Integer.parseInt(currentResponse.getOrderDetails().getItems().get(i).getProductID()));
                    buyingPriceList.add(currentResponse.getOrderDetails().getItems().get(i).getBuyingPrice());
                    productTitleList.add(currentResponse.getOrderDetails().getItems().get(i).getItem());
                    productUnitList.add(currentResponse.getOrderDetails().getItems().get(i).getUnitID());
                    soldFromList.add(currentResponse.getOrderDetails().getItems().get(i).getSoldFrom());
                    Double total = Double.parseDouble(currentQuantity.get(i)) * Double.parseDouble(currentResponse.getOrderDetails().getItems().get(i).getBuyingPrice());
                    currentTotal += total;
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            Log.d("ERROR", "ERROR");
        }
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        purchaseReturnViewModel.submitPurchaseReturn(
                getActivity(), currentResponse.getOrder().getOrderID(),
                String.valueOf(currentTotal), "0.0", orderDetailsIdList,
                productIdList, currentQuantity, buyingPriceList, productTitleList, productUnitList,
                soldFromList
        ).observe(getViewLifecycleOwner(), response -> {
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
                successMessage(getActivity().getApplication(), "" + response.getMessage());
                getActivity().onBackPressed();
            } catch (Exception e) {
                Log.d("ERROR", "ERROR");
            }
        });
    }


    public void confirmDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Alert");
        alertDialog.setIcon(R.drawable.warning_btn);
        alertDialog.setMessage("Do you want to return ?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                (dialog, which) -> {
                    dialog.dismiss();
                    validationAndSubmit();
                });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }


    private void confirmDialogForWholeOrderCancel() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Alert");
        alertDialog.setIcon(R.drawable.warning_btn);
        alertDialog.setMessage("Do you want to return ?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                (dialog, which) -> {
                    dialog.dismiss();
                    cancelWholePurchaseOrders();
                });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }

}