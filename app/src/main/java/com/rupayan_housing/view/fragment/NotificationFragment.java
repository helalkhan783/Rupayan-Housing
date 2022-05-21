package com.rupayan_housing.view.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.rupayan_housing.R;
import com.rupayan_housing.adapter.NotificationListAdapter;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.retrofit.PurchaseDeclineList;
import com.rupayan_housing.serverResponseModel.LoginResponse;
import com.rupayan_housing.serverResponseModel.NotificationListResponse;

import com.rupayan_housing.viewModel.CurrentPermissionViewModel;
import com.rupayan_housing.viewModel.NotificationListViewModel;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;


import static com.rupayan_housing.view.fragment.DueCollectionFragment.HIDE_KEYBOARD;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.jetbrains.annotations.NotNull;

public class NotificationFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener {
    List<NotificationListResponse> notificationListResponseList = new ArrayList<>();

    private NotificationListViewModel notificationListViewModel;
    private CurrentPermissionViewModel currentPermissionViewModel;
    String vendorId, token;


    private boolean isDataFetching = false; // variable to detect whether server data fetch is loading or not
    @BindView(R.id.toolbarTitle)
    TextView toolbar;
    @BindView(R.id.notification_list_rv)
    RecyclerView notificationListRv;
    @BindView(R.id.empty_notification_list_warning)
    TextView emtyWarning;
    @BindView(R.id.expendableEnterPriseList)
    ExpandableLayout expendableEnterPriseList;
    @BindView(R.id.dateOfBirth)
    TextView dateOfBirth;
    @BindView(R.id.customerSearchBtn)
    Button search;
    @BindView(R.id.customerResetBtn)
    Button resetBtn;
    String date;

    @BindView(R.id.filterBtnForNotification)
    ImageButton filterBtnForNotification;

    @BindView(R.id.status)
    SmartMaterialSpinner status;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    View view;
    /**
     * for pagination
     */
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    public static int pageNumber = 1, isFirstLoad = 0;

    private boolean endScroll = false;
    private ProgressDialog progressDialog;
    private LinearLayoutManager linearLayoutManager;

