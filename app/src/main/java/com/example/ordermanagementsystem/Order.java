package com.example.ordermanagementsystem;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Order {
    private String orderName;
    private String orderNo;
    private String orderDueDate;
    private String customerAddress;
    private String CustomerName;
    private String CustomerPhone;
    private String Total;
    private String countryName;
    private String id;
    private String city;


    public Order() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Order(String id,String orderNo, String orderDueDate, String customerAddress, String customerName, String customerPhone, String total, String countryName, String city) {
//        this.orderName = orderName;
        this.id=id;
        this.orderNo = orderNo;
        this.orderDueDate = orderDueDate;
        this.customerAddress = customerAddress;
        this.CustomerName = customerName;
        this.CustomerPhone = customerPhone;
        this.Total = total;
        this.countryName = countryName;
        this.city = city;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderDueDate() {
        return orderDueDate;
    }

    public void setOrderDueDate(String orderDueDate) {
        this.orderDueDate = orderDueDate;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getCustomerPhone() {
        return CustomerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        CustomerPhone = customerPhone;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


}
