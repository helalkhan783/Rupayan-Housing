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
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.FragmentMillerQcInformationEditBinding;
import com.rupayan_housing.serverResponseModel.GetPreviousMillerInfoResponse;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.viewModel.UpdateMillerViewModel;

import java.util.Objects;


public class MillerQcInformationEdit extends BaseFragment {
    private FragmentMillerQcInformationEditBinding binding;
    private UpdateMillerViewModel updateMillerViewModel;


    private ViewPager viewPager;
    private String selectedHaveALaboratory;
    private String selectedTestKitRadioGroup;
    private String selectedlaboratoryPersonRadioGroup;
    private String selectedprocedureRadioGroup;


    private String firstHaveALaboratory, firstTestKitRadioGrou, firstaboratoryPersonRadioGroup, firstprocedureRadioGroup;
    private String sid;

    private GetPreviousMillerInfoResponse getPreviousMillerInfoResponse;


    public MillerQcInformationEdit(String slId) {
        this.sid = slId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_miller_qc_information_edit, container, false);
        updateMillerViewModel = new ViewModelProvider(this).get(UpdateMillerViewModel.class);
        viewPager = getActivity().findViewById(R.id.viewPager);

        /**
         * For have Laboratory
         */

        onCliK();
        allRadioBtn();
        /**
         * get Previous selected data and set to ui
         */
        setPreviousDataToView();

        /**
         * Now update the profile information
         */

        binding.setClickHandle(() -> dialog());


        return binding.getRoot();
    }

    private void allRadioBtn() {
        binding.haveLaboratoryRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            int radioBtnID = group.getCheckedRadioButtonId();
            View radioB = group.findViewById(radioBtnID);
            int position = group.indexOfChild(radioB);
            firstHaveALaboratory = String.valueOf(position);


        });
        binding.testKitRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            int radioBtnID = group.getCheckedRadioButtonId();

            View radioB = group.findViewById(radioBtnID);
            int position = group.indexOfChild(radioB);
            firstTestKitRadioGrou = String.valueOf(position);
        });


        binding.laboratoryPersonRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            int radioBtnID = group.getCheckedRadioButtonId();

            View radioB = group.findViewById(radioBtnID);
            int position = group.indexOfChild(radioB);
            firstaboratoryPersonRadioGroup = String.valueOf(position);
        });


        binding.procedureRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            int radioBtnID = group.getCheckedRadioButtonId();

            View radioB = group.findViewById(radioBtnID);
            int position = group.indexOfChild(radioB);
            firstprocedureRadioGroup = String.valueOf(position);


        });

    }


    private void setPreviousDataToView() {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check your Internet Connection");
            return;
        }
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


                    selectedprocedureRadioGroup = response.getQcInfo().getStandardProcedure();
                    selectedlaboratoryPersonRadioGroup = response.getQcInfo().getTrainedLaboratoryPerson();
                    selectedTestKitRadioGroup = response.getQcInfo().getUseTestKit();
                    selectedHaveALaboratory = response.getQcInfo().getHaveLaboratory();

                    /**
                     * set standard procedure
                     */
                    try {
                        if (selectedprocedureRadioGroup.equals("1")) {
                            ((RadioButton) binding.procedureRadioGroup.getChildAt(0)).setChecked(true);
                        }
                        if (selectedprocedureRadioGroup.equals("0")) {
                            ((RadioButton) binding.procedureRadioGroup.getChildAt(1)).setChecked(true);
                        }
                        /**
                         *  handle trainedLaboratoryPerson
                         */
                        if (selectedlaboratoryPersonRadioGroup.equals("1")) {
                            ((RadioButton) binding.laboratoryPersonRadioGroup.getChildAt(0)).setChecked(true);
                        }
                        if (selectedlaboratoryPersonRadioGroup.equals("0")) {
                            ((RadioButton) binding.laboratoryPersonRadioGroup.getChildAt(1)).setChecked(true);
                        }
                        /**
                         * For use test kit
                         */
                        if (selectedTestKitRadioGroup.equals("1")) {
                            ((RadioButton) binding.testKitRadioGroup.getChildAt(0)).setChecked(true);
                        }
                        if (selectedTestKitRadioGroup.equals("0")) {
                            ((RadioButton) binding.testKitRadioGroup.getChildAt(1)).setChecked(true);
                        }
                        /**
                         * For handle  have Laboratory
                         */
                        if (selectedHaveALaboratory.equals("1")) {
                            ((RadioButton) binding.haveLaboratoryRadioGroup.getChildAt(0)).setChecked(true);
                        }
                        if (selectedHaveALaboratory.equals("0")) {
                            ((RadioButton) binding.haveLaboratoryRadioGroup.getChildAt(1)).setChecked(true);
                        }

                        binding.number.setText(response.getQcInfo().getLaboratoryPerson());
                        binding.remarks.setText(response.getQcInfo().getLabRemarks());
                    } catch (Exception e) {
                        Log.d("ERROR", "" + e.getLocalizedMessage());
                    }
                });
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
            validationAndSave();
        });
        alertDialog.show();

    }

    private void validationAndSave() {
        if (!isInternetOn(getActivity())) {
            infoMessage(getActivity().getApplication(), "Please check your internet connection");
            return;
        }
        if (firstTestKitRadioGrou != null) {
            if (firstTestKitRadioGrou.equals("1")) {
                selectedTestKitRadioGroup = "0";
            }
            if (firstTestKitRadioGrou.equals("0")) {
                selectedTestKitRadioGroup = "1";
            }
        }

        if (firstHaveALaboratory != null) {
            if (firstHaveALaboratory.equals("1")) {
                selectedHaveALaboratory = "0";
            }
            if (firstHaveALaboratory.equals("0")) {
                selectedHaveALaboratory = "1";
            }

        }
        if (firstaboratoryPersonRadioGroup != null) {
            if (firstaboratoryPersonRadioGroup.equals("1")) {
                selectedlaboratoryPersonRadioGroup = "0";
            }
            if (firstaboratoryPersonRadioGroup.equals("0")) {
                selectedlaboratoryPersonRadioGroup = "1";
            }
        }
        if (firstprocedureRadioGroup != null) {
            if (firstprocedureRadioGroup.equals("1")) {
                selectedprocedureRadioGroup = "0";
            }
            if (firstprocedureRadioGroup.equals("0")) {
                selectedprocedureRadioGroup = "1";
            }
        }


        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        updateMillerViewModel.updateQcInformation(
                getActivity(), getPreviousMillerInfoResponse.getQcInfo().getStatus(),
                binding.remarks.getText().toString(), binding.number.getText().toString(),
                selectedTestKitRadioGroup,
                selectedlaboratoryPersonRadioGroup,
                selectedprocedureRadioGroup, selectedHaveALaboratory,
                getPreviousMillerInfoResponse.getQcInfo().getSlId(),
                sid
        ).observe(getViewLifecycleOwner(), response -> {
            progressDialog.dismiss();
            if (response == null) {
                errorMessage(getActivity().getApplication(), "Something Wrong here");
                return;
            }
            if (response.getStatus() == 400) {
                infoMessage(getActivity().getApplication(), "" + response.getMessage());
                return;
            }
            infoMessage(getActivity().getApplication(), "" + response.getMessage());
            viewPager.setCurrentItem(4);
        });
    }

    private void onCliK() {
        binding.procedureRadioGroup.performClick();
        binding.laboratoryPersonRadioGroup.performClick();
        binding.laboratoryPersonRadioGroup.performClick();
        binding.haveLaboratoryRadioGroup.performClick();
        binding.standardYes.performClick();
        binding.standardNO.performClick();
        binding.trainedYes.performClick();
        binding.trainedNo.performClick();
        binding.kitYes.performClick();
        binding.kitNo.performClick();
        binding.havLavYes.performClick();
        binding.havLavNo.performClick();
    }

