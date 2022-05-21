package com.rupayan_housing.view.fragment.miller.addNewMiller;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.FragmentMillerEmployeeInformationBinding;
import com.rupayan_housing.serverResponseModel.DuePaymentResponse;
import com.rupayan_housing.utils.UrlUtil;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.view.fragment.HomeFragment;
import com.rupayan_housing.viewModel.MillerProfileInfoViewModel;

public class MillerEmployeeInformation extends BaseFragment {
    private FragmentMillerEmployeeInformationBinding binding;
    private MillerProfileInfoViewModel millerProfileInfoViewModel;
    private ViewPager viewPager;
    private boolean firstCondition = false;

    private boolean sndCondition = false;
    Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_miller_employee_information, container, false);
        viewPager = getActivity().findViewById(R.id.viewPager);
        millerProfileInfoViewModel = new ViewModelProvider(this).get(MillerProfileInfoViewModel.class);
        bundle = new Bundle();
        binding.setClickHandle(() -> validationAndSave());
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


        binding.termsTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    bundle.putString("pageName", "Terms");
                    bundle.putString("url", UrlUtil.terms);
                    Navigation.findNavController(getView()).navigate(R.id.action_millerEmployeeInformation_to_edit_appWebViewFragmnet, bundle);
                } catch (Exception e) {
                }
            }
        });

        binding.privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    bundle.putString("pageName", "Privacy");
                    bundle.putString("url", UrlUtil.privacyPolicy);
                    Navigation.findNavController(getView()).navigate(R.id.action_millerEmployeeInformation_to_edit_appWebViewFragmnet, bundle);
                } catch (Exception e) {
                }
            }
        });

        return binding.getRoot();
    }

    private void totalMaleEmployee() {
        int totalEmployee = 0;
        String fullTimeMale = binding.fullTimeMale.getText().toString();
        String partTimeMale = binding.partTimeMale.getText().toString();
        String techToalMale = binding.techToalMale.getText().toString();
        if (fullTimeMale.isEmpty()) {
            fullTimeMale = "0";
        }
        if (partTimeMale.isEmpty()) {
            partTimeMale = "0";
        }
        if (techToalMale.isEmpty()) {
            techToalMale = "0";
        }

        try {
            totalEmployee = Integer.parseInt(fullTimeMale)
                    + Integer.parseInt(partTimeMale)
                    + Integer.parseInt(techToalMale);

            binding.toalEmployeMale.setText(String.valueOf(totalEmployee));
        } catch (Exception e) {
            Log.d("ERROR", "" + e.getLocalizedMessage());
        }

    }

    private void totalFeMaleEmployee() {
        int totalEmployee = 0;
        String fullTimeFemale = binding.fullTimeFemale.getText().toString();
        String partTimeFemale = binding.partTimeFemale.getText().toString();
        String techTotalFemale = binding.techTotalFemale.getText().toString();
        if (fullTimeFemale.isEmpty()) {
            fullTimeFemale = "0";
        }
        if (partTimeFemale.isEmpty()) {
            partTimeFemale = "0";
        }
        if (techTotalFemale.isEmpty()) {
            techTotalFemale = "0";
        }

        try {
            totalEmployee = Integer.parseInt(fullTimeFemale)
                    + Integer.parseInt(partTimeFemale)
                    + Integer.parseInt(techTotalFemale);

            binding.totalEmployeeFemale.setText(String.valueOf(totalEmployee));
        } catch (Exception e) {
            Log.d("ERROR", "" + e.getLocalizedMessage());
        }

    }

    private void validationAndSave() {

      /*  if (binding.fullTimeMale.getText().toString().isEmpty()) {
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

        if (!HomeFragment.byTaping) {
            if (!firstCondition) {
                binding.firstCondition.setError("Please check this box,if you want to process");
                return;
            }
        }

        if (!sndCondition) {
            binding.sndCondition.setError("Please check this box,if you want to process");
            return;
        }


        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        millerProfileInfoViewModel.submitEmployeeInfo(
                getActivity(), MillerProfileInformation.profileIdFromServer, binding.fullTimeMale.getText().toString(),
                binding.fullTimeFemale.getText().toString(), binding.partTimeMale.getText().toString(), binding.partTimeFemale.getText().toString()
                , binding.techToalMale.getText().toString(), binding.techTotalFemale.getText().toString(), null
        ).observe(getViewLifecycleOwner(), response -> {
            progressDialog.dismiss();
            if (response == null) {
                errorMessage(getActivity().getApplication(), "ERRROR");
                return;
            }
            if (response.getStatus() != 200) {
                errorMessage(getActivity().getApplication(), "ERRROR");
                return;
            }
            successMessage(getActivity().getApplication(), "" + response.getMessage());
            HomeFragment.byTaping = false;
            getActivity().onBackPressed();
        });


    }

    @Override
    public void onStart() {
        super.onStart();

        if (HomeFragment.byTaping) {
            binding.firstCondition.setVisibility(View.GONE);
        }
    }
}