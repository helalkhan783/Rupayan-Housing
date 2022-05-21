package com.rupayan_housing.view.fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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
import com.rupayan_housing.adapter.ExpenseDueOrdersAdapter;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.CustomerResponse;
import com.rupayan_housing.serverResponseModel.CustomerSearchResponse;
import com.rupayan_housing.serverResponseModel.ExpenseOrdersResponse;
import com.rupayan_housing.viewModel.ExpenseVendorViewModel;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

import static com.rupayan_housing.view.fragment.DueCollectionFragment.HIDE_KEYBOARD;

public class SearchExpenseDue extends Fragment {
    View view;
    private ExpenseVendorViewModel expenseVendorViewModel;

    @BindView(R.id.toolbarTitle)
    TextView toolBar;
    private boolean isDataFetching = false; // variable to detect whether server data fetch is loading or not

    List<CustomerResponse> customerResponseList;
    List<CustomerResponse> customerList;
    private ArrayAdapter<String> customerArrayAdapter;

    @BindView(R.id.customerNameEtExpense)
    AutoCompleteTextView customerNameEtExpense;
    @BindView(R.id.tv_total_amountExpense)
    TextView totalDue;
    @BindView(R.id.card_submit_btn_Expense)
    CardView submitBtn;
    @BindView(R.id.expandable_layoutExpense)
    ExpandableLayout expandableLayoutExpense;
    @BindView(R.id.image_viewExpense)
    ImageView image_viewExpense;
    @BindView(R.id.customerNameTvExpense)
    TextView customerNameTv;
    @BindView(R.id.ordersRvExpense)
    RecyclerView ordersRv;
    @BindView(R.id.tv_noticeExpense)
    TextView noOrderFound;

    List<ExpenseOrdersResponse> expenseOrdersResponses = new ArrayList<>();


