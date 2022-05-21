package com.rupayan_housing.view.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.Task;
import com.rupayan_housing.R;
import com.rupayan_housing.adapter.LicenceCheckAdapter;
import com.rupayan_housing.currentDate.CurrentDate;
import com.rupayan_housing.dialog.MyApplication;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.notification.NotificationUtil;
import com.rupayan_housing.serverResponseModel.LicenceExpire;
import com.rupayan_housing.serverResponseModel.LoginResponse;
import com.rupayan_housing.utils.CrispUtil;
import com.rupayan_housing.utils.HomeUtils;
import com.rupayan_housing.utils.MillerUtils;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.utils.ReportUtils;
import com.rupayan_housing.utils.UrlUtil;
import com.rupayan_housing.viewModel.CurrentPermissionViewModel;
import com.rupayan_housing.viewModel.DashBoardViewModel;
import com.rupayan_housing.viewModel.HomePageViewModel;
import com.rupayan_housing.viewModel.LogoutViewModel;
import com.rupayan_housing.viewModel.PermissionViewModel;
import com.rupayan_housing.viewModel.report_all_view_model.SaleReportViewModel;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import im.crisp.client.ChatActivity;
import im.crisp.client.Crisp;


public class HomeFragment extends BaseFragment implements View.OnClickListener {
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private PermissionViewModel permissionViewModel;
    private LogoutViewModel logoutViewModel;
    private DashBoardViewModel dashBoardViewModel;
    private HomePageViewModel homePageViewModel;
    private CurrentPermissionViewModel currentPermissionViewModel;
    private SaleReportViewModel saleReportViewModel;

    String currentUsername;
    String currentUserProfileImageUrl;
    String currentUserPhoneNumber;
    private View view;
    private static final int VERTICAL_ITEM_SPACE = 15;

    @BindView(R.id.totalSale)
    TextView totalSale;
    @BindView(R.id.purchaseRawSalt)
    TextView purchaseRawSalt;
    @BindView(R.id.edibleRawSalt)
    TextView edibleRawSalt;
    @BindView(R.id.profileName)
    TextView profileName;

    @BindView(R.id.enterPriseShortName)
    TextView enterPriseShortName;
    //    @BindView(R.id.profileEmail)
//    TextView profileEmail;
    @BindView(R.id.version_codeTV)
    TextView version_code;
    //    @BindView(R.id.receipt)
//    TextView receipt;
//    @BindView(R.id.payment)
//    TextView payment;
    @BindView(R.id.industrialSaltSale)
    TextView industrialSaltSale;

