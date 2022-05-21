package com.rupayan_housing.view.fragment.items.edit;

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
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
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
import com.rupayan_housing.clickHandle.EditItemClickHandle;
import com.rupayan_housing.databinding.FragmentEditItemBinding;
import com.rupayan_housing.serverResponseModel.AddNewItemBrand;
import com.rupayan_housing.serverResponseModel.AddNewItemCategory;
import com.rupayan_housing.serverResponseModel.AddNewItemUnit;
import com.rupayan_housing.utils.PathUtil;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.viewModel.AddNewItemViewModel;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.SneakyThrows;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EditItem extends BaseFragment {
    private FragmentEditItemBinding binding;
    private AddNewItemViewModel addNewItemViewModel;


    private String id;


    private static final int PICK_IMAGE = 200;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 300;
    private Uri imageUri;

    /**
     * For category
     */
    private List<AddNewItemCategory> categoryResponseList;
    private List<String> categoryNameList;
    /**
     * for unit
     */
    private List<AddNewItemUnit> unitResponseList;
    private List<String> unitNameList;
    /**
     * For Brand
     */
    private List<AddNewItemBrand> brandResponseList;
    private List<String> brandNameList;


    private String selectedCategory, selectedPrimaryUnit, selectedBrand;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_item, container, false);
        binding.toolbar.toolbarTitle.setText("Update item");
        addNewItemViewModel = new ViewModelProvider(this).get(AddNewItemViewModel.class);
        binding.toolbar.setClickHandle(() -> {
            hideKeyboard(getActivity());
            getActivity().onBackPressed();
        });
        getDataFromPreviousFragment();

        binding.setClickHandle(new EditItemClickHandle() {
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
                hideKeyboard(getActivity());
                if (!(isInternetOn(getActivity()))) {
                    infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
                    return;
                }
                validationAndSubmit();

            }
        });


        /**
         * now handle onlClick on all dropdown
         */
        binding.category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = categoryResponseList.get(position).getCategoryID();
                binding.category.setEnableErrorLabel(false);
                binding.category.setErrorText("Empty");
                /**
                 * now get item code by selected category id
                 */
                getItemCodeBycatId(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.primaryUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPrimaryUnit = unitResponseList.get(position).getID();
                binding.primaryUnit.setEnableErrorLabel(false);
                binding.primaryUnit.setErrorText("Empty");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.brand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedBrand = brandResponseList.get(position).getBrandID();
                binding.brand.setEnableErrorLabel(false);
                binding.brand.setErrorText("Empty");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return binding.getRoot();
    }


    /**
     * get page data from server
     */
    private void getPageDataFromServer() {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        addNewItemViewModel.getAddNewPageData(getActivity())
                .observe(getViewLifecycleOwner(), response -> {
                    progressDialog.dismiss();
                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "Something Wrong");
                        return;
                    }
                    if (response.getStatus() == 400) {
                        infoMessage(getActivity().getApplication(), "" + response.getMessage());
                        return;
                    }

                    /**
                     * for set previous selected item
                     */
                    addNewItemViewModel.getEditItemPageData(getActivity(), id)
                            .observe(getViewLifecycleOwner(), editItemResponse -> {
                                if (editItemResponse == null) {
                                    errorMessage(getActivity().getApplication(), "Something Wrong");
                                    return;
                                }
                                if (editItemResponse.getStatus() == 400) {
                                    infoMessage(getActivity().getApplication(), "" + editItemResponse.getMessage());
                                    return;
                                }

                                /**
                                 * ok now set Data to view
                                 */
                                //for category
                                categoryResponseList = new ArrayList<>();
                                categoryResponseList.clear();
                                categoryNameList = new ArrayList<>();
                                categoryNameList.clear();
                                categoryResponseList.addAll(response.getCategory());
                                for (int i = 0; i < response.getCategory().size(); i++) {
                                    if (!response.getCategory().get(i).getCategoryID().equals("742")){
                                        categoryNameList.add(response.getCategory().get(i).getCategory());
                                    }
                                 }
                                binding.category.setItem(categoryNameList);

                                /**
                                 * now set previous selected category
                                 */
                                for (int i = 0; i < categoryResponseList.size(); i++) {
                                    if (categoryResponseList.get(i).getCategoryID().equals(editItemResponse.getProductDetails().getCategoryID())) {
                                        binding.category.setSelection(i);
                                        break;
                                    }
                                }


                                /**
                                 * for unit
                                 */
                                unitResponseList = new ArrayList<>();
                                unitResponseList.clear();
                                unitNameList = new ArrayList<>();
                                unitResponseList.clear();
                                unitResponseList.addAll(response.getUnit());
                                for (int i = 0; i < response.getUnit().size(); i++) {
                                    unitNameList.add("" + response.getUnit().get(i).getName());
                                }
                                binding.primaryUnit.setItem(unitNameList);

                                /**
                                 * now set previous selected unit
                                 */
                                for (int i = 0; i < unitResponseList.size(); i++) {
                                    if (unitResponseList.get(i).getID().equals(editItemResponse.getProductDetails().getBaseUnit())) {
                                        binding.primaryUnit.setSelection(i);
                                        break;
                                    }
                                }


                                /**
                                 * for brand
                                 */
                                brandResponseList = new ArrayList<>();
                                brandResponseList.clear();
                                brandNameList = new ArrayList<>();
                                brandNameList.clear();
                                brandResponseList.addAll(response.getBrand());
                                for (int i = 0; i < response.getBrand().size(); i++) {
                                    brandNameList.add("" + response.getBrand().get(i).getBrandName());
                                }
                                binding.brand.setItem(brandNameList);


                                for (int i = 0; i < brandResponseList.size(); i++) {
                                    if (brandResponseList.get(i).getBrandID().equals(editItemResponse.getProductDetails().getBrandID())) {
                                        binding.brand.setSelection(i);
                                        break;
                                    }
                                }
                                /**
                                 * now set others previous selected information
                                 */
                                binding.itemName.setText(editItemResponse.getProductDetails().getProductTitle());
                                binding.weight.setText(editItemResponse.getProductDetails().getProductDimensions());
                                binding.itemCode.setText(editItemResponse.getProductDetails().getPcode());
                                binding.note.setText(editItemResponse.getProductDetails().getProductDetails());
                                /**
                                 * now load image
                                 */
                                try {
                                    Glide
                                            .with(getContext())
                                            .load(editItemResponse.getProductDetails().getProductImage())
                                            .centerCrop()
                                            .error(R.drawable.unicef_main)
                                            .placeholder(R.drawable.unicef_main)
                                            .into(binding.image);
                                } catch (Exception e) {
                                    Log.d("ERROR", "" + e.getMessage());
                                }

                            });
                });
    }

    private void getDataFromPreviousFragment() {
        assert getArguments() != null;
        id = getArguments().getString("id");
    }

    private void getItemCodeBycatId(String selectedCategory) {

        hideKeyboard(getActivity());
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }

        addNewItemViewModel.getItemCodeByCatId(getActivity(), selectedCategory)
                .observe(getViewLifecycleOwner(), response -> {
                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "Something Wrong");
                        return;
                    }
                    if (response.getStatus() == 400) {
                        infoMessage(getActivity().getApplication(), "" + response.getMessage());
                        return;
                    }
                    binding.itemCode.setText(response.getCode());
                });
    }

    private void validationAndSubmit() {
        if (selectedCategory == null) {
            infoMessage(getActivity().getApplication(), "Please select category");

            return;
        }
        if (binding.itemName.getText().toString().isEmpty()) {
            binding.itemName.setError("Empty");
            binding.itemName.requestFocus();
            return;
        }
        if (selectedPrimaryUnit == null) {
            infoMessage(getActivity().getApplication(), "Please select primary unit");

            binding.primaryUnit.requestFocus();
            return;
        }
        if (selectedBrand == null) {
            infoMessage(getActivity().getApplication(), "Please brand category");

            return;
        }
        if (binding.itemCode.getText().toString().isEmpty()) {
            binding.itemCode.setError("Empty");
            binding.itemCode.requestFocus();
            return;
        }
        hideKeyboard(getActivity());
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please check your internet connection");
            return;
        }

        showDialoug();
    }

    private void showDialoug() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        @SuppressLint("InflateParams")
        View view = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.purchase_dialog, null);
        //Set the view
        builder.setView(view);
        TextView tvTitle, tvMessage;
        ImageView imageIcon = view.findViewById(R.id.img_icon);
        tvMessage = view.findViewById(R.id.tv_message);
        tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("Do you want to update ?");//set warning title
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


    private void validationAndSave() {

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
                                MultipartBody.Part.createFormData("product_image", file.getName(), requestFile);//here product_image is name of from data
                    } else {
                        logoBody = null;
                    }

                    /**
                     * all ok now save data to server
                     */
                    ProgressDialog progressDialog = new ProgressDialog(getContext());
                    progressDialog.show();
                    addNewItemViewModel.submitEditItemInfo(
                            getActivity(), id, binding.itemName.getText().toString(),
                            selectedCategory, selectedPrimaryUnit, selectedBrand,
                            binding.weight.getText().toString(), binding.note.getText().toString(), logoBody
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


    @Override
    public void onStart() {
        super.onStart();
        /**
         * now getData from server
         */
        getPageDataFromServer();
    }
}