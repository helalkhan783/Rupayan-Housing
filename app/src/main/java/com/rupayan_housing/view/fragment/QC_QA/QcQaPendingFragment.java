package com.rupayan_housing.view.fragment.QC_QA;

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
import com.rupayan_housing.adapter.DeclinedQcQaAdapter;
import com.rupayan_housing.adapter.PendingQcQaListAdapter;
import com.rupayan_housing.adapter.QcHistoryAdapter;
import com.rupayan_housing.databinding.FragmentQcQaPendingBinding;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.DeclineQcQaList;
import com.rupayan_housing.serverResponseModel.Enterprize;
import com.rupayan_housing.serverResponseModel.LoginResponse;
import com.rupayan_housing.serverResponseModel.PendingQcQaList;
import com.rupayan_housing.serverResponseModel.QcHistoryList;
import com.rupayan_housing.utils.InternetCheckerRecyclerBuddy;
import com.rupayan_housing.utils.QcQaUtil;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.viewModel.CurrentPermissionViewModel;
import com.rupayan_housing.viewModel.QcHistoryViewModel;
import com.rupayan_housing.viewModel.Qc_QaViewModel;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class QcQaPendingFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener,
        View.OnClickListener {

    private FragmentQcQaPendingBinding binding;

    private Qc_QaViewModel qc_qaViewModel;
    private QcHistoryViewModel qcHistoryViewModel;
    private CurrentPermissionViewModel currentPermissionViewModel;
    /**
     * for enterPrise
     */
    List<String> enterPriseNameList;
    List<Enterprize> enterprizeList;


    private boolean isStartDate = false;
    boolean click = false;

    /**
     * for pagination
     */
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    public static int pageNumber = 1;
    private  int isFirstLoad = 0;
    private boolean endScroll = false;
    private LinearLayoutManager linearLayoutManager;

    private String portion, toDate, fromDate, enterPriseId, model;
    ProgressDialog progressDialog;

    List<DeclineQcQaList> declineQcQaLists = new ArrayList<>();
    List<QcHistoryList> qcHistoryLists = new ArrayList<>();
    List<PendingQcQaList> pendingQcQaLists = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_qc_qa_pending, container, false);
        qc_qaViewModel = new ViewModelProvider(this).get(Qc_QaViewModel.class);
        qcHistoryViewModel = new ViewModelProvider(this).get(QcHistoryViewModel.class);
        currentPermissionViewModel = new ViewModelProvider(this).get(CurrentPermissionViewModel.class);
        progressDialog = new ProgressDialog(getContext());
        linearLayoutManager = new LinearLayoutManager(getContext());

        binding.toolbar.toolbarTitle.setText(portion);
        binding.toolbar.setClickHandle(() -> {
            hideKeyboard(getActivity());
            getActivity().onBackPressed();
        });