    @BindView(R.id.profileImageView)
    CircleImageView profileImageView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.navbar)
    NavigationView navbar;
    @BindView(R.id.navBtn)
    ImageButton navBtn;
    @BindView(R.id.bellBtn)
    ImageButton bellBtn;
    @BindView(R.id.homeRV)
    RecyclerView homeRV;
    @BindView(R.id.logout)
    LinearLayout logout;
    @BindView(R.id.receiptView)
    RelativeLayout receiptView;
    @BindView(R.id.paymentView)
    RelativeLayout paymentView;
    @BindView(R.id.expenseView)
    RelativeLayout expenseView;
    @BindView(R.id.about)
    LinearLayout about;
    @BindView(R.id.privacy)
    LinearLayout privacy;
    @BindView(R.id.share)
    LinearLayout share;
    @BindView(R.id.feedback)
    LinearLayout feedback;
    @BindView(R.id.rate)
    LinearLayout rate;

    /*   @BindView(R.id.banner2)
       ImageView banner2;
       @BindView(R.id.banner3)
       ImageView banner3;
       @BindView(R.id.banner4)
       ImageView banner4;*/
    @BindView(R.id.millerProfile)
    LinearLayout millerProfile;
    @BindView(R.id.settings)
    LinearLayout settings;
    @BindView(R.id.logo)
    CircleImageView logo;//for add current user logo
    @BindView(R.id.corporationTitle)
    TextView companyName;
    @BindView(R.id.shipping_agent)
    TextView companyAddress;
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.reconciliation)
    LinearLayout reconciliation;
    @BindView(R.id.profilePortion)
    LinearLayout profilePortion;

    @BindView(R.id.seeMoreBtn)
    RelativeLayout seeMoreBtn;
    @BindView(R.id.rl_sell_circle_shape)
    RelativeLayout rl_sell_circle_shape;
    @BindView(R.id.sliderRv)
    RecyclerView sliderRv;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swiperefresh;

    @BindView(R.id.verify)
    com.mikhaellopez.circularimageview.CircularImageView verify;
    /*  @BindView(R.id.yesBtn)
      com.mikhaellopez.circularimageview.CircularImageView yesBtn;*/
    //@BindView(R.id.bottom_navigation)
    // BottomNavigationView bottomNavigation;
    LinearLayout item, salee, purchase, production, stock, monitoring, customer, supplier, qcqa, user, miller, report;
    TextView headerCurrentUsername;
    CircularImageView currentUserImage;
    TextView currentUserPhone;
    public static String millerId;
    Bundle bundle;
    ReviewInfo reviewInfo;
    ReviewManager reviewManager;
    public static boolean byTaping = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        permissionViewModel = ViewModelProviders.of(this).get(PermissionViewModel.class);
        logoutViewModel = ViewModelProviders.of(this).get(LogoutViewModel.class);
        dashBoardViewModel = new ViewModelProvider(this).get(DashBoardViewModel.class);
        homePageViewModel = new ViewModelProvider(this).get(HomePageViewModel.class);
        currentPermissionViewModel = new ViewModelProvider(this).get(CurrentPermissionViewModel.class);
        saleReportViewModel = new ViewModelProvider(this).get(SaleReportViewModel.class);

        inti();
        /** set onClick 12 icon*/
        setClick();
        int width = (getResources().getDisplayMetrics().widthPixels / 2) + 100;
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) navbar.getLayoutParams();
        params.width = width;
        navbar.setLayoutParams(params);

        NotificationUtil.IS_LOGGED_IN = true;
        //swip refresh
        getPreviousFragmentInfo();
        swiperefresh.setOnRefreshListener(() -> getPreviousFragmentInfo());
        /**
         * for control system onBackPress
         */
        OnBackPressedCallback callback = new OnBackPressedCallback(true /** enabled by default **/) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                MyApplication.exitApp(getActivity());//for show exit app dialog
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        profilePortion.setOnClickListener(v -> {
            try {
                hideKeyboard(getActivity());
                drawerLayout.closeDrawers();
                Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_profileFragment, bundle);
            } catch (Exception e) {
            }
        });

        seeMoreBtn.setOnClickListener(v -> {
            Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_dashboardFragment);
        });


        logout.setOnClickListener(v -> {
            /**
             * after logout move to loin fragment
             */
            logoutViewModel.logout(getActivity()).observe(getViewLifecycleOwner(), message -> {
                drawerLayout.closeDrawers();
                Toasty.success(getContext(), message, Toasty.LENGTH_LONG).show();
                Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_loginFragment);
            });
        });
/**
 * for scroll off
 */
        homeRV.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return e.getAction() == MotionEvent.ACTION_MOVE;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.dayBook:
                    Bundle bundle = new Bundle();
                    bundle.putString("portion", "Inbox");
                    Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_managementFragment, bundle);
                    break;
                case R.id.home:
                    Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_self);
                    break;
                case R.id.chat:
                    /**
                     * For open chat window
                     */
                    Crisp.configure(getContext(), CrispUtil.crispSecretKey);
                    Intent crispIntent = new Intent(getActivity(), ChatActivity.class);
                    startActivity(crispIntent);
                    break;
            }
            return true;
        });

