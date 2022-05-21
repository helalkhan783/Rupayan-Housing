package com.rupayan_housing.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.bumptech.glide.Glide;

import com.rupayan_housing.R;
import com.rupayan_housing.adapter.PendingExpenseItemsAdapter;
import com.rupayan_housing.viewModel.ExpenseViewModel;
import com.rupayan_housing.viewModel.PendingExpenseApproveDeclineViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class PendingExpenseDetails extends Fragment {
    private View view;
    private ExpenseViewModel expenseViewModel;
    private PendingExpenseApproveDeclineViewModel pendingExpenseApproveDeclineViewModel;
    @BindView(R.id.toolbarTitle)
    TextView toolBar;
    @BindView(R.id.noteEditText)
    EditText note;

    @BindView(R.id.enterPriseDetails)
    TextView enterPriseDetails;
    @BindView(R.id.enterPriseName)
    TextView enterPriseName;
    @BindView(R.id.totalAmount)
    TextView totalAmount;
    @BindView(R.id.paidAmountEt)
    TextView paidAmount;
    @BindView(R.id.vendor)
    TextView vendor;
    @BindView(R.id.type)
    TextView type;
    @BindView(R.id.entryDate)
    TextView entryDate;
    @BindView(R.id.processByImg)
    CircularImageView processByImg;
    @BindView(R.id.processByName)
    TextView processByName;
    @BindView(R.id.pendingExpenseRv)
    RecyclerView pendingExpenseRv;
    String orderId, paymentID;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pending_expense_details, container, false);
        ButterKnife.bind(this, view);
        expenseViewModel = ViewModelProviders.of(this).get(ExpenseViewModel.class);
        pendingExpenseApproveDeclineViewModel = ViewModelProviders.of(this).get(PendingExpenseApproveDeclineViewModel.class);
        /**
         * for get data from previous fragment
         */
        getDataFromPreviousFragment();
        /**
         * now get pending expense details from server
         */
        getPendingExpenseDetailsFromServer();


        return view;
    }

    private void getPendingExpenseDetailsFromServer() {
        expenseViewModel.getPendingExpenseDetails(getActivity(), orderId)
                .observe(getViewLifecycleOwner(), response -> {
                    paymentID = response.getPaymentAmount().getPaymentID();//store payment id for approve and decline expense

                    enterPriseDetails.setText("#" + orderId);
                    enterPriseName.setText(response.getExpenseInfo().getStoreName());
                    totalAmount.setText(response.getPaymentAmount().getTotalAmount() + " Taka");
                    paidAmount.setText(response.getPaymentAmount().getPaidAmount() + " Taka");
                    vendor.setText(response.getExpenseInfo().getCompanyName() + "@" + response.getExpenseInfo().getCustomerFname());
                    type.setText(response.getExpenseInfo().getExpenseCategory());
                    entryDate.setText(response.getExpenseInfo().getEntryDateTime());

                    Glide.with(this)
                            .load(response.getProcessedBy().getProfilePhoto())
                            .placeholder(R.drawable.agamisoft_logo)
                            .error(R.drawable.agamisoft_logo)
                            .override(200, 200)
                            .into(processByImg);

                    processByName.setText(response.getProcessedBy().getFullName());
                    /**
                     * now set the pending amount to  recyclerView
                     */
                    PendingExpenseItemsAdapter adapter = new PendingExpenseItemsAdapter(getActivity(), response.getExpenseLists());
                    pendingExpenseRv.setLayoutManager(new LinearLayoutManager(getContext()));
                    pendingExpenseRv.setHasFixedSize(true);
                    pendingExpenseRv.setAdapter(adapter);
                });
    }

    private void getDataFromPreviousFragment() {
        toolBar.setText(getArguments().getString("pageName"));
        orderId = getArguments().getString("RefOrderId");
    }

    @OnClick(R.id.backbtn)
    public void backBtnClick() {
        getActivity().onBackPressed();
    }


    @OnClick(R.id.approveBtn)
    public void approvePendingExpense() {
        String notVal = note.getText().toString();
        if (notVal.isEmpty()) {
            note.setError("Note Mandatory");
            note.requestFocus();
            return;
        }


        pendingExpenseApproveDeclineViewModel.approvePendingExpense(getActivity(), orderId, paymentID, notVal)
                .observe(getViewLifecycleOwner(), response -> {
                    if (response.getStatus() == 200) {
                        Toasty.success(getContext(), "Expense Approved", Toasty.LENGTH_LONG).show();
                        getActivity().onBackPressed();
                    }

                });
    }

    @OnClick(R.id.declineBtn)
    public void declinePendingExpense() {
        String notVal = note.getText().toString();
        if (notVal.isEmpty()) {
            note.setError("Note Mandatory");
            note.requestFocus();
            return;
        }

        pendingExpenseApproveDeclineViewModel.declinePendingExpense(getActivity(), orderId, paymentID, notVal)
                .observe(getViewLifecycleOwner(), response -> {
                    if (response.getStatus() == 200) {
                        Toasty.success(getContext(), "Expense Declined", Toasty.LENGTH_LONG).show();
                        getActivity().onBackPressed();
                    }
                });
    }
}