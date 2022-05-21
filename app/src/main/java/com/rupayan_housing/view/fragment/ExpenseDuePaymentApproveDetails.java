package com.rupayan_housing.view.fragment;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.rupayan_housing.R;
import com.rupayan_housing.adapter.ExpenseDuePaymentApprovalAdapter;

import com.rupayan_housing.viewModel.ExpenseDuePaymentPendingDetailsViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class ExpenseDuePaymentApproveDetails extends BaseFragment {
    private View view;
    private ExpenseDuePaymentPendingDetailsViewModel expenseDuePaymentPendingDetailsViewModel;

    @BindView(R.id.toolbarTitle)
    TextView toolbar;

    @BindView(R.id.customerName)
    TextView customerName;
    @BindView(R.id.companyName)
    TextView companyName;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.address)
    TextView address;


    @BindView(R.id.expenseDuePaymentApprovalRv)
    RecyclerView expenseDuePaymentApprovalRv;
    @BindView(R.id.noteEditText)
    TextView noteEditText;

    String batch, customer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_expense_due_payment_approve_details, container, false);
        ButterKnife.bind(this, view);
        expenseDuePaymentPendingDetailsViewModel = new ViewModelProvider(this).get(ExpenseDuePaymentPendingDetailsViewModel.class);
        getDataFromPreviousFragment();

        /**
         * now get Expense Payment Due details from server
         */
        getExpensePaymentDueFromServer();


        return view;
    }

    private void getDataFromPreviousFragment() {
        toolbar.setText("Expense Payment Due");
        batch = getArguments().getString("batch");
        customer = getArguments().getString("customer");
    }

    private void getExpensePaymentDueFromServer() {
        expenseDuePaymentPendingDetailsViewModel.getExpenseDuePaymentApprovalDetails(getActivity(), batch, customer)
                .observe(getViewLifecycleOwner(), response -> {
                    customerName.setText(response.getCustomerInfo().getCustomerFname());
                    companyName.setText(response.getCustomerInfo().getCompanyName());
                    phone.setText(response.getCustomerInfo().getPhone());
                    address.setText(
                            response.getCustomerInfo().getAddress()
                    );
                    /**
                     * now show all pending payment approval in recyclerview
                     */

                    expenseDuePaymentApprovalRv.setLayoutManager(new LinearLayoutManager(getContext()));
                    expenseDuePaymentApprovalRv.setHasFixedSize(true);

                    ExpenseDuePaymentApprovalAdapter approvalAdapter = new ExpenseDuePaymentApprovalAdapter(getActivity(), response.getLists());
                    expenseDuePaymentApprovalRv.setAdapter(approvalAdapter);


                });
    }


    @OnClick(R.id.approveBtn)
    public void approveDuePayment() {
        String noteVal = noteEditText.getText().toString();
        if (noteVal.isEmpty()) {
            noteEditText.setError("Note mandatory");
            noteEditText.requestFocus();
            return;
        }
        expenseDuePaymentPendingDetailsViewModel.approveExpenseDuePaymentApprovalDetails(getActivity(), batch)
                .observe(getViewLifecycleOwner(), response -> {
                    Toasty.success(getContext(), "Approved", Toasty.LENGTH_LONG).show();
                    getActivity().onBackPressed();
                });
    }

    @OnClick(R.id.declineBtn)
    public void declineDuePayment() {
        String noteVal = noteEditText.getText().toString();
        if (noteVal.isEmpty()) {
            noteEditText.setError("Note mandatory");
            noteEditText.requestFocus();
            return;
        }

        expenseDuePaymentPendingDetailsViewModel.declineExpenseDuePaymentApprovalDetails(getActivity(), batch)
                .observe(getViewLifecycleOwner(), response -> {
                    Toasty.success(getContext(), "Declined", Toasty.LENGTH_LONG).show();
                    getActivity().onBackPressed();
                });
    }


    @OnClick(R.id.backbtn)
    public void backBtn() {
        getActivity().onBackPressed();
    }
}