package com.rupayan_housing.view.fragment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.jaredrummler.materialspinner.MaterialSpinner;
import com.rupayan_housing.R;
import com.rupayan_housing.adapter.SalesRequisitionProductsAdapter;
import com.rupayan_housing.adapter.SelectedSalesRequisitionProductsAdapter;
import com.rupayan_housing.adapter.SelectedSalesRequisitionProductsAdapter2;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.CustomerResponse;
import com.rupayan_housing.serverResponseModel.EnterpriseResponse;
import com.rupayan_housing.serverResponseModel.SalesRequisitionItems;
import com.rupayan_housing.serverResponseModel.SalesRequisitionItemsResponse;
import com.rupayan_housing.viewModel.AddRequisitionViewmodel;
import com.rupayan_housing.viewModel.DiscountViewModel;
import com.rupayan_housing.viewModel.DueCollectionViewModel;
import com.rupayan_housing.viewModel.SalesRequisitionViewModel;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class NewSalesRequisitionFragmentInfo extends Fragment implements DatePickerDialog.OnDateSetListener {
    private SalesRequisitionViewModel salesRequisitionViewModel;
    private AddRequisitionViewmodel addRequisitionViewmodel;
    private DiscountViewModel discountViewModel;
    private DueCollectionViewModel dueCollectionViewModel;
    private ArrayAdapter<String> customerArrayAdapter;
    List<CustomerResponse> customerResponseList = new ArrayList<>();
    List<CustomerResponse> customerList;
    View view;
    private boolean isDataFetching = false; // variable to detect whether server data fetch is loading or not
    @BindView(R.id.toolbarTitle)
    TextView toolbar;
    @BindView(R.id.selectedProductsRv)
    RecyclerView selectedProductsRv;
    @BindView(R.id.discountEt)
    EditText discountEt;
    @BindView(R.id.selectEnterPriceDdown)
    MaterialSpinner selectEnterPriceDdown;
    @BindView(R.id.totalAmount)
    TextView totalAmount;
    @BindView(R.id.grandTotal)
    TextView grandTotal;
    @BindView(R.id.tv_discount)
    TextView discountNoteTV;
    @BindView(R.id.paymentTypeDown)
    MaterialSpinner paymentTypeDown;
    @BindView(R.id.customerSearchEt)
    AutoCompleteTextView customerSearchEt;
    @BindView(R.id.receiptAmountEt)

    EditText receiptAmountEt;
    @BindView(R.id.startDate)
    TextView startDate;
    @BindView(R.id.endOrderDate)
    TextView endDate;
    String customerId;

    public static List<String> selectedPriceList;
    public static List<String> quantityList;
    public List<String> productIdList = new ArrayList<>();
    List<String> productTitleList = new ArrayList<>();//this is title list
    private List<SalesRequisitionItemsResponse> salesRequisitionItemsResponsesList;
    private List<SalesRequisitionItems> salesRequisitionItemsList;
    List<EnterpriseResponse> enterpriseResponsesList = new ArrayList<>();
    int enterpriseId;
    int paymentType;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new_sales_requisition2, container, false);
        ButterKnife.bind(this, view);
        salesRequisitionViewModel = ViewModelProviders.of(this).get(SalesRequisitionViewModel.class);
        discountViewModel = ViewModelProviders.of(this).get(DiscountViewModel.class);
        dueCollectionViewModel = ViewModelProviders.of(this).get(DueCollectionViewModel.class);
        addRequisitionViewmodel = ViewModelProviders.of(this).get(AddRequisitionViewmodel.class);
        getDataFromPreviousFragment();
        loadDataToRecyclerView();
        loadEnterPrice();

        customerSearchEt.setOnItemClickListener((parent, view, position, id) -> {
            customerId = customerResponseList.get(position).getCustomerID();
            Log.d("CUSTOMER_ID", customerId);
        });

        customerSearchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (customerSearchEt.isPerformingCompletion()) {
                    return;
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                if (!charSequence.toString().trim().isEmpty() && !isDataFetching) {
                    String currentText = customerSearchEt.getText().toString();
                    getCustomerDetailsFromServer(currentText);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        discountEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                double currentTotal = Double.parseDouble(getArguments().getString("total"));
                double discountNote = 0;
                if (discountEt.getText().toString().isEmpty()) {
                    discountNote = 0;
                    discountEt.requestFocus();
                    return;
                }
                discountNote = Double.parseDouble(discountEt.getText().toString());

                double afterDiscount = (currentTotal * discountNote) / 100;
                grandTotal.setText(String.valueOf(afterDiscount));

               /* discountViewModel.getTotalAfterDiscount(currentTotal, discountNote).observe(getViewLifecycleOwner(), new Observer<Double>() {
                    @Override
                    public void onChanged(Double afterDiscount) {
                        grandTotal.setText(String.valueOf(afterDiscount));
                        discountNoteTV.setText(String.valueOf((currentTotal - afterDiscount)));
                    }
                });*/
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        paymentTypeDown.setOnItemSelectedListener((view, position, id, item) -> {
            paymentTypeDown.setSelected(true);
            if (position == 0) {
                paymentType = 1;
                Log.d("PAYMENT", "1");
            }
            if (position == 1) {
                paymentType = 2;
                Log.d("PAYMENT", "2");
            }
        });

        selectEnterPriceDdown.setOnItemSelectedListener((view, position, id, item) -> {
            selectEnterPriceDdown.setSelected(true);
            enterpriseId = Integer.parseInt(enterpriseResponsesList.get(position).getStoreID());
            Log.d("SSS", String.valueOf(enterpriseId));
        });
        return view;
    }

    private void getCustomerDetailsFromServer(String currentText) {
        String token = PreferenceManager.getInstance(getActivity()).getUserCredentials().getToken();
        String vendorId = PreferenceManager.getInstance(getActivity()).getUserCredentials().getVendorID();
        dueCollectionViewModel.apiCallForGetCustomers(getActivity(), token, vendorId, currentText);
        dueCollectionViewModel.getCustomerList().observe(getViewLifecycleOwner(), customerSearchResponse -> {
            customerResponseList.clear();
            customerResponseList.addAll(customerSearchResponse.getLists());
            List<String> customerNameList = new ArrayList<>();
            customerList = new ArrayList<>();
            customerList.clear();
            customerList = customerSearchResponse.getLists();


            // customerList.forEach(customerResponse -> customerNameList.add(customerResponse.getCompanyName()));//for api level 28 or above
            for (int i = 0; i < customerList.size(); i++) {
                customerNameList.add(customerList.get(i).getCompanyName());
            }


            if (customerNameList.isEmpty()) { // show message in the item if the list is empty
                customerNameList.add("No data found!");
            }
            customerArrayAdapter = new ArrayAdapter<>(getContext(), R.layout.filter_model, R.id.customerNameModel, customerNameList);
            customerSearchEt.setAdapter(customerArrayAdapter);
            customerSearchEt.showDropDown();
            isDataFetching = false;
        });
    }

     private void loadEnterPrice() {
        salesRequisitionViewModel.getEnterpriseResponse(getActivity()).observe(getViewLifecycleOwner(), enterprise -> {
            enterpriseResponsesList.addAll(enterprise.getEnterprise());
            List<String> enterPriseName = new ArrayList<>();

            for (int i = 0; i < enterprise.getEnterprise().size(); i++) {
                enterPriseName.add(enterprise.getEnterprise().get(i).getStoreName());
            }
            selectEnterPriceDdown.setItems(enterPriseName);


        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadDataToRecyclerView() {
        quantityList = new ArrayList<>();
        productTitleList.clear();
        productIdList.clear();
        if (SalesRequisitionProductsAdapter.selectedProductsList != null) {
            for (int i = 0; i < SalesRequisitionProductsAdapter.selectedProductsList.size(); i++) {
                double currentItemPrice = Double.parseDouble(NewSaleFragment.getAllPrice().get(i));
                double currentItemQuantity = Double.parseDouble(NewSaleFragment.getAllQuantity().get(i));
                if (currentItemPrice != 0 && currentItemQuantity != 0) {
                    selectedPriceList = new ArrayList<>();
                    productIdList.add(SalesRequisitionProductsAdapter.selectedProductsList.get(i).getProductID());
                    productTitleList.add(SalesRequisitionProductsAdapter.selectedProductsList.get(i).getProductTitle());
                    selectedPriceList.add(String.valueOf(currentItemPrice));
                    quantityList.add(String.valueOf(currentItemQuantity));
                    salesRequisitionItemsResponsesList = new ArrayList<>();


                    salesRequisitionItemsResponsesList.add(SalesRequisitionProductsAdapter.selectedProductsList.get(i));
                }
            }
            /**
             * now set adapter in recyclerview
             */
            selectedProductsRv.setLayoutManager(new LinearLayoutManager(getActivity()));
            SelectedSalesRequisitionProductsAdapter adapter = new SelectedSalesRequisitionProductsAdapter(getActivity(), salesRequisitionItemsResponsesList);
            selectedProductsRv.setAdapter(adapter);

        } else {
            if (SalesRequisitionProductsAdapter.salesRequisitionItemsList != null) {
                selectedPriceList = new ArrayList<>();
                salesRequisitionItemsList = new ArrayList<>();
                selectedPriceList.clear();
                salesRequisitionItemsList.clear();
                try {
                    for (int i = 0; i < SalesRequisitionProductsAdapter.salesRequisitionItemsList.size(); i++) {
                        double currentItemPrice = Double.parseDouble(NewSaleFragment.getAllPrice().get(i));
                        double currentItemQuantity = Double.parseDouble(NewSaleFragment.getAllQuantity().get(i));
                        if (currentItemPrice != 0 && currentItemQuantity != 0) {
                            productIdList.add(SalesRequisitionProductsAdapter.salesRequisitionItemsList.get(i).getProductID());
                            productTitleList.add(SalesRequisitionProductsAdapter.salesRequisitionItemsList.get(i).getProductTitle());
                            selectedPriceList.add(String.valueOf(currentItemPrice));
                            quantityList.add(String.valueOf(currentItemQuantity));
                            salesRequisitionItemsList.add(SalesRequisitionProductsAdapter.salesRequisitionItemsList.get(i));
                        }
                    }
                } catch (Exception e) {
                    Log.d("ERROR", e.getLocalizedMessage());
                }
                selectedProductsRv.setLayoutManager(new LinearLayoutManager(getActivity()));
                SelectedSalesRequisitionProductsAdapter2 adapter = new SelectedSalesRequisitionProductsAdapter2(getActivity(), salesRequisitionItemsList);
                selectedProductsRv.setAdapter(adapter);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private void getDataFromPreviousFragment() {
        toolbar.setText("Sale Details");
        totalAmount.setText(getArguments().getString("total") + " " + "TK");
        grandTotal.setText(getArguments().getString("total") + " " + "TK");
        List<String> paymentType = new ArrayList<>();
        paymentType.add("Cash");//val = 1
        paymentType.add("Cheque");//val = 2
        paymentTypeDown.setItems(paymentType);

        /**
         * we want to show set current date in dateView
         */
      /*  DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();*///for api level 24 or above

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();

        startDate.setText(formatter.format(date));
        endDate.setText(formatter.format(date));
    }

    @OnClick(R.id.addMoreBtn)
    public void addMoreBtn() {
        Navigation.findNavController(getView()).navigate(R.id.action_newSalesRequisitionFragment2_to_newSaleFragment);
    }

    @OnClick(R.id.backbtn)
    public void onClickBackBtn() {
        getActivity().onBackPressed();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @OnClick(R.id.card_submit_btn)
    public void submitInfo() {

       /* if (customerId == null || customerList == null) {
            customerSearchEt.requestFocus();
            return;
        }*/


        String receiptAmount = receiptAmountEt.getText().toString();

        if (receiptAmount.isEmpty()) {
            receiptAmount = "0";//for default value
        }

        String total_discount = discountEt.getText().toString();
        if (total_discount.isEmpty()) {
            total_discount = "0";//for default value
        }

        if (customerId == null) {
            customerSearchEt.setError("Search Customer by Name");
            customerSearchEt.requestFocus();
            return;
        }


        String soldFrom = String.valueOf(enterpriseId);
        List<String> unitList = new ArrayList<>();//this is unit list
        unitList.add("2");
        List<String> sellingPriceList = selectedPriceList;//this is sellingPrice list
        List<String> quantitys = quantityList;//this is quantityList list
        String customDiscount = "0";
        String collected_amount = totalAmount.getText().toString();
        String is_confirm = "0";
        String paymentTypeSelected = "1";//for default value
        paymentTypeSelected = String.valueOf(paymentType);

       /* if (salesRequisitionItemsList != null) {
            salesRequisitionItemsList.forEach(salesRequisitionItems -> {
                //productIdList.add(salesRequisitionItems.getProductID());
               // unitList.add(salesRequisitionItems.getUnit());
                //productTitleList.add(salesRequisitionItems.getProductTitle());
            });
        }*/

        String finalReceiptAmount = receiptAmount;
        String finalTotal_discount = total_discount;
        String finalPaymentTypeSelected = paymentTypeSelected;

        discountViewModel.getLastOrderId(getActivity()).observe(getViewLifecycleOwner(), lastOrderId -> {
            addRequisitionViewmodel.apiCallForCreateRequisition(
                    getActivity(), productIdList, String.valueOf(enterpriseId), unitList, String.valueOf(lastOrderId), customerId, productTitleList,
                    sellingPriceList, quantitys, finalTotal_discount, finalReceiptAmount, finalPaymentTypeSelected, startDate.getText().toString(), endDate.getText().toString()
            );
            addRequisitionViewmodel.isAddRequisitionSuccessful().observe(getViewLifecycleOwner(), duePaymentResponse -> {
                if (duePaymentResponse.getStatus() == 200) {
                    Toasty.success(getContext(), "Add Requisition Successful", Toasty.LENGTH_LONG).show();
                }
            });
            getActivity().onBackPressed();
        });
    }

    /**
     * for set start order date
     */
    @OnClick(R.id.startDate)
    public void getStartOrderDate() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dialog = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR), // Initial year selection
                now.get(Calendar.MONTH), // Initial month selection
                now.get(Calendar.DAY_OF_MONTH) // Inital day selection
        );
        dialog.show(getActivity().getSupportFragmentManager(), "Datepickerdialog");
    }

    /**
     * for set end order date
     */
    @OnClick(R.id.endOrderDate)
    public void getEndOrderDate() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dialog = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR), // Initial year selection
                now.get(Calendar.MONTH), // Initial month selection
                now.get(Calendar.DAY_OF_MONTH) // Inital day selection
        );
        dialog.show(getActivity().getSupportFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        int month = monthOfYear;
        if (month == 12) {
            month = 1;
        } else {
            month = monthOfYear + 1;
        }
        String selectedDate = year + "-" + month + "-" + dayOfMonth;//set the selected date
        startDate.setText(selectedDate);
        endDate.setText(selectedDate);
    }
}