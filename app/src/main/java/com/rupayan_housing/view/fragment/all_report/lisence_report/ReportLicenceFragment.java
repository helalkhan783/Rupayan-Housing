package com.rupayan_housing.view.fragment.all_report.lisence_report;

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

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.FragmentReportLisenceBinding;
import com.rupayan_housing.permission.SharedPreferenceForZoneReprt;
import com.rupayan_housing.permission.SharedPreferenceReportForMill;
import com.rupayan_housing.serverResponseModel.MillType;
import com.rupayan_housing.serverResponseModel.MonitoringTypeList;
import com.rupayan_housing.serverResponseModel.ProcessType;
import com.rupayan_housing.serverResponseModel.ReportZoneList;
import com.rupayan_housing.serverResponseModel.response.MillStatus;
import com.rupayan_housing.utils.DashBoardReportUtils;
import com.rupayan_housing.utils.ReportUtils;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.serverResponseModel.miller_response.LicenceMillerList;
import com.rupayan_housing.serverResponseModel.miller_response.MillerLicenceResponse;
import com.rupayan_housing.serverResponseModel.response.MIllerLienceReportCertificateType;
import com.rupayan_housing.serverResponseModel.response.MillerLicenceReportResponse;
import com.rupayan_housing.serverResponseModel.response.MillerReportAsociationList;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class ReportLicenceFragment extends BaseFragment
        implements DatePickerDialog.OnDateSetListener, View.OnClickListener {
    private FragmentReportLisenceBinding binding;
    private LicenceReportViewModel licenceReportViewModel;
    private boolean isStartDate = false;

    private List<MillerReportAsociationList> asociationLists;

    private List<MIllerLienceReportCertificateType> certificateTypeList;
    private List<ReportZoneList> zoneLists;


    List<String> typeList;
    List<String> monitoringTypeList;
    List<MonitoringTypeList> monitoringTypes;
    private List<LicenceMillerList> licenceMillerLists;

    String associationId, portion, pageName;
    private String processTypeId, milltTypeId, millStatusId, millerProfileId, supplierId, certificateTypeID, zoneID, monitoringType, startDate, endDate;

    private String associationID;

    /**
     * for mill report page data
     */
    List<ProcessType> processTypes;
    List<MillType> millTypes;
    List<MillStatus> millStatuses;
    SharedPreferenceForZoneReprt sharedPreferenceForZoneReprt;
    SharedPreferenceReportForMill sharedPreferenceReportForMill;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_report_lisence, container, false);
        licenceReportViewModel = new ViewModelProvider(this).get(LicenceReportViewModel.class);
        sharedPreferenceForZoneReprt = new SharedPreferenceForZoneReprt(getActivity());
        sharedPreferenceReportForMill = new SharedPreferenceReportForMill(getActivity());
        getPreviousFragmentData();
         binding.toolbar.setClickHandle(() -> {
            sharedPreferenceForZoneReprt.deleteZoneData();
            sharedPreferenceReportForMill.deleteMillData();
            hideKeyboard(getActivity());
            getActivity().onBackPressed();
        });

        setOnClick();
