package com.rupayan_housing.view.fragment.miller.addNewMiller;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.FragmentMillerQcInformationBinding;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.viewModel.MillerProfileInfoViewModel;

public class MillerQcInformation extends BaseFragment {
    private FragmentMillerQcInformationBinding binding;
    private MillerProfileInfoViewModel millerProfileInfoViewModel;

    private ViewPager viewPager;
    private String selectedHaveALaboratory = null;
    private String selectedTestKitRadioGroup = null;
    private String selectedlaboratoryPersonRadioGroup = null;
    private String selectedprocedureRadioGroup = null;

    String selectedHaveALaboratory1,selectedprocedureRadioGroup1,selectedlaboratoryPersonRadioGroup1,selectedTestKitRadioGroup1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_miller_qc_information, container, false);
        viewPager = getActivity().findViewById(R.id.viewPager);
        millerProfileInfoViewModel = new ViewModelProvider(this).get(MillerProfileInfoViewModel.class);

        /**
         * For have Laboratory
         */
        binding.haveLaboratoryRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            int radioBtnID = group.getCheckedRadioButtonId();

            View radioB = group.findViewById(radioBtnID);
            int position = group.indexOfChild(radioB);
            selectedHaveALaboratory = String.valueOf(position);
        });
        binding.testKitRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            int radioBtnID = group.getCheckedRadioButtonId();
            View radioB = group.findViewById(radioBtnID);
            int position = group.indexOfChild(radioB);
            selectedTestKitRadioGroup = String.valueOf(position);
        });


        binding.laboratoryPersonRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            int radioBtnID = group.getCheckedRadioButtonId();

            View radioB = group.findViewById(radioBtnID);
            int position = group.indexOfChild(radioB);
            selectedlaboratoryPersonRadioGroup = String.valueOf(position);
        });

        binding.procedureRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            int radioBtnID = group.getCheckedRadioButtonId();

            View radioB = group.findViewById(radioBtnID);
            int position = group.indexOfChild(radioB);
            selectedprocedureRadioGroup = String.valueOf(position);
        });
        /**
         * For submit QC information to server
         */

        binding.setClickHandle(() -> {
            ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.show();
            if (selectedHaveALaboratory.equals("0") ){
              selectedHaveALaboratory1="1";
            }
            if (selectedHaveALaboratory.equals("1") ){
                selectedHaveALaboratory1="0";
            }

            if ( selectedprocedureRadioGroup.equals("0")){
                selectedprocedureRadioGroup1="1";
            }
            if ( selectedprocedureRadioGroup.equals("1")){
                selectedprocedureRadioGroup1="0";
            }


            if ( selectedlaboratoryPersonRadioGroup.equals("0")){
                selectedlaboratoryPersonRadioGroup1="1";
            }
            if ( selectedlaboratoryPersonRadioGroup.equals("1")){
                selectedlaboratoryPersonRadioGroup1="0";
            }


             if ( selectedTestKitRadioGroup.equals("0")){
                 selectedTestKitRadioGroup1="1";
            }
             if ( selectedTestKitRadioGroup.equals("1")){
                 selectedTestKitRadioGroup1="0";
            }
            if (!(isInternetOn(getActivity()))) {
                infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
                return;
            }
            millerProfileInfoViewModel.submitQcInformation(
                    getActivity(), MillerProfileInformation.profileIdFromServer, "1",
                    selectedHaveALaboratory1, selectedprocedureRadioGroup1, selectedlaboratoryPersonRadioGroup1,
                    selectedTestKitRadioGroup1, binding.number.getText().toString(), binding.remarks.getText().toString())
                    .observe(getViewLifecycleOwner(), response -> {
                        progressDialog.dismiss();
                        if (response == null) {
                            errorMessage(getActivity().getApplication(), "ERRROR");
                            return;
                        }
                        if (response.getStatus() != 200) {
                            errorMessage(getActivity().getApplication(), "ERRROR");
                            return;
                        }

                        infoMessage(getActivity().getApplication(), "" + response.getMessage());
                        viewPager.setCurrentItem(4);
                    });
        });
        return binding.getRoot();
    }
}