package com.rupayan_housing.view.fragment.QC_QA;

public interface GetAllQcQaDataFromAdapter {
    void getAllData(int position,String testId, String shortName,String reference,String value);
    void deleteItem(int position);
 }
