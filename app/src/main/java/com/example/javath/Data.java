package com.example.javath;

public class Data {
    private String date;
    private String poLocCd;
    private double grandTotal;

    public Data(String date, String poLocCd, double grandTotal) {
        this.date = date;
        this.poLocCd = poLocCd;
        this.grandTotal = grandTotal;
    }

    public String getDate() {
        return date;
    }

    public String getPoLocCd() {
        return poLocCd;
    }

    public double getGrandTotal() {
        return grandTotal;
    }
}
