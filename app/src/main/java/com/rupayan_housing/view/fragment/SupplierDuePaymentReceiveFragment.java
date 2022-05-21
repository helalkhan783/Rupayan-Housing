package com.rupayan_housing.view.fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
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

 import com.jaredrummler.materialspinner.MaterialSpinner;
import com.rupayan_housing.R;
import com.rupayan_housing.adapter.CustomerOrderAdapter;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.AccountResponse;
import com.rupayan_housing.serverResponseModel.MainBank;
import com.rupayan_housing.serverResponseModel.PaymentToResponse;
import com.rupayan_housing.viewModel.DuePaymentReceivedViewModel;
import com.rupayan_housing.viewModel.SupplierDueViewModel;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class SupplierDuePaymentReceiveFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    private SupplierDueViewModel supplierDueViewModel;
    private DuePaymentReceivedViewModel duePaymentReceivedViewModel;

    View view;
    @BindView(R.id.toolbarTitle)
    TextView toolbar;
    String paymentLimit;

    @BindView(R.id.accountNoEt)
    EditText accountNoEt;
    @BindView(R.id.brandsEt)
    EditText brandsEt;
    @BindView(R.id.nameTvSupplier)
    TextView nameTv;
    @BindView(R.id.phoneTvSupplier)
    TextView phoneTv;
    @BindView(R.id.addressTvSupplier)
    TextView addressTv;
    @BindView(R.id.receivableDueSupplier)
    TextView receivableDue;
    @BindView(R.id.dueLimitSupplier)
    TextView dueLimitTv;
    @BindView(R.id.paidAmountEtSupplier)
    TextView paidAmountEt;
    @BindView(R.id.paymentToOptionSupplier)
    MaterialSpinner paymentToOptionSupplier;
    @BindView(R.id.receiptMethodTvSupplier)
    MaterialSpinner receiptMethodTv;
    @BindView(R.id.receiptSubMethodSupplier)
    MaterialSpinner receiptSubMethod;
    @BindView(R.id.selectBankTvSupplier)
    MaterialSpinner selectBankTv;
    @BindView(R.id.SelectAccountNoTvSupplier)
    MaterialSpinner selectAccountNoTv;
    @BindView(R.id.ReceiptRemarksSupplier)
    EditText receiptRemarksEt;
    @BindView(R.id.dateSupplier)
    TextView date;
    @BindView(R.id.totalDueSupplier)
    TextView totalDue;
    @BindView(R.id.saveBtnSupplier)
    Button saveBtn;


    @BindView(R.id.receiptSubMethodDdownView)
    LinearLayout receiptSubMethodDdownView;
    @BindView(R.id.bankDDownView)
    LinearLayout bankDDownView;
    @BindView(R.id.accountDdownView)
    LinearLayout accountDdownView;
    @BindView(R.id.paymentToDDownView)
    LinearLayout paymentToDDownView;
    @BindView(R.id.withoutCash)
    LinearLayout withoutCash;


    List<String> selectedBankIdList = new ArrayList<>();

    String customerId;
    List<MainBank> banks = new ArrayList<>();
    List<String> receiptMethodList = new ArrayList<>();
    List<PaymentToResponse> paymentToOptionList = new ArrayList<>();
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


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_supplier_due_payment_receive, container, false);
        ButterKnife.bind(this, view);
        supplierDueViewModel = ViewModelProviders.of(this).get(SupplierDueViewModel.class);

        duePaymentReceivedViewModel = ViewModelProviders.of(this).get(DuePaymentReceivedViewModel.class);


        getDataFromPreViousFragment();
        /**
         * set customer info from previous DueCollectionFragment
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setCustomerInformation();
        }

        /**
         * get page data from server
         */
        supplierDueViewModel.apiCallForGetSupplierOrder(getActivity(), customerId);

        supplierDueViewModel.getSupplierOrders().observe(getViewLifecycleOwner(), supplierOrdersResponse -> {


            /**
             * set payment to option
             */
            paymentToOptionList.addAll(supplierOrdersResponse.getPaymentTo());

           /* paymentToOptionList.forEach(paymentToResponse -> {
                paymentToOptionNameList.add(paymentToResponse.getMainBankName());
            });*/
            for (int i = 0; i < paymentToOptionList.size(); i++) {
                paymentToOptionNameList.add(paymentToOptionList.get(i).getMainBankName());
            }


            paymentToOptionSupplier.setItems(paymentToOptionNameList);

            /**
             * for set current customer due limit
             */

            dueLimitTv.setText(String.valueOf(supplierOrdersResponse.getCustomer().getDueLimit()));
            /**
             * set  receiptMethod
             */
            receiptMethodList.add(supplierOrdersResponse.getPaymentTypes().get_1());
            receiptMethodList.add(supplierOrdersResponse.getPaymentTypes().get_2());
            receiptMethodList.add(supplierOrdersResponse.getPaymentTypes().get_3());
            receiptMethodList.add(supplierOrdersResponse.getPaymentTypes().get_4());
            /**
             * set  receiptSubMethod
             */
            receiptSubMethodList.add(supplierOrdersResponse.getPaymentSubTypes().get_1());
            receiptSubMethodList.add(supplierOrdersResponse.getPaymentSubTypes().get_2());
            receiptSubMethodList.add(supplierOrdersResponse.getPaymentSubTypes().get_3());
            /**
             * set the bank list
             */
            banks = supplierOrdersResponse.getMainBanks();
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
        receiptSubMethod.setOnItemSelectedListener((view, position, id, item) -> {
            //selectPaymentSubType = receiptSubMethodList.get(position);
            selectPaymentSubType = String.valueOf(position + 1);
            receiptSubMethod.setText(item.toString());
            receiptSubMethod.setSelected(true);
            //receiptSubMethod.setSelectedIndex(position);
        });


        /**
         * set receipt method after click
         */
        receiptMethodTv.setOnItemSelectedListener((view, position, id, item) -> {
            Toast.makeText(getContext(), "" + item, Toast.LENGTH_SHORT).show();
    /*        receiptSubMethodDdownView.setVisibility(View.VISIBLE);
            bankDDownView.setVisibility(View.VISIBLE);
            accountDdownView.setVisibility(View.VISIBLE);
            paymentToDDownView.setVisibility(View.VISIBLE);*/

            withoutCash.setVisibility(View.VISIBLE);


            if (position == 0) {//here position 0 means this is cash so...
        /*        receiptSubMethodDdownView.setVisibility(View.GONE);
                bankDDownView.setVisibility(View.GONE);
                accountDdownView.setVisibility(View.GONE);
                paymentToDDownView.setVisibility(View.GONE);*/
                withoutCash.setVisibility(View.GONE);
            }


            //selectedReceiptMethod = receiptMethodList.get(position);//set selected ReceiptMethod for send to server
            selectedReceiptMethod = String.valueOf(position + 1);//set selected ReceiptMethod for send to server


            receiptMethodTv.setText(item.toString());
            receiptMethodTv.setSelected(true);


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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setItemsToSelectAccountTv(List<AccountResponse> accountResponseList) {
        optionList.clear();
        accountResponseList.forEach(accountResponse -> {
            selectedBankIdList.add(accountResponse.getBankID());

            String option = accountResponse.getAccountantName() + "/" + accountResponse.getBankBranch() + "/" + accountResponse.getAccountNumber();
            optionList.add(option);
        });
        selectAccountNoTv.setItems(optionList);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        /*System.out.println(dtf.format(now));*/
        date.setText(dtf.format(now));
    }

    private void getDataFromPreViousFragment() {
        toolbar.setText("Pay Due");
        paymentLimit = getArguments().getString("paymentLimit");
    }

    @OnClick(R.id.backbtn)
    public void backBtn() {
        getActivity().onBackPressed();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @OnClick(R.id.saveBtnSupplier)
    public void saveBtnClick() {
        String customDiscount = "0";

        String token = PreferenceManager.getInstance(getContext()).getUserCredentials().getToken();
        String collectedPaidAmount = paidAmountEt.getText().toString();
        String receiptRemarks = receiptRemarksEt.getText().toString();

        String branch = brandsEt.getText().toString();
        String accountNo = accountNoEt.getText().toString();

        if (collectedPaidAmount.isEmpty()) {
            paidAmountEt.setError("Paid amount mandatory");
            paidAmountEt.requestFocus();
            return;
        }
        if (receiptRemarks.isEmpty()) {
            receiptRemarksEt.setError("Receipt Remarks Mandatory");
            receiptRemarksEt.requestFocus();
            return;
        }
        if (Integer.parseInt(selectedReceiptMethod) != 1) {//here 1 means cash without cash branch and account no is mandatory
            if (branch.isEmpty()) {
                brandsEt.setError("Branch Mandatory");
                brandsEt.requestFocus();
                return;
            }
            if (accountNo.isEmpty()) {
                accountNoEt.setError("Account No is Mandatory");
                accountNoEt.requestFocus();
                return;
            }
        }


        if (Double.parseDouble(paymentLimit) != 0) {//if payment limit have 0 then user can set any amount
            if (Double.parseDouble(collectedPaidAmount) < Double.parseDouble(paymentLimit) || Double.parseDouble(collectedPaidAmount) > Double.parseDouble(paymentLimit)) {
                paidAmountEt.setError("Paid Amount Should be " + paymentLimit);
                return;
            }
        }


        String storeId = PreferenceManager.getInstance(getContext()).getUserCredentials().getStoreID();
        String userId = PreferenceManager.getInstance(getContext()).getUserCredentials().getUserId();
        String vendorId = PreferenceManager.getInstance(getContext()).getUserCredentials().getVendorID();
        String permissions = PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions();
        String profileTypeId = PreferenceManager.getInstance(getContext()).getUserCredentials().getProfileTypeId();
        String paymentTypeVal = selectedReceiptMethod;


        String selectedPaymentToOption = selectPaymentOptionSupplier;


        String paymentSubType = selectPaymentSubType;
        String bankId = selectedBankId;
        String totalDuee = totalDue.getText().toString();
        Set<String> customerOrderIdList = CustomerOrderAdapter.selectedOrderList;


        // List<Integer> orders = customerOrderIdList.stream().map(Integer::parseInt).collect(Collectors.toList());
        Log.d("LLL", String.valueOf(customerOrderIdList));
        supplierDueViewModel.apiCallForPaySupplierDue(getActivity(),
                token, customerOrderIdList, collectedPaidAmount, totalDuee, storeId, userId, permissions, profileTypeId, customerId, vendorId,
                paymentTypeVal, paymentSubType, customDiscount, selectedBankId, branch, accountNo,
                receiptRemarks,
                date.getText().toString(),
                selectedPaymentToOption
        );
        getActivity().onBackPressed();
    }


    @OnClick(R.id.dateSupplier)
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