package com.rupayan_housing.view.fragment.notificationsManage;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
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

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.bumptech.glide.Glide;
import com.rupayan_housing.R;
import com.rupayan_housing.adapter.TransferDetailsItemAdapter;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.view.fragment.DateFormatRight;
import com.rupayan_housing.viewModel.TransferViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class TransferDetailsFragment extends BaseFragment {
    private View view;

    private TransferViewModel transferViewModel;

    @BindView(R.id.toolbarTitle)
    TextView toolbar;

    @BindView(R.id.transferNumber)
    TextView transferNumber;
    @BindView(R.id.referer)
    TextView referer;
    @BindView(R.id.transferredFrom)
    TextView transferredFrom;
    @BindView(R.id.transferredTo)
    TextView transferredTo;
    @BindView(R.id.dateTime)
    TextView dateTime;
    @BindView(R.id.processByImg)
    CircularImageView processByImg;
    @BindView(R.id.processByName)
    TextView processByName;

    @BindView(R.id.noteEt)
    TextView note;
    @BindView(R.id.transferItemsRV)
    RecyclerView transferItemsRV;
    @BindView(R.id.noDataFound)
    TextView noDataFound;
    @BindView(R.id.approveDeclineOption)
    LinearLayout approveDeclineOption;

    @BindView(R.id.noteEditText)
    EditText finalNote;
    String orderId, status, vendorId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_transfer_details, container, false);
        ButterKnife.bind(this, view);
        transferViewModel = ViewModelProviders.of(this).get(TransferViewModel.class);
        /**
         * for get data from previous fragment
         */
        getDataFromPreviousFragment();

        /**
         * approve and decline option will be VISIBLE only for profile id 7
         */


     /*   if (getProfileTypeId(getActivity().getApplication()).equals("7") &&
                (PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions().contains("1") ||//manageALlPreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions().contains("1")
                        PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions().contains("1312"))

        ) {
            view.findViewById(R.id.approveBtn).setVisibility(View.VISIBLE);
            view.findViewById(R.id.declineBtn).setVisibility(View.VISIBLE);
        } else {
            view.findViewById(R.id.approveBtn).setVisibility(View.GONE);
            view.findViewById(R.id.declineBtn).setVisibility(View.GONE);
        }*/

        if (  //manageALlPreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions().contains("1")
                PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions().contains("1312")) {
            view.findViewById(R.id.approveBtn).setVisibility(View.VISIBLE);
            view.findViewById(R.id.declineBtn).setVisibility(View.VISIBLE);
        } else {
            view.findViewById(R.id.approveBtn).setVisibility(View.GONE);
            view.findViewById(R.id.declineBtn).setVisibility(View.GONE);
        }

        /**
         * now get transfer details from server
         */
        getTransferDetailsFromServer();
        return view;
    }

    private void getTransferDetailsFromServer() {

        if (status != null) {//status get from notification list adapter
            if (status.equals("2")) {

                //here 2 means pending Iodization and this value comes from notification list adapter via bundle
//                if (getProfileTypeId(getActivity().getApplication()).equals("7")) {
//                    if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1) ||//manageALlPreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions().contains("1")
//                            PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1312)) {
//                        approveDeclineOption.setVisibility(View.VISIBLE);
//                    } else {
//                        approveDeclineOption.setVisibility(View.GONE);
//                    }
//                } else {
//                    approveDeclineOption.setVisibility(View.GONE);
//                }
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1312)) {
                    approveDeclineOption.setVisibility(View.VISIBLE);
                } else {
                    approveDeclineOption.setVisibility(View.GONE);
                }

            } else {
                approveDeclineOption.setVisibility(View.GONE);
            }
        } else {
            approveDeclineOption.setVisibility(View.GONE);
        }


        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }
        //  toolbar.setText("Transfer Details");
        String currentVendorId = null;
        if (vendorId != null) {
            currentVendorId = vendorId;
        } else {
            currentVendorId = PreferenceManager.getInstance(getContext()).getUserCredentials().getVendorID();
        }
        transferViewModel.getPendingTransferDetails(getActivity(), orderId, currentVendorId)
                .observe(getViewLifecycleOwner(), response -> {
                    try {
                        if (response == null) {
                            errorMessage(getActivity().getApplication(), "Something Wrong");
                            return;
                        }
                        if (response.getStatus() == 400) {
                            infoMessage(getActivity().getApplication(), "" + response.getMessage());
                            return;
                        }
                        transferNumber.setText(":  #" + orderId);
                        if (response.getCustomerInfo() != null) {
                            if (response.getCustomerInfo().getCustomerFname() == null) {
                                referer.setText(":  ");
                            } else {
                                referer.setText(":  " + response.getCustomerInfo().getCustomerFname());

                            }
                        } else referer.setText(":  ");
                        if (response.getTransferInfo().getOrderDate() != null) {
                            String date = response.getTransferInfo().getOrderDate() + " " + response.getTransferInfo().getOrderTime();

                            dateTime.setText(":  " + new DateFormatRight(getActivity(), date).dateFormatForWashing());
                        }
                        if (response.getTransferInfo().getNote() != null) {
                            note.setText(":  " + response.getTransferInfo().getNote());

                        }
                        processByName.setText(" " + response.getTransferInfo().getFullName());
                        if (response.getTransferFrom() == null) {
                            transferredFrom.setText(":  ");
                        } else {
                            transferredFrom.setText(":  " + response.getTransferFrom());

                        }
                        if (response.getTransferTo() == null) {
                            transferredTo.setText(":  ");
                        } else {
                            transferredTo.setText(":  " + response.getTransferTo());

                        }
                    } catch (Exception e) {
                        Log.d("ERROR", "" + e.getMessage());
                    }

                    Glide.with(this)
                            .load(response.getTransferInfo().getProfilePhoto())
                            .placeholder(R.drawable.erro_logo)
                            .error(R.drawable.erro_logo)
                            .override(200, 200)
                            //.centerCrop();
                            .into(processByImg);  // imageview object


                    if (response.getItems().isEmpty()) {
                        transferItemsRV.setVisibility(View.GONE);
                        noDataFound.setVisibility(View.VISIBLE);
                        return;
                    }

                    transferItemsRV.setLayoutManager(new LinearLayoutManager(getActivity()));
                    transferItemsRV.setHasFixedSize(true);
                    /**
                     * now set item list to recycler view
                     */
                    TransferDetailsItemAdapter adapter = new TransferDetailsItemAdapter(getActivity(), response.getItems());
                    transferItemsRV.setAdapter(adapter);
                });
    }

    private void getDataFromPreviousFragment() {
        orderId = getArguments().getString("RefOrderId");
        status = getArguments().getString("status");
        vendorId = getArguments().getString("vendorId");
        toolbar.setText(getArguments().getString("pageName") + " Id " + orderId);
    }

    @OnClick(R.id.backbtn)
    public void backBtnClick() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.approveBtn)
    public void approveBtn() {
        String noteVal = finalNote.getText().toString();
        if (noteVal.isEmpty()) {
            finalNote.setError("Note Mandatory");
            finalNote.requestFocus();
            return;
        }
        confirmApproveDialog();
    }

    @OnClick(R.id.declineBtn)
    public void declineBtn() {
        String noteVal = finalNote.getText().toString();
        if (noteVal.isEmpty()) {
            finalNote.setError("Note Mandatory");
            finalNote.requestFocus();
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
                    ProgressDialog progressDialog = new ProgressDialog(getContext());
                    progressDialog.show();
                    transferViewModel.approvePendingTransfer(getActivity(), orderId, finalNote.getText().toString())
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
                                if (response.getStatus() == 200) {
                                    hideKeyboard(getActivity());
                                    Toasty.success(getContext(), "Pending Transfer Approved", Toasty.LENGTH_LONG).show();
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
                    dialog.dismiss();
                    ProgressDialog progressDialog = new ProgressDialog(getContext());
                    progressDialog.show();
                    transferViewModel.declinedPendingTransfer(getActivity(), orderId, finalNote.getText().toString())
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
                                if (response.getStatus() == 200) {
                                    hideKeyboard(getActivity());
                                    Toasty.success(getContext(), "Pending Transfer Declined", Toasty.LENGTH_LONG).show();
                                    getActivity().onBackPressed();
                                }
                            });
                });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }


}