/** get Page Data */
     //   getDataForPage();


        binding.selectAssociation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                associationId = asociationLists.get(position).getStoreID();
                zoneID = asociationLists.get(position).getZoneID();
                sharedPreferenceForZoneReprt.saveZone(asociationLists.get(position).getStoreID());
                getMillerData(associationId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.selectLicence.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                certificateTypeID = certificateTypeList.get(position).getCertificateTypeID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.miller.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                millerProfileId = licenceMillerLists.get(position).getVendorID();
                sharedPreferenceReportForMill.saveMill(licenceMillerLists.get(position).getVendorID());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

/*

        // for monitoring report list
        binding.zone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                zoneID = zoneLists.get(position).getZoneID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
*/

        binding.monitoringType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    monitoringType = monitoringTypes.get(position).getTypeID();
                } catch (Exception e) {
                    Log.d("ERROR", e.getMessage());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // for mill report page data
        binding.processType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                processTypeId = processTypes.get(position).getProcessTypeID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.millType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                milltTypeId = millTypes.get(position).getMillTypeID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.millStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                millStatusId = String.valueOf(millStatuses.get(position).getStatus());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


/** for search button Click */
        binding.search.setOnClickListener(v -> {
            /** purchase Report from start here */
            if (!portion.equals(ReportUtils.projectReport)) {
              /*  if (binding.startDate.getText().toString().isEmpty()) {
                    binding.startDate.setError("Empty start date");
                    binding.startDate.requestFocus();
                    return;
                }
                if (binding.EndDate.getText().toString().isEmpty()) {
                    binding.EndDate.setError("Empty start date");
                    binding.EndDate.requestFocus();
                    return;
                }*/
            }

            Bundle bundle = new Bundle();
            bundle.putString("startDate", binding.startDate.getText().toString());
            bundle.putString("endDate", binding.EndDate.getText().toString());
            bundle.putString("millerProfileId", sharedPreferenceReportForMill.getMillId());
            bundle.putString("supplierId", supplierId);
            bundle.putString("certificateTypeID", certificateTypeID);
            bundle.putString("portion", portion);
            bundle.putString("pageName", pageName);
            bundle.putString("zone", zoneID);
            bundle.putString("monitoringType", monitoringType);
            bundle.putString("selectAssociationId", sharedPreferenceForZoneReprt.getZoneId());
            // extra data for mill report

            bundle.putString("processTypeId", processTypeId);
            bundle.putString("milltTypeId", milltTypeId);
            bundle.putString("millStatusId", millStatusId);


            Navigation.findNavController(getView()).navigate(R.id.action_reportLicenceFragment2_to_purchaseReturnListFragment, bundle);


        });

        return binding.getRoot();
    }

    private void getDataForPage() {
        if (portion.equals(ReportUtils.qcqaReport)) {
            //cause employee and qcqa  report has only two field
            binding.licenceLayout.setVisibility(View.GONE);
            binding.monitorTypeLayout.setVisibility(View.GONE);
            //get page Data for LicenceReport, LicenceExpireReport,QcQaReport

        }
        if (portion.equals(ReportUtils.employeeReport)) {
            binding.licenceLayout.setVisibility(View.GONE);
            binding.monitorTypeLayout.setVisibility(View.GONE);
            binding.dateLayout.setVisibility(View.GONE);
        }
        if (portion.equals(ReportUtils.projectReport)) {
            binding.licenceLayout.setVisibility(View.GONE);
            binding.monitorTypeLayout.setVisibility(View.GONE);
            binding.millReportlayout.setVisibility(View.VISIBLE);
        }


        if (portion.equals(ReportUtils.monitoringReport)) {
            //page data for monitoring Report
            binding.associationLayout.setVisibility(View.VISIBLE);
            binding.layoutForMonitoringReport.setVisibility(View.VISIBLE);
            binding.licenceLayout.setVisibility(View.GONE);
            binding.millLayout.setVisibility(View.GONE);
            //page data for monitoring Report
            getPageDataForMonitoringReport();
            getPageData();
            return;
        }
        if (portion.equals(ReportUtils.userReport) ||
                portion.equals(ReportUtils.customerReport)) {
            binding.monitorTypeLayout.setVisibility(View.GONE);
        }

        // handle dashboard report UI
        if (portion.equals(DashBoardReportUtils.iodization) ||
                portion.equals(DashBoardReportUtils.iodineStock) ||
                portion.equals(DashBoardReportUtils.acQa) ||
                portion.equals(DashBoardReportUtils.topTenMiller) ||
                portion.equals(DashBoardReportUtils.monitoring) ||
                portion.equals(DashBoardReportUtils.agencyMonitoring) ||
                portion.equals(DashBoardReportUtils.issueMonitoring) ||
                portion.equals(DashBoardReportUtils.zoneList)) {
            binding.licenceEmployeeAndQcqaLayout.setVisibility(View.VISIBLE);
            binding.associationLayout.setVisibility(View.VISIBLE);
            binding.layoutForMonitoringReport.setVisibility(View.GONE);
            binding.licenceLayout.setVisibility(View.GONE);
            binding.millLayout.setVisibility(View.GONE);
            getPageData();
            return;
        }

        if (portion.equals(DashBoardReportUtils.purchase) ||
                portion.equals(DashBoardReportUtils.production) ||
                portion.equals(DashBoardReportUtils.sale)) {
            binding.licenceEmployeeAndQcqaLayout.setVisibility(View.GONE);
            binding.layoutForMonitoringReport.setVisibility(View.GONE);
            binding.monitorTypeLayout.setVisibility(View.GONE);
        }

        getPageData();
    }

    private void getPageDataForMonitoringReport() {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        licenceReportViewModel.monitoringReportPageData(getActivity()).observe(getViewLifecycleOwner(), response -> {
            progressDialog.dismiss();
            try {
                if (response == null) {
                    errorMessage(getActivity().getApplication(), "something wrong");
                    return;
                }
                if (response.getStatus() == 400) {
                    infoMessage(requireActivity().getApplication(), response.getMessage());
                    return;
                }
                if (response.getStatus() == 200) {
                    List<String> zoneNameList = new ArrayList<>();
                    zoneLists = new ArrayList<>();
                    zoneLists.addAll(response.getZoneList());
                    for (int i = 0; i < response.getZoneList().size(); i++) {
                        zoneNameList.add("" + response.getZoneList().get(i).getZoneName());
                    }
                    binding.zone.setItem(zoneNameList);

                    /*typeList.clear();
                      typeList = new ArrayList<>();*/
                    monitoringTypeList = new ArrayList<>();
                    monitoringTypes = new ArrayList<>();
                    monitoringTypes.addAll(response.getMonitoringType());
                    for (int i = 0; i < response.getMonitoringType().size(); i++) {
                        monitoringTypeList.add(response.getMonitoringType().get(i).getMonitoringTypeName());
                    }

                    binding.monitoringType.setItem(monitoringTypeList);
                }

            } catch (Exception e) {
                Log.d("Error ", e.getMessage());
            }
        });
    }

    private void getPreviousFragmentData() {
        portion = getArguments().getString("portion");
        pageName = getArguments().getString("pageName");
        binding.toolbar.toolbarTitle.setText(pageName);


    }

    private void setOnClick() {
        binding.startDate.setOnClickListener(this);
        binding.EndDate.setOnClickListener(this);
    }


    private void getMillerData(String association) {
        licenceReportViewModel.getMillerData(getActivity(), association).observe(getViewLifecycleOwner(), new Observer<MillerLicenceResponse>() {
            @Override
            public void onChanged(MillerLicenceResponse response) {
                if (response == null) {
                    errorMessage(getActivity().getApplication(), "something wrong");
                    return;
                }
                if (response.getStatus() == 400) {
                    errorMessage(requireActivity().getApplication(), response.getMessage());
                    return;
                }
                if (response.getStatus() == 200) {
                    setDataInMillerSpinner(response);
                }
            }
        });


    }

    private void setDataInMillerSpinner(MillerLicenceResponse response) {
        List<String> millerNameList = new ArrayList<>();
        millerNameList.clear();
        licenceMillerLists = new ArrayList<>();
        licenceMillerLists.clear();
        licenceMillerLists.addAll(response.getMillerList());

        for (int i = 0; i < response.getMillerList().size(); i++) {
            millerNameList.add("" + response.getMillerList().get(i).getFullName());
            if (sharedPreferenceReportForMill.getMillId() !=null || !sharedPreferenceReportForMill.getMillId().isEmpty()){
                if (sharedPreferenceReportForMill.getMillId().equals(licenceMillerLists.get(i).getVendorID())){
                    binding.miller.setSelection(i);
                }
            }
        }
        binding.miller.setItem(millerNameList);

    }


    private void getPageData() {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        licenceReportViewModel.getLicencePageData(getActivity(), getProfileId(getActivity().getApplication())).observe(getViewLifecycleOwner(), response -> {
            progressDialog.dismiss();
            try {
                if (response == null) {
                    errorMessage(getActivity().getApplication(), "something wrong");
                    return;
                }
                if (response.getStatus() == 400) {
                    infoMessage(requireActivity().getApplication(), response.getMessage());
                    return;
                }
                if (response.getStatus() == 200) {
                    setDataInSpinner(response);

                }

            } catch (Exception e) {
                Log.d("Error ", e.getMessage());

            }
        });
    }

    private void setDataInSpinner(MillerLicenceReportResponse response) {
        /** for association list*/
        List<String> associationNameList = new ArrayList<>();
        associationNameList.clear();
        asociationLists = new ArrayList<>();
        asociationLists.clear();
        asociationLists.addAll(response.getAsociationList());
        for (int i = 0; i < response.getAsociationList().size(); i++) {
            associationNameList.add("" + response.getAsociationList().get(i).getDisplayName());
            if (!sharedPreferenceForZoneReprt.getZoneId().isEmpty() ||sharedPreferenceForZoneReprt.getZoneId() !=null){
                if (sharedPreferenceForZoneReprt.getZoneId().equals(asociationLists.get(i).getStoreID())){
                    binding.selectAssociation.setSelection(i);
                }
            }
        }
        binding.selectAssociation.setItem(associationNameList);

        if (getProfileTypeId(getActivity().getApplication()).equals("6")) {
            if (asociationLists.size() == 1) {
                binding.selectAssociation.setSelection(0);
                associationId = asociationLists.get(0).getStoreID();
                getMillerData(associationId);
            }
        }


        /** for licence list*/
        List<String> licenceNameList = new ArrayList<>();
        licenceNameList.clear();
        certificateTypeList = new ArrayList<>();
        certificateTypeList.clear();
        certificateTypeList.addAll(response.getCertificateTypes());


        for (int i = 0; i < response.getCertificateTypes().size(); i++) {
            licenceNameList.add("" + response.getCertificateTypes().get(i).getCertificateTypeName());
        }
        binding.selectLicence.setItem(licenceNameList);


        associationID = response.getAssociationID();


        if (checkProfileType()) {
            List<String> millerNameList = new ArrayList<>();
            millerNameList.clear();
            licenceMillerLists = new ArrayList<>();
            licenceMillerLists.clear();
            licenceMillerLists.addAll(response.getMillerList());

            for (int i = 0; i < response.getMillerList().size(); i++) {
                millerNameList.add("" + response.getMillerList().get(i).getDisplayName());
            }
            binding.miller.setItem(millerNameList);
        }


        //set process type

        processTypes = new ArrayList<>();
        processTypes.clear();
        processTypes.addAll(response.getProcessType());
        List<String> processTypeNameList = new ArrayList<>();
        processTypeNameList.clear();
        for (int i = 0; i < processTypes.size(); i++) {
            processTypeNameList.add("" + processTypes.get(i).getProcessTypeName());
        }
        binding.processType.setItem(processTypeNameList);

        //set mill type
        millTypes = new ArrayList<>();
        millTypes.clear();
        millTypes.addAll(response.getMillTypes());
        List<String> millTypeNamelist = new ArrayList<>();

        for (int i = 0; i < millTypes.size(); i++) {
            millTypeNamelist.add("" + millTypes.get(i).getMillTypeName());
        }
        binding.millType.setItem(millTypeNamelist);


        //set mill status
        millStatuses = new ArrayList<>();
        millStatuses.clear();
        millStatuses.addAll(response.getMillStatus());
        List<String> millStatusNameList = new ArrayList<>();

        for (int i = 0; i < millStatuses.size(); i++) {
            millStatusNameList.add("" + millStatuses.get(i).getName());
        }
        binding.millStatus.setItem(millStatusNameList);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startDate:
                isStartDate = true;
                timePicker();
                break;

            case R.id.EndDate:
                timePicker();
                break;
        }
    }

    private void timePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                ReportLicenceFragment.this,
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
        startDate = null;
        endDate = null;
//      selectAssociationId = null;
        millerProfileId = null;
        supplierId = null;
        certificateTypeID = null;
        zoneID = null;
        monitoringType = null;
        associationId = null;
        processTypeId = null;
        milltTypeId = null;
        millStatusId = null;

    }
}