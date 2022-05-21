package com.rupayan_housing.view.fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.rupayan_housing.R;
import com.rupayan_housing.adapter.ExpenseDueOrdersAdapter;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.AccountResponse;
import com.rupayan_housing.serverResponseModel.MainBank;
import com.rupayan_housing.serverResponseModel.PaymentToExpenseResponse;
import com.rupayan_housing.viewModel.DuePaymentReceivedViewModel;
import com.rupayan_housing.viewModel.ExpenseVendorViewModel;
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

public class ExpenseDuePaymentReceivedFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener {

    private ExpenseVendorViewModel expenseVendorViewModel;
    private DuePaymentReceivedViewModel duePaymentReceivedViewModel;


    View view;
    @BindView(R.id.toolbarTitle)
    TextView toolbar;

    @BindView(R.id.remarksEtExpenseDue)
    EditText remarksEt;
    @BindView(R.id.accountNoEtExpenseDue)
    EditText accountNoEt;
    @BindView(R.id.brandsEtExpenseDue)
    EditText brandsEt;
    @BindView(R.id.nameTvExpenseDue)
    TextView nameTv;
    @BindView(R.id.phoneTvExpenseDue)
    TextView phoneTv;
    @BindView(R.id.addressTvExpenseDue)
    TextView addressTv;
    @BindView(R.id.payableDueExpenseDue)
    TextView payableDue;
    @BindView(R.id.paidAmountEtExpenseDue)
    TextView paidAmountEt;
    @BindView(R.id.paymentToOptionExpenseDue)
    MaterialSpinner paymentToOptionSupplier;
    @BindView(R.id.paymentMethodTvExpenseDue)
    MaterialSpinner paymentMethodTv;
    @BindView(R.id.paymentSubMethodExpenseDue)
    MaterialSpinner paymentSubMethod;
    @BindView(R.id.selectBankTvExpenseDue)
    MaterialSpinner selectBankTv;
    @BindView(R.id.SelectAccountNoTvExpenseDue)
    MaterialSpinner selectAccountNoTv;
    @BindView(R.id.remainingTvExpenseDue)
    TextView remainingRemarksEt;
    @BindView(R.id.dateExpenseDue)
    TextView date;
    @BindView(R.id.totalDueExpenseDue)
    TextView totalDue;
    @BindView(R.id.saveBtnExpenseDue)
    Button saveBtn;

    @BindView(R.id.paymentSnd)
    LinearLayout paymentSnd;

    List<String> selectedBankIdList = new ArrayList<>();

