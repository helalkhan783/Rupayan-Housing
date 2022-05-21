package com.rupayan_housing.view.fragment.purchase;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.adapter.PurchaseDeclinedListAdapter;
import com.rupayan_housing.adapter.PurchaseHistoryAdapter;
import com.rupayan_housing.adapter.PurchasePendingListAdapter;
import com.rupayan_housing.adapter.PurchasePendingReturnListAdapter;
import com.rupayan_housing.adapter.PurchaseReturnHistoryListAdapter;
import com.rupayan_housing.databinding.FragmentAllPurchaseListBinding;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.retrofit.PurchaseDeclineList;
import com.rupayan_housing.serverResponseModel.Company;
import com.rupayan_housing.serverResponseModel.Enterprize;
import com.rupayan_housing.serverResponseModel.LoginResponse;
import com.rupayan_housing.serverResponseModel.PurchaseHistoryList;
import com.rupayan_housing.serverResponseModel.PurchasePendingList;
import com.rupayan_housing.serverResponseModel.PurchaseReturnHistoryList;
import com.rupayan_housing.serverResponseModel.PurchaseReturnPendingList;
import com.rupayan_housing.utils.InternetCheckerRecyclerBuddy;
import com.rupayan_housing.utils.PurchaseUtill;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.viewModel.CurrentPermissionViewModel;
import com.rupayan_housing.viewModel.PurchaseHistoryViewModel;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.dmoral.toasty.Toasty;


public class AllPurchaseListFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener,
        View.OnClickListener {
    ProgressDialog progressDialog;
    FragmentAllPurchaseListBinding binding;
    String portion;
    private PurchaseHistoryViewModel purchaseHistoryViewModel;
    private CurrentPermissionViewModel currentPermissionViewModel;
    boolean click = false;

    /**
     * for company
     */
    List<String> companyNameList;
    List<Company> companyList;

    /**
     * for enterPrise
     */
    List<String> enterPriseNameList;
    List<Enterprize> enterprizeList;
     /**
     * for pagination
     */
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    public static int pageNumber = 1, isFirstLoad = 0;
    private boolean endScroll = false;
    private LinearLayoutManager linearLayoutManager;
    private boolean isStartDate = false;
    private String companyId, enterPriseId, startDate, endDate;
    List<PurchaseHistoryList> purchaseHistoryLists = new ArrayList<>();
    List<PurchasePendingList> purchasePendingLists = new ArrayList<>();
    List<PurchaseReturnPendingList> purchaseReturnPendingLists = new ArrayList<>();
    List<PurchaseReturnHistoryList> purchaseReturnHistoryLists = new ArrayList<>();
    List<PurchaseDeclineList> purchaseDeclineLists = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_all_purchase_list, container, false);
        currentPermissionViewModel = new ViewModelProvider(this).get(CurrentPermissionViewModel.class);
        progressDialog = new ProgressDialog(getContext());

        /**
         * object create for viewModel
         */
        purchaseHistoryViewModel = new ViewModelProvider(this).get(PurchaseHistoryViewModel.class);


        binding.toolbar.filterBtn.setOnClickListener(v -> {
            if (binding.expandableView.isExpanded()){
                binding.expandableView.setExpanded(false);
                return;
            }{
                binding.expandableView.setExpanded(true);
            }

        });

        binding.toolbar.setClickHandle(() -> {
            hideKeyboard(getActivity());
            getActivity().onBackPressed();
            pageNumber = 1;
        });