//    @Override
//    public void onCheckedChanged(RadioGroup group, int checkedId) {
//        if (checkedId == R.id.standardYes) {
//            if (binding.standardYes.isChecked()) {
//                selectedprocedureRadioGroup = "1";
//                return;
//            }
//        }
//        if (checkedId == R.id.standardNO) {
//            if (binding.standardYes.isChecked()) {
//                selectedprocedureRadioGroup = "0";
//                return;
//            }
//        }
//
//        if (checkedId == R.id.trainedYes) {
//            if (binding.trainedYes.isChecked()) {
//                selectedlaboratoryPersonRadioGroup = "1";
//                return;
//            }
//        }
//        if (checkedId == R.id.trainedNo) {
//            if (binding.trainedNo.isChecked()) {
//                selectedlaboratoryPersonRadioGroup = "0";
//                return;
//            }
//
//        }
//
//        if (checkedId == R.id.kitYes) {
//            if (binding.kitYes.isChecked()) {
//                selectedTestKitRadioGroup = "1";
//                return;
//            }
//        }
//        if (checkedId == R.id.kitNo) {
//            if (binding.kitNo.isChecked()) {
//                selectedTestKitRadioGroup = "0";
//                return;
//            }
//        }
//
//
//        if (checkedId == R.id.havLavYes) {
//            if (binding.havLavYes.isChecked()) {
//                selectedHaveALaboratory = "1";
//                return;
//            }
//        }
//        if (checkedId == R.id.havLavNo) {
//            if (binding.havLavNo.isChecked()) {
//                selectedHaveALaboratory = "0";
//                return;
//            }
//        }
//    }
}