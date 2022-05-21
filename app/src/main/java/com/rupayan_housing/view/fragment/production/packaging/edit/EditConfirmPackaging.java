package com.rupayan_housing.view.fragment.production.packaging.edit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rupayan_housing.R;
import com.rupayan_housing.clickHandle.ConfirmPackagingClickHandle;
import com.rupayan_housing.databinding.FragmentConfirmPackagingBinding;
import com.rupayan_housing.databinding.FragmentEditConfirmPackagingBinding;
import com.rupayan_housing.localDatabase.MyPackagingDatabaseHelper;
import com.rupayan_housing.serverResponseModel.CustomerResponse;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.view.fragment.production.packaging.AddNewPackaging;
import com.rupayan_housing.viewModel.DueCollectionViewModel;
import com.rupayan_housing.viewModel.PackagingViewModel;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;


public class EditConfirmPackaging extends BaseFragment implements DatePickerDialog.OnDateSetListener {
     private FragmentEditConfirmPackagingBinding binding;
    private PackagingViewModel packagingViewModel;
    private DueCollectionViewModel dueCollectionViewModel;
    private MyPackagingDatabaseHelper myPackagingDatabaseHelper;
    public static int isBack = 0;


    private String NO_DATA_FOUND = "No Data Found";
    private boolean isDataFetching = false; // variable to detect whether server data fetch is loading or not
    private ArrayAdapter<String> customerNameAdapter;


    /**
     * for search Reference  person
     */
    private List<CustomerResponse> referencePersonResponseList;
    private List<String> referencePersonNameList;


    private String selectedReferencePerson, selectedStore, selectedEnterPrice, selectedPackagingId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_confirm_packaging, container, false);
        packagingViewModel = new ViewModelProvider(this).get(PackagingViewModel.class);
        dueCollectionViewModel = new ViewModelProvider(this).get(DueCollectionViewModel.class);
        myPackagingDatabaseHelper = new MyPackagingDatabaseHelper(getContext());
        binding.toolbar.toolbarTitle.setText("Update Packaging");
        binding.toolbar.setClickHandle(() -> {
            hideKeyboard(getActivity());
            getActivity().onBackPressed();
        });
        getDataFromPreviousFragment();

        /**
         * Now get Page Data From Server
         */
/*
        getPageDataFromServer();
*/


        binding.setClickHandle(new ConfirmPackagingClickHandle() {
            @Override
            public void submit() {
                if (selectedStore == null) {
                    infoMessage(getActivity().getApplication(), "Store selection missing");
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
                showDialog();
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

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        @SuppressLint("InflateParams")
        View view = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.purchase_dialog, null);
        //Set the view
        builder.setView(view);
        TextView tvTitle, tvMessage;
        ImageView imageIcon = view.findViewById(R.id.img_icon);
        tvMessage = view.findViewById(R.id.tv_message);
        tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("Do You Want to Update This Packaging ?");//set warning title
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
            validationAndSave();
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

    private void validationAndSave() {

        List<String> itemIdList = new ArrayList<>();
        List<String> convertedIdList = new ArrayList<>();
        List<String> weightList = new ArrayList<>();
        List<String> quantityList = new ArrayList<>();
        List<String> packagingNotes = new ArrayList<>();
        List<String> totalWeightList = new ArrayList<>();

        List<String> packedProductList = new ArrayList<>();//will add all pkt product id here

        for (int i = 0; i < EditPackegingData.packagingDatabaseModelList.size(); i++) {
            if (!(EditPackegingData.packagingDatabaseModelList.get(i).getItemId().equals("0"))
                    && !(EditPackegingData.packagingDatabaseModelList.get(i).getPackedId().equals("0"))) {
                itemIdList.add(EditPackegingData.packagingDatabaseModelList.get(i).getItemId());
                convertedIdList.add(EditPackegingData.packagingDatabaseModelList.get(i).getPackedId());
                weightList.add(EditPackegingData.packagingDatabaseModelList.get(i).getWeight());
                quantityList.add(EditPackegingData.packagingDatabaseModelList.get(i).getQuantity());
                packedProductList.add("");
                if (EditPackegingData.packagingDatabaseModelList.get(i).getNote() == null || EditPackegingData.packagingDatabaseModelList.get(i).getNote().isEmpty()) {
                    packagingNotes.add("");
                } else {
                    packagingNotes.add(EditPackegingData.packagingDatabaseModelList.get(i).getNote());
                }
                try {
                    totalWeightList.add(((TextView) EditPackegingData.binding.productList.getLayoutManager().findViewByPosition(i).findViewById(R.id.totalWeight)).getText().toString());
                } catch (Exception e) {
                    Log.d("ERROR", "ERROR");
                }
            }
        }
        packagingViewModel.confirmPackaging(
                getActivity(), selectedStore, binding.note.getText().toString(), itemIdList, convertedIdList, weightList, quantityList, packedProductList,
                totalWeightList, packagingNotes, selectedPackagingId, binding.processingDate.getText().toString(), selectedReferencePerson,selectedEnterPrice)
                .observe(getViewLifecycleOwner(), response -> {
                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "Something Wrong");
                        return;
                    }
                    if (response.getStatus() == 400) {
                        infoMessage(getActivity().getApplication(), "" + response.getMessage());
                        return;
                    }
                    if (response.getStatus() != 200) {
                        errorMessage(getActivity().getApplication(), "Something Wrong");
                        return;
                    }

                    hideKeyboard(getActivity());
                    successMessage(getActivity().getApplication(), "" + response.getMessage());
                    myPackagingDatabaseHelper.deleteAllData();
                    AddNewPackaging.packagingDatabaseModelList.clear();
                    isBack = 1;
                    getActivity().onBackPressed();
                });


    }

    private void getPageDataFromServer() {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check your Internet Connection");
            return;
        }
        packagingViewModel.getNextPackagingId(getActivity())
                .observe(getViewLifecycleOwner(), nextPackagingId -> {
                    binding.processNo.setText(String.valueOf(nextPackagingId.getPackagingID()));
                    selectedPackagingId = String.valueOf(nextPackagingId.getPackagingID());
                });
    }

    private void getDataFromPreviousFragment() {
        assert getArguments() != null;
        selectedStore = getArguments().getString("selectedStoreId");
        selectedEnterPrice = getArguments().getString("selectedEnterPrice");


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

}