package com.rupayan_housing;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rupayan_housing.databinding.FragmentEnterPriseEditBinding;
import com.rupayan_housing.serverResponseModel.DuePaymentResponse;
import com.rupayan_housing.utils.PathUtil;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.view.fragment.store.list_response.StoreListViewModel;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Objects;

import lombok.SneakyThrows;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class EnterPriseEditFragment extends BaseFragment {
    FragmentEnterPriseEditBinding binding;
    String fullName, shortName, address, contact, email, companyLogo, enterPriseLogo, storeID, storeNo;
    private static final int STORAGE_PERMISSION_REQUEST_CODE_FORUPDATE_COMPANY_LOGO = 300;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 600;
    private Uri imageUriForCompanyLogo, imageUriStoreLogo;

    private StoreListViewModel storeListViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_enter_prise_edit, container, false);
        binding.toolbar.toolbarTitle.setText("Update Enterprise");
        storeListViewModel = new ViewModelProvider(this).get(StoreListViewModel.class);

        getPreviousData();
        setData();
        binding.toolbar.setClickHandle(() -> {
            hideKeyboard(getActivity());
            getActivity().onBackPressed();
        });

        binding.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.fullName.getText().toString().isEmpty()) {
                    binding.fullName.setError("Empty");
                    binding.fullName.requestFocus();
                    return;
                }
                if (binding.shortName.getText().toString().isEmpty()) {
                    binding.shortName.setError("Empty");
                    binding.shortName.requestFocus();
                    return;
                }
                shoeDialog();

            }
        });
        binding.updateCompanyLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(checkStoragePermission())) {
                    requestStoragePermission(STORAGE_PERMISSION_REQUEST_CODE_FORUPDATE_COMPANY_LOGO);
                } else {
                    getLogoImageFromFile(getActivity().getApplication(), 200);
                }
            }
        });
   /*     binding.updateEnterPriseLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(checkStoragePermission())) {
                    requestStoragePermission(STORAGE_PERMISSION_REQUEST_CODE);
                } else {
                    getLogoImageFromFile(getActivity().getApplication(), 400);
                }
            }
        });
*/

        return binding.getRoot();
    }

    private void shoeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        @SuppressLint("InflateParams")
        View view = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.purchase_dialog, null);
        //Set the view
        builder.setView(view);
        TextView tvTitle, tvMessage;
        ImageView imageIcon = view.findViewById(R.id.img_icon);
        tvMessage = view.findViewById(R.id.tv_message);
        tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("Do You Want to Update Enterprise ?");//set warning title
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
            updateEnterPriseData();
        });

        alertDialog.show();
    }

    private void updateEnterPriseData() {
        /**
         * for Image
         */
        MultipartBody.Part imageUriForCompanyLogologoBody = null;//
        MultipartBody.Part imageUriStoreLogoLogologoBody = null;
        try {

            if (imageUriForCompanyLogo != null) {//logo image not mandatory here so if user not select any logo image by default it send null
                File file = null;
                try {
                    file = new File(PathUtil.getPath(getActivity(), imageUriForCompanyLogo));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

                // MultipartBody.Part is used to send also the actual file name
                imageUriForCompanyLogologoBody = MultipartBody.Part.createFormData("company_logo", file.getName(), requestFile);//here user_photo is name of from data
            } else {
                imageUriForCompanyLogologoBody = null;
            }

        } catch (Exception e) {
        }
        try {

            if (imageUriStoreLogo != null) {//logo image not mandatory here so if user not select any logo image by default it send null
                File file = null;
                try {
                    file = new File(PathUtil.getPath(getActivity(), imageUriStoreLogo));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

                // MultipartBody.Part is used to send also the actual file name
                imageUriStoreLogoLogologoBody = MultipartBody.Part.createFormData("store_logo", file.getName(), requestFile);//here user_photo is name of from data
            } else {
                imageUriStoreLogoLogologoBody = null;
            }

        } catch (Exception e) {
        }
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }
        storeListViewModel.editEnterPrise(getActivity(), storeNo, binding.fullName.getText().toString(),
                binding.shortName.getText().toString(), binding.address.getText().toString(), storeID,
                imageUriStoreLogoLogologoBody, imageUriForCompanyLogologoBody, binding.contact.getText().toString(), binding.email.getText().toString()).observe(getViewLifecycleOwner(), new Observer<DuePaymentResponse>() {
            @Override
            public void onChanged(DuePaymentResponse response) {
                try {
                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "Something Wrong");
                        return;
                    }
                    if (response.getStatus() == 400) {
                        errorMessage(getActivity().getApplication(), response.getMessage());
                        return;
                    }
                    successMessage(getActivity().getApplication(), response.getMessage());
                    hideKeyboard(getActivity());
                    getActivity().onBackPressed();

                } catch (Exception e) {
                }
            }
        });

    }

    private void setData() {
        binding.fullName.setText("" + fullName);
        binding.shortName.setText("" + shortName);
        if (address != null) {
            binding.address.setText("" + address);
        }
        if (contact != null) {
            binding.contact.setText("" + contact);
        }
        if (email != null) {
            binding.email.setText("" + email);
        }

        try {
            Glide.with(getContext()).load(companyLogo).centerCrop().
                    into(binding.updateCompanyLogo);

        } catch (NullPointerException e) {
            Log.d("ERROR", e.getMessage());
        }
        try {
            Glide.with(getContext()).load(enterPriseLogo).centerCrop().
                    into(binding.updateEnterPriseLogo);

        } catch (NullPointerException e) {
            Log.d("ERROR", e.getMessage());
        }
    }

    private void getPreviousData() {
        try {
            fullName = getArguments().getString("fullName");
            shortName = getArguments().getString("shortName");
            address = getArguments().getString("address");
            contact = getArguments().getString("contact");
            email = getArguments().getString("email");
            companyLogo = getArguments().getString("companyLogo");
            enterPriseLogo = getArguments().getString("enterPriseLogo");
            storeID = getArguments().getString("id");
            storeNo = getArguments().getString("storeNo");

        } catch (Exception e) {
        }
    }

    @SneakyThrows
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    //Display an error
                    return;
                }


                InputStream inputStream = getContext().getContentResolver().openInputStream(data.getData());
                imageUriStoreLogo = data.getData();


                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                binding.updateCompanyLogo.setImageDrawable(null);
                binding.updateCompanyLogo.setImageDrawable(null);
                binding.updateCompanyLogo.destroyDrawingCache();
                binding.updateCompanyLogo.setImageBitmap(bitmap);

                binding.imageName2.setText(String.valueOf(new File("" + data.getData()).getName()));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    private void saveImageq(int requestCode, int resultCode, Intent data) throws FileNotFoundException {
      /*  if (requestCode == 400 && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }

            InputStream inputStream = getContext().getContentResolver().openInputStream(data.getData());
            imageUriForCompanyLogo = data.getData();


            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        *//*    binding.updateEnterPriseLogo.setImageDrawable(null);
            binding.updateEnterPriseLogo.setImageDrawable(null);
            binding.updateEnterPriseLogo.destroyDrawingCache();*//*
            binding.updateEnterPriseLogo.setImageBitmap(bitmap);


            binding.imageName1.setText(String.valueOf(new File("" + data.getData()).getName()));
        }
*/
        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }


            InputStream inputStream = getContext().getContentResolver().openInputStream(data.getData());
            imageUriStoreLogo = data.getData();


            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        /*    binding.updateCompanyLogo.setImageDrawable(null);
            binding.updateCompanyLogo.setImageDrawable(null);
            binding.updateCompanyLogo.destroyDrawingCache();*/
            binding.updateCompanyLogo.setImageBitmap(bitmap);

            binding.imageName2.setText(String.valueOf(new File("" + data.getData()).getName()));
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case STORAGE_PERMISSION_REQUEST_CODE_FORUPDATE_COMPANY_LOGO:
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


}