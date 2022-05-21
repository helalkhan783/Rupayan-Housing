package com.rupayan_housing.view.fragment.supplier;

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
import com.rupayan_housing.clickHandle.ToolbarClickHandle;
import com.rupayan_housing.databinding.FragmentAddNewForeignSupplierBinding;
import com.rupayan_housing.serverResponseModel.CountryListResponse;
import com.rupayan_housing.serverResponseModel.MillerProfileInfoResponse;
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

import lombok.SneakyThrows;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AddNewForeignSupplierFragment extends BaseFragment {
    private FragmentAddNewForeignSupplierBinding binding;

    MillerProfileInfoViewModel millerProfileInfoViewModel;
    private CustomerViewModel customerViewModel;

    private static final int PICK_IMAGE = 200;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 300;
    private Uri imageUri;


    /**
     * For Selected Customer
     */
    private List<String> selectedCustomerTypeName;
    private List<String> selectedCustomerTypeIdList;


    /**
     * For Selected  country
     */

    private List<String> countryName;
    private List<CountryListResponse> countryResponsesList;

    String countryId;
    String selectedCustomerType;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_new_foreign_supplier, container, false);
        millerProfileInfoViewModel = new ViewModelProvider(this).get(MillerProfileInfoViewModel.class);
        customerViewModel = new ViewModelProvider(this).get(CustomerViewModel.class);


        binding.toolbar.toolbarTitle.setText("Add Foreign Supplier");

        /** back control */
        binding.toolbar.setClickHandle(() -> {
            hideKeyboard(getActivity());
            getActivity().onBackPressed();
        });
        getPageData();
        setClick();
        /**
         * now handle selected customer item click
         */

        /**
         * now set selected customer
         */
        selectedCustomerTypeName = new ArrayList<>();
        selectedCustomerTypeName.clear();
        selectedCustomerTypeIdList = new ArrayList<>();
        selectedCustomerTypeIdList.clear();

        selectedCustomerTypeName.addAll(Arrays.asList("General"));
        selectedCustomerTypeIdList.addAll(Arrays.asList("5"));
        binding.customerType.setItem(selectedCustomerTypeName);

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

        binding.country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countryId = String.valueOf(countryResponsesList.get(position).getCountryID());
                binding.country.setEnableErrorLabel(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return binding.getRoot();
    }

    private void setClick() {
        binding.image.setOnClickListener(v -> {
            if (!(checkStoragePermission())) {
                requestStoragePermission(STORAGE_PERMISSION_REQUEST_CODE);
            } else {
                getLogoImageFromFile(getActivity().getApplication(), PICK_IMAGE);
            }
        });

        /**
         * For Add new foreign supplier
         */
        binding.saveBtn.setOnClickListener(v -> validation());
    }

    private void validation() {

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
                binding.phone.setError("Invalid Mobile Number");
                binding.phone.requestFocus();
                return;
            }
        }
        if (!binding.altPhone.getText().toString().isEmpty()) {
            if (!isValidPhone(binding.altPhone.getText().toString())) {
                binding.altPhone.setError("Invalid Mobile Number");
                binding.altPhone.requestFocus();
                return;
            }
        }
        if (!binding.email.getText().toString().isEmpty()) {
            if (!isValidEmail(binding.email.getText().toString())) {
                binding.email.setError("Invalid EmailX");
                binding.email.requestFocus();
                return;
            }
        }

        if (countryId == null) {
            infoMessage(requireActivity().getApplication(), "Please select country");
            return;
        }
        if (selectedCustomerType == null) {
            infoMessage(requireActivity().getApplication(), "Please select supplier type");
            return;
        }
        addDialog();
    }

    private void getPageData() {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check your Internet Connection");
            return;
        }
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        millerProfileInfoViewModel.getProfileInfoResponse(getActivity()).observe(getViewLifecycleOwner(), new Observer<MillerProfileInfoResponse>() {
            @Override
            public void onChanged(MillerProfileInfoResponse response) {
                progressDialog.dismiss();
                if (response == null) {
                    errorMessage(getActivity().getApplication(), "Something Wrong");
                    return;
                }
                if (response.getStatus() == 400) {
                    infoMessage(getActivity().getApplication(), "" + response.getMessage());
                    return;
                }

                if (response.getStatus() == 200) {
                    /**
                     * now set division list
                     */
                    countryResponsesList = new ArrayList<>();
                    countryResponsesList.clear();
                    countryResponsesList.addAll(response.getCountries());

                    countryName = new ArrayList<>();
                    countryName.clear();

                    for (int i = 0; i < response.getCountries().size(); i++) {
                        countryName.add(response.getCountries().get(i).getCountryName());
                    }
                    binding.country.setItem(countryName);

                }


                /**
                 * now set selected customer
                 */
                selectedCustomerTypeName = new ArrayList<>();
                selectedCustomerTypeName.clear();
                selectedCustomerTypeIdList = new ArrayList<>();
                selectedCustomerTypeIdList.clear();

                selectedCustomerTypeName.addAll(Arrays.asList("General"));
                selectedCustomerTypeIdList.addAll(Arrays.asList("5"));
                binding.customerType.setItem(selectedCustomerTypeName);
            }
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

    private void addDialog() {
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
            addNewNewForeignSupplierDialog();
        });

        alertDialog.show();

    }

    private void addNewNewForeignSupplierDialog() {
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

        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check your Internet Connection");
            return;
        }

        /**
         * All ok now send customer info to server
         */
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        customerViewModel.addNewForeignSupplier(
                getActivity(), binding.companyName.getText().toString(),
                binding.companyName.getText().toString(),
                binding.phone.getText().toString(),
                binding.altPhone.getText().toString(),
                binding.email.getText().toString(),
                "0", "0", "0", "0",
                "0",
                binding.licence.getText().toString(),
                "0",
                countryId,
                "5",
                binding.address.getText().toString(),
                "0",
                logoBody,
                binding.note.getText().toString(), binding.licence.getText().toString())
                .observe(getViewLifecycleOwner(), response -> {
                    progressDialog.dismiss();
                    try {
                        if (response == null) {
                            errorMessage(getActivity().getApplication(), "ERROR");
                            return;
                        }
                        if (response.getStatus() == 400) {
                            infoMessage(getActivity().getApplication(), "" + response.getMessage());
                            Log.d("RESPONSE", "" + response.getMessage());
                            return;
                        }
                        successMessage(getActivity().getApplication(), "" + response.getMessage());
                        getActivity().onBackPressed();
                    } catch (Exception e) {
                        Log.d("Error", e.getMessage());
                    }
                });
    }


}