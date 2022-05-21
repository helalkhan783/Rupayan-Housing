package com.rupayan_housing.view.fragment;

import android.annotation.SuppressLint;
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
import com.rupayan_housing.adapter.PaymentInstructionListAdapter;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.PaymentInstructionListResponse;
import com.rupayan_housing.viewModel.PaymentInstructionListViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PaymentInstructionListFragment extends Fragment {
    List<PaymentInstructionListResponse> paymentInstructionListResponseList;
    private PaymentInstructionListViewModel paymentInstructionListViewModel;
    String vendorId;
    PaymentInstructionListAdapter adapter;

    private boolean isDataFetching = false; // variable to detect whether server data fetch is loading or not

    @BindView(R.id.payment_instruction_list_rv)
    RecyclerView paymentInstructionListRv;
    @BindView(R.id.empty_instruction_list_warning)
    TextView emtyWarning;
    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_payment_instruction_list, container, false);
        ButterKnife.bind(this, view);
        paymentInstructionListViewModel = ViewModelProviders.of(this).get(PaymentInstructionListViewModel.class);
        vendorId = PreferenceManager.getInstance(getContext()).getUserCredentials().getVendorID();
        getPaymentInstructionResponse(vendorId);
        return view;
    }


    @SuppressLint("SetTextI18n")
    private void getPaymentInstructionResponse(String vendorID) {
        isDataFetching = true;

        paymentInstructionListRv.setHasFixedSize(true);
        paymentInstructionListRv.setLayoutManager(new GridLayoutManager(getContext(), 1));

        paymentInstructionListViewModel.apiCallForGetPaymentInstructionTotalList(getActivity(), vendorID);
        paymentInstructionListViewModel.getPaymentInstructionList().observe(getViewLifecycleOwner(), paymentInstructionList -> {
            paymentInstructionListResponseList = paymentInstructionList.getPaymentInstructionListResponseList();

            if (paymentInstructionListResponseList.isEmpty()) {
                paymentInstructionListRv.setVisibility(View.GONE);
                emtyWarning.setVisibility(View.VISIBLE);
                emtyWarning.setText("No data found");
            }
            if (!paymentInstructionListResponseList.isEmpty()) {
                paymentInstructionListRv.setVisibility(View.VISIBLE);
                emtyWarning.setVisibility(View.GONE);
                adapter = new PaymentInstructionListAdapter(getActivity(), paymentInstructionListResponseList);
                paymentInstructionListRv.setAdapter(adapter);
            }
        });
    }

    @OnClick(R.id.backbtn)
    public void backClick() {
        getActivity().onBackPressed();
    }

}