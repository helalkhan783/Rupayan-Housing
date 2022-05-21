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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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
import android.widget.Toast;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.rupayan_housing.R;
import com.rupayan_housing.adapter.NewQcQaAdapter;
import com.rupayan_housing.clickHandle.AddQc_QaClickHandle;
import com.rupayan_housing.clickHandle.ToolbarClickHandle;
import com.rupayan_housing.databinding.FragmentAddQcQaBinding;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.AddQcQaPageResponse;
import com.rupayan_housing.serverResponseModel.DuePaymentResponse;
import com.rupayan_housing.serverResponseModel.EnterprizeList;
import com.rupayan_housing.serverResponseModel.TestList;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.viewModel.Qc_QaViewModel;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class AddQc_Qa extends BaseFragment implements DatePickerDialog.OnDateSetListener, GetAllQcQaDataFromAdapter {
    private FragmentAddQcQaBinding binding;
    private Qc_QaViewModel qc_qaViewModel;
    /**
     * for enterPrise
     */
    private List<EnterprizeList> enterPriceList;
    private List<String> enterPriceNameList;

    /**
     * for testName List
     */
    private List<TestList> testNameList;
    private List<String> testNameNameList;


    private String selectedEnterPrise;

    Set<String> selectedTestName = new HashSet<>();
    LinkedList<String> valueList = new LinkedList<>();

    public static int size = 1;
    public static List<String> sizeList;
    NewQcQaAdapter adapter;
    public static List<TestList> testLists;
    public static AddQcQaPageResponse response1;
    public static boolean manageAdapter = false;
    private int adapterPositionUpdate = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_qc__qa, container, false);
        qc_qaViewModel = new ViewModelProvider(this).get(Qc_QaViewModel.class);
        binding.toolbar.toolbarTitle.setText("Add New QC/QA");
        setCurrentDate();
        sizeList = new ArrayList<>();
        sizeList.add("");
        /**
         * now get page Data From server
         */
        getPageDataFromServer();
        binding.toolbar.setClickHandle(() -> {
            sizeList.clear();
            hideKeyboard(getActivity());
            getActivity().onBackPressed();
        });

        binding.toolbar.addBtn.setVisibility(View.VISIBLE);
        binding.toolbar.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sizeList.add("");
                adapterPositionUpdate += 1;
                manageAdapter = true;
                adapter.notifyItemRangeInserted(adapterPositionUpdate, sizeList.size());
                adapter.notifyItemInserted(adapterPositionUpdate);
            }
        });
        /**
         * For submit Add new qc qa data
         */
        binding.setClickHandle(() -> {
            if (selectedEnterPrise == null) {
                infoMessage(getActivity().getApplication(), "Please select enterprise");
                return;
            }
            if (binding.sampleName.getText().toString().isEmpty()) {
                binding.sampleName.setError("Empty");
                binding.sampleName.requestFocus();
                return;
            }


            hideKeyboard(getActivity());

            // for recycler item validation
            for (int i = 0; i < sizeList.size(); i++) {
                try {  //for value validation
                    String value1 = ((EditText) binding.qcqaRV.getLayoutManager().findViewByPosition(i).findViewById(R.id.value)).getText().toString();
                    if (value1 == null || value1.isEmpty()) {
                        ((EditText) binding.qcqaRV.getLayoutManager().findViewByPosition(i).findViewById(R.id.value)).setError("Empty field");
                        ((EditText) binding.qcqaRV.getLayoutManager().findViewByPosition(i).findViewById(R.id.value)).requestFocus();
                        return;
                    }

                    // for test name validation
                    if (((SmartMaterialSpinner) binding.qcqaRV.getLayoutManager().findViewByPosition(i).findViewById(R.id.testName)).isSelected()) {
                       //  selectedTestName.add(NewQcQaAdapter.testLists.get(i).getTestID());
                    }

//                    if (((SmartMaterialSpinner) binding.qcqaRV.getLayoutManager().findViewByPosition(i).findViewById(R.id.testName)).getSelectedItemPosition() == i){
//                        selectedTestName.add(NewQcQaAdapter.testLists.get(i).getTestID());
//                    }



                    valueList.add("" + value1);
                } catch (Exception e) {
                }

            }

            addDialog();

        });
        binding.testDate.setOnClickListener(v -> showDatePickerDialog());

        binding.enterPrise.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedEnterPrise = enterPriceList.get(position).getStoreID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.testName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                testNameList.get(position).getTestID();
                /**
                 * now set others field value based on this selected testName
                 */
                binding.shortName.setText(testNameList.get(position).getShortName());
                binding.reference.setText(testNameList.get(position).getReference());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return binding.getRoot();
    }




    private void addDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        @SuppressLint("InflateParams")
        View view = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.purchase_dialog, null);
        //Set the view
        builder.setView(view);
        TextView tvTitle, tvMessage;
        ImageView imageIcon = view.findViewById(R.id.img_icon);
        tvMessage = view.findViewById(R.id.tv_message);
        tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("Do you want to add ?");//set warning title
        tvMessage.setText("MIS ERP");
        imageIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.app_logo));//set warning image
        Button bOk = view.findViewById(R.id.btn_ok);
        Button cancel = view.findViewById(R.id.cancel);
        android.app.AlertDialog alertDialog = builder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        cancel.setOnClickListener(v -> alertDialog.dismiss());//for cancel
        bOk.setOnClickListener(v -> {
            alertDialog.dismiss();
            addNewQc_qaDialog();
        });

        alertDialog.show();
    }

    private void showDatePickerDialog() {
        Calendar now = Calendar.getInstance();
        com.wdullaer.materialdatetimepicker.date.DatePickerDialog dialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR), // Initial year selection
                now.get(Calendar.MONTH), // Initial month selection
                now.get(Calendar.DAY_OF_MONTH) // Initial day selection
        );
        dialog.show(getActivity().getSupportFragmentManager(), "Datepickerdialog");
    }


    private void getPageDataFromServer() {
        qc_qaViewModel.getAddQcPageData(getActivity(), "0").observe(getViewLifecycleOwner(), response -> {
            if (response == null) {
                Log.d("ERROR", "ERROR");
                return;
            }
            if (response == null) {
                errorMessage(getActivity().getApplication(), "Something Wrong");
                return;
            }
            if (response.getStatus() == 400) {
                infoMessage(getActivity().getApplication(), "" + response.getMessage());
                return;
            }
            setNeededDataToView(response);
            testLists = new ArrayList<>();
            testLists.addAll(response.getTestList());
            response1 = response;
            addToRv();

        });
    }

    private void addToRv() {
        try {

            adapter = new NewQcQaAdapter(getActivity(), null, null, sizeList, this);
            binding.qcqaRV.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.qcqaRV.setAdapter(adapter);
            // binding.qcqaRV.getAdapter().notifyItemInserted(adapterPositionUpdate);
        } catch (Exception e) {
        }
    }

    private void setNeededDataToView(AddQcQaPageResponse response) {
        /**
         * set enterPrise
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
         * set Test Name
         */
        testNameList = new ArrayList<>();
        testNameList.clear();
        testNameNameList = new ArrayList<>();
        testNameNameList.clear();
        testNameList.addAll(response.getTestList());
        for (int i = 0; i < response.getTestList().size(); i++) {
            testNameNameList.add("" + response.getTestList().get(i).getTestName());
        }
        binding.testName.setItem(testNameNameList);

    }

    private void setCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        //System.out.println(formatter.format(date));
        String currentDate = formatter.format(date);
        binding.testDate.setText(currentDate);
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

    public void addNewQc_qaDialog() {
        String profileTypeId = PreferenceManager.getInstance(getContext()).getUserCredentials().getProfileTypeId();
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        qc_qaViewModel.addQc_qa(getActivity(), binding.testDate.getText().toString(), selectedTestName, valueList,
                binding.sampleName.getText().toString(), profileTypeId, binding.note.getText().toString(), selectedEnterPrise)
                .observe(getViewLifecycleOwner(), response -> {
                    progressDialog.dismiss();
                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "Something Wrong");
                        return;
                    }
                    if (response.getStatus() == 400) {
                        infoMessage(getActivity().getApplication(), "" + response.getMessage());
                        return;
                    }
                    /**
                     * now successfully added qc/qa data
                     */
                    successMessage(getActivity().getApplication(), "" + response.getMessage());
                    getActivity().onBackPressed();
                });
    }

    @Override
    public void getAllData(int position, String testId, String shortName, String reference, String value) {
        selectedTestName.add(testId);
    }

    @Override
    public void deleteItem(int position) {
        try {
            adapter.notifyItemRemoved(position);
            sizeList.remove(position);
            adapter.notifyItemChanged(position);
         } catch (Exception e) {
        }
    }
}