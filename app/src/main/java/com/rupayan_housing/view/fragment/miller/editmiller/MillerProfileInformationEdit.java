package com.rupayan_housing.view.fragment.miller.editmiller;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.rupayan_housing.R;
import com.rupayan_housing.adapter.EditMillerTypeAdapter;
import com.rupayan_housing.adapter.MillerTypeAdapter;
import com.rupayan_housing.clickHandle.EditMillerProfileInformationEditClickHandle;
import com.rupayan_housing.databinding.FragmentMillerProfileinformationEditBinding;
import com.rupayan_housing.retrofit.GetMillTypeId;
import com.rupayan_housing.serverResponseModel.DistrictListResponse;
import com.rupayan_housing.serverResponseModel.DivisionResponse;
import com.rupayan_housing.serverResponseModel.DuePaymentResponse;
import com.rupayan_housing.serverResponseModel.GetPreviousMillerInfoResponse;
import com.rupayan_housing.serverResponseModel.MillByZoneIdResponse;
import com.rupayan_housing.serverResponseModel.MillTypeResponse;
import com.rupayan_housing.serverResponseModel.OwnerTypeResponse;
import com.rupayan_housing.serverResponseModel.ProcessTypeResponse;
import com.rupayan_housing.serverResponseModel.ThanaList;
import com.rupayan_housing.serverResponseModel.ZoneResponse;
import com.rupayan_housing.utils.PathUtil;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.view.fragment.miller.MillerDetailsViewFragment;
import com.rupayan_housing.viewModel.MillerProfileInfoViewModel;
import com.rupayan_housing.viewModel.UpdateMillerViewModel;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import lombok.SneakyThrows;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MillerProfileInformationEdit extends BaseFragment implements GetMillTypeId {
    private FragmentMillerProfileinformationEditBinding binding;
    private UpdateMillerViewModel updateMillerViewModel;

    private ViewPager viewPager;

    private static final int PICK_IMAGE = 200;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 300;
    private Uri imageUri;
    private MillerProfileInfoViewModel millerProfileInfoViewModel;
    public static List<MillTypeResponse> millTypeResponses;
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
    private List<MillTypeResponse> millTypeResponseList;
    Set<String> millRemarks = new HashSet<>();//for store miller type id

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


    public static GetPreviousMillerInfoResponse previousGetPreviousMillerInfoResponse;//for store previous miller information


    private String selectedProcessType, selectedOwnerType, selectedDivision, selectedDistrict, selectedThana;
    private String millerType1;
    private String millerType2;

    private String selectedZoneRemarks, countProfile;
    public static String selectedZone, previousZoneId;
    private String millIdForReselectZone; // jodi user zone change kore abar previous zone rakhte chay tahole ager mill id ta save hobe

    //private Set<String> selectedMillerTypeList = new HashSet<>();//for store miller type id


    private String slid;
    private String millId;
    public static String id1, id2;
    List<String> previousMilTypeId;
    int listSize; // fro mill id handle

    String onlyZone, onlyProfile;

    public MillerProfileInformationEdit(String slId) {
        this.slid = slId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_miller_profileinformation_edit, container, false);
        millerProfileInfoViewModel = new ViewModelProvider(this).get(MillerProfileInfoViewModel.class);
        updateMillerViewModel = new ViewModelProvider(this).get(UpdateMillerViewModel.class);
        viewPager = getActivity().findViewById(R.id.viewPager);
        // selectedMillerTypeList = new HashSet<>();

        try {
            getPreviousMillerInformationBySid();
        } catch (Exception e) {
        }

        binding.setClickHandle(new EditMillerProfileInformationEditClickHandle() {
            @Override
            public void save() {
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
//               if (millId == null || millId.isEmpty()) {
//                   infoMessage(getActivity().getApplication(), "Please Select Your mill Type");
//                     return;
//             }


                if (binding.millId.getText().toString().isEmpty()) {
                    infoMessage(getActivity().getApplication(), "Please Select Your mill Type");
                    return;
                }
                if (binding.capacity.getText().toString().isEmpty()) {
                    binding.capacity.setError("Empty Field");
                    binding.capacity.requestFocus();
                    return;
                }
             /*   if (binding.remarks.getText().toString().isEmpty()) {
                    binding.remarks.setError("Empty Field");
                    binding.remarks.requestFocus();
                    return;
                }
*/
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
                    infoMessage(getActivity().getApplication(), "Please select upazila/thana");
                    return;
                }


                dialog();
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


        return binding.getRoot();
    }

    private void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        @SuppressLint("InflateParams")
        View view = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.purchase_dialog, null);
        //Set the view
        builder.setView(view);
        TextView tvTitle, tvMessage;
        ImageView imageIcon = view.findViewById(R.id.img_icon);
        tvMessage = view.findViewById(R.id.tv_message);
        tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("Do You Want to Update it ?");//set warning title
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
            validationAndSave();
        });
        alertDialog.show();
    }


    private void getPageInfoFromServer() {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check your internet Connection");
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        try {
            millerProfileInfoViewModel.getProfileInfoResponse(getActivity())
                    .observe(getViewLifecycleOwner(), response -> {
                        try {
                            progressDialog.dismiss();
                            if (response == null) {
                                errorMessage(getActivity().getApplication(), "Something Wrong");
                                return;
                            } if (response.getStatus() == 400) {
                                infoMessage(getActivity().getApplication(), "" + response.getMessage());
                                return;
                            }
                            /**
                             * for zone
                             */
                            zoneResponseList = new ArrayList<>();
                            zoneResponseList.clear();
                            zoneList = new ArrayList<>();
                            zoneList.clear();
                            zoneResponseList.addAll(response.getZones());

                            for (int i = 0; i < response.getZones().size(); i++) {
                                zoneList.add(response.getZones().get(i).getZoneName());
                            }
                            binding.zone.setItem(zoneList);

                            /**
                             * now set select Previous selected zone
                             */
                            for (int i = 0; i < zoneResponseList.size(); i++) {
                                if (zoneResponseList.get(i).getZoneID().equals(previousGetPreviousMillerInfoResponse.getProfileInfo().getZoneID())) {
                                    binding.zone.setSelection(i);
                                    break;
                                }
                            }
                            /**
                             * for process type
                             */
                            processTypeResponseList = new ArrayList<>();
                            processTypeResponseList.clear();
                            processTypeNameList = new ArrayList<>();
                            processTypeNameList.clear();
                            processTypeResponseList.addAll(response.getProcessTypes());

                            for (int i = 0; i < response.getProcessTypes().size(); i++) {
                                processTypeNameList.add("" + response.getProcessTypes().get(i).getProcessTypeName());
                            }
                            binding.processType.setItem(processTypeNameList);

                            /**
                             * now set previous selected process type
                             */
                            for (int i = 0; i < processTypeResponseList.size(); i++) {
                                if (processTypeResponseList.get(i).getProcessTypeID().equals(previousGetPreviousMillerInfoResponse.getProfileInfo().getProcessTypeID())) {
                                    binding.processType.setSelection(i);
                                    break;
                                }
                            }
                            /**
                             * now set Previous selected name and others more
                             */
                            try {
                                binding.shortNameEt.setText(previousGetPreviousMillerInfoResponse.getProfileInfo().getDisplayName());
                                // binding.millType1//will set mill type
                                binding.millId.setText(previousGetPreviousMillerInfoResponse.getProfileInfo().getMillID());
                                onlyZone = getZone(previousGetPreviousMillerInfoResponse.getProfileInfo().getMillID());
                                onlyProfile = getProfileId(previousGetPreviousMillerInfoResponse.getProfileInfo().getMillID());
                                //Toast.makeText(getContext(), "onlyZone : " + onlyZone + " \n onlyProfile: " + onlyProfile, Toast.LENGTH_SHORT).show();
                                binding.capacity.setText(previousGetPreviousMillerInfoResponse.getProfileInfo().getCapacity());
                                binding.remarks.setText(previousGetPreviousMillerInfoResponse.getProfileInfo().getRemarks());
                                binding.nameEt.setText(previousGetPreviousMillerInfoResponse.getProfileInfo().getFullName());
                            } catch (Exception e) {
                            }

                            previousMilTypeId = new ArrayList<>();
                            for (int i = 0; i < previousGetPreviousMillerInfoResponse.getProfileInfo().getMillTypeIDs().size(); i++) {
                                previousMilTypeId.add(previousGetPreviousMillerInfoResponse.getProfileInfo().getMillTypeIDs().get(i));
                            }
                            listSize = previousMilTypeId.size();
                            /**
                             * now set logo image
                             */
                            Glide.with(getContext())
                                    .load(previousGetPreviousMillerInfoResponse.getProfileInfo().getProfilePhoto())
                                    .centerCrop()
                                    .placeholder(R.drawable.erro_logo)
                                    .error(R.drawable.erro_logo)
                                    .into(binding.logoImage);

                            /**
                             * now set millType
                             */

                            millTypeResponseList = new ArrayList<>();
                            millTypeResponseList.clear();
                            millTypeResponseList.addAll(response.getMillTypes());
                            try {
                            /* binding.millType1.setTag(response.getMillTypes().get(0).getMillTypeName());
                            binding.millType2.setTag(response.getMillTypes().get(1).getMillTypeName());
                            binding.millType3.setTag(response.getMillTypes().get(2).getMillTypeName());
                            for (int i = 0; i < previousGetPreviousMillerInfoResponse.getProfileInfo().getMillTypeIDs().size(); i++) {
                                for (int i1 = 0; i1 < response.getMillTypes().size(); i1++) {
                                    if (previousGetPreviousMillerInfoResponse.getProfileInfo().getMillTypeIDs().get(i).equals(response.getMillTypes().get(i1).getMillTypeID())) {
                                        if (i1 == 0) {
                                            binding.millType1.setChecked(true);
                                        }
                                        if (i1 == 1) {
                                            binding.millType2.setChecked(true);
                                        }
                                        if (i1 == 2) {
                                            binding.millType3.setChecked(true);
                                        }
                                    }
                                }
                            }
  */
                                millTypeResponses = new ArrayList<>();
                                millTypeResponses.addAll(response.getMillTypes());
                                try {
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                                    binding.millTypeRv.setLayoutManager(layoutManager);
                                    EditMillerTypeAdapter adapter = new EditMillerTypeAdapter(getContext(), millTypeResponses, this);
                                    binding.millTypeRv.setAdapter(adapter);

                                } catch (Exception e) {
                                }

                            } catch (Exception e) {
                            }


                            /**
                             * now set Type of owner
                             */
                            ownerTypeResponseList = new ArrayList<>();
                            ownerTypeResponseList.clear();
                            ownerTypeResponseList.addAll(response.getOwnerTypes());
                            ownerTypeNameList = new ArrayList<>();
                            ownerTypeNameList.clear();
                            for (int i = 0; i < response.getOwnerTypes().size(); i++) {
                                ownerTypeNameList.add("" + response.getOwnerTypes().get(i).getOwnerTypeName());
                            }
                            binding.ownerType.setItem(ownerTypeNameList);

                            /**
                             * Now set previous selected owner type
                             */
                            for (int i = 0; i < ownerTypeResponseList.size(); i++) {
                                if (ownerTypeResponseList.get(i).getOwnerTypeID().equals(previousGetPreviousMillerInfoResponse.getProfileInfo().getOwnerTypeID())) {
                                    binding.ownerType.setSelection(i);
                                    break;
                                }
                            }


                            /**
                             * now set division
                             */
                            divisionResponseList = new ArrayList<>();
                            divisionResponseList.clear();
                            divisionNameList = new ArrayList<>();
                            divisionResponseList.clear();
                            divisionResponseList.addAll(response.getDivisions());
                            for (int i = 0; i < response.getDivisions().size(); i++) {
                                divisionNameList.add("" + response.getDivisions().get(i).getName());
                            }
                            binding.division.setItem(divisionNameList);

                            /**
                             * Now set Previous selected division
                             */
                            for (int i = 0; i < divisionResponseList.size(); i++) {
                                if (divisionResponseList.get(i).getDivisionId().equals(previousGetPreviousMillerInfoResponse.getProfileInfo().getDivisionID())) {
                                    binding.division.setSelection(i);
                                    selectedDivision = previousGetPreviousMillerInfoResponse.getProfileInfo().getDivisionID();//set Previous selected Division
                                    break;
                                }
                            }
                            /**
                             * Now set Previous selected district by previous selected division id
                             */

                            getDistrictListByDivisionId(selectedDivision);


                            /**
                             * set count profile
                             */
                            countProfile = response.getCountProfile();
                        } catch (Exception e) {
                        }

                    });

        } catch (Exception e) {
        }
        binding.zone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if (MillerDetailsViewFragment.isSubmit != null) {
                        if (MillerDetailsViewFragment.isSubmit.equals("1")) {
                           // infoMessage(getActivity().getApplication(), "Zone already submitted");
                            binding.zone.setClickable(false);
                            return;
                        }
                    }

                    selectedZone = zoneResponseList.get(position).getZoneID();
                    selectedZoneRemarks = zoneResponseList.get(position).getRemarks();

                    // jodi user zone and milltype change korar pore abaro ager zone and milltype select kore tahole mill id ager ta thakbe
                    if (previousZoneId.equals(selectedZone) && previousMilTypeId.size() == listSize) {
                        binding.millId.setText("" + millIdForReselectZone);
                        return;
                    }
                    totalMillByZone(selectedZone);

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

    private void setMillIdBasedOnZoneAndMillType() {

        if (countProfile.length() == 1) {
            countProfile = "00" + countProfile;
        }
        if (countProfile.length() == 2) {
            countProfile = "0" + countProfile;
        }

        if (previousGetPreviousMillerInfoResponse.getProfileInfo().getMillID().length() == 10) {
            nowSetMillId();
        }

        try {
         /*   String millType = "";
            if (!millerType1.isEmpty()) {
                millType = millerType1;
            }
            if (!millerType2.isEmpty()) {
                millType = millerType2;
            }
            if (!millerType1.isEmpty() && !millerType2.isEmpty()) {
                millType = "201";
            }



            millId = selectedZoneRemarks + "-" + millType + "-" + countProfile;

            if (millId == null || millId.isEmpty()) {
                millId = previousGetPreviousMillerInfoResponse.getProfileInfo().getMillID();
            }
            if (millerType1.isEmpty() && millerType2.isEmpty()) {
                binding.millId.setText("");
                return;
            }
            binding.millId.setText("" + millId);*/

        } catch (Exception e) {
        }
    }

    private void nowSetMillId() {
        // note : self logic , others developer  should know, about mill profile edit process
        String newMillId;
        if (previousMilTypeId.size() == 2) {
             if (previousMilTypeId.get(1).equals("3")) {
                if (getMainMillId(previousGetPreviousMillerInfoResponse.getProfileInfo().getMillID()).equals("101")) {
                    newMillId = selectedZoneRemarks + "-" + "201" + "-" + countProfile;// here 201 means industrial and edible selected
                    binding.millId.setText("" + newMillId);
                    return;
                }
                if (getMainMillId(previousGetPreviousMillerInfoResponse.getProfileInfo().getMillID()).equals("201")) {
                    newMillId = selectedZoneRemarks + "-" + "201" + "-" + countProfile;// here 201 means industrial and edible selected
                    binding.millId.setText("" + newMillId);
                    return;
                }
            }

        }
        if (previousMilTypeId.size() == 1) {
             if (previousMilTypeId.get(0).equals("2")) {
                if (getMainMillId(previousGetPreviousMillerInfoResponse.getProfileInfo().getMillID()).equals("101")) {
                    newMillId = selectedZoneRemarks + "-" + "101" + "-" + countProfile;// here 201 means industrial and edible selected
                    binding.millId.setText("" + newMillId);
                    return;
                } if (getMainMillId(previousGetPreviousMillerInfoResponse.getProfileInfo().getMillID()).equals("201")) {
                    newMillId = selectedZoneRemarks + "-" + "101" + "-" + countProfile;// here 201 means industrial and edible selected
                    binding.millId.setText("" + newMillId);
                    return;
                }
            }

        }


    }

    /**
     * for set district Name
     */
    private void getDistrictListByDivisionId(String selectedDivision) {
        millerProfileInfoViewModel.getDistrictListByDivisionId(getActivity(), selectedDivision)
                .observe(getViewLifecycleOwner(), response -> {
                    districtListResponseList = new ArrayList<>();
                    districtListResponseList.clear();
                    districtNameList = new ArrayList<>();
                    districtNameList.clear();
                    districtListResponseList.addAll(response.getLists());

                    for (int i = 0; i < districtListResponseList.size(); i++) {
                        districtNameList.add(response.getLists().get(i).getName());
                    }
                    binding.district.setItem(districtNameList);


                    if (previousGetPreviousMillerInfoResponse != null) {
                        for (int i = 0; i < districtListResponseList.size(); i++) {
                            if (districtListResponseList.get(i).getDistrictId().equals(previousGetPreviousMillerInfoResponse.getProfileInfo().getDistrictID())) {
                                binding.district.setSelection(i);
                                selectedDistrict = districtListResponseList.get(i).getDistrictId();//set previous selected district
                                getThanaListByDistrictId(selectedDistrict);
                                break;
                            }
                        }
                    }


                });
    }


    /**
     * for set thana Name
     */
    private void getThanaListByDistrictId(String selectedDistrict) {
        millerProfileInfoViewModel.getThanaListByDistrictId(getActivity(), selectedDistrict)
                .observe(getViewLifecycleOwner(), response -> {
                    thanaListsResponse = new ArrayList<>();
                    thanaListsResponse.clear();
                    thanaNameResponse = new ArrayList<>();
                    thanaNameResponse.clear();
                    thanaListsResponse.addAll(response.getLists());
                    for (int i = 0; i < response.getLists().size(); i++) {
                        thanaNameResponse.add(response.getLists().get(i).getName());
                    }
                    binding.thana.setItem(thanaNameResponse);

                    if (previousGetPreviousMillerInfoResponse != null) {
                        for (int i = 0; i < thanaListsResponse.size(); i++) {
                            if (thanaListsResponse.get(i).getUpazilaId().equals(previousGetPreviousMillerInfoResponse.getProfileInfo().getUpazilaID())) {
                                selectedThana = thanaListsResponse.get(i).getUpazilaId();
                                binding.thana.setSelection(i);
                                break;
                            }
                        }
                    }
                });

    }


    public void getPreviousMillerInformationBySid() {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check your Internet Connection");
            return;
        }
        updateMillerViewModel.getPreviousMillerInfoBySid(getActivity(), slid)
                .observe(getViewLifecycleOwner(), response -> {
                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "Something Wrong");
                        return;
                    }
                    if (response.getStatus() == 400) {
                        infoMessage(requireActivity().getApplication(), "" + response.getMessage());
                        return;
                    }
                    previousGetPreviousMillerInfoResponse = response;
                    if (selectedZone == null) {
                        previousZoneId = response.getProfileInfo().getZoneID();
                        selectedZone = previousZoneId;
                    }

                    // binding.millId.setText(""+response.getProfileInfo().getMillID());


                    millIdForReselectZone = response.getProfileInfo().getMillID();
                    getPageInfoFromServer();
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
            binding.logoImage.setImageDrawable(null);
            binding.logoImage.setImageDrawable(null);
            binding.logoImage.destroyDrawingCache();
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


    private void validationAndSave() {

        /**
         * for logo image
         */

        MultipartBody.Part logoBody;
        if (imageUri != null) {//logo image not mandatory here so if user not select any logo image by default it send null
            File file = null;
            try {
                file = new File(Objects.requireNonNull(PathUtil.getPath(getActivity(), imageUri)));
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


       /* if (selectedMillerTypeList.isEmpty() || selectedMillerTypeList == null) {
            selectedMillerTypeList.addAll();
            // add previous value if doesn't change mill type
        }
        selectedMillerTypeList.removeAll(Arrays.asList("", null));
*/
        if (!isInternetOn(getActivity())) {
            infoMessage(getActivity().getApplication(), "Please check your internet connection");
            return;
        }
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        updateMillerViewModel.submitUpdateMillerProfileInformation(
                getActivity(), binding.remarks.getText().toString(), binding.capacity.getText().toString(), selectedZone, selectedOwnerType, selectedProcessType, binding.nameEt.getText().toString(),
                previousMilTypeId, binding.millId.getText().toString(), selectedDivision, selectedDistrict, selectedThana,
                slid, previousGetPreviousMillerInfoResponse.getProfileInfo().getProfileID(), logoBody,
                previousGetPreviousMillerInfoResponse.getProfileInfo().getProfileDetailsId(),
                previousGetPreviousMillerInfoResponse.getProfileInfo().getRefSl(), previousGetPreviousMillerInfoResponse.getProfileInfo().getIsSubmit(),
                previousGetPreviousMillerInfoResponse.getProfileInfo().getAssociationID(), binding.shortNameEt.getText().toString()).observe(getViewLifecycleOwner(), response -> {
            progressDialog.dismiss();
            try {
                if (response == null) {
                    errorMessage(getActivity().getApplication(), "Something Wrong");
                    return;
                }
                if (response.getStatus() == 400) {
                    infoMessage(getActivity().getApplication(), "" + response.getMessage());
                    Log.d("CURRENT_RESPONSE", "" + response.getMessage());
                    return;
                }
                successMessage(getActivity().getApplication(), "" + response.getMessage());
                Log.d("RESPONSE", response.getMessage());
                viewPager.setCurrentItem(1);//go to snd tab
            } catch (Exception e) {
                Log.e("Error", e.getMessage());

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

    @Override
    public void millTypeId(int position, String milId, String remarks) {
        try {
            if (position == 1) {

                if (milId.isEmpty()) {
                    previousMilTypeId.remove(1);

                }
                if (!milId.isEmpty()) {
                    previousMilTypeId.add(milId);
                }


                if (previousMilTypeId.size() == 2) {
                    if (previousMilTypeId.get(1).equals("3")) {
                        setMill("201");
                        return;
                    }
                }

                if (previousMilTypeId.size() == 1) {
                    if (previousMilTypeId.get(0).equals("2")) {
                        setMill("101");
                        return;
                    }
                }


                // setMillIdBasedOnZoneAndMillType();

            }

        } catch (Exception e) {
        }
    }

    public void setMill(String s) {
        String newMillId = "";
        binding.millId.setText("");
        if (!selectedZone.equals(previousZoneId)) {
            if (s.equals("201")){
                newMillId = selectedZoneRemarks + "-" + s + "-" + countProfile;
                binding.millId.setText("" + newMillId);
                return;
            }

        }

        if (!selectedZone.equals(previousZoneId)) {
            if (s.equals("101")){
                newMillId = selectedZoneRemarks + "-" + s + "-" + countProfile;
                binding.millId.setText("" + newMillId);
                return;
            }

        }
        newMillId = onlyZone + "-" + s + "-" + onlyProfile;
      //  Toast.makeText(getContext(), "" + newMillId, Toast.LENGTH_SHORT).show();
        binding.millId.setText("" + newMillId);
    }

    public static String getMainMillId(String millId) {
        char char1 = millId.charAt(3);
        char char2 = millId.charAt(4);
        char char3 = millId.charAt(5);
        String newMillId = "" + char1 + char2 + char3;
        return newMillId;
    }

    public static String getZone(String millId) {
        char char1 = millId.charAt(0);
        char char2 = millId.charAt(1);
        String zoneRemarks = "" + char1 + char2;
        return zoneRemarks;
    }

    public static String getProfileId(String millId) {
        char char1 = millId.charAt(7);
        char char2 = millId.charAt(8);
        char char3 = millId.charAt(9);
        String profile = "" + char1 + char2 + char3;
        return profile;
    }
}