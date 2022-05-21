package com.rupayan_housing.view.fragment.items;

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
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.rupayan_housing.R;

import com.rupayan_housing.adapter.AssignItemPacketAdapter;
import com.rupayan_housing.adapter.BrandListAdapter;
import com.rupayan_housing.adapter.CategoryItemListAdapter;
import com.rupayan_housing.adapter.ItemListAdapter;
import com.rupayan_housing.adapter.TrashListAadpter;


import com.rupayan_housing.databinding.FragmentItemListBinding;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.AssignPacketItemResponse;
import com.rupayan_housing.serverResponseModel.Brand;
import com.rupayan_housing.serverResponseModel.BrandList;
import com.rupayan_housing.serverResponseModel.BrandModel;
import com.rupayan_housing.serverResponseModel.Categories;
import com.rupayan_housing.serverResponseModel.CategoryList;
import com.rupayan_housing.serverResponseModel.CategoryListResponse;
import com.rupayan_housing.serverResponseModel.DuePaymentResponse;
import com.rupayan_housing.serverResponseModel.ItemListResponse;
import com.rupayan_housing.serverResponseModel.LoginResponse;
import com.rupayan_housing.serverResponseModel.Packet;
import com.rupayan_housing.serverResponseModel.Product;
import com.rupayan_housing.serverResponseModel.ProductList;
import com.rupayan_housing.serverResponseModel.TrashList;
import com.rupayan_housing.serverResponseModel.TrashResponse;
import com.rupayan_housing.utils.ImageBaseUrl;
import com.rupayan_housing.utils.InternetCheckerRecyclerBuddy;
import com.rupayan_housing.utils.ManagementUtils;
import com.rupayan_housing.utils.PathUtil;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.view.fragment.items.brand_edit.BrandEdit;
import com.rupayan_housing.viewModel.BrandViewModel;
import com.rupayan_housing.viewModel.CurrentPermissionViewModel;
import com.rupayan_housing.viewModel.ItemCategoryViewModel;
import com.rupayan_housing.viewModel.ItemListViewModel;
import com.rupayan_housing.viewModel.ItemsViewModel;
import com.rupayan_housing.viewModel.MonitoringDetailsViewModel;
import com.rupayan_housing.viewModel.TrashListViewModel;


