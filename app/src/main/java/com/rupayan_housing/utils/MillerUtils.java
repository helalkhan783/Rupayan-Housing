package com.rupayan_housing.utils;

import com.rupayan_housing.R;

import java.util.ArrayList;
import java.util.List;

public class MillerUtils {

    public static final String addnewMiller = "Add New Mill";
    public static final String millreProfileList = "Pending Mill List";
    public static final String millerHistoryList = "History of MIll List";
    public static final String millerDeclineList = "Declined Mill List";


    public MillerUtils() {
    }

    public static List<String> getMillerNameList() {
        List<String> itemName = new ArrayList<>();
        itemName.add(addnewMiller);
        itemName.add(millerHistoryList);
        itemName.add(millreProfileList);
        itemName.add(millerDeclineList);

        return itemName;
    }

    public static List<Integer> getMillerImageList() {
        List<Integer> itemImage = new ArrayList<>();
        itemImage.add(R.drawable.add_new_miller);
        itemImage.add(R.drawable.miller_history);
        itemImage.add(R.drawable.miller_pending);
        itemImage.add(R.drawable.miller_declined);
        return itemImage;
    }

}
