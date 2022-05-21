package com.rupayan_housing.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rupayan_housing.R;
import com.rupayan_housing.adapter.SalesRequisitionProductsAdapter;
import com.rupayan_housing.serverResponseModel.SalesRequisitionItemsResponse;
import com.rupayan_housing.utils.InternetConnection;
import com.rupayan_housing.viewModel.SalesRequisitionViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

import static com.rupayan_housing.view.fragment.DueCollectionFragment.HIDE_KEYBOARD;

public class NewSaleFragment extends Fragment {

    private SalesRequisitionViewModel salesRequisitionViewModel;
    List<SalesRequisitionItemsResponse> productItemsList;
    List<SalesRequisitionItemsResponse> productsList;
    private ArrayAdapter<String> productsArrayAdapter;
    private boolean isDataFetching = false; // variable to detect whether server data fetch is loading or not
    View view;
    @BindView(R.id.toolbarTitle)
    TextView toolbar;
    @BindView(R.id.itemSearchEt)
    AutoCompleteTextView itemSearchEt;
    SalesRequisitionProductsAdapter adapter;


    public static RecyclerView salesRequisitionProductListRv;
    @SuppressLint("StaticFieldLeak")
    public static Button totalSubmitBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new_sale, container, false);
        ButterKnife.bind(this, view);
        totalSubmitBtn = view.findViewById(R.id.totalSubmitBtn);
        salesRequisitionProductListRv = view.findViewById(R.id.salesRequisitionProductListRv);
        salesRequisitionViewModel = ViewModelProviders.of(this).get(SalesRequisitionViewModel.class);
        getDataFromPreviousFragment();
        /**
         * load recycler data from server
         */
        loadProductListInRecyclerView();

        itemSearchEt.setOnItemClickListener((parent, view, position, id) -> {
            if (!productItemsList.isEmpty()) {
                itemSearchEt.clearFocus();
                HIDE_KEYBOARD(getActivity());

                SalesRequisitionItemsResponse selectedProducts = productItemsList.get(position);
                /**
                 * for show selected product in recyclerView
                 */
                SalesRequisitionProductsAdapter adapter = new SalesRequisitionProductsAdapter(getActivity(), Arrays.asList(selectedProducts), null);
                salesRequisitionProductListRv.setLayoutManager(new LinearLayoutManager(getActivity()));
                salesRequisitionProductListRv.setAdapter(adapter);
            }
        });
        /**
         * for control search item text change action
         */
        itemSearchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (itemSearchEt.isPerformingCompletion()) {
                    return;
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (!charSequence.toString().trim().isEmpty() && !isDataFetching) {
                    String currentSearchItemName = itemSearchEt.getText().toString();
                    /**
                     * now get Data from server
                     */
                    getSearchingDataFromServer(currentSearchItemName);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return view;
    }

    private void loadProductListInRecyclerView() {
        if (!InternetConnection.isOnline(getActivity())) {
//            Navigation.findNavController(getView()).navigate(R.id.newsal);
            Toasty.info(getActivity(), "Please Check Your Internet Connection", Toasty.LENGTH_LONG).show();
            return;
        }
        salesRequisitionViewModel.getSalesRequisitionItemList(getActivity()).observe(getViewLifecycleOwner(), salesRequisitionItemList -> {
            salesRequisitionProductListRv.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter = new SalesRequisitionProductsAdapter(getActivity(), null, salesRequisitionItemList);
            salesRequisitionProductListRv.setAdapter(adapter);

        });
    }

    /**
     * method from search search item by name
     *
     * @param
     */
    private void getSearchingDataFromServer(String currentText) {
        /**
         * set categoryS id static as like the provided this API so make a list of string
         */
        List<String> categoryS = new ArrayList<>();
        categoryS.addAll(Arrays.asList("739", "740", "741"));
        /**
         * now call the api
         */
        salesRequisitionViewModel.apiCallForSearchSalesRequisitionItemByName(getActivity(), categoryS, currentText);
        /**
         * now get the api data
         */
        salesRequisitionViewModel.getSearchSalesRequisitionItemByName().observe(getViewLifecycleOwner(), response -> {

            productItemsList = new ArrayList<>();
            productItemsList.clear();
            productItemsList.addAll(response.getItems());
            List<String> salesProductsList = new ArrayList<>();
            productsList = response.getItems();

            //  productsList.forEach(products -> salesProductsList.add(products.getProductTitle()));//for api level 24 or above

            for (int i = 0; i < productsList.size(); i++) {
                salesProductsList.add(productsList.get(i).getProductTitle());
            }

            if (salesProductsList.isEmpty()) { // show message in the item if the list is empty
                salesProductsList.add("No data found!");
            }
            productsArrayAdapter = new ArrayAdapter<>(getContext(), R.layout.filter_model, R.id.customerNameModel, salesProductsList);
            itemSearchEt.setAdapter(productsArrayAdapter);
            itemSearchEt.showDropDown();
            isDataFetching = false;
        });

    }

    private void getDataFromPreviousFragment() {
        toolbar.setText("New Sale Requisition");
    }

    @OnClick(R.id.backbtn)
    public void onClickBackBtn() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.totalSubmitBtn)
    public void totalSubmit() {
        String removeText = "Total ";
        String currentText = totalSubmitBtn.getText().toString();
        double currentTotal = 0;
        if (currentText.contains(removeText)) {
            currentTotal = Double.parseDouble(currentText.substring(removeText.length(), currentText.length()));
        }
        Log.d("TOTAL ", String.valueOf(currentTotal));


        if (currentTotal <= 0) {
            Toasty.info(getContext(), "Total Sale is Empty", Toasty.LENGTH_LONG).show();
            return;
        }

        Bundle bundle = new Bundle();

       /* bundle.putStringArrayList("allPrice", new ArrayList<>(getAllPrice()));
        bundle.putStringArrayList("allQuantity", new ArrayList<>(getAllQuantity()));*/
        bundle.putString("total", String.valueOf(currentTotal));
        Navigation.findNavController(getView()).navigate(R.id.action_newSaleFragment_to_newSalesRequisitionFragment2, bundle);
    }

    /**
     * for get all title from the current recyclerview
     */

    private static List<String> getAllTitle() {
        List<String> paymentLimitAmount = new ArrayList<>();
        for (int i = 0; i < salesRequisitionProductListRv.getChildCount(); i++) {
            paymentLimitAmount.add(((TextView) salesRequisitionProductListRv.getLayoutManager().findViewByPosition(i).findViewById(R.id.productNameTv)).getText().toString());
        }
        return paymentLimitAmount;
    }

    /**
     * for get all price from the current recyclerview
     *
     * @return
     */
    public static List<String> getAllPrice() {
        List<String> paymentLimitAmount = new ArrayList<>();
        for (int i = 0; i < salesRequisitionProductListRv.getChildCount(); i++) {


            try {
                paymentLimitAmount.add(((AutoCompleteTextView) salesRequisitionProductListRv.getLayoutManager().findViewByPosition(i).findViewById(R.id.priceEt)).getText().toString());
            } catch (Exception e) {
                Log.d("ERROR", e.getMessage());
            }
            //paymentLimitAmount.add(SalesRequisitionProductsAdapter.MyHolder().priceEt.getText().toString());

        }
        return paymentLimitAmount;
    }

    /**
     * for get all quantity from the recyclerview
     *
     * @return
     */
    public static List<String> getAllQuantity() {
        List<String> paymentLimitAmount = new ArrayList<>();
        try {
            for (int i = 0; i < salesRequisitionProductListRv.getChildCount(); i++) {
                paymentLimitAmount.add(((EditText) salesRequisitionProductListRv.getLayoutManager().findViewByPosition(i).findViewById(R.id.quantityEt)).getText().toString());
            }
        } catch (Exception e) {
            Log.d("ERROR", e.getLocalizedMessage());
        }
        return paymentLimitAmount;
    }

    /**
     * after get all price and quantity count the total price in this method
     * will call method for the current recyclerview adapter
     *
     * @return
     */
    public static double getTotalPrice() {
        double totalPrice = 0;
        List<String> allPrice = new ArrayList<>();
        allPrice.clear();
        List<String> allQuantity = new ArrayList<>();
        allQuantity.clear();
        allPrice = getAllPrice();
        allQuantity = getAllQuantity();
       try {
           for (int i = 0; i < allPrice.size(); i++) {
               double currentPrice = Double.parseDouble(allPrice.get(i));
               double currentQuantity = Double.parseDouble(allQuantity.get(i));
               totalPrice += currentPrice * currentQuantity;
           }
       }catch (Exception e){
           Log.d("ERROR",e.getLocalizedMessage());
       }
        Log.d("TOTAL", String.valueOf(totalPrice));
        return totalPrice;
    }
}
