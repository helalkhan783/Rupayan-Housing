package com.rupayan_housing.view.fragment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProviders;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.rupayan_housing.R;
import com.rupayan_housing.adapter.CustomerOrderAdapter;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.AccountResponse;
import com.rupayan_housing.serverResponseModel.MainBank;
import com.rupayan_housing.viewModel.DueCollectionViewModel;
import com.rupayan_housing.viewModel.DuePaymentReceivedViewModel;
import com.rupayan_housing.viewModel.PayDueAmountViewModel;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class DuePaymentReceivedFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener {
    View view;
    private DueCollectionViewModel dueCollectionViewModel;
    private DuePaymentReceivedViewModel duePaymentReceivedViewModel;
    private PayDueAmountViewModel payDueAmountViewModel;
    @BindView(R.id.backbtn)
    ImageButton backBtn;
    @BindView(R.id.toolbarTitle)
    TextView toolbar;
    @BindView(R.id.nameTv)
    TextView nameTv;
    @BindView(R.id.phoneTv)
    TextView phoneTv;
    @BindView(R.id.addressTv)
    TextView addressTv;
    @BindView(R.id.receivableDue)
    TextView receivableDue;
    @BindView(R.id.dueLimit)
    TextView dueLimitTv;
    @BindView(R.id.paidAmountEt)
    TextView paidAmountEt;
    @BindView(R.id.receiptMethodTv)
    MaterialSpinner receiptMethodTv;
    @BindView(R.id.receiptSubMethod)
    MaterialSpinner receiptSubMethod;
    @BindView(R.id.selectBankTv)
    MaterialSpinner selectBankTv;
    @BindView(R.id.SelectAccountNoTv)
    MaterialSpinner selectAccountNoTv;
    @BindView(R.id.ReceiptRemarks)
    EditText receiptRemarksEt;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.totalDue)
    TextView totalDue;
    @BindView(R.id.saveBtn)
    Button saveBtn;

    @BindView(R.id.subPaymentAll)
    LinearLayout subPaymentAll;


    String customerId;
    List<MainBank> banks = new ArrayList<>();
    List<String> receiptMethodList = new ArrayList<>();
    List<String> receiptSubMethodList = new ArrayList<>();
    List<String> bankList = new ArrayList<>();
    List<String> bankIdList = new ArrayList<>();
    List<String> optionList = new ArrayList<>();
    List<String> accountBankId = new ArrayList<>();
    List<AccountResponse> accountResponseList = new ArrayList<>();
    String selectedReceiptMethod;
    String selectedBankId;
    String selectPaymentSubType;
    String accountNo;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_due_payment_received, container, false);
        ButterKnife.bind(this, view);
        toolbar.setText("Due Payment Received");//set the toolbar title

        hideKeyboard(getActivity());


        dueCollectionViewModel = ViewModelProviders.of(this).get(DueCollectionViewModel.class);
        duePaymentReceivedViewModel = ViewModelProviders.of(this).get(DuePaymentReceivedViewModel.class);
        payDueAmountViewModel = ViewModelProviders.of(this).get(PayDueAmountViewModel.class);

        /**
         * set customer info from previous DueCollectionFragment
         */
            setCustomerInformation();


        /**
         * now get data for view from server
         */
        dueCollectionViewModel
                .apiCallForgetDueOrdersBySelectedCustomerIdAndVendorId(getActivity(), customerId, PreferenceManager.getInstance(getContext()).getUserCredentials().getVendorID());
        dueCollectionViewModel.getGetDueOrders().observe(getViewLifecycleOwner(), dueOrdersResponse -> {
            /**
             * for set current customer due limit
             */
            dueLimitTv.setText(dueOrdersResponse.getCustomer().getDueLimit());
            /**
             * set  receiptMethod
             */
            receiptMethodList.add(dueOrdersResponse.getPaymentTypes().get_1());
            receiptMethodList.add(dueOrdersResponse.getPaymentTypes().get_2());
            receiptMethodList.add(dueOrdersResponse.getPaymentTypes().get_3());
            receiptMethodList.add(dueOrdersResponse.getPaymentTypes().get_4());
            /**
             * set  receiptSubMethod
             */
            receiptSubMethodList.add(dueOrdersResponse.getPaymentSubTypes().get_1());
            receiptSubMethodList.add(dueOrdersResponse.getPaymentSubTypes().get_2());
            receiptSubMethodList.add(dueOrdersResponse.getPaymentSubTypes().get_3());
            /**
             * set the bank list
             */
            banks = dueOrdersResponse.getMainBanks();
           /* banks.forEach(mainBank -> {
                bankList.add(mainBank.getMainBankName());
                bankIdList.add(mainBank.getMainBankID());
            });*/
            for (int i = 0; i < banks.size(); i++) {
                bankList.add(banks.get(i).getMainBankName());
                bankIdList.add(banks.get(i).getMainBankID());
            }

            receiptMethodTv.setItems(receiptMethodList);
            receiptSubMethod.setItems(receiptSubMethodList);
            selectBankTv.setItems(bankList);
        });

        receiptMethodTv.setOnItemSelectedListener((view, position, id, item) -> {
            selectedReceiptMethod = receiptMethodList.get(position);
            /**
             * here position 0 means "Cash" so we can hide the other payment info
             */
            if (position==0){
                subPaymentAll.setVisibility(View.GONE);
            }
        });
        receiptSubMethod.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                selectPaymentSubType = receiptSubMethodList.get(position);
            }
        });


        /**
         * when user select the bank account then get the the bank account list by user selected bank account name id
         */
        selectBankTv.setOnItemSelectedListener((view, position, id, item) -> {
//            selectBankTv.setText(item.toString());
//            selectedBankIndex = position;\

            selectedBankId = bankIdList.get(position);
            /**
             * now get bank account list by bank name id
             */
            duePaymentReceivedViewModel.apiCallForGetAccountListByBankNameId(//call api for get the list
                    getActivity(), PreferenceManager.getInstance(getContext()).getUserCredentials().getVendorID(),
                    banks.get(position).getMainBankID());//

            /**
             * now get the list from viewModel and the bank account list to selectAccountNoTv dropdown
             */
            duePaymentReceivedViewModel.getAccountListByBankId().observe(getViewLifecycleOwner(), accountNumberListResponse -> {
                accountResponseList.clear();
                /**
                 * select account list in accountList dropdown
                 */
                setItemsToSelectAccountTv(accountNumberListResponse.getLists());
            });
        });


        selectAccountNoTv.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                accountNo = accountBankId.get(position);

            }
        });


        return view;
    }

    /**
     * set customer info from previous DueCollectionFragment
     */
     private void setCustomerInformation() {
        customerId = getArguments().getString("customerId");
        nameTv.setText(getArguments().getString("customerName"));
        phoneTv.setText(getArguments().getString("customerPhone"));
        addressTv.setText(getArguments().getString("customerAddress"));
        totalDue.setText(getArguments().getString("totalDue"));
        receivableDue.setText(getArguments().getString("totalDue"));

        /**
         * we want to show set current date in dateView
         */
         DateFormat df = new SimpleDateFormat("KK:mm:ss a, dd/MM/yyyy", Locale.getDefault());
         String currentDateAndTime = df.format(new Date());

        /*System.out.println(dtf.format(now));*/
        date.setText(currentDateAndTime);
    }

    private void setItemsToSelectAccountTv(List<AccountResponse> accountResponseList) {
        optionList.clear();
        accountBankId.clear();
      /*  accountResponseList.forEach(accountResponse -> {
            String option = accountResponse.getAccountantName() + "/" + accountResponse.getBankBranch() + "/" + accountResponse.getAccountNumber();
            optionList.add(option);
        });*/
        for (int i = 0; i < accountResponseList.size(); i++) {
            String option = accountResponseList.get(i).getAccountantName() + "/" + accountResponseList.get(i).getBankBranch() + "/" + accountResponseList.get(i).getAccountNumber();
            optionList.add(option);
            accountBankId.add(accountResponseList.get(i).getBankID());
        }


        selectAccountNoTv.setItems(optionList);
    }


    @OnClick(R.id.backbtn)
    public void backBtnClick() {
        getActivity().onBackPressed();
    }


    @OnClick(R.id.saveBtn)
    public void saveCurrentUserDuePayment() {
        String collectedPaidAmount = paidAmountEt.getText().toString();
        String storeId = PreferenceManager.getInstance(getContext()).getUserCredentials().getStoreID();
        String userId = PreferenceManager.getInstance(getContext()).getUserCredentials().getUserId();
        String vendorId = PreferenceManager.getInstance(getContext()).getUserCredentials().getVendorID();
        String permissions = PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions();
        String profileTypeId = PreferenceManager.getInstance(getContext()).getUserCredentials().getProfileTypeId();
        String paymentTypeVal = selectedReceiptMethod;

        String paymentSubType = selectPaymentSubType;
        String bankId = selectedBankId;
        String totalDuee = totalDue.getText().toString();
        Set<String> customerOrderIdList = CustomerOrderAdapter.selectedOrderList;

        String remarks = receiptRemarksEt.getText().toString();
        /// List<Integer> orders = customerOrderIdList.stream().map(Integer::parseInt).collect(Collectors.toList());
        Log.d("LLL", String.valueOf(customerOrderIdList));

        if (collectedPaidAmount.isEmpty()) {
            paidAmountEt.setError("Paid Amount is Mandatory");
            paidAmountEt.requestFocus();
            return;
        }
        if (paymentTypeVal == null) {
            Toasty.info(getContext(), "Please Select your Receipt Method", Toasty.LENGTH_LONG).show();
            receiptMethodTv.requestFocus();
            return;
        }

        if (remarks.isEmpty()) {
            receiptRemarksEt.setError("Remarks Mandatory");
            receiptRemarksEt.requestFocus();
            return;
        }
        if (!paymentTypeVal.equals("Cash")) {
            if (paymentSubType == null) {
                Toasty.info(getContext(), "Please Select Receipt Sub Method", Toasty.LENGTH_LONG).show();
                receiptSubMethod.requestFocus();
                return;
            }

            if (bankId == null) {
                Toasty.info(getContext(), "Please Select your Bank", Toasty.LENGTH_LONG).show();
                selectBankTv.requestFocus();
                return;
            }
            if (accountNo == null) {
                Toasty.info(getContext(), "Please Select your Bank", Toasty.LENGTH_LONG).show();
                selectAccountNoTv.requestFocus();
                return;
            }

        }


        payDueAmountViewModel.apiCallForPayDueAmount(getActivity(), vendorId, customerOrderIdList, collectedPaidAmount,
                totalDuee, storeId, userId, permissions, profileTypeId, paymentTypeVal,
                paymentSubType, accountNo, date.getText().toString(),
                remarks);

        CustomerOrderAdapter.selectedOrderList.clear();//clear the selected due id
        getActivity().onBackPressed();
    }


    @OnClick(R.id.date)
    public void getDate() {
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
     * for set user selected date
     *
     * @param view
     * @param year
     * @param monthOfYear
     * @param dayOfMonth
     */
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        // String selectedDate = dayOfMonth + "/" + monthOfYear + "/" + year;//set the selected date
        int month = monthOfYear;
        if (month == 12) {
            month = 1;
        } else {
            month = monthOfYear + 1;
        }

        String selectedDate = year + "-" + month + "-" + dayOfMonth;//set the selected date
        date.setText(selectedDate);
    }


}