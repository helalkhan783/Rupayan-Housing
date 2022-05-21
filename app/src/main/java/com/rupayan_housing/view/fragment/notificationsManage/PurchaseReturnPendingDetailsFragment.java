package com.rupayan_housing.view.fragment.notificationsManage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.FragmentPurchaseReturnPendingDetailsBinding;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.PurchaseReturnPendingDetailsResponse;
import com.rupayan_housing.serverResponseModel.PurchaseReturnPendingOrderDetail;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.adapter.PurchaseReturnOrderDetailsAdapter;
import com.rupayan_housing.view.fragment.DateFormatRight;
import com.rupayan_housing.viewModel.PendingSalesReturnViewModel;
import com.rupayan_housing.viewModel.PurchaseReturnPendingDetailsViewModel;
import com.rupayan_housing.viewModel.PurchaseReturnViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class PurchaseReturnPendingDetailsFragment extends BaseFragment {
    private FragmentPurchaseReturnPendingDetailsBinding binding;
    private PendingSalesReturnViewModel pendingSalesReturnViewModel;
    private String id, orderVendorId, portion, pageName, enterprise, status, forHistoryLayout;
    private PurchaseReturnPendingDetailsViewModel purchaseReturnPendingDetailsViewModel;
    private PurchaseReturnViewModel purchaseReturnViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_purchase_return_pending_details, container, false);
        pendingSalesReturnViewModel = new ViewModelProvider(this).get(PendingSalesReturnViewModel.class);
        getPreviousFragmentData();
        purchaseReturnPendingDetailsViewModel = new ViewModelProvider(this).get(PurchaseReturnPendingDetailsViewModel.class);
        purchaseReturnViewModel = new ViewModelProvider(this).get(PurchaseReturnViewModel.class);
        binding.toolbar.setClickHandle(() -> {
            hideKeyboard(getActivity());
            getActivity().onBackPressed();
        });


        /**
         * Here check Permission for control/manage approve decline based on permission and portion
         */
        if (portion.equals("PENDING_PURCHASE")) {//for pending purchase return
            //check permission first
            checkPermission(1314);
            /**
             * now get purchase return details
             */
            getPurchaseReturnDetailsPageData();
        }
        if (portion.equals("SALES_RETURNS_DETAILS")) {
            //check permission first
            checkPermission(1311);
            /**
             * now get sale return details
             */
            getSaleReturnDetailsPageData();
        }


        /**
         * For Approve
         */
        binding.approveBtn.setOnClickListener(v -> {
            if (!(isInternetOn(getActivity()))) {
                infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
                return;
            }

            if (binding.NoteEt.getText().toString().isEmpty()) {
                binding.NoteEt.setError("Empty");
                binding.NoteEt.requestFocus();
                return;
            }


            /**
             * For PENDING_PURCHASE return
             */
            if (portion.equals("PENDING_PURCHASE")) {
                confirmApproveDialog("Do you want to approve ?");
                return;
            }
            /**
             * For Approve SALES_RETURNS_DETAILS
             */
            if (portion.equals("SALES_RETURNS_DETAILS")) {
                confirmApproveDialog("Do you want to approve ?");
                return;
            }
        });

        /**
         * For Decline pending purchase return
         */
        binding.declineBtn.setOnClickListener(v -> {
            if (!(isInternetOn(getActivity()))) {
                infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
                return;
            }
            if (binding.NoteEt.getText().toString().isEmpty()) {
                binding.NoteEt.setError("Empty");
                binding.NoteEt.requestFocus();
                return;
            }

            /**
             * For PENDING_PURCHASE
             */
            if (portion.equals("PENDING_PURCHASE")) {
                confirmDeclineDialog("Do you want to decline ?");
                return;
            }

            /**
             * For Decline SALES_RETURNS_DETAILS
             */
            if (portion.equals("SALES_RETURNS_DETAILS")) {
                confirmDeclineDialog("Do you want to approve ?");
                return;
            }
        });


        return binding.getRoot();
    }

    private void getSaleReturnDetailsPageData() {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check your Internet Connection");
            return;
        }
        String currentVendorId = null;
        if (orderVendorId != null) {
            currentVendorId = orderVendorId;
        } else {
            currentVendorId = PreferenceManager.getInstance(getContext()).getUserCredentials().getVendorID();
        }
        purchaseReturnPendingDetailsViewModel.getSaleReturnPendingDetails(getActivity(), currentVendorId, id)
                .observe(getViewLifecycleOwner(), response -> {
                    try {
                        if (response == null) {
                            errorMessage(getActivity().getApplication(), "Something Wrong");
                            return;
                        }
                        if (response.getStatus() == 400) {
                            infoMessage(requireActivity().getApplication(), response.getMessage());
                            return;
                        }
                        /**
                         * now set data to view
                         */
                        binding.poNoTv.setText(":  " + response.getOrderinfo().getOrderSerial());
                        binding.orderSerial.setText(":  " + response.getOrderinfo().getOrderID());
                        binding.poDateTv.setText(":  " + new DateFormatRight(getActivity(),response.getOrderinfo().getOrderDate()).onlyDayMonthYear());

                        binding.supplierNameTv.setText(":  " + response.getCustomer().getCustomerFname());
                        binding.companyNameTv.setText(":  " + response.getCustomer().getCompanyName());
                        binding.supplierPhoneTv.setText(":  " + response.getCustomer().getPhone());
                        binding.addressTv.setText(":  " + response.getCustomer().getAddress());


                        List<PurchaseReturnPendingOrderDetail> currentReturnList = new ArrayList<>();
                        List<PurchaseReturnPendingOrderDetail> currentQuantityList = new ArrayList<>();
                        List<PurchaseReturnPendingOrderDetail> alreadyReturnList = new ArrayList<>();
                        List<PurchaseReturnPendingOrderDetail> cancelOrderList = new ArrayList<>();

                        if (forHistoryLayout == null   ) {
                            binding.currentReturnAndQuantityListLAyout.setVisibility(View.GONE);
                            for (int i = 0; i < response.getOrderDetails().size(); i++) {
                                //405 means current return quantity
                                if (response.getOrderDetails().get(i).getSalesTypeID().equals("404")/* for sale **/) {
                                    currentReturnList.add(response.getOrderDetails().get(i));
                                }
                            }
                            if (currentReturnList.isEmpty() || currentReturnList == null) {
                                binding.currentReturnListRvPortion.setVisibility(View.GONE);
                                binding.currentReturnListRv.setVisibility(View.GONE);
                            }
                            if (!currentReturnList.isEmpty()) {
                                PurchaseReturnOrderDetailsAdapter adapter = new PurchaseReturnOrderDetailsAdapter(getActivity(), currentReturnList, response.getOrderinfo().getStoreName());
                                binding.currentReturnListRv.setLayoutManager(new LinearLayoutManager(getContext()));
                                binding.currentReturnListRv.setAdapter(adapter);
                            }


                            for (int i = 0; i < response.getOrderDetails().size(); i++) {//for purchase return
                                if (response.getOrderDetails().get(i).getSalesTypeID().equals("402")/* for sale **/) {
                                    currentQuantityList.add(response.getOrderDetails().get(i));
                                }
                            }
                            if (currentQuantityList.isEmpty()) {
                                binding.currentQuantityPortion.setVisibility(View.GONE);
                                binding.currentQuantity.setVisibility(View.GONE);
                            }
                            if (!currentQuantityList.isEmpty()) {
                                PurchaseReturnOrderDetailsAdapter adapter = new PurchaseReturnOrderDetailsAdapter(getActivity(), currentQuantityList, response.getOrderinfo().getStoreName());
                                binding.currentQuantity.setLayoutManager(new LinearLayoutManager(getContext()));
                                binding.currentQuantity.setAdapter(adapter);
                            }


                            for (int i = 0; i < response.getOrderDetails().size(); i++) {//for purchase return
                                if (response.getOrderDetails().get(i).getSalesTypeID().equals("406")/* for sale **/) {
                                    cancelOrderList.add(response.getOrderDetails().get(i));
                                }
                            }
                            if (cancelOrderList.isEmpty()) {
                                binding.cancelOrderId.setVisibility(View.GONE);
                                binding.pendingCancelOrderRv.setVisibility(View.GONE);
                            }
                            if (!cancelOrderList.isEmpty()) {
                                PurchaseReturnOrderDetailsAdapter adapter = new PurchaseReturnOrderDetailsAdapter(getActivity(), cancelOrderList, response.getOrderinfo().getStoreName());
                                binding.pendingCancelOrderRv.setLayoutManager(new LinearLayoutManager(getContext()));
                                binding.pendingCancelOrderRv.setAdapter(adapter);
                            }

                            for (int i = 0; i < response.getOrderDetails().size(); i++) {//for sales return
                                if (response.getOrderDetails().get(i).getSalesTypeID().equals("3")/* for sale **/) {
                                    alreadyReturnList.add(response.getOrderDetails().get(i));
                                }
                            }
                            if (alreadyReturnList.isEmpty() || alreadyReturnList == null) {
                                binding.alreadyReturnListRvPortion.setVisibility(View.GONE);
                                binding.alreadyReturnListRv.setVisibility(View.GONE);
                            }
                            if (!alreadyReturnList.isEmpty()) {
                                binding.alreadyReturnListRvPortion.setVisibility(View.VISIBLE);
                                binding.alreadyReturnListRv.setVisibility(View.VISIBLE);
                                PurchaseReturnOrderDetailsAdapter adapter = new PurchaseReturnOrderDetailsAdapter(getActivity(), alreadyReturnList, response.getOrderinfo().getStoreName());
                                binding.alreadyReturnListRv.setLayoutManager(new LinearLayoutManager(getContext()));
                                binding.alreadyReturnListRv.setAdapter(adapter);
                            }


                        }

                        if (forHistoryLayout.equals("1")) {

                            binding.currentReturnAndQuantityListLAyout.setVisibility(View.GONE);

                            for (int i = 0; i < response.getOrderDetails().size(); i++) {//for sales return
                                if (response.getOrderDetails().get(i).getSalesTypeID().equals("3")/* for sale **/) {
                                    alreadyReturnList.add(response.getOrderDetails().get(i));
                                }
                            }
                            if (alreadyReturnList.isEmpty() || alreadyReturnList == null) {
                                binding.alreadyReturnListRvPortion.setVisibility(View.GONE);
                                binding.alreadyReturnListRv.setVisibility(View.GONE);
                            }
                            if (!alreadyReturnList.isEmpty()) {
                                binding.alreadyReturnListRvPortion.setVisibility(View.VISIBLE);
                                binding.alreadyReturnListRv.setVisibility(View.VISIBLE);
                                PurchaseReturnOrderDetailsAdapter adapter = new PurchaseReturnOrderDetailsAdapter(getActivity(), alreadyReturnList, response.getOrderinfo().getStoreName());
                                binding.alreadyReturnListRv.setLayoutManager(new LinearLayoutManager(getContext()));
                                binding.alreadyReturnListRv.setAdapter(adapter);
                            }


                        }


                    } catch (Exception e) {
                        Log.d("Error", e.getMessage());
                    }


                });
    }

    private void checkPermission(int permissionKey) {
        if (status != null) {//status get from notification list adapter
            if (status.equals("2")) {
                try {
                    if (getProfileTypeId(getActivity().getApplication()).equals("7")) {
                        if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1) |
                                PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(permissionKey)) {
                            binding.approveDeclineOption.setVisibility(View.VISIBLE);
                        } else {
                            binding.approveDeclineOption.setVisibility(View.GONE);
                        }
                    } else {
                        binding.approveDeclineOption.setVisibility(View.GONE);
                    }
                } catch (Exception E) {
                    Log.d("ERROR", "" + E.getMessage());
                }
            }
        } else {
            binding.approveDeclineOption.setVisibility(View.GONE);
        }
    }

    private void getPurchaseReturnDetailsPageData() {
        String currentVendorId = null;
        if (orderVendorId != null) {
            currentVendorId = orderVendorId;
        } else {
            currentVendorId = PreferenceManager.getInstance(getContext()).getUserCredentials().getVendorID();
        }
        purchaseReturnPendingDetailsViewModel.getPurchaseReturnPendingDetails(getActivity(), currentVendorId, id).observe(getViewLifecycleOwner(), new Observer<PurchaseReturnPendingDetailsResponse>() {
            @Override
            public void onChanged(PurchaseReturnPendingDetailsResponse response) {
                try {

                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "Something Wrong");
                        return;
                    }
                    if (response.getStatus() == 400) {
                        infoMessage(requireActivity().getApplication(), response.getMessage());
                        return;
                    }
                    /**
                     * now set data to view
                     */
                    binding.poNoTv.setText(":  " + response.getOrderinfo().getOrderSerial());
                    binding.orderSerial.setText(":  " + response.getOrderinfo().getOrderID());
                    binding.poDateTv.setText(":  " + new DateFormatRight(getActivity(),response.getOrderinfo().getOrderDate()).onlyDayMonthYear());
                    binding.supplierNameTv.setText(":  " + response.getCustomer().getCustomerFname());
                    binding.companyNameTv.setText(":  " + response.getCustomer().getCompanyName());
                    binding.supplierPhoneTv.setText(":  " + response.getCustomer().getPhone());
                    binding.addressTv.setText(":  " + response.getCustomer().getAddress());

                    List<PurchaseReturnPendingOrderDetail> currentReturnList = new ArrayList<>();
                    List<PurchaseReturnPendingOrderDetail> currentQuantityList = new ArrayList<>();
                    List<PurchaseReturnPendingOrderDetail> alreadyReturnList = new ArrayList<>();
                    List<PurchaseReturnPendingOrderDetail> cancelOrderList = new ArrayList<>();
                    if (forHistoryLayout == null) {

                        for (int i = 0; i < response.getOrderDetails().size(); i++) {
                            //405 means current return quantity
                            if (response.getOrderDetails().get(i).getSalesTypeID().equals("405")) {
                                currentReturnList.add(response.getOrderDetails().get(i));
                            }
                        }
                        if (currentReturnList.isEmpty() || currentReturnList == null) {
                            binding.currentReturnListRvPortion.setVisibility(View.GONE);
                            binding.currentReturnListRv.setVisibility(View.GONE);
                        }
                        if (!currentReturnList.isEmpty()) {
                            PurchaseReturnOrderDetailsAdapter adapter = new PurchaseReturnOrderDetailsAdapter(getActivity(), currentReturnList, response.getOrderinfo().getStoreName());
                            binding.currentReturnListRv.setLayoutManager(new LinearLayoutManager(getContext()));
                            binding.currentReturnListRv.setAdapter(adapter);
                        }


                        for (int i = 0; i < response.getOrderDetails().size(); i++) {
                            if (response.getOrderDetails().get(i).getSalesTypeID().equals("401")) {
                                currentQuantityList.add(response.getOrderDetails().get(i));
                            }
                        }
                        if (currentQuantityList.isEmpty()) {
                            binding.currentQuantityPortion.setVisibility(View.GONE);
                            binding.currentQuantity.setVisibility(View.GONE);
                        }
                        if (!currentQuantityList.isEmpty()) {
                            PurchaseReturnOrderDetailsAdapter adapter = new PurchaseReturnOrderDetailsAdapter(getActivity(), currentQuantityList, response.getOrderinfo().getStoreName());
                            binding.currentQuantity.setLayoutManager(new LinearLayoutManager(getContext()));
                            binding.currentQuantity.setAdapter(adapter);
                        }

                        for (int i = 0; i < response.getOrderDetails().size(); i++) {
                            if (response.getOrderDetails().get(i).getSalesTypeID().equals("401")) {
                                cancelOrderList.add(response.getOrderDetails().get(i));
                            }
                        }
                        if (cancelOrderList.isEmpty()) {
                            binding.cancelOrderId.setVisibility(View.GONE);
                            binding.pendingCancelOrderRv.setVisibility(View.GONE);
                        }
                        if (!cancelOrderList.isEmpty()) {
                            PurchaseReturnOrderDetailsAdapter adapter = new PurchaseReturnOrderDetailsAdapter(getActivity(), cancelOrderList, response.getOrderinfo().getStoreName());
                            binding.pendingCancelOrderRv.setLayoutManager(new LinearLayoutManager(getContext()));
                            binding.pendingCancelOrderRv.setAdapter(adapter);
                        }

                        for (int i = 0; i < response.getOrderDetails().size(); i++) {
                            if (response.getOrderDetails().get(i).getSalesTypeID().equals("4")) {
                                alreadyReturnList.add(response.getOrderDetails().get(i));
                            }
                        }
                        if (alreadyReturnList.isEmpty() || alreadyReturnList == null) {
                            binding.alreadyReturnListRvPortion.setVisibility(View.GONE);
                            binding.alreadyReturnListRv.setVisibility(View.GONE);
                        }
                        if (!alreadyReturnList.isEmpty()) {
                            binding.alreadyReturnListRvPortion.setVisibility(View.VISIBLE);
                            binding.alreadyReturnListRv.setVisibility(View.VISIBLE);
                            PurchaseReturnOrderDetailsAdapter adapter = new PurchaseReturnOrderDetailsAdapter(getActivity(), alreadyReturnList, response.getOrderinfo().getStoreName());
                            binding.alreadyReturnListRv.setLayoutManager(new LinearLayoutManager(getContext()));
                            binding.alreadyReturnListRv.setAdapter(adapter);
                        }

                    }
                    if (forHistoryLayout.equals("1")) {
                        binding.currentReturnAndQuantityListLAyout.setVisibility(View.GONE);
                        for (int i = 0; i < response.getOrderDetails().size(); i++) {
                            if (response.getOrderDetails().get(i).getSalesTypeID().equals("4")) {
                                alreadyReturnList.add(response.getOrderDetails().get(i));
                            }
                        }
                        if (alreadyReturnList.isEmpty() || alreadyReturnList == null) {
                            binding.alreadyReturnListRvPortion.setVisibility(View.GONE);
                            binding.alreadyReturnListRv.setVisibility(View.GONE);
                        }
                        if (!alreadyReturnList.isEmpty()) {
                            binding.alreadyReturnListRvPortion.setVisibility(View.VISIBLE);
                            binding.alreadyReturnListRv.setVisibility(View.VISIBLE);
                            PurchaseReturnOrderDetailsAdapter adapter = new PurchaseReturnOrderDetailsAdapter(getActivity(), alreadyReturnList, response.getOrderinfo().getStoreName());
                            binding.alreadyReturnListRv.setLayoutManager(new LinearLayoutManager(getContext()));
                            binding.alreadyReturnListRv.setAdapter(adapter);
                        }

                    }


                } catch (Exception e) {
                    Log.d("Error", e.getMessage());
                }
            }
        });
    }

    private void getPreviousFragmentData() {
        id = getArguments().getString("RefOrderId");
        orderVendorId = getArguments().getString("orderVendorId");
        portion = getArguments().getString("portion");
        pageName = getArguments().getString("pageName");
        enterprise = getArguments().getString("enterprise");
        status = getArguments().getString("status");
        forHistoryLayout = getArguments().getString("forHistoryLayout");
        binding.toolbar.toolbarTitle.setText(pageName);
    }


    public void confirmApproveDialog(String message) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());

        @SuppressLint("InflateParams")
        View view = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.purchase_dialog, null);
        //Set the view
        builder.setView(view);
        TextView tvTitle, tvMessage;
        ImageView imageIcon = view.findViewById(R.id.img_icon);
        tvMessage = view.findViewById(R.id.tv_message);
        tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText(message);//set warning title
        tvMessage.setText("MIS ERP");
        imageIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.app_logo));//set warning image
        Button bOk = view.findViewById(R.id.btn_ok);
        Button cancel = view.findViewById(R.id.cancel);
        android.app.AlertDialog alertDialog = builder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        cancel.setOnClickListener(v -> alertDialog.dismiss());//for cancel
        bOk.setOnClickListener(v -> {
            alertDialog.dismiss();
            if (portion.equals("PENDING_PURCHASE")) {
                purchaseReturnViewModel.approvePurchaseReturn(getActivity(), id, binding.NoteEt.getText().toString())
                        .observe(getViewLifecycleOwner(), response -> {
                            if (response == null) {
                                errorMessage(getActivity().getApplication(), "Something Wrong");
                                return;
                            }
                            if (response.getStatus() == 400) {
                                infoMessage(getActivity().getApplication(), "" + response.getMessage());
                                return;
                            }
                            successMessage(getActivity().getApplication(), "" + response.getMessage());
                            getActivity().onBackPressed();
                        });
            }
            if (portion.equals("SALES_RETURNS_DETAILS")) {
                pendingSalesReturnViewModel.approvePendingSalesReturnDetails(getActivity(), id, binding.NoteEt.getText().toString())
                        .observe(getViewLifecycleOwner(), response -> {
                            if (response == null) {
                                errorMessage(getActivity().getApplication(), "Something Wrong");
                                return;
                            }
                            if (response.getStatus() == 400) {
                                infoMessage(getActivity().getApplication(), "" + response.getMessage());
                                return;

                            }
                            hideKeyboard(getActivity());
                            successMessage(requireActivity().getApplication(), "" + response.getMessage());
                            getActivity().onBackPressed();

                        });
            }

        });

        alertDialog.show();

        //----------------------------------//

    }

    public void confirmDeclineDialog(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Alert");
        alertDialog.setIcon(R.drawable.warning_btn);
        alertDialog.setMessage("" + message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                (dialog, which) -> {

                    if (portion.equals("PENDING_PURCHASE")) {

                        purchaseReturnViewModel.declinePurchaseReturn(getActivity(), id, binding.NoteEt.getText().toString())
                                .observe(getViewLifecycleOwner(), response -> {
                                    if (response == null) {
                                        errorMessage(getActivity().getApplication(), "Something Wrong");
                                        return;
                                    }
                                    if (response.getStatus() == 400) {
                                        infoMessage(getActivity().getApplication(), "" + response.getMessage());
                                        return;
                                    }
                                    successMessage(getActivity().getApplication(), "" + response.getMessage());
                                    getActivity().onBackPressed();
                                });

                        return;
                    }
                    if (portion.equals("SALES_RETURNS_DETAILS")) {
                        pendingSalesReturnViewModel.declinePendingSalesReturnDetails(getActivity(), id, binding.NoteEt.getText().toString())
                                .observe(getViewLifecycleOwner(), response -> {
                                    if (response == null) {
                                        errorMessage(getActivity().getApplication(), "Something Wrong");
                                        return;
                                    }
                                    if (response.getStatus() == 400) {
                                        infoMessage(getActivity().getApplication(), "" + response.getMessage());
                                        return;

                                    }
                                    hideKeyboard(getActivity());
                                    successMessage(requireActivity().getApplication(), "" + response.getMessage());
                                    getActivity().onBackPressed();

                                });
                        return;
                    }

                    dialog.dismiss();
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                (dialog, which) -> {
                    dialog.dismiss();
                });
        alertDialog.show();
    }


}