import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import lombok.SneakyThrows;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ItemListFragment extends BaseFragment implements View.OnClickListener, ItemDelete, BrandEdit {
    private FragmentItemListBinding binding;
    private ItemListViewModel itemListViewModel;
    private ItemCategoryViewModel categoryListViewModel;
    private BrandViewModel brandViewModel;
    private TrashListViewModel trashListViewModel;
    private ItemsViewModel itemsViewModel;
    private MonitoringDetailsViewModel monitoringDetailsViewModel;
    private CurrentPermissionViewModel currentPermissionViewModel;
    String portion;
    private boolean forAddBrand = false;
    /**
     * For get Image
     */
    private static final int PICK_IMAGE = 200;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 300;
    private Uri imageUri;
    ImageView brandImage;
    String selectPacketId;

    /**
     * for pagination
     */
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    public static int pageNumber = 1, isFirstLoad = 0;
    private boolean increasePage = false;
    private List<Product> productList = new ArrayList<>();
    private List<BrandList> brandLists = new ArrayList<>();
    private List<CategoryList> categoryLists = new ArrayList<>();
    private List<TrashList> trashLists = new ArrayList<>();
    private List<ProductList> productLists = new ArrayList<>();

    private boolean click = false;
    private boolean endScroll = false;

    LinearLayoutManager linearLayoutManager;

    ProgressDialog progressDialog;

    String categoryTYpe, itemName, brandType; // here brand type will use to itemCategory
    List<Categories> categories;
    List<Brand> brands;
    String selectCategoryId, selectBrandID;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_item_list, container, false);
        itemsViewModel = new ViewModelProvider(this).get(ItemsViewModel.class);
        currentPermissionViewModel = new ViewModelProvider(this).get(CurrentPermissionViewModel.class);


        /**
         this object for viewModel
         * */
        itemListViewModel = new ViewModelProvider(this).get(ItemListViewModel.class);
        categoryListViewModel = new ViewModelProvider(this).get(ItemCategoryViewModel.class);
        brandViewModel = new ViewModelProvider(this).get(BrandViewModel.class);
        trashListViewModel = new ViewModelProvider(this).get(TrashListViewModel.class);
        brandViewModel = new ViewModelProvider(this).get(BrandViewModel.class);

        binding.toolbar.setClickHandle(() -> {
            pageNumber = 1;
            hideKeyboard(getActivity());
            getActivity().onBackPressed();

        });

        /** add btn click for Add new item this */
        binding.toolbar.addBtn.setOnClickListener(v -> {
//            if (getProfileTypeId(getActivity().getApplication()) != null) {
//                if (!getProfileTypeId(getActivity().getApplication()).equals("7")) {
//                    Toasty.info(getContext(), PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
//                    return;
//                }
//            }M
            if (portion.equals(ManagementUtils.brands)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getActivity()).getUserCredentials().getPermissions()).contains(1514)) {
                    addNewBrandDialog(getActivity(), "", "", "", "add");
                    return;
                } else {
                    Toasty.info(getContext(), PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }

            } else {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getActivity()).getUserCredentials().getPermissions()).contains(2)) {
                    Navigation.findNavController(getView()).navigate(R.id.itemListFragment_to_addNewItem);
                    return;
                } else {
                    Toasty.info(getContext(), PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }
            }
        });

        /** get text from ET field*/
        categoryTYpe = binding.categoryTypeEt.getText().toString();
        itemName = binding.itemNameEt.getText().toString();
        brandType = binding.brandTypeEt.getText().toString();

        /** click*/
        click();
        hideKeyboard(getActivity());

        /** for  pagination */
        binding.itemListRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) { //check for scroll down
                    visibleItemCount = linearLayoutManager.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();
                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            if (endScroll) {
                                return;
                            }
                            loading = false;
                            pageNumber += 1;
                            getAllListData();
                            loading = true;
                        }
                    }
                }
            }
        });




        binding.category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectCategoryId = categories.get(position).getCategoryID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.brand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectBrandID = brands.get(position).getBrandID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return binding.getRoot();
    }

    private void getAllListData() {
        if (!new InternetCheckerRecyclerBuddy(getActivity()).isInternetAvailableHere(binding.itemListRv, binding.noDataFound)) {
            return;
        }
        if (pageNumber == 1) {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.show();
        }

        if (pageNumber > 1) {
            binding.progress.setVisibility(View.VISIBLE);
            binding.progress.setProgress(20);
            binding.progress.setMax(100);
        }
        if (portion.equals(ManagementUtils.itemLists)) {
            /** visible toolbar Add btn*/
            binding.toolbar.addBtn.setVisibility(View.VISIBLE);
            /** get data from server*/
            getDataFromViewModel();
            binding.toolbar.toolbarTitle.setText("Item List");
        }
        if (portion.equals(ManagementUtils.itemCategory)) {
            binding.layout.setVisibility(View.GONE);
            binding.brandTypeEt.setHint("Search Here");
            binding.toolbar.toolbarTitle.setText(""+ManagementUtils.itemCategory);

            getCategoryDataFromViewMOdel();
        }
        if (portion.equals(ManagementUtils.brands)) {
            binding.toolbar.addBtn.setVisibility(View.VISIBLE);
            binding.toolbar.toolbarTitle.setText(""+ManagementUtils.brands);
            getBrandListFromViewModel();
        }
        if (portion.equals(ManagementUtils.trash)) {
            getTrashListFromViewMOdel();
            binding.toolbar.toolbarTitle.setText("Trash");
        }
        if (portion.equals(ManagementUtils.assignItemPacket)) {
        getItemPacketTag();
            binding.toolbar.toolbarTitle.setText("Assign Item Packet");
        }
    }

    private void getItemPacketTag() {
        trashListViewModel.getItemPacketList(getActivity(), String.valueOf(pageNumber),  binding.brandTypeEt.getText().toString(), selectBrandID, selectCategoryId).observe(getViewLifecycleOwner(), new Observer<AssignPacketItemResponse>() {
            @Override
            public void onChanged(AssignPacketItemResponse response) {
                progressDialog.dismiss();
                if (response == null) {
                    errorMessage(getActivity().getApplication(), "something wrong");
                    return;
                }
                if (response.getStatus() == 400) {
                    infoMessage(getActivity().getApplication(), response.getMessage());
                    return;
                }
                if (response != null) {
                    if (response.getProducts().isEmpty()) {
                        managePaginationAndFilter();
                    }


                    // add category data to spinner
                    List<String> categoryNameList = new ArrayList<>();
                    categories = new ArrayList<>();
                    List<String> brandNameList = new ArrayList<>();
                    brands = new ArrayList<>();
                    brands.addAll(response.getBrand());
                    categories.addAll(response.getCategory());

                    for (int i = 0; i < categories.size(); i++) {
                        if (selectCategoryId != null) {
                            if (selectCategoryId.equals(categories.get(i).getCategoryID())) {
                                binding.category.setSelection(i);
                            }
                        }
                        categoryNameList.add("" + categories.get(i).getCategory());
                    }

                    binding.category.setItem(categoryNameList);

                    // add brand data to spinner

                    for (int i = 0; i < brands.size(); i++) {
                        if (selectBrandID != null) {
                            if (selectBrandID.equals(brands.get(i).getBrandID())) {
                                binding.brand.setSelection(i);
                            }
                        }
                        brandNameList.add("" + brands.get(i).getBrandName());
                    }

                    binding.brand.setItem(brandNameList);

                    manageFilterBtnAndRvAndDataNotFound();

                    productLists.addAll(response.getProducts());
                    productLists.addAll(response.getProducts());
                    AssignItemPacketAdapter adapter = new AssignItemPacketAdapter(getActivity(), productLists, ItemListFragment.this);
                    linearLayoutManager = new LinearLayoutManager(getContext());
                    binding.itemListRv.setLayoutManager(linearLayoutManager);
                    binding.itemListRv.setHasFixedSize(true);
                    binding.itemListRv.setAdapter(adapter);


                }
            }
        });


    }
    private void getTrashListFromViewMOdel() {
        trashListViewModel.getTrashList(getActivity(), String.valueOf(pageNumber)).observe(getViewLifecycleOwner(), trashResponse -> {
            progressDialog.dismiss();
            if (trashResponse == null) {
                errorMessage(getActivity().getApplication(), "something wrong");
                return;
            }
            if (trashResponse.getStatus() == 400) {
                infoMessage(getActivity().getApplication(), trashResponse.getMessage());
                return;
            }
            if (trashResponse != null) {
                if (trashResponse.getLists().isEmpty()) {
                    binding.itemListRv.setVisibility(View.GONE);
                    binding.noDataFound.setVisibility(View.VISIBLE);
                    return;
                }
                setDataInRv(trashResponse.getLists());
            }
        });
    }

    private void setDataInRv(List<TrashList> lists) {
        trashLists.addAll(lists);
        TrashListAadpter adapter = new TrashListAadpter(getActivity(), trashLists, ItemListFragment.this);
        linearLayoutManager = new LinearLayoutManager(getContext());
        binding.itemListRv.setLayoutManager(linearLayoutManager);
        binding.itemListRv.setHasFixedSize(true);
        binding.itemListRv.setAdapter(adapter);
    }

    /**
     * now Set CategoryList data In RecyclerView
     */
    private void getCategoryDataFromViewMOdel() {
        try {
            categoryListViewModel.getCategoryList(getActivity(), String.valueOf(pageNumber), binding.brandTypeEt.getText().toString()).observe(getViewLifecycleOwner(), response -> {
                progressDialog.dismiss();
                binding.progress.setVisibility(View.GONE);
                if (response == null) {
                    errorMessage(getActivity().getApplication(), "something wrong");
                    return;
                }
                if (response.getStatus() == 400) {
                    infoMessage(getActivity().getApplication(), "" + response.getMessage());
                    return;
                }
                if (response.getStatus() == 200) {
                    if (response.getLists() == null || response.getLists().isEmpty()) {
                        managePaginationAndFilter();
                    }
                    /**
                     * now Set ItemList data In RecyclerView
                     * */
                    else {
                        /** if itemList isn't empty ,so than   filter btn will be visible   than set click Listener */
                        manageFilterBtnAndRvAndDataNotFound();
                        setCategoryDataInRv(response.getLists());
                    }
                }


            });

        } catch (Exception e) {
        }
    }


    /**
     * now Set CategoryList data In RecyclerView
     */
    private void setCategoryDataInRv(List<CategoryList> lists) {

        categoryLists.addAll(lists);
        CategoryItemListAdapter adapter = new CategoryItemListAdapter(getActivity(), categoryLists);
        linearLayoutManager = new LinearLayoutManager(getContext());
        binding.itemListRv.setLayoutManager(linearLayoutManager);
        binding.itemListRv.setHasFixedSize(true);
        binding.itemListRv.setAdapter(adapter);
    }

    private void getDataFromViewModel() {
        try {
            itemListViewModel.getItemlist(getActivity(), String.valueOf(pageNumber), binding.categoryTypeEt.getText().toString(), binding.itemNameEt.getText().toString(), binding.brandTypeEt.getText().toString()).observe(getViewLifecycleOwner(), new Observer<ItemListResponse>() {
                @Override
                public void onChanged(ItemListResponse response) {
                    progressDialog.dismiss();
                    binding.progress.setVisibility(View.GONE);
                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "something wrong");
                        return;
                    }
                    if (response.getStatus() == 400) {
                        infoMessage(getActivity().getApplication(), response.getMessage());
                        return;
                    }
                    if (response.getStatus() == 200) {
                        if (response.getProducts() == null || response.getProducts().isEmpty()) {
                            managePaginationAndFilter();
                        }
                        /**
                         * now Set ItemList data In RecyclerView
                         * */
                        else {
                            /** if itemList isn't empty ,so than filter btn will be visible   than set click Listener */
                            manageFilterBtnAndRvAndDataNotFound();
                            setItemListDataInRv(response.getProducts());
                        }
                    }

                }
            });

        } catch (Exception e) {
        }
    }

    private void getBrandListFromViewModel() {
        brandViewModel.getBrandList(getActivity(), String.valueOf(pageNumber)).observe(getViewLifecycleOwner(), brandModel -> {
            progressDialog.dismiss();
            binding.progress.setVisibility(View.GONE);
            try {
                if (brandModel == null) {
                    errorMessage(getActivity().getApplication(), "something wrong");
                    return;
                }
                if (brandModel.getStatus() == 400) {
                    infoMessage(getActivity().getApplication(), brandModel.getMessage());
                    return;
                }

                if (brandModel.getStatus() == 200) {
                    if (brandModel.getLists().isEmpty() || brandModel.getLists() == null) {
                        managePaginationAndFilter();
                    }
                    if (forAddBrand) {
                        brandLists.clear();
                    }
                    manageFilterBtnAndRvAndDataNotFound();

                    setdataInRv(brandModel.getLists());
                }
            } catch (Exception e) {
                Log.d("ERROR", e.getMessage());
            }
        });
    }

    private void setdataInRv(List<BrandList> lists) {
        brandLists.addAll(lists);
        BrandListAdapter adapter = new BrandListAdapter(getActivity(), brandLists, this);
        linearLayoutManager = new LinearLayoutManager(getContext());
        binding.itemListRv.setLayoutManager(linearLayoutManager);
        binding.itemListRv.setHasFixedSize(true);
        binding.itemListRv.setAdapter(adapter);


    }

    private void setItemListDataInRv(List<Product> products) {
        productList.addAll(products);
        ItemListAdapter adapter = new ItemListAdapter(getActivity(), productList);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.itemListRv.setLayoutManager(linearLayoutManager);
        binding.itemListRv.setVerticalScrollBarEnabled(true);
        binding.itemListRv.setHasFixedSize(true);
        binding.itemListRv.setAdapter(adapter);
    }


    private void getDataFromPreviousFragment() {
        assert getArguments() != null;
        portion = getArguments().getString("porson");
    }

    private void click() {
        binding.toolbar.filterBtn.setOnClickListener(this);
        binding.resetBtn.setOnClickListener(this);
        binding.searchBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.filterBtn:
                if (portion.equals(ManagementUtils.assignItemPacket)) {
                    binding.layout.setVisibility(View.GONE);
                    binding.layoutForAssignPacket.setVisibility(View.VISIBLE);
                    binding.brandTypeEt.setHint("Item Name");
                }
                if (binding.expandableView.isExpanded()) {
                    binding.expandableView.setExpanded(false);
                    return;
                } else {
                    binding.expandableView.setExpanded(true);
                }

                break;

            case R.id.searchBtn:
                isFirstLoad = 0;
                pageNumber = 1;
                productList.clear();
                productLists.clear();
                brandLists.clear();
                categoryLists.clear();
                getAllListData();
                break;

            case R.id.resetBtn:
                selectBrandID=null;
                selectCategoryId=null;
                binding.categoryTypeEt.setText("");
                binding.brandTypeEt.setText("");
                binding.itemNameEt.setText("");
                isFirstLoad = 0;
                pageNumber = 1;
                productList.clear();
                productLists.clear();
                brandLists.clear();
                categoryLists.clear();
                getAllListData();
//                if (nullChecked()) {
//                    binding.noDataFound.setVisibility(View.GONE);
//                    binding.itemListRv.setVisibility(View.VISIBLE);
//
//                }
                break;
        }
    }

    private boolean nullChecked() {
        if (categoryTYpe == null && itemName == null && brandType == null) {
            return true;
        }
        return false;
    }

    public void addNewBrandDialog(FragmentActivity context, String brandId, String brandNamee, String brandImagee, String from) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        @SuppressLint("InflateParams")
        View view = ((Activity) context).getLayoutInflater().inflate(R.layout.band_dialog_layout, null);
        //Set the view
        builder.setView(view);


        EditText brandName;
        brandName = view.findViewById(R.id.branNameEt1);
        brandImage = view.findViewById(R.id.imageView);
        Button bOk = view.findViewById(R.id.saveBtn1);
        Button cancel = view.findViewById(R.id.cancelBtn1);
        AlertDialog alertDialog = builder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (from.equals("forEdit")) {
            brandName.setText("" + brandNamee);
            brandImage.setImageBitmap(null);
            brandImage.setImageDrawable(null);
            try {
                Glide.with(context).load(ImageBaseUrl.image_base_url + brandImagee)
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .into(brandImage);

            } catch (NullPointerException e) {
                Log.d("ERROR", e.getMessage());
            }
        }

        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        brandImage.setOnClickListener(v -> {
            if (!(checkStoragePermission())) {
                requestStoragePermission(STORAGE_PERMISSION_REQUEST_CODE);
            } else {
                getLogoImageFromFile(getActivity().getApplication(), PICK_IMAGE);
            }
        });


        bOk.setOnClickListener(v -> {
            if (brandName.getText().toString().isEmpty()) {
                brandName.setError("Empty");
                brandName.requestFocus();
                return;
            }

            hideKeyboard(getActivity());

            if (!(isInternetOn(getActivity()))) {
                infoMessage(getActivity().getApplication(), "Please Check your Internet Connection");
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
                logoBody =
                        MultipartBody.Part.createFormData("distributor", file.getName(), requestFile);//here distributor is name of from data
            } else {
                logoBody = null;
            }
            if (from.equals("add")) {
                ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.show();
                brandViewModel.addNewBrand(getActivity(), logoBody, brandName.getText().toString())
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
                            alertDialog.dismiss();
                            successMessage(getActivity().getApplication(), "" + response.getMessage());
                            forAddBrand = true;
                            pageNumber = 1;
                            getBrandListFromViewModel();//for show current added data with current list
                        });

            }


        });
        cancel.setOnClickListener(v -> {
            alertDialog.dismiss();
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
            brandImage.setImageDrawable(null);
            brandImage.setImageBitmap(bitmap);


            /**
             * now set licenseImageName
             * */
            //  binding.imageName.setText(String.valueOf(new File("" + data.getData()).getName()));

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
    public void onStart() {
        super.onStart();
        productList.clear();
         brandLists.clear();
        categoryLists.clear();
        trashLists.clear();
        productLists.clear();;
        pageNumber = 1;
        isFirstLoad = 0;
        getDataFromPreviousFragment();
        getAllListData();

        try {
            /**
             * For update current permission and token
             */
            updateCurrentUserPermission(getActivity());
        } catch (Exception e) {
            infoMessage(getActivity().getApplication(), "" + e.getMessage());
        }
    }

    private void managePaginationAndFilter() {
        if (isFirstLoad == 0) { // if filter time list is null.  so then, data_not_found will be visible
            binding.itemListRv.setVisibility(View.GONE);
            binding.noDataFound.setVisibility(View.VISIBLE);
            return;
        }
        if (isFirstLoad > 0) {//for scrolling off
            endScroll = true;//means scroll off
            pageNumber -= 1;
            return;
        }
        return;
    }

    private void manageFilterBtnAndRvAndDataNotFound() {


        isFirstLoad += 1;
        binding.toolbar.filterBtn.setVisibility(View.VISIBLE);
        if (portion.equals(ManagementUtils.brands)) {
            binding.toolbar.filterBtn.setVisibility(View.GONE);
        }
        //for filter
        // sometime filter list data came null when, data_not_found have visible,
        // And again search comes data in list by the others filter parameter.that for recycler view visible
        binding.noDataFound.setVisibility(View.GONE);
        binding.itemListRv.setVisibility(View.VISIBLE);
    }


    private void updateCurrentUserPermission(FragmentActivity activity) {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }
        currentPermissionViewModel.getCurrentUserRealtimePermissions(
                PreferenceManager.getInstance(activity).getUserCredentials().getToken(),
                PreferenceManager.getInstance(activity).getUserCredentials().getUserId()
        ).observe(getViewLifecycleOwner(), response -> {
            if (response == null) {
                Toasty.error(activity, "Something Wrong", Toasty.LENGTH_LONG).show();
                return;
            }
            if (response.getStatus() == 400) {
                Toasty.info(activity, "" + response.getMessage(), Toasty.LENGTH_LONG).show();
            }
            try {
                LoginResponse loginResponse = PreferenceManager.getInstance(activity).getUserCredentials();
                if (loginResponse != null) {
                    loginResponse.setPermissions(response.getMessage());
                    loginResponse.setToken(response.getToken());
                    PreferenceManager.getInstance(activity).saveUserCredentials(loginResponse);
                }
            } catch (Exception e) {
                infoMessage(getActivity().getApplication(), "" + e.getMessage());
                Log.d("ERROR", "" + e.getMessage());
            }
        });
    }

    /**
     * For delete item trash list items
     */
    @Override
    public void delete(String vendorId, String storeId, String productId) {
        hideKeyboard(getActivity());
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please check your internet connection");
            return;
        }
        itemsViewModel.itemDeleteFromItemTrashList(getActivity(), vendorId, storeId, productId)
                .observe(getViewLifecycleOwner(), response -> {
                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "Something Wrong");
                        return;
                    }
                    if (response.getStatus() == 400) {
                        infoMessage(getActivity().getApplication(), "" + response.getMessage());
                        return;
                    }
                    successMessage(getActivity().getApplication(), "" + response.getMessage());
                    getTrashListFromViewMOdel();
                    binding.itemListRv.getAdapter().notifyDataSetChanged();
                });
    }

    @Override
    public void addTag(Integer position, String categoryId, String productId, String packetId, String productTitl) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        @SuppressLint("InflateParams")
        View view = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.add_packet_dialog_layout, null);
        //Set the view
        builder.setView(view);
        TextView itemNameTv;

        SmartMaterialSpinner itemTagList = view.findViewById(R.id.itemTagList);
        itemNameTv = view.findViewById(R.id.itemNameTv);
        Button bOk = view.findViewById(R.id.btn_ok);
        Button cancel = view.findViewById(R.id.cancel);
        android.app.AlertDialog alertDialog = builder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);

        List<String> productTitle = new ArrayList<>();
        List<Packet> packets = new ArrayList<>();

        trashListViewModel.getDropDoenPacketItem(getActivity(), productId).observe(getViewLifecycleOwner(), response -> {
            if (response != null && response.getStatus() == 200) {
                packets.addAll(response.getPacketList());
                for (int i = 0; i < response.getPacketList().size(); i++) {
                    if (packetId.equals(response.getPacketList().get(i).getProductID())) {
                        itemTagList.setSelection(i);
                    }
                    productTitle.add("" + response.getPacketList().get(i).getProductTitle());
                }
                itemTagList.setItem(productTitle);

            }
        }
        );
        itemNameTv.setText("Item Name : " + productTitl);// set product title

