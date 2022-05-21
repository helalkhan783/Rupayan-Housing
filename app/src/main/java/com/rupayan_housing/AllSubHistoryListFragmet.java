package com.rupayan_housing;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rupayan_housing.adapter.CustomerEditHistoryAdapter;
import com.rupayan_housing.adapter.MillEditHistoryAdapter;
import com.rupayan_housing.adapter.MillerHistoryListAdapter;
import com.rupayan_housing.adapter.QcQaHistoryAdapter;
import com.rupayan_housing.databinding.FragmentAllSubHistoryListFragmetBinding;
import com.rupayan_housing.serverResponseModel.MillEditHistoryResponse;
import com.rupayan_housing.utils.InternetCheckerRecyclerBuddy;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.viewModel.MonitoringViewModel;


public class AllSubHistoryListFragmet extends BaseFragment {
    FragmentAllSubHistoryListFragmetBinding binding;
    String id, pageName;
    private MonitoringViewModel monitoringViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_all_sub_history_list_fragmet, container, false);
        binding.toolbar.setClickHandle(() -> {
            hideKeyboard(getActivity());
            getActivity().onBackPressed();
        });
        monitoringViewModel = new ViewModelProvider(this).get(MonitoringViewModel.class);
        try {
            id = getArguments().getString("id", id);
            pageName = getArguments().getString("pageName", pageName);
            binding.toolbar.toolbarTitle.setText("" + pageName);
        } catch (Exception e) {
        }
        getDataFromServer();

        return binding.getRoot();
    }

    private void getDataFromServer() {
        if (!new InternetCheckerRecyclerBuddy(getActivity()).isInternetAvailableHere(binding.historyRv, binding.noDataFound)) {
            return;
        }
        if (pageName.equals("Monitoring History")) {
            getMonitoringData();
            return;
        }
        if (pageName.equals("QcQa History")) {
            gteQcQaHistory();
            return;
        }
        if (pageName.equals("Customer Edit History")) {
            getCustomerEditHistory("1");
            return;
        }
        if (pageName.equals("Supplier Edit History")) {
            getCustomerEditHistory("2");
            return;
        }
        if (pageName.equals("Mill Edit History")) {
            getMillEditHistory();
            return;
        }
    }

    private void getMillEditHistory() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        monitoringViewModel.getMillEditHistory(getActivity(), id).observe(getViewLifecycleOwner(), response -> {
            progressDialog.dismiss();
            binding.progress.setVisibility(View.GONE);
            if (response == null) {
                errorMessage(getActivity().getApplication(), "Something wrong");
                return;
            }
            if (response.getStatus() == 400) {
                infoMessage(getActivity().getApplication(), response.getMessage());
                return;
            }

            if (response.getLists().isEmpty() || response.getLists() == null) {
                binding.historyRv.setVisibility(View.GONE);
                binding.noDataFound.setVisibility(View.VISIBLE);
                return;
            }

          MillEditHistoryAdapter adapter = new MillEditHistoryAdapter(getActivity(), response.getLists());
            binding.historyRv.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.historyRv.setAdapter(adapter);

        });
    }

    private void getCustomerEditHistory(String type) {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        monitoringViewModel.getCustomerAndSupplierEditHistory(getActivity(), id, type).observe(getViewLifecycleOwner(), response -> {
            progressDialog.dismiss();
            binding.progress.setVisibility(View.GONE);
            if (response == null) {
                errorMessage(getActivity().getApplication(), "Something wrong");
                return;
            }
            if (response.getStatus() == 400) {
                infoMessage(getActivity().getApplication(), response.getMessage());
                return;
            }

            if (response.getLists().isEmpty() || response.getLists() == null) {
                binding.historyRv.setVisibility(View.GONE);
                binding.noDataFound.setVisibility(View.VISIBLE);
                return;
            }
            // now set data to Rv
            CustomerEditHistoryAdapter adapter = new CustomerEditHistoryAdapter(getActivity(), response.getLists());
            binding.historyRv.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.historyRv.setAdapter(adapter);
        });

    }

    private void gteQcQaHistory() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        monitoringViewModel.getQcqaHistoryList(getActivity(), id).observe(getViewLifecycleOwner(), response -> {
            progressDialog.dismiss();
            binding.progress.setVisibility(View.GONE);
            if (response == null) {
                errorMessage(getActivity().getApplication(), "Something wrong");
                return;
            }
            if (response.getStatus() == 400) {
                infoMessage(getActivity().getApplication(), response.getMessage());
                return;
            }

            if (response.getLists().isEmpty() || response.getLists() == null) {
                binding.historyRv.setVisibility(View.GONE);
                binding.noDataFound.setVisibility(View.VISIBLE);
                return;
            }
            // now set data to Rv
            QcQaHistoryAdapter adapter = new QcQaHistoryAdapter(getActivity(), response.getLists(), response.enterprizeList);
            binding.historyRv.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.historyRv.setAdapter(adapter);


        });

    }

    private void getMonitoringData() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        monitoringViewModel.getMonitoringHistoryList(getActivity(), id).observe(getViewLifecycleOwner(), response -> {
            progressDialog.dismiss();
            binding.progress.setVisibility(View.GONE);
            if (response == null) {
                errorMessage(getActivity().getApplication(), "Something wrong");
                return;
            }
            if (response.getStatus() == 400) {
                infoMessage(getActivity().getApplication(), response.getMessage());
                return;
            }

            if (response.getLists().isEmpty() || response.getLists() == null) {
                binding.historyRv.setVisibility(View.GONE);
                binding.noDataFound.setVisibility(View.VISIBLE);
                return;
            }
            // now set data to Rv
            binding.historyRv.setLayoutManager(new LinearLayoutManager(getContext()));
            MonitoringHistoryAdapter adapter = new MonitoringHistoryAdapter(getActivity(), response.getLists(), response.getMonitoringType());
            binding.historyRv.setHasFixedSize(true);
            binding.historyRv.setAdapter(adapter);
        });
    }
}