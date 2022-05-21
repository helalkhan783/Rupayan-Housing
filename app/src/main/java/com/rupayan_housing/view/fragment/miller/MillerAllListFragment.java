package com.rupayan_housing.view.fragment.miller;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.adapter.DeclineListAdapter;
import com.rupayan_housing.adapter.MillerHistoryListAdapter;
import com.rupayan_housing.adapter.MillerPendingListAdapter;
import com.rupayan_housing.databinding.FragmentMillerAllListBinding;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.AddMonitoringPageResponse;
import com.rupayan_housing.serverResponseModel.DeclineList;
import com.rupayan_housing.serverResponseModel.DistrictListResponse;
import com.rupayan_housing.serverResponseModel.DivisionResponse;
import com.rupayan_housing.serverResponseModel.HistoryList;
import com.rupayan_housing.serverResponseModel.LoginResponse;
import com.rupayan_housing.serverResponseModel.MillType;
import com.rupayan_housing.serverResponseModel.MillerPendingList;
import com.rupayan_housing.serverResponseModel.ProcessType;
import com.rupayan_housing.serverResponseModel.ThanaList;
import com.rupayan_housing.serverResponseModel.ZoneListResponse;
import com.rupayan_housing.utils.InternetCheckerRecyclerBuddy;
import com.rupayan_housing.utils.MillerUtils;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.view.fragment.all_report.reconcilation_report.page_data_response.ReconciliationPageDataResponse;
import com.rupayan_housing.view.fragment.all_report.reconcilation_report.page_data_response.ReconciliationReportAssociationList;
import com.rupayan_housing.viewModel.CurrentPermissionViewModel;
import com.rupayan_housing.viewModel.MillerDeclineViewModel;
import com.rupayan_housing.viewModel.MillerHistoryViewModel;
import com.rupayan_housing.viewModel.MillerPendingViewModel;
import com.rupayan_housing.viewModel.MillerProfileInfoViewModel;
import com.rupayan_housing.viewModel.MonitoringViewModel;
import com.rupayan_housing.viewModel.report_all_view_model.ReconciliationReportViewModel;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class MillerAllListFragment extends BaseFragment implements View.OnClickListener {
    private FragmentMillerAllListBinding binding;
    private String portion, status;
    private MillerPendingViewModel millerPendingViewModel;
    private MillerHistoryViewModel millerHistoryViewModel;
    private MillerDeclineViewModel millerDeclineViewModel;
    private CurrentPermissionViewModel currentPermissionViewModel;
    private MonitoringViewModel monitoringViewModel;


    /**
     * for division
     */
    private MillerProfileInfoViewModel millerProfileInfoViewModel;
    List<DivisionResponse> divisionResponseList;
    List<String> divisionNameList;


    /**
     * For District
     */
    private List<DistrictListResponse> districtListResponseList;
    private List<String> districtNameList;
    /**
     * For Thana
     */
    private List<ThanaList> thanaListsResponse;
    private List<String> thanaNameList;

    /**
     * for process type and millerTYpe
     */
    List<ProcessType> processTypeList;
    List<MillType> millTypeList;

    private boolean click = false;//for filter layout visible and gone
    ProgressDialog progressDialog;

    String selectedDivision, selectedDistrict, zoneId, processType, millerType;
    /**
     * For zone
     */
    private List<ReconciliationReportAssociationList> zoneListResponseList;
    private List<String> zoneNameList;
    /**
     * for pagination
     */
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    public static int pageNumber = 1, isFirstLoad = 0;
    private boolean endScroll = false;
    private LinearLayoutManager linearLayoutManager;
    List<MillerPendingList> millerPendingLists = new ArrayList<>();
    List<DeclineList> declineLists = new ArrayList<>();
    List<HistoryList> historyLists = new ArrayList<>();
    private ReconciliationReportViewModel reconciliationViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_miller_all_list, container, false);

        monitoringViewModel = new ViewModelProvider(this).get(MonitoringViewModel.class);
        millerPendingViewModel = new ViewModelProvider(this).get(MillerPendingViewModel.class);
        millerHistoryViewModel = new ViewModelProvider(this).get(MillerHistoryViewModel.class);
        millerDeclineViewModel = new ViewModelProvider(this).get(MillerDeclineViewModel.class);
        millerProfileInfoViewModel = new ViewModelProvider(this).get(MillerProfileInfoViewModel.class);
        currentPermissionViewModel = new ViewModelProvider(this).get(CurrentPermissionViewModel.class);
        reconciliationViewModel = new ViewModelProvider(this).get(ReconciliationReportViewModel.class);

        progressDialog = new ProgressDialog(getContext());// for multiple use
        linearLayoutManager = new LinearLayoutManager(getContext());// for multiple use


        binding.toolbar.setClickHandle(() -> {
            hideKeyboard(getActivity());
            getActivity().onBackPressed();
        });

        /** for division List */

        //  getDivisionData();
