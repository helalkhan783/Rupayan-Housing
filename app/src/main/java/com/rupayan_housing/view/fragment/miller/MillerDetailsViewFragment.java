package com.rupayan_housing.view.fragment.miller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rupayan_housing.R;
import com.rupayan_housing.adapter.CertificateInfoAdapter;
import com.rupayan_housing.adapter.MillerOwnerInfoAdapter;
import com.rupayan_housing.databinding.FragmentMillerDetailsViewBinding;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.DuePaymentResponse;
import com.rupayan_housing.utils.MillerUtils;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.serverResponseModel.view_details.EmployeeData;
import com.rupayan_housing.serverResponseModel.view_details.MillerCertificateDatum;
import com.rupayan_housing.serverResponseModel.view_details.MillerOwnerDatum;
import com.rupayan_housing.serverResponseModel.view_details.ProfileData;
import com.rupayan_housing.serverResponseModel.view_details.QcData;
import com.rupayan_housing.viewModel.UpdateMillerViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MillerDetailsViewFragment extends BaseFragment implements CertificateApprovalDataGet {
    private FragmentMillerDetailsViewBinding binding;
    private MillerViewDetailsViewModel millerViewDetailsViewModel;
    private UpdateMillerViewModel updateMillerViewModel;

    String portion, pageName, status, typeKey;
    List<String> permissionNameType;
    List<String> permissionIdList;
    String reviewStatus;
    String permissionId;

    public static String slId;

    List<MillerCertificateDatum> certificateData1;

    String edibleId, industrialId, tinId, tradeId;
    private boolean haveOwnerData = false;
    int totalEmployeeMale, totalEmployeeFemale;
    public static String isSubmit;

    public static String profileId, sl;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_miller_details_view, container, false);
        millerViewDetailsViewModel = new ViewModelProvider(this).get(MillerViewDetailsViewModel.class);
        updateMillerViewModel = new ViewModelProvider(this).get(UpdateMillerViewModel.class);
        binding.toolbar.edit.setVisibility(View.VISIBLE);
        binding.toolbar.setClickHandle(() -> {
            hideKeyboard(getActivity());
            getActivity().onBackPressed();
        });

        getPeriviousData();

        getAllDetailsFromViewModel();

        approveDeclineOptionSensor();
        binding.savetn.setOnClickListener(v -> approveMiller());


        permissionNameType = new ArrayList<>();
        permissionNameType.clear();
        permissionIdList = new ArrayList<>();
        permissionIdList.clear();

        permissionNameType.addAll(Arrays.asList("Approve", "Decline"));
        permissionIdList.addAll(Arrays.asList("1", "2"));
        binding.permissionSelect.setItem(permissionNameType);


        binding.permissionSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                permissionId = permissionIdList.get(position);
                binding.permissionSelect.setEnableErrorLabel(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        /**
         * For approval Submit
         */
        binding.submittedStatus.setOnClickListener(v -> approvalSubmitDialog());
        binding.toolbar.edit.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("slID", slId);
            bundle.putString("portion", MillerUtils.millreProfileList);

            Navigation.findNavController(getView()).navigate(R.id.action_millerDetailsViewFragment_to_editMillerFragment, bundle);

        });

        return binding.getRoot();
    }

    private void approveDeclineOptionSensor() {
        try {
            if (portion.equals(MillerUtils.millerHistoryList) || portion.equals("notification")) {
                String profileTypeId = getProfileTypeId(getActivity().getApplication());
                if (profileTypeId.equals("4") || profileTypeId.equals("5") || profileTypeId.equals("6")) {
                    if (havePermission()) {
                        if (reviewStatus == null || reviewStatus.equals("0")) {
                            binding.layoutPermission.setVisibility(View.VISIBLE);
                        }
                    }
                    return;
                }

            }
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
        }

    }


    private void approveMiller() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());

        @SuppressLint("InflateParams")
        View view = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.purchase_dialog, null);
        //Set the view
        builder.setView(view);
        TextView tvTitle, tvMessage;
        ImageView imageIcon = view.findViewById(R.id.img_icon);
        tvMessage = view.findViewById(R.id.tv_message);
        tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("Do You Want to Confirm This Sale ?");//set warning title
        tvMessage.setText("SALT ERP");
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
            if (permissionId == null) {
                binding.permissionSelect.setEnableErrorLabel(true);
                binding.permissionSelect.setErrorText("Empty");
                return;
            }
            millerViewDetailsViewModel.approveMiller(getActivity(), slId, permissionId, reviewStatus).observe(getViewLifecycleOwner(), response -> {
                try {
                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "something wrong");
                        return;
                    }
                    if (response.getStatus() == 400) {
                        infoMessage(requireActivity().getApplication(), response.getMessage());
                        return;
                    }
                    if (response.getStatus() == 200) {
                        successMessage(getActivity().getApplication(), "Successful");
                        getActivity().onBackPressed();
                    }

                } catch (Exception e) {
                    Log.d("Error", e.getMessage());
                }
            });
        });

        alertDialog.show();
    }

    private void getAllDetailsFromViewModel() {
        hideKeyboard(getActivity());
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        millerViewDetailsViewModel.getMillerViewDetails(getActivity(), slId).observe(getViewLifecycleOwner(), response -> {
            progressDialog.dismiss();
            if (response == null) {
                errorMessage(getActivity().getApplication(), "something wrong");
                return;
            }

            if (response.getStatus() == 400) {
                infoMessage(getActivity().getApplication(), response.getMessage());
                return;
            }

            sl = response.getGetDetails().getSl();

            if (response.getStatus() == 200) {
                /** set profile data*/
                setDataInView(response.getProfileData());
                reviewStatus = response.getProfileData().getReviewStatus();

                /** set ownerInfo*/
                if (response.getOwnerData().isEmpty()) {
                    binding.companyOwnerInfoRv.setVisibility(View.GONE);
                    binding.onerTv.setVisibility(View.VISIBLE);
                    haveOwnerData = true;
                } else {
                    haveOwnerData = false;
                    setOwnerInfo(response.getOwnerData());
                }

                /** now set certificateInfo */

                if (response.getCertificateData().isEmpty()) {
                    binding.certificketInfoRv.setVisibility(View.GONE);
                    binding.certificketTv.setVisibility(View.VISIBLE);
                } else {
                    setCertificateInfoInRv(response.getCertificateData());
                }
                /** now set QC information */

                setQcData(response.getQcData());
            }

            /** now set employee information */

            setEmployeeInformation(response.getEmployeeData());
        });
    }

    private void setEmployeeInformation(EmployeeData employeeData) {
        binding.fullTimeMale.setText(employeeData.getFullTimeMale());
        binding.fullTimeFemale.setText(employeeData.getFullTimeFemale());

        binding.partTimeMale.setText(employeeData.getPartTimeMale());
        binding.partTimeFemale.setText(employeeData.getPartTimeFemail());

        binding.totalTechMale.setText(employeeData.getTotalTechMale());
        binding.totalTechFemale.setText(employeeData.getTotalTechFemale());

        totalEmployeeMale = employeeData.getTotalEmployeeMale();
        totalEmployeeFemale = employeeData.getTotalEmployeeFemale();

        binding.totalEmployeeMale.setText(String.valueOf(employeeData.getTotalEmployeeMale()));
        binding.totalEmployeeFemale.setText(String.valueOf(employeeData.getTotalEmployeeFemale()));

    }

    private void setQcData(QcData qcData) {
        if (qcData.getHaveLaboratory() == null) {
            binding.haveALabe.setText(":");
        } else {
            binding.haveALabe.setText(":  " + qcData.getHaveLaboratory());

        }
        if (qcData.getTrainedLaboratoryPerson() == null) {
            binding.labPerson.setText(":");
        } else {
            binding.labPerson.setText(":  " + qcData.getTrainedLaboratoryPerson());

        }
        if (qcData.getLaboratoryPerson() == null) {
            binding.numberLabPerson.setText(":");
        } else {
            binding.numberLabPerson.setText(":  " + qcData.getLaboratoryPerson());

        }
        if (qcData.getStandardProcedure() == null) {
            binding.procedure.setText(":");
        } else {
            binding.procedure.setText(":  " + qcData.getStandardProcedure());

        }
        if (qcData.getUseTestKit() == null) {
            binding.testKit.setText(":");
        } else {
            binding.testKit.setText(":  " + qcData.getUseTestKit());
        }
        if (qcData.getLabRemarks() == null) {
            binding.remarkQc.setText(":");
        } else {
            binding.remarkQc.setText(":  " + qcData.getLabRemarks());
        }
    }

    private void setCertificateInfoInRv(List<MillerCertificateDatum> certificateData) {
        certificateData1 = new ArrayList<>();
        for (int i = 0; i < certificateData.size(); i++) {
            if (certificateData.get(i).getCertificateTypeID().equals("4")) {
                edibleId = certificateData.get(i).getCertificateTypeID();
            }
            if (certificateData.get(i).getCertificateTypeID().equals("5")) {
                industrialId = certificateData.get(i).getCertificateTypeID();
            }
            if (certificateData.get(i).getCertificateTypeID().equals("6")) {
                tradeId = certificateData.get(i).getCertificateTypeID();
            }
            if (certificateData.get(i).getCertificateTypeID().equals("7")) {
                tinId = certificateData.get(i).getCertificateTypeID();
            }
        }


        binding.certificketInfoRv.setLayoutManager(new LinearLayoutManager(getContext()));
        CertificateInfoAdapter adapter = new CertificateInfoAdapter(getActivity(), certificateData, this);
        binding.certificketInfoRv.setAdapter(adapter);
    }

    private void setOwnerInfo(List<MillerOwnerDatum> ownerInfo) {

        binding.companyOwnerInfoRv.setLayoutManager(new LinearLayoutManager(getContext()));
        MillerOwnerInfoAdapter adapter = new MillerOwnerInfoAdapter(getActivity(), ownerInfo);
        binding.companyOwnerInfoRv.setAdapter(adapter);

    }

    private void getPeriviousData() {

        slId = getArguments().getString("slId");

        portion = getArguments().getString("portion");
        pageName = getArguments().getString("pageName");
        status = getArguments().getString("status");
        typeKey = getArguments().getString("typeKey");

        binding.toolbar.toolbarTitle.setText(pageName);


    }

    private void setDataInView(ProfileData profileData) {
        if (profileData.getDisplayName() != null) {
            binding.nameTv.setText(":  " + profileData.getDisplayName());

        }
        if (profileData.getFullName() != null) {
            binding.fullName.setText(":  " + profileData.getFullName());
        }

        if (profileData.getProcessTypeName() == null) {
            binding.processType.setText(":");
        } else {
            binding.processType.setText(":  " + profileData.getProcessTypeName());
        }


        if (profileData.getMillTypeName() == null) {
            binding.typeOfMiller.setText(":");
        } else {
            binding.typeOfMiller.setText(":  " + profileData.getMillTypeName());
        }

        if (profileData.getCapacity() == null) {
            binding.capacitYTpa.setText(":");
        } else {
            binding.capacitYTpa.setText(":  " + profileData.getCapacity());
        }
        if (profileData.getZoneName() == null) {
            binding.zone.setText(":  ");
        } else {
            binding.zone.setText(":  " + profileData.getZoneName());
        }
        if (profileData.getMillID() == null) {
            binding.millerId.setText(":  ");
        } else {
            binding.millerId.setText(":  " + profileData.getMillID());
        }

        if (profileData.getReviewStatus() == null) {
            binding.reviewStatus.setText(":  ");
        } else {
            if (profileData.getReviewStatus().equals("0")) {
                binding.reviewStatus.setText(":  " + "Pending Review");

            }
            if (profileData.getReviewStatus().equals("1")) {
                binding.reviewStatus.setText(":  " + "Approved");

            }
        }

        if (profileData.getOwnerTypeName() == null) {
            binding.typeOfOwner.setText(":  ");
        } else {
            binding.typeOfOwner.setText(":  " + profileData.getOwnerTypeName());
        }

        if (profileData.getDivisionName() == null) {
            binding.division.setText(":  ");
        } else {
            binding.division.setText(":  " + profileData.getDivisionName());
        }

        if (profileData.getDistrictName() == null) {
            binding.district.setText(":");
        } else {
            binding.district.setText(":  " + profileData.getDistrictName());
        }

        if (profileData.getUpazilaName() == null) {
            binding.thanaUpazilla.setText(":");
        } else {
            binding.thanaUpazilla.setText(":  " + profileData.getUpazilaName());

        }

        if (profileData.getRemarks() == null) {
            binding.remarks.setText(":  ");

        } else {
            binding.remarks.setText(":  " + profileData.getRemarks());
        }


        try {
            isSubmit = String.valueOf(profileData.getIsSubmit());
            if (profileData.getIsSubmit().equals("1")) {
                binding.submittedStatus.setVisibility(View.GONE);
                binding.alreadySubmitted.setVisibility(View.VISIBLE);
                return;
            }

            if (profileData.getIsSubmit() == null) {
                if (getProfileTypeId(getActivity().getApplication()).equals("7")) {
                    if (PermissionUtil.currentUserPermissionList(PreferenceManager.
                            getInstance(getContext()).getUserCredentials().getPermissions()).contains(1444) ||
                            PermissionUtil.currentUserPermissionList(PreferenceManager.
                                    getInstance(getContext()).getUserCredentials().getPermissions()).contains(1)) {
                        binding.alreadySubmitted.setVisibility(View.GONE);
                        binding.submittedStatus.setVisibility(View.VISIBLE);
                        return;
                    } else {
                        binding.submittedStatus.setVisibility(View.GONE);
                        binding.alreadySubmitted.setVisibility(View.VISIBLE);
                        binding.alreadySubmitted.setText("Not submit");
                        return;
                    }
                }
            }
            if (getProfileTypeId(getActivity().getApplication()).equals("4")) {
                binding.submittedStatus.setVisibility(View.GONE);
                binding.alreadySubmitted.setVisibility(View.VISIBLE);
                binding.alreadySubmitted.setText("Not submit");
            }

        } catch (Exception e) {
            Log.d("ERRor", "" + e.getMessage());
        }


    }


    public void approvalSubmitDialog() {
        if (haveOwnerData == true) {
            infoMessage(getActivity().getApplication(), "Add  owner info");
            return;
        }
        if (edibleId == null) {
            infoMessage(getActivity().getApplication(), "Add  Edible certificate");
            return;
        }
        if (industrialId == null) {
            infoMessage(getActivity().getApplication(), "Add  Industrial certificate");
            return;
        }
        if (tinId == null) {
            infoMessage(getActivity().getApplication(), "Add  TIN certificate");
            return;
        }
        if (tradeId == null) {
            infoMessage(getActivity().getApplication(), "Add  Trade License (TL) certificate");
            return;
        }
        int totalEmploee = totalEmployeeMale + totalEmployeeFemale;
        if (totalEmploee == 0) {
            infoMessage(getActivity().getApplication(), "Add  employee info");
            return;
        }


        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());

        @SuppressLint("InflateParams")
        View view = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.purchase_dialog, null);
        //Set the view
        builder.setView(view);
        TextView tvTitle, tvMessage;
        ImageView imageIcon = view.findViewById(R.id.img_icon);
        tvMessage = view.findViewById(R.id.tv_message);
        tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("Do You Want To Submit For Approval ?");//set warning title
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
            validationAndSave();
        });
        alertDialog.show();

    }

    private void validationAndSave() {
        if (!isInternetOn(getActivity())) {
            infoMessage(getActivity().getApplication(), "Please check your internet connection");
            return;
        }
        updateMillerViewModel.submitMillerApproval(getActivity(), slId).observe(getViewLifecycleOwner(), new Observer<DuePaymentResponse>() {
            @Override
            public void onChanged(DuePaymentResponse response) {
                if (response == null) {
                    errorMessage(getActivity().getApplication(), "Something Wrong");
                    return;
                }
                if (response.getStatus() == 400) {
                    infoMessage(getActivity().getApplication(), "" + response.getMessage());
                    return;
                }
                successMessage(getActivity().getApplication(), "" + response.getMessage());

                binding.alreadySubmitted.setVisibility(View.VISIBLE);
                binding.submittedStatus.setVisibility(View.GONE);
            }
        });
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onStart() {
        super.onStart();
       /* slId = getArguments().getString("slId");
        portion = getArguments().getString("portion");*/
/*

        try {

            if (portion.equals(MillerUtils.millerHistoryList)) {
                String profileTypeId = getProfileTypeId(getActivity().getApplication());
                if (profileTypeId.equals("4") || profileTypeId.equals("5")) {
                    if (havePermission()) {
                       // if (reviewStatus == null || reviewStatus.equals("0")) {
                            binding.layoutPermission.setVisibility(View.VISIBLE);
                            binding.savetn.setVisibility(View.VISIBLE);
                       // }
                    }
                    return;
                }
                if (profileTypeId.equals("7") || profileTypeId.equals("6")) {
                    checkStatus();
                }
            }
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
        }
*/

    }

    /**
     * check Permission for approve and decline
     */
    private boolean havePermission() {
        try {
            if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getActivity()).getUserCredentials().getPermissions()).contains(1449) || PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getActivity()).getUserCredentials().getPermissions()).contains(1)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    private void checkStatus() {
        if (reviewStatus.equals("2")) {
            binding.tv.setText("Declined");
            binding.tv.setVisibility(View.VISIBLE);
            return;
        }
        if (reviewStatus.equals("1")) {
            binding.tv.setText("Approved");
            binding.tv.setVisibility(View.VISIBLE);
            return;
        }

        if (reviewStatus.equals("0") || reviewStatus == null) {
            binding.tv.setText("Pending");
            binding.tv.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void approve(int adapterPosition, String certificateId, String status) {
        if (certificateId != null) {
            showApprovalDialog("Decline", certificateId, status);
        }

    }


    @Override
    public void decline(int adapterPosition, String certificateId) {

    }

    private void showApprovalDialog(String fromApproveOrDecline, String certificateId, String sttatus) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        @SuppressLint("InflateParams")
        View view = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.purchase_dialog, null);
        //Set the view
        builder.setView(view);
        TextView tvTitle, tvMessage;
        ImageView imageIcon = view.findViewById(R.id.img_icon);
        tvMessage = view.findViewById(R.id.tv_message);
        tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("Do You Want to " + fromApproveOrDecline + " ?");//set warning title
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
            if (!(isInternetOn(getActivity()))) {
                infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
                return;
            }
            alertDialog.dismiss();
            millerViewDetailsViewModel.getCertificateApprovalResponse(getActivity(), certificateId, sttatus).observe(getViewLifecycleOwner(), new Observer<DuePaymentResponse>() {
                @Override
                public void onChanged(DuePaymentResponse response) {
                    if (response == null) {
                        infoMessage(getActivity().getApplication(), "Something Wrong");
                        return;
                    }

                    if (response.getStatus() == 400) {
                        errorMessage(getActivity().getApplication(), response.getMessage());
                        return;
                    }
                    if (response.getStatus() == 500) {
                        errorMessage(getActivity().getApplication(), "Something Wrong");
                        return;
                    }
                    if (response.getStatus() == 200) {
                        successMessage(getActivity().getApplication(), response.getMessage());
                        getAllDetailsFromViewModel();
                    }

                }
            });

        });
        alertDialog.show();
    }


}