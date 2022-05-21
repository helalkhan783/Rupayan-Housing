package com.rupayan_housing.view.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.rupayan_housing.R;
import com.rupayan_housing.adapter.PendingRequisitionListAdapter;
import com.rupayan_housing.serverResponseModel.CompanyNameResponse;
import com.rupayan_housing.serverResponseModel.StoreNameResponse;
import com.rupayan_housing.viewModel.PendingRequisitionListViewModel;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PendingRequisitionListFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    private PendingRequisitionListViewModel pendingRequisitionListViewModel;
    private View view;

    @BindView(R.id.toolbarTitle)
    TextView toolBar;
    @BindView(R.id.pendingRequisitionListRv)
    RecyclerView pendingRequisitionListRv;
    @BindView(R.id.startDate)
    TextView startDate;
    @BindView(R.id.endDate)
    TextView endDate;
    @BindView(R.id.companyNameDropDown)
    MaterialSpinner companyNameDropDown;
    @BindView(R.id.enterpriseDropDown)
    MaterialSpinner enterpriseNameDropDown;
    @BindView(R.id.noDataFound)
    TextView noDataFound;
    List<CompanyNameResponse> companyNameResponseList;
    List<String> companyList;
    List<String> enterpriseList;
    List<StoreNameResponse> storeNameResponseList;
    List<String> storeNameList;
    String selectedStoreId;
    String selectedCompanyId;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pending_requisition_list, container, false);
        ButterKnife.bind(this, view);
        pendingRequisitionListViewModel = new ViewModelProvider(getActivity()).get(PendingRequisitionListViewModel.class);
        // pendingRequisitionListViewModel = ViewModelProviders.of(this).get(PendingRequisitionListViewModel.class);
        getDataFromPreviousFragment();
        /**
         * show page header info
         */


        getActivity().runOnUiThread(this::showPageHeaderInfo);

        /**
         * show data in recyclerView from server
         */

        getActivity().runOnUiThread(() -> showPendingRequisitionListInRecyclerView());


        companyNameDropDown.setOnItemSelectedListener((view, position, id, item) -> {
            companyNameDropDown.setSelected(true);
            selectedCompanyId = companyNameResponseList.get(position).getCustomerID();
        });
        enterpriseNameDropDown.setOnItemSelectedListener((view, position, id, item) -> {
            enterpriseNameDropDown.setSelected(true);
            selectedStoreId = storeNameResponseList.get(position).getStoreID();
        });
        return view;
    }

    private void showPageHeaderInfo() {
        pendingRequisitionListViewModel.getPenReqDetailsPageInfo(getActivity()).observe(getViewLifecycleOwner(), pageInfoResponse -> {


            companyNameResponseList = new ArrayList<>();
            storeNameResponseList = new ArrayList<>();
            companyList = new ArrayList<>();
            enterpriseList = new ArrayList<>();
            storeNameList = new ArrayList<>();

          /*  companyList.clear();
            enterpriseList.clear();
            storeNameList.clear();*/
            //List<String> k = Collections.singletonList("");

            /**
             * now set the enterprise name list
             */

            /*pageInfoResponse.getStore().forEach(storeNameResponse -> {
                storeNameResponseList.add(storeNameResponse);
            });*/

            for (int i = 0; i < pageInfoResponse.getStore().size(); i++) {
                storeNameResponseList.add(pageInfoResponse.getStore().get(i));
            }


            // storeNameResponseList.forEach(storeNameResponse -> storeNameList.add(storeNameResponse.getStoreName()));
            for (int i = 0; i < storeNameResponseList.size(); i++) {
                storeNameList.add(storeNameResponseList.get(i).getStoreName());
            }
            enterpriseNameDropDown.setItems(storeNameList);//set items


          /*  pageInfoResponse.getCustomer().forEach(companyNameResponse -> {
                companyNameResponseList.add(companyNameResponse);
                *//**
             * for set only company name
             *//*
                companyList.add(companyNameResponse.getCompanyName() + "@" + companyNameResponse.getCustomerFname());

                *//**
             * now set the company name to dropdown
             *//*
                companyNameDropDown.setItems(companyList);

            });*/

            for (int i = 0; i < pageInfoResponse.getCustomer().size(); i++) {
                companyNameResponseList.add(pageInfoResponse.getCustomer().get(i));
                /**
                 * for set only company name
                 */
                companyList.add(pageInfoResponse.getCustomer().get(i).getCompanyName() + "@" + pageInfoResponse.getCustomer().get(i).getCustomerFname());
                /**
                 * now set the company name to dropdown
                 */
                companyNameDropDown.setItems(companyList);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getDataFromPreviousFragment() {
        toolBar.setText("Pending Req. List");
        /**
         * we want to show set current date in dateView
         */
     /*   DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();*/

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        //System.out.println(formatter.format(date));
        String currentDate = formatter.format(date);


        /*System.out.println(dtf.format(now));*/
        startDate.setText(currentDate);
        endDate.setText(currentDate);
    }

    private void showPendingRequisitionListInRecyclerView() {
        pendingRequisitionListRv.setLayoutManager(new LinearLayoutManager(getContext()));
        pendingRequisitionListRv.setHasFixedSize(true);
        pendingRequisitionListViewModel.getPendingRequisitionList(getActivity(), "", "", "", "").observe(getViewLifecycleOwner(), pendingRequisitionList -> {
            PendingRequisitionListAdapter adapter = new PendingRequisitionListAdapter(getActivity(), pendingRequisitionList);
            pendingRequisitionListRv.setAdapter(adapter);
        });
    }

    @OnClick({R.id.backbtn})
    public void backBtnClick() {
        getActivity().onBackPressed();
    }


    @OnClick(R.id.startDate)
    public void clickStartDate() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dialog = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR), // Initial year selection
                now.get(Calendar.MONTH), // Initial month selection
                now.get(Calendar.DAY_OF_MONTH) // Inital day selection
        );
        dialog.show(getActivity().getSupportFragmentManager(), "Datepickerdialog");
    }


    @OnClick(R.id.endDate)
    public void clickEndDate() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dialog = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR), // Initial year selection
                now.get(Calendar.MONTH), // Initial month selection
                now.get(Calendar.DAY_OF_MONTH) // Inital day selection
        );
        dialog.show(getActivity().getSupportFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        int month = monthOfYear;
        if (month == 12) {
            month = 1;
        } else {
            month = monthOfYear + 1;
        }
        String selectedDate = year + "-" + month + "-" + dayOfMonth;//set the selected date
        startDate.setText(selectedDate);
        endDate.setText(selectedDate);
    }


    /**
     * when user want to search by date,company and enterprise name
     */
    @OnClick(R.id.searchBtn)
    public void searchBtnClick() {
        String startdate = startDate.getText().toString();
        String enddate = endDate.getText().toString();

        pendingRequisitionListViewModel.getPendingRequisitionList(getActivity(), selectedStoreId, startdate, enddate, selectedCompanyId)
                .observe(getViewLifecycleOwner(), pendingRequisitionListResponses -> {
                    /**
                     * now set the search pending requisition list to the current list
                     */
                    if (pendingRequisitionListResponses.isEmpty()) {
                        pendingRequisitionListRv.setVisibility(View.GONE);
                        noDataFound.setVisibility(View.VISIBLE);
                        return;
                    }
                    PendingRequisitionListAdapter adapter = new PendingRequisitionListAdapter(getActivity(), pendingRequisitionListResponses);
                    pendingRequisitionListRv.setAdapter(adapter);
                });
    }
}