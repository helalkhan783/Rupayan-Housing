package com.rupayan_housing.view.fragment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.rupayan_housing.R;
import com.rupayan_housing.adapter.CustomerOrderAdapter;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.CustomerResponse;
import com.rupayan_housing.serverResponseModel.Order;
import com.rupayan_housing.viewModel.SupplierDueViewModel;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

import static com.rupayan_housing.view.fragment.DueCollectionFragment.HIDE_KEYBOARD;

public class PayDueToSupplierSearchFragment extends Fragment {
    List<CustomerResponse> customerResponseList;
    List<CustomerResponse> customerList;
    private ArrayAdapter<String> customerArrayAdapter;
    private SupplierDueViewModel supplierDueViewModel;
    View view;
    private boolean isDataFetching = false; // variable to detect whether server data fetch is loading or not
    @BindView(R.id.toolbarTitle)
    TextView toolBar;
    @BindView(R.id.customerNameEtSupplier)
    AutoCompleteTextView customerNameEtSupplier;
    @BindView(R.id.tv_total_amountSupplier)
    TextView totalDue;
    @BindView(R.id.card_submit_btn)
    CardView submitBtn;
    @BindView(R.id.expandable_layoutSupplier)
    ExpandableLayout expandableLayoutSupplier;
    @BindView(R.id.image_viewSupplier)
    ImageView image_viewSupplier;
    @BindView(R.id.customerNameTvSupplier)
    TextView customerNameTv;
    @BindView(R.id.ordersRvSupplier)
    RecyclerView ordersRv;
    @BindView(R.id.tv_noticeSupplier)
    TextView noOrderFound;


    double paymentLimit;


