package com.rupayan_housing.view.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rupayan_housing.R;
import com.rupayan_housing.adapter.AddNewLimitPaymentInstructionAdapter;
import com.rupayan_housing.adapter.PaymentInstructionAdapter;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.AddNewLimitInstructionResponse;
import com.rupayan_housing.serverResponseModel.PaymentInstructionResponse;
import com.rupayan_housing.viewModel.AddNewLimitInstructionViewModel;
import com.rupayan_housing.viewModel.PaymentInstructionViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class PaymentInstructionFragment extends Fragment {
    List<PaymentInstructionResponse> paymentInstructionResponseList;
    List<AddNewLimitInstructionResponse> addNewLimitInstructionResponseList;
    private PaymentInstructionViewModel paymentInstructionViewModel;
    private AddNewLimitInstructionViewModel addNewLimitInstructionViewModel;
    private int mYear, mMonth, mDay;
    String vendorId, currentDate, userId, storeId;
    AddNewLimitPaymentInstructionAdapter adapter;

    private boolean isDataFetching = false; // variable to detect whether server data fetch is loading or not

    @BindView(R.id.dateEditText)
    EditText dateEditText;
    @BindView(R.id.searchButton)
    Button searchButton;
    @BindView(R.id.resetButton)
    Button resetButton;
    @BindView(R.id.payment_instruction_rv)
    RecyclerView paymentInstructionRv;
    @BindView(R.id.empty_instruction_list_warning)
    TextView emtyWarning;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.add_new_limit_rv)
    RecyclerView addNewLimitRv;
    @BindView(R.id.add_new_limit_button)
    Button addNewLimitButton;
    @BindView(R.id.note_edittext)
    EditText noteEdittext;
    @BindView(R.id.submit_button)
    Button submitButton;
    @BindView(R.id.toolbarTitle)
    TextView toolbarTitle;

    View view;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_payment_instruction, container, false);
        ButterKnife.bind(this, view);
        paymentInstructionViewModel = ViewModelProviders.of(this).get(PaymentInstructionViewModel.class);
        addNewLimitInstructionViewModel = ViewModelProviders.of(this).get(AddNewLimitInstructionViewModel.class);
        vendorId = PreferenceManager.getInstance(getContext()).getUserCredentials().getVendorID();
        userId = PreferenceManager.getInstance(getContext()).getUserCredentials().getUserId();
        storeId = PreferenceManager.getInstance(getContext()).getUserCredentials().getStoreID();
        currentDate = getCurrentDate();

        initComponent();


        dateEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /**
                 * get current date payment instruction from server
                 */
                getCurrentDateDataFromServer();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        dateEditText.setOnClickListener(v -> {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (view, year, monthOfYear, dayOfMonth) -> dateEditText.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth), mYear, mMonth, mDay);
            datePickerDialog.show();

        });
        addNewLimitButton.setOnClickListener(v -> showAddNewLimitPaymentInstructionList());
        searchButton.setOnClickListener(v -> {
            paymentInstructionResponseList.clear();
            addNewLimitInstructionResponseList.clear();
            getPaymentInstructionResponse(vendorId, dateEditText.getText().toString());
            showPaymentInstructionList();
        });
        resetButton.setOnClickListener(v -> {
            if (dateEditText.getText().toString().equals(currentDate)) {
                return;
            }
            dateEditText.setText(getCurrentDate());
            paymentInstructionResponseList.clear();
            addNewLimitInstructionResponseList.clear();
            getPaymentInstructionResponse(vendorId, dateEditText.getText().toString());
        });
        submitButton.setOnClickListener(v -> {
            //if payment limit edit text is empty then return
            if (checkThePaymentLimitIsEmptyOrNot()) {
                return;
            }

            //if there is no data to add new payment limit then simply return
            if (addNewLimitInstructionResponseList.isEmpty()) {
                return;
            }

            //check if the payment limit is greater than the due amount or not
            //if payment limit is greater than due amount then don't submit the request
            //submit the request only when payment limit is equal or less than the due amount
            if (checkThePaymentLimitIsValidOrNot()) {
                return;
            }


            String note = noteEdittext.getText().toString().trim();
            if (note.isEmpty()) {
                noteEdittext.setError("Note is mandatory.");
                noteEdittext.requestFocus();
                return;
            }

            submitNewPaymentLimit(getPaymentLimitAmount(), AddNewLimitPaymentInstructionAdapter.customerIdArray, userId, vendorId, storeId, noteEdittext.getText().toString(), dateEditText.getText().toString());

//            //reload the instruction list
//            paymentInstructionResponseList.clear();
//            addNewLimitInstructionResponseList.clear();
//            getPaymentInstructionResponse(vendorId, dateEditText.getText().toString());
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (Build.VERSION.SDK_INT >= 26) {
                ft.setReorderingAllowed(false);
            }
            ft.detach(this).attach(this).commit();
            noteEdittext.setText("");


        });

        return view;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initComponent() {
        toolbarTitle.setText("Payment Instruction");
        dateEditText.setText(getCurrentDate());
        /**
         * get current date  payment instruction from server
         */
        getCurrentDateDataFromServer();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getCurrentDate() {
      /*  Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());*/

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
        String currentDateAndTime = df.format(new Date());

        return currentDateAndTime;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getCurrentDateDataFromServer() {
        getPaymentInstructionResponse(vendorId, dateEditText.getText().toString());
        adapter = new AddNewLimitPaymentInstructionAdapter(getActivity(), addNewLimitInstructionResponseList);
        refreshLayout.setColorSchemeResources(R.color.colorG, R.color.colorG, R.color.colorG);
        refreshLayout.setOnRefreshListener(() -> {
            refreshLayout.setRefreshing(true);
            (new Handler()).postDelayed(() -> {
                refreshLayout.setRefreshing(false);
                getPaymentInstructionResponse(vendorId, dateEditText.getText().toString());
            }, 500);
            Toasty.info(getActivity(), "Data updated", Toasty.LENGTH_LONG).show();
        });
    }

    @SuppressLint("SetTextI18n")
    private void getPaymentInstructionResponse(String vendorID, String date) {
        isDataFetching = true;

        paymentInstructionRv.setHasFixedSize(true);
        paymentInstructionRv.setLayoutManager(new GridLayoutManager(getContext(), 1));

        paymentInstructionViewModel.apiCallForGetPaymentInstructionList(getActivity(), vendorID, date);
        paymentInstructionViewModel.getPaymentInstructionList().observe(getViewLifecycleOwner(), paymentInstruction -> {
            paymentInstructionResponseList = paymentInstruction.getPaymentInstructionResponseList();
            addNewLimitInstructionResponseList = paymentInstruction.getAddNewPaymentInstructionsList();
            /**
             * now set the recycler view
             */
            PaymentInstructionAdapter adapter = new PaymentInstructionAdapter(getActivity(), paymentInstructionResponseList);
            paymentInstructionRv.setAdapter(adapter);

        });
    }

    private void showPaymentInstructionList() {
        paymentInstructionRv.setHasFixedSize(true);
        paymentInstructionRv.setLayoutManager(new GridLayoutManager(getContext(), 1));

        PaymentInstructionAdapter adapter = new PaymentInstructionAdapter(getActivity(), paymentInstructionResponseList);
        paymentInstructionRv.setAdapter(adapter);
    }

    private void showAddNewLimitPaymentInstructionList() {

        addNewLimitRv.setHasFixedSize(true);
        addNewLimitRv.setLayoutManager(new GridLayoutManager(getContext(), 1));

        /**
         * now set NewLimit Payment Instruction List in recycler view
         */
        AddNewLimitPaymentInstructionAdapter adapter = new AddNewLimitPaymentInstructionAdapter(getActivity(), addNewLimitInstructionResponseList);
        addNewLimitRv.setAdapter(adapter);
    }


    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void submitNewPaymentLimit(List<String> paymentLimitArray, List<String> customerIdArray, String userId, String vendorId, String storeId, String note, String paymentDate) {
        isDataFetching = true;

        addNewLimitInstructionViewModel.apiCallForSubmitNewPaymentLimit(getActivity(),paymentLimitArray, customerIdArray, userId, vendorId, storeId, note, paymentDate);
        addNewLimitInstructionViewModel.getAddNewPaymentInstructionList().observe(getViewLifecycleOwner(), addNewPaymentResponse -> {

            String message;
            message = addNewPaymentResponse.getMessage();

            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

            //emtyWarning.setVisibility(View.VISIBLE);
            //emtyWarning.setText(message);

        });
    }

    private List<String> getPaymentLimitAmount() {
        List<String> paymentLimitAmount = new ArrayList<>();

        for (int i = 0; i < addNewLimitRv.getChildCount(); i++) {
            String value = ((EditText) addNewLimitRv.getLayoutManager().findViewByPosition(i).findViewById(R.id.payment_limit_edittext)).getText().toString();
            paymentLimitAmount.add(value);
        }
        return paymentLimitAmount;
    }


    private boolean checkThePaymentLimitIsValidOrNot() {

        for (int i = 0; i < addNewLimitRv.getChildCount(); i++) {
            String value = ((EditText) Objects.requireNonNull(Objects.requireNonNull(addNewLimitRv.getLayoutManager()).findViewByPosition(i)).findViewById(R.id.payment_limit_edittext)).getText().toString();
            if (Integer.parseInt(value) > addNewLimitInstructionResponseList.get(i).getDue()) {
                ((EditText) Objects.requireNonNull(addNewLimitRv.getLayoutManager().findViewByPosition(i)).findViewById(R.id.payment_limit_edittext)).setError("Limit amount can't be greater than due amount");
                ((EditText) Objects.requireNonNull(addNewLimitRv.getLayoutManager().findViewByPosition(i)).findViewById(R.id.payment_limit_edittext)).requestFocus();
                return true;

            }
        }
        return false;
    }

    private boolean checkThePaymentLimitIsEmptyOrNot() {
        for (int i = 0; i < addNewLimitRv.getChildCount(); i++) {
            String value = ((EditText) Objects.requireNonNull(Objects.requireNonNull(addNewLimitRv.getLayoutManager()).findViewByPosition(i)).findViewById(R.id.payment_limit_edittext)).getText().toString();
            if (value.isEmpty()) {
                return true;
            }
        }
        return false;
    }


    @OnClick(R.id.backbtn)
    public void backClick() {
        getActivity().onBackPressed();
    }
}