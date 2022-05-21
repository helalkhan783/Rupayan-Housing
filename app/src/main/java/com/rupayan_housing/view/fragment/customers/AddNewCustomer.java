package com.rupayan_housing.view.fragment.customers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
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

import com.rupayan_housing.R;
import com.rupayan_housing.clickHandle.AddNewCustomerClickHandle;
import com.rupayan_housing.databinding.FragmentAddNewCustomerBinding;
import com.rupayan_housing.serverResponseModel.DistrictListResponse;
import com.rupayan_housing.serverResponseModel.DivisionResponse;
import com.rupayan_housing.serverResponseModel.DuePaymentResponse;
import com.rupayan_housing.serverResponseModel.ThanaList;
import com.rupayan_housing.utils.PathUtil;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.viewModel.CustomerViewModel;
import com.rupayan_housing.viewModel.MillerProfileInfoViewModel;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.SneakyThrows;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AddNewCustomer extends BaseFragment {
    private FragmentAddNewCustomerBinding binding;
    private MillerProfileInfoViewModel millerProfileInfoViewModel;
    private CustomerViewModel customerViewModel;
    private static final int PICK_IMAGE = 200;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 300;
    private Uri imageUri;
    /**
     * For Division
     */
    private List<DivisionResponse> divisionResponseList;
    private List<String> divisionNameList;
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
     * For Selected Customer
     */
    private List<String> selectedCustomerTypeName;
    private List<String> selectedCustomerTypeIdList;

    private String selectedDivision, selectedDistrict, selectedThana, selectedCustomerType;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_new_customer, container, false);
        millerProfileInfoViewModel = new ViewModelProvider(this).get(MillerProfileInfoViewModel.class);
        customerViewModel = new ViewModelProvider(this).get(CustomerViewModel.class);
        binding.toolbar.toolbarTitle.setText("Add New Customer");

        /**
         * now get Page Data from server
         */
        getPageDataFromServer();
        binding.toolbar.setClickHandle(() -> {
            hideKeyboard(getActivity());
            getActivity().onBackPressed();
        });
        /**
         * Now save customer info
         */
        binding.setClickHandle(new AddNewCustomerClickHandle() {
            @Override
            public void getImage() {
                if (!(checkStoragePermission())) {
                    requestStoragePermission(STORAGE_PERMISSION_REQUEST_CODE);
                } else {
                    getLogoImageFromFile(getActivity().getApplication(), PICK_IMAGE);
                }
            }

            @Override
            public void submit() {
                if (!(isInternetOn(getActivity()))) {
                    infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
                    return;
                }
                if (binding.companyName.getText().toString().isEmpty()) {
                    binding.companyName.setError("Empty");
                    binding.companyName.requestFocus();
                    return;
                }
                if (binding.ownerName.getText().toString().isEmpty()) {
                    binding.ownerName.setError("Empty");
                    binding.ownerName.requestFocus();
                    return;
                }
                if (binding.phone.getText().toString().isEmpty()) {
                    binding.phone.setError("Empty");
                    binding.phone.requestFocus();
                    return;
                }
                if (!binding.phone.getText().toString().isEmpty()) {
                    if (!isValidPhone(binding.phone.getText().toString())) {
                        binding.phone.setError("Invalid Contact Number");
                        binding.phone.requestFocus();
                        return;
                    }
                }

                if (!binding.altPhone.getText().toString().isEmpty()) {
                    if (!isValidPhone(binding.altPhone.getText().toString())) {
                        binding.altPhone.setError("Invalid Number");
                        binding.altPhone.requestFocus();
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

                if (selectedDivision == null) {
                    infoMessage(getActivity().getApplication(), "Please select division");
                    return;
                }
                if (selectedDistrict == null) {
                    infoMessage(getActivity().getApplication(), "Please select district");
                    return;
                }

                if (selectedThana == null) {
                    infoMessage(getActivity().getApplication(), "Please select upazila/thana");
                    return;
                }
                if (selectedCustomerType == null) {
                    infoMessage(getActivity().getApplication(), "Please select customer type");
                    return;
                }
                addNewCustomerDialog();
            }
        });
        /**
         * Now handle division item click
         */
        binding.division.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDivision = divisionResponseList.get(position).getDivisionId();
                binding.division.setEnableErrorLabel(false);
                /**
                 * Now get district list based on division id
                 */
                getDistrictListByDivisionId(selectedDivision);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        /**
         * Now handle district item click
         */
        binding.district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDistrict = districtListResponseList.get(position).getDistrictId();
                binding.district.setEnableErrorLabel(false);
                /**
                 * Now get Thana list based on district
                 */
                getThanaListByDistrictId(selectedDistrict);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /**
         * now handle Thana item click
         */
        binding.thana.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedThana = thanaListsResponse.get(position).getUpazilaId();
                binding.thana.setEnableErrorLabel(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        /**
         * now handle selected customer item click
         */
        binding.customerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCustomerType = selectedCustomerTypeIdList.get(position);
                binding.customerType.setEnableErrorLabel(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return binding.getRoot();
    }

    private void validationAndSubmit() {
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
                    MultipartBody.Part.createFormData("image", file.getName(), requestFile);//here document is name of from data
        } else {
            logoBody = null;
        }
        /**
         * All ok now send customer info to server
         */

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        customerViewModel.addNewCustomer(
                getActivity(), binding.companyName.getText().toString(),
                binding.ownerName.getText().toString(),
                binding.phone.getText().toString(),
                binding.altPhone.getText().toString(),
                binding.email.getText().toString(),
                selectedDivision, selectedDistrict, selectedThana, binding.bazar.getText().toString(),
                binding.nid.getText().toString(),
                binding.tin.getText().toString(),
                "0",
                "1",
                selectedCustomerType,
                binding.address.getText().toString(),
                "0",
                logoBody,
                binding.note.getText().toString())
                .observe(getViewLifecycleOwner(), response -> {
                    progressDialog.dismiss();
                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "ERROR");
                        return;
                    }
                    if (response.getStatus() != 200) {
                        infoMessage(getActivity().getApplication(), "" + response.getMessage());
                        return;
                    }
                    hideKeyboard(getActivity());
                    successMessage(getActivity().getApplication(), "" + response.getMessage());
                    getActivity().onBackPressed();
                });


    }

    private void getPageDataFromServer() {

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

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//for api level 24 or up
                        divisionNameList = response.getDivisions()
                                .stream()
                                .map(DivisionResponse::getName)
                                .filter(name -> !name.isEmpty())
                                .collect(Collectors.toList());
                    } else {//for lower version device below 24
                        for (int i = 0; i < response.getDivisions().size(); i++) {
                            divisionNameList.add(response.getDivisions().get(i).getName());
                        }
                    }
                    binding.division.setItem(divisionNameList);
                });


        /**
         * now set selected customer
         */
        selectedCustomerTypeName = new ArrayList<>();
        selectedCustomerTypeName.clear();
        selectedCustomerTypeIdList = new ArrayList<>();
        selectedCustomerTypeIdList.clear();

        selectedCustomerTypeName.addAll(Arrays.asList("General"));
        selectedCustomerTypeIdList.addAll(Arrays.asList("7"));
        binding.customerType.setItem(selectedCustomerTypeName);

    }

    private void getThanaListByDistrictId(String selectedDistrict) {

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
                        thanaNameList.add(response.getLists().get(i).getName());
                    }
                    binding.thana.setItem(thanaNameList);
                });
    }

    private void getDistrictListByDivisionId(String selectedDivision) {

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
                        districtNameList.add(response.getLists().get(i).getName());
                    }
                    binding.district.setItem(districtNameList);
                });
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
             * now set licenseImageName
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
                    infoMessage(requireActivity().getApplication(), "Permission Granted");
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    infoMessage(requireActivity().getApplication(), "Permission Decline");
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }

    private void addNewCustomerDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());

        @SuppressLint("InflateParams")
        View view = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.purchase_dialog, null);
        //Set the view
        builder.setView(view);
        TextView tvTitle, tvMessage;
        ImageView imageIcon = view.findViewById(R.id.img_icon);
        tvMessage = view.findViewById(R.id.tv_message);
        tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("Do you want to add Customer?");//set warning title
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