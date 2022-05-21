package com.rupayan_housing.view.fragment.auth;

import android.graphics.Color;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.rupayan_housing.R;
import com.rupayan_housing.clickHandle.ToolbarClickHandle;
import com.rupayan_housing.databinding.FragmentProfileBinding;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.viewModel.HomePageViewModel;

import es.dmoral.toasty.Toasty;


public class ProfileFragment extends BaseFragment {
    private FragmentProfileBinding binding;
    private HomePageViewModel homePageViewModel;
    public static int noCall = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        binding.toolbar.toolbarTitle.setText("Profile");
        homePageViewModel = new ViewModelProvider(this).get(HomePageViewModel.class);

        binding.toolbar.setClickHandle(() -> {
            hideKeyboard(getActivity());
            getActivity().onBackPressed();
        });
        setPageData();

            if (!getUserId(getActivity().getApplication()).equals(getVendorId(getActivity().getApplication()))) {
                binding.updateBtn.setClickable(false);
                binding.updateBtn.setBackgroundColor(Color.GRAY);
            }

        binding.updateBtn.setOnClickListener(v -> {
            try {
                if (!getUserId(getActivity().getApplication()).equals(getVendorId(getActivity().getApplication()))) {
                    binding.updateBtn.setClickable(false);
                    binding.updateBtn.setBackgroundColor(Color.GRAY);
                    return;
                }
                Navigation.findNavController(getView()).navigate(R.id.action_profileFragment_to_updateProfileFragmentn);
            } catch (Exception e) {}
        });

        binding.passwordChange.setOnClickListener(v -> {
            try {
                Bundle bundle = new Bundle();
                bundle.putString("phone", getPhone(getActivity().getApplication()));
                Navigation.findNavController(getView()).navigate(R.id.action_profileFragment_to_updatePasswordFragment, bundle);

            } catch (Exception e) {
            }
        });
        return binding.getRoot();
    }


    private void setPageData() {
        //These data get from home fragment by dashboard api
        try {
            if (getArguments().getString("primaryNumber") != null) {
                binding.phone.setText("" + getArguments().getString("primaryNumber"));
            }

            if (getArguments().getString("email") != null) {
                binding.email.setText("" + getArguments().getString("email"));
            }
            if (getArguments().getString("photo") != null) {
                Glide.with(getContext())
                        .load(getArguments().getString("photo"))
                        .centerInside()
                        .error(R.drawable.error_one)
                        .placeholder(R.drawable.error_one)
                        .into(binding.profilePic);
            }
        } catch (Exception e) {
        }

        try {
            if (noCall == 1) {
                homePageViewModel.getHomePageData(getActivity())
                        .observe(getViewLifecycleOwner(), response -> {
                            if (response == null) {
                                errorMessage(getActivity().getApplication(), "Something Wrong");
                                return;
                            }

                            if (response.getStatus() == 400) {
                                infoMessage(getActivity().getApplication(), "" + response.getMessage());
                                return;
                            }

                            binding.phone.setText("" + response.getUserInfo().getPrimaryMobile());
                            binding.email.setText("" + response.getUserInfo().getEmail());
                            try {
                                Glide.with(getContext())
                                        .load(response.getUserInfo().getProfilePhoto())
                                        .centerInside()
                                        .error(R.drawable.error_one)
                                        .placeholder(R.drawable.error_one)
                                        .into(binding.profilePic);
                            } catch (Exception e) {
                            }
                        });

            }
        } catch (Exception e) {
        }
        /*binding.phone.setText("" + getPhone(getActivity().getApplication()));
        if (getEmail(getActivity().getApplication()) != null) {
            binding.email.setText("" + getEmail(getActivity().getApplication()));
        }
        Glide.with(getContext())
                .load(getProfilePhoto(getActivity().getApplication()))
                .centerInside()
                .error(R.drawable.person)
                .placeholder(R.drawable.person)
                .into(binding.profilePic);*/
    }
}