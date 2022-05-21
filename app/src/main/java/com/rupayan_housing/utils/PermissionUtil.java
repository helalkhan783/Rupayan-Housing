package com.rupayan_housing.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PermissionUtil {

    public static final String permissionMessage = "You don't have permission for access this portion";

    /**
     * for manage all permission
     */
    public static int manageAll = 1;//if a user have permission 1 user can control everything
    /**
     * show main home fragment permission
     */
    public static int showAccount = 1239;
    public static int showSalesRequisition = 1036;
    /**
     * main home fragment (Account) child show permission
     */
    public static int showDueReceived = 1058;
    public static int showPayDue = 1062;
    public static int payDueExpense = 1070;
    public static int payInstruction = 1073;
    public static int payInstructionList = 1336;

    /**
     * main home fragment (sales requisition) child show permission
     */
    public static int showAddNewSaleRequisition = 1036;
    public static int showSalesReqList = 1037;
    public static int showSalesPendingReqList = 1038;
    public static int showSalesDeclinedReqList = 1039;
    public static int UserList = 25;
    /**
     * For handle sale portion permission
     */
    public static int newSale = 5;

    public static List<Integer> currentUserPermissionList(String permission) {
        List<Integer> favList = null;
        try {
            String replace = permission.replace("[", "");
            String replace1 = replace.replace("]", "");
            if (replace1.isEmpty() || replace1 == null) {//if permission id comes null or empty ,, because the application crashed ,
                replace1 = "0";                                    // when permission id is empty or null(this is one kind off validation)
            }
            List<String> arrayList = new ArrayList<String>(Arrays.asList(replace1.split(",")));
              favList = new ArrayList<Integer>();
            for (String fav : arrayList) {
                favList.add(Integer.parseInt(fav.trim()));
            }

        }catch (Exception e){}
        return favList;
    }

}