    List<String> statusList = new ArrayList<>();
    String satutusId, positionSelected;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_notification, container, false);
        ButterKnife.bind(this, view);
        notificationListViewModel = ViewModelProviders.of(this).get(NotificationListViewModel.class);
        currentPermissionViewModel = new ViewModelProvider(this).get(CurrentPermissionViewModel.class);
        HIDE_KEYBOARD(getActivity());

        getDataFromPreviousFragment();

        initComponent();

        /**
         * get current user notification list from server
         */
        getNotificationList();
        statusList.add("Approved");
        statusList.add("Pending");
        statusList.add("Declined");
        status.setItem(statusList);
        status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    satutusId = "1";
                }
                if (position == 1) {
                    satutusId = "2";
                }
                if (position == 2) {
                    satutusId = "0";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        /** for pagination **/
        notificationListRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

                            getNotificationList();
                            loading = true;
                        }
                    }
                }
            }
        });

        filterBtnForNotification.setOnClickListener(v -> {
            if (expendableEnterPriseList.isExpanded()) {
                expendableEnterPriseList.setExpanded(false);
                return;
            }
            expendableEnterPriseList.setExpanded(true);
        });
        dateOfBirth.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            DatePickerDialog dialog = DatePickerDialog.newInstance(
                    NotificationFragment.this,
                    now.get(Calendar.YEAR), // Initial year selection
                    now.get(Calendar.MONTH), // Initial month selection
                    now.get(Calendar.DAY_OF_MONTH) // Initial day selection
            );
            dialog.show(getActivity().getSupportFragmentManager(), "Datepickerdialog");
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationListResponseList.clear();
                isFirstLoad = 0;
                pageNumber = 1;
                getNotificationList();
            }
        });
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = null;
                satutusId = null;
                notificationListResponseList.clear();
                status.clearSelection();
                dateOfBirth.setText("");
                isFirstLoad = 0;
                pageNumber = 1;
                getNotificationList();


            }
        });

        return view;
    }

    private void getNotificationList() {
        progressDialog = new ProgressDialog(getContext());
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }
        if (pageNumber == 1) {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.show();
        }

        if (pageNumber > 1) {
//            binding.progress.setVisibility(View.VISIBLE);
//            binding.progress.setProgress(20);
//            binding.progress.setMax(100);
        }
        getNotificationListResponse(token, vendorId);

    }

    private void getDataFromPreviousFragment() {
        vendorId = PreferenceManager.getInstance(getContext()).getUserCredentials().getVendorID();
        token = PreferenceManager.getInstance(getContext()).getUserCredentials().getToken();
        toolbar.setText("Notifications");
    }

    private void initComponent() {
        refreshLayout.setColorSchemeResources(R.color.colorG, R.color.colorG, R.color.colorG);
        refreshLayout.setOnRefreshListener(() -> {
            refreshLayout.setRefreshing(true);
            (new Handler()).postDelayed(() -> {
                refreshLayout.setRefreshing(false);
                getNotificationList();
            }, 500);
            Toasty.info(getActivity(), "Notification updated", Toasty.LENGTH_LONG).show();
        });
    }


    @SuppressLint("SetTextI18n")
    private void getNotificationListResponse(String token, String vendorID) {
        isDataFetching = true;
        notificationListViewModel.apiCallForGetNotificationList(getActivity(), String.valueOf(pageNumber), token, vendorID, satutusId, date)
                .observe(getViewLifecycleOwner(), notificationResponse -> {
                    progressDialog.dismiss();
                    if (notificationResponse == null) {
                        errorMessage(getActivity().getApplication(), "Something Wrong");
                        return;
                    }
                    if (notificationResponse.getStatus() == 400) {
                        infoMessage(getActivity().getApplication(), " " + notificationResponse.getMessage());
                        return;
                    }
                    //  notificationListResponseList.clear();
                    notificationListResponseList.addAll(notificationResponse.getNotifications());
                    //  notificationListResponseList = notificationResponse.getNotifications();


                    if (notificationResponse.getNotifications().isEmpty() || notificationResponse.getNotifications() == null) {
                        if (isFirstLoad > 0) {
                            endScroll = true;
                            pageNumber -= 1;
                            return;
                        }
                        notificationListRv.setVisibility(View.GONE);
                        emtyWarning.setVisibility(View.VISIBLE);
                        emtyWarning.setText("No data found");

                    } else {
                        isFirstLoad += 1;
                        emtyWarning.setVisibility(View.GONE);
                        notificationListRv.setVisibility(View.VISIBLE);

                        /**
                         * now set current all notification to view
                         */
                        notificationListRv.setHasFixedSize(true);
                        linearLayoutManager = new LinearLayoutManager(getContext());
                        notificationListRv.setLayoutManager(linearLayoutManager);

                        /**
                         * now set notification list to recyclerView
                         */
                        NotificationListAdapter adapter = new NotificationListAdapter(getActivity(), notificationListResponseList);
                        notificationListRv.setAdapter(adapter);
                    }
                });
        //notificationListViewModel.getNotificationList().observe(getViewLifecycleOwner(), notificationResponse -> {

        //});
    }

    @OnClick(R.id.backbtn)
    public void backClick() {
        getActivity().onBackPressed();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        pageNumber = 1;
        notificationListResponseList.clear();
    }

    @Override
    public void onStart() {
        date = null;
        satutusId = null;
        filterBtnForNotification.setVisibility(View.VISIBLE);
        super.onStart();

        try {
            /**
             * now update current credentials
             */
            updateCurrentUserPermission(getActivity());
        } catch (Exception e) {
            Log.d("ERROR", "" + e.getLocalizedMessage());
        }

    }


    public void updateCurrentUserPermission(FragmentActivity activity) {
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
                return;
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
        String selectedDate = mainDay + "-" + mainMonth + "-" + year;//set the selected date
        dateOfBirth.setText(selectedDate);
        date = dateOfBirth.getText().toString();
    }
}