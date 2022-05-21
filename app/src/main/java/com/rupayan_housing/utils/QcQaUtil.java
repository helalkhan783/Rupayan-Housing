package com.rupayan_housing.utils;

import com.rupayan_housing.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class QcQaUtil {
    public static final String addQcQa = "Add QC/QA";
    public static final String pendingQcQa = "QC/QA Pending List";
    public static final String declinedQcQa = "QC/QA Declined List";
    public static final String qcQaHistory = "QC/QA History";
    @NotNull
    public static List<String> qcQaNameList() {
        List<String> nameList = new ArrayList<>();
        nameList.add(addQcQa);
        nameList.add(pendingQcQa);
        nameList.add(declinedQcQa);
        nameList.add(qcQaHistory);
        return nameList;
    }


    @NotNull
    public static List<Integer> qcQaImageList() {
        List<Integer> imageList = new ArrayList<>();
        imageList.add(R.drawable.add_qc_qa);
        imageList.add(R.drawable.qc_qa_pending);
        imageList.add(R.drawable.qc_qa_decline);
        imageList.add(R.drawable.qc_qa_history);
        return imageList;
    }

}