    ArrayList<String> selectedOrderListFromPrevious;
    String customerId;
    List<MainBank> banks = new ArrayList<>();
    List<String> receiptMethodList = new ArrayList<>();
    List<PaymentToExpenseResponse> paymentToOptionList = new ArrayList<>();
    List<String> paymentToOptionNameList = new ArrayList<>();
    List<String> receiptSubMethodList = new ArrayList<>();
    List<String> bankList = new ArrayList<>();
    List<String> bankIdList = new ArrayList<>();
    List<String> optionList = new ArrayList<>();
    List<AccountResponse> accountResponseList = new ArrayList<>();
    String selectedReceiptMethod;
    String selectedBankId;
    String selectPaymentSubType;
    String selectPaymentOptionSupplier;
    private String selectedAccountNo;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_expense_due_payment_received, container, false);
        ButterKnife.bind(this, view);
        expenseVendorViewModel = ViewModelProviders.of(this).get(ExpenseVendorViewModel.class);
        duePaymentReceivedViewModel = ViewModelProviders.of(this).get(DuePaymentReceivedViewModel.class);
        getDataFromPreviousFragment();
        hideKeyboard(getActivity());

        /**
         * now get the page data from server
         */
        expenseVendorViewModel.apiCallForGetExpenseVendorByVendorAndCustomerId(getActivity(), customerId);
        expenseVendorViewModel.getExpenseVendorByVendorAndCustomerId().observe(getViewLifecycleOwner(), expenseDueResponse -> {

            /**
             * set payment to option
             */
            paymentToOptionList.addAll(expenseDueResponse.getPaymentTo());

          /*  paymentToOptionList.forEach(paymentToResponse -> {
                paymentToOptionNameList.add(paymentToResponse.getMainBankName());
            });*/

            for (int i = 0; i < paymentToOptionList.size(); i++) {
                paymentToOptionNameList.add(paymentToOptionList.get(i).getMainBankName());
            }


            paymentToOptionSupplier.setItems(paymentToOptionNameList);
            /**
             * set  receiptMethod
             */
            receiptMethodList.add(expenseDueResponse.getPaymentTypes().get_1());
            receiptMethodList.add(expenseDueResponse.getPaymentTypes().get_2());
            receiptMethodList.add(expenseDueResponse.getPaymentTypes().get_3());
            receiptMethodList.add(expenseDueResponse.getPaymentTypes().get_4());
            /**
             * set  receiptSubMethod
             */
            receiptSubMethodList.add(expenseDueResponse.getPaymentSubTypes().get_1());
            receiptSubMethodList.add(expenseDueResponse.getPaymentSubTypes().get_2());
            receiptSubMethodList.add(expenseDueResponse.getPaymentSubTypes().get_3());
            /**
             * set the bank list
             */
            banks = expenseDueResponse.getMainBanks();
          /*  banks.forEach(mainBank -> {
                bankList.add(mainBank.getMainBankName());
                bankIdList.add(mainBank.getMainBankID());
            });*/

            for (int i = 0; i < banks.size(); i++) {
                bankList.add(banks.get(i).getMainBankName());
                bankIdList.add(banks.get(i).getMainBankID());
            }


            paymentMethodTv.setItems(receiptMethodList);
            paymentSubMethod.setItems(receiptSubMethodList);
            selectBankTv.setItems(bankList);
        });

        /**
         * if user want to select the bank account before select her bank name then show the Info below (as a toast)
         */
        selectAccountNoTv.setOnClickListener(v -> {
            if (selectAccountNoTv.getItems() == null) {
                Toasty.info(getContext(), "Please First Select Your Bank Name", Toasty.LENGTH_LONG).show();
            }
        });


        selectAccountNoTv.setOnItemSelectedListener((view, position, id, item) -> {
            selectAccountNoTv.setText(item.toString());
            selectAccountNoTv.setSelected(true);

            selectedAccountNo = String.valueOf(selectedBankIdList.get(position));

            //selectAccountNoTv.setSelectedIndex(position);
        });
        paymentSubMethod.setOnItemSelectedListener((view, position, id, item) -> {
            //selectPaymentSubType = receiptSubMethodList.get(position);
            selectPaymentSubType = String.valueOf(position + 1);
            paymentSubMethod.setText(item.toString());
            paymentSubMethod.setSelected(true);
            //receiptSubMethod.setSelectedIndex(position);
        });
        paymentMethodTv.setOnItemSelectedListener((view, position, id, item) -> {
            Toast.makeText(getContext(), "" + item, Toast.LENGTH_SHORT).show();

            //selectedReceiptMethod = receiptMethodList.get(position);//set selected ReceiptMethod for send to server
            selectedReceiptMethod = String.valueOf(position + 1);//set selected ReceiptMethod for send to server

            if (position == 0) {//here 0 means cash ,,,so we can hide other bank statement
                paymentSnd.setVisibility(View.GONE);
            } else {
                paymentSnd.setVisibility(View.VISIBLE);
            }


            paymentMethodTv.setText(item.toString());
            paymentMethodTv.setSelected(true);
            //receiptMethodTv.setSelectedIndex(position);
        });


        paymentToOptionSupplier.setOnItemSelectedListener((view, position, id, item) -> {
            selectPaymentOptionSupplier = String.valueOf(position + 1);
            paymentToOptionSupplier.setText(item.toString());
            paymentToOptionSupplier.setSelected(true);
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
        return view;
    }

    private void setItemsToSelectAccountTv(List<AccountResponse> accountResponseList) {
        optionList.clear();


    /*    accountResponseList.forEach(accountResponse -> {
            selectedBankIdList.add(accountResponse.getBankID());
            String option = accountResponse.getAccountantName() + "/" + accountResponse.getBankBranch() + "/" + accountResponse.getAccountNumber();
            optionList.add(option);
        });*/

        for (int i = 0; i < accountResponseList.size(); i++) {
            selectedBankIdList.add(accountResponseList.get(i).getBankID());
            String option = accountResponseList.get(i).getAccountantName() + "/" + accountResponseList.get(i).getBankBranch() + "/" + accountResponseList.get(i).getAccountNumber();
            optionList.add(option);
        }


        selectAccountNoTv.setItems(optionList);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getDataFromPreviousFragment() {
        toolbar.setText("Due Payment (Expense)");
        selectedOrderListFromPrevious = getArguments().getStringArrayList("selectedDueList");
        customerId = getArguments().getString("customerId");
        nameTv.setText(getArguments().getString("customerName"));
        phoneTv.setText(getArguments().getString("customerPhone"));
        addressTv.setText(getArguments().getString("customerAddress"));
        totalDue.setText(getArguments().getString("totalDue"));
        remainingRemarksEt.setText(getArguments().getString("totalDue"));
        payableDue.setText(getArguments().getString("totalDue"));
        /**
         * we want to show set current date in dateView
         */
      /*  DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();*/

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDateAndTime = df.format(new Date());

        /*System.out.println(dtf.format(now));*/
        date.setText(currentDateAndTime);


    }


    /**
     * for pay expense due data
     */

    @RequiresApi(api = Build.VERSION_CODES.N)
    @OnClick(R.id.saveBtnExpenseDue)
    public void payExpenseDue() {
        String customDiscount = "0";
        String token = PreferenceManager.getInstance(getContext()).getUserCredentials().getToken();
        String collectedPaidAmount = paidAmountEt.getText().toString();
        String storeId = PreferenceManager.getInstance(getContext()).getUserCredentials().getStoreID();
        String userId = PreferenceManager.getInstance(getContext()).getUserCredentials().getUserId();
        String paymentTypeVal = selectedReceiptMethod;

        String branch = brandsEt.getText().toString();
        String accountNo = accountNoEt.getText().toString();
        String selectedPaymentToOption = selectPaymentOptionSupplier;
        String remarks = remarksEt.getText().toString();
        String paymentSubType = selectPaymentSubType;
        String totalDuee = totalDue.getText().toString();
        Set<String> customerOrderIdList = ExpenseDueOrdersAdapter.selectedOrderList;
//        List<Integer> orders = customerOrderIdList.stream().map(Integer::parseInt).collect(Collectors.toList());
        Log.d("LLL", String.valueOf(customerOrderIdList));


        if (paymentTypeVal == null) {
            Toasty.info(getActivity(), "Please Select your Payment method", Toasty.LENGTH_LONG).show();
            paymentMethodTv.requestFocus();
            return;
        }

        if (collectedPaidAmount.isEmpty()) {
            paidAmountEt.setError("Paid Amount Empty");
            paidAmountEt.requestFocus();
            return;
        }
        if (remarks.isEmpty()) {
            remarksEt.setError("Remarks Mandatory");
            remarksEt.requestFocus();
            return;
        }
        if (selectedReceiptMethod == null) {
            paymentMethodTv.setError("Select Payment Method");
            paymentMethodTv.requestFocus();
            return;
        }


        ExpenseDueOrdersAdapter.selectedOrderList.clear();
        /**
         * now set data to server
         */
        expenseVendorViewModel.apiCallForPayExpenseDue(
                getActivity(), storeId, customerId, selectedOrderListFromPrevious, collectedPaidAmount, totalDuee, customDiscount, userId,
                paymentTypeVal, paymentSubType, date.getText().toString(), remarks, selectedBankId, branch, accountNo, selectedAccountNo);
        /**
         * after successfully complete the payment user go back to her search fragment
         */
        ExpenseDueOrdersAdapter.selectedOrderList.clear();
        getActivity().onBackPressed();
    }

    @OnClick(R.id.backbtn)
    public void backBtnClick() {
        /**
         * for clear searching orders
         */
        ExpenseDueOrdersAdapter.selectedOrderList.clear();
        getActivity().onBackPressed();
    }

    @OnClick(R.id.dateExpenseDue)
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