package com.rupayan_housing.view.fragment.notificationsManage;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rupayan_housing.R;
import com.rupayan_housing.adapter.PendingWashingAndCrushingAdapter;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.DuePaymentResponse;
import com.rupayan_housing.serverResponseModel.GetPendingWashingCrushingDetailsResponse;
import com.rupayan_housing.serverResponseModel.PendingWashingCrushingItem;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.view.fragment.DateFormatRight;
import com.rupayan_housing.viewModel.GetPendingWashingCrushingViewModel;
import com.rupayan_housing.viewModel.WashingAndCrushingViewmodel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class GetPendingWashingAndCrushingDetails extends BaseFragment {
    private View view;
    private GetPendingWashingCrushingViewModel getPendingWashingCrushingViewModel;
    private GetPendingWashingCrushingDetailsResponse pageData;
    private WashingAndCrushingViewmodel washingAndCrushingViewmodel;


    @BindView(R.id.toolbarTitle)
    TextView toolbar;

    @BindView(R.id.outputItem)
    TextView outputItem;
    @BindView(R.id.refferName)
    TextView referrerName;
    @BindView(R.id.phoneNum)
    TextView phone;
    @BindView(R.id.noteETT)
    TextView note;
    @BindView(R.id.washingAndCrushingNumber)
    TextView washingAndCrushingNumber;
    @BindView(R.id.dateTv)
    TextView date;
    @BindView(R.id.processByName)
    TextView processByName;
    @BindView(R.id.totalQuantity)
    TextView totalQuantityTv;
    @BindView(R.id.ItemRecyclerView)
    RecyclerView itemRecyclerView;
    @BindView(R.id.noteEt)
    EditText noteEt;
    @BindView(R.id.approveDeclineOption)
    LinearLayout approveDeclineOption;

    private String orderId, status, portion, vendorId;//store click order id from previous notification fragment

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_get_pending_washing_and_crushing_details, container, false);
        ButterKnife.bind(this, view);
        getPendingWashingCrushingViewModel = ViewModelProviders.of(this).get(GetPendingWashingCrushingViewModel.class);
        washingAndCrushingViewmodel = ViewModelProviders.of(this).get(WashingAndCrushingViewmodel.class);
        /**
         * now get data from previous fragment
         */
        getDataFromPreviousFragment();

        /**
         * approve and decline option will be VISIBLE only for profile id 7
         */
        if (!getProfileTypeId(getActivity().getApplication()).equals("7")) {
            view.findViewById(R.id.approveWashingAndCrushingDetails).setVisibility(View.GONE);
            view.findViewById(R.id.WashingAndCrushingDetailsDeclineBtn).setVisibility(View.GONE);
        }

        if (status != null) {//status get from notification list adapter
            if (status.equals("2")) {//here 2 means pending washing & Crushing and this value comes from notification list adapter via bundle
                checkApproveDeclinePermission();
            }
        } else {
            approveDeclineOption.setVisibility(View.GONE);
        }
        /**
         * if not come from notification
         */
        try {
            if (portion != null) {
                if (portion.equals("WASHING_&_CRUSHING_DETAILS")) {
                    approveDeclineOption.setVisibility(View.GONE);
                }
                if (portion.equals("PENDING_WASHING_&_CRUSHING_DETAILS")) {
                    checkApproveDeclinePermission();
                }


            }
        } catch (Exception e) {
            Log.d("ERROR", "" + e.getMessage());
        }

        if (vendorId != null) {//get details show request from dash board
try {
    setNotificationDashBoardDetailsToView(orderId, vendorId);

}catch (Exception e){}
        } else {//get details view request from notifications
            try {
                String vendorID = PreferenceManager.getInstance(getContext()).getUserCredentials().getVendorID();
                setNotificationDashBoardDetailsToView(orderId, vendorID);
            }catch (Exception e){}
        }

        return view;
    }

    private void checkApproveDeclinePermission() {
        if (getProfileTypeId(getActivity().getApplication()).equals("7")) {
            if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1) ||//manageALlPreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions().contains("1")
                    PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1430)) {//here 1430 isa permission for approve and declined pending washing and crushing
                approveDeclineOption.setVisibility(View.VISIBLE);
            } else {
                approveDeclineOption.setVisibility(View.GONE);
            }
        } else {
            approveDeclineOption.setVisibility(View.GONE);
        }
    }

    private void setNotificationDashBoardDetailsToView(String orderID, String vendorID) {
        /**
         * now get page info from server
         */
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        getPendingWashingCrushingViewModel.getPendingWashingDetails(getActivity(), orderID, vendorID)
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
                    pageData = response;//store the response for future approve and decline request
                    /**
                     * now set data to view
                     */
                    try {
                        outputItem.setText(":  " + response.getItems().getOutputItem());
                        referrerName.setText(":  " + response.getReferrer().getCustomerFname());
                        phone.setText(":  " + response.getReferrer().getPhone());
                        note.setText(":  " + response.getOrderInfo().getNote());

                        washingAndCrushingNumber.setText(":  " + response.getOrderInfo().getOrderID());
                        toolbar.setText(getArguments().getString("pageName")+" SL No. "+response.getOrderInfo().getOrderID());


                        date.setText(":  " + new DateFormatRight(getActivity(),response.getOrderInfo().getOrderDate()).onlyDayMonthYear()+ " & "+new DateFormatRight(getActivity(),null).onlyTime(response.getOrderInfo().getOrderTime()));
                        processByName.setText(":  " + response.getOrderInfo().getUserName());
                    } catch (Exception e) {
                        Log.d("ERROR", e.getMessage());
                    }
                    Log.d("RESPONSE", new Gson().toJson(response));
                    //  Toast.makeText(getContext(), "" + new Gson().toJson(response), Toast.LENGTH_SHORT).show();
                    /**
                     * now set item list in recyclerview
                     */
                    setDateToRv(response.getItems().getItems());
                   try {
                       double quantity = 0.0;
                       for (int i = 0; i < response.getItems().getItems().size(); i++) {
                           quantity = + Double.parseDouble(response.getItems().getItems().get(i).getQuantity());
                       }
                       totalQuantityTv.setText(""+quantity + "  "+response.getItems().getItems().get(0).getUnit());

                   }catch (Exception e){}
                });
    }

    private void setDateToRv(List<PendingWashingCrushingItem> items) {
       try {
           itemRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
           itemRecyclerView.setHasFixedSize(true);
           PendingWashingAndCrushingAdapter adapter = new PendingWashingAndCrushingAdapter(getActivity(), items);
           itemRecyclerView.setAdapter(adapter);
       }catch (Exception e){}
    }

    private void getDataFromPreviousFragment() {
        try {
           orderId = getArguments().getString("RefOrderId");
           status = getArguments().getString("status");
           portion = getArguments().getString("portion");//this portion originally comes from dashboard all needed washing crushing view details
           vendorId = getArguments().getString("vendorId");
       }catch (Exception e){}
    }

    @OnClick(R.id.backbtn)
    public void backVBtnClick() {
        getActivity().onBackPressed();
    }


    @OnClick(R.id.approveWashingAndCrushingDetails)
    public void WashingAndCrushingDetails() {
        String noteVal = noteEt.getText().toString();
        if (noteVal.isEmpty()) {
            noteEt.setError("Note Mandatory");
            noteEt.requestFocus();
            return;
        }
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check your Internet Connection");
            return;
        }
        hideKeyboard(getActivity());
        confirmApproveDialog();
    }

    @OnClick(R.id.WashingAndCrushingDetailsDeclineBtn)
    public void decline() {
        String noteVal = noteEt.getText().toString();
        if (noteVal.isEmpty()) {
            noteEt.setError("Note Mandatory");
            noteEt.requestFocus();
            return;
        }
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check your Internet Connection");
            return;
        }
        hideKeyboard(getActivity());
        confirmDeclineDialog();
    }


    public void confirmApproveDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Alert");
        alertDialog.setIcon(R.drawable.warning_btn);
        alertDialog.setMessage("Do you want to Approve ?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                (dialog, which) -> {
                    dialog.dismiss();
                    washingAndCrushingViewmodel.approveWashingAndCrushing(getActivity(), orderId, noteEt.getText().toString())
                            .observe(getViewLifecycleOwner(), response -> {
                                if (response == null) {
                                    errorMessage(getActivity().getApplication(), "Something Wrong");
                                    return;
                                }
                                if (response.getStatus() == 400) {
                                    infoMessage(getActivity().getApplication(), "" + response.getMessage());
                                    return;
                                }
                                if (response.getStatus() == 200) {
                                    Toasty.success(getContext(), "" + response.getMessage(), Toasty.LENGTH_LONG).show();
                                    getActivity().onBackPressed();
                                }
                            });


                });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }

    public void confirmDeclineDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Alert");
        alertDialog.setIcon(R.drawable.warning_btn);
        alertDialog.setMessage("Do you want to decline ?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                (dialog, which) -> {
                    washingAndCrushingViewmodel.declineWashingAndCrushing(getActivity(), orderId, noteEt.getText().toString())
                            .observe(getViewLifecycleOwner(), response -> {
                                if (response == null) {
                                    errorMessage(getActivity().getApplication(), "Something Wrong");
                                    return;
                                }
                                if (response.getStatus() == 400) {
                                    infoMessage(getActivity().getApplication(), "" + response.getMessage());
                                    return;
                                }
                                if (response.getStatus() == 200) {
                                    Toasty.success(getContext(), "" + response.getMessage(), Toasty.LENGTH_LONG).show();
                                    getActivity().onBackPressed();
                                }
                            });
                });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }


}