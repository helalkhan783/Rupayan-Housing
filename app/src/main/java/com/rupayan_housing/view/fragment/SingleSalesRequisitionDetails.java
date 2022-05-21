package com.rupayan_housing.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rupayan_housing.R;
import com.rupayan_housing.adapter.SalesRequisitionDetailsProductsAdapter;
import com.rupayan_housing.serverResponseModel.SingleRequisitionDetailsResponse;
import com.rupayan_housing.viewModel.PendingRequisitionApproveDeclineViewModel;
import com.rupayan_housing.viewModel.SalesRequisitionListViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class SingleSalesRequisitionDetails extends Fragment {
    private SalesRequisitionListViewModel salesRequisitionListViewModel;
    private PendingRequisitionApproveDeclineViewModel pendingRequisitionApproveDeclineViewModel;
    private View view;
    @BindView(R.id.customerNameEt)
    TextView customerNameEt;
    @BindView(R.id.companyNameTv)
    TextView companyNameTv;
    @BindView(R.id.phoneEt)
    TextView phoneEt;
    @BindView(R.id.paymentType)
    TextView paymentType;
    @BindView(R.id.addressTv)
    TextView addressTv;
    @BindView(R.id.startDateTv)
    TextView startDateTv;
    @BindView(R.id.endDateTv)
    TextView endDateTv;
    @BindView(R.id.requisitionNumberTv)
    TextView requisitionNumberTv;
    @BindView(R.id.totalAmountTvMain)
    TextView totalAmountTv;
    @BindView(R.id.discountTv)
    TextView discountTv;
    @BindView(R.id.collectedAmountTv)
    TextView collectedAmountTv;
    @BindView(R.id.toolbarTitle)
    TextView toolbar;
    @BindView(R.id.productListRv)
    RecyclerView productListRv;


    private LinearLayout pendingView;
    private Button approveBtn;
    private Button declineBtn;
    LinearLayout noteView;
    @BindView(R.id.NoteEt)
    EditText noteEt;

    /**
     * for get data from previous fragment
     */
    String selectedRequisitionId;
    boolean isPendingDetails = false;
    SingleRequisitionDetailsResponse response;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.single_sales_requisition, container, false);
        ButterKnife.bind(this, view);
        salesRequisitionListViewModel = ViewModelProviders.of(this).get(SalesRequisitionListViewModel.class);
        pendingRequisitionApproveDeclineViewModel = ViewModelProviders.of(this).get(PendingRequisitionApproveDeclineViewModel.class);
        intit();
        getDataFromPreviousFragment();
        /**
         * load requisition details in recyclerview
         */
        loadDetailsInRv();
        /**
         * if this fragment come from pending requisition fragment then  approve and decline btn should be VISIBLE
         */
        if (isPendingDetails) {
            pendingView.setVisibility(View.VISIBLE);
            noteView.setVisibility(View.VISIBLE);
        }
        return view;
    }

    private void intit() {
        pendingView = (LinearLayout) view.findViewById(R.id.pendingView);
        approveBtn = view.findViewById(R.id.approveBtn);
        declineBtn = view.findViewById(R.id.declineBtn);
        noteView = view.findViewById(R.id.noteView);
    }


    private void loadDetailsInRv() {
        salesRequisitionListViewModel.getSingleRequisitionDetails(getActivity(), selectedRequisitionId).observe(getViewLifecycleOwner(), singleDetails -> {
            response = singleDetails.getDetails();//will access this data for show main this fragment
            Log.d("DETAILS", String.valueOf(new Gson().toJson(singleDetails.getDetails())));
            productListRv.setLayoutManager(new LinearLayoutManager(getContext()));
            SalesRequisitionDetailsProductsAdapter adapter = new SalesRequisitionDetailsProductsAdapter(getActivity(), response.getItems());
            productListRv.setAdapter(adapter);
            /**
             * load data to view
             */
            loadDataToView();
        });
    }

    private void loadDataToView() {
        customerNameEt.setText(response.getCustomer().getCustomerFname());
        companyNameTv.setText(response.getCustomer().getCompanyName());
        phoneEt.setText(response.getCustomer().getPhone());
        paymentType.setText(response.getPaymentType());
        addressTv.setText(response.getCustomer().getAddress());
        startDateTv.setText(response.getRequisitionDate());
        endDateTv.setText(response.getRequisitionEndDate());
        requisitionNumberTv.setText("# " + selectedRequisitionId);
        totalAmountTv.setText(String.valueOf(response.getTotalAmount()) + " TK");
        discountTv.setText(String.valueOf(response.getDiscount()));
        collectedAmountTv.setText(String.valueOf(response.getCollected()) + " TK");
    }

    private void getDataFromPreviousFragment() {
        toolbar.setText("Requisition Details");
        selectedRequisitionId = getArguments().getString("id");
        isPendingDetails = getArguments().getBoolean("isPending");//for confirm this page come from (Pending requisition list)
    }

    @OnClick(R.id.backbtn)
    public void backBtnClick() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.approveBtn)
    public void approveBtn() {
        String note = noteEt.getText().toString();
        if (note.isEmpty()){
            noteEt.setError("Note mandatory");
            noteEt.requestFocus();
            return;
        }
        //selectedRequisitionId //this is the current order id
        pendingRequisitionApproveDeclineViewModel.sendRequisitionApprovedRequest(getActivity(), selectedRequisitionId, note)
                .observe(getViewLifecycleOwner(), duePaymentResponse -> {
                    Toasty.success(getContext(), duePaymentResponse.getMessage(), Toasty.LENGTH_LONG).show();
                    getActivity().onBackPressed();
                });
    }

    @OnClick(R.id.declineBtn)
    public void declineBtn() {
        String note = noteEt.getText().toString();
   /*     if (note.equals(null)) {
            Toasty.info(getContext(), "Note Mandatory", Toasty.LENGTH_LONG).show();
            noteEt.requestFocus();
            return;
        }*/

        if (note.isEmpty()){
            noteEt.setError("Note mandatory");
            noteEt.requestFocus();
            return;
        }

        //selectedRequisitionId //this is the current order id
        pendingRequisitionApproveDeclineViewModel.sendRequisitionDeclineRequest(getActivity(), selectedRequisitionId, note)
                .observe(getViewLifecycleOwner(), duePaymentResponse -> {
                    Toasty.success(getActivity(), duePaymentResponse.getMessage(), Toasty.LENGTH_LONG).show();
                    getActivity().onBackPressed();
                });
    }


}