getPAgeDataFromViewModel();
     //   getZone();
/**
 * click
 * */
        setClick();


        /**
         * Now handle division item click
         */
        binding.sale.division.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDivision = divisionResponseList.get(position).getDivisionId();

                /**
                 * Now get district list based on division id
                 */
                getDistrictListByDivisionId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        /**
         * Nw handle district item click
         */
        binding.sale.district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDistrict = districtListResponseList.get(position).getDistrictId();
                binding.sale.district.setEnableErrorLabel(false);
                /**
                 * Now get Thana list based on district
                 */
                getThanaListByDistrictId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /**
         * now handle Thana item click
         */
        binding.sale.zone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                zoneId = zoneListResponseList.get(position).getZoneID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        /**
         * now handle process type
         */
        binding.sale.processType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                processType = processTypeList.get(position).getProcessTypeID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /**
         * now handle process type
         */
        binding.sale.millerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                millerType = millTypeList.get(position).getMillTypeID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        /** for pagination **/
        binding.millerRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) { //check for scroll down
                    try {
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

                                allListData();

                                loading = true;
                            }
                        }
                    } catch (Exception e) {
                        Log.d("ERROR", e.getMessage());

                    }

                }
            }
        });


        return binding.getRoot();
    }




    private void allListData() {
        if (!new InternetCheckerRecyclerBuddy(getActivity()).isInternetAvailableHere(binding.millerRv, binding.noDataFound)) {
            return;
        }

        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
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

        /**
         * pending Miller Profile
         * */
        if (portion.equals(MillerUtils.millreProfileList)) {
            getDataFromProfilePendingViewModel();
        }

        /**
         *   Miller Decline List
         * */
        if (portion.equals(MillerUtils.millerDeclineList)) {
            getDataFromDeclineViewModel();
        }
        /**
         *   Miller History List
         * */
        if (portion.equals(MillerUtils.millerHistoryList)) {
            getDataFromMillerHistoryViewModel();
        }
    }


    private void getDivisionData() {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }
        /**
         * For get Division list from This Api
         */
        millerProfileInfoViewModel.getProfileInfoResponse(getActivity())
                .observe(getViewLifecycleOwner(), response -> {
                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "Something Wrong");
                        return;
                    }
                    if (response.getStatus() == 400) {
                        infoMessage(getActivity().getApplication(), "" + response.getMessage());
                        return;
                    }
                    /**
                     * now set division list
                     */
                    divisionResponseList = new ArrayList<>();
                    divisionResponseList.clear();
                    divisionResponseList.addAll(response.getDivisions());

                    divisionNameList = new ArrayList<>();
                    divisionNameList.clear();

                    for (int i = 0; i < response.getDivisions().size(); i++) {
                        divisionNameList.add("" + response.getDivisions().get(i).getName());
                    }
                    binding.sale.division.setItem(divisionNameList);
                });


    }

    private void getDistrictListByDivisionId() {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }

        millerProfileInfoViewModel.getDistrictListByDivisionId(getActivity(), selectedDivision)
                .observe(getViewLifecycleOwner(), response -> {
                    if (response == null) {
                        return;
                    }
                    if (response.getStatus() != 200) {
                        return;
                    }
                    districtListResponseList = new ArrayList<>();
                    districtListResponseList.clear();
                    districtNameList = new ArrayList<>();
                    districtNameList.clear();

                    districtListResponseList.addAll(response.getLists());

                    for (int i = 0; i < response.getLists().size(); i++) {
                        districtNameList.add("" + response.getLists().get(i).getName());
                    }
                    binding.sale.district.setItem(districtNameList);
                });
    }

    private void getThanaListByDistrictId() {

        millerProfileInfoViewModel.getThanaListByDistrictId(getActivity(), selectedDistrict)
                .observe(getViewLifecycleOwner(), response -> {
                    if (response == null) {
                        return;
                    }
                    if (response.getStatus() != 200) {
                        return;
                    }
                    thanaListsResponse = new ArrayList<>();
                    thanaListsResponse.clear();
                    thanaNameList = new ArrayList<>();
                    thanaNameList.clear();

                    thanaListsResponse.addAll(response.getLists());
                    for (int i = 0; i < response.getLists().size(); i++) {
                        thanaNameList.add("" + response.getLists().get(i).getName());
                    }
                    binding.sale.zone.setItem(thanaNameList);
                });
    }

    private void setMillerType(List<MillType> millType) {
        millTypeList = new ArrayList<>();
        millTypeList = new ArrayList<>();
        millTypeList.clear();
        millTypeList.addAll(millType);

        List<String> millerTypeName = new ArrayList<>();
        millerTypeName.clear();
        for (int i = 0; i < millType.size(); i++) {
            millerTypeName.add("" + millType.get(i).getMillTypeName());
        }
        binding.sale.millerType.setItem(millerTypeName);
    }

    private void setProcessType(List<ProcessType> processType) {
        processTypeList = new ArrayList<>();
        processTypeList.clear();
        processTypeList.addAll(processType);

        List<String> processTypeName = new ArrayList<>();
        processTypeName.clear();
        for (int i = 0; i < processType.size(); i++) {
            processTypeName.add("" + processType.get(i).getProcessTypeName());
        }
        binding.sale.processType.setItem(processTypeName);
    }

    private void getDataFromDeclineViewModel() {
        millerDeclineViewModel.getMillerDeclineHistory(getActivity(), String.valueOf(pageNumber), processType, millerType, selectedDivision, selectedDistrict, zoneId).observe(getViewLifecycleOwner(), response -> {
            progressDialog.dismiss();
            binding.progress.setVisibility(View.GONE);
            if (response == null) {
                errorMessage(requireActivity().getApplication(), "something wrong");
            }
            if (response.getStatus() == 400) {
                infoMessage(getActivity().getApplication(), response.getMessage());
                return;
            }
            if (response.getStatus() == 200) {
                if (response.getLists() == null || response.getLists().isEmpty()) {
                    managePaginationAndFilter();
                } else {
                    manageFilterBtnAndRvAndDataNotFound();

                    try {
                        declineLists.addAll(response.getLists());
                        DeclineListAdapter adapter = new DeclineListAdapter(getActivity(), declineLists);
                        binding.millerRv.setLayoutManager(linearLayoutManager);
                        binding.millerRv.setHasFixedSize(true);
                        binding.millerRv.setAdapter(adapter);
                    } catch (Exception e) {
                        Log.d("Error", e.getMessage());
                    }

                    /** now set processType*/
                    setProcessType(response.getProcessType());
                    /** now set miller type*/

                    setMillerType(response.getMillType());
                }
            }


        });
    }

    private void getDataFromMillerHistoryViewModel() {
        millerHistoryViewModel.getMillerHistory(getActivity(), String.valueOf(pageNumber), processType, millerType, selectedDivision, selectedDistrict, zoneId, status).observe(getViewLifecycleOwner(), response -> {
            progressDialog.dismiss();
            binding.progress.setVisibility(View.GONE);
            if (response == null) {
                errorMessage(getActivity().getApplication(), "Something Wrong");
                return;
            }
            if (response.getStatus() == 400) {
                infoMessage(getActivity().getApplication(), "" + response.getMessage());
                return;
            }
            if (response.getStatus() == 200) {

                if (response.getLists() == null || response.getLists().isEmpty()) {
                    managePaginationAndFilter();
                } else {
                    manageFilterBtnAndRvAndDataNotFound();
                    try {
                        historyLists.addAll(response.getLists());
                        MillerHistoryListAdapter adapter = new MillerHistoryListAdapter(getActivity(), historyLists);
                        binding.millerRv.setLayoutManager(linearLayoutManager);
                        binding.millerRv.setHasFixedSize(true);
                        binding.millerRv.setAdapter(adapter);
                    } catch (Exception e) {
                        Log.d("Error", e.getMessage());
                    }
                    /** now set processType*/
                    setProcessType(response.getProcessType());
                    /** now set miller type*/

                    setMillerType(response.getMillType());
                }
            }
        });
    }

    private void getDataFromProfilePendingViewModel() {
        millerPendingViewModel.getPendingMillerList(getActivity(), String.valueOf(pageNumber), processType, millerType, selectedDivision, selectedDistrict, zoneId).observe(getViewLifecycleOwner(), response -> {
            progressDialog.dismiss();
            binding.progress.setVisibility(View.GONE);
            if (response == null) {
                errorMessage(getActivity().getApplication(), "Something Wrong");
                return;
            }
            if (response.getStatus() == 400) {
                infoMessage(getActivity().getApplication(), "" + response.getMessage());
                return;
            }
            if (response.getLists().isEmpty() || response.getLists() == null) {
                managePaginationAndFilter();
            }

            manageFilterBtnAndRvAndDataNotFound();
            /**
             * set data in recycler view
             */
            try {
                millerPendingLists.addAll(response.getLists());
                MillerPendingListAdapter adapter = new MillerPendingListAdapter(getActivity(), millerPendingLists, getView());
                binding.millerRv.setAdapter(adapter);
                binding.millerRv.setLayoutManager(linearLayoutManager);//for multiple use
                binding.millerRv.setHasFixedSize(true);// for multiple use
            } catch (Exception e) {
                Log.d("Error", e.getMessage());
            }


            /** now set processType*/
            setProcessType(response.getProcessType());

            /** now set miller type*/
            setMillerType(response.getMillType());

        });

    }

    /**
     *
     **/
    private void getPeriviousFragmentData() {
        assert getArguments() != null;
        try {
            portion = getArguments().getString("porson");
            status = getArguments().getString("status");
            binding.toolbar.toolbarTitle.setText(getArguments().getString("pageName"));

        } catch (Exception e) {
        }
    }

    private void setClick() {
        binding.toolbar.filterBtn.setOnClickListener(this);
        binding.sale.filterSearchBtn.setOnClickListener(this);
        binding.sale.resetBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.filterBtn:
               /* if (click) {
                    binding.filter.setVisibility(View.GONE);
                    click = false;
                    return;
                }
                click = true;
                binding.filter.setVisibility(View.VISIBLE);*/
                if (binding.filter.isExpanded()) {
                    binding.filter.setExpanded(false);
                    return;
                }
                binding.filter.setExpanded(true);
                break;
            case R.id.filterSearchBtn:
                pageNumber = 1;
                //for filter
                isFirstLoad = 0;
                millerPendingLists.clear();
                declineLists.clear();
                historyLists.clear();
                allListData();

                break;

            case R.id.resetBtn:
                selectedDivision = null;
                selectedDistrict = null;
                zoneId = null;
                processType = null;
                millerType = null;
                binding.sale.division.clearSelection();
                binding.sale.district.clearSelection();
                binding.sale.zone.clearSelection();

                //for filter
                isFirstLoad = 0;
                pageNumber = 1;
                millerPendingLists.clear();
                declineLists.clear();
                historyLists.clear();

                if (nullChecked()) {
                    binding.noDataFound.setVisibility(View.GONE);
                    binding.millerRv.setVisibility(View.VISIBLE);
                    allListData();
                    progressDialog.dismiss();
                }
                break;
        }
    }


    private void managePaginationAndFilter() {
        if (isFirstLoad == 0) { // if filter time list data is  null.  so then, data_not_found will be visible
            binding.millerRv.setVisibility(View.GONE);
            binding.noDataFound.setVisibility(View.VISIBLE);
            return;
        }
        if (isFirstLoad > 0) {//for scrolling off
            endScroll = true;//means scroll off
            pageNumber -= 1;
            return;
        }
        return;
    }

    private void manageFilterBtnAndRvAndDataNotFound() {
        isFirstLoad += 1;
        binding.toolbar.filterBtn.setVisibility(View.VISIBLE);
        //for filter
        // sometime filter list data came null when, data_not_found have visible,
        // And again search comes data in list by the others filter parameter.that for recycler view visible
        binding.noDataFound.setVisibility(View.GONE);
        binding.millerRv.setVisibility(View.VISIBLE);
    }

    private boolean nullChecked() {
        if (selectedDivision == null &&
                selectedDistrict == null &&
                zoneId == null &&
                processType == null &&
                millerType == null) {
            return true;
        }
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        millerPendingLists.clear();
        declineLists.clear();
        historyLists.clear();
        pageNumber = 1;
        isFirstLoad = 0;

        getPeriviousFragmentData();
        /**
         * get all list data from server
         */
        allListData();
        try {
            updateCurrentUserPermission(getActivity());
        } catch (Exception e) {
            Log.d("ERROR", "" + e.getMessage());
        }
    }

    public void updateCurrentUserPermission(FragmentActivity activity) {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }
        currentPermissionViewModel.getCurrentUserRealtimePermissions(
                PreferenceManager.getInstance(activity).getUserCredentials().getToken(),
                PreferenceManager.getInstance(activity).getUserCredentials().getUserId()
        ).observe(getViewLifecycleOwner(), response -> {
            if (response == null) {
                Toasty.error(activity, "Something Wrong", Toasty.LENGTH_LONG).show();
                return;
            }
            if (response.getStatus() == 400) {
                Toasty.info(activity, "" + response.getMessage(), Toasty.LENGTH_LONG).show();
            }
            try {
                LoginResponse loginResponse = PreferenceManager.getInstance(activity).getUserCredentials();
                if (loginResponse != null) {
                    loginResponse.setPermissions(response.getMessage());
                    loginResponse.setToken(response.getToken());
                    PreferenceManager.getInstance(activity).saveUserCredentials(loginResponse);
                }
            } catch (Exception e) {
                infoMessage(getActivity().getApplication(), "" + e.getMessage());
                Log.d("ERROR", "" + e.getMessage());
            }
        });
    }
    private void getPAgeDataFromViewModel() {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        reconciliationViewModel.getReconciliationPageData(getActivity(), getProfileId(getActivity().getApplication())).observe(getViewLifecycleOwner(), new Observer<ReconciliationPageDataResponse>() {
            @Override
            public void onChanged(ReconciliationPageDataResponse response) {
                progressDialog.dismiss();
                try {
                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "something wrong");
                        return;
                    }
                    if (response.getStatus() == 400) {
                        errorMessage(getActivity().getApplication(), response.getMessage());
                        return;

                    }
                    if (response.getStatus() == 200) {
                        /** for Association */
                        zoneListResponseList = new ArrayList<>();
                        zoneListResponseList.clear();

                        zoneNameList = new ArrayList<>();
                        zoneNameList.clear();

                        zoneListResponseList.addAll(response.getAssociationList());

                        for (int i = 0; i < response.getAssociationList().size(); i++) {
                            zoneNameList.add("" + response.getAssociationList().get(i).getZoneName()+"/"+response.getAssociationList().get(i).getDisplayName());
                            if (zoneId !=null ){
                                if ( zoneId.equals(zoneListResponseList.get(i).getZoneID())){
                                    binding.sale.zone.setSelection(i);
                                }
                            }
                        }
                        binding.sale.zone.setItem(zoneNameList);


                    }

                } catch (Exception e) {
                    Log.d("Error", e.getMessage());
                }
            }
        });
    }

}