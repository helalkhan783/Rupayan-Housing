package com.rupayan_housing.view.fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rupayan_housing.R;
import com.rupayan_housing.clickHandle.ToolbarClickHandle;
import com.rupayan_housing.databinding.FragmentTermsAndPrivacyBinding;

public class TermsAndPrivacyFragment extends Fragment {
    FragmentTermsAndPrivacyBinding binding;
    String pageName, portion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_terms_and_privacy, container, false);
        getPreViousData();
        binding.toolbar.setClickHandle(() -> getActivity().onBackPressed());
        try {
         setAllData();
        } catch (Exception e) {
        }

        return binding.getRoot();
    }

    private void setAllData() {
        if (portion.equals("fromHomeForAbout")) {
            binding.terms.setVisibility(View.GONE);
            binding.aboutPortionLayout.setVisibility(View.VISIBLE);
            return;
        }
        if (portion.equals("Privacy")) {
            binding.aboutPortionLayout.setVisibility(View.GONE);
            binding.terms.setVisibility(View.VISIBLE);
            return;
        }
    }



    private void getPreViousData() {
        try {
            pageName = getArguments().getString("pageName");
            portion = getArguments().getString("fromHomeForAbout");
            binding.toolbar.toolbarTitle.setText("" + pageName);
        } catch (Exception e) {
        }
    }
}