    String selectedCustomerId;
    private String customerPhone;
    private String customerAddress;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_expense_due, container, false);
        ButterKnife.bind(this, view);
        HIDE_KEYBOARD(getActivity());
        expenseVendorViewModel = ViewModelProviders.of(this).get(ExpenseVendorViewModel.class);
        getDataFromPreviousFragment();

        customerNameEtExpense.setOnItemClickListener((parent, view, position, id) -> {
            if (!customerResponseList.isEmpty()) {
                customerNameEtExpense.clearFocus();

                HIDE_KEYBOARD(getActivity());

                String selectedExpenseVendorId = customerResponseList.get(position).getCustomerID();
                String currentUserId = PreferenceManager.getInstance(getContext()).getUserCredentials().getVendorID();
                /**
                 * for show supplier order in recyclerView
                 */
                getDueOrdersAndShowInRecyclerView(selectedExpenseVendorId, currentUserId); // get due orders from server

            } else {
                customerNameEtExpense.getText().clear();
            }

        });

        /**
         * for search expense Vendor
         */
        customerNameEtExpense.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (customerNameEtExpense.isPerformingCompletion()) {
                    return;
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // if item is not selected
                if (!charSequence.toString().trim().isEmpty() && !isDataFetching) {
                    collapseLayout(); // collapse bottom layout
                    String currentText = customerNameEtExpense.getText().toString();
                    /**
                     * method for search a customer
                     */
                    getExpenseVendorFromServer(currentText);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getDueOrdersAndShowInRecyclerView(String selectedSExpenseVendor, String selectedUserVendorId) {
        List<ExpenseOrdersResponse> mainDueOrderList = new ArrayList<>();


        /**
         * call the api for get expense due orders
         */
        expenseVendorViewModel.apiCallForGetExpenseVendorByVendorAndCustomerId(getActivity(), selectedSExpenseVendor);
        /**
         *now get data from the responses
         */
        expenseVendorViewModel.getExpenseVendorByVendorAndCustomerId().observe(getViewLifecycleOwner(), expenseDueResponse -> {


            expenseOrdersResponses = expenseDueResponse.getOrders();


            selectedCustomerId = selectedSExpenseVendor;


            customerNameTv.setText(expenseDueResponse.getCustomer().getCustomerFname());//set customer name
            customerPhone = expenseDueResponse.getCustomer().getPhone();
            customerAddress = expenseDueResponse.getCustomer().getAddress();
            ordersRv.setLayoutManager(new LinearLayoutManager(getContext()));
            /**
             * for show selected customer list in Recycler view
             */


            List<ExpenseOrdersResponse> orderList = expenseDueResponse.getOrders();

            /**
             * if with order don't have any due ignore this order for this purpose so write the condition
             */
            mainDueOrderList.clear();
            for (int i = 0; i < orderList.size(); i++) {
                if (orderList.get(i).getDue() > 0) {
                    mainDueOrderList.add(orderList.get(i));
                }
            }
            /**
             * now set data to recyclerview
             */
            //Double totalDueOfTheUser = mainDueOrderList.stream().mapToDouble(value -> value.getDue()).sum();
            Double totalDueOfTheUser=0.0;
            for (int i = 0; i < mainDueOrderList.size(); i++) {
                totalDueOfTheUser+=mainDueOrderList.get(i).getDue();
            }



            totalDue.setText(String.valueOf(totalDueOfTheUser));//set total due

            if (!mainDueOrderList.isEmpty()) {
                ordersRv.setVisibility(View.VISIBLE);
                noOrderFound.setVisibility(View.GONE);
                /**
                 * set expense due list in recyclerview
                 */
                ExpenseDueOrdersAdapter adapter = new ExpenseDueOrdersAdapter(getActivity(), mainDueOrderList);
                ordersRv.setAdapter(adapter);
            } else {
                ordersRv.setVisibility(View.GONE);
                noOrderFound.setText("No order Found");
                noOrderFound.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * for search expense vendors
     */
    private void getExpenseVendorFromServer(String currentText) {
        expenseVendorViewModel.apiCallForSearchExpenseVendor(getActivity(), currentText);
        expenseVendorViewModel.getSearchExpenseVendors().observe(getViewLifecycleOwner(), new Observer<CustomerSearchResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChanged(CustomerSearchResponse customerSearchResponse) {

                customerResponseList = new ArrayList<>();
                customerResponseList.clear();
                customerResponseList.addAll(customerSearchResponse.getLists());
                List<String> customerNameList = new ArrayList<>();
                customerList = customerSearchResponse.getLists();
                for (int i = 0; i < customerList.size(); i++) {
                    customerNameList.add(customerList.get(i).getCompanyName() + " @ " + customerList.get(i).getCustomerFname());
                }
                if (customerNameList.isEmpty()) { // show message in the item if the list is empty
                    customerNameList.add("No data found!");
                }
                customerArrayAdapter = new ArrayAdapter<>(getContext(), R.layout.filter_model, R.id.customerNameModel, customerNameList);
                customerNameEtExpense.setAdapter(customerArrayAdapter);
                customerNameEtExpense.showDropDown();
                isDataFetching = false;
            }
        });
    }

    private void getDataFromPreviousFragment() {
        toolBar.setText("Pay Due (Expense)");


    }

    @OnClick(R.id.backbtn)
    public void backBtnClick() {
        /**
         * for clear searching order data
         */
        ExpenseDueOrdersAdapter.selectedOrderList.clear();
        getActivity().onBackPressed();
    }


    @OnClick(R.id.card_submit_btn_Expense)
    public void submit() {
        double currentTotalDue = Double.parseDouble(totalDue.getText().toString());


        List<String> selectedOrderList = new ArrayList<>(ExpenseDueOrdersAdapter.selectedOrderList);

        double selectedTotalOrderDue = 0;

        for (int i = 0; i < expenseOrdersResponses.size(); i++) {
            for (int i1 = 0; i1 < selectedOrderList.size(); i1++) {
                if (expenseOrdersResponses.get(i).getOrderID().equals(selectedOrderList.get(i1))) {
                    selectedTotalOrderDue += expenseOrdersResponses.get(i).getDue();
                }
            }
        }


        if (selectedCustomerId != null && currentTotalDue > 0) {
            Bundle bundle = new Bundle();

            bundle.putStringArrayList("selectedDueList",new ArrayList<>(selectedOrderList) );

            bundle.putString("customerId", selectedCustomerId);
            bundle.putString("totalDue", String.valueOf(selectedTotalOrderDue));
            bundle.putString("customerName", customerNameTv.getText().toString());
            bundle.putString("customerPhone", customerPhone);
            bundle.putString("customerAddress", customerAddress);


            Navigation.findNavController(getView())
                    .navigate(R.id.action_searchExpenseDue_to_expenseDuePaymentReceivedFragment, bundle);
        } else {
            if (currentTotalDue == 0.0) {//just for show a specific message to user
                Toasty.info(getContext(), "You don't have any due", Toasty.LENGTH_LONG).show();
                return;
            }
            Toasty.info(getContext(), "Please select a customer by search", Toasty.LENGTH_LONG).show();
        }

    }


    // expand the layout
    private void expandLayout() {
        if (!expandableLayoutExpense.isExpanded()) {
            expandableLayoutExpense.expand();
            image_viewExpense.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_drop_down_black_24dp));
        }
    }

    // collapse the layout
    private void collapseLayout() {
        if (expandableLayoutExpense.isExpanded()) {
            expandableLayoutExpense.collapse();
            image_viewExpense.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_drop_up_black_24dp));
        }
    }
}