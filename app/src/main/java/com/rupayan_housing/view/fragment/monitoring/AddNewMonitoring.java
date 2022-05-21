package com.rupayan_housing.view.fragment.monitoring;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.rupayan_housing.R;
import com.rupayan_housing.clickHandle.AddNewMonitoringClickHandle;
import com.rupayan_housing.databinding.FragmentAddNewMonitoringBinding;
import com.rupayan_housing.serverResponseModel.AddMonitoringPageResponse;
import com.rupayan_housing.serverResponseModel.MillerByZone;
import com.rupayan_housing.serverResponseModel.MillerListByZoneIdResponse;
import com.rupayan_housing.serverResponseModel.MillerListResponse;
import com.rupayan_housing.serverResponseModel.MonitoringTypeList;
import com.rupayan_housing.serverResponseModel.ZoneListResponse;
import com.rupayan_housing.utils.PathUtil;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.view.fragment.all_report.reconcilation_report.page_data_response.ReconciliationPageDataResponse;
import com.rupayan_housing.view.fragment.all_report.reconcilation_report.page_data_response.ReconciliationReportAssociationList;
import com.rupayan_housing.viewModel.MonitoringViewModel;
import com.rupayan_housing.viewModel.report_all_view_model.ReconciliationReportViewModel;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import lombok.SneakyThrows;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AddNewMonitoring extends BaseFragment implements DatePickerDialog.OnDateSetListener {
    private MonitoringViewModel monitoringViewModel;
    private FragmentAddNewMonitoringBinding binding;
    private static final int PICK_IMAGE = 200;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 300;
    private Uri imageUri;
    private boolean isMonitorDateClick = false;
    /**
     * For zone
     */
    private List<ReconciliationReportAssociationList> zoneListResponseList;
    private List<String> zoneNameList;
    /**
     * For monitoring Type
     */
    private List<MonitoringTypeList> monitoringTypeList;
    private List<String> monitoringTypeNameList;
    /**
     * For Miller list
     */
    private List<MillerByZone> millerListResponseList;
    private List<String> millerNameList;
    private ReconciliationReportViewModel reconciliationViewModel;

    private String selectedZone, selectedMonitoringType, selectedMiller;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_new_monitoring, container, false);
        monitoringViewModel = new ViewModelProvider(this).get(MonitoringViewModel.class);
        reconciliationViewModel =new ViewModelProvider(this).get(ReconciliationReportViewModel.class);
        binding.toolbar.toolbarTitle.setText("Add new Monitoring");
        setCurrentDateTime();
        /**
         * Now Get Page Data From Server
         */
        getPageDataFromServer();

        binding.toolbar.setClickHandle(() -> {
            hideKeyboard(getActivity());
            getActivity().onBackPressed();
        });
        binding.setClickHandle(new AddNewMonitoringClickHandle() {
            @Override
            public void save() {
                if (selectedZone == null) {
                    infoMessage(getActivity().getApplication(), "Please select zone");
                    return;
                }
                if (selectedMonitoringType == null) {
                    infoMessage(getActivity().getApplication(), "Please select monitoring type");
                    return;
                }
                if (selectedMiller == null) {
                    infoMessage(getActivity().getApplication(), "Please select miller");
                    return;
                }

                if (binding.monitoringSummary.getText().toString().isEmpty()) {
                    binding.monitoringSummary.setError("Type a short summery");
                    binding.monitoringSummary.requestFocus();
                    return;
                }
                /**
                 * Everything Ok Now save Monitoring data to server
                 */
                hideKeyboard(getActivity());
                addNewMonitoringDialog();
            }

            @Override
            public void documentImage() {
                if (!(checkStoragePermission())) {
                    requestStoragePermission(STORAGE_PERMISSION_REQUEST_CODE);
                } else {
                    getLogoImageFromFile(getActivity().getApplication(), PICK_IMAGE);
                }
            }

            @Override
            public void monitoringDate() {
                isMonitorDateClick = true;
                showDatePickerDialog();
            }

            @Override
            public void publishDate() {
                showDatePickerDialog();
            }
        });
        /**
         * Now handle page all dropdown Item Click
         */
        binding.zone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedZone = zoneListResponseList.get(position).getZoneID();
               if (selectedZone !=null ){
                   MonitoringList(selectedZone);
               }
                binding.zone.setEnableErrorLabel(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.monitoringType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMonitoringType = monitoringTypeList.get(position).getTypeID();
                binding.monitoringType.setEnableErrorLabel(false);
                if (monitoringTypeList.get(position).equals("4")) {
                    binding.otherNameView.setVisibility(View.VISIBLE);
                    return;
                }
                binding.otherNameView.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        binding.miller.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMiller = millerListResponseList.get(position).getStoreID();
                binding.miller.setEnableErrorLabel(false);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return binding.getRoot();
    }

    private void MonitoringList(String selectedZone) {
        monitoringViewModel.getMillerByZone(getActivity(), selectedZone).observe(getViewLifecycleOwner(), response -> {

            try {
                millerListResponseList = new ArrayList<>();
                millerListResponseList.clear();
                millerListResponseList.addAll(response.getMillerList());
                millerNameList = new ArrayList<>();
                millerNameList.clear();
                for (int i = 0; i < millerListResponseList.size(); i++) {
                    millerNameList.add("" + millerListResponseList.get(i).getFullName());
                }
                binding.miller.setItem(millerNameList);

            }catch (Exception e){}
        });
    }

    private void validationAndSave() {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }
        /**
         * for Image
         */

        MultipartBody.Part logoBody;
        if (imageUri != null) {//logo image not mandatory here so if user not select any logo image by default it send null
            File file = null;
            try {
                file = new File(PathUtil.getPath(getActivity(), imageUri));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

            // MultipartBody.Part is used to send also the actual file name
            logoBody =
                    MultipartBody.Part.createFormData("document", file.getName(), requestFile);//here document is name of from data
        } else {
            logoBody = null;
        }

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        monitoringViewModel.addNewMonitoring(
                getActivity(), binding.monitoringDate.getText().toString(),
                binding.publishDate.getText().toString(), selectedZone, selectedMiller, selectedMonitoringType, logoBody
                , binding.monitoringSummary.getText().toString(), binding.othersName.getText().toString()).observe(getViewLifecycleOwner(), response -> {
            progressDialog.dismiss();
            if (response == null) {
                errorMessage(getActivity().getApplication(), "Something Wrong");
                return;
            }
            if (response.getStatus() != 200) {
                infoMessage(getActivity().getApplication(), "" + response.getMessage());
                return;
            }
            successMessage(getActivity().getApplication(), "" + response.getMessage());
            getActivity().onBackPressed();
        });


    }

    private void getPageDataFromServer() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        monitoringViewModel.getAppMonitoringPageData(getActivity())
                .observe(getViewLifecycleOwner(), response -> {
                    progressDialog.dismiss();
                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "Something Wrong");
                        return;
                    }
                    if (response.getStatus() != 200) {
                        infoMessage(getActivity().getApplication(), "" + response.getMessage());
                        return;
                    }
                    /**
                     * Everything Ok now set server data To view
                     */
                    setServerDataToView(response);

                });

        getPAgeDataFromViewModel();

    }

    private void getPAgeDataFromViewModel() {
        reconciliationViewModel.getReconciliationPageData(getActivity(), getProfileId(getActivity().getApplication())).observe(getViewLifecycleOwner(), new Observer<ReconciliationPageDataResponse>() {
            @Override
            public void onChanged(ReconciliationPageDataResponse response) {

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

                        }
                        binding.zone.setItem(zoneNameList);


                    }

                } catch (Exception e) {
                    Log.d("Error", e.getMessage());
                }
            }
        });
    }


    private void setServerDataToView(AddMonitoringPageResponse response) {
        /**
         * now set Monitoring type dynamically
         */
        monitoringTypeList = new ArrayList<>();
        monitoringTypeList.addAll(response.getMonitoringType());

//        monitoringTypeList.add("1");
//        monitoringTypeList.add("2");
//        monitoringTypeList.add("3");
//        monitoringTypeList.add("4");

        monitoringTypeNameList = new ArrayList<>();
        monitoringTypeNameList.clear();
        //   monitoringTypeNameList.addAll(Arrays.asList("QC/QA", "License", "Stock Availability", "Other"));

        for (int i = 0; i < response.getMonitoringType().size(); i++) {
            monitoringTypeNameList.add("" + response.getMonitoringType().get(i).getMonitoringTypeName());
        }
        binding.monitoringType.setItem(monitoringTypeNameList);

        /**
         * now set miller list
         */
//        millerListResponseList = new ArrayList<>();
//        millerListResponseList.clear();
//        millerListResponseList.addAll(response.getMillerList());
//        millerNameList = new ArrayList<>();
//        millerNameList.clear();
//
//        for (int i = 0; i < response.getMillerList().size(); i++) {
//            millerNameList.add(response.getMillerList().get(i).getDisplayName());
//        }
//        binding.miller.setItem(millerNameList);

    }

    @SneakyThrows
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }

            InputStream inputStream = getContext().getContentResolver().openInputStream(data.getData());
            imageUri = data.getData();

            //convertUriToBitmapImageAndSetInImageView(getPath(data.getData()), data.getData());
            /**
             * for set selected image in image view
             */
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
            binding.documentImage.setImageDrawable(null);
            binding.documentImage.setImageBitmap(bitmap);


            /**
             * now set licenseImageName
             * */
            binding.documentImageName.setText(String.valueOf(new File("" + data.getData()).getName()));

            //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
            Log.d("LOGO_IMAGE", String.valueOf(inputStream));


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case STORAGE_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    infoMessage(requireActivity().getApplication(), "Permission Granted");
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    infoMessage(requireActivity().getApplication(), "Permission Decline");
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
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


    private void setCurrentDateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        //System.out.println(formatter.format(date));
        String currentDate = formatter.format(date);
        /*System.out.println(dtf.format(now));*/
        binding.monitoringDate.setText(currentDate);
        binding.publishDate.setText(currentDate);
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
        if (isMonitorDateClick) {
            isMonitorDateClick = false;
            binding.monitoringDate.setText(selectedDate);
            return;
        }
        binding.publishDate.setText(selectedDate);

    }


    private void addNewMonitoringDialog() {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        @SuppressLint("InflateParams")
        View view = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.purchase_dialog, null);
        //Set the view
        builder.setView(view);
        TextView tvTitle, tvMessage;
        ImageView imageIcon = view.findViewById(R.id.img_icon);
        tvMessage = view.findViewById(R.id.tv_message);
        tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("Do you want to add ?");//set warning title
        tvMessage.setText("MIS ERP");
        imageIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.app_logo));//set warning image
        Button bOk = view.findViewById(R.id.btn_ok);
        Button cancel = view.findViewById(R.id.cancel);
        android.app.AlertDialog alertDialog = builder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        cancel.setOnClickListener(v -> alertDialog.dismiss());//for cancel
        bOk.setOnClickListener(v -> {
            alertDialog.dismiss();
            validationAndSave();
        });

        alertDialog.show();
        //----------//


    }
}