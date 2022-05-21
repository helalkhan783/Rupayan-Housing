package com.rupayan_housing.view.fragment.user.addnew;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.rupayan_housing.R;
import com.rupayan_housing.clickHandle.AddNewUserClickHandle;
import com.rupayan_housing.databinding.FragmentAddNewUserBinding;
import com.rupayan_housing.databinding.NumberVerificationDialogBinding;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.BloodGroup;
import com.rupayan_housing.serverResponseModel.DepartmentList;
import com.rupayan_housing.serverResponseModel.DesignationList;
import com.rupayan_housing.serverResponseModel.EnterpriseResponse;
import com.rupayan_housing.utils.PathUtil;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.viewModel.AddNewUserViewModel;
import com.rupayan_housing.viewModel.SalesRequisitionViewModel;
import com.rupayan_housing.viewModel.UserViewModel;
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
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import lombok.SneakyThrows;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AddNewUser extends BaseFragment implements DatePickerDialog.OnDateSetListener {
    private FragmentAddNewUserBinding binding;
    private boolean isPasswordVisible = false; // variable to detect whether password is visible or not
    private UserViewModel userViewModel;
    private SalesRequisitionViewModel salesRequisitionViewModel;
    private boolean isJoiningDate = false;
    private static final int PICK_IMAGE = 200;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 300;
    private Uri imageUri;
    /**
     * For Department
     */
    private List<DepartmentList> departmentResponseLists;
    private List<String> departmentNameLists;
    private Boolean isVerifiedPhoneNumber = false;

    /**
     * For DesignationList
     */
    private List<DesignationList> designationResponseLists;
    private List<String> designationNameList;

    /**
     * For Blood Group
     */
    private List<BloodGroup> bloodGroupResponseList;
    private List<String> bloodNameList;

    /**
     * For Title
     */
    List<String> titleList;
    /**
     * For Gender
     */
    List<String> genderList;
    /**
     * For Enterprise
     */
    private List<EnterpriseResponse> enterpriseResponseList;
    private List<String> enterpriseNameList;

    /**
     * For selected storeAccess List
     */
    Set<Integer> selectedStoreAccessList;

    private String selectedTitle, selectedGender, selectedDepartment, selectedDesignation, selectedBloodGroup, selectedEnterprise;

    private AddNewUserViewModel addNewUserViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_new_user, container, false);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        salesRequisitionViewModel = new ViewModelProvider(this).get(SalesRequisitionViewModel.class);
        binding.toolbar.toolbarTitle.setText("Add New User");
        setCurrentDateToView();
        addNewUserViewModel = new ViewModelProvider(this).get(AddNewUserViewModel.class);

        /**
         * now get Page Data From Server
         */
        try {
            getPageDataFromServer();
        } catch (Exception e) {
        }
        binding.toolbar.setClickHandle(() -> {
            hideKeyboard(getActivity());
            getActivity().onBackPressed();
        });
        binding.setClickhandle(new AddNewUserClickHandle() {
            @Override
            public void joiningDate() {
                isJoiningDate = true;
                showDatePickerDialog();
            }

            @Override
            public void dateOfBirth() {
                showDatePickerDialog();
            }

            @Override
            public void getProfileImage() {
                if (!(checkStoragePermission())) {
                    requestStoragePermission(STORAGE_PERMISSION_REQUEST_CODE);
                } else {
                    getLogoImageFromFile(getActivity().getApplication(), PICK_IMAGE);
                }
            }

            @Override
            public void submit() {
                try {
                    if (selectedTitle == null) {
                        infoMessage(getActivity().getApplication(), "Please select title");
                        return;
                    }
                    if (binding.fullName.getText().toString().isEmpty()) {
                        binding.fullName.setError("Empty");
                        binding.fullName.requestFocus();
                        return;
                    }
                    if (binding.displayName.getText().toString().isEmpty()) {
                        binding.displayName.setError("Empty");
                        binding.displayName.requestFocus();
                        return;
                    }
                    if (binding.primaryMobile.getText().toString().isEmpty()) {
                        binding.primaryMobile.setError("Empty");
                        binding.primaryMobile.requestFocus();
                        return;
                    }
                    if (!isValidPhone(binding.primaryMobile.getText().toString())) {
                        binding.primaryMobile.setError("Invalid Phone Number");
                        binding.primaryMobile.requestFocus();
                        return;
                    }
                    if (!binding.altMobile.getText().toString().isEmpty()) {
                        if (!isValidPhone(binding.altMobile.getText().toString())) {
                            binding.altMobile.setError("Invalid Phone Number");
                            binding.altMobile.requestFocus();
                            return;
                        }
                    }

                    if (!binding.email.getText().toString().isEmpty()) {
                        if (!isValidEmail(binding.email.getText().toString())) {
                            binding.email.setError("Invalid Email");
                            binding.email.requestFocus();
                            return;
                        }
                    }

                    if (binding.password.getText().toString().isEmpty()) {
                        binding.password.setError("Empty");
                        binding.password.requestFocus();
                        return;
                    }
                    if (binding.password.getText().toString().length() < 6) {
                        binding.password.setError("Password length should be 6 or more");
                        binding.password.requestFocus();
                        return;
                    }

                    if (!(binding.password.getText().toString().equals(binding.password1.getText().toString()))) {
                        binding.password1.requestFocus();
                        binding.password1.requestFocus();
                        infoMessage(getActivity().getApplication(), "Invalid Password");
                        return;
                    }

                    if (selectedEnterprise == null) {
                        infoMessage(requireActivity().getApplication(), "Please select enterprise");
                        return;
                    }

                    hideKeyboard(getActivity());
                    if (!(isInternetOn(getActivity()))) {
                        infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
                        return;
                    }
                } catch (Exception e) {
                }
                addNewUserDialog();
            }
        });

        /**
         * For Handle Password visibility
         */
        binding.passwordVisibilityImgBtn.setOnClickListener(v -> {
            if (isPasswordVisible) {
                binding.passwordVisibilityImgBtn.setImageResource(R.drawable.ic_visibility_off_grey_24dp);
                // hide password
                binding.password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                isPasswordVisible = false;
            } else {
                binding.passwordVisibilityImgBtn.setImageResource(R.drawable.ic_visibility_grey_24dp);
                // show password
                binding.password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                isPasswordVisible = true;
            }
        });
        binding.passwordVisibilityImgBtn1.setOnClickListener(v -> {
            if (isPasswordVisible) {
                binding.passwordVisibilityImgBtn1.setImageResource(R.drawable.ic_visibility_off_grey_24dp);
                // hide password
                binding.password1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                isPasswordVisible = false;
            } else {
                binding.passwordVisibilityImgBtn1.setImageResource(R.drawable.ic_visibility_grey_24dp);
                // show password
                binding.password1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                isPasswordVisible = true;
            }
        });

        /**
         * now handle on item select  on dropdown
         */
        binding.department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDepartment = departmentResponseLists.get(position).getDepartmentID();
                binding.department.setEnableErrorLabel(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.designation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDesignation = designationResponseLists.get(position).getUserDesignationId();
                binding.designation.setEnableErrorLabel(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.bloodGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedBloodGroup = bloodGroupResponseList.get(position).getBloodGroupId();
                binding.bloodGroup.setEnableErrorLabel(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.title.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTitle = titleList.get(position);
                binding.title.setEnableErrorLabel(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedGender = genderList.get(position);
                binding.gender.setEnableErrorLabel(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.enterprise.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedEnterprise = enterpriseResponseList.get(position).getStoreID();
                binding.enterprise.setEnableErrorLabel(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.storeAccess.addOnItemsSelectedListener((list, booleans) -> {
            selectedStoreAccessList = new HashSet<>();
            selectedStoreAccessList.clear();
            for (int i = 0; i < enterpriseResponseList.size(); i++) {
                if (list.contains(enterpriseResponseList.get(i).getStoreName())) {
                    selectedStoreAccessList.add(Integer.parseInt(enterpriseResponseList.get(i).getStoreID()));
                }

            }
        });

        /**
         * For Phone Number Verification
         */
        binding.verificationBtn.setOnClickListener(v -> {

            if (binding.primaryMobile.getText().toString().isEmpty()) {
                binding.primaryMobile.setError("Empty");
                binding.primaryMobile.requestFocus();
                return;
            }
            if (!binding.primaryMobile.getText().toString().isEmpty()) {
                if (!isValidPhone(binding.primaryMobile.getText().toString())) {
                    binding.primaryMobile.setError("Invalid Mobile Number");
                    binding.primaryMobile.requestFocus();
                    return;
                }
            }
            if (!(isInternetOn(getActivity()))) {
                infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
                return;
            }

            addNewUserViewModel.sendPhoneNumberForGetOtp(getActivity(), binding.primaryMobile.getText().toString().trim())
                    .observe(getViewLifecycleOwner(), successResponse -> {
                        try {
                            if (successResponse == null) {
                                errorMessage(getActivity().getApplication(), "Something Wrong");
                                return;
                            }
                            if (successResponse.getStatus() == 400) {
                                infoMessage(getActivity().getApplication(), "" + successResponse.getMessage());
                                return;
                            }
                            if (successResponse.getStatus() < 400) {
                                /**
                                 * Open a dialog here for verification this number
                                 */
                                sendPrimaryPhoneNumberForVerification();
                            }
                        } catch (Exception e) {
                            Log.d("ERROR", e.getMessage());
                        }
                    });


        });


        return binding.getRoot();
    }


    public void sendPrimaryPhoneNumberForVerification() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        NumberVerificationDialogBinding dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.number_verification_dialog, null, false);
        builder.setView(dialogBinding.getRoot());
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);


        dialogBinding.submitBtn.setOnClickListener(v -> {
            hideKeyboard(getActivity());
            if (dialogBinding.otpField.getText().toString().isEmpty()) {
                dialogBinding.otpField.setError("Empty");
                dialogBinding.otpField.requestFocus();
                return;
            }
            if (dialogBinding.otpField.getText().toString().length() < 4 || dialogBinding.otpField.getText().toString().length() > 4) {
                dialogBinding.otpField.setError("Otp length should be 4");
                dialogBinding.otpField.requestFocus();
                return;
            }
            /**
             * After all call the send phone number api for get verification Otp code
             */
            addNewUserViewModel.sendOTPCode(getActivity(), binding.primaryMobile.getText().toString().trim(), dialogBinding.otpField.getText().toString().trim())
                    .observe(getViewLifecycleOwner(), successResponse -> {
                        try {
                            if (successResponse == null) {
                                errorMessage(getActivity().getApplication(), "Something Wrong");
                                return;
                            }
                            if (successResponse.getStatus() == 400) {
                                infoMessage(getActivity().getApplication(), "" + successResponse.getMessage());
                                isVerifiedPhoneNumber = false;
                                return;
                            }
                            if (successResponse.getStatus() < 400) {
                                isVerifiedPhoneNumber = true;
                                alertDialog.dismiss();
                                successMessage(requireActivity().getApplication(), "" + successResponse.getMessage());
                                return;
                            }
                            isVerifiedPhoneNumber = false;
                            binding.primaryMobile.setError(null);
                        } catch (Exception e) {
                            Log.d("ERROR", e.getMessage());
                        }
                    });

        });

        dialogBinding.cancenBtn.setOnClickListener(v -> {
            hideKeyboard(getActivity());
            alertDialog.dismiss();
        });


        alertDialog.show();
    }


    private void showDatePickerDialog() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dialog = DatePickerDialog.newInstance(
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

        if (!isJoiningDate) {
            binding.dateOfBirth.setText(selectedDate);
            binding.dateOfBirth.setError(null);
        } else {
            binding.joiningDate.setText(selectedDate);
            binding.joiningDate.setError(null);
        }
    }

    private void setCurrentDateToView() {
        /**
         * set current date will send input from user
         */
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date(System.currentTimeMillis());
        String currentDate = formatter.format(date);
        binding.dateOfBirth.setText(currentDate);
        binding.joiningDate.setText(currentDate);
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
            binding.image.setImageDrawable(null);
            binding.image.setImageBitmap(bitmap);
            /**
             * now set profile Image
             * */
            binding.imageName.setText(String.valueOf(new File("" + data.getData()).getName()));

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
                    infoMessage(getActivity().getApplication(), "Permission Granted");
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    infoMessage(getActivity().getApplication(), "Permission Decline");
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }


    private void getPageDataFromServer() {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check your internet Connection");
            return;
        }
        titleList = new ArrayList<>();
        titleList.clear();
        genderList = new ArrayList<>();
        genderList.clear();
        titleList.addAll(Arrays.asList("Mr", "Ms", "Mrs"));
        genderList.addAll(Arrays.asList("Male", "Female"));
        binding.title.setItem(titleList);
        binding.gender.setItem(genderList);


        userViewModel.getAddNewUserPageData(getActivity())
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
                     * now set department, designation_lists and blood group
                     */
                    departmentResponseLists = new ArrayList<>();
                    departmentResponseLists.clear();
                    departmentNameLists = new ArrayList<>();
                    departmentNameLists.clear();
                    departmentResponseLists.addAll(response.getDepartmentLists());
                    for (int i = 0; i < response.getDepartmentLists().size(); i++) {
                        departmentNameLists.add("" + response.getDepartmentLists().get(i).getDepartmentName());
                    }

                    binding.department.setItem(departmentNameLists);
                    /**
                     * designation_lists
                     */
                    designationResponseLists = new ArrayList<>();
                    designationResponseLists.clear();
                    designationNameList = new ArrayList<>();
                    designationNameList.clear();
                    designationResponseLists.addAll(response.getDesignationLists());
                    for (int i = 0; i < response.getDesignationLists().size(); i++) {
                        designationNameList.add("" + response.getDesignationLists().get(i).getDesignationName());
                    }
                    binding.designation.setItem(designationNameList);
                    /**
                     * blood group
                     */
                    bloodGroupResponseList = new ArrayList<>();
                    bloodGroupResponseList.clear();
                    bloodNameList = new ArrayList<>();
                    bloodNameList.clear();
                    bloodGroupResponseList.addAll(response.getBloodGroups());
                    for (int i = 0; i < response.getBloodGroups().size(); i++) {
                        bloodNameList.add("" + response.getBloodGroups().get(i).getGruopName());
                    }
                    binding.bloodGroup.setItem(bloodNameList);

                });
        /**
         * now set enterprise
         */
        salesRequisitionViewModel.forGetUserEnterprise(getActivity())
                .observe(getViewLifecycleOwner(), response -> {
                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "Something Wrong");
                        return;
                    }
                    if (response.getStatus() == 400) {
                        infoMessage(getActivity().getApplication(), "" + response.getMessage());
                        return;
                    }

                    enterpriseResponseList = new ArrayList<>();
                    enterpriseResponseList.clear();
                    enterpriseNameList = new ArrayList<>();
                    enterpriseResponseList.clear();
                    enterpriseResponseList.addAll(response.getEnterprise());
                    for (int i = 0; i < response.getEnterprise().size(); i++) {
                        enterpriseNameList.add("" + response.getEnterprise().get(i).getStoreName());
                    }
                    binding.enterprise.setItem(enterpriseNameList);
                    if (enterpriseResponseList.isEmpty()) {
                        return;
                    }
                    binding.storeAccess.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                    binding.storeAccess.setSpinnerList(enterpriseNameList);
                });

    }

    private void validationAndSubmit() {

        /**
         * for Image
         */
        MultipartBody.Part logoBody = null;
        try {
            
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
                        MultipartBody.Part.createFormData("user_photo", file.getName(), requestFile);//here user_photo is name of from data
            } else {
                logoBody = null;
            }
            String storeAccess = PreferenceManager.getInstance(getContext()).getUserCredentials().getStoreID();
            selectedStoreAccessList.add(Integer.parseInt(storeAccess));
        } catch (Exception e) {
        }
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        userViewModel.submitAddNewUserInfo(
                getActivity(), selectedDesignation, selectedDepartment, selectedTitle, binding.displayName.getText().toString(),
                binding.fullName.getText().toString(), selectedGender, binding.primaryMobile.getText().toString(), binding.email.getText().toString(),
                selectedEnterprise, binding.description.getText().toString(), binding.dateOfBirth.getText().toString(),
                selectedBloodGroup, binding.nationality.getText().toString(), binding.altEmail.getText().toString(), binding.altMobile.getText().toString(),
                binding.website.getText().toString(), binding.joiningDate.getText().toString(), logoBody,
                binding.password.getText().toString()
        ).observe(getViewLifecycleOwner(), response -> {
            progressDialog.dismiss();
            if (response == null) {
                errorMessage(getActivity().getApplication(), "Something Wrong");
                return;
            }
            if (response.getStatus() == 400) {
                infoMessage(getActivity().getApplication(), "" + response.getMessage());
                return;
            }
            successMessage(getActivity().getApplication(), "" + response.getMessage());
            getActivity().onBackPressed();
        });


    }


    public void addNewUserDialog() {


        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());

        @SuppressLint("InflateParams")
        View view = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.purchase_dialog, null);
        //Set the view
        builder.setView(view);
        TextView tvTitle, tvMessage;
        ImageView imageIcon = view.findViewById(R.id.img_icon);
        tvMessage = view.findViewById(R.id.tv_message);
        tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("Do you want to add new user ?");//set warning title
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
            validationAndSubmit();
        });

        alertDialog.show();
    }


}