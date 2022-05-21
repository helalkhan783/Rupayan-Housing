package com.rupayan_housing.view.fragment.all_report;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rupayan_housing.R;
import com.rupayan_housing.adapter.DashBoardAgencyMonitoringAdapter;
import com.rupayan_housing.adapter.DashBoardIodizationAdapter;
import com.rupayan_housing.adapter.DashBoardMonitoringAdapter;
import com.rupayan_housing.adapter.DistrictWiseSaleReportAdapter;
import com.rupayan_housing.adapter.IodineReportListAdapter;
import com.rupayan_housing.adapter.IodineStockAdapter;
import com.rupayan_housing.adapter.IssueMonitoringAdapter;
import com.rupayan_housing.adapter.LastMonthQcAaAdapter;
import com.rupayan_housing.adapter.LastMonthSaleAdapter;
import com.rupayan_housing.adapter.MillReportListAdapter;
import com.rupayan_housing.adapter.MonitoringReportAdapter;
import com.rupayan_housing.adapter.PackegingReportListAdapter;
import com.rupayan_housing.adapter.PackettingReportListAdapter;
import com.rupayan_housing.adapter.ProductionReportLisAdapter;
import com.rupayan_housing.adapter.PurchaseReportAdapterByDate;
import com.rupayan_housing.adapter.PurchaseReturnReportListAdapter;
import com.rupayan_housing.adapter.QcQaReportListAdapter;
import com.rupayan_housing.adapter.TopTenMillerAdapter;
import com.rupayan_housing.adapter.ZoneListHierachyAdapter;
import com.rupayan_housing.databinding.FragmentPurchaseReturnListBinding;
import com.rupayan_housing.serverResponseModel.MillReportResponse;
import com.rupayan_housing.serverResponseModel.MillerEmployeeReportRespons;
import com.rupayan_housing.serverResponseModel.PurchaseReportByDateResponse;
import com.rupayan_housing.utils.DashBoardReportUtils;
import com.rupayan_housing.utils.ReportUtils;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.view.fragment.stock.list_adapter.DeclineTransferredListAdapter;
import com.rupayan_housing.viewModel.DashBoardViewModel;
import com.rupayan_housing.viewModel.report_all_view_model.EmployeeReportViewModel;
import com.rupayan_housing.viewModel.report_all_view_model.IodineReportViewModel;
import com.rupayan_housing.view.fragment.all_report.iodine_used_report.list.IodineReportListResponse;
import com.rupayan_housing.viewModel.report_all_view_model.LicenceExpireViewModel;
import com.rupayan_housing.view.fragment.all_report.licence_expire_report.list.MillerLicenceExpireReportListResponse;
import com.rupayan_housing.view.fragment.all_report.lisence_report.LicenceReportViewModel;
import com.rupayan_housing.serverResponseModel.list_response.MillerLicenceReportListResponse;
import com.rupayan_housing.viewModel.report_all_view_model.PackagingViewModel;
import com.rupayan_housing.view.fragment.all_report.packaging_report.list.PacketingReportListResponse;
import com.rupayan_housing.viewModel.report_all_view_model.PacketingReportViewModel;
import com.rupayan_housing.view.fragment.all_report.packeting_report.list.PackegingReportListResponse;
import com.rupayan_housing.adapter.MillerEmployeeReportListAdapter;
import com.rupayan_housing.adapter.MillerLicenceExpireReportListAdapter;
import com.rupayan_housing.adapter.MillerLicenceReportList;
import com.rupayan_housing.view.fragment.all_report.reconcilation_report.ReconciliationReportListAdapter;
import com.rupayan_housing.viewModel.report_all_view_model.ReconciliationReportViewModel;
import com.rupayan_housing.view.fragment.all_report.reconcilation_report.list_response.ReconciliationReportListResponse;
import com.rupayan_housing.view.fragment.all_report.sale_and_purchase_report.sale_report.SaleReportListAdapter;
import com.rupayan_housing.view.fragment.all_report.sale_and_purchase_report.sale_return_report.SaleReturnReportListAdapter;
import com.rupayan_housing.viewModel.report_all_view_model.SaleReturnReportViewModel;
import com.rupayan_housing.viewModel.report_all_view_model.StockIOReportViewModel;
import com.rupayan_housing.view.fragment.all_report.stock_in_out_report.StockIoReportAdapter;
import com.rupayan_housing.viewModel.report_all_view_model.ReportViewModel;
import com.rupayan_housing.viewModel.report_all_view_model.SaleReportViewModel;


public class PurchaseReturnListFragment extends BaseFragment {
    private FragmentPurchaseReturnListBinding binding;
    private String portion, startDate, endDate, supplierId, referrerId, brandId, districId, itemId, storeId, millerprofileId, categoryId, certificateTypeID, selectAssociationId; //get previous fragment
    private ReportViewModel reportViewModel;
    private LicenceReportViewModel licenceReportViewModel;
    private LicenceExpireViewModel licenceExpireViewModel;
    private EmployeeReportViewModel employeeReportViewModel;
    private ReconciliationReportViewModel reconciliationViewModel;
    private SaleReportViewModel saleReportViewModel;
    private SaleReturnReportViewModel saleReturnReportViewModel;
    private DashBoardViewModel dashBoardViewModel;
    private StockIOReportViewModel stockIOReportViewModel;
    private PacketingReportViewModel packetingReportViewModel;
    private PackagingViewModel packagingViewModel;
    private IodineReportViewModel iodineReportViewModel;
    private String selectReconciliation = "", pageName, monitoringType, zone;
    ProgressDialog progressDialog;

