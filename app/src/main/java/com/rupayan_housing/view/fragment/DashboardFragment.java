package com.rupayan_housing.view.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.rupayan_housing.R;
import com.rupayan_housing.adapter.LastMontIodizationAdapter;
import com.rupayan_housing.databinding.FragmentDashboardBinding;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.LastMonthIodine;
import com.rupayan_housing.serverResponseModel.LoginResponse;
import com.rupayan_housing.serverResponseModel.Production;
import com.rupayan_housing.serverResponseModel.Sales;
import com.rupayan_housing.utils.CrispUtil;
import com.rupayan_housing.utils.MillerUtils;
import com.rupayan_housing.utils.MonitoringUtil;
import com.rupayan_housing.utils.MtUtils;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.utils.ReportUtils;
import com.rupayan_housing.utils.replace.KgToTon;
import com.rupayan_housing.utils.replace.ReplaceCommaFromString;
import com.rupayan_housing.view.fragment.monitoring.MonitoringListFragment;
import com.rupayan_housing.viewModel.CurrentPermissionViewModel;
import com.rupayan_housing.viewModel.DashBoardViewModel;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import im.crisp.client.ChatActivity;
import im.crisp.client.Crisp;


public class DashboardFragment extends BaseFragment implements View.OnClickListener {
    private FragmentDashboardBinding binding;
    private DashBoardViewModel dashBoardViewModel;
    private CurrentPermissionViewModel currentPermissionViewModel;
    public static boolean manageMonitor = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false);
        dashBoardViewModel = new ViewModelProvider(this).get(DashBoardViewModel.class);
        currentPermissionViewModel = new ViewModelProvider(this).get(CurrentPermissionViewModel.class);
        getPeriviousFragmentData();
        backControl();
        getDashBoardData();
        dashBoardPermissionManage();
        setClick();

        binding.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.dayBook:
                    try {
                        Bundle bundle = new Bundle();
                        bundle.putString("portion", "Inbox");
                        Navigation.findNavController(getView()).navigate(R.id.action_dashboardFragment_to_managementFragment, bundle);

                    } catch (Exception e) {
                    }
                    break;
                case R.id.home:
                    Navigation.findNavController(getView()).navigate(R.id.action_dashboardFragment_to_homeFragment);
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
        return binding.getRoot();
    }

    private void getDashBoardData() {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check your Internet Connection");
            return;
        }

        dashBoardViewModel.dashboardData(getActivity()).observe(getViewLifecycleOwner(), response -> {
            try {

                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1347)) {
                    binding.totalMillTv.setText("" + response.getTotalMills());
                    binding.activeMillTv.setText("" + response.getActiveMills());
                    binding.inactiveMillTv.setText("" + response.getInactiveMills());
                }
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1278)) {

                    binding.purchase.setText("" + KgToTon.kgToTon(response.getPurchaseCm()) + MtUtils.metricTon);
                }
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1535)) {
                    binding.totalProductionTv.setText("" + KgToTon.kgToTon(response.getProductionCm()) + MtUtils.metricTon);
                }
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1277)) {
                    binding.totalSale.setText("" + KgToTon.kgToTon(response.getTotalSaleCm()) + MtUtils.metricTon);
                }
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1541)) {
                    binding.iodinePurchase.setText("" + KgToTon.kgToTon(response.getIodinePurchaseCm()) + MtUtils.metricTon);
                }
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1277)) {
                    binding.iodizedSaltSaleTv.setText("" + KgToTon.kgToTon(response.getIodizedSaleCm()) + MtUtils.metricTon);
                }
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1602)) {
                    binding.totalZone.setText("" + response.getTotalZone());
                }
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1603)) {
                    binding.devlopPartnar.setText("" + response.getTotalUnOrganization());
                }
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1604)) {
                    binding.govtAgencyTv.setText("" + response.getGoAgencies());
                }

                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1427)) {
                    binding.totalMonitoringTv.setText("" + response.getTotalMonitoring());
                }
            } catch (Exception e) {

            }
        });
        dashBoardViewModel.lastMontIodizationList(getActivity(), null, null, null).observe(getViewLifecycleOwner(), lastMonthIodizationResponse -> {
            if (lastMonthIodizationResponse.getStatus() == 200) {
                if (!lastMonthIodizationResponse.getList().isEmpty()) {
                    LastMontIodizationAdapter lastMontIodizationAdapter = new LastMontIodizationAdapter(getActivity(), lastMonthIodizationResponse.getList());
                    binding.listLayout.lastMonthIodizationRv.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.listLayout.lastMonthIodizationRv.setAdapter(lastMontIodizationAdapter);
                }
                if (lastMonthIodizationResponse.getList().isEmpty()) {
                    noDataFound(binding.listLayout.lastMonthIodizationRv, binding.listLayout.tv1);
                }

            }
        });
        dashBoardViewModel.lastMontStock(getActivity(), null, null, null).observe(getViewLifecycleOwner(), lastMonthIodizationResponse -> {
            if (lastMonthIodizationResponse.getStatus() == 200) {
                if (!lastMonthIodizationResponse.getList().isEmpty()) {

                    LastMonthIodineStockAdapter lastMonthIodineStockAdapter = new LastMonthIodineStockAdapter(getActivity(), lastMonthIodizationResponse.getList(), this);
                    binding.listLayout.currentMonthIodizationRv.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.listLayout.currentMonthIodizationRv.setAdapter(lastMonthIodineStockAdapter);
                }
                if (lastMonthIodizationResponse.getList().isEmpty()) {
                    noDataFound(binding.listLayout.currentMonthIodizationRv, binding.listLayout.tv2);
                }

            }
        });
        dashBoardViewModel.lastMontQCQA(getActivity(), null, null, null).observe(getViewLifecycleOwner(), lastMonthIodizationResponse -> {
            if (lastMonthIodizationResponse.getStatus() == 200) {
                if (!lastMonthIodizationResponse.getList().isEmpty()) {

                    LastMontQcQaAdapter lastMontQcQaAdapter = new LastMontQcQaAdapter(getActivity(), lastMonthIodizationResponse.getList());
                    binding.listLayout.lastMonthQcQaRv.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.listLayout.lastMonthQcQaRv.setAdapter(lastMontQcQaAdapter);
                }
                if (lastMonthIodizationResponse.getList().isEmpty()) {
                    noDataFound(binding.listLayout.lastMonthQcQaRv, binding.listLayout.tv3);
                }

            }
        });
        dashBoardViewModel.zoneWiseMonitoringList(getActivity(), null, null, null).observe(getViewLifecycleOwner(), lastMonthIodizationResponse -> {
            if (lastMonthIodizationResponse.getStatus() == 200) {
                if (!lastMonthIodizationResponse.getList().isEmpty()) {


                    ZoneWiseMonitoringAdapter zoneWiseMonitoringAdapter = new ZoneWiseMonitoringAdapter(getActivity(), lastMonthIodizationResponse.getList());
                    binding.listLayout.zoneWiseMonitoring.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.listLayout.zoneWiseMonitoring.setAdapter(zoneWiseMonitoringAdapter);
                }
                if (lastMonthIodizationResponse.getList().isEmpty()) {
                    noDataFound(binding.listLayout.zoneWiseMonitoring, binding.listLayout.tv4);
                }

            }
        });
        dashBoardViewModel.agencyMonitoringlist(getActivity(), null, null).observe(getViewLifecycleOwner(), lastMonthIodizationResponse -> {
            if (lastMonthIodizationResponse.getStatus() == 200) {
                if (!lastMonthIodizationResponse.getList().isEmpty()) {

                    AgengyMonitoringAdapter agengyMonitoringAdapter = new AgengyMonitoringAdapter(getActivity(), lastMonthIodizationResponse.getList());
                    binding.listLayout.lastMonthMonitoringAgencyBased.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.listLayout.lastMonthMonitoringAgencyBased.setAdapter(agengyMonitoringAdapter);
                }
                if (lastMonthIodizationResponse.getList().isEmpty()) {
                    noDataFound(binding.listLayout.lastMonthMonitoringAgencyBased, binding.listLayout.tv5);
                }

            }
        });
        dashBoardViewModel.issueMonitoringList(getActivity(), null, null).observe(getViewLifecycleOwner(), lastMonthIodizationResponse -> {
            if (lastMonthIodizationResponse.getStatus() == 200) {
                if (!lastMonthIodizationResponse.getList().isEmpty()) {
                    IssueMonitoringListAdapter issueMonitoringListAdapter = new IssueMonitoringListAdapter(getActivity(), lastMonthIodizationResponse.getList());
                    binding.listLayout.lastMonthMonitoringIssueWise.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.listLayout.lastMonthMonitoringIssueWise.setAdapter(issueMonitoringListAdapter);
                }
                if (lastMonthIodizationResponse.getList().isEmpty()) {
                    noDataFound(binding.listLayout.lastMonthMonitoringIssueWise, binding.listLayout.tv6);
                }

            }
        });
        dashBoardViewModel.lastMonthsale(getActivity(), null, null).observe(getViewLifecycleOwner(), lastMonthIodizationResponse -> {
            if (lastMonthIodizationResponse.getStatus() == 200) {
                if (!lastMonthIodizationResponse.getList().isEmpty()) {
                    try {
                        LastMonthMonitoringSaleAdapter lastMonthMonitoringSaleAdapter = new LastMonthMonitoringSaleAdapter(getActivity(), lastMonthIodizationResponse.getList(), this);
                        binding.listLayout.lastMonthSaleRv.setLayoutManager(new LinearLayoutManager(getContext()));
                        binding.listLayout.lastMonthSaleRv.setAdapter(lastMonthMonitoringSaleAdapter);
                    } catch (Exception e) {
                    }
                }
                if (lastMonthIodizationResponse.getList().isEmpty()) {
                    noDataFound(binding.listLayout.lastMonthSaleRv, binding.listLayout.tv7);
                }

            }
        });
        dashBoardViewModel.lastProduction(getActivity(), null, null).observe(getViewLifecycleOwner(), lastMonthIodizationResponse -> {
            if (lastMonthIodizationResponse.getStatus() == 200) {
                if (!lastMonthIodizationResponse.getList().isEmpty()) {

                    LastMonthMonitoringSaleAdapter lastMonthMonitoringSaleAdapter = new LastMonthMonitoringSaleAdapter(getActivity(), lastMonthIodizationResponse.getList(), this);
                    binding.listLayout.lastMonthProductionRv.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.listLayout.lastMonthProductionRv.setAdapter(lastMonthMonitoringSaleAdapter);
                }
                if (lastMonthIodizationResponse.getList().isEmpty()) {
                    noDataFound(binding.listLayout.lastMonthProductionRv, binding.listLayout.tv8);
                }

            }
        });
        dashBoardViewModel.lastPurchase(getActivity(), null, null).observe(getViewLifecycleOwner(), lastMonthIodizationResponse -> {
            if (lastMonthIodizationResponse.getStatus() == 200) {
                if (!lastMonthIodizationResponse.getList().isEmpty()) {

                    LastMonthMonitoringSaleAdapter lastMonthMonitoringSaleAdapter = new LastMonthMonitoringSaleAdapter(getActivity(), lastMonthIodizationResponse.getList(), this);
                    binding.listLayout.lastMonthPurchaseRv.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.listLayout.lastMonthPurchaseRv.setAdapter(lastMonthMonitoringSaleAdapter);
                }
                if (lastMonthIodizationResponse.getList().isEmpty()) {
                    noDataFound(binding.listLayout.lastMonthPurchaseRv, binding.listLayout.tv9);
                }

            }
        });
        dashBoardViewModel.topTenMillList(getActivity(), null, null, null).observe(getViewLifecycleOwner(), lastMonthIodizationResponse -> {
           try {
               if (lastMonthIodizationResponse.getStatus() == 200) {
                   if (!lastMonthIodizationResponse.getList().isEmpty()) {
                       TopTenMillerListAdapter topTenMillerListAdapter = new TopTenMillerListAdapter(getActivity(), lastMonthIodizationResponse.getList(), this);
                       binding.listLayout.topTenMillRv.setLayoutManager(new LinearLayoutManager(getContext()));
                       binding.listLayout.topTenMillRv.setAdapter(topTenMillerListAdapter);
                   }
                   if (lastMonthIodizationResponse.getList().isEmpty()) {
                       noDataFound(binding.listLayout.topTenMillRv, binding.listLayout.tv10);
                   }

               }
           }catch (Exception e){}
        });
        dashBoardViewModel.janinaList(getActivity(), null, null, null).observe(getViewLifecycleOwner(), lastMonthIodizationResponse -> {
            if (lastMonthIodizationResponse.getStatus() == 200) {
                if (!lastMonthIodizationResponse.getList().isEmpty()) {

                    JaninaAdapter janinaAdapter = new JaninaAdapter(getActivity(), lastMonthIodizationResponse.getList(), this);
                    binding.listLayout.zoneListHierarchyRv.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.listLayout.zoneListHierarchyRv.setAdapter(janinaAdapter);
                }
                if (lastMonthIodizationResponse.getList().isEmpty()) {
                    noDataFound(binding.listLayout.zoneListHierarchyRv, binding.listLayout.tv11);
                }

            }
        });
        /**
         * pie chart data Set
         */
        dashBoardViewModel.pieChartData(getActivity()).observe(getViewLifecycleOwner(), dataresponse -> {
            if (dataresponse.getStatus() == 200) {
                showPieChart(dataresponse.getData().getSales());
                showPieChart1(dataresponse.getData().getProduction());
                showPieChart$(dataresponse.getData().getLastMonthIodine());
            }

        });

    }

    private void noDataFound(RecyclerView recyclerView, TextView textView) {
        recyclerView.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
    }

    private void dashBoardPermissionManage() {
        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1606)) {
//production pie chart
            binding.productionPieChartLayout.setVisibility(View.GONE);
        }

        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1607)) {
//sale piechart
            binding.salePieChartLayout.setVisibility(View.GONE);
        }
        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1617)) {
//iodine
            binding.listLayout.iodineUsedPieChartLayout.setVisibility(View.GONE);
        }
        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1608)) {
//last month iodization (zone wise toal mill)
            binding.listLayout.lastMonthIodization.setVisibility(View.GONE);
        }
        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1609)) {
            binding.listLayout.currentMonthIodineStockLayout.setVisibility(View.GONE);
        }
        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1610)) {
            binding.listLayout.lastMonthQcQaLayout.setVisibility(View.GONE);
        }
        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1611)) {
            binding.listLayout.lastMonthMonitoringLayout.setVisibility(View.GONE);
        }
        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1612)) {
            binding.listLayout.monitoringAgencyLayout.setVisibility(View.GONE);
        }
        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1613)) {
            binding.listLayout.monitoringIssueLayout.setVisibility(View.GONE);
        }
        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1633)) {
            binding.listLayout.lastMonthSaleLayout.setVisibility(View.GONE);
        }

        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1634)) {
            binding.listLayout.lastMonthProductionLayout.setVisibility(View.GONE);
        }
        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1635)) {
            binding.listLayout.lastMonthPurchaseLayout.setVisibility(View.GONE);
        }
        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1618)) {
            binding.listLayout.topTenMillerLayout.setVisibility(View.GONE);
        }
        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1619)) {
            binding.listLayout.zoneListLayout.setVisibility(View.GONE);
        }
    }

    private void showPieChart1(Production production) {
        try {
            ArrayList<PieEntry> pieEntries = new ArrayList<>();
            String label = "";

            //initializing data

            String industrial = "Industrial: " + production.getIndustrial();
            String iodization = "Iodized: " + production.getIodized();
            Map<String, Float> typeAmountMap = new HashMap<>();
            typeAmountMap.put(industrial, Float.parseFloat(String.valueOf(production.getIndustrial())));
            String a = String.valueOf(production.getIodized());
            typeAmountMap.put(iodization, Float.parseFloat(a));


            //initializing colors for the entries
            ArrayList<Integer> colors = new ArrayList<>();
            colors.add(Color.parseColor("#D15700"));
            colors.add(Color.parseColor("#782B44"));

            //input data and fit data into pie chart entry
            for (String type : typeAmountMap.keySet()) {
                pieEntries.add(new PieEntry(typeAmountMap.get(type), type));
            }

            //collecting the entries with label name
            PieDataSet pieDataSet = new PieDataSet(pieEntries, label);
            //setting text size of the value
            pieDataSet.setValueTextSize(0f);
            //providing color list for coloring different entries
            pieDataSet.setColors(colors);
            //grouping the data set from entry to chart
            PieData pieData = new PieData(pieDataSet);
            //showing the value of the entries, default true if not set
            pieData.setDrawValues(true);

            binding.pieChartlayout1.pieChartView.setData(pieData);
            binding.pieChartlayout1.pieChartView.invalidate();
            binding.pieChartlayout1.pieChartView.setDescription(null);
            binding.pieChartlayout1.pieChartView.setHoleColor(ContextCompat.getColor(getContext(), R.color.violet_color));
            binding.pieChartlayout1.pieChartView.setHoleRadius(0f);
            binding.pieChartlayout1.pieChartView.setTransparentCircleRadius(0f);
        } catch (Exception e) {
        }
    }

    private void showPieChart(Sales sales) {
        try {
            ArrayList<PieEntry> pieEntries = new ArrayList<>();
            String label = "";
            //initializing data

            String industrial = "Industrial: " + sales.getIndustrial();
            String iodization = "Iodized: " + sales.getIodized();
            Map<String, Float> typeAmountMap = new HashMap<>();
            typeAmountMap.put(industrial, Float.parseFloat(String.valueOf(sales.getIndustrial())));

            typeAmountMap.put(iodization, Float.parseFloat(String.valueOf(sales.getIodized())));




/*
 typeAmountMap.put("Industrial", Integer.parseInt(sales.getIndustrial()));
        typeAmountMap.put("Iodized", Integer.parseInt(sales.getIodized()));
*/


            //initializing colors for the entries
            ArrayList<Integer> colors = new ArrayList<>();
            colors.add(Color.parseColor("#D15700"));
            colors.add(Color.parseColor("#782B44"));


            //input data and fit data into pie chart entry
            for (String type : typeAmountMap.keySet()) {
                pieEntries.add(new PieEntry(typeAmountMap.get(type), type));
            }

            //collecting the entries with label name
            PieDataSet pieDataSet = new PieDataSet(pieEntries, label);
            //setting text size of the value
            pieDataSet.setValueTextSize(0f);

            //providing color list for coloring different entries
            pieDataSet.setColors(colors);
            //grouping the data set from entry to chart
            PieData pieData = new PieData(pieDataSet);
            //showing the value of the entries, default true if not set
            pieData.setDrawValues(true);

            binding.pieChartlayout2.pieChartView.setData(pieData);
            binding.pieChartlayout2.pieChartView.invalidate();
            binding.pieChartlayout2.pieChartView.setDescription(null);
            binding.pieChartlayout2.pieChartView.setHoleColor(ContextCompat.getColor(getContext(), R.color.violet_color));
            binding.pieChartlayout2.pieChartView.setHoleRadius(0f);
            binding.pieChartlayout2.pieChartView.setTransparentCircleRadius(0f);
        } catch (Exception e) {
        }


    }

    private void showPieChart$(LastMonthIodine lastMonthIodine) {
        try {
            ArrayList<PieEntry> pieEntries = new ArrayList<>();
            String label = "";

            //initializing data
            String purchase = "Purchase: " + lastMonthIodine.getPurchase();
            String used = "Used: " + lastMonthIodine.getSell();
            String inStock = "In Stock: " + lastMonthIodine.getInStock();
            String inStock1 = ReplaceCommaFromString.replaceComma(String.valueOf(lastMonthIodine.getInStock()));

            Map<String, Float> typeAmountMap = new HashMap<>();
            typeAmountMap.put(purchase, Float.parseFloat(lastMonthIodine.getPurchase()));
            typeAmountMap.put(used, Float.parseFloat(lastMonthIodine.getSell()));
            typeAmountMap.put(inStock, Float.parseFloat(inStock1));


            //initializing colors for the entries
            ArrayList<Integer> colors = new ArrayList<>();
            colors.add(Color.parseColor("#FF7D7D"));
            colors.add(Color.parseColor("#937DFF"));
            colors.add(Color.parseColor("#83B094"));


            //input data and fit data into pie chart entry
            for (String type : typeAmountMap.keySet()) {
                pieEntries.add(new PieEntry(typeAmountMap.get(type), type));
            }


            //collecting the entries with label name
            PieDataSet pieDataSet = new PieDataSet(pieEntries, label);

            //setting text size of the value
            pieDataSet.setValueTextSize(0f);
            //providing color list for coloring different entries
            pieDataSet.setColors(colors);
            //grouping the data set from entry to chart
            PieData pieData = new PieData(pieDataSet);
            //showing the value of the entries, default true if not set
            pieData.setDrawValues(true);
            binding.listLayout.pieChartlayout3.pieChartView.setData(pieData);
            binding.listLayout.pieChartlayout3.pieChartView.invalidate();
            binding.listLayout.pieChartlayout3.pieChartView.setDescription(null);
            binding.listLayout.pieChartlayout3.pieChartView.setHoleColor(ContextCompat.getColor(getContext(), R.color.violet_color));
            binding.listLayout.pieChartlayout3.pieChartView.setHoleRadius(0f);
            binding.listLayout.pieChartlayout3.pieChartView.setTransparentCircleRadius(0f);

        } catch (Exception e) {
        }
    }


    private void backControl() {
        binding.back.setOnClickListener(v -> getActivity().onBackPressed());
    }

    private void getPeriviousFragmentData() {
        try {
            getArguments().getString("portion");
        } catch (Exception e) {
        }
    }


    private void setClick() {
        binding.totalMill.setOnClickListener(this);
        binding.activeMills.setOnClickListener(this);
        binding.inActiveMills.setOnClickListener(this);
        binding.totalPurchaseLayout.setOnClickListener(this);
        binding.totalProduction.setOnClickListener(this);
        binding.totalSaleLayout.setOnClickListener(this);
        binding.totalIodinePurchaseLayout.setOnClickListener(this);
        binding.totalIodideSaltSale.setOnClickListener(this);
        binding.totalZone.setOnClickListener(this);
        binding.totalDeveloperPartnerLayout.setOnClickListener(this);
        binding.totalGovtAgency.setOnClickListener(this);
        binding.totalConsolidoid.setOnClickListener(this);
        binding.swiprRefresjh.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        if (v.getId() == R.id.totalMill) {
            goToMillHistory(null, "Mill History Lists");
        }


        if (v.getId() == R.id.activeMills) {
            goToMillHistory("1", "Active Mills");
        }


        if (v.getId() == R.id.inActiveMills) {
            goToMillHistory("0", "Inactive Mills");
        }


        if (v.getId() == R.id.totalPurchaseLayout) {
            try {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1278)) {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyy-MM-dd");
                    Date date = new Date();
                    // for first day of current month.
                    LocalDate now = LocalDate.now();
                    LocalDate firstDay = now.with(TemporalAdjusters.firstDayOfMonth());
                    bundle.putString("startDate", String.valueOf(firstDay));
                    bundle.putString("endDate", formatter.format(date));
                    bundle.putString("pageName", "Purchase Report");
                    bundle.putString("portion", ReportUtils.saleDetailsReport);
                    Navigation.findNavController(getView()).navigate(R.id.action_dashboardFragment_to_purchaseReturnListFragment, bundle);
                    return;
                } else {
                    Toasty.info(getContext(), PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }
            } catch (Exception e) {
            }
        }


        if (v.getId() == R.id.totalProduction) {
            if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1535)) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyy-MM-dd");
                Date date = new Date();
                // for first day of current month.
                LocalDate now = LocalDate.now();
                LocalDate firstDay = now.with(TemporalAdjusters.firstDayOfMonth());
                bundle.putString("startDate", String.valueOf(firstDay));
                bundle.putString("endDate", formatter.format(date));
                bundle.putString("pageName", "Production Report");
                bundle.putString("portion", ReportUtils.productionReport);
                Navigation.findNavController(getView()).navigate(R.id.action_dashboardFragment_to_purchaseReturnListFragment, bundle);
                return;
            } else {
                Toasty.info(getContext(), PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
            }
        }


        if (v.getId() == R.id.totalSaleLayout) {
            if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1277)) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyy-MM-dd");
                Date date = new Date();
                // for first day of current month.
                LocalDate now = LocalDate.now();
                LocalDate firstDay = now.with(TemporalAdjusters.firstDayOfMonth());
                bundle.putString("startDate", String.valueOf(firstDay));
                bundle.putString("endDate", formatter.format(date));
                bundle.putString("portion", ReportUtils.processingReport);
                bundle.putString("pageName", "Sale Report");
                Navigation.findNavController(getView()).navigate(R.id.action_dashboardFragment_to_purchaseReturnListFragment, bundle);
                return;
            } else {
                Toasty.info(getContext(), PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
            }
        }


        if (v.getId() == R.id.totalIodinePurchaseLayout) {
            if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1541)) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyy-MM-dd");
                Date date = new Date();
                // for first day of current month.
                LocalDate now = LocalDate.now();
                LocalDate firstDay = now.with(TemporalAdjusters.firstDayOfMonth());
                bundle.putString("startDate", String.valueOf(firstDay));
                bundle.putString("endDate", formatter.format(date));
                bundle.putString("portion", ReportUtils.iodineUsedReport);
                bundle.putString("pageName", "Iodine Used Report");
                Navigation.findNavController(getView()).navigate(R.id.action_dashboardFragment_to_purchaseReturnListFragment, bundle);
                return;
            } else {
                Toasty.info(getContext(), PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
            }
        }


        if (v.getId() == R.id.totalIodideSaltSale) {
            if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1277)) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyy-MM-dd");
                Date date = new Date();
                // for first day of current month.
                LocalDate now = LocalDate.now();
                LocalDate firstDay = now.with(TemporalAdjusters.firstDayOfMonth());
                bundle.putString("startDate", String.valueOf(firstDay));
                bundle.putString("endDate", formatter.format(date));
                bundle.putString("categoryId", "738");
                bundle.putString("portion", ReportUtils.processingReport);
                bundle.putString("pageName", "Sale Report");
                Navigation.findNavController(getView()).navigate(R.id.action_dashboardFragment_to_purchaseReturnListFragment, bundle);
                return;
            } else {
                Toasty.info(getContext(), PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
            }
        }


        if (v.getId() == R.id.totalZone) {
            //   Toast.makeText(getContext(), "Total Zone", Toast.LENGTH_SHORT).show();
        }


        if (v.getId() == R.id.totalDeveloperPartnerLayout) {
            //  Toast.makeText(getContext(), "Total Developer partner", Toast.LENGTH_SHORT).show();
        }


        if (v.getId() == R.id.totalGovtAgency) {
            //  Toast.makeText(getContext(), "Total Government Agency", Toast.LENGTH_SHORT).show();
        }


        if (v.getId() == R.id.totalConsolidoid) {
            try {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1427)) {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyy-MM-dd");
                    Date date = new Date();
                    // for first day of current month.
                    LocalDate now = LocalDate.now();
                    LocalDate firstDay = now.with(TemporalAdjusters.firstDayOfMonth());
                    bundle.putString("startDate", String.valueOf(firstDay));
                    bundle.putString("endDate", formatter.format(date));
                    bundle.putString("portion", MonitoringUtil.monitoringHistory);
                    MonitoringListFragment.isFirstLoad = 0;
                    MonitoringListFragment.pageNumber = 1;
                    manageMonitor = true;
                    Navigation.findNavController(getView()).navigate(R.id.action_dashboardFragment_to_monitoringListFragment, bundle);
                } else {
                    Toasty.info(getContext(), PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }


            } catch (Exception e) {
            }
        }
        if (v.getId() == R.id.swiprRefresjh) {
            getDashBoardData();
        }
    }

    private void goToMillHistory(String s, String pageName) {
        try {
            if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getContext()).getUserCredentials().getPermissions()).contains(1347)) {
                Bundle bundle = new Bundle();
                bundle.putString("porson", MillerUtils.millerHistoryList);
                bundle.putString("status", s);
                bundle.putString("pageName", pageName);
                Navigation.findNavController(getView()).navigate(R.id.action_dashboardFragment_to_millerAllListFragment, bundle);
                return;
            } else {
                Toasty.info(getContext(), "You don't have permission for access this portion", Toasty.LENGTH_LONG).show();
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }
        currentPermissionViewModel.getCurrentUserRealtimePermissions(
                PreferenceManager.getInstance(getActivity()).getUserCredentials().getToken(),
                PreferenceManager.getInstance(getActivity()).getUserCredentials().getUserId()
        ).observe(getViewLifecycleOwner(), response -> {
            if (response == null) {
                Toasty.error(getActivity(), "Something Wrong", Toasty.LENGTH_LONG).show();
                return;
            }
            if (response.getStatus() == 400) {
                Toasty.info(getActivity(), "" + response.getMessage(), Toasty.LENGTH_LONG).show();
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
}
