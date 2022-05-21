package com.rupayan_housing.view.fragment.notificationsManage;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.bumptech.glide.Glide;

import com.google.gson.Gson;
import com.rupayan_housing.R;
import com.rupayan_housing.adapter.NotificationPendingPurchaseAdapter;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.DuePaymentResponse;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.utils.UrlUtil;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.view.fragment.DateFormatRight;
import com.rupayan_housing.viewModel.ApproveDeclinePendingPurchaseViewModel;
import com.rupayan_housing.viewModel.PendingPurchaseApproveDeclineViewModel;
import com.rupayan_housing.viewModel.PendingSaleApproveDeclineViewModel;
import com.rupayan_housing.viewModel.PermissionViewModel;
import com.rupayan_housing.viewModel.SalesReturnViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class PendingPurchaseDetailsFragment extends BaseFragment {
    private View view;
    private PendingPurchaseApproveDeclineViewModel pendingPurchaseApproveDeclineViewModel;
    private ApproveDeclinePendingPurchaseViewModel approveDeclinePendingPurchaseViewModel;
    private PendingSaleApproveDeclineViewModel pendingSaleApproveDeclineViewModel;
    private SalesReturnViewModel salesReturnViewModel;
    private PermissionViewModel permissionViewModel;
    @BindView(R.id.poNoTv)
    TextView poNoTv;
    @BindView(R.id.orderSerial)
    TextView orderSerial;
    @BindView(R.id.poDateTv)
    TextView poDateTv;
    @BindView(R.id.processByIcon)
    CircularImageView processByIcon;
    @BindView(R.id.processBynameTv)
    TextView processBynameTv;
    @BindView(R.id.uniquePortion)
    TextView uniquePortion;
    //
    @BindView(R.id.supplierNameTv)
    TextView supplierNameTv;
    @BindView(R.id.totalQuantity)
    TextView totalQuantityTv;
    @BindView(R.id.companyNameTv)
    TextView companyNameTv;
    @BindView(R.id.supplierPhoneTv)
    TextView supplierPhoneTv;
    @BindView(R.id.addressTv)
    TextView addressTv;
    @BindView(R.id.productListRv)
    RecyclerView productListRv;
    @BindView(R.id.NoteEt)
    EditText noteEt;
    @BindView(R.id.toolbarTitle)
    TextView toolbar;
    @BindView(R.id.paymentTypeTv)
    TextView paymentTypeTv;
    @BindView(R.id.totalAmountTv)
    TextView totalAmountTv;
    @BindView(R.id.collectedAmountTv)
    TextView collectedAmountTv;
    @BindView(R.id.procedureLAyout)
    LinearLayout procedureLAyout;
    @BindView(R.id.optionalStatus)
    LinearLayout optionalStatus;//for handle approve and decline functionality
    TextView textView;
    String typeKey, refOrderId, OrderSerialId, pageName, portion, porson, status, orderVendorId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pending_purchase_details, container, false);
        ButterKnife.bind(this, view);
        pendingPurchaseApproveDeclineViewModel = ViewModelProviders.of(this).get(PendingPurchaseApproveDeclineViewModel.class);
        approveDeclinePendingPurchaseViewModel = ViewModelProviders.of(this).get(ApproveDeclinePendingPurchaseViewModel.class);
        pendingSaleApproveDeclineViewModel = ViewModelProviders.of(this).get(PendingSaleApproveDeclineViewModel.class);
        salesReturnViewModel = new ViewModelProvider(this).get(SalesReturnViewModel.class);
        getDataFromPreviousFragment();

        textView = view.findViewById(R.id.uniqueOrderId);

        /**
         * approve and decline option will be VISIBLE only for profile id 7
         */
