package com.rupayan_housing.view.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rupayan_housing.R;
import com.rupayan_housing.adapter.HomeListAdapter;
import com.rupayan_housing.databinding.FragmentHomePageListBinding;
import com.rupayan_housing.serverResponseModel.HomepageListResponse;
import com.rupayan_housing.utils.InternetCheckerRecyclerBuddy;
import com.rupayan_housing.viewModel.HomePageListViewModel;
import com.rupayan_housing.viewModel.HomePageViewModel;

import org.jetbrains.annotations.NotNull;

public class HomePageListFragment extends BaseFragment {
    private FragmentHomePageListBinding binding;
    private String portion;
    private ProgressDialog progressDialog;

    /**
     * for pagination
     */
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    public int pageNumber = 1, isFirstLoad = 0;
    private boolean endScroll = false;
    private LinearLayoutManager linearLayoutManager;

    private HomePageListViewModel homePageViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_page_list, container, false);

        homePageViewModel = new ViewModelProvider(this).get(HomePageListViewModel.class);
        binding.toolbar.filterBtn.setVisibility(View.GONE);

        binding.toolbar.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
/**
 get previous fragment Data
 */
        getPreviousFragmentData();

        /**
         * get All List from viewModel (server)
         */
        getAllList();
        /** for pagination **/
        binding.homePageRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) { //check for scroll down
                    visibleItemCount = linearLayoutManager.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            if (endScroll) {
                                return;
                            }
                            loading = false;
                            pageNumber += 1;

                            getAllList();

                            loading = true;
                        }
                    }
                }
            }
        });

        return binding.getRoot();
    }

    private void getAllList() {
       if (!new InternetCheckerRecyclerBuddy(getActivity()).isInternetAvailableHere(binding.homePageRv,binding.datanotfound)){
           return;
       }
        if (pageNumber == 1) {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.show();
        }

        if (pageNumber > 1) {
            binding.progress.setVisibility(View.VISIBLE);
            binding.progress.setProgress(20);
            binding.progress.setMax(100);
        }

        if (portion.equals("PurchaseRawSalt")) {
            binding.toolbar.toolbarTitle.setText("Raw Salt Purchase Lists");
            getList("736", "1");
        }
        if (portion.equals("EdibleRawSalt")) {
            binding.toolbar.toolbarTitle.setText("Iodized Salt Sale Lists");
            getList("740", "2");
        }
        if (portion.equals("IndustrialRawSalt")) {
            binding.toolbar.toolbarTitle.setText("Industrial Salt Sale Lists");
            getList("739", "2");
        }

    }

    private void getList(String category, String type) {
        homePageViewModel.homepageList(getActivity(), String.valueOf(pageNumber), category, type).observe(getViewLifecycleOwner(), new Observer<HomepageListResponse>() {
            @Override
            public void onChanged(HomepageListResponse response) {
                progressDialog.dismiss();
                binding.progress.setVisibility(View.GONE);
                try {
                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "Something wrong");
                        return;
                    }
                    if (response.getStatus() == 400) {
                        infoMessage(requireActivity().getApplication(), response.getMessage());
                        return;
                    }
                    if (response.getQtyBreakdowns() == null || response.getQtyBreakdowns().isEmpty()) {
                        if (isFirstLoad > 0) {
                            endScroll = true;
                            pageNumber -= 1;
                            return;
                        }
                        binding.homePageRv.setVisibility(View.GONE);
                        binding.datanotfound.setVisibility(View.VISIBLE);
                        return;
                    } else {
                        isFirstLoad += 1;

                        /**
                         * set data in recyclerView
                         */

                        HomeListAdapter adapter = new HomeListAdapter(getActivity(), response.getQtyBreakdowns(), portion);
                        linearLayoutManager = new LinearLayoutManager(getContext());
                        binding.homePageRv.setLayoutManager(linearLayoutManager);
                        binding.homePageRv.setAdapter(adapter);

                    }
                } catch (Exception e) {
                    Log.d("Error", e.getMessage());
                }

            }
        });
    }

    private void getPreviousFragmentData() {
        try {
            portion = getArguments().getString("portion");
        } catch (Exception e) {
            Log.d("ERROR", e.getMessage());
        }
    }
}