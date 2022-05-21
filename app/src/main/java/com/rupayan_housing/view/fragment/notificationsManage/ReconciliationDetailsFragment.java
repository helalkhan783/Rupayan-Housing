package com.rupayan_housing.view.fragment.notificationsManage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rupayan_housing.R;
import com.rupayan_housing.adapter.ReconciliationAdapter;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.view.fragment.DateFormatRight;
import com.rupayan_housing.viewModel.ReconciliationViewModel;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class ReconciliationDetailsFragment extends BaseFragment {
    private View view;
    private ReconciliationViewModel reconciliationViewModel;


    @BindView(R.id.toolbarTitle)
    TextView toolbar;

    @BindView(R.id.reconciliationNo)
    TextView reconciliationNo;
    @BindView(R.id.enterprise)
    TextView enterprise;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.note)
    TextView note;
    @BindView(R.id.itemRv)
    RecyclerView itemRv;
    @BindView(R.id.noteEt)
    EditText noteEt;
    @BindView(R.id.approveDeclineOption)
    LinearLayout approveDeclineOption;


    String orderId, status, portion,vendorId;//for store id from previous fragment


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reconciliation_details, container, false);
        ButterKnife.bind(this, view);
        reconciliationViewModel = ViewModelProviders.of(this).get(ReconciliationViewModel.class);
        /**
         * now get Data from previous fragment
         */
        nowGetDataFromPreviousFragment();
        /**
         * now get Data from server
         */
        getReconciliationDetailsFromServer();


        return view;
    }

    private void getReconciliationDetailsFromServer() {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }
        /**
         * Now Check Permission
         */
        if (status != null) {//status get from notification list adapter
            if (status.equals("2")) {//here 2 means pending washing & Crushing and this value comes from notification list adapter via bundle
                checkApproveDeclinePermission();
            }
        } else {
            approveDeclineOption.setVisibility(View.GONE);
        }
        String currentVendorId = null;
        if (vendorId != null) {
            currentVendorId = vendorId;
        } else {
            currentVendorId = PreferenceManager.getInstance(getContext()).getUserCredentials().getVendorID();
        }
        reconciliationViewModel.getReconciliationDetails(getActivity(), orderId, currentVendorId)
                .observe(getViewLifecycleOwner(), response -> {
                    reconciliationNo.setText(":  #" + response.getOrderInfo().getOrderID());
                    toolbar.setText(getArguments().getString("pageName")+" No "+response.getOrderInfo().getOrderID());

                    enterprise.setText(":  " + response.getEnterprise());
                    if (portion.equals("Pending Reconciliation Details")){

                        date.setText(":  " + new DateFormatRight(getActivity(),response.getOrderInfo().getOrderDate()).forReconciliationDayMonthYear() + " & " + response.getOrderInfo().getOrderTime());
                    }
                    if (portion.equals("Decline Reconciliation Details")){

                        date.setText(":  " + new DateFormatRight(getActivity(),response.getOrderInfo().getOrderDate()).forReconciliationDayMonthYear() + " & " + response.getOrderInfo().getOrderTime());
                    }

                    if (portion.equals("Reconciliation History Details")){

                        date.setText(":  " + new DateFormatRight(getActivity(),response.getOrderInfo().getOrderDate()).onlyDayMonthYear() + " & " + response.getOrderInfo().getOrderTime());
                    }

                    note.setText(":  " + response.getOrderInfo().getNote());
                    /**
                     * now set order list to RecyclerView
                     **/
                    ReconciliationAdapter adapter = new ReconciliationAdapter(getActivity(), response.getDetails());
                    itemRv.setLayoutManager(new LinearLayoutManager(getActivity()));
                    itemRv.setHasFixedSize(true);
                    itemRv.setAdapter(adapter);


                });
    }

    private void checkApproveDeclinePermission() {
       /* if (getProfileTypeId(getActivity().getApplication()).equals("7")) {
            if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1313)) {//here 1313 isa permission for approve and declined pending Reconciliation
                approveDeclineOption.setVisibility(View.VISIBLE);
            } else {
                approveDeclineOption.setVisibility(View.GONE);
            }
        } else {
            approveDeclineOption.setVisibility(View.GONE);
        }*/

            if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1313)) {//here 1313 isa permission for approve and declined pending Reconciliation
                approveDeclineOption.setVisibility(View.VISIBLE);
            } else {
                approveDeclineOption.setVisibility(View.GONE);
            }

    }

    private void nowGetDataFromPreviousFragment() {
        orderId = getArguments().getString("RefOrderId");
        status = getArguments().getString("status");
        vendorId = getArguments().getString("vendorId");
        portion = getArguments().getString("pageName");
    }


    @OnClick(R.id.approveBtn)
    public void approve() {


        String noteVal = noteEt.getText().toString();

        if (noteVal.isEmpty()) {
            noteEt.setError("Note Mandatory");
            noteEt.requestFocus();
            return;
        }
        confirmApproveDialog();
    }

    @OnClick(R.id.declineBtn)
    public void decline() {

        String noteVal = noteEt.getText().toString();

        if (noteVal.isEmpty()) {
            noteEt.setError("Note Mandatory");
            noteEt.requestFocus();
            return;
        }
        confirmDeclineDialog();

    }


    @OnClick(R.id.backbtn)
    public void backBtnClick() {
        getActivity().onBackPressed();
    }


    public void confirmApproveDialog() {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());

        @SuppressLint("InflateParams")
        View view = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.purchase_dialog, null);
        //Set the view
        builder.setView(view);
        TextView tvTitle, tvMessage;
        ImageView imageIcon = view.findViewById(R.id.img_icon);
        tvMessage = view.findViewById(R.id.tv_message);
        tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("Do You Want to approve it ?");//set warning title
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
            reconciliationViewModel.approveReconciliationDetails(getActivity(), orderId, noteEt.getText().toString())
                    .observe(getViewLifecycleOwner(), response -> {
                        Toasty.success(getActivity(), "Approved", Toasty.LENGTH_LONG).show();
                        getActivity().onBackPressed();
                    });
        });
        alertDialog.show();

    }

    public void confirmDeclineDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Alert");
        alertDialog.setIcon(R.drawable.warning_btn);
        alertDialog.setMessage("Do you want to decline ?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                (dialog, which) -> {
                    dialog.dismiss();
                    reconciliationViewModel.declineReconciliationDetails(getActivity(), orderId, noteEt.getText().toString())
                            .observe(getViewLifecycleOwner(), response -> {
                                Toasty.success(getActivity(), "Declined", Toasty.LENGTH_LONG).show();
                                getActivity().onBackPressed();
                            });
                });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }


}