package com.rupayan_housing.view.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.adapter.CustomerOrderAdapter;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.CustomerResponse;
import com.rupayan_housing.serverResponseModel.Order;
import com.rupayan_housing.viewModel.DueCollectionViewModel;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;


public class DueCollectionFragment extends Fragment {
    List<CustomerResponse> customerList;
    String customerId;
    private DueCollectionViewModel dueCollectionViewModel;
    View view;
    private boolean isDataFetching = false; // variable to detect whether server data fetch is loading or not
    private ArrayAdapter<String> customerArrayAdapter;
    List<CustomerResponse> customerResponseList;
    @BindView(R.id.backbtn)
    ImageButton backBtn;
    @BindView(R.id.toolbarTitle)
    TextView toolbarTitle;
    @BindView(R.id.image_view)
    ImageView imageView; // collapsing image view
    @BindView(R.id.expandable_layout)
    ExpandableLayout expandableLayout;
    @BindView(R.id.ordersRv)
    RecyclerView ordersRv;
    @BindView(R.id.customerNameEt)
    AutoCompleteTextView customerNameEt;
    @BindView(R.id.receivedNow)
    TextView receivedNow;
    @BindView(R.id.tv_total_amount)
    TextView totalDue;
    @BindView(R.id.customerNameTv)
    TextView customerNameTv;
    @BindView(R.id.tv_notice)
    TextView noOrderFound;


    List<Order> orders = new ArrayList<>();

