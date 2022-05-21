package com.rupayan_housing.view.fragment.salt_distribution;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.rupayan_housing.R;
import com.rupayan_housing.adapter.VarientlistAdapter;
import com.rupayan_housing.databinding.FragmentVarientlistBinding;
import com.rupayan_housing.serverResponseModel.AddNewItemUnit;
import com.rupayan_housing.utils.InternetCheckerRecyclerBuddy;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.viewModel.AddNewItemViewModel;
import com.rupayan_housing.viewModel.AddPacketItemViewModel;
import com.rupayan_housing.viewModel.VarientViewModel;

import java.util.ArrayList;
import java.util.List;


public class VarientlistFragment extends BaseFragment implements ManageAddVarient {
    FragmentVarientlistBinding binding;
    VarientViewModel varientViewModel;
    private AddNewItemViewModel addNewItemViewModel;
    private AddPacketItemViewModel addPacketItemViewModel;

    String refProductID, pageName, title;

    /**
     * for unit
     */
    private List<AddNewItemUnit> unitResponseList;
    private List<String> unitNameList;

    String selectedPrimaryUnit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_varientlist, container, false);
        binding.toolbar.setClickHandle(() -> getActivity().onBackPressed());
        varientViewModel = new ViewModelProvider(this).get(VarientViewModel.class);
        addNewItemViewModel = new ViewModelProvider(this).get(AddNewItemViewModel.class);
        addPacketItemViewModel = new ViewModelProvider(this).get(AddPacketItemViewModel.class);


        getPreviousFragment();
        binding.toolbar.toolbarTitle.setText("" + pageName);
        binding.toolbar.addBtn.setVisibility(View.VISIBLE);
        binding.toolbar.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.expandableView.isExpanded()) {
                    binding.expandableView.setExpanded(false);
                    return;
                }
                binding.expandableView.setExpanded(true);
            }
        });
        getPageData();
        getVarientList();
        binding.unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPrimaryUnit = unitResponseList.get(position).getID();
                binding.unit.setEnableErrorLabel(false);
                binding.unit.setErrorText("Empty");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.saveBtn.setOnClickListener(v -> validation());

        return binding.getRoot();
    }

    private void validation() {
        if (binding.itemName.getText().toString().isEmpty()) {
            binding.itemName.setError("Empty");
            binding.itemName.requestFocus();
            return;
        }

        if (binding.quantity.getText().toString().isEmpty()) {
            binding.quantity.setError("Empty");
            binding.quantity.requestFocus();
            return;
        }
        if (binding.price.getText().toString().isEmpty()) {
            binding.price.setError("Empty");
            binding.price.requestFocus();
            return;
        }
        if (selectedPrimaryUnit == null) {
            binding.unit.setEnableErrorLabel(true);
            binding.unit.setErrorText("Empty");
            binding.unit.requestFocus();
        }
        addPacketItemViewModel.addVarient(getActivity(), refProductID, binding.itemName.getText().toString(), binding.quantity.getText().toString(), selectedPrimaryUnit,
                "1", binding.price.getText().toString()).observe(getViewLifecycleOwner(), response -> {
            if (response == null) {
                errorMessage(getActivity().getApplication(), "Something wrong");
                return;
            }
            if (response.getStatus() == 400) {
                infoMessage(requireActivity().getApplication(), response.getMessage());
                return;
            }
            successMessage(getActivity().getApplication(), "" + response.getMessage());
            binding.noDataFound.setVisibility(View.GONE);
            binding.varientRv.setVisibility(View.VISIBLE);
            getVarientList();
            binding.expandableView.setExpanded(false);
        });

    }

    private void getPageData() {
        addNewItemViewModel.getAddNewPageData(getActivity()).observe(getViewLifecycleOwner(), response -> {
            if (response == null) {
                errorMessage(getActivity().getApplication(), "Something Wrong");
                return;
            }
            if (response.getStatus() == 400) {
                infoMessage(getActivity().getApplication(), "" + response.getMessage());
                return;
            }

            unitResponseList = new ArrayList<>();
            unitResponseList.clear();
            unitNameList = new ArrayList<>();
            unitResponseList.clear();
            unitResponseList.addAll(response.getUnit());
            for (int i = 0; i < response.getUnit().size(); i++) {
                unitNameList.add(response.getUnit().get(i).getName());
            }
            binding.unit.setItem(unitNameList);
        });
    }

    private void getPreviousFragment() {
        refProductID = getArguments().getString("id");
        pageName = getArguments().getString("pageName");
        title = getArguments().getString("title");
    }

    private void getVarientList() {
        if (!new InternetCheckerRecyclerBuddy(getActivity()).isInternetAvailableHere(binding.varientRv, binding.noDataFound)) {
            return;
        }
        varientViewModel.getVarientList(getActivity(), refProductID).observe(getViewLifecycleOwner(), response -> {
            if (response == null || response.getStatus() == 400 || response.getStatus() == 500) {
                errorMessage(getActivity().getApplication(), "Something wrong");
                return;
            }

            if (response.getStatus() == 200) {
                if (response.getList().isEmpty()) {
                    binding.varientRv.setVisibility(View.GONE);
                    binding.noDataFound.setVisibility(View.VISIBLE);
                    return;
                }
                VarientlistAdapter adapter = new VarientlistAdapter(getContext(), response.getList(), VarientlistFragment.this);
                binding.varientRv.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.varientRv.setAdapter(adapter);
            }

        });
    }

    @Override
    public void addVarient(int podition, String productTitle, String productId) {

    }
}