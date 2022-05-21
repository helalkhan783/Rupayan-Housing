package com.rupayan_housing.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rupayan_housing.R;
import com.rupayan_housing.adapter.RequisitionListAdapter;
import com.rupayan_housing.viewModel.SalesRequisitionListViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SalesRequisitionListFragment extends Fragment {
    private SalesRequisitionListViewModel salesRequisitionListViewModel;
    private View view;

    @BindView(R.id.toolbarTitle)
    TextView toolBar;
    @BindView(R.id.requisitionListRv)
    RecyclerView requisitionListRv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sales_requisition_list, container, false);
        ButterKnife.bind(this, view);
        salesRequisitionListViewModel = ViewModelProviders.of(this).get(SalesRequisitionListViewModel.class);
        getDataFromPreviousFragment();
        /**
         * show data in recyclerView from server
         */
        showSalesRequisitionListInRecyclerView();
        return view;
    }

    private void getDataFromPreviousFragment() {
        toolBar.setText("Requisition List");
    }

    private void showSalesRequisitionListInRecyclerView() {
        requisitionListRv.setLayoutManager(new LinearLayoutManager(getContext()));
        salesRequisitionListViewModel.getSalesRequisitionList(getActivity()).observe(getViewLifecycleOwner(), salesRequisitionList -> {
            RequisitionListAdapter adapter = new RequisitionListAdapter(getActivity(), salesRequisitionList);
            requisitionListRv.setAdapter(adapter);
        });
    }

    @OnClick({R.id.backbtn})
    public void backBtnClick() {
        getActivity().onBackPressed();
    }
}