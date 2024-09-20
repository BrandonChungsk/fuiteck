package com.example.javath;

public class DataModel {
    private String outletCode;
    private String totalSales;
    private String date;

    public DataModel(String outletCode, String totalSales, String date) {
        this.outletCode = outletCode;
        this.totalSales = totalSales;
        this.date = date;
    }

    public String getOutletCode() {
        return outletCode;
    }

    public String getTotalSales() {
        return totalSales;
    }

    public String getDate() {
        return date;
    }
}