    String selectedCustomerId;
    private String customerPhone;
    private String customerAddress;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pay_due_to_supplier_search_fragment, container, false);
        ButterKnife.bind(this, view);
        supplierDueViewModel = ViewModelProviders.of(this).get(SupplierDueViewModel.class);
        getDataFromPreviousFragment();
        /**
         * for search customer (Supplier)
         */
        customerNameEtSupplier.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (customerNameEtSupplier.isPerformingCompletion()) {
                    return;
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // if item is not selected
                if (!charSequence.toString().trim().isEmpty() && !isDataFetching) {
                    collapseLayout(); // collapse bottom layout
                    String currentText = customerNameEtSupplier.getText().toString();
                    String currentUserToken = PreferenceManager.getInstance(getContext()).getUserCredentials().getToken();
                    String currentVendorId = PreferenceManager.getInstance(getContext()).getUserCredentials().getVendorID();
                    /**
                     * method for search a customer
                     */
                    getSuppliersFromServer(currentUserToken, currentVendorId, currentText);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /**
         * for get supplier orders by selected customer id
         */
        customerNameEtSupplier.setOnItemClickListener((parent, view, position, id) -> {
            if (!customerResponseList.isEmpty()) {//if have any customer in the current list
                customerNameEtSupplier.clearFocus();
                HIDE_KEYBOARD(getActivity());

                String selectedSupplierId = customerResponseList.get(position).getCustomerID();
                String selectedUserVendorId = PreferenceManager.getInstance(getContext()).getUserCredentials().getVendorID();
                /**
                 * for show supplier order in recyclerView
                 */
                getDueOrdersAndShowInRecyclerView(selectedSupplierId, selectedUserVendorId); // get due orders from server

            } else {
                customerNameEtSupplier.getText().clear();
            }
        });


        return view;
    }

    /*
         for get orders and show in recyclerview
     */
    private void getDueOrdersAndShowInRecyclerView(String selectedSupplierId, String selectedUserVendorId) {

        List<Order> mainDueOrderList = new ArrayList<>();

        supplierDueViewModel.apiCallForGetSupplierOrder(getActivity(), selectedSupplierId);

        supplierDueViewModel.getSupplierOrders().observe(getViewLifecycleOwner(), supplierOrdersResponse -> {
            selectedCustomerId = selectedSupplierId;

            paymentLimit = supplierOrdersResponse.getPaymentLimit().getPayLimitAmount();//store payment limit.

            ///Log.d("DDD",String.valueOf(supplierOrdersResponse.getPaymentLimit()));

            customerNameTv.setText(supplierOrdersResponse.getCustomer().getCustomerFname());//set customer name
            customerPhone = supplierOrdersResponse.getCustomer().getPhone();
            customerAddress = supplierOrdersResponse.getCustomer().getAddress();
            ordersRv.setLayoutManager(new LinearLayoutManager(getContext()));
            /**
             * for show selected customer list in Recycler view
             */
            List<Order> orderList = supplierOrdersResponse.getOrders();
            /**
             * if with order don't have any due ignore this order for this purpose so write the condition
             */
            mainDueOrderList.clear();
            for (int i = 0; i < orderList.size(); i++) {
                mainDueOrderList.add(orderList.get(i));
            }

           /* orderList.forEach(order -> {
                if (Integer.parseInt(order.getDue()) > 0) {
                    mainDueOrderList.add(order);
                }
            });*/


            //Double totalDueOfTheUser = mainDueOrderList.stream().mapToDouble(value -> Double.parseDouble(value.getDue())).sum();

            double total=0;
            for (int i = 0; i < mainDueOrderList.size(); i++) {
                total += Double.parseDouble(mainDueOrderList.get(i).getDue());
            }

            totalDue.setText(String.valueOf(total));//set total due

            if (!mainDueOrderList.isEmpty()) {
                ordersRv.setVisibility(View.VISIBLE);
                noOrderFound.setVisibility(View.GONE);
                CustomerOrderAdapter adapter = new CustomerOrderAdapter(getActivity(), mainDueOrderList);
                ordersRv.setAdapter(adapter);
                return;
            } else {
                ordersRv.setVisibility(View.GONE);
                noOrderFound.setText("No order Found");
                noOrderFound.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * for search suppliers
     *
     * @param currentUserToken
     * @param currentVendorId
     * @param currentText
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getSuppliersFromServer(String currentUserToken, String currentVendorId, String currentText) {

        supplierDueViewModel.apiCallForSearchSuppliers(currentUserToken, currentVendorId, currentText);


        supplierDueViewModel.getAllSuppliers().observe(getViewLifecycleOwner(), customerSearchResponse -> {
            customerResponseList = new ArrayList<>();
            customerResponseList.clear();
            customerResponseList.addAll(customerSearchResponse.getLists());
            List<String> customerNameList = new ArrayList<>();
            customerList = customerSearchResponse.getLists();

//            customerList.forEach(customerResponse -> customerNameList.add(customerResponse.getCompanyName() + " @ " + customerResponse.getCustomerFname()));

            for (int i = 0; i < customerList.size(); i++) {
                customerNameList.add(customerList.get(i).getCompanyName() + " @ " + customerList.get(i).getCustomerFname());
            }


            if (customerNameList.isEmpty()) { // show message in the item if the list is empty
                customerNameList.add("No data found!");
            }
            customerArrayAdapter = new ArrayAdapter<>(getContext(), R.layout.filter_model, R.id.customerNameModel, customerNameList);
            customerNameEtSupplier.setAdapter(customerArrayAdapter);
            customerNameEtSupplier.showDropDown();
            isDataFetching = false;

        });
    }

    private void getDataFromPreviousFragment() {
        toolBar.setText("Receive Supplier Due");
    }


    @OnClick(R.id.card_submit_btn)
    public void submit() {

        double currentTotalDue = Double.parseDouble(totalDue.getText().toString());

        if (selectedCustomerId != null && currentTotalDue > 0) {
            Bundle bundle = new Bundle();
            bundle.putString("customerId", selectedCustomerId);
            bundle.putString("totalDue", totalDue.getText().toString());
            bundle.putString("customerName", customerNameTv.getText().toString());
            bundle.putString("customerPhone", customerPhone);
            bundle.putString("customerAddress", customerAddress);
            bundle.putString("paymentLimit", String.valueOf(paymentLimit));
            Navigation.findNavController(getView()).navigate(R.id.action_payDueToSupplier_to_supplierDuePaymentReceiveFragment, bundle);
        } else {
            if (currentTotalDue == 0.0) {//just for show a specific message to user
                Toasty.info(getContext(), "You don't have any due", Toasty.LENGTH_LONG).show();
                return;
            }
            Toasty.info(getContext(), "Please select a customer by search", Toasty.LENGTH_LONG).show();
        }

    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.backbtn)
    public void backBtnClick() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.image_viewSupplier)
    public void setArrowBtnClick() {
        expandAndCollapseLayout();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void expandAndCollapseLayout() {
        if (expandableLayoutSupplier.isExpanded()) {
            expandableLayoutSupplier.collapse();
            image_viewSupplier.setImageDrawable(getContext().getDrawable(R.drawable.ic_arrow_drop_up_black_24dp));
        } else {
            expandableLayoutSupplier.expand();
            image_viewSupplier.setImageDrawable(getContext().getDrawable(R.drawable.ic_arrow_drop_down_black_24dp));
        }
    }

    // expand the layout
    private void expandLayout() {
        if (!expandableLayoutSupplier.isExpanded()) {
            expandableLayoutSupplier.expand();
            image_viewSupplier.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_drop_down_black_24dp));
        }
    }

    // collapse the layout
    private void collapseLayout() {
        if (expandableLayoutSupplier.isExpanded()) {
            expandableLayoutSupplier.collapse();
            image_viewSupplier.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_drop_up_black_24dp));
        }
    }
}