// click sale circle
        rl_sell_circle_shape.setOnClickListener(v -> {
            try {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1277)) {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyy-MM-dd");
                    Date date = new Date();
                    bundle.putString("startDate", formatter.format(date));
                    bundle.putString("endDate", formatter.format(date));
                    bundle.putString("portion", ReportUtils.processingReport);
                    bundle.putString("pageName", "Sale Report");
                    Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_purchaseReturnListFragment, bundle);
                    return;
                } else {
                    Toasty.info(getContext(), PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }
            } catch (Exception e) {
            }
        });
        return view;
    }

    private void inti() {
        item = view.findViewById(R.id.item);
        salee = view.findViewById(R.id.salee);
        purchase = view.findViewById(R.id.purchase);
        production = view.findViewById(R.id.production);
        stock = view.findViewById(R.id.stock);
        monitoring = view.findViewById(R.id.monitoring);
        customer = view.findViewById(R.id.customer);
        supplier = view.findViewById(R.id.supplier);
        qcqa = view.findViewById(R.id.qcqa);
        user = view.findViewById(R.id.user);
        receiptView = view.findViewById(R.id.receiptView);
        paymentView = view.findViewById(R.id.paymentView);
        expenseView = view.findViewById(R.id.expenseView);
        millerProfile = view.findViewById(R.id.millerProfile);
        miller = view.findViewById(R.id.miller);
        report = view.findViewById(R.id.report);
        View navigationHeader = navbar.inflateHeaderView(R.layout.nav_header);
        headerCurrentUsername = navigationHeader.findViewById(R.id.currentUsername);
        currentUserImage = navigationHeader.findViewById(R.id.currentUserImage);
        currentUserPhone = navigationHeader.findViewById(R.id.currentUserPhone);
        reconciliation = view.findViewById(R.id.reconciliation);
        bottomNavigationView = view.findViewById(R.id.bottom_navigation);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private void getPreviousFragmentInfo() {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }
        getSummeryData();
        homePageViewModel.getHomePageData(getActivity())
                .observe(getViewLifecycleOwner(), response -> {
                    if (response == null || response.getStatus() == 400 || response.getStatus() == 500) {
                        errorMessage(getActivity().getApplication(), "Something Wrong");
                        return;
                    }
                    bundle = new Bundle();
                    if (response.getUserInfo().getFullName() != null) {
                   /*     bundle.putString("name", response.getUserInfo().getFullName());
                        profileName.setText("" + response.getUserInfo().getDisplayName());
                   */
                    }
                    bundle.putString("email", response.getUserInfo().getEmail());
                    bundle.putString("photo", response.getUserInfo().getProfilePhoto());
                    bundle.putString("primaryNumber", response.getUserInfo().getPrimaryMobile());
                    try {
                        if (response.getEnterpriseInfo() != null) {
                            enterPriseShortName.setText("" + response.getEnterpriseInfo().getStoreName() + "\n" + response.getUserInfo().getUserName());
                        }
                    } catch (Exception e) {
                    }

                    //                    if (response.getUserInfo().getEmail() != null) {
//                        profileEmail.setText("" + response.getUserInfo().getUserName());
//                    }
                    try {
                        Glide.with(getContext()).load(response.getUserInfo().getProfilePhoto()).
                                fitCenter().error(R.drawable.error_one)
                                .placeholder(R.drawable.error_one).
                                into(profileImageView);
                    } catch (Exception e) {
                    }
                    /**
                     * set banner
                     */

                  /*  try {
                        if (!response.getSliderLists().isEmpty()) {
                            SliderAdapter sliderAdapter = new SliderAdapter(getActivity(), response.getSliderLists());
                            sliderRv.setLayoutManager(new LinearLayoutManager(getContext()));
                            sliderRv.setAdapter(sliderAdapter);
                        }
                    } catch (Exception e) {
                    }*/


                    List<Integer> list = new ArrayList<>();
                    list.add(R.drawable.banner1);
                    list.add(R.drawable.banner2);

                    SliderAdapter sliderAdapter = new SliderAdapter(getActivity(), list);
                    sliderRv.setLayoutManager(new LinearLayoutManager(getContext()));
                    sliderRv.setAdapter(sliderAdapter);


                    /**
                     * now set company information
                     */
                    TextDrawable drawable = null;
                    try {
                        if (response.getEnterpriseInfo().getCompanyLogo() == null) {
                            if (response.getEnterpriseInfo().getFullName() != null) {
                                String firstFont;
                                String name = String.valueOf(response.getEnterpriseInfo().getFullName());
                                firstFont = String.valueOf(name.charAt(0)).toUpperCase();

                                ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
                                int color = generator.getColor(firstFont);
                                drawable = TextDrawable.builder().buildRound(firstFont, color);//radius in px color);
                            }
                            logo.setImageDrawable(drawable);
                        } else {


                            if (getProfileTypeId(getActivity().getApplication()).equals("7")) {
                                try {
                                    Glide.with(getContext())
                                            .load(response.getEnterpriseInfo().getStoreLogo())
                                            .centerInside()
                                            .error(R.drawable.dash_icon)
                                            .into(logo);
                                } catch (Exception e) {
                                }
                            }

                            if (!getProfileTypeId(getActivity().getApplication()).equals("7")) {
                                if (getUserId(getActivity().getApplication()).equals(getVendorId(getActivity().getApplication()))) {
                                    try {
                                        Glide.with(getContext())
                                                .load(response.getUserInfo().getProfilePhoto())
                                                .centerInside()
                                                .error(R.drawable.dash_icon)
                                                .into(logo);
                                    } catch (Exception e) {
                                    }

                                }

                                if (!getUserId(getActivity().getApplication()).equals(getVendorId(getActivity().getApplication()))) {
                                    try {
                                        Glide.with(getContext())
                                                .load(response.getEnterpriseInfo().getStoreLogo())
                                                .centerInside()
                                                .error(R.drawable.dash_icon)
                                                .into(logo);
                                    } catch (Exception e) {
                                    }

                                }
                            }

                        }
                    } catch (Exception e) {
                        Log.d("ERROR", e.getLocalizedMessage());
                    }
                    /**
                     * now set company name and address
                     */
                    try {
                        if (response.getEnterpriseInfo().getFullName() != null) {
                            companyName.setText("Rupayan Housing Ltd."  /*response.getEnterpriseInfo().getFullName()*/);
                        }
                   /*     companyAddress.setText("" + response.getEnterpriseInfo().getStoreAddress());
                        totalSale.setText("" + KgToTon.kgToTon(ReplaceCommaFromString.replaceComma(String.valueOf(response.getTotalSale()))) + MtUtils.metricTon);
                        purchaseRawSalt.setText("" + KgToTon.kgToTon(ReplaceCommaFromString.replaceComma(String.valueOf(response.getRawSaltBuy()))) + MtUtils.metricTon);
                        edibleRawSalt.setText("" + KgToTon.kgToTon(ReplaceCommaFromString.replaceComma(String.valueOf(response.getEdibleSaltSale()))) + MtUtils.metricTon);
                        industrialSaltSale.setText("" + KgToTon.kgToTon(ReplaceCommaFromString.replaceComma(String.valueOf(response.getIndustrialSaltSale()))) + MtUtils.metricTon);
                   */
                    } catch (Exception e) {
                        Log.d("ERROR", e.getLocalizedMessage());
                    }

                    if (!(response.getStoreAdded() == 0)) {
                        //  yesBtn.setVisibility(View.VISIBLE);
                    }

                    /**
                     * now show dialog if millerProfile not approved
                     */
                    if (getProfileTypeId(getActivity().getApplication()).equals("7")) {

                        if (response.getStoreAdded() == 0) {//
                            millerMakeProfileDialog(getActivity(), null, R.drawable.create_miller_dialog_image);//for add new Miller
                            return;
                        }
                        if (response.getStoreApproved() == 0) {
                            millerMakeProfileDialog(getActivity(), "Status: Pending", R.drawable.review_miller_dialog_image);//for pending miller profile
                            millerId = response.getMill_id();
                            verify.setVisibility(View.GONE);// tik mark hide
                        }
                    }

                    /**
                     * check licence validity
                     */
                 /*   try {
                        if (!(response.getLicenceExpire() == null || response.getLicenceExpire().isEmpty())) {

                            showDialog(response.getLicenceExpire());
                        }
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }*/

                    swiperefresh.setRefreshing(false);
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getSummeryData() {
        saleReportViewModel.getSaleReportList(getActivity(), CurrentDate.currentDate(), CurrentDate.currentDate(), null,
                null, null, null, null, null).observe(getViewLifecycleOwner(), response -> {

            if (response == null || response.getStatus() == 400 || response.getStatus() == 500) {
                errorMessage(getActivity().getApplication(), "Something wrong");
                return;
            }

            if (response.getStatus() == 200) {
                for (int i = 0; i < response.getProfuctList().size(); i++) {
                    if (response.getProfuctList().get(i).getCategoryID().equals("")) {// 740 Iodized Salt (Packet)


                    }
                }
            }
        });

    }

    private void showDialog(List<LicenceExpire> licenceExpires) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            @SuppressLint("InflateParams")
            View view = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.layout_for_licence_check_dialog, null);
            //Set the view
            builder.setView(view);

            RecyclerView recyclerView = view.findViewById(R.id.checkRv);
            ImageButton df = view.findViewById(R.id.btn_neg);
            AlertDialog alertDialog = builder.create();
            Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            df.setOnClickListener(v -> alertDialog.dismiss());//for cancel
            try {

                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                LicenceCheckAdapter adapter = new LicenceCheckAdapter(getActivity(), licenceExpires);
                recyclerView.setAdapter(adapter);
            } catch (Exception e) {
                Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            alertDialog.show();

        } catch (Exception e) {
            Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * for set this fragment RV data
     */
    @SuppressLint({"RtlHardcoded", "NonConstantResourceId"})
    @OnClick(R.id.navBtn)
    void openNavDrawer() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT))
            drawerLayout.closeDrawer(Gravity.LEFT);
        else drawerLayout.openDrawer(Gravity.LEFT);
    }

    @OnClick(R.id.bellBtn)
    public void onclickNotificationBtn() {
        Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_notificationFragment);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnClick(R.id.qrScan)
    public void qrScanClick() {
        drawerLayout.closeDrawers();

        try {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
            } else {
                Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_qrScanFragment);
            }
        } catch (Exception e) {
            Log.d("ERROR", "ERROR");
        }

    }

    @OnClick(R.id.receiptView)
    public void receiptView() {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString("pageName", "Creditors");
        bundle.putString("portion", "Credit");
        Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_creditorsFragment, bundle);
    }

    @OnClick(R.id.paymentView)
    public void paymentView() {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString("pageName", "Debitors");
        bundle.putString("portion", "Debit");
        Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_creditorsFragment, bundle);
    }

    @OnClick(R.id.expenseView)
    public void expenseView() {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString("pageName", "Debitors");
        bundle.putString("portion", "Debit");
        Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_dashBoardExpenseList, bundle);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        NotificationUtil.IS_LOGGED_IN = false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "camera permission granted", Toast.LENGTH_LONG).show();
                Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_qrScanFragment);
            } else {
                Toast.makeText(getContext(), "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void setClick() {
        item.setOnClickListener(this);
        salee.setOnClickListener(this);
        purchase.setOnClickListener(this);
        production.setOnClickListener(this);
        stock.setOnClickListener(this);
        monitoring.setOnClickListener(this);
        customer.setOnClickListener(this);
        supplier.setOnClickListener(this);
        qcqa.setOnClickListener(this);
        user.setOnClickListener(this);
        report.setOnClickListener(this);
        millerProfile.setOnClickListener(this);
        reconciliation.setOnClickListener(this);
        settings.setOnClickListener(this);
        receiptView.setOnClickListener(this);
        paymentView.setOnClickListener(this);
        expenseView.setOnClickListener(this);
        about.setOnClickListener(this);
        rate.setOnClickListener(this);
        share.setOnClickListener(this);
        feedback.setOnClickListener(this);
        privacy.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.about:
                try {
                    bundle.putString("pageName", "About");
                    bundle.putString("url", UrlUtil.aboutUrl);
                    Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_appWebViewFragmnet, bundle);
                } catch (Exception e) {
                }

                break;
            case R.id.privacy:
                try {
                    bundle.putString("pageName", "Privacy Policy");
                    bundle.putString("url", UrlUtil.privacyUti);
                    Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_appWebViewFragmnet, bundle);
                } catch (Exception e) {
                }

                break;
            case R.id.feedback:
                try {
                    String[] recipients = new String[]{"support@usi.net.bd"};// Replace your email id here
                    final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipients);
                    emailIntent.setType("text/plain");
                    final PackageManager pm = getContext().getPackageManager();
                    final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
                    ResolveInfo best = null;
                    for (final ResolveInfo info : matches)
                        if (info.activityInfo.packageName.endsWith(".gm") ||
                                info.activityInfo.name.toLowerCase().contains("gmail")) best = info;
                    if (best != null)
                        emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
                    startActivity(emailIntent);
                } catch (Exception e) {
                }

                break;

            case R.id.share:
           /*     try {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Android Studio Pro");
                    intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + "com.usibd_central_mis" + "&hl=en");
                    intent.setType("text/plain");
                    startActivity(intent);
                } catch (Exception e) {
                }*/

                break;
            case R.id.rate:
                try {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.usibd_central_mis")));
                } catch (Exception e) {
                }

                break;


            case R.id.item:
                if (!(isInternetOn(getActivity()))) {
                    infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
                    return;
                }
                bundle.putString("Item", HomeUtils.itemManagement);
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_managementFragment, bundle);
                break;

            case R.id.salee:
                if (!(isInternetOn(getActivity()))) {
                    infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
                    return;
                }
                bundle.putString("Item", HomeUtils.sales);
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_managementFragment, bundle);
                break;
            case R.id.purchase:
                if (!(isInternetOn(getActivity()))) {
                    infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
                    return;
                }
                bundle.putString("Item", HomeUtils.purchases);
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_managementFragment, bundle);
                break;
            case R.id.production:
                if (!(isInternetOn(getActivity()))) {
                    infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
                    return;
                }
                bundle.putString("Item", HomeUtils.production);
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_managementFragment, bundle);
                break;
            case R.id.stock:
                if (!(isInternetOn(getActivity()))) {
                    infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
                    return;
                }
                bundle.putString("Item", HomeUtils.stock);
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_managementFragment, bundle);
                break;
            case R.id.monitoring:
                if (!(isInternetOn(getActivity()))) {
                    infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
                    return;
                }
                bundle.putString("Item", HomeUtils.monitoring);
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_managementFragment, bundle);
                break;
            case R.id.customer:
                if (!(isInternetOn(getActivity()))) {
                    infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
                    return;
                }
                bundle.putString("Item", HomeUtils.customers);
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_managementFragment, bundle);
                break;
            case R.id.supplier:
                if (!(isInternetOn(getActivity()))) {
                    infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
                    return;
                }
                bundle.putString("Item", HomeUtils.suppliers);
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_managementFragment, bundle);
                break;
            case R.id.qcqa:
                if (!(isInternetOn(getActivity()))) {
                    infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
                    return;
                }
                bundle.putString("Item", HomeUtils.qcQa);
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_managementFragment, bundle);
                break;

            case R.id.user:
                if (!(isInternetOn(getActivity()))) {
                    infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
                    return;
                }
                try {
                    bundle.putString("Item", HomeUtils.user);
                    Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_managementFragment, bundle);
                } catch (Exception e) {
                    Log.d("ERROR", "" + e.getLocalizedMessage());
                }
                break;

            case R.id.report:
                if (!(isInternetOn(getActivity()))) {
                    infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
                    return;
                }
                bundle.putString("Item", HomeUtils.report);
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_managementFragment, bundle);
                break;

            case R.id.reconciliation:
                if (!(isInternetOn(getActivity()))) {
                    infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
                    return;
                }
                bundle.putString("Item", HomeUtils.reconciliation);
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_managementFragment, bundle);
                break;

            case R.id.millerProfile:
                if (!(isInternetOn(getActivity()))) {
                    infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
                    return;
                }
                bundle.putString("Item", "Mill Profile");
                drawerLayout.closeDrawers();
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_managementFragment, bundle);
                break;

            case R.id.settings:
                if (!(isInternetOn(getActivity()))) {
                    infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
                    return;
                }
                bundle.putString("Item", "Settings");
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_managementFragment, bundle);
                break;

            case R.id.receiptView:
                bundle.putString("portion", ReportUtils.availAbleReport);
                bundle.putString("pageName", ReportUtils.availAbleReport);
                Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_reportListFragment, bundle);


                //   goToReportList("736", 1278);

                /* bundle.putString("portion", "PurchaseRawSalt");
                Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_homePageListFragment, bundle);*/
                break;

            case R.id.paymentView:

                bundle.putString("portion", ReportUtils.processingReport);
                bundle.putString("pageName", ReportUtils.processingReport);
                Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_reportListFragment, bundle);

                //    goToReportList("740", 1277);

             /*   if (!(isInternetOn(getActivity()))) {
                    infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
                    return;
                }
                bundle.putString("portion", "EdibleRawSalt");
                Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_homePageListFragment, bundle);
               */
                break;

            case R.id.expenseView:

                bundle.putString("pageName", ReportUtils.noteSheetGenerateReport);
                bundle.putString("portion", ReportUtils.noteSheetGenerateReport);
                Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_reportListFragment, bundle);
                break;
            //          goToReportList("739", 1277);

