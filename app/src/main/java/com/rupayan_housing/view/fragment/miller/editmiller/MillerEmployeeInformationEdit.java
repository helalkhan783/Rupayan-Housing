package com.rupayan_housing.view.fragment.miller.editmiller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rupayan_housing.R;
import com.rupayan_housing.adapter.MillerPendingListAdapter;
import com.rupayan_housing.clickHandle.MillerEmployeeInformationEditClickHandle;
import com.rupayan_housing.databinding.FragmentMillerEmployeeInformationEditBinding;
import com.rupayan_housing.serverResponseModel.DuePaymentResponse;
import com.rupayan_housing.serverResponseModel.GetPreviousMillerInfoResponse;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.view.fragment.miller.MillerDetailsViewFragment;
import com.rupayan_housing.viewModel.MillerProfileInfoViewModel;
import com.rupayan_housing.viewModel.UpdateMillerViewModel;

import java.util.Objects;


public class MillerEmployeeInformationEdit extends BaseFragment {
    private FragmentMillerEmployeeInformationEditBinding binding;
    private UpdateMillerViewModel updateMillerViewModel;

    private MillerProfileInfoViewModel millerProfileInfoViewModel;
    private ViewPager viewPager;

    private String sid;
    private boolean firstCondition = false;

    private boolean sndCondition = false;


    private GetPreviousMillerInfoResponse getPreviousMillerInfoResponse;


    public MillerEmployeeInformationEdit(String slId) {
        this.sid = slId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_miller_employee_information_edit, container, false);
        viewPager = getActivity().findViewById(R.id.viewPager);
        millerProfileInfoViewModel = new ViewModelProvider(this).get(MillerProfileInfoViewModel.class);
        updateMillerViewModel = new ViewModelProvider(this).get(UpdateMillerViewModel.class);
        binding.setClickHandle(() -> {
            /***
             *  submit info here
             */

           /* if (binding.fullTimeMale.getText().toString().isEmpty()) {
                binding.fullTimeMale.setError("Empty Field");
                binding.fullTimeMale.requestFocus();
                return;
            }
            if (binding.fullTimeFemale.getText().toString().isEmpty()) {
                binding.fullTimeFemale.setError("Empty Field");
                binding.fullTimeFemale.requestFocus();
                return;
            }


            if (binding.partTimeMale.getText().toString().isEmpty()) {
                binding.partTimeMale.setError("Empty Field");
                binding.partTimeMale.requestFocus();
                return;
            }
            if (binding.partTimeFemale.getText().toString().isEmpty()) {
                binding.partTimeFemale.setError("Empty Field");
                binding.partTimeFemale.requestFocus();
                return;
            }


            if (binding.techTotalFemale.getText().toString().isEmpty()) {
                binding.techTotalFemale.setError("Empty Field");
                binding.techTotalFemale.requestFocus();
                return;
            }
            if (binding.techToalMale.getText().toString().isEmpty()) {
                binding.techToalMale.setError("Empty Field");
                binding.techToalMale.requestFocus();
                return;
            }


            if (binding.toalEmployeMale.getText().toString().isEmpty()) {
                binding.toalEmployeMale.setError("Empty Field");
                binding.toalEmployeMale.requestFocus();
                return;
            }
            if (binding.totalEmployeeFemale.getText().toString().isEmpty()) {
                binding.totalEmployeeFemale.setError("Empty Field");
                binding.totalEmployeeFemale.requestFocus();
                return;
            }*/
           /* if (!firstCondition) {
                binding.firstCondition.setError("Please check this box,if you want to process");
                return;
            }
            if (!sndCondition) {
                binding.sndCondition.setError("Please check this box,if you want to process");
                return;
            }
*/

            dialog();

        });
        /**
         * now set previous selected data
         */
        setPreviousSelectedData();

        binding.fullTimeMale.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                totalMaleEmployee();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.partTimeMale.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                totalMaleEmployee();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        binding.techToalMale.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                totalMaleEmployee();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        /**
         * for female
         */