    private String transferItemId, fromStoreId, toStoreId, productionId, processTypeIdd, milltTypeId, millStatusId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_purchase_return_list, container, false);

        /** object create for viewModelProvider*/
        reportViewModel = new ViewModelProvider(this).get(ReportViewModel.class);
        licenceReportViewModel = new ViewModelProvider(this).get(LicenceReportViewModel.class);
        licenceExpireViewModel = new ViewModelProvider(this).get(LicenceExpireViewModel.class);
        employeeReportViewModel = new ViewModelProvider(this).get(EmployeeReportViewModel.class);
        reconciliationViewModel = new ViewModelProvider(this).get(ReconciliationReportViewModel.class);
        saleReportViewModel = new ViewModelProvider(this).get(SaleReportViewModel.class);
        saleReturnReportViewModel = new ViewModelProvider(this).get(SaleReturnReportViewModel.class);
        stockIOReportViewModel = new ViewModelProvider(this).get(StockIOReportViewModel.class);
        packetingReportViewModel = new ViewModelProvider(this).get(PacketingReportViewModel.class);
        packagingViewModel = new ViewModelProvider(this).get(PackagingViewModel.class);
        iodineReportViewModel = new ViewModelProvider(this).get(IodineReportViewModel.class);
        dashBoardViewModel = new ViewModelProvider(this).get(DashBoardViewModel.class);

        progressDialog = new ProgressDialog(getContext());

        /** get previous fragment data */
        getPreviousFragmentData();
        binding.toolbar.toolbarTitle.setText(pageName);
        /** back control*/
        binding.toolbar.setClickHandle(() -> {
            hideKeyboard(getActivity());
            getActivity().onBackPressed();
        });
