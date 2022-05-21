package com.rupayan_housing;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rupayan_housing.adapter.IodineSetStoreAdapter;
import com.rupayan_housing.databinding.FragmentIodinesetBinding;
import com.rupayan_housing.serverResponseModel.DuePaymentResponse;
import com.rupayan_housing.serverResponseModel.SetIodineResponse;
import com.rupayan_housing.serverResponseModel.SetIodineStoreList;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.view.fragment.set_iodine.GetData;
import com.rupayan_housing.view.fragment.user.UserAllListFragment;
import com.rupayan_housing.viewModel.IodineStoreSetViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class IodinesetFragment extends BaseFragment implements GetData {
    FragmentIodinesetBinding binding;
    IodineStoreSetViewModel iodineStoreSetViewModel;
    String storeId;
    List<SetIodineStoreList> storeList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_iodineset, container, false);
        iodineStoreSetViewModel = new ViewModelProvider(this).get(IodineStoreSetViewModel.class);
        binding.toolbar.toolbarTitle.setText("Set Iodine Store");
        getData();
        binding.toolbar.setClickHandle(() -> {
            hideKeyboard(getActivity());
            getActivity().onBackPressed();
        });
        binding.saveBtn.setOnClickListener(v -> {
            if (storeId.isEmpty()) {
                infoMessage(getActivity().getApplication(), "Please select store");
                return;
            }
            dialog();
        });
        return binding.getRoot();

    }

    private void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        @SuppressLint("InflateParams")
        View view = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.purchase_dialog, null);
        //Set the view
        builder.setView(view);
        TextView tvTitle, tvMessage;
        ImageView imageIcon = view.findViewById(R.id.img_icon);
        tvMessage = view.findViewById(R.id.tv_message);
        tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("Do You Want to Add?");//set warning title
        tvMessage.setText("MIS ERP");
        imageIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.app_logo));//set warning image
        Button bOk = view.findViewById(R.id.btn_ok);
        Button cancel = view.findViewById(R.id.cancel);
        AlertDialog alertDialog = builder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        cancel.setOnClickListener(v -> alertDialog.dismiss());//for cancel
        bOk.setOnClickListener(v -> {
            alertDialog.dismiss();
            saveStore();
        });

        alertDialog.show();
    }

    private void saveStore() {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }
        iodineStoreSetViewModel.setIodineStore(getActivity(), storeId).observe(getViewLifecycleOwner(), response -> {
            if (response == null) {
                infoMessage(getActivity().getApplication(), "SomeThing Wrong");
                return;
            }
            if (response.getStatus() == 400) {
                errorMessage(getActivity().getApplication(), response.getMessage());
                return;
            }
            successMessage(getActivity().getApplication(), response.getMessage());
            getActivity().onBackPressed();
        });
    }

    private void getData() {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        iodineStoreSetViewModel.getIodineSetStoreList(getActivity()).observe(getViewLifecycleOwner(), response -> {

            progressDialog.dismiss();
            if (response == null) {
                infoMessage(getActivity().getApplication(), "Something Wrong");
                return;
            }
            if (response.getStatus() == 400) {
                errorMessage(getActivity().getApplication(), response.getMessage());
                return;
            }

            if (response.getStatus() == 200) {
                if (response.getList().isEmpty() || response.getList() == null) {
                    binding.iodineSetRv.setVisibility(View.GONE);
                    binding.noDataFound.setVisibility(View.VISIBLE);
                    binding.saveBtn.setVisibility(View.GONE);
                    return;
                }
                storeList = new ArrayList<>();
                storeList.addAll(response.getList());
                IodineSetStoreAdapter adapter = new IodineSetStoreAdapter(getActivity(), storeList, getView(), this);
                binding.iodineSetRv.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.iodineSetRv.setAdapter(adapter);
                return;
            }
        });
    }

    @Override
    public void getData(int position, String id) {
        storeId = id;
       /* for (int i = 0; i < storeList.size(); i++) {
            if ( ((CheckBox) binding.iodineSetRv.getLayoutManager().findViewByPosition(i).findViewById(R.id.checkboxItem)).isChecked())
        }*/
    }
}