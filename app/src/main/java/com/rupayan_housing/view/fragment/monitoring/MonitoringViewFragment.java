package com.rupayan_housing.view.fragment.monitoring;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.Toast;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.FragmentMonitoringViewBinding;
import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.serverResponseModel.Miller;
import com.rupayan_housing.serverResponseModel.MonitoringViewResponse;
import com.rupayan_housing.serverResponseModel.ZoneList;
import com.rupayan_housing.utils.FileDownloaderHelper;
import com.rupayan_housing.utils.RequestPermissionHandler;
import com.rupayan_housing.utils.RequestPermissionListener;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.view.fragment.DateFormatRight;
import com.rupayan_housing.viewModel.MonitoringDetailsViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MonitoringViewFragment extends BaseFragment {
    FragmentMonitoringViewBinding binding;
    private MonitoringDetailsViewModel monitoringDetailsViewModel;
    private RequestPermissionHandler mRequestPermissionHandler;//for handle storage permission
    String slId, document;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_monitoring_view, container, false);
        monitoringDetailsViewModel = new ViewModelProvider(this).get(MonitoringDetailsViewModel.class);
        mRequestPermissionHandler = new RequestPermissionHandler();
        binding.toolbar.setText("Monitoring Details");

        getPreviousFragmentData();

        getDataFromViewMOdel();

        binding.back.setOnClickListener(v -> getActivity().onBackPressed());

        binding.document.setOnClickListener(v -> {
            try {
                /**
                 * First get Permission
                 */
                handleButtonClicked();
                /**
                 * now download the file
                 */
                try {
                    FileDownloaderHelper.downloadFile(getActivity(), document);
                } catch (Exception e) {
                    Log.d("ERROR", "" + e.getMessage());
                }

            } catch (Exception e) {
                Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Error", e.getMessage());
            }
        });

        return binding.getRoot();
    }


    private void getPreviousFragmentData() {
        slId = getArguments().getString("slId");
    }


    private void getDataFromViewMOdel() {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check your Internet Connection");
            return;
        }
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        monitoringDetailsViewModel.getMonitoringDetails(getActivity(), slId).observe(getViewLifecycleOwner(),
                response -> {
                    progressDialog.dismiss();

                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "Something Wrong");
                        return;
                    }
                    if (response.getStatus() == 400) {
                        infoMessage(getActivity().getApplication(), "" + response.getMessage());
                        return;
                    }


                    document = response.getMonitoringDetails().getDocument();


                    try {
                        if (response.getMonitoringDetails().getMonitoringSummery() != null) {
                            binding.monitoringSummery.setText(":  " + response.getMonitoringDetails().getMonitoringSummery().replaceAll("\\<.*?>", "") );
                        }
                        if (response.getMonitoringDetails().getMonitoringDate() != null) {
                            binding.monitoringDate.setText(":  " + new DateFormatRight(getActivity(), response.getMonitoringDetails().getMonitoringDate()).onlyDayMonthYear());
                        }

                        binding.published.setText(":  " + new DateFormatRight(getActivity(), response.getMonitoringDetails().getPublishDate()).onlyDayMonthYear());

                        List<String> namelists = new ArrayList<>();
                        namelists.clear();

                        for (int i = 0; i < response.getZoneList().size(); i++) {
                            namelists.add(response.getZoneList().get(i).getZoneName());
                            if (response.getMonitoringDetails().getZoneID() != null) {
                                if (response.getZoneList().get(i).getZoneID().equals(response.getMonitoringDetails().getZoneID())) {
                                    binding.zone.setText(":  " + response.getZoneList().get(i).getZoneName());
                                }
                            }
                        }
                        for (int i = 0; i < response.getMonitoringType().size(); i++) {
                            if (response.getMonitoringDetails().getMonitoringType().equals(response.getMonitoringType().get(i).getTypeID())) {
                                binding.monitoringType.setText(":  " + response.getMonitoringType().get(i).getMonitoringTypeName());
                            }
                        }

//
                        List<String> millerNameList = new ArrayList<>();
                        for (int i = 0; i < response.getMiller().size(); i++) {
                            millerNameList.add(response.getMiller().get(i).getDisplayName());
                            if (response.getMonitoringDetails().getMillID().equals(response.getMiller().get(i).getSl())) {
                                binding.miller.setText(":   " + response.getMiller().get(i).getDisplayName());
                            }
                        }
                        if (response.getMonitoringDetails().getMonitorBy() !=null){
                            binding.monitorBy.setText(""+response.getMonitoringDetails().getMonitorBy());
                        }
                    } catch (Exception e) {
                        Log.d("ERROR", "" + e.getMessage());
                    }
                });
    }

    private void handleButtonClicked() {
        mRequestPermissionHandler.requestPermission(getActivity(), new String[]{
                Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_EXTERNAL_STORAGE
        }, 123, new RequestPermissionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getContext(), "request permission success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed() {
                Toast.makeText(getContext(), "request permission failed", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mRequestPermissionHandler.onRequestPermissionsResult(requestCode, permissions,
                grantResults);
    }

}