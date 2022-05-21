package com.rupayan_housing.view.fragment.production.packaging;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rupayan_housing.R;
import com.rupayan_housing.adapter.PackagingDetailsAdapter;
import com.rupayan_housing.clickHandle.ToolbarClickHandle;
import com.rupayan_housing.databinding.FragmentPackagingDetailsBinding;
import com.rupayan_housing.serverResponseModel.PackagingDetailsResponse;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.viewModel.PackagingDetailsViewModel;

public class PackagingDetails extends BaseFragment {
    private FragmentPackagingDetailsBinding binding;
    private PackagingDetailsViewModel packagingDetailsViewModel;
    private String packagingVendorId, PackagingSID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_packaging_details, container, false);
        getBundleData();
        packagingDetailsViewModel = new ViewModelProvider(this).get(PackagingDetailsViewModel.class);
        binding.toolbar.setClickHandle(() -> {
            getActivity().onBackPressed();

        });
        binding.toolbar.toolbarTitle.setText("Packaging Details");
        getDetailsFromServer();
        return binding.getRoot();
    }

    private void getDetailsFromServer() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        packagingDetailsViewModel.getPackagingDetails(getActivity(), packagingVendorId, PackagingSID).observe(getViewLifecycleOwner(), response -> {
            progressDialog.dismiss();
            if (response == null || response.getStatus() == 400 || response.getStatus() ==500) {
                infoMessage(getActivity().getApplication(), "Something Wrong");
                return;
            }


      try {
          binding.packagingNumber.setText(":  #" + response.getDetails().getPackagingInfos().getPackagingSLID());
          binding.date.setText(":  " + response.getDetails().getPackagingInfos().getPackagingEntryDateTime());
          binding.note.setText(":  " + response.getDetails().getPackagingInfos().getNote());

          binding.refPerson.setText(":  " + response.getDetails().getRefUserInfos().getCustomerFname() + " @ " + response.getDetails().getRefUserInfos().getCompanyName());
          binding.phoneNo.setText(":  " + response.getDetails().getRefUserInfos().getPhone());
          binding.email.setText(":  " + response.getDetails().getRefUserInfos().getEmail());

      }catch (Exception e){}
            PackagingDetailsAdapter adapter = new PackagingDetailsAdapter(getActivity(), response.getDetails().getPackagingDetails());
            binding.packagingListRv.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.packagingListRv.setAdapter(adapter);
            progressDialog.dismiss();
        });
    }

    private void getBundleData() {
        packagingVendorId = getArguments().getString("packagingVendorId");
        PackagingSID = getArguments().getString("PackagingSID");
    }
}