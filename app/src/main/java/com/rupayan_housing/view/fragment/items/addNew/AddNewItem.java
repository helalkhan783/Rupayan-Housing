package com.rupayan_housing.view.fragment.items.addNew;

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
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.rupayan_housing.R;
import com.rupayan_housing.clickHandle.AddNewItemClickHandle;
import com.rupayan_housing.clickHandle.ToolbarClickHandle;
import com.rupayan_housing.databinding.FragmentAddNewItemBinding;
import com.rupayan_housing.serverResponseModel.AddNewItemBrand;
import com.rupayan_housing.serverResponseModel.AddNewItemCategory;
import com.rupayan_housing.serverResponseModel.AddNewItemResponse;
import com.rupayan_housing.serverResponseModel.AddNewItemUnit;
import com.rupayan_housing.serverResponseModel.DuePaymentResponse;
import com.rupayan_housing.serverResponseModel.ItemCodeResponse;
import com.rupayan_housing.utils.ManagementUtils;
import com.rupayan_housing.utils.PathUtil;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.viewModel.AddNewItemViewModel;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

@NoArgsConstructor
public class AddNewItem extends BaseFragment implements AdapterView.OnItemSelectedListener {
    private FragmentAddNewItemBinding binding;
    private AddNewItemViewModel addNewItemViewModel;
    public boolean isSubmit = false;

    private static final int PICK_IMAGE = 200;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 300;
    private Uri imageUri;
    Toasty toast;
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

    ArrayAdapter<String> arrayadapter;


    private String selectedCategory, selectedPrimaryUnit, selectedBrand;


    public AddNewItem(boolean isSubmit) {
        this.isSubmit = isSubmit;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_new_item, container, false);

        binding.toolbar.toolbarTitle.setText("Add "+ ManagementUtils.addNewItem);
        addNewItemViewModel = new ViewModelProvider(this).get(AddNewItemViewModel.class);
        binding.toolbar.setClickHandle(() -> {
            hideKeyboard(getActivity());
            getActivity().onBackPressed();
        });
        binding.spinner1.setOnItemSelectedListener(this);
        /**
         * now getData from server
         */
        getPageDataFromServer();

        binding.setClickHandle(new AddNewItemClickHandle() {
            @Override
            public void getImage() {
                if (!(checkStoragePermission())) {
                    requestStoragePermission(STORAGE_PERMISSION_REQUEST_CODE);
                } else {
                    getLogoImageFromFile(getActivity().getApplication(), PICK_IMAGE);
                }
            }

            @Override
            public void save() {
                if (!(isInternetOn(getActivity()))) {
                    infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
                    return;
                }
                hideKeyboard(getActivity());
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

                    return;
                }
                if (selectedBrand == null) {
                    infoMessage(getActivity().getApplication(), "Please select brand");

                    return;
                }
                if (binding.itemCode.getText().toString().isEmpty()) {
                    binding.itemCode.setError("Empty");
                    binding.itemCode.requestFocus();
                    return;
                }
                addNewItemDialog();
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
     * For save add new item info
     */
    private void validationAndSave() {


        hideKeyboard(getActivity());
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
                    MultipartBody.Part.createFormData("product_image", file.getName(), requestFile);//here product_image is name of from data
        } else {
            logoBody = null;
        }

        /**
         * all ok now save data to server
         */
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        addNewItemViewModel.submitAddNewItemInfo(
                getActivity(), selectedCategory, binding.itemName.getText().toString(),
                selectedPrimaryUnit, selectedBrand, binding.weight.getText().toString(),
                binding.note.getText().toString(), logoBody
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
            successMessage(getActivity().getApplication(), "" + response.getMessage());Bundle bundle = new Bundle();
            bundle.putString("id", response.getProductID());
            bundle.putString("itemName", binding.itemName.getText().toString());

            Navigation.findNavController(getView()).navigate(R.id.action_addNewItem_to_confirmAddNewItem, bundle);
            removeAllInputData();
        });
    }

