package com.rupayan_housing.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rupayan_housing.R;
import com.rupayan_housing.adapter.SalesRequisitionAdapter;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.utils.SalesRequisitionUtil;
import com.rupayan_housing.viewModel.PermissionViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SalesRequisitionManagementFragment extends Fragment {

    private PermissionViewModel permissionViewModel;
    View view;
    @BindView(R.id.toolbarTitle)
    TextView toolbar;
    @BindView(R.id.salesRequisitionAdapter)
    RecyclerView salesRequisitionRv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sales_requisition_management, container, false);
        ButterKnife.bind(this, view);
        permissionViewModel = ViewModelProviders.of(this).get(PermissionViewModel.class);
        getDataFromPreviousFragment();
        /**
         * now set data to recyclerview
         */
        setDataToRecyclerView();
        return view;
    }


    private void setDataToRecyclerView() {
        permissionViewModel.getAccountPermission(getActivity()).observe(getViewLifecycleOwner(), permissions -> {
            if (permissions.contains(PermissionUtil.manageAll)) {
                SalesRequisitionAdapter adapter = new SalesRequisitionAdapter(getActivity(), SalesRequisitionUtil.getAllSaleRequisitionTitle(), SalesRequisitionUtil.getAllSaleRequisitionImage());
                salesRequisitionRv.setLayoutManager(new GridLayoutManager(getContext(), 2));
                salesRequisitionRv.setAdapter(adapter);
            } else {
                /**
                 * for show add new requisition
                 */
                if (!permissions.contains(PermissionUtil.showAddNewSaleRequisition)) {
                    if (SalesRequisitionUtil.getAllSaleRequisitionImage().contains(SalesRequisitionUtil.newSaleImage)) {
                        SalesRequisitionUtil.getAllSaleRequisitionImage().remove(SalesRequisitionUtil.newSaleImage);
                    }
                    if (SalesRequisitionUtil.getAllSaleRequisitionTitle().contains(SalesRequisitionUtil.newSaleTitle)) {
                        SalesRequisitionUtil.getAllSaleRequisitionTitle().remove(SalesRequisitionUtil.newSaleTitle);
                    }
                }

                /**
                 * for show sales requisition list
                 */
                if (!permissions.contains(PermissionUtil.showSalesReqList)) {

                    if (SalesRequisitionUtil.getAllSaleRequisitionImage().contains(SalesRequisitionUtil.salesReqListImage)) {
                        SalesRequisitionUtil.getAllSaleRequisitionImage().remove(SalesRequisitionUtil.salesReqListImage);
                    }
                    if (SalesRequisitionUtil.getAllSaleRequisitionTitle().contains(SalesRequisitionUtil.salesReqListTitle)) {
                        SalesRequisitionUtil.getAllSaleRequisitionTitle().remove(SalesRequisitionUtil.salesReqListTitle);
                    }
                }

                /**
                 * for show sales pending requisition list
                 */
                if (!permissions.contains(PermissionUtil.showSalesPendingReqList)) {

                    if (SalesRequisitionUtil.getAllSaleRequisitionImage().contains(SalesRequisitionUtil.pendingReqListImage)) {
                        SalesRequisitionUtil.getAllSaleRequisitionImage().remove(SalesRequisitionUtil.pendingReqListImage);
                    }
                    if (SalesRequisitionUtil.getAllSaleRequisitionTitle().contains(SalesRequisitionUtil.pendingReqListTitle)) {
                        SalesRequisitionUtil.getAllSaleRequisitionTitle().remove(SalesRequisitionUtil.pendingReqListTitle);
                    }
                }

                /**
                 * for show sales declined requisition list
                 */
                if (!permissions.contains(PermissionUtil.showSalesDeclinedReqList)) {

                    if (SalesRequisitionUtil.getAllSaleRequisitionImage().contains(SalesRequisitionUtil.declinedReqListImage)) {
                        SalesRequisitionUtil.getAllSaleRequisitionImage().remove(SalesRequisitionUtil.declinedReqListImage);
                    }
                    if (SalesRequisitionUtil.getAllSaleRequisitionTitle().contains(SalesRequisitionUtil.declinedReqListTitle)) {
                        SalesRequisitionUtil.getAllSaleRequisitionTitle().remove(SalesRequisitionUtil.declinedReqListTitle);
                    }
                }


                SalesRequisitionAdapter adapter = new SalesRequisitionAdapter(getActivity(), SalesRequisitionUtil.getAllSaleRequisitionTitle(), SalesRequisitionUtil.getAllSaleRequisitionImage());
                salesRequisitionRv.setLayoutManager(new GridLayoutManager(getContext(), 2));
                salesRequisitionRv.setAdapter(adapter);
            }


        });

    }

    private void getDataFromPreviousFragment() {
        toolbar.setText(getArguments().getString("Item"));
    }


    @OnClick(R.id.backbtn)
    public void backBtnClick() {
        //getActivity().onBackPressed();
        getActivity().getOnBackPressedDispatcher().onBackPressed();
    }
}