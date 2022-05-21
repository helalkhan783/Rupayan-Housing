package com.rupayan_housing.view.fragment.miller.editmiller;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.rupayan_housing.R;
import com.rupayan_housing.adapter.MillerPendingListAdapter;
import com.rupayan_housing.clickHandle.MillerCompanyOwnerInfoClickHandle;
import com.rupayan_housing.clickHandle.ToolbarClickHandle;
import com.rupayan_housing.databinding.FragmentEditTimeOwnweAddBinding;
import com.rupayan_housing.serverResponseModel.DistrictListResponse;
import com.rupayan_housing.serverResponseModel.DivisionResponse;
import com.rupayan_housing.serverResponseModel.ThanaList;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.view.fragment.miller.MillerDetailsViewFragment;
import com.rupayan_housing.view.fragment.miller.addNewMiller.AddNewMiller;
import com.rupayan_housing.view.fragment.miller.addNewMiller.MillerProfileInformation;
import com.rupayan_housing.viewModel.MillerProfileInfoViewModel;
import com.rupayan_housing.viewModel.UpdateMillerViewModel;

import java.util.ArrayList;
import java.util.List;


public class EditTimeOwnweAdd extends BaseFragment {
    FragmentEditTimeOwnweAddBinding binding;
    private MillerProfileInfoViewModel millerProfileInfoViewModel;
    private UpdateMillerViewModel updateMillerViewModel;
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


    private String selectedDivision, selectedDistrict, selectedThana;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_time_ownwe_add, container, false);
        millerProfileInfoViewModel = new ViewModelProvider(this).get(MillerProfileInfoViewModel.class);
        updateMillerViewModel = new ViewModelProvider(this).get(UpdateMillerViewModel.class);
        /**
         * get Page Data from server
         */
        binding.toolbar.toolbarTitle.setText("Add Owner Info");
        binding.toolbar.setClickHandle(new ToolbarClickHandle() {
            @Override
            public void backBtn() {
                hideKeyboard(getActivity());
                getActivity().onBackPressed();
            }
        });
        getPageDataFromServer();
        binding.setClickHandle(new MillerCompanyOwnerInfoClickHandle() {
            @Override
            public void addNew() {
               /* DialogClass updDiag = new DialogClass();
                updDiag.show(getActivity().getSupportFragmentManager(), "dialog");*/
            }

            @Override
            public void save() {
                validationAndSave();
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


    private void validationAndSave() {
        if (binding.name.getText().toString().isEmpty()) {
            binding.name.setError("Empty Field");
            binding.name.requestFocus();
            return;
        }


        if (!binding.altMobile.getText().toString().isEmpty()) {
            if (!isValidPhone(binding.altMobile.getText().toString())) {
                binding.altMobile.setError("Invalid Mobile");
                binding.altMobile.requestFocus();
                return;
            }
        }
        if (!binding.mobile.getText().toString().isEmpty()) {
            if (!isValidPhone(binding.mobile.getText().toString())) {
                binding.mobile.setError("Invalid Mobile");
                binding.mobile.requestFocus();
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
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }
        String profileId =MillerPendingListAdapter.profileId;
        if (profileId == null){
            profileId =   MillerDetailsViewFragment.profileId;
        }
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        millerProfileInfoViewModel.submitMillerCompanyOwnerInfo(
                getActivity(), profileId, binding.name.getText().toString(), selectedDivision, selectedDistrict,
                selectedThana, binding.nid.getText().toString(), binding.mobile.getText().toString(),
                binding.altMobile.getText().toString(), binding.email.getText().toString())
                .observe(getViewLifecycleOwner(), response -> {
                    progressDialog.dismiss();
                    try {
                        if (response == null) {
                            errorMessage(getActivity().getApplication(), "" + response.getMessage());
                            Log.d("ERROR", "ERROR");
                            return;
                        }
                        if (response.getStatus() == 400) {
                            infoMessage(getActivity().getApplication(), "" + response.getMessage());
                            return;
                        }
                        binding.saveBtn.setVisibility(View.GONE);
                        successMessage(getActivity().getApplication(), "" + response.getMessage());
                        getActivity().onBackPressed();
                    } catch (Exception e) {
                    }
                });
    }

    private void getPageDataFromServer() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        try {
            millerProfileInfoViewModel.getProfileInfoResponse(getActivity())
                    .observe(getViewLifecycleOwner(), response -> {
                        try {
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
                                divisionNameList.add("" + response.getDivisions().get(i).getName());
                            }
                            binding.division.setItem(divisionNameList);
                        } catch (Exception e) {
                            Log.e("ERROR", e.getLocalizedMessage());
                        }
                    });
        } catch (Exception e) {
        }
    }


    /**
     * for set district Name
     */
    private void getDistrictListByDivisionId(String selectedDivision) {
        millerProfileInfoViewModel.getDistrictListByDivisionId(getActivity(), selectedDivision)
                .observe(getViewLifecycleOwner(), response -> {
                    districtListResponseList = new ArrayList<>();
                    districtListResponseList.clear();
                    districtNameList = new ArrayList<>();
                    districtNameList.clear();
                    districtListResponseList.addAll(response.getLists());

                    for (int i = 0; i < districtListResponseList.size(); i++) {
                        districtNameList.add(response.getLists().get(i).getName());
                    }
                    binding.district.setItem(districtNameList);
                });
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
                });
    }
}