    private void removeAllInputData() {
        selectedCategory = null;
        selectedPrimaryUnit = null;
        selectedBrand = null;
        binding.spinner1.setSelected(false);
        binding.itemName.setText("");
        binding.primaryUnit.setSelected(false);
        binding.weight.setText("");
        binding.brand.setSelected(false);
        binding.itemCode.setText("");
        binding.note.setText("");
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

    /**
     * get page data from server
     */
    private void getPageDataFromServer() {
        if (isSubmit) {
            selectedCategory = null;
            selectedPrimaryUnit = null;
            selectedBrand = null;
            binding.spinner1.setSelected(false);
            binding.itemName.setText("");
            binding.primaryUnit.setSelected(false);
            binding.weight.setText("");
            binding.brand.setSelected(false);
            binding.itemCode.setText("");
            binding.note.setText("");
        }


        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }
        addNewItemViewModel.getAddNewPageData(getActivity())
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
                     * for unit
                     */
                    unitResponseList = new ArrayList<>();
                    unitResponseList.clear();
                    unitNameList = new ArrayList<>();
                    unitResponseList.clear();
                    unitResponseList.addAll(response.getUnit());
                    for (int i = 0; i < response.getUnit().size(); i++) {
                        unitNameList.add(response.getUnit().get(i).getName());
                    }
                    binding.primaryUnit.setItem(unitNameList);
                    /**
                     * for brand
                     */
                    brandResponseList = new ArrayList<>();
                    brandResponseList.clear();
                    brandNameList = new ArrayList<>();
                    brandNameList.clear();
                    brandResponseList.addAll(response.getBrand());
                    for (int i = 0; i < response.getBrand().size(); i++) {
                        brandNameList.add(response.getBrand().get(i).getBrandName());
                    }
                    binding.brand.setItem(brandNameList);


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
                        categoryNameList.add(response.getCategory().get(i).getCategory());

                    }

                    List<String> ok = new ArrayList<>(Arrays.asList("Select"));
                    ok.addAll(categoryNameList);
                    try {
                        arrayadapter = new ArrayAdapter<String>(getActivity(), R.layout.tv_ok, ok) {
                            @Override
                            public boolean isEnabled(int position) {
                                if (position == 1 || position == 7) { //Disable the third item of spinner.
                                    return false;
                                } else {
                                    return true;
                                }
                            }

                            @Override
                            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                                View spinnerview = super.getDropDownView(position, convertView, parent);
                                TextView spinnertextview = (TextView) spinnerview;
                                if (position == 1 ||position == 7) { //Set the disable spinner item color fade.
                                    spinnertextview.setTextColor(Color.parseColor("#bcbcbb"));
                                }
                                else {
                                    spinnertextview.setTextColor(Color.BLACK);
                                }
                                return spinnerview;
                            }
                        };
                        arrayadapter.setDropDownViewResource(R.layout.tv_ok);
                        binding.spinner1.setAdapter(arrayadapter);
                    } catch (Exception e) {
                    }


                });
    }

    private void getItemCodeBycatId(String selectedCategory) {

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
                    binding.itemCode.setText("" + response.getCode());
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isSubmit) {
            selectedCategory = null;
            selectedPrimaryUnit = null;
            selectedBrand = null;
            binding.spinner1.setSelected(false);
            binding.itemName.setText("");
            binding.primaryUnit.setSelected(false);
            binding.weight.setText("");
            binding.brand.setSelected(false);
            binding.itemCode.setText("");
            binding.note.setText("");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isSubmit) {
            selectedCategory = null;
            selectedPrimaryUnit = null;
            selectedBrand = null;
            binding.spinner1.setSelected(false);
            binding.itemName.setText("");
            binding.primaryUnit.setSelected(false);
            binding.weight.setText("");
            binding.brand.setSelected(false);
            binding.itemCode.setText("");
            binding.note.setText("");
        }
    }

    private void addNewItemDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        @SuppressLint("InflateParams")
        View view = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.purchase_dialog, null);
        //Set the view
        builder.setView(view);
        TextView tvTitle, tvMessage;
        ImageView imageIcon = view.findViewById(R.id.img_icon);
        tvMessage = view.findViewById(R.id.tv_message);
        tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("Do you want to add this item?");//set warning title
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        try {
            if (position == 0) {
                selectedCategory = null;
                return;
            }
            selectedCategory = categoryResponseList.get(position).getCategoryID();
            getItemCodeBycatId(selectedCategory);
        } catch (Exception e) {

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}