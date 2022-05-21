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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rupayan_housing.R;
import com.rupayan_housing.clickHandle.EditMonitoringFragmentClickHandle;
import com.rupayan_housing.databinding.FragmentEditMonitoringBinding;
import com.rupayan_housing.serverResponseModel.AddMonitoringPageResponse;
import com.rupayan_housing.serverResponseModel.DuePaymentResponse;
import com.rupayan_housing.serverResponseModel.MillerByZone;
import com.rupayan_housing.serverResponseModel.MillerListResponse;
import com.rupayan_housing.serverResponseModel.MonitoringDetails;
import com.rupayan_housing.serverResponseModel.MonitoringTypeList;
import com.rupayan_housing.serverResponseModel.UpdateMonitoringPageResponse;
import com.rupayan_housing.serverResponseModel.ZoneListResponse;
import com.rupayan_housing.utils.ImageBaseUrl;
import com.rupayan_housing.utils.PathUtil;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.viewModel.MonitoringViewModel;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import lombok.SneakyThrows;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class EditMonitoringFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener {
    private FragmentEditMonitoringBinding binding;
    private MonitoringViewModel monitoringViewModel;
    private boolean isMonitorDateClick = false;
    private static final int PICK_IMAGE = 200;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 300;
    private Uri imageUri;


    /**
     * For zone
     */
    private List<ZoneListResponse> zoneListResponseList;
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

    private String selectedZone, selectedMonitoringType, selectedMiller;


    String previousSelectedId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_monitoring, container, false);
        monitoringViewModel = new ViewModelProvider(this).get(MonitoringViewModel.class);
        binding.toolbar.toolbarTitle.setText("Update Monitoring");
        getDataFromPreviousFragment();
        /**
         * get Previous data from Server
         */
        getPreviousDataFromServer();
        binding.toolbar.setClickHandle(() -> {
            hideKeyboard(getActivity());
            getActivity().onBackPressed();
        });
        binding.setClickHandle(new EditMonitoringFragmentClickHandle() {
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
                if (!(isInternetOn(getActivity()))) {
                    infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
                    return;
                }
                if (binding.monitoringSummary.getText().toString().isEmpty()) {
                    binding.monitoringSummary.setError("Empty");
                    binding.monitoringSummary.requestFocus();
                    return;
                }
                /**
                 * Everything Ok Now save Monitoring data to server
                 */
                hideKeyboard(getActivity());
                editMonitoringDialog();
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
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        EditMonitoringFragment.this,
                        now.get(Calendar.YEAR), // Initial year selection
                        now.get(Calendar.MONTH), // Initial month selection
                        now.get(Calendar.DAY_OF_MONTH) // Initial day selection
                );
                assert getFragmentManager() != null;
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }

            @Override
            public void publishDate() {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        EditMonitoringFragment.this,
                        now.get(Calendar.YEAR), // Initial year selection
                        now.get(Calendar.MONTH), // Initial month selection
                        now.get(Calendar.DAY_OF_MONTH) // Initial day selection
                );
                assert getFragmentManager() != null;
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });
        /**
         * Now handle page all dropdown Item Click
         */
        binding.zone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedZone = zoneListResponseList.get(position).getZoneID();
                binding.zone.setEnableErrorLabel(false);
                if (selectedZone != null) {
                    MonitoringList(selectedZone);
                    return;
                }
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

    private void getDataFromPreviousFragment() {
        assert getArguments() != null;
        previousSelectedId = getArguments().getString("id");
    }

    private void getPreviousDataFromServer() {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        monitoringViewModel.getUpdateMonitoringPageData(getActivity(), previousSelectedId)
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
                     * all ok now set selected data to view
                     */
                    setSelectedAllDataToView(response);
                    getPageDataFromServer(response.getMonitoringDetails().getMonitoringType());

                });

    }

    private void getPageDataFromServer(String type) {
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
                    setServerDataToView(response, type);

                });
    }

    private void setServerDataToView(AddMonitoringPageResponse response, String type) {

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

        for (int i = 0; i < monitoringTypeList.size(); i++) {
            if (type.equals(monitoringTypeList.get(i).getTypeID())) {
                selectedMonitoringType = monitoringTypeList.get(i).getTypeID();
                binding.monitoringType.setSelection(i);
                break;
            }
        }

    }


    private void setSelectedAllDataToView(UpdateMonitoringPageResponse response) {
        /**
         * For Zone
         */
        zoneListResponseList = new ArrayList<>();
        zoneListResponseList.clear();
        zoneNameList = new ArrayList<>();
        zoneNameList.clear();
        zoneListResponseList.addAll(response.getZoneList());

        for (int i = 0; i < response.getZoneList().size(); i++) {
            zoneNameList.add(response.getZoneList().get(i).getZoneName());
        }
        binding.zone.setItem(zoneNameList);


        /**
         * now set miller list
         */
//        millerListResponseList = new ArrayList<>();
//        millerListResponseList.clear();
//        millerListResponseList.addAll(response.getMiller());
//        millerNameList = new ArrayList<>();
//        millerNameList.clear();
//
//        for (int i = 0; i < response.getMiller().size(); i++) {
//            millerNameList.add(response.getMiller().get(i).getDisplayName());
//        }
//        binding.miller.setItem(millerNameList);
//

        /**
         * all data set now set previous selected data
         */
        MonitoringDetails monitoringDetails = response.getMonitoringDetails();


        binding.monitoringDate.setText(monitoringDetails.getMonitoringDate());
        binding.publishDate.setText(monitoringDetails.getPublishDate());
        /**
         * set zone
         */
        for (int i = 0; i < zoneListResponseList.size(); i++) {
            if (zoneListResponseList.get(i).getZoneID().equals(monitoringDetails.getZoneID())) {
                selectedZone = monitoringDetails.getZoneID();
                binding.zone.setSelection(i);
                break;
            }
        }
        selectedZone = monitoringDetails.getZoneID();
        selectedMiller = response.getMonitoringDetails().getMillID();
        if (selectedZone != null) {
            MonitoringList(selectedZone);
        }


        /**
         * set Monitoring Type
         */

        /**
         * set type Name
         */
        binding.othersName.setText(String.valueOf(monitoringDetails.getOtherMonitoringTypeName()));
        /**
         * set miller
         */
       /* for (int i = 0; i < millerListResponseList.size(); i++) {
            if (millerListResponseList.get(i).getSl().equals(response.getMonitoringDetails().getMillID())) {
                selectedMiller = millerListResponseList.get(i).getMillID();
                binding.miller.setSelection(i);
                break;
            }
        }
*/
        /**
         * Now set image  to   image view
         */
        Glide.with(this)
                .load(ImageBaseUrl.image_base_url + monitoringDetails.getDocument())
                .error(R.drawable.file)
                .placeholder(R.drawable.file)
                .into(binding.documentImage);
        /**
         * now set monitoring summary
         */
        binding.monitoringSummary.setText(String.valueOf(monitoringDetails.getMonitoringSummery()).replaceAll("\\<.*?\\>", ""));
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


                for (int i = 0; i < millerListResponseList.size(); i++) {
                    if (millerListResponseList.get(i).getStoreID().equals(selectedMiller)) {
                        selectedMiller = millerListResponseList.get(i).getStoreID();
                        binding.miller.setSelection(i);
                        break;
                    }
                }


            } catch (Exception e) {
            }
        });
    }


    private void validationAndUpdate() {
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
        monitoringViewModel.updateMonitoring(
                getActivity(), previousSelectedId, binding.monitoringDate.getText().toString(),
                binding.publishDate.getText().toString(), selectedZone, selectedMonitoringType, selectedMiller,
                binding.monitoringSummary.getText().toString(), logoBody, binding.othersName.getText().toString())
                .observe(getViewLifecycleOwner(), response -> {
                    progressDialog.dismiss();
                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "Something Wrong");
                        return;
                    }
                    if (response.getStatus() != 200) {
                        errorMessage(getActivity().getApplication(), "Something Wrong");
                        return;
                    }
                    successMessage(getActivity().getApplication(), "" + response.getMessage());
                    getActivity().onBackPressed();
                });

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
  /*      int month = monthOfYear;
        if (month == 12) {
            month = 1;
        } else {
            month = monthOfYear + 1;
        }
        String selectedDate = year + "-" + month + "-" + dayOfMonth;//set the selected date

     */


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

      /*  if (isMonitorDateClick) {
            isMonitorDateClick = false;
            binding.monitoringDate.setText(selectedDate);
            return;
        }
        binding.publishDate.setText(selectedDate);*/
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
            binding.documentImage.setImageDrawable(null);
            binding.documentImage.destroyDrawingCache();
            binding.documentImage.setImageBitmap(bitmap);


            /**
             * now set license Image Name
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


    private void editMonitoringDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        @SuppressLint("InflateParams")
        View view = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.purchase_dialog, null);
        //Set the view
        builder.setView(view);
        TextView tvTitle, tvMessage;
        ImageView imageIcon = view.findViewById(R.id.img_icon);
        tvMessage = view.findViewById(R.id.tv_message);
        tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("DDo you want to update  ?");//set warning title
        tvMessage.setText("MIS ERP");
        imageIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.app_logo));//set warning image
        Button bOk = view.findViewById(R.id.btn_ok);
        Button cancel = view.findViewById(R.id.cancel);
        AlertDialog alertDialog = builder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        cancel.setOnClickListener(v -> alertDialog.dismiss());//for cancel
        bOk.setOnClickListener(v -> {
            alertDialog.dismiss();
            validationAndUpdate();
        });
        alertDialog.show();
    }


}