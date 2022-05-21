package com.rupayan_housing.view.fragment.production.packating;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.rupayan_housing.R;
import com.rupayan_housing.clickHandle.ConfirmPackatingPageClickHandle;
import com.rupayan_housing.databinding.FragmentConfirmPackatingBinding;
import com.rupayan_housing.localDatabase.PackatingDatabaseHelper;
import com.rupayan_housing.serverResponseModel.CustomerResponse;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.viewModel.DueCollectionViewModel;
import com.rupayan_housing.viewModel.PacketingViewModel;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ConfirmPackating extends BaseFragment implements DatePickerDialog.OnDateSetListener {
    private FragmentConfirmPackatingBinding binding;
    private PacketingViewModel packetingViewModel;
    private DueCollectionViewModel dueCollectionViewModel;
    private PackatingDatabaseHelper packatingDatabaseHelper;

    /**
     * for search Reference  person
     */
    private List<CustomerResponse> referencePersonResponseList;
    private List<String> referencePersonNameList;


    private String selectedEnterprise, selectedStore, selectedReferencePerson;

    private final String NO_DATA_FOUND = "No Data Found";
    private boolean isDataFetching = false; // variable to detect whether server data fetch is loading or not
    private ArrayAdapter<String> customerNameAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_confirm_packating, container, false);
        packatingDatabaseHelper = new PackatingDatabaseHelper(getContext());
        packetingViewModel = new ViewModelProvider(this).get(PacketingViewModel.class);
        dueCollectionViewModel = new ViewModelProvider(this).get(DueCollectionViewModel.class);
        binding.toolbar.toolbarTitle.setText("Confirm Packating");
        hideKeyboard(getActivity());
        /**
         * show current date and needed data
         */
        getDataFromPreviousFragment();

        binding.toolbar.setClickHandle(() -> {
            hideKeyboard(getActivity());
            getActivity().onBackPressed();
        });

        binding.setClickHandle(new ConfirmPackatingPageClickHandle() {
            @Override
            public void submit() {
                if (!(isInternetOn(getActivity()))) {
                    infoMessage(getActivity().getApplication(), "Please Check your Internet Connection");
                    return;
                }
                if (selectedReferencePerson == null) {
                    binding.referencePerson.setError("Empty");
                    binding.referencePerson.requestFocus();
                    return;
                }
                if (binding.note.getText().toString().isEmpty()) {
                    binding.note.setError("Empty");
                    binding.note.requestFocus();
                    return;
                }

                showDialouge();
            }

            @Override
            public void dateBtn() {
                showDatePickerDialog();
            }
        });


        /**
         * handle Reference Person suggested item click
         */
        binding.referencePerson.setOnItemClickListener((parent, view, position, id) -> {
            hideKeyboard(getActivity());
            if (referencePersonNameList.get(position).equals(NO_DATA_FOUND)) {
                binding.referencePerson.getText().clear();
            } else {
                selectedReferencePerson = referencePersonResponseList.get(position).getCustomerID();
            }
        });


        /**
         * now handle search Reference Person
         */
        binding.referencePerson.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!binding.referencePerson.isPerformingCompletion()) {
                    return;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (binding.referencePerson.isPerformingCompletion()) { // selected a product
                    return;
                }
                if (!s.toString().trim().isEmpty() && !isDataFetching) {
                    String currentText = binding.referencePerson.getText().toString();
                    if (!(isInternetOn(getActivity()))) {
                        infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
                    } else {
                        selectedReferencePerson = null;//for handle proper selected customer
                        getReferencePersonBySearchKey(currentText);
                    }

                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return binding.getRoot();
    }

    private void showDialouge() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        @SuppressLint("InflateParams")
        View view = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.purchase_dialog, null);
        //Set the view
        builder.setView(view);
        TextView tvTitle, tvMessage;
        ImageView imageIcon = view.findViewById(R.id.img_icon);
        tvMessage = view.findViewById(R.id.tv_message);
        tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("Do You Want to Add This Cartooning ?");//set warning title
        tvMessage.setText("MIS ERP");
        imageIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.app_logo));//set warning image
        Button bOk = view.findViewById(R.id.btn_ok);
        Button cancel = view.findViewById(R.id.cancel);
        AlertDialog alertDialog = builder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        cancel.setOnClickListener(v -> alertDialog.dismiss());//for cancel
        bOk.setOnClickListener(v -> {
            alertDialog.dismiss();
            validationAndSubmit();
        });

        alertDialog.show();
    }

    private void getReferencePersonBySearchKey(String currentText) {
        /**
         * call
         */
        dueCollectionViewModel
                .apiCallForGetCustomers(
                        getActivity(),
                        getToken(getActivity().getApplication()),
                        getVendorId(getActivity().getApplication()),
                        currentText
                );

        /**
         * get data from above calling api
         */
        dueCollectionViewModel.getCustomerList()
                .observe(getViewLifecycleOwner(), response -> {
                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "Something Wrong");
                        return;
                    }
                    if (response.getStatus() != 200) {
                        errorMessage(getActivity().getApplication(), "Something Wrong");
                        return;
                    }

                    isDataFetching = true;
                    selectedReferencePerson = null;
                    referencePersonResponseList = new ArrayList<>();
                    referencePersonResponseList.clear();
                    referencePersonResponseList.addAll(response.getLists());

                    referencePersonNameList = new ArrayList<>();
                    referencePersonNameList.clear();


                    for (int i = 0; i < response.getLists().size(); i++) {
                        referencePersonNameList.add("" + response.getLists().get(i).getCustomerFname());
                    }

                    if (referencePersonNameList.isEmpty()) {
                        referencePersonNameList.add(NO_DATA_FOUND);
                    }

                    customerNameAdapter = new ArrayAdapter<String>(getContext(), R.layout.filter_model, R.id.customerNameModel, referencePersonNameList);
                    binding.referencePerson.setAdapter(customerNameAdapter);
                    binding.referencePerson.showDropDown();
                    isDataFetching = false;
                });
    }

    private void validationAndSubmit() {


        packetingViewModel.getPackagingId(getActivity(), "15")
                .observe(getViewLifecycleOwner(), getPackatingNo -> {
                    String lastOrderId = String.valueOf(getPackatingNo);
                    /**
                     * now send add new packating data to server
                     */

                    List<String> productIdList = new ArrayList<>();
                    List<String> productQuantityList = new ArrayList<>();
                    List<String> productTotalList = new ArrayList<>();


                    for (int i = 0; i < AddNewPackating.databasepackagingList.size(); i++) {
                        productIdList.add(AddNewPackating.databasepackagingList.get(i).getItemId());
                        productQuantityList.add(AddNewPackating.databasepackagingList.get(i).getQuantity());
                        productTotalList.add(AddNewPackating.databasepackagingList.get(i).getTotal());
                    }

                    packetingViewModel.addNewPackating(
                            getActivity(), selectedEnterprise, lastOrderId, binding.note.getText().toString(),
                            productIdList, productQuantityList, productTotalList, selectedStore, binding.processingDate.getText().toString(),
                            selectedReferencePerson)
                            .observe(getViewLifecycleOwner(), response -> {
                                if (response == null) {
                                    errorMessage(getActivity().getApplication(), "Something Wrong");
                                    return;
                                }
                                if (response.getStatus() == 400) {
                                    hideKeyboard(getActivity());
                                    getActivity().onBackPressed();
                                    infoMessage(getActivity().getApplication(), "" + response.getMessage());
                                    return;
                                }
                                if (response.getStatus() == 200) {
                                    packatingDatabaseHelper.deleteAllData();
                                    AddNewPackating.databasepackagingList.clear();
                                    hideKeyboard(getActivity());
                                    infoMessage(getActivity().getApplication(), "" + response.getMessage());
                                    getActivity().onBackPressed();
                                }
                            });
                });
    }

    private void showDatePickerDialog() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dialog = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR), // Initial year selection
                now.get(Calendar.MONTH), // Initial month selection
                now.get(Calendar.DAY_OF_MONTH) // Initial day selection
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

        String selectedDate = dayOfMonth + "-" + month + "-" + year;//set the selected date
        binding.processingDate.setText(selectedDate);
    }

    private void getDataFromPreviousFragment() {

        assert getArguments() != null;
        selectedEnterprise = getArguments().getString("selectedEnterpriseId");
        selectedStore = getArguments().getString("selectedStore");
        /**
         * set current date will send input from user
         */
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date(System.currentTimeMillis());
        //System.out.println(formatter.format(date));
        String currentDate = formatter.format(date);
        /*System.out.println(dtf.format(now));*/
        binding.processingDate.setText(currentDate);
    }
}