        binding.fullTimeFemale.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                totalFeMaleEmployee();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.partTimeFemale.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                totalFeMaleEmployee();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        binding.techTotalFemale.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                totalFeMaleEmployee();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.firstCondition.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (binding.firstCondition.isChecked()) {
                firstCondition = true;
                return;
            }
            firstCondition = false;
        });
        binding.sndCondition.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (binding.sndCondition.isChecked()) {
                sndCondition = true;
                return;
            }
            sndCondition = false;
        });


        return binding.getRoot();
    }

    private void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        @SuppressLint("InflateParams")
        View view = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.purchase_dialog, null);
        //Set the view
        builder.setView(view);
        TextView tvTitle, tvMessage;
        ImageView imageIcon = view.findViewById(R.id.img_icon);
        tvMessage = view.findViewById(R.id.tv_message);
        tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("Do You Want to Update it ?");//set warning title
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

    private void setPreviousSelectedData() {
        updateMillerViewModel.getPreviousMillerInfoBySid(getActivity(), sid)
                .observe(getViewLifecycleOwner(), response -> {
                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "Something Wrong");
                        return;
                    }
                    if (response.getStatus() == 400) {
                        infoMessage(getActivity().getApplication(), "" + response.getMessage());
                        return;
                    }

                    getPreviousMillerInfoResponse = response;



                    binding.fullTimeMale.setText(response.getEmployeeInfo().getFullTimeMale());
                    binding.fullTimeFemale.setText(response.getEmployeeInfo().getFullTimeFemale());
                    binding.partTimeMale.setText(response.getEmployeeInfo().getPartTimeMale());
                    binding.partTimeFemale.setText(response.getEmployeeInfo().getPartTimeFemail());
                    binding.techToalMale.setText(response.getEmployeeInfo().getTotalTechMale());
                    binding.techTotalFemale.setText(response.getEmployeeInfo().getTotalTechFemale());

                });
    }


    private void totalMaleEmployee() {
        double totalEmployee = 0.0;
        String fullTimeMale = binding.fullTimeMale.getText().toString();
        String partTimeMale = binding.partTimeMale.getText().toString();
        String techToalMale = binding.techToalMale.getText().toString();
        if (fullTimeMale.isEmpty()) {
            fullTimeMale = "0.0";
        }
        if (partTimeMale.isEmpty()) {
            partTimeMale = "0.0";
        }
        if (techToalMale.isEmpty()) {
            techToalMale = "0.0";
        }

        try {
            totalEmployee = Double.parseDouble(fullTimeMale)
                    + Double.parseDouble(partTimeMale)
                    + Double.parseDouble(techToalMale);

            binding.toalEmployeMale.setText(String.valueOf(totalEmployee));
        } catch (Exception e) {
            Log.d("ERROR", "" + e.getLocalizedMessage());
        }

    }

    private void totalFeMaleEmployee() {
        double totalEmployee = 0.0;


        String fullTimeFemale = binding.fullTimeFemale.getText().toString();
        String partTimeFemale = binding.partTimeFemale.getText().toString();
        String techTotalFemale = binding.techTotalFemale.getText().toString();
        if (fullTimeFemale.isEmpty()) {
            fullTimeFemale = "0.0";
        }
        if (partTimeFemale.isEmpty()) {
            partTimeFemale = "0.0";
        }
        if (techTotalFemale.isEmpty()) {
            techTotalFemale = "0.0";
        }

        try {
            totalEmployee = Double.parseDouble(fullTimeFemale)
                    + Double.parseDouble(partTimeFemale)
                    + Double.parseDouble(techTotalFemale);

            binding.totalEmployeeFemale.setText(String.valueOf(totalEmployee));
        } catch (Exception e) {
            Log.d("ERROR", "" + e.getLocalizedMessage());
        }

    }

    private void validationAndSubmit() {
        if (!isInternetOn(getActivity())) {
            infoMessage(getActivity().getApplication(), "Please check your internet connection");
            return;
        }
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        String profileId =MillerPendingListAdapter.profileId;
        if (profileId == null){
            profileId =   MillerDetailsViewFragment.profileId;
        }
        updateMillerViewModel.updateEmployeeInfo(
                getActivity(), getPreviousMillerInfoResponse.getQcInfo().getStatus(),
                binding.techTotalFemale.getText().toString(),
                binding.techToalMale.getText().toString(),
                binding.partTimeFemale.getText().toString(),
                binding.partTimeMale.getText().toString(),
                binding.fullTimeFemale.getText().toString(),
                binding.fullTimeMale.getText().toString(),
                getPreviousMillerInfoResponse.getQcInfo().getSlId(), profileId
        ).observe(getViewLifecycleOwner(), response -> {
            progressDialog.dismiss();
            Log.d("RESPONSE", "" + String.valueOf(response));
            if (response == null) {
                errorMessage(getActivity().getApplication(), "Something Wrong Here");
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
}