    //set data for move next fragment
    String customerPhone;
    String customerAddress;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_due_collection, container, false);
        ButterKnife.bind(this, view);
        dueCollectionViewModel = ViewModelProviders.of(this).get(DueCollectionViewModel.class);
        getPreviousFragmentData();
        HIDE_KEYBOARD(getActivity());
        customerNameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (customerNameEt.isPerformingCompletion()) {
                    return;
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                // if item is not selected
                if (!charSequence.toString().trim().isEmpty() && !isDataFetching) {
                    collapseLayout(); // collapse bottom layout
                    String currentText = customerNameEt.getText().toString();
                    String currentUserToken = PreferenceManager.getInstance(getContext()).getUserCredentials().getToken();
                    String currentVendorId = PreferenceManager.getInstance(getContext()).getUserCredentials().getVendorID();
                    /**
                     * for search a customer
                     */
                    getCustomersFromServer(currentUserToken, currentVendorId, currentText);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        customerNameEt.setOnItemClickListener((parent, view, position, id) -> {
            if (!customerResponseList.isEmpty()) {//if have any customer in the current list
                customerNameEt.clearFocus();
                HIDE_KEYBOARD(getActivity());

                String selectedCustomerId = customerResponseList.get(position).getCustomerID();
                String selectedUserVendorId = PreferenceManager.getInstance(getContext()).getUserCredentials().getVendorID();
                /**
                 * get due orders by selected customer id and vendor id
                 */
                getDueOrdersAndShowInRecyclerView(selectedCustomerId, selectedUserVendorId); // get due orders from server

            } else {
                customerNameEt.getText().clear();
            }
        });
        return view;
    }

    /**
     * for get customer orders by customers id
     */
    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getDueOrdersAndShowInRecyclerView(String selectedCustomerId, String vendorId) {
        List<Order> mainDueOrderList = new ArrayList<>();


        dueCollectionViewModel.apiCallForgetDueOrdersBySelectedCustomerIdAndVendorId(getActivity(), selectedCustomerId, vendorId);

        dueCollectionViewModel.getGetDueOrders().observe(getViewLifecycleOwner(), dueOrdersResponse -> {
            customerId = selectedCustomerId;

            orders.clear();
            orders = dueOrdersResponse.getOrders();///for store current due response


            customerNameTv.setText(dueOrdersResponse.getCustomer().getCustomerFname());//set customer name
            customerPhone = dueOrdersResponse.getCustomer().getPhone();
            customerAddress = dueOrdersResponse.getCustomer().getAddress();
            ordersRv.setLayoutManager(new LinearLayoutManager(getContext()));
            /**
             * for show selected customer list in Recycler view
             */


            List<Order> orderList = dueOrdersResponse.getOrders();
            /**
             * if with order don't have any due ignore this order for this purpose so write the condition
             */
            mainDueOrderList.clear();
            for (int i = 0; i < orderList.size(); i++) {
                if (Integer.parseInt(orderList.get(i).getDue()) > 0) {
                    mainDueOrderList.add(orderList.get(i));
                }
            }


            //Double totalDueOfTheUser = mainDueOrderList.stream().mapToDouble(value -> Double.parseDouble(value.getDue())).sum();
            double totalDueOfTheUser = 0;
            for (int i = 0; i < mainDueOrderList.size(); i++) {
                totalDueOfTheUser += Double.parseDouble(mainDueOrderList.get(i).getDue());
            }


            totalDue.setText(String.valueOf(totalDueOfTheUser));//set total due

            if (!mainDueOrderList.isEmpty()) {
                ordersRv.setVisibility(View.VISIBLE);
                noOrderFound.setVisibility(View.GONE);
                /**
                 * set due orderList in recycler view
                 */
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

    // hide keyboard
    public static void HIDE_KEYBOARD(FragmentActivity activity) {
        try {
            View view = activity.findViewById(android.R.id.content);
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception ignored) {
        }
    }

    private void getPreviousFragmentData() {
        /**
         * get data from management fragment
         */
        toolbarTitle.setText(getArguments().getString("name"));
    }


    @OnClick(R.id.backbtn)
    public void backClick() {
        CustomerOrderAdapter.selectedOrderList.clear();
        getActivity().onBackPressed();
    }

    @OnClick(R.id.image_view)
    public void setArrowBtnClick() {
        expandAndCollapseLayout();
    }

    @OnClick(R.id.receivedNow)
    public void receivedNowClick() {

        double currentTotalDue = Double.parseDouble(totalDue.getText().toString());


        List<String> selectedOrderList = new ArrayList<>(CustomerOrderAdapter.selectedOrderList);


        double currentSelectedTotal = 0;
        for (int i = 0; i < orders.size(); i++) {
            for (int i1 = 0; i1 < CustomerOrderAdapter.selectedOrderList.size(); i1++) {
                if (orders.get(i).getOrderID().equals(selectedOrderList.get(i1))) {
                    currentSelectedTotal += Double.parseDouble(orders.get(i).getDue());
                }
            }
        }
        CustomerOrderAdapter.selectedOrderList.clear();

        if (customerId != null && currentTotalDue > 0) {
            Bundle bundle = new Bundle();
            bundle.putString("customerId", customerId);
            bundle.putString("totalDue", String.valueOf(currentSelectedTotal));
            bundle.putString("customerName", customerNameTv.getText().toString());
            bundle.putString("customerPhone", customerPhone);
            bundle.putString("customerAddress", customerAddress);

            Navigation.findNavController(getView()).navigate(R.id.action_dueCollectionFragment_to_duePaymentReceivedFragment, bundle);
        } else {
            if (currentTotalDue == 0.0) {//just for show a specific message to user
                Toasty.info(getContext(), "You don't have any due", Toasty.LENGTH_LONG).show();
                return;
            }
            Toasty.info(getContext(), "Please select a customer by search", Toasty.LENGTH_LONG).show();
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getCustomersFromServer(String token, String vendorID, String key) {
        isDataFetching = true;

        dueCollectionViewModel.apiCallForGetCustomers(getActivity(), token, vendorID, key);
        dueCollectionViewModel.getCustomerList().observe(this, customerSearchResponse -> {
            customerResponseList = new ArrayList<>();
            customerResponseList.clear();
            customerResponseList.addAll(customerSearchResponse.getLists());
            List<String> customerNameList = new ArrayList<>();
            customerList = customerSearchResponse.getLists();

            for (int i = 0; i < customerList.size(); i++) {
                CustomerResponse customerResponse = customerList.get(i);
                customerNameList.add(customerResponse.getCompanyName() + " @ " + customerResponse.getCustomerFname());
            }

            // customerList.forEach(customerResponse -> customerNameList.add(customerResponse.getCompanyName() + " @ " + customerResponse.getCustomerFname()));


            if (customerNameList.isEmpty()) { // show message in the item if the list is empty
                customerNameList.add("No data found!");
            }
            customerArrayAdapter = new ArrayAdapter<>(getContext(), R.layout.filter_model, R.id.customerNameModel, customerNameList);
            customerNameEt.setAdapter(customerArrayAdapter);
            customerNameEt.showDropDown();
            isDataFetching = false;
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void expandAndCollapseLayout() {
        if (expandableLayout.isExpanded()) {
            expandableLayout.collapse();
            imageView.setImageDrawable(getContext().getDrawable(R.drawable.ic_arrow_drop_up_black_24dp));
        } else {
            expandableLayout.expand();
            imageView.setImageDrawable(getContext().getDrawable(R.drawable.ic_arrow_drop_down_black_24dp));
        }
    }

    // expand the layout
    private void expandLayout() {
        if (!expandableLayout.isExpanded()) {
            expandableLayout.expand();
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_drop_down_black_24dp));
        }
    }

    // collapse the layout
    @SuppressLint("UseCompatLoadingForDrawables")
    private void collapseLayout() {
        if (expandableLayout.isExpanded()) {
            expandableLayout.collapse();
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_drop_up_black_24dp));
        }
    }
}