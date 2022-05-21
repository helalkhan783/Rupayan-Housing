package com.rupayan_housing.utils.replace;

public class KgToTon {
    public static String kgToTon(String mainValue){
        if (mainValue == null||mainValue.isEmpty()){
            mainValue = "0";
        }
        double value = Double.parseDouble(mainValue);
        double mt = value/1000;// kg to metric ton
        return String.format("%.3f",mt);
    }
}
