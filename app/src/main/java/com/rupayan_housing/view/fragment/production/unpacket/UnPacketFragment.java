package com.rupayan_housing.view.fragment.production.unpacket;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rupayan_housing.R;
import com.rupayan_housing.clickHandle.ToolbarClickHandle;
import com.rupayan_housing.databinding.FragmentUnPacketBinding;
import com.rupayan_housing.view.fragment.BaseFragment;


public class UnPacketFragment extends BaseFragment {
    private FragmentUnPacketBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_un_packet, container, false);
        binding.toolbar.toolbarTitle.setText("Unpacket");
        binding.toolbar.setClickHandle(() -> {
            hideKeyboard(getActivity());
            getActivity().onBackPressed();
        });




        return binding.getRoot();
    }
}