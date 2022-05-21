package com.rupayan_housing.view.fragment.all_report.packeting_report;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.FragmentPacketingReportBinding;
import com.rupayan_housing.permission.SharedPreferenceForZoneReprt;
import com.rupayan_housing.permission.SharedPreferenceReportForMill;
import com.rupayan_housing.utils.ReportUtils;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.view.fragment.all_report.packeting_report.miller.PacketMIllerReportResponse;
import com.rupayan_housing.view.fragment.all_report.packeting_report.page_data_response.PacketReportAssociationList;
import com.rupayan_housing.view.fragment.all_report.packeting_report.page_data_response.PacketReportMillerList;
import com.rupayan_housing.view.fragment.all_report.packeting_report.page_data_response.PacketReportReferer;
import com.rupayan_housing.view.fragment.all_report.packeting_report.page_data_response.PacketingPageDataReportResponse;
import com.rupayan_housing.view.fragment.all_report.packeting_report.page_data_response.ProductionType;
import com.rupayan_housing.view.fragment.all_report.packeting_report.store.PacketReportStore;
import com.rupayan_housing.view.fragment.all_report.packeting_report.store.PacketReportStorteResponse;

import com.rupayan_housing.viewModel.report_all_view_model.PacketingReportViewModel;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class PacketingReportFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener, View.OnClickListener {
    private PacketingReportViewModel packetingReportViewModel;

    private ProgressDialog progressDialog;
    private boolean isStartDate = false;//for detect select start or end date


    /**
     * for Association list
     */
    private List<String> associationNamelist;
    private List<PacketReportAssociationList> associationLists;
    /**
     * for store List
     */
    private List<String> storeNameList;
    private List<PacketReportStore> storeLists;
    /**
     * for referrer list
     */
    private List<String> referrerNameList;
    private List<PacketReportReferer> packetReportReferers;
    /**
     * for referrer list
     */
    private List<String> productionNameList;
    private List<ProductionType> productionTypeList;


    /**
     * miller List for type id 6 ,7
     */
    private List<String> millerNameList;
    private List<PacketReportMillerList> millerLists;

    private String selectAssociationId, millerProfileId, referrerId, storeId, productionId;
    String startDate, endDate, portion, pageName;
    String profileTypeId, profileId;//for profile type id 6,7
    FragmentPacketingReportBinding binding;
    SharedPreferenceReportForMill sharedPreferenceReportForMill;
    SharedPreferenceForZoneReprt sharedPreferenceForZoneReprt;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_packeting_report, container, false);

        packetingReportViewModel = new ViewModelProvider(this).get(PacketingReportViewModel.class);
        sharedPreferenceReportForMill = new SharedPreferenceReportForMill(getActivity());
        sharedPreferenceForZoneReprt = new SharedPreferenceForZoneReprt(getActivity());
        binding.toolbar.setClickHandle(() -> {
            sharedPreferenceReportForMill.deleteMillData();
            sharedPreferenceForZoneReprt.deleteZoneData();
             hideKeyboard(getActivity());
            getActivity().onBackPressed();
        });


        progressDialog = new ProgressDialog(getContext());
        getPreviousFragmentData();
        if (portion.equals(ReportUtils.iodineUsedReport)) {
            binding.referrerLayout.setVisibility(View.GONE);
        }

        if (portion.equals(ReportUtils.productionReport)) {
            binding.referrerLayout.setVisibility(View.GONE);
            binding.productionTypeLayout.setVisibility(View.VISIBLE);
        }
        /** get Data from credential */
        profileId = getProfileId(getActivity().getApplication());


        getPageData();

        setOnClick();
        binding.toolbar.setClickHandle(() -> {
            hideKeyboard(getActivity());
            getActivity().onBackPressed();
        });


        /** for search button Click */
        binding.search.setOnClickListener(v -> {
            /** purchase Report from start here */
    /*        if (binding.startDate.getText().toString().isEmpty()) {
                binding.startDate.setError("Empty start date");
                binding.startDate.requestFocus();
                return;
            }
            if (binding.EndDate.getText().toString().isEmpty()) {
                binding.EndDate.setError("Empty start date");
                binding.EndDate.requestFocus();
                return;
            }
*/

            Bundle bundle = new Bundle();
            bundle.putString("startDate", binding.startDate.getText().toString());
            bundle.putString("endDate", binding.EndDate.getText().toString());
            bundle.putString("millerProfileId", sharedPreferenceReportForMill.getMillId());
            bundle.putString("referrerId", referrerId);
            bundle.putString("portion", portion);
            bundle.putString("storeId", storeId);
            bundle.putString("productionId", productionId);
            bundle.putString("pageName", pageName);


           /* if (checkProfileType()){
                bundle.putString("associationId", selectAssociationId);//for miller means (Profile TypeId 6,7)
                Navigation.findNavController(getView()).navigate(R.id.action_packetingReportFragment_to_purchaseReturnListFragment, bundle);
            }*/

            bundle.putString("associationId", sharedPreferenceForZoneReprt.getZoneId());//for admin means (Profile TypeId 4)
            Navigation.findNavController(getView()).navigate(R.id.action_packetingReportFragment_to_purchaseReturnListFragment, bundle);

        });

        /**
         * spinner click handle
         */

        binding.selectAssociation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectAssociationId = associationLists.get(position).getStoreID();
                sharedPreferenceForZoneReprt.saveZone(associationLists.get(position).getStoreID());
                getMillerPageData(selectAssociationId);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.selectReferrer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                referrerId = packetReportReferers.get(position).getCustomerID();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.miller.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                millerProfileId = millerLists.get(position).getStoreID();
                sharedPreferenceReportForMill.saveMill(millerLists.get(position).getStoreID());
                getStoreData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.selectStore.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                storeId = storeLists.get(position).getStoreID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.productionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                productionId = String.valueOf(productionTypeList.get(position).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return binding.getRoot();
    }

    private void getStoreData() {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }
        packetingReportViewModel.getPacketStore(getActivity(), millerProfileId).observe(getViewLifecycleOwner(), new Observer<PacketReportStorteResponse>() {
            @Override
            public void onChanged(PacketReportStorteResponse response) {

                if (response == null) {
                    errorMessage(getActivity().getApplication(), "something wrong");
                    return;
                }
                if (response.getStatus() == 400) {
                    errorMessage(getActivity().getApplication(), response.getMessage());
                    return;
                }
                if (response.getStatus() == 200) {
                    setInStoreSpinner(response);
                }
            }
        });

    }

    private void setInStoreSpinner(PacketReportStorteResponse response) {
        storeNameList = new ArrayList<>();
        storeNameList.clear();
        storeLists = new ArrayList<>();
        storeLists.clear();
        storeLists.addAll(response.getMillerList());

        for (int i = 0; i < response.getMillerList().size(); i++) {
            storeNameList.add(response.getMillerList().get(i).getStoreName());
        }
        binding.selectStore.setItem(storeNameList);
    }


    private void getPreviousFragmentData() {
        try {

            portion = getArguments().getString("portion");
            pageName = getArguments().getString("pageName");
            binding.toolbar.toolbarTitle.setText(pageName);
        } catch (Exception e) {
            Log.d("ERROR", e.getMessage());
        }

    }

    private void getMillerPageData(String associationId) {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        packetingReportViewModel.getPacketMillerByAssociation(getActivity(), associationId).observe(getViewLifecycleOwner(), new Observer<PacketMIllerReportResponse>() {
            @Override
            public void onChanged(PacketMIllerReportResponse response) {
                progressDialog.dismiss();
                if (response == null) {
                    errorMessage(getActivity().getApplication(), "something wrong");
                    return;
                }
                if (response.getStatus() == 400) {
                    errorMessage(getActivity().getApplication(), response.getMessage());
                    return;
                }
                if (response.getStatus() == 200) {
                    /** now set data to select spinner */
                    setDataInMillerSpinner(response);
                }
            }
        });
    }

    private void setDataInMillerSpinner(PacketMIllerReportResponse response) {
        millerNameList = new ArrayList<>();
        millerNameList.clear();
        millerLists = new ArrayList<>();
        millerLists.clear();

        millerLists.addAll(response.getMillerList());
        for (int i = 0; i < response.getMillerList().size(); i++) {
            millerNameList.add("" + response.getMillerList().get(i).getFullName());
        }
        binding.miller.setItem(millerNameList);
    }


    private void getPageData() {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }
        progressDialog.show();
        packetingReportViewModel.getPacketReportPageData(getActivity(), profileId).observe(getViewLifecycleOwner(), new Observer<PacketingPageDataReportResponse>() {
            @Override
            public void onChanged(PacketingPageDataReportResponse response) {
                progressDialog.dismiss();
                try {
                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "something wrong");
                        return;
                    }
                    if (response.getStatus() == 400) {
                        infoMessage(getActivity().getApplication(), response.getMessage());
                        return;
                    }
                    if (response.getStatus() == 200) {
                        setDataInView(response);
                    }
                } catch (Exception e) {
                    Log.d("Error", e.getMessage());
                }
            }
        });

    }


    private void setDataInView(PacketingPageDataReportResponse response) {

        /** for association List */

        if (response.getAsociationList().isEmpty() || response.getAsociationList() == null) {
            Toast.makeText(getContext(), "Association is nul", Toast.LENGTH_SHORT).show();
        } else {

            associationLists = new ArrayList<>();
            associationLists.clear();
            associationLists.addAll(response.getAsociationList());
            associationNamelist = new ArrayList<>();
            associationNamelist.clear();

            for (int i = 0; i < response.getAsociationList().size(); i++) {
                associationNamelist.add("" + response.getAsociationList().get(i).getDisplayName());
                if (sharedPreferenceForZoneReprt.getZoneId() != null || !sharedPreferenceForZoneReprt.getZoneId().isEmpty()){
                    if (sharedPreferenceForZoneReprt.getZoneId().equals(associationLists.get(i).getStoreID())){
                        binding.selectAssociation.setSelection(i);
                    }
                }
            }
            binding.selectAssociation.setItem(associationNamelist);

            if (getProfileTypeId(getActivity().getApplication()).equals("6")) {
                if (associationLists.size() == 1) {
                    binding.selectAssociation.setSelection(0);
                    selectAssociationId = associationLists.get(0).getStoreID();
                    getMillerPageData(selectAssociationId);
                }
            }


        }

        /** for referrer list */
        referrerNameList = new ArrayList<>();
        referrerNameList.clear();
        packetReportReferers = new ArrayList<>();
        packetReportReferers.addAll(response.getReferer());

        for (int i = 0; i < response.getReferer().size(); i++) {
            referrerNameList.add("" + response.getReferer().get(i).getCustomerFname());
        }
        binding.selectReferrer.setItem(referrerNameList);


        /** for production type*/
        productionTypeList = new ArrayList<>();
        productionTypeList.clear();
        productionTypeList.addAll(response.getProductionType());

        productionNameList = new ArrayList<>();
        productionNameList.clear();
        for (int i = 0; i < response.getProductionType().size(); i++) {
            productionNameList.add("" + response.getProductionType().get(i).getName());
        }
        binding.productionType.setItem(productionNameList);

        /**   miller list for ProfileTypeId 6,7  */

        if (checkProfileType()) {

            millerNameList = new ArrayList<>();
            millerNameList.clear();
            millerLists = new ArrayList<>();
            millerLists.clear();

            millerLists.addAll(response.getMillerList());
            for (int i = 0; i < response.getMillerList().size(); i++) {
                millerNameList.add("" + response.getMillerList().get(i).getDisplayName());

                Toast.makeText(getContext(), "Previous"+sharedPreferenceReportForMill.getMillId(), Toast.LENGTH_SHORT).show();

                if (!sharedPreferenceReportForMill.getMillId().isEmpty() || sharedPreferenceReportForMill.getMillId() != null){
                    if (sharedPreferenceReportForMill.getMillId().equals(millerLists.get(i).getStoreID())){
                        binding.miller.setSelection(i);
                    }
                }
               /* if (!sharedPreferenceReportForMill.getMillId().isEmpty() || sharedPreferenceReportForMill.getMillId() !=null){
                    if (sharedPreferenceReportForMill.getMillId().equals(millerLists.get(i).getStoreID())){
                        binding.miller.setSelection(i);
                    }
                }*/
            }
            binding.miller.setItem(millerNameList);


        }
    }

    private void setOnClick() {
        binding.startDate.setOnClickListener(this);
        binding.EndDate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startDate:
                isStartDate = true;
                showDatePickerDialog();
                break;

            case R.id.EndDate:
                showDatePickerDialog();
                break;
        }
    }

    private void showDatePickerDialog() {
        Calendar now = Calendar.getInstance();
        com.wdullaer.materialdatetimepicker.date.DatePickerDialog dialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR), // Initial year selection
                now.get(Calendar.MONTH), // Initial month selection
                now.get(Calendar.DAY_OF_MONTH) // Initial day selection
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
            binding.EndDate.setText(selectedDate);
            binding.EndDate.setError(null);
        } else {
            binding.startDate.setText(selectedDate);
            binding.startDate.setError(null);
            isStartDate = false;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
         selectAssociationId = null;
        millerProfileId = null;
        referrerId = null;
        startDate = null;
        endDate = null;
        selectAssociationId = null;
        storeId = null;
        productionId = null;

    }

}