//        if (!getProfileTypeId(getActivity().getApplication()).equals("7")) {
//            view.findViewById(R.id.approveBtn).setVisibility(View.GONE);
//            view.findViewById(R.id.declineBtn).setVisibility(View.GONE);
//        }

        /**
         * now load data to recyclerView
         */
        if (portion.equals("PENDING_PURCHASE")) {

            if (status == null) {
                optionalStatus.setVisibility(View.GONE);
            }

            if (status != null) {
                if (status.equals("2")) {
                    try {
//                        if (getProfileTypeId(getActivity().getApplication()).equals("7")) {
//                            if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1) ||//manageALlPreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions().contains("1")
//                                    PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1429)) {//here 1429 is permission for approve decline pending Purchase
//                                optionalStatus.setVisibility(View.VISIBLE);
//                            } else {
//                                optionalStatus.setVisibility(View.GONE);
//                            }
//                        } else {
//                            optionalStatus.setVisibility(View.GONE);
//                        }

                        if ( //manageALlPreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions().contains("1")
                                PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1429)) {//here 1429 is permission for approve decline pending Purchase
                            optionalStatus.setVisibility(View.VISIBLE);
                        }


                    } catch (Exception e) {
                        Log.d("ERROR", "" + e.getMessage());
                    }
                }
            }

            if (porson != null) {
                if (porson.equals("PurchaseHistoryDetails")) {
                    optionalStatus.setVisibility(View.GONE);
                    if (status != null) {
                        if (status.equals("2")) {
                            optionalStatus.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
            loadPurchaseDataToView();

        }
        if (portion.equals("PENDING_SALE")) {
            textView.setText("S.O ID");
            uniquePortion.setText("Customer Details");

            if (status == null) {//here status for handle pending or details view from notificationListAdapter
                optionalStatus.setVisibility(View.GONE);
            }

            if (status != null) {
                if (status.equals("2")) {
                    try {
//                        if (getProfileTypeId(getActivity().getApplication()).equals("7")) {//here 1428 is permission for approve decline pending sale
//                            if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1) ||//manageALlPreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions().contains("1")
//                                    PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1428)) {
//                                optionalStatus.setVisibility(View.VISIBLE);
//                            } else {
//                                optionalStatus.setVisibility(View.GONE);
//                            }
//                        } else {
//                            optionalStatus.setVisibility(View.GONE);
//                        }

                        if ( //manageALlPreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions().contains("1")
                                PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1428)) {
                            optionalStatus.setVisibility(View.VISIBLE);
                        }

                    } catch (Exception e) {
                        Log.d("ERROR", "" + e.getMessage());
                    }
                }
            }

            if (porson != null) {
                if (porson.equals("SalePendingDetails")) {
                    textView.setText("S.O ID");
                    //    toolbar.setText("Sale Pending Details");
                    optionalStatus.setVisibility(View.GONE);
                    if (status != null) {
                        if (status.equals("2")) {
                            /*if (getProfileTypeId(getActivity().getApplication()).equals("7")) {//here 1428 is permission for approve decline pending sale
                                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1) ||//manageALlPreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions().contains("1")
                                        PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1428)) {
                                    optionalStatus.setVisibility(View.VISIBLE);
                                } else {
                                    optionalStatus.setVisibility(View.GONE);
                                }
                            } else {
                                optionalStatus.setVisibility(View.GONE);
                            }*/
                            if ( //manageALlPreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions().contains("1")
                                    PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1428)) {
                                optionalStatus.setVisibility(View.VISIBLE);
                            }

                        } else {
                            optionalStatus.setVisibility(View.GONE);
                        }
                    }
                }
                if (porson.equals("SaleHistoryDetails")) {
                    textView.setText("S.O ID");
                    //  toolbar.setText("Sale History Details");
                    optionalStatus.setVisibility(View.GONE);
                    if (status != null) {
                        if (status.equals("2")) {
                            optionalStatus.setVisibility(View.VISIBLE);
                        }
                    }
                }
                if (porson.equals("Sale_Declined_Details")) {
                    textView.setText("S.O ID");
                    procedureLAyout.setVisibility(View.GONE);
                    //   toolbar.setText("Sale Declined Details");
                    optionalStatus.setVisibility(View.GONE);
                    if (status != null) {
                        if (status.equals("2")) {
                            optionalStatus.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            loadSalesDataToView();

        }

        if (portion.equals("SALES_WHOLE_ORDER_CANCEL")) {
            if (status != null) {
//                if (getProfileTypeId(getActivity().getApplication()).equals("7")) {
//                    if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1) ||
//                            PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1311)) {
//                        optionalStatus.setVisibility(View.VISIBLE);
//                    } else {
//                        optionalStatus.setVisibility(View.GONE);
//                    }
//                } else {
//                    optionalStatus.setVisibility(View.GONE);
//                }
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1311)) {
                    optionalStatus.setVisibility(View.VISIBLE);
                }
                loadSalesWholeOrderCancelDetails();
            } else {
                loadSalesWholeOrderCancelDetailsNew();
            }
        }

        return view;
    }

    private void loadSalesWholeOrderCancelDetailsNew() {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check your Internet Connection");
            return;
        }
        String vendorId = null;
        if (orderVendorId != null) {
            vendorId = orderVendorId;
        } else {
            vendorId = PreferenceManager.getInstance(getContext()).getUserCredentials().getVendorID();
        }
        pendingSaleApproveDeclineViewModel.getPendingSalesReturnNotificationDetailsNew(getActivity(), refOrderId, vendorId)
                .observe(getViewLifecycleOwner(), response -> {
                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "Something Wrong");
                        return;
                    }
                    if (response.getStatus() == 400) {
                        infoMessage(getActivity().getApplication(), "" + response.getMessage());
                        return;
                    }
                    try {
                        Log.d("DATA", new Gson().toJson(response));
                        poNoTv.setText(":  " + refOrderId + " (Order serial ID)");
                        if (response.getOrderInfo().getOrder_serial() == null) {
                            orderSerial.setText(":  ");
                        } else {
                            orderSerial.setText(":  " + response.getOrderInfo().getOrder_serial() + " (Sale order ID)");

                        }
                        poDateTv.setText(":  " + new DateFormatRight(getActivity(), response.getOrderInfo().getOrderDate()).onlyDayMonthYear());

                        Glide.with(getContext())
                                .load(UrlUtil.profileBaseUrl + "" + response.getProcessedBy().getProfilePhoto())
                                .error(R.drawable.erro_logo)
                                .placeholder(R.drawable.erro_logo)
                                .into(processByIcon);
                        processBynameTv.setText(response.getProcessedBy().getFullName());
                        /**
                         * now set supplier part
                         */
                        supplierNameTv.setText(":  " + response.getCustomer().getCustomerFname());
                        companyNameTv.setText(":  " + response.getCustomer().getCompanyName());
                        supplierPhoneTv.setText(":  " + response.getCustomer().getPhone());
                        addressTv.setText(":  " + response.getCustomer().getAddress());

                        /**
                         * now load data to recycler view
                         */
                        String enterPriseName = response.getEnterprise_name();
                        /**
                         * now set data list to recycler view
                         */
                        if (response.getItems() != null || !response.getItems().isEmpty()) {
                            double totalQuantity = 0.0;
                            for (int i = 0; i < response.getItems().size(); i++) {
                                totalQuantity += Double.parseDouble(response.getItems().get(i).getQuantity());
                            }
                            totalQuantityTv.setText(":  " + totalQuantity + " " + response.getItems().get(0).getUnit());

                            NotificationPendingPurchaseAdapter adapter = new NotificationPendingPurchaseAdapter(getActivity(), response.getItems(), enterPriseName);
                            productListRv.setLayoutManager(new LinearLayoutManager(getContext()));
                            productListRv.setAdapter(adapter);
                        }
                        /**
                         * get data from adapter
                         */

                        double total = 0;
                        for (int i = 0; i < response.getItems().size(); i++) {
                            try {
                                total += Double.parseDouble(response.getItems().get(i).getBuyingPrice()) * Double.parseDouble(response.getItems().get(i).getQuantity());
                            } catch (Exception e) {

                            }
                        }


                        totalAmountTv.setText(":  " + NotificationPendingPurchaseAdapter.totalAmount + "TK");
                        totalAmountTv.setText(":  " + total + "TK");
                        paymentTypeTv.setText(":  " + response.getPayment_type());

                    } catch (Exception e) {
                        Log.d("ERROR", "" + e.getMessage());
                        orderSerial.setText(":  ");
                        poDateTv.setText(":  ");

                    }
                });
    }

    private void loadSalesWholeOrderCancelDetails() {
        loadSalesDataToView();
    }

    private void loadSalesDataToView() {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check your Internet Connection");
            return;
        }
        String vendorId = null;
        if (orderVendorId != null) {
            vendorId = orderVendorId;
        } else {
            vendorId = PreferenceManager.getInstance(getContext()).getUserCredentials().getVendorID();
        }
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        pendingSaleApproveDeclineViewModel.getPendingNotificationDetails(getActivity(), refOrderId, vendorId)
                .observe(getViewLifecycleOwner(), response -> {
                    progressDialog.dismiss();
                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "Something Wrong");
                        return;
                    }
                    if (response.getStatus() == 400) {
                        infoMessage(getActivity().getApplication(), "" + response.getMessage());
                        return;
                    }
                    try {
                        Log.d("DATA", new Gson().toJson(response));
                        poNoTv.setText(":  " + refOrderId + " (Order serial ID)");
                        if (response.getOrderInfo().getOrder_serial() == null) {
                            orderSerial.setText(":  ");
                        } else {
                            orderSerial.setText(":  " + response.getOrderInfo().getOrder_serial() + " (Sale order ID)");

                        }
                        poDateTv.setText(":  " + new DateFormatRight(getActivity(), response.getOrderInfo().getOrderDate()).onlyDayMonthYear());

                        Glide.with(getContext())
                                .load(UrlUtil.profileBaseUrl + "" + response.getProcessedBy().getProfilePhoto())
                                .error(R.drawable.erro_logo)
                                .placeholder(R.drawable.erro_logo)
                                .into(processByIcon);
                        processBynameTv.setText(response.getProcessedBy().getFullName());
                        /**
                         * now set supplier part
                         */
                        supplierNameTv.setText(":  " + response.getCustomer().getCustomerFname());
                        companyNameTv.setText(":  " + response.getCustomer().getCompanyName());
                        supplierPhoneTv.setText(":  " + response.getCustomer().getPhone());
                        addressTv.setText(":  " + response.getCustomer().getAddress());
                        /**
                         * now set the majhi/driver part
                         */
                        //  majhiNameTv.setText(response.getOtherInfo().getDriverName());
                        //majhiPhoneTv.setText(response.getOtherInfo().getPhone());
                   /* transPortNameTv.setText(response.getOtherInfo().getCourrierName());
                    vehicleNoTv.setText(response.getOtherInfo().getLicenseNo());*/
                        /**
                         * now load data to recycler view
                         */
                        String enterPriseName = response.getEnterprise_name();
                        /**
                         * now set data list to recycler view
                         */
                        if (response.getItems() != null || !response.getItems().isEmpty()) {
                            double totalQuantity = 0.0;
                            for (int i = 0; i < response.getItems().size(); i++) {
                                totalQuantity += Double.parseDouble(response.getItems().get(i).getQuantity());
                            }
                            totalQuantityTv.setText(":  " + totalQuantity + " " + response.getItems().get(0).getUnit());

                            NotificationPendingPurchaseAdapter adapter = new NotificationPendingPurchaseAdapter(getActivity(), response.getItems(), enterPriseName);
                            productListRv.setLayoutManager(new LinearLayoutManager(getContext()));
                            productListRv.setAdapter(adapter);
                        }
                        /**
                         * get data from adapter
                         */

                        double total = 0;
                        for (int i = 0; i < response.getItems().size(); i++) {
                            try {
                                total += Double.parseDouble(response.getItems().get(i).getBuyingPrice()) * Double.parseDouble(response.getItems().get(i).getQuantity());
                            } catch (Exception e) {

                            }
                        }
                        totalAmountTv.setText(":  " + NotificationPendingPurchaseAdapter.totalAmount + "TK");
                        totalAmountTv.setText(":  " + total + "TK");
                        paymentTypeTv.setText(":  " + response.getPayment_type());

                    } catch (Exception e) {
                        Log.d("ERROR", "" + e.getMessage());
                        orderSerial.setText(":  ");
                        poDateTv.setText(":  ");
                        progressDialog.dismiss();

                    }
                });
    }


    private void getDataFromPreviousFragment() {
        //typeKey = getArguments().getString("TypeKey");
        refOrderId = getArguments().getString("RefOrderId");
        OrderSerialId = getArguments().getString("OrderSerialId");
        pageName = getArguments().getString("pageName");
        porson = getArguments().getString("porson");///this porson value get from dash bord all needed view details
        portion = getArguments().getString("portion");//here portion means action come from.....
        status = getArguments().getString("status");
        orderVendorId = getArguments().getString("orderVendorId");
        toolbar.setText("" + pageName);
    }

    private void loadPurchaseDataToView() {
        String vendorId = null;
        if (orderVendorId != null) {
            vendorId = orderVendorId;
        } else {
            vendorId = PreferenceManager.getInstance(getContext()).getUserCredentials().getVendorID();
        }
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        pendingPurchaseApproveDeclineViewModel.getPendingPurchaseNotificationDetails(getActivity(), refOrderId, vendorId)
                .observe(getViewLifecycleOwner(), response -> {
                    progressDialog.dismiss();
                    try {
                        if (response == null) {
                            errorMessage(getActivity().getApplication(), "Something Wrong");
                            return;
                        }
                        if (response.getStatus() == 400) {
                            infoMessage(getActivity().getApplication(), "" + response.getMessage());
                            return;
                        }
                        Log.d("DATA", new Gson().toJson(response));
                        poNoTv.setText(":  " + response.getOrderInfo().getOrder_serial() + " (Purchase order ID)");
                        orderSerial.setText(":  " + refOrderId + " (Order serial ID)");


                        poDateTv.setText(":  " + new DateFormatRight(getActivity(), response.getOrderInfo().getOrderDate()).onlyDayMonthYear());
                        Glide.with(getContext())
                                .load(UrlUtil.profileBaseUrl + "" + response.getProcessedBy().getProfilePhoto())
                                .error(R.drawable.erro_logo)
                                .placeholder(R.drawable.erro_logo)
                                .into(processByIcon);
                        processBynameTv.setText(response.getProcessedBy().getFullName());
                        /**
                         * now set supplier part
                         */
                        supplierNameTv.setText(":  " + response.getCustomer().getCustomerFname());
                        companyNameTv.setText(":  " + response.getCustomer().getCompanyName());
                        supplierPhoneTv.setText(":  " + response.getCustomer().getPhone());
                        addressTv.setText(":  " + response.getCustomer().getAddress());

                        /**
                         * now load data to recycler view
                         */
                        String enterPriseName = response.getEnterprise_name();
                        if (response.getItems() != null || !response.getItems().isEmpty()) {
                            double totalQuantity = 0.0;
                            for (int i = 0; i < response.getItems().size(); i++) {
                                totalQuantity += Double.parseDouble(response.getItems().get(i).getQuantity());
                            }
                            totalQuantityTv.setText(":  " + totalQuantity + " " + response.getItems().get(0).getUnit());

                            NotificationPendingPurchaseAdapter adapter = new NotificationPendingPurchaseAdapter(getActivity(), response.getItems(), enterPriseName);
                            productListRv.setLayoutManager(new LinearLayoutManager(getContext()));
                            productListRv.setAdapter(adapter);
                        }

                        double total = 0;
                        for (int i = 0; i < response.getItems().size(); i++) {
                            try {
                                total += Double.parseDouble(response.getItems().get(i).getBuyingPrice()) * Double.parseDouble(response.getItems().get(i).getQuantity());

                            } catch (Exception e) {
                                Log.d("ERROR", "" + e.getLocalizedMessage());
                            }
                        }
                        totalAmountTv.setText(":  " + total + " Tk");
                        paymentTypeTv.setText(":  " + response.getPayment_type());

                    } catch (Exception e) {
                        Log.d("ERROR", "" + e.getMessage());
                        progressDialog.dismiss();
                    }
                });
    }


    @OnClick(R.id.approveBtn)
    public void approveBtnClick() {

        hideKeyboard(getActivity());
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check your Internet Connection");
            return;
        }
        String note = "";
        if (noteEt.getText().toString().isEmpty()) {
            infoMessage(getActivity().getApplication(), "Please type a short note");

            return;
        }
        if (portion.equals("PENDING_PURCHASE")) {
            confirmApprovePendingPurchaseDialog();
        }

        if (portion.equals("PENDING_SALE")) {
            confirmApprovePendingSaleDialog();
        }
        if (portion.equals("SALES_WHOLE_ORDER_CANCEL")) {
            String currentVendorId = null;
            if (orderVendorId != null) {
                currentVendorId = orderVendorId;
            } else {
                currentVendorId = PreferenceManager.getInstance(getContext()).getUserCredentials().getVendorID();
            }

            ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.show();
            salesReturnViewModel.approveSalesReturnWholeOrderCancel(getActivity(), note, refOrderId, currentVendorId)
                    .observe(getViewLifecycleOwner(), response -> {
                        progressDialog.dismiss();
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

    }

    @OnClick(R.id.declineBtn)
    public void declineBtnClick() {
        String note = noteEt.getText().toString();
        if (note.isEmpty()) {
            noteEt.setError("Note is Mandatory");
            noteEt.requestFocus();
            return;
        }
        hideKeyboard(getActivity());
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check your Internet Connection");
            return;
        }
        if (portion.equals("PENDING_PURCHASE")) {
            confirmDeclinePendingPurchaseDialog();
            return;
        }
        if (portion.equals("PENDING_SALE")) {
            confirmDeclinePendingSaleDialog();
            return;
        }
        if (portion.equals("SALES_WHOLE_ORDER_CANCEL")) {
            if (!(isInternetOn(getActivity()))) {
                infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
                return;
            }

            String currentVendorId = null;
            if (orderVendorId != null) {
                currentVendorId = orderVendorId;
            } else {
                currentVendorId = PreferenceManager.getInstance(getContext()).getUserCredentials().getVendorID();
            }
            ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.show();
            salesReturnViewModel.declineApproveSalesReturnWholeOrderCancel(getActivity(), note, refOrderId, currentVendorId)
                    .observe(getViewLifecycleOwner(), response -> {
                        progressDialog.dismiss();
                        if (response == null) {
                            errorMessage(getActivity().getApplication(), "Something Wrong");
                            return;
                        }
                        if (response.getStatus() == 400) {
                            infoMessage(getActivity().getApplication(), "" + response.getStatus());
                            return;
                        }
                        successMessage(getActivity().getApplication(), "" + response.getMessage());
                        getActivity().onBackPressed();
                    });
        }

    }

    @OnClick(R.id.backbtn)
    public void onBackBtnClick() {
        hideKeyboard(getActivity());
        getActivity().onBackPressed();
    }


    public void confirmApprovePendingPurchaseDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Alert");
        alertDialog.setIcon(R.drawable.warning_btn);
        alertDialog.setMessage("Do you want to Approve ?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                (dialog, which) -> {
                    dialog.dismiss();
                    approveDeclinePendingPurchaseViewModel.pendingPurchaseApproveRequest(getActivity(), refOrderId, noteEt.getText().toString())
                            .observe(getViewLifecycleOwner(), duePaymentResponse -> {
                                Toasty.success(getContext(), "Pending Purchase Approved", Toasty.LENGTH_LONG).show();
                                getActivity().onBackPressed();
                            });

                });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }

    public void confirmDeclinePendingPurchaseDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Alert");
        alertDialog.setIcon(R.drawable.warning_btn);
        alertDialog.setMessage("Do you want to decline ?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                (dialog, which) -> {
                    approveDeclinePendingPurchaseViewModel.pendingPurchaseDeclineRequest(getActivity(), refOrderId, noteEt.getText().toString())
                            .observe(getViewLifecycleOwner(), duePaymentResponse -> {
                                Toasty.success(getContext(), "Pending Purchase Decline", Toasty.LENGTH_LONG).show();
                                getActivity().onBackPressed();
                            });
                });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }


    public void confirmApprovePendingSaleDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Alert");
        alertDialog.setIcon(R.drawable.warning_btn);
        alertDialog.setMessage("Do you want to Approve ?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                (dialog, which) -> {
                    dialog.dismiss();
                    pendingSaleApproveDeclineViewModel.pendingSalesApproveRequest(getActivity(), refOrderId, noteEt.getText().toString())
                            .observe(getViewLifecycleOwner(), duePaymentResponse -> {
                                Toasty.success(getContext(), "Pending Purchase Approved", Toasty.LENGTH_LONG).show();
                                getActivity().onBackPressed();
                            });
                });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }

    public void confirmDeclinePendingSaleDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Alert");
        alertDialog.setIcon(R.drawable.warning_btn);
        alertDialog.setMessage("Do you want to decline ?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                (dialog, which) -> {
                    pendingSaleApproveDeclineViewModel.pendingSalesDeclineRequest(getActivity(), refOrderId, noteEt.getText().toString())
                            .observe(getViewLifecycleOwner(), duePaymentResponse -> {
                                Toasty.success(getContext(), "Pending Purchase Decline", Toasty.LENGTH_LONG).show();
                                getActivity().onBackPressed();
                            });
                });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }


}