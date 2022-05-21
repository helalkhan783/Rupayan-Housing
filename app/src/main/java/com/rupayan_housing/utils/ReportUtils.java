package com.rupayan_housing.utils;

import com.rupayan_housing.R;

import java.util.ArrayList;
import java.util.List;

public class ReportUtils {
    public static final String saleDetailsReport = "Sales Details Report";//
    public static final String salesSummeryReport = "Sales Summery Report";//
    public static final String noteSheetGenerateReport = "Note Sheet Generate";//
    public static final String processingReport = "Processing Report";//
    public static final String availAbleReport = "Available Report";
    public static final String inventoryReport = "Inventory Report";//
    public static final String stockInOutReport = "Stock I/O Report";//
    public static final String projectReport = "Project Report";
    public static final String projectWiseItemReport = "Project Wise Item Report";
    public static final String customerReport = "Customer Report";
    public static final String userReport = "User Report";


    public static final String employeeReport = "Miller Employee";
    public static final String transferReport = "Transfer Report";
    public static final String reconciliation = "Reconciliation Report";
    public static final String productionReport = "Production Report";
    public static final String PacketingReport = "Packaging Report";
    public static final String packagingReport = "Cartoning Report";
    public static final String iodineUsedReport = "Iodine Used Report";
    public static final String qcqaReport = "QC/QA Report";
    public static final String monitoringReport = "Monitoring Report";

    //dashboard report
    public static final String dashBoardReport = "Dashboard Report";

    public static List<String> reportNameList() {
        List<String> nameList = new ArrayList<>();
         nameList.add(saleDetailsReport);
         nameList.add(salesSummeryReport);
        nameList.add(noteSheetGenerateReport);
/*        nameList.add(productionReport);
        nameList.add(PacketingReport);
        nameList.add(packagingReport);*/
        nameList.add(processingReport);
        nameList.add(availAbleReport);//
        nameList.add(inventoryReport);//
  /*      nameList.add(qcqaReport);//
        nameList.add(transferReport);*/
       // nameList.add(stockInOutReport);//
      /*  nameList.add(reconciliation);//
        nameList.add(monitoringReport);//
        nameList.add(iodineUsedReport);//*/
        nameList.add(projectReport);//
        nameList.add(projectWiseItemReport);//
        nameList.add(customerReport);//
        nameList.add(userReport);//
       // nameList.add(employeeReport);//
        // add dash board report
       // nameList.add(dashBoardReport);


        return nameList;
    }

    public static List<Integer> reportImageList() {
        List<Integer> imageList = new ArrayList<>();
         imageList.add(R.drawable.purchase_report);
        imageList.add(R.drawable.purchase_return_report);
        imageList.add(R.drawable.packaging_report);
        imageList.add(R.drawable.packeting_report);
        imageList.add(R.drawable.stock_input_output_repurt);
        imageList.add(R.drawable.stock_input_output_repurt);
        imageList.add(R.drawable.sale_history);
        imageList.add(R.drawable.sale_report);
        imageList.add(R.drawable.sale_return_report);//
        imageList.add(R.drawable.qc_qa_history);//
      //  imageList.add(R.drawable.transfer_history_in);//

      /*
        imageList.add(R.drawable.licence_report);
        imageList.add(R.drawable.licence_expire_report);
        imageList.add(R.drawable.miller_employee_report);
        imageList.add(R.drawable.reconciliation_report);
        imageList.add(R.drawable.miller_employee_report);
        imageList.add(R.drawable.packaging_report);
        imageList.add(R.drawable.iodine_report);
        imageList.add(R.drawable.monitoring_history);
        //dashboard report
        imageList.add(R.drawable.stock_input_output_repurt);
*/
        return imageList;
    }

}