/**
 * all Report List
 */
        allReportListData();


        binding.downloadBtn.setOnClickListener(v -> {

        });

        return binding.getRoot();
    }

    private void allReportListData() {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }
        progressDialog.show();
        if (portion.equals(ReportUtils.noteSheetGenerateReport)) {
            purchaseReturnReportList();
            return;
        }
        if (portion.equals(ReportUtils.saleDetailsReport)) {
            getPurchaseReportDataFromViewModelByDate();
            return;
        }
        if (portion.equals(ReportUtils.processingReport)) {
            getSaleReportListFromViewModel();
            return;
        }
        if (portion.equals(ReportUtils.availAbleReport)) {
            getSaleReturnReportList();
            return;
        } if (portion.equals(ReportUtils.projectWiseItemReport)) {
            getSaleReturnReportList();
            return;
        }
        if (portion.equals(ReportUtils.inventoryReport)) {
            getDistrictWiseSaleReport();
            return;
        }
        if (portion.equals(ReportUtils.customerReport)) {
            getDataFromLicenceReportViewModel();
            return;
        }
        if (portion.equals(ReportUtils.stockInOutReport)) {
            binding.toolbar.toolbarTitle.setText("Inventory Report");
            getStockIOList();
            return;
        }
        if (portion.equals(ReportUtils.userReport)) {
            getDataFromLicenceExpireViewModel();
            return;
        }
        if (portion.equals(ReportUtils.employeeReport)) {
            getDataFromEmployeeReportViewModel();
            return;
        }

        if (portion.equals(ReportUtils.projectReport)) {
            getMillReport();
            return;
        }
        if (portion.equals(ReportUtils.reconciliation)) {
            getDataFromReconciliationViewModel();
            return;
        }
        /** for Packaging Report */
        if (portion.equals(ReportUtils.PacketingReport)) {
            getPackegingReportListFromViewModel();
            return;
        }

        /** for Packaging Report */
        if (portion.equals(ReportUtils.productionReport)) {
            getProductionReport();
            return;
        }
        /** for packeting Report */
        if (portion.equals(ReportUtils.packagingReport)) {
            getPacketingReportData();
            return;
        }

        /** for iodine Report */
        if (portion.equals(ReportUtils.iodineUsedReport)) {
            getIodineReportList();
            return;
        }

        if (portion.equals(ReportUtils.qcqaReport)) {
            getQCQAList();
            return;
        }

        if (portion.equals(ReportUtils.monitoringReport)) {
            getMonitoringReportLst();
            return;
        }
        if (portion.equals(ReportUtils.transferReport)) {
            getTransferReportData();
            return;
        }

        //dash board report start here

        if (portion.equals(DashBoardReportUtils.iodization)) {
            getIodizationData();
            return;
        }
        if (portion.equals(DashBoardReportUtils.iodineStock)) {
            getIodineStock();
            return;
        }
        if (portion.equals(DashBoardReportUtils.acQa)) {
            getQcQaDashboardReport();
            return;
        }
        if (portion.equals(DashBoardReportUtils.monitoring)) {
            getDashBoardMonitoring();
            return;
        }

        if (portion.equals(DashBoardReportUtils.agencyMonitoring)) {
            getAgencyMonitoring();
            return;
        }
        if (portion.equals(DashBoardReportUtils.issueMonitoring)) {
            getIssueMonitoring();
            return;
        }
        if (portion.equals(DashBoardReportUtils.sale)) {
            //  getIodineStock();
            getSaleReportDashboard("Sale Qty");
            return;
        }
        if (portion.equals(DashBoardReportUtils.production)) {
            getSaleReportDashboard("Production Qty");
            return;
        }
        if (portion.equals(DashBoardReportUtils.purchase)) {
            getSaleReportDashboard("Purchase Qty");
            return;
        }
        if (portion.equals(DashBoardReportUtils.topTenMiller)) {
            getTopTenMiller();
            return;
        }
        if (portion.equals(DashBoardReportUtils.zoneList)) {
            getZoneList();
            return;
        }
    }

    private void getMillReport() {
        employeeReportViewModel.getMillReportList(getActivity(), startDate,endDate,selectAssociationId, millerprofileId, processTypeIdd, milltTypeId, millStatusId).observe(getViewLifecycleOwner(), new Observer<MillReportResponse>() {
            @Override
            public void onChanged(MillReportResponse response) {
                progressDialog.dismiss();
                if (response == null) {
                    errorMessage(getActivity().getApplication(), "something wrong");
                    return;
                }
                if (response.getStatus() == 400) {
                    errorMessage(getActivity().getApplication(), response.getMessage());
                    return;
                }
                if (response.getStatus() == 500) {
                    errorMessage(getActivity().getApplication(), response.getMessage());
                    return;
                }
                if (response.getList().isEmpty()) {
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.textView.setVisibility(View.VISIBLE);
                    return;
                }

                MillReportListAdapter adapter = new MillReportListAdapter(getActivity(), response.getList());
                binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.recyclerView.setAdapter(adapter);
            }
        });
    }

    private void getZoneList() {
        dashBoardViewModel.janinaList(getActivity(), startDate, endDate, zone).observe(getViewLifecycleOwner(), response -> {
            progressDialog.dismiss();
            if (response == null) {
                errorMessage(getActivity().getApplication(), "something wrong");
            }
            if (response.getStatus() == 400) {
                errorMessage(getActivity().getApplication(), response.getMessage());
            }
            if (response.getStatus() == 200) {
                if (response.getList().isEmpty()) {
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.textView.setVisibility(View.VISIBLE);
                    return;
                } else {
                  binding.downloadBtn.setVisibility(View.VISIBLE);

                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.recyclerView.setHasFixedSize(true);
                    ZoneListHierachyAdapter adapter = new ZoneListHierachyAdapter(getActivity(), response.getList());
                    binding.recyclerView.setAdapter(adapter);

                }
            }
        });

    }

    private void getTopTenMiller() {
        dashBoardViewModel.topTenMillList(getActivity(), startDate, endDate, zone).observe(getViewLifecycleOwner(), response -> {
            progressDialog.dismiss();
            if (response == null) {
                errorMessage(getActivity().getApplication(), "something wrong");
            }
            if (response.getStatus() == 400) {
                errorMessage(getActivity().getApplication(), response.getMessage());
            }
            if (response.getStatus() == 200) {

                if (response.getList().isEmpty()) {
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.textView.setVisibility(View.VISIBLE);
                    return;
                } else {
                    binding.downloadBtn.setVisibility(View.VISIBLE);

                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.recyclerView.setHasFixedSize(true);
                    TopTenMillerAdapter adapter = new TopTenMillerAdapter(getActivity(), response.getList());
                    binding.recyclerView.setAdapter(adapter);

                }
            }
        });

    }

    private void getSaleReportDashboard(String name) {
        dashBoardViewModel.lastMonthsale(getActivity(), startDate, endDate).observe(getViewLifecycleOwner(), response -> {
            progressDialog.dismiss();
            if (response == null) {
                errorMessage(getActivity().getApplication(), "something wrong");
            }
            if (response.getStatus() == 400) {
                errorMessage(getActivity().getApplication(), response.getMessage());
            }
            if (response.getStatus() == 200) {

                if (response.getList().isEmpty()) {
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.textView.setVisibility(View.VISIBLE);
                    return;
                } else {
                    binding.downloadBtn.setVisibility(View.VISIBLE);

                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.recyclerView.setHasFixedSize(true);
                    LastMonthSaleAdapter adapter = new LastMonthSaleAdapter(getActivity(), response.getList(), name);
                    binding.recyclerView.setAdapter(adapter);

                }
            }
        });

    }

    private void getIssueMonitoring() {
        dashBoardViewModel.issueMonitoringList(getActivity(), startDate, endDate).observe(getViewLifecycleOwner(), response -> {
            progressDialog.dismiss();
            if (response == null) {
                errorMessage(getActivity().getApplication(), "something wrong");
            }
            if (response.getStatus() == 400) {
                errorMessage(getActivity().getApplication(), response.getMessage());
            }
            if (response.getStatus() == 200) {
                if (response.getList().isEmpty()) {
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.textView.setVisibility(View.VISIBLE);
                    return;
                } else {
                    binding.downloadBtn.setVisibility(View.VISIBLE);

                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.recyclerView.setHasFixedSize(true);
                    IssueMonitoringAdapter adapter = new IssueMonitoringAdapter(getActivity(), response.getList());
                    binding.recyclerView.setAdapter(adapter);

                }
            }
        });

    }

    private void getAgencyMonitoring() {
        dashBoardViewModel.agencyMonitoringlist(getActivity(), startDate, endDate).observe(getViewLifecycleOwner(), response -> {
            progressDialog.dismiss();
            if (response == null) {
                errorMessage(getActivity().getApplication(), "something wrong");
            }
            if (response.getStatus() == 400) {
                errorMessage(getActivity().getApplication(), response.getMessage());
            }
            if (response.getStatus() == 200) {

                if (response.getList().isEmpty()) {
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.textView.setVisibility(View.VISIBLE);
                    return;
                } else {
                    binding.downloadBtn.setVisibility(View.VISIBLE);

                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.recyclerView.setHasFixedSize(true);
                    DashBoardAgencyMonitoringAdapter adapter = new DashBoardAgencyMonitoringAdapter(getActivity(), response.getList());
                    binding.recyclerView.setAdapter(adapter);

                }
            }
        });

    }

    private void getDashBoardMonitoring() {
        dashBoardViewModel.zoneWiseMonitoringList(getActivity(), startDate, endDate, zone).observe(getViewLifecycleOwner(), response -> {
            progressDialog.dismiss();
            if (response == null) {
                errorMessage(getActivity().getApplication(), "something wrong");
            }
            if (response.getStatus() == 400) {
                errorMessage(getActivity().getApplication(), response.getMessage());
            }
            if (response.getStatus() == 200) {

                if (response.getList().isEmpty()) {
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.textView.setVisibility(View.VISIBLE);
                    return;
                } else {
                    binding.downloadBtn.setVisibility(View.VISIBLE);

                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.recyclerView.setHasFixedSize(true);
                    DashBoardMonitoringAdapter adapter = new DashBoardMonitoringAdapter(getActivity(), response.getList());
                    binding.recyclerView.setAdapter(adapter);

                }
            }
        });

    }

    private void getQcQaDashboardReport() {
        dashBoardViewModel.lastMontQCQA(getActivity(), startDate, endDate, zone).observe(getViewLifecycleOwner(), response -> {
            progressDialog.dismiss();
            if (response == null) {
                errorMessage(getActivity().getApplication(), "something wrong");
            }
            if (response.getStatus() == 400) {
                errorMessage(getActivity().getApplication(), response.getMessage());
            }
            if (response.getStatus() == 200) {

                if (response.getList().isEmpty()) {
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.textView.setVisibility(View.VISIBLE);
                    return;
                } else {
                    binding.downloadBtn.setVisibility(View.VISIBLE);

                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.recyclerView.setHasFixedSize(true);
                    LastMonthQcAaAdapter adapter = new LastMonthQcAaAdapter(getActivity(), response.getList());
                    binding.recyclerView.setAdapter(adapter);
                }
            }
        });

    }

    private void getIodineStock() {
        dashBoardViewModel.lastMontStock(getActivity(), startDate, endDate, zone).observe(getViewLifecycleOwner(), response -> {
            progressDialog.dismiss();
            if (response == null) {
                errorMessage(getActivity().getApplication(), "something wrong");
            }
            if (response.getStatus() == 400) {
                errorMessage(getActivity().getApplication(), response.getMessage());
            }
            if (response.getStatus() == 200) {

                if (response.getList().isEmpty()) {
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.textView.setVisibility(View.VISIBLE);
                    return;
                } else {
                    binding.downloadBtn.setVisibility(View.VISIBLE);

                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.recyclerView.setHasFixedSize(true);
                    IodineStockAdapter adapter = new IodineStockAdapter(getActivity(), response.getList());
                    binding.recyclerView.setAdapter(adapter);

                }
            }
        });

    }

    private void getIodizationData() {
        dashBoardViewModel.lastMontIodizationList(getActivity(), startDate, endDate, zone).observe(getViewLifecycleOwner(), response -> {
            progressDialog.dismiss();
            if (response == null) {
                errorMessage(getActivity().getApplication(), "something wrong");
            }
            if (response.getStatus() == 400) {
                errorMessage(getActivity().getApplication(), response.getMessage());
            }
            if (response.getStatus() == 200) {

                if (response.getList().isEmpty()) {
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.textView.setVisibility(View.VISIBLE);
                    return;
                } else {
                    binding.downloadBtn.setVisibility(View.VISIBLE);
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.recyclerView.setHasFixedSize(true);
                    DashBoardIodizationAdapter adapter = new DashBoardIodizationAdapter(getActivity(), response.getList());
                    binding.recyclerView.setAdapter(adapter);

                }
            }
        });
    }

    private void getDistrictWiseSaleReport() {
        saleReturnReportViewModel.getDistrictWiseSaleReport(getActivity(), startDate, endDate, selectAssociationId, millerprofileId, districId).observe(getViewLifecycleOwner(), response -> {
            progressDialog.dismiss();
            if (response == null) {
                errorMessage(getActivity().getApplication(), "something wrong");
            }
            if (response.getStatus() == 400) {
                errorMessage(getActivity().getApplication(), response.getMessage());
            }
            if (response.getStatus() == 200) {

                if (response.getSaleList().isEmpty()) {
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.textView.setVisibility(View.VISIBLE);
                    return;
                } else {
                    binding.downloadBtn.setVisibility(View.VISIBLE);

                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.recyclerView.setHasFixedSize(true);
                    DistrictWiseSaleReportAdapter adapter = new DistrictWiseSaleReportAdapter(getActivity(), response.getSaleList());
                    binding.recyclerView.setAdapter(adapter);

                }
            }
        });

    }

    private void getProductionReport() {
        packetingReportViewModel.getProductionList(getActivity(), startDate, endDate, selectAssociationId, millerprofileId, storeId, productionId).observe(getViewLifecycleOwner(), response -> {
            progressDialog.dismiss();
            if (response == null) {
                errorMessage(getActivity().getApplication(), "something wrong");
                return;
            }
            if (response.getStatus() == 400) {
                infoMessage(requireActivity().getApplication(), response.getMessage());
                return;
            }
            if (response.getStatus() == 200) {
                if (response.getProfuctList() == null || response.getProfuctList().isEmpty()) {
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.textView.setVisibility(View.VISIBLE);
                    return;
                } else {
                    binding.downloadBtn.setVisibility(View.VISIBLE);

                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.recyclerView.setHasFixedSize(true);

                    ProductionReportLisAdapter adapter = new ProductionReportLisAdapter(getActivity(), response.getProfuctList());
                    binding.recyclerView.setAdapter(adapter);

                }
            }
        });

    }

    private void getTransferReportData() {
        reconciliationViewModel.transferReportList(getActivity(), fromStoreId, toStoreId, startDate, endDate, transferItemId).observe(
                getViewLifecycleOwner(), response -> {
                    progressDialog.dismiss();
                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "something wrong");
                        return;
                    }
                    if (response.getStatus() == 400) {
                        infoMessage(requireActivity().getApplication(), response.getMessage());
                        return;
                    }
                    if (response.getStatus() == 200) {
                        if (response.getTransferedList() == null || response.getTransferedList().isEmpty()) {
                            binding.recyclerView.setVisibility(View.GONE);
                            binding.textView.setVisibility(View.VISIBLE);
                            return;
                        } else {
                            binding.downloadBtn.setVisibility(View.VISIBLE);

                            binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            binding.recyclerView.setHasFixedSize(true);

                            String manageView = "reportList";
                            DeclineTransferredListAdapter adapter = new DeclineTransferredListAdapter(getActivity(), response.getTransferedList(), manageView);
                            binding.recyclerView.setAdapter(adapter);

                        }
                    }

                }
        );
    }

    private void getMonitoringReportLst() {
        licenceExpireViewModel.monitoringReportList(getActivity(), startDate, endDate, zone, monitoringType).observe(getViewLifecycleOwner(),
                response -> {
                    progressDialog.dismiss();
                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "something wrong");
                        return;
                    }
                    if (response.getStatus() == 400) {
                        infoMessage(requireActivity().getApplication(), response.getMessage());
                        return;
                    }
                    if (response.getStatus() == 200) {
                        if (response.getMonitoringList() == null || response.getMonitoringList().isEmpty()) {
                            binding.recyclerView.setVisibility(View.GONE);
                            binding.textView.setVisibility(View.VISIBLE);
                            return;
                        } else {
                            binding.downloadBtn.setVisibility(View.VISIBLE);

                            binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            binding.recyclerView.setHasFixedSize(true);
                            MonitoringReportAdapter adapter = new MonitoringReportAdapter(getActivity(), response.getMonitoringList());
                            binding.recyclerView.setAdapter(adapter);

                        }
                    }

                });
    }

    private void getQCQAList() {
        licenceExpireViewModel.qcQaReportList(getActivity(), startDate, endDate, selectAssociationId, millerprofileId).observe(getViewLifecycleOwner(),
                response -> {
                    progressDialog.dismiss();
                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "something wrong");
                        return;
                    }
                    if (response.getStatus() == 400) {
                        infoMessage(requireActivity().getApplication(), response.getMessage());
                        return;
                    }
                    if (response.getStatus() == 200) {
                        if (response.getQcqaList() == null || response.getQcqaList().isEmpty()) {
                            binding.recyclerView.setVisibility(View.GONE);
                            binding.textView.setVisibility(View.VISIBLE);
                            return;
                        } else {
                            binding.downloadBtn.setVisibility(View.VISIBLE);

                            binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            binding.recyclerView.setHasFixedSize(true);

                            QcQaReportListAdapter adapter = new QcQaReportListAdapter(getActivity(), response.getQcqaList());
                            binding.recyclerView.setAdapter(adapter);

                        }
                    }

                });
    }

    private void getDataFromReconciliationViewModel() {
        reconciliationViewModel.getReconciliationReportList(getActivity(), selectReconciliation, startDate, endDate,
                selectAssociationId, millerprofileId, storeId, brandId, itemId).observe(getViewLifecycleOwner(), new Observer<ReconciliationReportListResponse>() {
            @Override
            public void onChanged(ReconciliationReportListResponse response) {
                progressDialog.dismiss();
                if (response == null) {
                    errorMessage(getActivity().getApplication(), "something wrong");
                    return;
                }
                if (response.getStatus() == 400) {
                    errorMessage(requireActivity().getApplication(), response.getMessage());
                    return;
                }
                if (response.getStatus() == 200) {
                    if (response.getProfuctList().isEmpty()) {
                        binding.recyclerView.setVisibility(View.GONE);
                        binding.textView.setVisibility(View.VISIBLE);
                        return;
                    } else {
                        binding.downloadBtn.setVisibility(View.VISIBLE);

                        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        binding.recyclerView.setHasFixedSize(true);
                        ReconciliationReportListAdapter adapter = new ReconciliationReportListAdapter(getActivity(), response.getProfuctList());
                        binding.recyclerView.setAdapter(adapter);

                    }
                }
            }
        });

    }

    private void getPurchaseReportDataFromViewModelByDate() {
        reportViewModel.getPurchaseReportByDate(getActivity(), startDate, endDate, selectAssociationId, brandId, millerprofileId, categoryId, supplierId, storeId).observe(getViewLifecycleOwner(), new Observer<PurchaseReportByDateResponse>() {
            @Override
            public void onChanged(PurchaseReportByDateResponse response) {
                progressDialog.dismiss();
                if (response == null) {
                    errorMessage(getActivity().getApplication(), "something wrong");
                    return;
                }
                if (response.getStatus() == 400) {
                    errorMessage(getActivity().getApplication(), "" + response.getMessage());
                    return;
                }
                if (response.getStatus() == 200) {
                    if (response.getProfuctList().isEmpty()) {
                        binding.recyclerView.setVisibility(View.GONE);
                        binding.textView.setVisibility(View.VISIBLE);
                        return;
                    } else {
                        binding.downloadBtn.setVisibility(View.VISIBLE);
                        PurchaseReportAdapterByDate adapter = new PurchaseReportAdapterByDate(getActivity(), response.getProfuctList());
                        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        binding.recyclerView.setAdapter(adapter);
                    }
                }

            }
        });
    }

    private void getSaleReportListFromViewModel() {
        saleReportViewModel.getSaleReportList(getActivity(), startDate, endDate, selectAssociationId,
                millerprofileId, supplierId, storeId, brandId, categoryId).observe(getViewLifecycleOwner(), response -> {
            progressDialog.dismiss();
            if (response == null) {
                errorMessage(getActivity().getApplication(), "Something wrong");
                return;
            }
            if (response.getStatus() == 400) {
                errorMessage(requireActivity().getApplication(), response.getMessage());
                return;
            }

            if (response.getProfuctList().isEmpty()) {
                binding.recyclerView.setVisibility(View.GONE);
                binding.textView.setVisibility(View.VISIBLE);
                return;
            }
            if (response.getStatus() == 200) {
                binding.downloadBtn.setVisibility(View.VISIBLE);
                binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.recyclerView.setHasFixedSize(true);
                SaleReportListAdapter adapter = new SaleReportListAdapter(getContext(), response.getProfuctList());
                binding.recyclerView.setAdapter(adapter);
            }
        });
    }

    private void getSaleReturnReportList() {
        saleReturnReportViewModel.saleReturnReportList(getActivity(), startDate, endDate, selectAssociationId,
                millerprofileId, null, supplierId, brandId, itemId).observe(getViewLifecycleOwner(), response -> {
            progressDialog.dismiss();
            if (response == null) {
                errorMessage(getActivity().getApplication(), "something wrong");
            }
            if (response.getStatus() == 400) {
                errorMessage(getActivity().getApplication(), response.getMessage());
            }
            if (response.getStatus() == 200) {

                if (response.getProfuctList().isEmpty()) {
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.textView.setVisibility(View.VISIBLE);
                    return;
                } else {
                    binding.downloadBtn.setVisibility(View.VISIBLE);
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.recyclerView.setHasFixedSize(true);
                    SaleReturnReportListAdapter adapter = new SaleReturnReportListAdapter(getActivity(), response.getProfuctList());
                    binding.recyclerView.setAdapter(adapter);

                }
            }
        });
    }

    private void getDataFromEmployeeReportViewModel() {
        employeeReportViewModel.getEmployeeReportList(getActivity(), selectAssociationId, millerprofileId).observe(getViewLifecycleOwner(), new Observer<MillerEmployeeReportRespons>() {
            @Override
            public void onChanged(MillerEmployeeReportRespons response) {
                progressDialog.dismiss();
                if (response == null) {
                    errorMessage(getActivity().getApplication(), "something wrong");
                    return;
                }
                if (response.getStatus() == 400) {
                    errorMessage(requireActivity().getApplication(), response.getMessage());
                    return;
                }
                if (response.getStatus() == 200) {
                    if (response.getProfuctList().isEmpty()) {
                        binding.recyclerView.setVisibility(View.GONE);
                        binding.textView.setVisibility(View.VISIBLE);
                        return;
                    } else {
                        binding.downloadBtn.setVisibility(View.VISIBLE);
                        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        binding.recyclerView.setHasFixedSize(true);
                        MillerEmployeeReportListAdapter adapter = new MillerEmployeeReportListAdapter(getActivity(), response.getProfuctList());
                        binding.recyclerView.setAdapter(adapter);
                    }
                }
            }
        });


    }

    private void getDataFromLicenceExpireViewModel() {
        licenceExpireViewModel.getMillerLicenceExpireReportList(getActivity(), startDate, endDate, selectAssociationId, millerprofileId, certificateTypeID).observe(getViewLifecycleOwner(), new Observer<MillerLicenceExpireReportListResponse>() {
            @Override
            public void onChanged(MillerLicenceExpireReportListResponse response) {
                progressDialog.dismiss();
                if (response == null) {
                    errorMessage(getActivity().getApplication(), "something wrong");
                    return;
                }
                if (response.getStatus() == 400) {
                    errorMessage(requireActivity().getApplication(), response.getMessage());
                    return;
                }
                if (response.getStatus() == 200) {
                    if (response.getExpireList().isEmpty()) {
                        binding.recyclerView.setVisibility(View.GONE);
                        binding.textView.setVisibility(View.VISIBLE);
                        return;
                    } else {
                        binding.downloadBtn.setVisibility(View.VISIBLE);

                        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        binding.recyclerView.setHasFixedSize(true);
                        MillerLicenceExpireReportListAdapter adapter = new MillerLicenceExpireReportListAdapter(getActivity(), response.getExpireList());
                        binding.recyclerView.setAdapter(adapter);
                    }
                }
            }
        });

    }

    private void getDataFromLicenceReportViewModel() {
        licenceReportViewModel.licenceReportList(getActivity(), startDate, endDate, selectAssociationId, millerprofileId, certificateTypeID).observe(getViewLifecycleOwner(), new Observer<MillerLicenceReportListResponse>() {
            @Override
            public void onChanged(MillerLicenceReportListResponse response) {
                progressDialog.dismiss();
                if (response == null) {
                    errorMessage(getActivity().getApplication(), "something wrong");
                    return;
                }
                if (response.getStatus() == 400) {
                    errorMessage(requireActivity().getApplication(), response.getMessage());
                    return;
                }
                if (response.getStatus() == 200) {
                    if (response.getProfuctList().isEmpty()) {
                        binding.recyclerView.setVisibility(View.GONE);
                        binding.textView.setVisibility(View.VISIBLE);
                        return;
                    } else {
                        binding.downloadBtn.setVisibility(View.VISIBLE);

                        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        binding.recyclerView.setHasFixedSize(true);
                        MillerLicenceReportList adapter = new MillerLicenceReportList(getActivity(), response.getProfuctList());
                        binding.recyclerView.setAdapter(adapter);
                    }
                }
            }
        });
    }

    private void getStockIOList() {
        stockIOReportViewModel.getStockIOReportList(getActivity(),
                startDate, endDate, selectAssociationId, millerprofileId,
                storeId, supplierId, brandId, itemId).observe(getViewLifecycleOwner(), response -> {
            progressDialog.dismiss();
            if (response == null) {
                errorMessage(getActivity().getApplication(), "something wrong");
                return;
            }
            if (response.getStatus() == 400) {
                errorMessage(getActivity().getApplication(), response.getMessage());
                return;
            }
            if (response.getStatus() == 200) {
                if (response.getStockReport().isEmpty()) {
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.textView.setVisibility(View.VISIBLE);
                    return;
                } else {
                    binding.downloadBtn.setVisibility(View.VISIBLE);
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.recyclerView.setHasFixedSize(true);
                    StockIoReportAdapter adapter = new StockIoReportAdapter(getActivity(), response.getStockReport());
                    binding.recyclerView.setAdapter(adapter);

                }
            }

        });
    }

    private void purchaseReturnReportList() {
        reportViewModel.getPurchaseReturnReportList(getActivity(), startDate
                , endDate, selectAssociationId, brandId, millerprofileId, categoryId, supplierId, storeId).observe(getViewLifecycleOwner(),
                response -> {
                    progressDialog.dismiss();
                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "something wrong");
                        return;
                    }
                    if (response.getStatus() == 400) {
                        errorMessage(getActivity().getApplication(), response.getMessage());
                        return;
                    }
                    if (response.getStatus() == 200) {
                        if (response.getProfuctList().isEmpty()) {
                            binding.recyclerView.setVisibility(View.GONE);
                            binding.textView.setVisibility(View.VISIBLE);
                            return;
                        }
                        binding.downloadBtn.setVisibility(View.VISIBLE);
                        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        binding.recyclerView.setHasFixedSize(true);
                        PurchaseReturnReportListAdapter adapter = new PurchaseReturnReportListAdapter(getActivity(), response.getProfuctList());
                        binding.recyclerView.setAdapter(adapter);
                    }
                });
    }

    private void getIodineReportList() {
        iodineReportViewModel.getIodineReportList(getActivity(), startDate, endDate, selectAssociationId, millerprofileId, storeId).observe(getViewLifecycleOwner(), new Observer<IodineReportListResponse>() {
            @Override
            public void onChanged(IodineReportListResponse response) {
                progressDialog.dismiss();
                if (response == null) {
                    errorMessage(getActivity().getApplication(), "something wrong");
                    return;
                }
                if (response.getStatus() == 400) {
                    infoMessage(requireActivity().getApplication(), response.getMessage());
                    return;
                }
                if (response.getStatus() == 200) {
                    if (response.getIodineReport() == null || response.getIodineReport().isEmpty()) {
                        binding.recyclerView.setVisibility(View.GONE);
                        binding.textView.setVisibility(View.VISIBLE);
                        return;
                    } else {
                        binding.downloadBtn.setVisibility(View.VISIBLE);

                        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        binding.recyclerView.setHasFixedSize(true);
                        IodineReportListAdapter adapter = new IodineReportListAdapter(getActivity(), response.getIodineReport());
                        binding.recyclerView.setAdapter(adapter);

                    }
                }
            }
        });
    }

    private void getPacketingReportData() {
        packetingReportViewModel.gePacketingReportList(getActivity(), startDate, endDate, selectAssociationId, millerprofileId, storeId, referrerId).observe(getViewLifecycleOwner(), new Observer<PacketingReportListResponse>() {
            @Override
            public void onChanged(PacketingReportListResponse response) {
                progressDialog.dismiss();
                if (response == null) {
                    errorMessage(getActivity().getApplication(), "something wrong");
                    return;
                }
                if (response.getStatus() == 400) {
                    infoMessage(requireActivity().getApplication(), response.getMessage());
                    return;
                }
                if (response.getStatus() == 200) {
                    if (response.getPacketingList() == null || response.getPacketingList().isEmpty()) {
                        binding.recyclerView.setVisibility(View.GONE);
                        binding.textView.setVisibility(View.VISIBLE);
                        return;
                    } else {
                        binding.downloadBtn.setVisibility(View.VISIBLE);

                        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        binding.recyclerView.setHasFixedSize(true);
                        PackettingReportListAdapter adapter = new PackettingReportListAdapter(getActivity(), response.getPacketingList());
                        binding.recyclerView.setAdapter(adapter);
                    }
                }
            }
        });

    }

    private void getPackegingReportListFromViewModel() {
        packetingReportViewModel.getPackegingList(getActivity(), startDate, endDate, selectAssociationId, millerprofileId, storeId, referrerId).observe(getViewLifecycleOwner(), new Observer<PackegingReportListResponse>() {
            @Override
            public void onChanged(PackegingReportListResponse response) {
                progressDialog.dismiss();
                if (response == null) {
                    errorMessage(getActivity().getApplication(), "something wrong");
                    return;
                }
                if (response.getStatus() == 400) {
                    infoMessage(requireActivity().getApplication(), response.getMessage());
                    return;
                }
                if (response.getStatus() == 200) {
                    if (response.getPackegingList() == null || response.getPackegingList().isEmpty()) {
                        binding.recyclerView.setVisibility(View.GONE);
                        binding.textView.setVisibility(View.VISIBLE);
                        return;
                    } else {
                        binding.downloadBtn.setVisibility(View.VISIBLE);

                        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        binding.recyclerView.setHasFixedSize(true);
                        PackegingReportListAdapter adapter = new PackegingReportListAdapter(getActivity(), response.getPackegingList());
                        binding.recyclerView.setAdapter(adapter);

                    }
                }
            }
        });
    }


    private void getPreviousFragmentData() {
        assert getArguments() != null;
        startDate = getArguments().getString("startDate");
        endDate = getArguments().getString("endDate");
        portion = getArguments().getString("portion");
        supplierId = getArguments().getString("supplierId");
        brandId = getArguments().getString("brandId");
        districId = getArguments().getString("districId");
        millerprofileId = getArguments().getString("millerProfileId");
        categoryId = getArguments().getString("categoryId");
        selectAssociationId = getArguments().getString("selectAssociationId");
        certificateTypeID = getArguments().getString("certificateTypeID");
        portion = getArguments().getString("portion");
        itemId = getArguments().getString("itemId");
        storeId = getArguments().getString("storeId");
        pageName = getArguments().getString("pageName");
        referrerId = getArguments().getString("referrerId");
        selectReconciliation = getArguments().getString("selectReconciliation");
        monitoringType = getArguments().getString("monitoringType");
        zone = getArguments().getString("zone");
        //for transfer report
        fromStoreId = getArguments().getString("fromStoreId");
        toStoreId = getArguments().getString("toStoreId");
        transferItemId = getArguments().getString("transferItemId");
        productionId = getArguments().getString("productionId");


        // for mill report
        processTypeIdd = getArguments().getString("processTypeId");
        milltTypeId = getArguments().getString("milltTypeId");
        millStatusId = getArguments().getString("millStatusId");


        String selectedStoreName, millerSelectedName, selectedAssociationName, customerName;
        // for show report information
        selectedAssociationName = getArguments().getString("selectedAssociationName");
        selectedStoreName = getArguments().getString("selectedStoreName");
        millerSelectedName = getArguments().getString("millerSelectedName");
        customerName = getArguments().getString("customerName");

        if (startDate.isEmpty() || startDate == null) {
            binding.dateANdTimeLayout.setVisibility(View.GONE);
        }

        binding.startDate.setText(" " + startDate);
        binding.endDatetDate.setText(endDate);

        if (selectedAssociationName != null) {
            binding.association.setVisibility(View.VISIBLE);
            binding.association.setText("Association : " + selectedAssociationName + ";");
        }
        if (millerSelectedName != null) {
            binding.miller.setVisibility(View.VISIBLE);

            binding.miller.setText(" Mill : " + millerSelectedName);
        }
        if (selectedStoreName != null) {
            binding.storeName.setVisibility(View.VISIBLE);

            binding.storeName.setText("; Store : " + selectedStoreName);
        }

        if (portion.equals(ReportUtils.noteSheetGenerateReport) || portion.equals(ReportUtils.noteSheetGenerateReport)) {
            if (customerName != null) {
                binding.customer.setVisibility(View.VISIBLE);
                binding.customer.setText(" Supplier : " + customerName);
            }
        }
        if (portion.equals(ReportUtils.availAbleReport) || portion.equals(ReportUtils.processingReport)) {
            if (customerName != null) {
                binding.customer.setVisibility(View.VISIBLE);
                binding.customer.setText(" Customer : " + customerName);
            }
        }
    }
}