// select packet item
        itemTagList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectPacketId = packets.get(position).getProductID();
               // Toast.makeText(getContext(), "" + selectPacketId, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        cancel.setOnClickListener(v -> alertDialog.dismiss());//for cancel
        bOk.setOnClickListener(v -> {
            if (!selectPacketId.equals(packetId)) {
                updateTag(productId, selectPacketId);

            }

            alertDialog.dismiss();

        });

        alertDialog.show();

    }

    @Override
    public void deleteTag(Integer position, String productId, String packetId) {
        updateTag(productId, "0");
    }
    private void updateTag(String productId, String selectPacketId) {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check your Internet Connection");
            return;
        }
        itemsViewModel.saveAddTagResponse(getActivity(), productId, selectPacketId).observe(getViewLifecycleOwner(), new Observer<DuePaymentResponse>() {
            @Override
            public void onChanged(DuePaymentResponse response) {
                try {
                    if (response != null && response.getStatus() == 200) {
                        successMessage(getActivity().getApplication(), response.getMessage());
                        productLists.clear();
                        pageNumber = 1;
                        getItemPacketTag();
                    }
                } catch (Exception e) {
                }

            }
        });

    }

    @Override
    public void getData(int position, String brandSlId, String brandName, String image) {
        //addNewBrandDialog(getActivity(), brandSlId, brandName, image, "forEdit");

        editBrand(brandSlId, brandName, image);
    }

    private void editBrand(String brandSlId, String brandNamee, String image) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        @SuppressLint("InflateParams")
        View view = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.band_dialog_layout, null);
        //Set the view
        builder.setView(view);
        EditText brandName;
        brandName = view.findViewById(R.id.branNameEt1);
        brandImage = view.findViewById(R.id.imageView);
        Button bOk = view.findViewById(R.id.saveBtn1);
        Button cancel = view.findViewById(R.id.cancelBtn1);
        AlertDialog alertDialog = builder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        brandName.setText("" + brandNamee);
        brandImage.setImageBitmap(null);
        brandImage.setImageDrawable(null);

        try {
            Glide.with(getContext()).load(ImageBaseUrl.image_base_url + image)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(brandImage);

        } catch (NullPointerException e) {
            Log.d("ERROR", e.getMessage());
        }
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        brandImage.setOnClickListener(v -> {
            if (!(checkStoragePermission())) {
                requestStoragePermission(STORAGE_PERMISSION_REQUEST_CODE);
            } else {
                getLogoImageFromFile(getActivity().getApplication(), PICK_IMAGE);
            }
        });


        bOk.setOnClickListener(v -> {
            if (brandName.getText().toString().isEmpty()) {
                brandName.setError("Empty");
                brandName.requestFocus();
                return;
            }

            hideKeyboard(getActivity());

            if (!(isInternetOn(getActivity()))) {
                infoMessage(getActivity().getApplication(), "Please Check your Internet Connection");
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
                logoBody =
                        MultipartBody.Part.createFormData("distributor", file.getName(), requestFile);//here distributor is name of from data
            } else {
                logoBody = null;
            }

            ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.show();
            brandViewModel.editBrand(getActivity(), logoBody, brandName.getText().toString(), brandSlId).observe(getViewLifecycleOwner(), new Observer<DuePaymentResponse>() {
                @Override
                public void onChanged(DuePaymentResponse response) {
                    progressDialog.dismiss();
                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "Something Wrong");
                        return;
                    }
                    if (response.getStatus() == 400) {
                        infoMessage(getActivity().getApplication(), "" + response.getMessage());
                        return;
                    }
                    alertDialog.dismiss();
                    successMessage(getActivity().getApplication(), "" + response.getMessage());
                    forAddBrand = true;
                    pageNumber = 1;
                    getBrandListFromViewModel();

                }
            });

        });
        cancel.setOnClickListener(v -> {
            alertDialog.dismiss();
        });
        alertDialog.show();
    }

    @Override
    public void delete(int position, String brandSlId) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());

        @SuppressLint("InflateParams")
        View view = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.purchase_dialog, null);
        //Set the view
        builder.setView(view);
        TextView tvTitle, tvMessage;
        ImageView imageIcon = view.findViewById(R.id.img_icon);
        tvMessage = view.findViewById(R.id.tv_message);
        tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("Do You Want to delete brand ?");//set warning title
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
            brandViewModel.deleteBrand(getActivity(), brandSlId).observe(getViewLifecycleOwner(), new Observer<DuePaymentResponse>() {
                @Override
                public void onChanged(DuePaymentResponse response) {
                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "Something Wrong");
                        return;
                    }
                    if (response.getStatus() == 400) {
                        infoMessage(getActivity().getApplication(), "" + response.getMessage());
                        return;
                    }
                    successMessage(getActivity().getApplication(), "" + response.getMessage());
                    forAddBrand = true;
                    getBrandListFromViewModel();
                    binding.itemListRv.getAdapter().notifyDataSetChanged();
                }
            });

        });

        alertDialog.show();

    }
}