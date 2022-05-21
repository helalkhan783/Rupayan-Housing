package com.rupayan_housing.utils;

import com.rupayan_housing.R;

import java.util.ArrayList;
import java.util.List;
public class DashBoardReportUtils {
    public static final String iodization = "Last Month Iodization %";
    public static final String iodineStock = "Current Month Iodine Stock";//
    public static final String acQa = "Last Month QC/QA %";//
    public static final String monitoring = "Last Month Monitoring";//
    public static final String agencyMonitoring = "Monitoring (Based on Surveillance Agency)";//
    public static final String issueMonitoring = "Monitoring (Based on Monitoring Issue)";//
    public static final String sale = "Last Month Sale";//
    public static final String production = "Last Month Production";//
    public static final String purchase = "Last Month Purchase";//
    public static final String topTenMiller = "Top 10 Miller (Based on Iodized Salt Sale)";//
    public static final String zoneList = "Zone List Hierarchy (Based on Iodized Salt Sale)";//

    public static List<String> dashBoardReportNameList() {
        List<String> nameList = new ArrayList<>();
        nameList.add(iodization);
        nameList.add(iodineStock);
        nameList.add(acQa);
        nameList.add(monitoring);
        nameList.add(agencyMonitoring);
        nameList.add(issueMonitoring);
        nameList.add(sale);
        nameList.add(production);//
        nameList.add(purchase);//
        nameList.add(topTenMiller);//
        nameList.add(zoneList);
        return nameList;
    }

    public static List<Integer> dashBoardReeportImageList() {
        List<Integer> imageList = new ArrayList<>();
        imageList.add(R.drawable.report_phycal);
        imageList.add(R.drawable.purchase_report);
        imageList.add(R.drawable.purchase_return_report);
        imageList.add(R.drawable.packaging_report);
        imageList.add(R.drawable.packeting_report);
        imageList.add(R.drawable.stock_input_output_repurt);
        imageList.add(R.drawable.sale_history);
        imageList.add(R.drawable.sale_report);
        imageList.add(R.drawable.sale_return_report);//
        imageList.add(R.drawable.qc_qa_history);//
        imageList.add(R.drawable.transfer_history_in);//
        return imageList;
    }
}