/** click */

        click();

        /* getDataFromPreviousFragment();
         *//** get AllList from viewModel (Server)*//*
        getAllListData();

*/
        /** for pagination **/
        binding.pendingQcQaListRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
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


        binding.qcqaLayout.enterprise.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                enterPriseId = enterprizeList.get(position).getStoreID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return binding.getRoot();
    }

    private void getAllListData() {
        if (!new InternetCheckerRecyclerBuddy(getActivity()).isInternetAvailableHere(binding.pendingQcQaListRV, binding.noDataFound)) {
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

        /** for decline Qcqa */

        if (portion.equals(QcQaUtil.declinedQcQa)) {//for show decline qc-qa list from server
            binding.toolbar.toolbarTitle.setText("Qc/Qa Declined List");
            getDeclinedQcQaListFromServer();
            return;
        }
        /**
         *This is History qc qa list
         */
        if (portion.equals(QcQaUtil.qcQaHistory)) {
            binding.qcqaLayout.toDateLayout.setVisibility(View.VISIBLE);
            binding.toolbar.toolbarTitle.setText("QC/QA History List");
            getDataFromViewModel();
        }
        if (portion.equals(QcQaUtil.pendingQcQa)){
            binding.toolbar.toolbarTitle.setText("Qc/Qa Pending List");
            getPendingQcQaListFromServer();
        }


    }

    private void getDataFromViewModel() {
        qcHistoryViewModel.getQcHistoryList(getActivity(), String.valueOf(pageNumber), fromDate, toDate, enterPriseId, binding.qcqaLayout.searchModelEt.getText().toString()).observe(getViewLifecycleOwner(), qcHistoryResponse -> {
            progressDialog.dismiss();
            binding.progress.setVisibility(View.GONE);

            try {
                if (qcHistoryResponse == null) {
                    errorMessage(getActivity().getApplication(), "Something Wrong");
                    return;
                }
                if (qcHistoryResponse.getStatus() == 400) {
                    infoMessage(getActivity().getApplication(), "" + qcHistoryResponse.getMessage());
                    return;
                }

                if (qcHistoryResponse.getLists().isEmpty() || qcHistoryResponse.getLists() == null) {
                    managePaginationAndFilter();


                }

                manageFilterBtnAndRvAndDataNotFound();

                /** setData in RecyclerView*/

                qcHistoryLists.addAll(qcHistoryResponse.getLists());
                QcHistoryAdapter adapter = new QcHistoryAdapter(getActivity(), qcHistoryLists);
                binding.pendingQcQaListRV.setHasFixedSize(true);
                binding.pendingQcQaListRV.setLayoutManager(linearLayoutManager);
                binding.pendingQcQaListRV.setAdapter(adapter);


                /** enterPrise list set in spinner */
                setDatainEnterPrise(qcHistoryResponse.getEnterprizeList());

            } catch (Exception e) {
                Log.d("ERRROR", "" + e.getMessage());
            }
        });
    }


    private void getDeclinedQcQaListFromServer() {
        qc_qaViewModel.getDeclineQcQaList(getActivity(), String.valueOf(pageNumber), fromDate, toDate, enterPriseId, binding.qcqaLayout.searchModelEt.getText().toString())
                .observe(getViewLifecycleOwner(), response -> {
                    progressDialog.dismiss();
                    binding.progress.setVisibility(View.GONE);

                    try {
                        if (response == null) {
                            errorMessage(getActivity().getApplication(), "Something Wrong");
                            return;
                        }
                        if (response.getStatus() == 400) {
                            infoMessage(getActivity().getApplication(), "" + response.getMessage());
                            return;
                        }
                        /**
                         * All ok now set Decline Qc/qa list to recycler view
                         */
                        if (response.getLists().isEmpty() || response.getLists() == null) {
                            managePaginationAndFilter();
                        }

                        else {
                            manageFilterBtnAndRvAndDataNotFound();
                            declineQcQaLists.addAll(response.getLists());
                            DeclinedQcQaAdapter adapter = new DeclinedQcQaAdapter(getActivity(), declineQcQaLists);
                            binding.pendingQcQaListRV.setLayoutManager(linearLayoutManager);
                            binding.pendingQcQaListRV.setAdapter(adapter);
                            /** set enterprise in spinner */
                            setDatainEnterPrise(response.getEnterprizeList());
                        }
                    } catch (Exception e) {
                        Log.d("ERROR", "" + e.getMessage());
                    }

                });
    }

    private void getDataFromPreviousFragment() {
        assert getArguments() != null;
        portion = getArguments().getString("portion");
    }

    private void getPendingQcQaListFromServer() {
        qc_qaViewModel.getPendingQcQaList(getActivity(), String.valueOf(pageNumber), fromDate, toDate, enterPriseId, binding.qcqaLayout.searchModelEt.getText().toString())
                .observe(getViewLifecycleOwner(), response -> {
                    binding.progress.setVisibility(View.GONE);
                    progressDialog.dismiss();
                    try {
                        if (response == null) {
                            errorMessage(getActivity().getApplication(), "Something Wrong");
                            return;
                        }
                        if (response.getStatus() == 400) {
                            infoMessage(getActivity().getApplication(), "" + response.getMessage());
                            return;
                        }
                        /**
                         * All ok now set pending Qc/qa list to recycler view
                         */
                        if (response.getLists().isEmpty() || response.getLists() == null) {
                            managePaginationAndFilter();

                        }

                        manageFilterBtnAndRvAndDataNotFound();

                        /**
                         * now set qc qa data to recycler view
                         */
                        pendingQcQaLists.addAll(response.getLists());
                        PendingQcQaListAdapter adapter = new PendingQcQaListAdapter(getActivity(), pendingQcQaLists);
                        binding.pendingQcQaListRV.setLayoutManager(linearLayoutManager);
                        binding.pendingQcQaListRV.setAdapter(adapter);

                        /** set data in spinner */
                        setDatainEnterPrise(response.getEnterprizeList());

                    } catch (Exception e) {
                        Log.d("ERROR", "" + e.getMessage());
                    }
                });
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
                    binding.qcqaLayout.enterprise.setSelection(i);
                }
            }
        }
        binding.qcqaLayout.enterprise.setItem(enterPriseNameList);
    }


    private void click() {
        binding.toolbar.filterBtn.setOnClickListener(this);
        binding.qcqaLayout.startDate.setOnClickListener(this);
        binding.qcqaLayout.EndDate.setOnClickListener(this);
        binding.qcqaLayout.resetBtn.setOnClickListener(this);
        binding.qcqaLayout.filterSearchBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.filterBtn:

                if (binding.expandableView.isExpanded()) {
                    binding.expandableView.setExpanded(false);
                    return;
                } else {
                    binding.expandableView.setExpanded(true);
                }
             /*   if (click) {
                    binding.qcqaFilterLayout.setVisibility(View.GONE);
                    click = false;
                    return;
                }
                click = true;
                binding.qcqaFilterLayout.setVisibility(View.VISIBLE);
                */
                break;

            case R.id.startDate:
                timePicker();
                isStartDate = true;
                break;
            case R.id.EndDate:
                timePicker();
                break;

            case R.id.filterSearchBtn:
                pageNumber = 1;
                // for filter
                isFirstLoad = 0;
                declineQcQaLists.clear();
                pendingQcQaLists.clear();
                qcHistoryLists.clear();
                hideKeyboard(getActivity());
                getAllListData();
                break;

            case R.id.resetBtn:
                hideKeyboard(getActivity());

                pageNumber = 1;
                toDate = null;
                fromDate = null;
                model = null;
                enterPriseId = null;
                binding.qcqaLayout.startDate.setText("");
                binding.qcqaLayout.EndDate.setText("");
                binding.qcqaLayout.searchModelEt.setText("");

                //for filter
                isFirstLoad = 0;
                declineQcQaLists.clear();
                pendingQcQaLists.clear();
                qcHistoryLists.clear();

                if (toDate == null && fromDate == null && model == null && enterPriseId == null) {
                    binding.noDataFound.setVisibility(View.GONE);
                    binding.pendingQcQaListRV.setVisibility(View.VISIBLE);
                    getAllListData();
                }
        }
    }

    private void managePaginationAndFilter() {
        if (isFirstLoad == 0) { // if filter time list is null.  so then, data_not_found will be visible
            binding.pendingQcQaListRV.setVisibility(View.GONE);
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
        binding.pendingQcQaListRV.setVisibility(View.VISIBLE);
    }

    private void timePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
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
            binding.qcqaLayout.EndDate.setText(selectedDate);
            toDate = binding.qcqaLayout.EndDate.getText().toString();
        } else {
            binding.qcqaLayout.startDate.setText(selectedDate);
            fromDate = binding.qcqaLayout.startDate.getText().toString();
            isStartDate = false;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        declineQcQaLists.clear();
        qcHistoryLists.clear();
        pendingQcQaLists.clear();

        pageNumber = 1;
        isFirstLoad=0;
        getDataFromPreviousFragment();
        /** get AllList from viewModel (Server)*/
        getAllListData();


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


}