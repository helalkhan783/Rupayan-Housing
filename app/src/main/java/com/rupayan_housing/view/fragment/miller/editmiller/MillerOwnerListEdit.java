package com.rupayan_housing.view.fragment.miller.editmiller;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rupayan_housing.R;
import com.rupayan_housing.adapter.MillerOwnerListAdapter;
import com.rupayan_housing.adapter.MillerOwnerListLicenseAdapter;
import com.rupayan_housing.databinding.FragmentMillerOwnerListEditBinding;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.view.fragment.miller.addNewMiller.AddNewMiller;
import com.rupayan_housing.viewModel.UpdateMillerViewModel;


public class MillerOwnerListEdit extends BaseFragment implements MillerOwnerListClickHandle {

    private FragmentMillerOwnerListEditBinding binding;
    private UpdateMillerViewModel updateMillerViewModel;
    private String slId, portion;


    public MillerOwnerListEdit() {
    }

    public MillerOwnerListEdit(String slId, String portion) {
        this.slId = slId;
        this.portion = portion;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_miller_owner_list_edit, container, false);
        updateMillerViewModel = new ViewModelProvider(this).get(UpdateMillerViewModel.class);

        getPageDataFromServer();

        if (portion.equals("OWNER_INFO_EDIT")) {
            binding.addnew.setText("Add Owner Info");
        }
        if (portion.equals("LICENSE_INFO_EDIT")) {
            binding.addnew.setText("Add Licence");
            binding.addnew.setVisibility(View.VISIBLE);
        }
        binding.addnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (portion.equals("OWNER_INFO_EDIT")) {

                        Navigation.findNavController(getView()).navigate(R.id.action_editMillerFragment_to_editTimeOwnweAdd);
                    }

                } catch (Exception e) {
                }

                try {
                    if (portion.equals("LICENSE_INFO_EDIT")) {
                        try {
                            Navigation.findNavController(getView()).navigate(R.id.action_editMillerFragment_to_addEditTimeLicence);
                        } catch (Exception e) {
                            Log.d("ERROR", "" + e.getMessage());
                        }
                        return;
                    }
                } catch (Exception e) {
                }

            }
        });


        return binding.getRoot();
    }

    private void getPageDataFromServer() {

        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check your Internet Connection");
            return;
        }

        updateMillerViewModel.getPreviousMillerInfoBySid(getActivity(), slId)
                .observe(getViewLifecycleOwner(), response -> {
                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "" + response.getMessage());
                        return;
                    }
                    if (response.getStatus() == 400) {
                        infoMessage(getActivity().getApplication(), "" + response.getMessage());
                        return;
                    }


                    if (portion.equals("OWNER_INFO_EDIT")) {
                        if (response.getOwnerInfo().isEmpty() || response.getOwnerInfo() == null) {
                            binding.ownerListRv.setVisibility(View.GONE);
                            binding.noDataFound.setVisibility(View.VISIBLE);
                            binding.addnew.setVisibility(View.VISIBLE);
                            return;
                        }
                        binding.addnew.setVisibility(View.GONE);
                        //response.getOwnerInfo() // all list
                        binding.addnew.setVisibility(View.VISIBLE);
                        MillerOwnerListAdapter adapter = new MillerOwnerListAdapter(getActivity(), response.getOwnerInfo(), MillerOwnerListEdit.this);
                        binding.ownerListRv.setLayoutManager(new LinearLayoutManager(getContext()));
                        binding.ownerListRv.setAdapter(adapter);
                        return;
                    }
                    if (portion.equals("LICENSE_INFO_EDIT")) {
                        if (response.getOwnerInfo().isEmpty() || response.getOwnerInfo() == null) {
                            binding.ownerListRv.setVisibility(View.GONE);
                            binding.noDataFound.setVisibility(View.VISIBLE);
                            return;
                        }
                      //  binding.addnew.setText("Add More Licence");
                        MillerOwnerListLicenseAdapter adapter = new MillerOwnerListLicenseAdapter(getActivity(), response.getCertificateInfo(), MillerOwnerListEdit.this);
                        binding.ownerListRv.setLayoutManager(new LinearLayoutManager(getContext()));
                        binding.ownerListRv.setAdapter(adapter);
                    }
                });
    }

    @Override
    public void click(String ownerId, String profileId) {
        if (portion.equals("OWNER_INFO_EDIT")) {
            try {
                Bundle bundle = new Bundle();
                bundle.putString("sl_id", ownerId);
                Navigation.findNavController(getView()).navigate(R.id.fragment_edit_miller_to_fragment_miller_millerCompanyOwnerInfoEdit, bundle);
            } catch (Exception e) {
                Log.d("ERROR", "" + e.getMessage());
            }
            return;
        }

        if (portion.equals("LICENSE_INFO_EDIT")) {
            try {
                Bundle bundle = new Bundle();
                bundle.putString("sl_id", ownerId);
                bundle.putString("profileID", profileId);
                Navigation.findNavController(getView()).navigate(R.id.fragment_edit_miller_to_fragment_millerLicenseinfoEdit, bundle);
            } catch (Exception e) {
                Log.d("ERROR", "" + e.getMessage());
            }
            return;
        }

    }
}