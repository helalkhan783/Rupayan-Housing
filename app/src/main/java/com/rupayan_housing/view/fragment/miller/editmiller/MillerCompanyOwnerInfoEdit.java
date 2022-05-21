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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rupayan_housing.R;
import com.rupayan_housing.clickHandle.MillerCompanyOwnerInfoEditClickHandle;
import com.rupayan_housing.clickHandle.ToolbarClickHandle;
import com.rupayan_housing.databinding.FragmentMillerCompanyOwnerInfoEditBinding;
import com.rupayan_housing.serverResponseModel.DistrictListResponse;
import com.rupayan_housing.serverResponseModel.DivisionResponse;
import com.rupayan_housing.serverResponseModel.DuePaymentResponse;
import com.rupayan_housing.serverResponseModel.GetPreviousMillerInfoResponse;
import com.rupayan_housing.serverResponseModel.OwnerDetailsResponse;
import com.rupayan_housing.serverResponseModel.ThanaList;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.view.fragment.miller.addNewMiller.MillerProfileInformation;
import com.rupayan_housing.viewModel.MillerOwnerViewModel;
import com.rupayan_housing.viewModel.MillerProfileInfoViewModel;
import com.rupayan_housing.viewModel.UpdateMillerViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MillerCompanyOwnerInfoEdit extends BaseFragment {
    private FragmentMillerCompanyOwnerInfoEditBinding binding;
    private UpdateMillerViewModel updateMillerViewModel;
    private MillerOwnerViewModel millerOwnerViewModel;



    private MillerProfileInfoViewModel millerProfileInfoViewModel;
    private ViewPager viewPager;
    /**
     * for division
     */
    private List<DivisionResponse> divisionResponseList;
    private List<String> divisionNameList;
    /**
     * for district
     */
    private List<DistrictListResponse> districtListResponseList;
    private List<String> districtNameList;
    /**
     * for Thana
     */
    private List<ThanaList> thanaListsResponse;
    private List<String> thanaNameResponse;


    private OwnerDetailsResponse previousMillerInfoResponse;


    private String selectedDivision, selectedDistrict, selectedThana;

    private String sid,fromEdit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_miller_company_owner_info_edit, container, false);
        millerOwnerViewModel = new ViewModelProvider(this).get(MillerOwnerViewModel.class);
        viewPager = getActivity().findViewById(R.id.viewPager);
        millerProfileInfoViewModel = new ViewModelProvider(this).get(MillerProfileInfoViewModel.class);
        updateMillerViewModel = new ViewModelProvider(this).get(UpdateMillerViewModel.class);
        binding.toolbar.toolbarTitle.setText("Mill Owner Info Update");
        binding.toolbar.setClickHandle(() -> {
            hideKeyboard(getActivity());
            getActivity().onBackPressed();
        });
        getDataFromPreviousFragment();
        /**
         * get Page Data from server
         */
        getPageDataFromServer();


        binding.setClickHandle(new MillerCompanyOwnerInfoEditClickHandle() {
            @Override
            public void save() {
                hideKeyboard(getActivity());
                if (binding.name.getText().toString().isEmpty()) {
                    binding.name.setError("Empty Field");
                    binding.name.requestFocus();
                    return;
                }
                if (selectedDivision == null) {
                    infoMessage(getActivity().getApplication(), "Please select division");
                    return;
                }
                if (selectedDistrict == null) {
                    infoMessage(getActivity().getApplication(), "Please select district");
                    return;

                }
                if (selectedThana == null) {
                    infoMessage(getActivity().getApplication(), "Please select upazila/thana");
                    return;

                }


                if (!binding.mobile.getText().toString().isEmpty()) {
                    if (!isValidPhone(binding.mobile.getText().toString())) {
                        binding.mobile.setError("Invalid Mobile Number");
                        binding.mobile.requestFocus();
                        return;
                    }
                }
                if (!binding.altMobile.getText().toString().isEmpty()) {
                    if (!isValidPhone(binding.altMobile.getText().toString())) {
                        binding.altMobile.setError("Invalid Mobile Number");
                        binding.altMobile.requestFocus();
                        return;
                    }
                }
                if (!binding.email.getText().toString().isEmpty()) {
                    if (!isValidEmail(binding.email.getText().toString())) {
                        binding.email.setError("Invalid Email");
                        binding.email.requestFocus();
                        return;
                    }
                }

                dialog();

            }

            @Override
            public void addNew() {

            }
        });

        binding.division.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDivision = divisionResponseList.get(position).getDivisionId();
                binding.division.setEnableErrorLabel(false);
                /**
                 * now set district based on the current division
                 */
                if (selectedDivision != null) {
                    getDistrictListByDivisionId(selectedDivision);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDistrict = districtListResponseList.get(position).getDistrictId();
                binding.district.setEnableErrorLabel(false);
                /**
                 * now set thana based on the current district
                 */
                if (selectedDistrict != null) {
                    getThanaListByDistrictId(selectedDistrict);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.upazila.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedThana = thanaListsResponse.get(position).getUpazilaId();
                binding.upazila.setEnableErrorLabel(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
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
            validationAndSave();
        });
        alertDialog.show();
    }


    // for add new



    private void getDataFromPreviousFragment() {
        try {
            assert getArguments() != null;
            sid = getArguments().getString("sl_id");
        }catch (Exception e){}

    }

    private void getPageDataFromServer() {
        /**
         * Now set Previous selected  data
         */
           getPreviousSelectedData();



        try {
            ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.show();
            millerProfileInfoViewModel.getProfileInfoResponse(getActivity())
                    .observe(getViewLifecycleOwner(), response -> {
                        progressDialog.dismiss();
                        if (response == null) {
                            errorMessage(getActivity().getApplication(), "Something Wrong");
                            return;
                        } if (response.getStatus() == 400) {
                            infoMessage(getActivity().getApplication(), "" + response.getMessage());
                            return;
                        }
                        divisionResponseList = new ArrayList<>();
                        divisionResponseList.clear();
                        divisionNameList = new ArrayList<>();
                        divisionNameList.clear();
                        divisionResponseList.addAll(response.getDivisions());
                        for (int i = 0; i < response.getDivisions().size(); i++) {
                            divisionNameList.add(response.getDivisions().get(i).getName());
                        }
                        binding.division.setItem(divisionNameList);

                        /**
                         * now set Previous selected division
                         */
                        if (previousMillerInfoResponse != null) {
                            for (int i = 0; i < divisionResponseList.size(); i++) {
                                if (!previousMillerInfoResponse.getOwnerData().isEmpty()) {
                                    if (divisionResponseList.get(i).getDivisionId().equals(previousMillerInfoResponse.getOwnerData().get(0).getDivision())) {
                                        selectedDivision = divisionResponseList.get(i).getDivisionId();//set previous selected division
                                        binding.division.setSelection(i);
                                        getDistrictListByDivisionId(selectedDivision);
                                    }
                                }
                            }
                        }


                    });
        }catch (Exception e){}
    }

    /**
     * For set previous selected data
     */
    private void getPreviousSelectedData() {
        try {
            millerOwnerViewModel.getOwnerDetailsResponse(getActivity(), sid)
                    .observe(getViewLifecycleOwner(), response -> {
                        if (response == null) {
                            errorMessage(getActivity().getApplication(), "Something Wrong");
                            return;
                        }
                        if (response.getStatus() == 400) {
                            infoMessage(getActivity().getApplication(), "" + response.getMessage());
                            return;
                        }

                        previousMillerInfoResponse = response;
                        try {
                            if (!previousMillerInfoResponse.getOwnerData().isEmpty()) {
                                binding.name.setText("" + previousMillerInfoResponse.getOwnerData().get(0).getOwnerName());
                                binding.nid.setText("" + response.getOwnerData().get(0).getNid());
                                binding.mobile.setText("" + response.getOwnerData().get(0).getMobileNo());
                                binding.altMobile.setText("" + response.getOwnerData().get(0).getAltMobile());
                                binding.email.setText("" + response.getOwnerData().get(0).getEmail());
                            }
                        } catch (Exception e) {
                            Log.d("ERROR", "" + e.getLocalizedMessage());
                        }


                    });
        }catch (Exception e){}

       /* updateMillerViewModel.getPreviousMillerInfoBySid(getActivity(), sid)
                .observe(getViewLifecycleOwner(), response -> {
                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "Something Wrong");
                        return;
                    }
                    if (response.getStatus() == 400) {
                        infoMessage(getActivity().getApplication(), "" + response.getMessage());
                        return;
                    }
                    previousMillerInfoResponse = response;
                    try {
                        if (!previousMillerInfoResponse.getOwnerInfo().isEmpty()) {
                            binding.name.setText(previousMillerInfoResponse.getOwnerInfo().get(0).getOwnerName());
                            binding.nid.setText(response.getOwnerInfo().get(0).getNid());
                            binding.mobile.setText(response.getOwnerInfo().get(0).getMobileNo());
                            binding.altMobile.setText(response.getOwnerInfo().get(0).getAltMobile());
                            binding.email.setText(response.getOwnerInfo().get(0).getEmail());
                        }
                    } catch (Exception e) {
                        Log.d("ERROR", "" + e.getLocalizedMessage());
                    }
                });*/
    }

    /**
     * for set district Name
     */
    private void getDistrictListByDivisionId(String selectedDivision) {

    try {
        millerProfileInfoViewModel.getDistrictListByDivisionId(getActivity(), selectedDivision)
                .observe(getViewLifecycleOwner(), response -> {

                    districtListResponseList = new ArrayList<>();
                    districtListResponseList.clear();
                    districtNameList = new ArrayList<>();
                    districtNameList.clear();
                    districtListResponseList.addAll(response.getLists());

                    for (int i = 0; i < districtListResponseList.size(); i++) {
                        districtNameList.add(""+response.getLists().get(i).getName());
                    }
                    binding.district.setItem(districtNameList);

                    if (previousMillerInfoResponse != null) {

                        try {
                            for (int i = 0; i < districtListResponseList.size(); i++) {
                                if (districtListResponseList.get(i).getDistrictId().equals(previousMillerInfoResponse.getOwnerData().get(0).getDistrict())) {
                                    selectedDistrict = districtListResponseList.get(i).getDistrictId();
                                    binding.district.setSelection(i);
                                    /**
                                     * now set previous selected thana by current district
                                     */
                                    getThanaListByDistrictId(selectedDistrict);
                                }
                            }

                        } catch (Exception e) {
                            Log.d("ERROR", "Something Wrong in  = " + this.getClass().getName() + "\n" + e.getMessage());
                        }

                    }

                });
    }catch (Exception e){}
    }

    /**
     * for set thana Name
     */
    private void getThanaListByDistrictId(String selectedDistrict) {
        millerProfileInfoViewModel.getThanaListByDistrictId(getActivity(), selectedDistrict)
                .observe(getViewLifecycleOwner(), response -> {

                    thanaListsResponse = new ArrayList<>();
                    thanaListsResponse.clear();
                    thanaNameResponse = new ArrayList<>();
                    thanaNameResponse.clear();
                    thanaListsResponse.addAll(response.getLists());
                    for (int i = 0; i < response.getLists().size(); i++) {
                        thanaNameResponse.add(response.getLists().get(i).getName());
                    }
                    binding.upazila.setItem(thanaNameResponse);
                    if (previousMillerInfoResponse != null) {
                        try {
                            for (int i = 0; i < thanaListsResponse.size(); i++) {
                                if (thanaListsResponse.get(i).getUpazilaId().equals(previousMillerInfoResponse.getOwnerData().get(0).getUpazila())) {
                                    selectedThana = thanaListsResponse.get(i).getUpazilaId();
                                    binding.upazila.setSelection(i);
                                }
                            }
                        } catch (Exception e) {
                            Log.d("ERROR", "Something Wrong in  = " + this.getClass().getName() + "\n" + e.getMessage());
                        }
                    }


                });
    }


    private void validationAndSave() {
        if (!isInternetOn(getActivity())) {
            infoMessage(getActivity().getApplication(), "Please check your internet connection");
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        updateMillerViewModel.updateMillerOwnerInfo(
                getActivity(), binding.name.getText().toString(),previousMillerInfoResponse.getOwnerData().get(0).getProfileID(),
                selectedDivision, selectedDistrict, selectedThana, binding.nid.getText().toString(),
                binding.mobile.getText().toString(),binding.altMobile.getText().toString(), binding.email.getText().toString(),
                previousMillerInfoResponse.getOwnerData().get(0).getSlID()
        ).observe(getViewLifecycleOwner(), response -> {
            progressDialog.dismiss();
            if (response == null) {
                errorMessage(getActivity().getApplication(), "Something Wrong");
                return;
            }
            if (response.getStatus() == 400) {
                infoMessage(getActivity().getApplication(), "" + response.getMessage());
                return;
            }
            if (response.getStatus() != 200) {
                Log.d("ERROR", "ERROR");
                return;
            }
            hideKeyboard(getActivity());
            successMessage(getActivity().getApplication(), "" + response.getMessage());
            getActivity().onBackPressed();
        });

    }
}