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
import com.rupayan_housing.adapter.DeclinedRequisitionListAdapter;
import com.rupayan_housing.viewModel.DeclinedRequisitionListViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeclinedRequisitionListFragment extends Fragment {
    private DeclinedRequisitionListViewModel declinedRequisitionListViewModel;
    private View view;

    @BindView(R.id.toolbarTitle)
    TextView toolBar;
    @BindView(R.id.declinedRequisitionListRv)
    RecyclerView declinedRequisitionListRv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_declined_requisition_list, container, false);
        ButterKnife.bind(this, view);
        declinedRequisitionListViewModel = ViewModelProviders.of(this).get(DeclinedRequisitionListViewModel.class);
        getDataFromPreviousFragment();
        /**
         * show data in recyclerView from server
         */
        showDeclinedRequisitionListInRecyclerView();
        return view;

    }

    private void getDataFromPreviousFragment() {
        toolBar.setText("Declined Req. List");
    }

    private void showDeclinedRequisitionListInRecyclerView() {
        declinedRequisitionListRv.setLayoutManager(new LinearLayoutManager(getContext()));
        declinedRequisitionListViewModel.getDeclinedRequisitionList(getActivity()).observe(getViewLifecycleOwner(), declinedReqList -> {
            DeclinedRequisitionListAdapter adapter = new DeclinedRequisitionListAdapter(getActivity(), declinedReqList);
            declinedRequisitionListRv.setAdapter(adapter);
        });
    }

    @OnClick({R.id.backbtn})
    public void backBtnClick() {
        getActivity().onBackPressed();
    }
}