/** click handle*/
        setOnClick();



        binding.sale.selectCompany.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                companyId = companyList.get(position).getCustomerID();
                binding.sale.selectCompany.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.sale.enterprise.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                enterPriseId = enterprizeList.get(position).getStoreID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /** for pagination **/
        binding.purchaserRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

                            getAllListData();

                            loading = true;
                        }
                    }
                }
            }
        });

        return binding.getRoot();
    }

    private void getAllListData() {
        if (!new InternetCheckerRecyclerBuddy(getActivity()).isInternetAvailableHere(binding.purchaserRv, binding.dataNotFound)) {
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


        if (portion.equals(PurchaseUtill.purchaseHistory) ) {
            binding.toolbar.toolbarTitle.setText(""+PurchaseUtill.purchaseHistory);
            getPurchaseHistoryFromViewmOdel();
        }
        if (portion.equals(PurchaseUtill.pendingPurchaseList)) {
            binding.toolbar.toolbarTitle.setText("Purchase Pending List");
            pendingPurchaseList();

        }
        if (portion.equals(PurchaseUtill.pendingPurchaseReturn)) {
            binding.toolbar.toolbarTitle.setText("Pending Purchase Return List");
            pendingReturnPurchaseList();

        }

        if (portion.equals(PurchaseUtill.purchaseReturnHistory)) {
            binding.toolbar.toolbarTitle.setText("Purchase Return History List");
            purchaseReturnHistoryList();

        }


        if (portion.equals(PurchaseUtill.declinePurchaseList)) {
            binding.toolbar.toolbarTitle.setText("Purchase Declined List");
            getPurchaseDeclineList();
        }

    }

    private void getPurchaseDeclineList() {
        purchaseHistoryViewModel.getPurchaseDeclineList(getActivity(), String.valueOf(pageNumber), startDate, endDate, companyId, enterPriseId)
                .observe(getViewLifecycleOwner(), response -> {
                    progressDialog.dismiss();
                    binding.progress.setVisibility(View.GONE);

                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "Something wrong");
                        return;
                    }

                    if (response.getStatus() == 400) {
                        infoMessage(getActivity().getApplication(), response.getMessage());
                        return;
                    }

                    if (response.getStatus() == 200) {
                        if (response.getLists() == null || response.getLists().isEmpty() || response.getLists().size() == 0) {


                            managePaginationAndFilter();

                        } else {

                            manageFilterBtnAndRvAndDataNotFound();

                            /**
                             now set data in recyclerview
                             */

                            purchaseDeclineLists.addAll(response.getLists());
                            PurchaseDeclinedListAdapter adapter = new PurchaseDeclinedListAdapter(getActivity(), purchaseDeclineLists);
                            linearLayoutManager = new LinearLayoutManager(getContext());
                            binding.purchaserRv.setLayoutManager(linearLayoutManager);
                            binding.purchaserRv.setAdapter(adapter);

                            /**Now set data to company select Spinner */
                            setNameInSpinner(response.getCompanyList());

                            /** set enterPrise */
                            setDatainEnterPrise(response.getEnterprizeList());
                        }
                    }

                });


    }

    private void purchaseReturnHistoryList() {
        purchaseHistoryViewModel.getPurchaseReturnHistoryList(getActivity(), String.valueOf(pageNumber), startDate, endDate, companyId, enterPriseId)
                .observe(getViewLifecycleOwner(), response -> {
                    progressDialog.dismiss();
                    binding.progress.setVisibility(View.GONE);

                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "Something wrong");
                        return;
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
                            /**
                             now set data in recyclerview
                             */
                            purchaseReturnHistoryLists.addAll(response.getLists());
                            PurchaseReturnHistoryListAdapter adapter = new PurchaseReturnHistoryListAdapter(getActivity(), purchaseReturnHistoryLists);
                            linearLayoutManager = new LinearLayoutManager(getContext());
                            binding.purchaserRv.setLayoutManager(linearLayoutManager);
                            binding.purchaserRv.setAdapter(adapter);

                            /**Now set data to company select Spinner */
                            setNameInSpinner(response.getCompanyList());

                            /** set enterPrise */
                            setDatainEnterPrise(response.getEnterprizeList());
                        }
                    }
                });
    }

    private void pendingReturnPurchaseList() {
        purchaseHistoryViewModel.getPurchaseReturnPendingList(getActivity(), String.valueOf(pageNumber), startDate, endDate, companyId, enterPriseId)
                .observe(getViewLifecycleOwner(), response -> {
                    progressDialog.dismiss();
                    binding.progress.setVisibility(View.GONE);

                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "Something wrong");
                        return;
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

                            /**
                             * now set data in recyclerview
                             */

                            purchaseReturnPendingLists.addAll(response.getLists());
                            PurchasePendingReturnListAdapter adapter = new PurchasePendingReturnListAdapter(getActivity(), purchaseReturnPendingLists);
                            linearLayoutManager = new LinearLayoutManager(getContext());
                            binding.purchaserRv.setLayoutManager(linearLayoutManager);
                            binding.purchaserRv.setAdapter(adapter);

                            /**Now set data to company select Spinner */

                            setNameInSpinner(response.getCompanyList());

                            /** set enterPrise */
                            setDatainEnterPrise(response.getEnterprizeList());
                        }
                    }

                });


    }

    private void pendingPurchaseList() {
        purchaseHistoryViewModel.getPendingPurchaseList(getActivity(), String.valueOf(pageNumber), startDate, endDate, companyId, enterPriseId)
                .observe(getViewLifecycleOwner(), response -> {
                    progressDialog.dismiss();
                    binding.progress.setVisibility(View.GONE);
                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "Something wrong");
                        return;
                    }
                    if (response.getStatus() == 400) {
                        infoMessage(getActivity().getApplication(), response.getMessage());
                        return;
                    }

                    if (response.getStatus() == 200) {
                        if (response.getLists() == null || response.getLists().isEmpty() || response.getLists().equals(0)) {
                            managePaginationAndFilter();
                        } else {
                            manageFilterBtnAndRvAndDataNotFound();

                            /**
                             * now set data in recyclerview
                             */

                            purchasePendingLists.addAll(response.getLists());

                            PurchasePendingListAdapter adapter = new PurchasePendingListAdapter(getActivity(), purchasePendingLists);
                            linearLayoutManager = new LinearLayoutManager(getContext());
                            binding.purchaserRv.setLayoutManager(linearLayoutManager);
                            binding.purchaserRv.setAdapter(adapter);

                            /**Now set data to company select Spinner */

                            setNameInSpinner(response.getCompanyList());

                            /** set enterPrise */
                            setDatainEnterPrise(response.getEnterprizeList());
                        }
                    }

                });

    }

    private void getPurchaseHistoryFromViewmOdel() {
        purchaseHistoryViewModel.getPeechaseHistoryList(getActivity(), String.valueOf(pageNumber), startDate, endDate, companyId, enterPriseId)
                .observe(getViewLifecycleOwner(), response -> {
                    progressDialog.dismiss();
                    binding.progress.setVisibility(View.GONE);

                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "Something wrong");
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

                            /**
                             * now set data in recyclerview
                             */

                            purchaseHistoryLists.addAll(response.getLists());
                            PurchaseHistoryAdapter adapter = new PurchaseHistoryAdapter(getActivity(), purchaseHistoryLists);
                            linearLayoutManager = new LinearLayoutManager(getContext());
                            binding.purchaserRv.setLayoutManager(linearLayoutManager);
                            binding.purchaserRv.setAdapter(adapter);

                            /**Now set data to company select Spinner */

                            setNameInSpinner(response.getCompanyList());

                            /** set enterPrise */
                            setDatainEnterPrise(response.getEnterprizeList());
                        }
                    }

                });
    }



    private void managePaginationAndFilter() {

        if (isFirstLoad == 0) { // if filter time list is null.  so then, data_not_found will be visible
            binding.purchaserRv.setVisibility(View.GONE);
            binding.dataNotFound.setVisibility(View.VISIBLE);
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
        binding.dataNotFound.setVisibility(View.GONE);
        binding.purchaserRv.setVisibility(View.VISIBLE);
    }

    private void setNameInSpinner(List<Company> list) {
        companyList = new ArrayList<>();
        companyList.clear();
        companyList.addAll(list);
        companyNameList = new ArrayList<>();
        companyNameList.clear();

        for (int i = 0; i < companyList.size(); i++) {
            companyNameList.add("" + companyList.get(i).getCompanyName());
            if (companyId != null) {
                if (companyList.get(i).getCustomerID().equals(companyId)) {
                    binding.sale.selectCompany.setSelection(i);
                }
            }
        }
        binding.sale.selectCompany.setItem(companyNameList);
    }

    private void setDatainEnterPrise(List<Enterprize> list) {
        enterPriseNameList = new ArrayList<>();
        enterPriseNameList.clear();
        enterprizeList = new ArrayList<>();
        enterprizeList.clear();
        enterprizeList.addAll(list);

        for (int i = 0; i < list.size(); i++) {
            enterPriseNameList.add(enterprizeList.get(i).getStoreName());
            if (enterPriseId != null) {
                if (enterprizeList.get(i).getStoreID().equals(enterPriseId)) {
                    binding.sale.enterprise.setSelection(i);
                }
            }
        }
        binding.sale.enterprise.setItem(enterPriseNameList);
    }

    private void PreviousFragmentData() {
        assert getArguments() != null;
        portion = getArguments().getString("porson");

    }

    private void setOnClick() {
        binding.sale.startDate.setOnClickListener(this);
        binding.sale.EndDate.setOnClickListener(this);
        binding.sale.filterSearchBtn.setOnClickListener(this);
        binding.sale.resetBtn.setOnClickListener(this);
        binding.toolbar.backbtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startDate:
                timePicker();
                isStartDate = true;
                break;
            case R.id.EndDate:
                timePicker();
                break;

            case R.id.backbtn:
                pageNumber = 1;
                getActivity().onBackPressed();
                break;
            case R.id.filterSearchBtn:
                pageNumber = 1;
                isFirstLoad = 0; //if filter time list is null, data_not_found will be visible by (isFirstLoad = 0).
                purchaseHistoryLists.clear();//for filter
                purchasePendingLists.clear();//for filter
                purchaseReturnPendingLists.clear();//for filter
                purchaseReturnHistoryLists.clear();//for filter
                purchaseDeclineLists.clear();//for filter
                getAllListData();
                break;
            case R.id.resetBtn:
                startDate = null;//for reser
                endDate = null;
                companyId = null;
                enterPriseId = null;
                binding.sale.startDate.setText("");
                binding.sale.EndDate.setText("");//for reset
                isFirstLoad = 0;
                purchaseHistoryLists.clear();//for filter
                purchasePendingLists.clear();//for filter
                purchaseReturnPendingLists.clear();//for filter
                purchaseReturnHistoryLists.clear();//for filter
                purchaseDeclineLists.clear();//for filter
                pageNumber = 1;
                if (nullChecked()) {
                    binding.dataNotFound.setVisibility(View.GONE);
                    binding.purchaserRv.setVisibility(View.VISIBLE);
                    getAllListData();
                }
                break;
        }
    }

    private boolean nullChecked() {

        if (startDate == null && endDate == null && companyId == null && enterPriseId == null) {
            return true;
        }
        return false;
    }

    private void timePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                AllPurchaseListFragment.this,
                now.get(Calendar.YEAR), // Initial year selection
                now.get(Calendar.MONTH), // Initial month selection
                now.get(Calendar.DAY_OF_MONTH) // Inital day selection
        );
        assert getFragmentManager() != null;
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        int month = monthOfYear;
        if (month == 12) {
            month = 1;
        } else {
            month = monthOfYear + 1;
        }

        String mainMonth, mainDay;


        if (month <= 9) {
            mainMonth = "0" + month;
        } else {
            mainMonth = String.valueOf(month);
        }
        if (dayOfMonth <= 9) {
            mainDay = "0" + dayOfMonth;
        } else {
            mainDay = String.valueOf(dayOfMonth);
        }
        String selectedDate = year + "-" + mainMonth + "-" + mainDay;//set the selected date


        if (!isStartDate) {
            binding.sale.EndDate.setText(selectedDate);
            endDate = binding.sale.EndDate.getText().toString();
        } else {
            binding.sale.startDate.setText(selectedDate);
            startDate = binding.sale.startDate.getText().toString();
            isStartDate = false;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        purchaseHistoryLists.clear();
        purchasePendingLists.clear();
        purchaseReturnPendingLists.clear();
        purchaseReturnHistoryLists.clear();
        purchaseDeclineLists.clear();

        PreviousFragmentData();
        /**
         * get all list data
         */

        getAllListData();



        try {
            updateCurrentUserPermission(getActivity());
        } catch (Exception e) {
            Log.d("ERROR", e.getMessage());
        }
    }
    private void updateCurrentUserPermission(FragmentActivity activity) {
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

}