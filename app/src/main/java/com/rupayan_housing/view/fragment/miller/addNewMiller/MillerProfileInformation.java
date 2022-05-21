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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.rupayan_housing.R;
import com.rupayan_housing.adapter.MillerTypeAdapter;
import com.rupayan_housing.clickHandle.MillerProfileInformationClickHandle;
import com.rupayan_housing.databinding.FragmentMillerProfileInformationBinding;
import com.rupayan_housing.retrofit.GetMillTypeId;
import com.rupayan_housing.serverResponseModel.DistrictListResponse;
import com.rupayan_housing.serverResponseModel.DivisionResponse;
import com.rupayan_housing.serverResponseModel.MillByZoneIdResponse;
import com.rupayan_housing.serverResponseModel.MillTypeResponse;
import com.rupayan_housing.serverResponseModel.OwnerTypeResponse;
import com.rupayan_housing.serverResponseModel.ProcessTypeResponse;
import com.rupayan_housing.serverResponseModel.ThanaList;
import com.rupayan_housing.serverResponseModel.ZoneResponse;
import com.rupayan_housing.utils.PathUtil;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.viewModel.MillerProfileInfoViewModel;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.SneakyThrows;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MillerProfileInformation extends BaseFragment implements GetMillTypeId {
    public static FragmentMillerProfileInformationBinding binding;

    public static String profileIdFromServer;


    private ViewPager viewPager;

    private MillerProfileInfoViewModel millerProfileInfoViewModel;
    private static final int PICK_IMAGE = 200;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 300;
    private Uri imageUri;

    /**
     * for zone
     */
    private List<ZoneResponse> zoneResponseList;
    private List<String> zoneList;

    /**
     * for process type
     */
    private List<ProcessTypeResponse> processTypeResponseList;
    private List<String> processTypeNameList;

    /**
     * for mill type
     */

    /**
     * for type of owner
     */
    private List<OwnerTypeResponse> ownerTypeResponseList;
    private List<String> ownerTypeNameList;

    /**
     * for division
     */
    private List<DivisionResponse> divisionResponseList;
    private List<String> divisionNameList;

    /**
     * for district
     */
    private List<DistrictListResponse> districtListResponseList;
    private List<String> districtNameList;

    /**
     * for Thana
     */
    private List<ThanaList> thanaListsResponse;
    private List<String> thanaNameResponse;

    private String selectedProcessType, selectedOwnerType, selectedDivision, selectedDistrict, selectedThana;
    public static String selectedZone;
    private String millerType1 = "";
    private String millerType2 = "";

    public static List<String> typeAllList = new ArrayList<>();

    private String countProfile;
    public static String selectedZoneRemarks;

    public static String id1, id2;
    String millId;

    public static Set<String> selectedMillerTypeList = new HashSet<>();//for store miller type id
    //Set<String> millRemarks = new HashSet<>();//for store miller type id
    List<String> millRemarks = new ArrayList<>();//for store miller type id
    private List<MillTypeResponse> millTypeResponseList;

    private boolean firstCondition = false;

    private boolean sndCondition = false;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_miller_profile_information, container, false);
        viewPager = getActivity().findViewById(R.id.viewPager);
        millerProfileInfoViewModel = new ViewModelProvider(this).get(MillerProfileInfoViewModel.class);
        selectedMillerTypeList = new HashSet<>();

        /** now get Page data from server */
        getPageInfoFromServer();

        /** for save */
        try {
            binding.setClickHandle(new MillerProfileInformationClickHandle() {
                @Override
                public void save() {
                    validationAndSave();
                }

                @Override
                public void pickImage() {
                    if (!(checkStoragePermission())) {
                        requestStoragePermission(STORAGE_PERMISSION_REQUEST_CODE);
                    } else {
                        getLogoImageFromFile(getActivity().getApplication(), PICK_IMAGE);
                    }
                }
            });

        } catch (Exception e) {
        }
        return binding.getRoot();
    }

    private void getPageInfoFromServer() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        try {
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

                        /** for zone */
                        zoneResponseList = new ArrayList<>();
                        zoneResponseList.clear();
                        zoneList = new ArrayList<>();
                        zoneList.clear();
                        zoneResponseList.addAll(response.getZones());

                        for (int i = 0; i < response.getZones().size(); i++) {
                            zoneList.add("" + response.getZones().get(i).getZoneName());
                        }
                        binding.zone.setItem(zoneList);

                        /** for process type */
                        try {
                            processTypeResponseList = new ArrayList<>();
                            processTypeResponseList.clear();
                            processTypeNameList = new ArrayList<>();
                            processTypeNameList.clear();
                            processTypeResponseList.addAll(response.getProcessTypes());

                            for (int i = 0; i < response.getProcessTypes().size(); i++) {
                                processTypeNameList.add("" + response.getProcessTypes().get(i).getProcessTypeName());
                            }
                            binding.processType.setItem(processTypeNameList);

                            /** now set millType */
                            millTypeResponseList = new ArrayList<>();
                            millTypeResponseList.clear();
                            millTypeResponseList.addAll(response.getMillTypes());
                            countProfile = response.getCountProfile();

                            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                            binding.millTypeRv.setLayoutManager(layoutManager);
                            MillerTypeAdapter adapter = new MillerTypeAdapter(getContext(), response.getMillTypes(), this);
                            binding.millTypeRv.setAdapter(adapter);


                        } catch (Exception e) {
                        }
                        /** now set Type of owner */
                        ownerTypeResponseList = new ArrayList<>();
                        ownerTypeResponseList.clear();
                        ownerTypeResponseList.addAll(response.getOwnerTypes());
                        ownerTypeNameList = new ArrayList<>();
                        ownerTypeNameList.clear();
                        for (int i = 0; i < response.getOwnerTypes().size(); i++) {
                            ownerTypeNameList.add("" + response.getOwnerTypes().get(i).getOwnerTypeName());
                        }
                        binding.ownerType.setItem(ownerTypeNameList);

                        /** now set division */
                        divisionResponseList = new ArrayList<>();
                        divisionResponseList.clear();
                        divisionNameList = new ArrayList<>();
                        divisionResponseList.clear();
                        divisionResponseList.addAll(response.getDivisions());
                        for (int i = 0; i < response.getDivisions().size(); i++) {
                            divisionNameList.add("" + response.getDivisions().get(i).getName());
                        }
                        binding.division.setItem(divisionNameList);

                        /** set count profile */
                        countProfile = response.getCountProfile();

                    });

        } catch (Exception e) {
        }

        binding.zone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedZone = zoneResponseList.get(position).getZoneID();
                totalMillByZone(selectedZone);

                try {
                    if (selectedZoneRemarks != null) {
                        selectedZoneRemarks = null;
                        selectedZoneRemarks = zoneResponseList.get(position).getRemarks();
                        return;
                    }
                    selectedZoneRemarks = zoneResponseList.get(position).getRemarks();
                } catch (Exception e) {
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.processType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedProcessType = processTypeResponseList.get(position).getProcessTypeID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.ownerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedOwnerType = ownerTypeResponseList.get(position).getOwnerTypeID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.division.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDivision = divisionResponseList.get(position).getDivisionId();
                binding.division.setEnableErrorLabel(false);
                /**
                 * now set district based on the current division
                 */
                if (selectedDivision != null) {
                    getDistrictListByDivisionId(selectedDivision);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDistrict = districtListResponseList.get(position).getDistrictId();
                binding.district.setEnableErrorLabel(false);
                /**
                 * now set thana based on the current district
                 */
                if (selectedDistrict != null) {
                    getThanaListByDistrictId(selectedDistrict);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
    }

    private void totalMillByZone(String selectedZone) {
        millerProfileInfoViewModel.getMill(getActivity(), selectedZone).observe(getViewLifecycleOwner(), new Observer<MillByZoneIdResponse>() {
            @Override
            public void onChanged(MillByZoneIdResponse response) {
                try {
                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "Something wrong");
                        return;
                    }
                    if (response.getStatus() == 400) {
                        return;
                    }
                    if (response.getStatus() == 500) {
                        return;
                    }
                    countProfile = String.valueOf(response.getTotalMill());

                    setMillIdBasedOnZoneAndMillType();

                } catch (Exception e) {
                }
            }
        });
    }

    public void transfer(String selectedZoneRemarks) {

    }

    /**
     * for set thana Name
     */
    private void getThanaListByDistrictId(String selectedDistrict) {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();

        try {
            millerProfileInfoViewModel.getThanaListByDistrictId(getActivity(), selectedDistrict)
                    .observe(getViewLifecycleOwner(), response -> {
                        progressDialog.dismiss();
                        thanaListsResponse = new ArrayList<>();
                        thanaListsResponse.clear();
                        thanaNameResponse = new ArrayList<>();
                        thanaNameResponse.clear();
                        thanaListsResponse.addAll(response.getLists());
                        for (int i = 0; i < response.getLists().size(); i++) {
                            thanaNameResponse.add("" + response.getLists().get(i).getName());
                        }
                        binding.thana.setItem(thanaNameResponse);
                    });

        } catch (Exception e) {
        }
    }


    /**
     * for set district Name
     */
    private void getDistrictListByDivisionId(String selectedDivision) {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        try {
            millerProfileInfoViewModel.getDistrictListByDivisionId(getActivity(), selectedDivision)
                    .observe(getViewLifecycleOwner(), response -> {
                        progressDialog.dismiss();
                        districtListResponseList = new ArrayList<>();
                        districtListResponseList.clear();
                        districtNameList = new ArrayList<>();
                        districtNameList.clear();
                        districtListResponseList.addAll(response.getLists());

                        for (int i = 0; i < districtListResponseList.size(); i++) {
                            districtNameList.add("" + response.getLists().get(i).getName());
                        }
                        binding.district.setItem(districtNameList);
                    });
        } catch (Exception e) {
        }
    }

    private void validationAndSave() {
        if (selectedZone == null) {
            infoMessage(getActivity().getApplication(), "Please select zone");
            return;
        }
        if (selectedProcessType == null) {
            infoMessage(getActivity().getApplication(), "Please select process type");
            return;
        }
        if (binding.nameEt.getText().toString().isEmpty()) {
            binding.nameEt.setError("Empty Field");
            binding.nameEt.requestFocus();
            return;
        }
        if (binding.shortNameEt.getText().toString().isEmpty()) {
            binding.shortNameEt.setError("Empty Field");
            binding.shortNameEt.requestFocus();
            return;
        }
        if (selectedMillerTypeList.isEmpty()) {
            infoMessage(getActivity().getApplication(), "Please Select Your mill Type");
            return;
        }
        if (binding.capacity.getText().toString().isEmpty()) {
            binding.capacity.setError("Empty Field");
            binding.capacity.requestFocus();
            return;
        }
//        if (binding.remarks.getText().toString().isEmpty()) {
//            binding.remarks.setError("Empty Field");
//            binding.remarks.requestFocus();
//            return;
//        }

        if (selectedOwnerType == null) {
            infoMessage(getActivity().getApplication(), "Please select owner type");

            return;
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
            infoMessage(getActivity().getApplication(), "Please select  upazila/thana");

            return;
        }


        /**
         * for logo image
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
                    MultipartBody.Part.createFormData("profile_photo", file.getName(), requestFile);//here logoImage is name of from data
        } else {
            logoBody = null;
        }


        /**
         * now save all data
         */
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        millerProfileInfoViewModel.saveMillerProfileInfo(
                getActivity(), selectedZone, selectedProcessType, binding.nameEt.getText().toString(), selectedMillerTypeList,
                binding.capacity.getText().toString(), binding.millId.getText().toString(), binding.remarks.getText().toString(),
                "21", selectedOwnerType, selectedDivision, selectedDistrict, selectedThana, logoBody, "1",
                "1", binding.shortNameEt.getText().toString()).observe(getViewLifecycleOwner(), response -> {
            progressDialog.dismiss();
            if (response == null) {
                errorMessage(getActivity().getApplication(), "ERRROR");
                return;
            }
            if (response.getStatus() == 400) {
                infoMessage(getActivity().getApplication(), "" + response.getMessage());
                return;
            }
            successMessage(getActivity().getApplication(), "" + response.getMessage());
            Log.d("PROFILE_INFO", "" + response.getMessage());
            binding.saveBtn.setVisibility(View.GONE);
            viewPager.setCurrentItem(1);//go to snd tab
            AddNewMiller.isTabPermission = true;
            profileIdFromServer = response.getLast_id();
        });


    }

    private void setMillIdBasedOnZoneAndMillType() {
        String millType = "";
        if (!millerType1.isEmpty()) {
            millType = millerType1;
        }
        if (!millerType2.isEmpty()) {
            millType = millerType2;
        }

        try {
            if (millerType1.isEmpty() && millerType2.isEmpty()) {
                binding.millId.setText("");
                return;
            }
            if (!millerType1.isEmpty() && !millerType2.isEmpty()) {
                millType = "201";
            }

            if (countProfile.length() == 1) {
                countProfile = "00" + countProfile;
            }

            if (countProfile.length() == 2) {
                countProfile = "0" + countProfile;
            }

            millId = selectedZoneRemarks + "-" + millType + "-" + countProfile;
            binding.millId.setText(millId);
        } catch (Exception e) {
        }
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
            binding.logoImage.setImageDrawable(null);
            binding.logoImage.setImageBitmap(bitmap);


            /**
             * now set licenseImageName
             * */
            binding.logoimageName.setText(String.valueOf(new File("" + data.getData()).getName()));

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

    @Override
    public void millTypeId(int position, String milId, String remarks) {
        try {

            for (int i = 0; i < millTypeResponseList.size(); i++) {
                selectedMillerTypeList.add("" + milId);
                typeAllList.add("" + milId);
                if (milId.isEmpty()) {
                    selectedMillerTypeList.remove(position);
                    typeAllList.remove(position);

                }

                if (position == 0) {
                    millerType1 = remarks;

                }
                if (position == 1) {
                    millerType2 = remarks;
                }


            }

            setMillIdBasedOnZoneAndMillType();

            if (position == 0) {
                if (milId.isEmpty()) {
                    id1 = "";
                    return;
                }
                id1 = milId;
            }
            if (position == 1) {
                if (milId.isEmpty()) {
                    id2 = "";
                    return;
                }
                id2 = milId;
            }

        } catch (Exception e) {
        }
    }


}