//                if (!(isInternetOn(getActivity()))) {
//                    infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
//                    return;
//                }
//                bundle.putString("portion", "IndustrialRawSalt");
//                Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_homePageListFragment, bundle);
//                break;
        }
    }

    private void getReviewInfo() {
        Task<ReviewInfo> manager = reviewManager.requestReviewFlow();
        manager.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                reviewInfo = task.getResult();
            } else {
                // Toast.makeText(getActivity(), "In App ReviewFlow failed to start", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void startReviewFlow() {
        if (reviewInfo != null) {
            Task<Void> flow = reviewManager.launchReviewFlow(getActivity(), reviewInfo);
            flow.addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(Task<Void> task) {
                    Toast.makeText(getActivity(), "In App Rating complete", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            // Toast.makeText(getContext(), "In App Rating failed", Toast.LENGTH_LONG).show();
        }
    }

    private void goToReportList(String category, Integer permission) {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }
        if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(permission)) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyy-MM-dd");
            Date date = new Date();
            bundle.putString("startDate", formatter.format(date));
            bundle.putString("endDate", formatter.format(date));
            bundle.putString("categoryId", category);
            if (category.equals("736")) {
                bundle.putString("portion", ReportUtils.saleDetailsReport);
                bundle.putString("pageName", "Raw Salt Purchase");
            }
            if (category.equals("740")) {
                bundle.putString("portion", ReportUtils.processingReport);
                bundle.putString("pageName", "Iodized Salt Sale");
            }
            if (category.equals("739")) {
                bundle.putString("portion", ReportUtils.processingReport);
                bundle.putString("pageName", "Industrial Salt Sale");
            }

            Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_purchaseReturnListFragment, bundle);
            return;
        } else {
            Toasty.info(getContext(), PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
        }
    }

    public void millerMakeProfileDialog(FragmentActivity context, String title, int imagePath) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        @SuppressLint("InflateParams")
        View view = ((Activity) context).getLayoutInflater().inflate(R.layout.miller_profile_creation_request_dialog, null);
        //Set the view
        builder.setView(view);
        Button createMillerButton, millerProfileListBtn, dialogLogout;
        TextView tvTitle;
        ImageView createMillerCreateDialogImage;
        ImageView imageIcon = view.findViewById(R.id.img_icon);
        tvTitle = view.findViewById(R.id.tv_title);
        millerProfileListBtn = view.findViewById(R.id.millerProfileListBtn);
        createMillerCreateDialogImage = view.findViewById(R.id.createMillerCreateDialogImage);
        createMillerButton = view.findViewById(R.id.createMillerProfileButton);
        dialogLogout = view.findViewById(R.id.dialogLogout);

        if (title == null) {
            tvTitle.setText("Create New Mill");//set title
            createMillerCreateDialogImage.setImageDrawable(ContextCompat.getDrawable(context, imagePath));

        } else {
            createMillerButton.setVisibility(View.GONE);
            millerProfileListBtn.setVisibility(View.VISIBLE);
            tvTitle.setText("" + title);
            createMillerCreateDialogImage.setImageDrawable(ContextCompat.getDrawable(context, imagePath));
        }
        imageIcon.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_launcher));//set warning image
        AlertDialog alertDialog = builder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        createMillerButton.setOnClickListener(v -> {
            alertDialog.dismiss();
            Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_addNewMiller);
            byTaping = true;
        });
        /**
         * for show miller list
         */
        millerProfileListBtn.setOnClickListener(v -> {
            alertDialog.dismiss();
            if (!(isInternetOn(getActivity()))) {
                infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putString("porson", MillerUtils.millreProfileList);
            bundle.putString("slId", millerId);
            Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_millerDetailsViewFragment, bundle);
        });

        dialogLogout.setOnClickListener(v -> {
            try {
                logoutViewModel.logout(getActivity()).observe(getViewLifecycleOwner(), message -> {
                    alertDialog.dismiss();
                    drawerLayout.closeDrawers();
                    Toasty.success(getContext(), message, Toasty.LENGTH_LONG).show();
                    Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_loginFragment);
                });
            } catch (Exception e) {
                Log.d("ERROR", "" + e.getMessage());
            }
        });
        alertDialog.show();
    }


    @Override
    public void onStart() {
        super.onStart();

        try {
            version_code.setText("VERSION " + getCurrentApplicationVersion());
        } catch (Exception e) {
            Log.d("ERROR", "" + e.getMessage());
        }

        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }
        currentPermissionViewModel.getCurrentUserRealtimePermissions(
                PreferenceManager.getInstance(getActivity()).getUserCredentials().getToken(),
                PreferenceManager.getInstance(getActivity()).getUserCredentials().getUserId()
        ).observe(getViewLifecycleOwner(), response -> {
            if (response == null) {
                //  Toasty.error(getActivity(), "Something Wrong", Toasty.LENGTH_LONG).show();
                return;
            }
            if (response.getStatus() == 400) {
                //   Toasty.info(getActivity(), "" + response.getMessage(), Toasty.LENGTH_LONG).show();
                return;
            }
            try {
                LoginResponse loginResponse = PreferenceManager.getInstance(getActivity()).getUserCredentials();
                if (loginResponse != null) {
                    loginResponse.setPermissions(response.getMessage());
                    loginResponse.setToken(response.getToken());
                    PreferenceManager.getInstance(getActivity()).saveUserCredentials(loginResponse);
                }
            } catch (Exception e) {
                infoMessage(getActivity().getApplication(), "" + e.getMessage());
                Log.d("ERROR", "" + e.getMessage());
            }
        });
    }

    /**
     * For detect current app version
     */
    public String getCurrentApplicationVersion() {
        try {
            String versionName = getContext().getPackageManager()
                    .getPackageInfo(getContext().getPackageName(), 0).versionName;
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Double kgToTon(String purchaseCm) {
        try {
            double number = 0.00;
            if (purchaseCm != null) {
                number = Double.parseDouble(purchaseCm) / 1000;
            }

            return number;
        } catch (Exception e) {
            return 0.0;
        }
    }

}