package com.rupayan_housing.view.fragment.miller.addNewMiller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.rupayan_housing.R;
import com.rupayan_housing.clickHandle.MillerLicenseInfoClickHandle;
import com.rupayan_housing.clickHandle.ToolbarClickHandle;
import com.rupayan_housing.databinding.FragmentMillerLicenseInfoBinding;
import com.rupayan_housing.serverResponseModel.CertificateResponse;
import com.rupayan_housing.serverResponseModel.DuePaymentResponse;
import com.rupayan_housing.utils.LicenseInfoDialog;
import com.rupayan_housing.utils.PathUtil;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.view.fragment.miller.editmiller.MillerOwnerListEdit;
import com.rupayan_housing.view.fragment.miller.editmiller.MillerProfileInformationEdit;
import com.rupayan_housing.viewModel.MillerProfileInfoViewModel;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import lombok.SneakyThrows;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class MillerLicenseInfo extends BaseFragment implements DatePickerDialog.OnDateSetListener {
    private FragmentMillerLicenseInfoBinding binding;
    private MillerProfileInfoViewModel millerProfileInfoViewModel;
    private ViewPager viewPager;
    private boolean isIssuingDate = false;
    private boolean isCirtificateDate = false;

    private static final int PICK_IMAGE = 200;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 300;
    private Uri imageUri;
    /**
     * For Certificate
     */
    private List<CertificateResponse> certificateResponsesList;
    private List<String> certificateNameList;

    private String selectedCertificateType;
    List<Set<String>> typeList;
    String tin = "7";
    String tradeLicence = "6";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_miller_license_info, container, false);
        viewPager = getActivity().findViewById(R.id.viewPager);
        millerProfileInfoViewModel = new ViewModelProvider(this).get(MillerProfileInfoViewModel.class);


        if (MillerProfileInformation.selectedMillerTypeList != null) {

        }
        typeList = new ArrayList<>();

        for (int i = 0; i < MillerProfileInformation.selectedMillerTypeList.size(); i++) {
            typeList.add(MillerProfileInformation.selectedMillerTypeList);
        }

        /**
         * set Current date time
         */
        setCurrentDateTime();
        /**
         * set Page Data from server
         */
        getPageDataFromServer();
        binding.setClickHandle(new MillerLicenseInfoClickHandle() {
            @Override
            public void addNew() {

                manageCheck();
                  /* LicenseInfoDialog dialog = new LicenseInfoDialog();
                dialog.show(getActivity().getSupportFragmentManager(), "dialog");*/

            }

            @Override
            public void save() {
                ValidationAndSave();
            }

            @Override
            public void certificateImage() {
                if (!(checkStoragePermission())) {
                    requestStoragePermission(STORAGE_PERMISSION_REQUEST_CODE);
                } else {
                    getLogoImageFromFile(getActivity().getApplication(), PICK_IMAGE);
                }
            }

            @Override
            public void issuingDate() {
                isIssuingDate = true;
                Calendar now = Calendar.getInstance();
                DatePickerDialog dialog = DatePickerDialog.newInstance(
                        MillerLicenseInfo.this,
                        now.get(Calendar.YEAR), // Initial year selection
                        now.get(Calendar.MONTH), // Initial month selection
                        now.get(Calendar.DAY_OF_MONTH) // Inital day selection
                );
                dialog.show(getActivity().getSupportFragmentManager(), "Datepickerdialog");
            }

            @Override
            public void certificateDate() {
                isCirtificateDate = true;
                Calendar now = Calendar.getInstance();
                DatePickerDialog dialog = DatePickerDialog.newInstance(
                        MillerLicenseInfo.this,
                        now.get(Calendar.YEAR), // Initial year selection
                        now.get(Calendar.MONTH), // Initial month selection
                        now.get(Calendar.DAY_OF_MONTH) // Inital day selection
                );
                dialog.show(getActivity().getSupportFragmentManager(), "Datepickerdialog");
            }

            @Override
            public void renewalDate() {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dialog = DatePickerDialog.newInstance(
                        MillerLicenseInfo.this,
                        now.get(Calendar.YEAR), // Initial year selection
                        now.get(Calendar.MONTH), // Initial month selection
                        now.get(Calendar.DAY_OF_MONTH) // Inital day selection
                );
                dialog.show(getActivity().getSupportFragmentManager(), "Datepickerdialog");
            }
        });


        binding.certificateType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCertificateType = certificateResponsesList.get(position).getCertificateTypeID();
                binding.issuerNameEt.setText(certificateResponsesList.get(position).getCertificateProviderName().replaceAll("^\"|\"$", ""));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return binding.getRoot();
    }

    private void getPreViousdata() {

    }

    private void manageCheck() {
        try {
            //manage trade licence and tin licence (ei certificate gulo mendatory dite hobe)
            if (!tin.isEmpty()) {
                infoMessage(getActivity().getApplication(), "Please save the TIN Licence");
                return;

            }
            if (!tradeLicence.isEmpty()) {
                infoMessage(getActivity().getApplication(), "Please save the Trade Licence");
                return;

            }
//ei duita certificate jodi profile info te select kore tahole mendatory dite hobe,ekta korle eta dite hobe
            if (MillerProfileInformation.id1.equals("2") && MillerProfileInformation.id2.equals("3")) {
                infoMessage(getActivity().getApplication(), "Please save the both licence information");
                return;
            }
            if (MillerProfileInformation.id1.equals("2")) {
                infoMessage(getActivity().getApplication(), "Please save the edible licence information");
                return;
            }
            if (MillerProfileInformation.id2.equals("3")) {
                infoMessage(getActivity().getApplication(), "Please save the industrial licence information");
                return;
            }
            if (MillerProfileInformation.id1.isEmpty() && MillerProfileInformation.id2.isEmpty()) {
                viewPager.setCurrentItem(3);
                return;
            }
            if (MillerProfileInformation.id1.isEmpty() || MillerProfileInformation.id2.isEmpty()) {
                viewPager.setCurrentItem(3);
                return;
            }


        } catch (Exception e) {

        }
    }

    private void setCurrentDateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        //System.out.println(formatter.format(date));
        String currentDate = formatter.format(date);

        /*System.out.println(dtf.format(now));*/
        //  binding.issuingDate.setText(currentDate);
        //  binding.certificateDate.setText(currentDate);
    }

    private void ValidationAndSave() {
        if (selectedCertificateType == null) {
            infoMessage(getActivity().getApplication(), "Please select certificate type");
            return;


        }
        if (binding.issuerNameEt.getText().toString().isEmpty()) {
            binding.issuerNameEt.setError("Empty Field");
            binding.issuerNameEt.requestFocus();
            binding.issuerNameEt.requestFocus();
            return;
        }

        if (binding.issuingDate.getText().toString().isEmpty()) {
            binding.issuingDate.setError("Empty Field");
            binding.issuingDate.requestFocus();
            return;
        }
        if (binding.certificateDate.getText().toString().isEmpty()) {
            binding.certificateDate.setError("Empty Field");
            binding.certificateDate.requestFocus();
            return;
        }
        if (binding.renewalDate.getText().toString().isEmpty()) {
            binding.renewalDate.setError("Empty Field");
            binding.renewalDate.requestFocus();
            return;
        }if (binding.remarksEt.getText().toString().isEmpty()) {
            binding.remarksEt.setError("Empty Field");
            binding.remarksEt.requestFocus();
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
            logoBody = MultipartBody.Part.createFormData("certificateImage", file.getName(), requestFile);//here logoImage is name of from data
        } else {
            logoBody = null;
        }
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        millerProfileInfoViewModel.submitLicenseInfoDetails(
                getActivity(), MillerProfileInformation.profileIdFromServer.replaceAll("^\"|\"$", ""), "1",
                selectedCertificateType.replaceAll("^\"|\"$", ""), binding.issuerNameEt.getText().toString(), binding.issuingDate.getText().toString(),
                binding.certificateDate.getText().toString(), logoBody, binding.renewalDate.getText().toString(),
                binding.remarksEt.getText().toString()).observe(getViewLifecycleOwner(), new Observer<DuePaymentResponse>() {
            @Override
            public void onChanged(DuePaymentResponse response) {
                progressDialog.dismiss();
                if (response.getStatus() == null) {
                    errorMessage(getActivity().getApplication(), "Something wrong");
                    return;
                }

                if (response.getStatus() == 400) {
                    errorMessage(getActivity().getApplication(), response.getMessage());
                    return;
                }
                if (response.getStatus() == 500) {
                    errorMessage(getActivity().getApplication(), response.getMessage());
                    return;
                }

                if (response.getStatus() == 200) {

                    //   binding.saveBtn.setVisibility(View.GONE);
                    if (selectedCertificateType.equals("4")) {
                        MillerProfileInformation.id1 = "";
                    }
                    if (selectedCertificateType.equals("5")) {
                        MillerProfileInformation.id2 = "";
                    }
                    //manage trade licence and tin licence (ei certificate gulo mendatory dite hobe)
                    if (selectedCertificateType.equals(tin)) {
                        tin = "";
                    }
                    if (selectedCertificateType.equals(tradeLicence)) {
                        tradeLicence = "";
                    }
                    successMessage(getActivity().getApplication(), "" + response.getMessage());

                    clearAllData();

                    return;
                }


            }
        });
    }

    private void clearAllData() {
        try {
            binding.certificateType.clearSelection();
            binding.issuerNameEt.setText("");
            binding.issuingDate.setText("");
            binding.certificateDate.setText("");
            binding.remarksEt.setText("");
            binding.renewalDate.setText("");
            binding.certificateImageview.setImageBitmap(null);
            binding.certificateImageview.destroyDrawingCache();
            binding.certificateImageName.setText("");
        } catch (Exception e) {
        }
    }

    private void getPageDataFromServer() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        millerProfileInfoViewModel.getProfileInfoResponse(getActivity())
                .observe(getViewLifecycleOwner(), response -> {
                    progressDialog.dismiss();
                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "Something Wrong");
                        return;
                    } if (response.getStatus() == 400) {
                        infoMessage(getActivity().getApplication(), "" + response.getMessage());
                        return;
                    }
                    if (response.getStatus() == 200) {
                        certificateResponsesList = new ArrayList<>();
                        certificateNameList = new ArrayList<>();
                        certificateResponsesList.clear();
                        certificateNameList.clear();

                        certificateResponsesList.addAll(response.getCertificate());
                        for (int i = 0; i < response.getCertificate().size(); i++) {
                            certificateNameList.add(response.getCertificate().get(i).getCertificateTypeName());
                        }
                        binding.certificateType.setItem(certificateNameList);
                    }
                });
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


        if (isIssuingDate) {
            binding.issuingDate.setText(selectedDate);
            binding.issuingDate.setError(null);
            isIssuingDate = false;
            return;
        }
        if (isCirtificateDate) {
            binding.certificateDate.setText(selectedDate);
            binding.certificateDate.setError(null);
            isCirtificateDate = false;
            return;
        }

        binding.renewalDate.setText(selectedDate);
        binding.renewalDate.setError(null);
        return;
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
            binding.certificateImageview.setImageDrawable(null);
            binding.certificateImageview.setImageBitmap(bitmap);


            /**
             * now set licenseImageName
             * */
            binding.certificateImageName.setText(String.valueOf(new File("" + data.getData()).getName()));

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

}