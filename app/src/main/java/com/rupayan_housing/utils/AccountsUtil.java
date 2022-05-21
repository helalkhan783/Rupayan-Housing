package com.rupayan_housing.utils;


import com.rupayan_housing.R;

import java.util.ArrayList;
import java.util.List;

public class AccountsUtil {
    public static String receiveDue = "Received Due";
    public static String payDue = "pay Due";
    public static String payDueExpense = "pay Due Expense";
    public static String payInstruction = "Payment Instruction";
    public static String payInstructionList = "Payment Instruction List";

    public static Integer receiveDueImage = R.drawable.ic_acc_due_receive;
    public static Integer payDueImage = R.drawable.ic_acc_due_pay;
    public static Integer payDueExpenseImage = R.drawable.ic_acc_due_pay;
    public static Integer payInstructionImage = R.drawable.ic_acc_due_pay;
    public static Integer payInstructionListImage = R.drawable.ic_acc_due_pay;


    public static List<String> getAccountsChildNameList() {
        List<String> accountChild = new ArrayList<>();
        accountChild.add(receiveDue);
        accountChild.add(payDue);
        accountChild.add(payDueExpense);
        accountChild.add(payInstruction);
        accountChild.add(payInstructionList);
        return accountChild;
    }

    public static List<Integer> getAccountsChildImageList() {
        List<Integer> imageList = new ArrayList<>();
        imageList.add(receiveDueImage);
        imageList.add(payDueImage);
        imageList.add(payDueExpenseImage);
        imageList.add(payInstructionImage);
        imageList.add(payInstructionListImage);
        return imageList;
    }
}
