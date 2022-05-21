package com.rupayan_housing.view.fragment.QC_QA;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.rupayan_housing.R;
import com.rupayan_housing.databinding.FragmentEditQCQABinding;
import com.rupayan_housing.serverResponseModel.AddQcQaPageResponse;
import com.rupayan_housing.serverResponseModel.EnterprizeList;
import com.rupayan_housing.serverResponseModel.GetEditQcQaResponse;
import com.rupayan_housing.serverResponseModel.TestList;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.viewModel.Qc_QaViewModel;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


public class Edit_QCQAFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener, EditQcQaSelectionHandle {
    private FragmentEditQCQABinding binding;
    private Qc_QaViewModel qc_qaViewModel;

    /**
     * for enterPrise
     */
    private List<EnterprizeList> enterPriceList;
    private List<String> enterPriceNameList;

    /**
     * for testName List
     */
    public List<TestList> testNameList;
    private List<String> testNameNameList;

    private String selectedQcId, selectedSlID;

    private String selectedStoreId;
    private String[] selectedTestNameList;

    List<String> selectedTestIds = new ArrayList<>();

    LinkedList<String> linkedList = new LinkedList<>();
    public GetEditQcQaResponse getEditQcQaResponse;
    int loading = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit__q_c_q_a, container, false);
        qc_qaViewModel = new ViewModelProvider(this).get(Qc_QaViewModel.class);
        binding.toolbar.toolbarTitle.setText("Update QC/QA");


        getDataFromPreviousFragment();
        setCurrentDate();

        /**
         * now get page Data From server
         */
        getPageDataFromServer();

        binding.toolbar.setClickHandle(() -> {
            hideKeyboard(getActivity());
            getActivity().onBackPressed();
        });

        /**
         * For submit edit qc-qa data
         */
        binding.setClickHandle(() -> {
            if (selectedStoreId == null) {
                infoMessage(getActivity().getApplication(), "Please select enterprise");
                return;
            }
            if (binding.sampleName.getText().toString().isEmpty()) {
                infoMessage(getActivity().getApplication(), "Please select sample name");
                return;
            }

            if (getEditQcQaResponse.getQcDetails().isEmpty()) {
                infoMessage(getActivity().getApplication(), "details missing");
                return;
            }
            if (!(isInternetOn(getActivity()))) {
                infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
                return;
            }
            hideKeyboard(getActivity());
            updateQc_qaDialog();
        });
        binding.testDate.setOnClickListener(v -> showDatePickerDialog());

        binding.enterPrise.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStoreId = enterPriceList.get(position).getStoreID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return binding.getRoot();
    }

    private void getDataFromPreviousFragment() {
        assert getArguments() != null;
        selectedQcId = getArguments().getString("id");
        selectedSlID = getArguments().getString("SL_ID");
    }


    private void setCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        String currentDate = formatter.format(date);
        binding.testDate.setText(currentDate);
    }


    private void showDatePickerDialog() {
        Calendar now = Calendar.getInstance();
        com.wdullaer.materialdatetimepicker.date.DatePickerDialog dialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                (com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener) this,
                now.get(Calendar.YEAR), // Initial year selection
                now.get(Calendar.MONTH), // Initial month selection
                now.get(Calendar.DAY_OF_MONTH) // Initial day selection
        );
        dialog.show(getActivity().getSupportFragmentManager(), "Datepickerdialog");
    }


    private void getPageDataFromServer() {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check your Internet Connection");
            return;
        }
        qc_qaViewModel.getAddQcPageData(getActivity(), selectedSlID).observe(getViewLifecycleOwner(), response -> {
            if (response == null) {
                Log.d("ERROR", "ERROR");
                return;
            }
            if (response.getStatus() == 400) {
                infoMessage(getActivity().getApplication(), "" + response.getMessage());
                return;
            }
            setNeededDataToView(response);
        });
    }

    private void setNeededDataToView(AddQcQaPageResponse response) {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }
        qc_qaViewModel.getQCQaPageData(getActivity(), selectedSlID)
                .observe(getViewLifecycleOwner(), previousQcQaResponse -> {
                    if (previousQcQaResponse == null) {
                        errorMessage(getActivity().getApplication(), "Something Wrong");
                        return;
                    }
                    if (previousQcQaResponse.getStatus() == 400) {
                        infoMessage(getActivity().getApplication(), "" + previousQcQaResponse.getMessage());
                        return;
                    }

                    try {
                        getEditQcQaResponse = previousQcQaResponse;

                        if (selectedStoreId == null) {
                            selectedStoreId = getEditQcQaResponse.getQcInfo().getStoreID();

                        }
                        /**
                         * now set data to view
                         */

                        /**
                         * First set enterPrise
                         */
                        enterPriceList = new ArrayList<>();
                        enterPriceList.clear();
                        enterPriceNameList = new ArrayList<>();
                        enterPriceNameList.clear();
                        enterPriceList.addAll(response.getEnterprizeList());
                        for (int i = 0; i < response.getEnterprizeList().size(); i++) {
                            enterPriceNameList.add("" + response.getEnterprizeList().get(i).getStoreName());
                        }
                        binding.enterPrise.setItem(enterPriceNameList);

                        /**
                         * now set Previous selected enterprise
                         */
                        for (int i = 0; i < enterPriceList.size(); i++) {
                            if (selectedStoreId != null) {
                                if (enterPriceList.get(i).getStoreID().equals(selectedStoreId)) {
                                    binding.enterPrise.setSelected(true);
                                    binding.enterPrise.setSelection(i);
                                    binding.enterPrise.setEnableErrorLabel(false);
                                    break;
                                }
                            }

                        }


                        testNameList = new ArrayList<>();
                        testNameList.clear();
                        testNameNameList = new ArrayList<>();
                        testNameNameList.clear();
                        testNameList.addAll(response.getTestList());


                        selectedTestNameList = new String[getEditQcQaResponse.getQcDetails().size()];
                        for (int i = 0; i < response.getTestList().size(); i++) {
                            testNameNameList.add("" + response.getTestList().get(i).getTestName());
                        }

                        for (int i = 0; i < getEditQcQaResponse.getQcDetails().size(); i++) {
                            selectedTestNameList[i] = getEditQcQaResponse.getQcDetails().get(i).getTestID();
                            linkedList.add(getEditQcQaResponse.getQcDetails().get(i).getTestID());
                        }

                        binding.testDate.setText(previousQcQaResponse.getQcInfo().getTestDate());
                        binding.sampleName.setText(String.valueOf(previousQcQaResponse.getQcInfo().getModel()));
                        binding.note.setText(String.valueOf(previousQcQaResponse.getQcInfo().getNote()));


                        EditQcQaAdapter adapter = new EditQcQaAdapter(getActivity(), testNameNameList, previousQcQaResponse, testNameList, previousQcQaResponse.getQcDetails(), Edit_QCQAFragment.this);

                        binding.testParamRv.setLayoutManager(new LinearLayoutManager(getContext()));
                        binding.testParamRv.setAdapter(adapter);

                    } catch (Exception e) {
                        Log.d("Error", e.getMessage());
                    }
                });
    }


    private void validationAndSave() {
        List<String> valueList = new ArrayList<>();
        valueList.clear();
        for (int i = 0; i < getEditQcQaResponse.getQcDetails().size(); i++) {
            try {
                String currentValue = ((EditText) binding.testParamRv.getLayoutManager()
                        .findViewByPosition(i).findViewById(R.id.value)).getText().toString();
                valueList.add(currentValue);

                // for test name validation
                if (((SmartMaterialSpinner) binding.testParamRv.getLayoutManager().findViewByPosition(i).findViewById(R.id.testName)).isSelected()) {
                    selectedTestIds.add(EditQcQaAdapter.testNameList.get(i).getTestID());
                }

            } catch (Exception e) {
                Log.d("ERROR", "" + e.getMessage());
            }
        }


        List<String> slIdList = new ArrayList<>();
        List<String> refQcSlID = new ArrayList<>();

        for (int i = 0; i < getEditQcQaResponse.getQcDetails().size(); i++) {
            slIdList.add(getEditQcQaResponse.getQcDetails().get(i).getSlID());
            refQcSlID.add(getEditQcQaResponse.getQcDetails().get(i).getRefqcSlID());
        }


        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        qc_qaViewModel.updateQcQaInformation(
                getActivity(), linkedList,
                valueList, binding.sampleName.getText().toString(),
                selectedSlID, getEditQcQaResponse.getQcInfo().getStatus(),
                selectedQcId, binding.testDate.getText().toString(), binding.note.getText().toString(),
                refQcSlID,
                slIdList,
                Collections.singletonList(selectedSlID), selectedStoreId
        ).observe(getViewLifecycleOwner(), response -> {
            progressDialog.dismiss();
            Log.d("RESPONSE", String.valueOf(response));
            if (response == null) {
                errorMessage(getActivity().getApplication(), "Something Wrong");
                return;
            }
            if (response.getStatus() == 400) {
                infoMessage(getActivity().getApplication(), "" + response.getMessage());
                return;
            }
            successMessage(getActivity().getApplication(), "" + response.getMessage());
            getActivity().onBackPressed();
        });


    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        int month = monthOfYear;
        if (month == 12) {
            month = 1;
        } else {
            month = monthOfYear + 1;
        }

        String mainMonth, mainDay;

        if (month <= 9) {
            mainMonth = "0" + month;
        } else {
            mainMonth = String.valueOf(month);
        }
        if (dayOfMonth <= 9) {
            mainDay = "0" + dayOfMonth;
        } else {
            mainDay = String.valueOf(dayOfMonth);
        }
        String selectedDate = year + "-" + mainMonth + "-" + mainDay;//set the selected date

        binding.testDate.setText(selectedDate);
    }


    @Override
    public void select(int position, String selectedTestNameId) {
        try {
            //than replace id by  position
            linkedList.set(position, selectedTestNameId);

            loading += 1;
            if (loading >= this.getEditQcQaResponse.getTestList().size() + 1) {
                selectedTestNameList[position] = selectedTestNameId;
                for (int i = 0; i < selectedTestNameList.length; i++) {

                    Log.d("OKKK", "" + selectedTestNameList[i]);
                }
            }

        } catch (Exception e) {
            Log.d("ERROR", "" + e.getMessage());
        }
    }


    public void updateQc_qaDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        @SuppressLint("InflateParams")
        View view = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.purchase_dialog, null);
        //Set the view
        builder.setView(view);
        TextView tvTitle, tvMessage;
        ImageView imageIcon = view.findViewById(R.id.img_icon);
        tvMessage = view.findViewById(R.id.tv_message);
        tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("Do you want to Update ?");//set warning title
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


}