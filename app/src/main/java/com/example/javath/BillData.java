package com.example.javath;

public class BillData {
    private String receiptNo;
    private String receiptDate;
    private String location;
    private double discount;
    private double amount;

    public BillData(String receiptNo, String receiptDate, String location, double discount, double amount) {
        this.receiptNo = receiptNo;
        this.receiptDate = receiptDate;
        this.location = location;
        this.discount = discount;
        this.amount = amount;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public String getReceiptDate() {
        return receiptDate;
    }

    public String getLocation() {
        return location;
    }

    public double getDiscount() {
        return discount;
    }

    public double getAmount() {
        return amount;
    }
}
