package com.sg.ollie.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Order {
    private int orderNo;
    private String customerName;
    private String state;
    private BigDecimal taxRate;
    private String productType;
    private BigDecimal area;
    private BigDecimal costPerSquareFoot;
    private BigDecimal labourCostPerSquareFoot;
    private BigDecimal materialCost;
    private BigDecimal labourCost;
    private BigDecimal tax;
    private BigDecimal total;

    public Order(){
    }

    //Constructor containing new info only
    public Order(String customerName, String state, String productType, BigDecimal area) {
        this.customerName = customerName;
        this.state = state;
        this.productType = productType;
        this.area = area;
    }

    //Constructor containing valid, fetched info from Taxes and Products
    public Order(String customerName, String state, BigDecimal taxRate, String productType, BigDecimal area, BigDecimal costPerSquareFoot, BigDecimal labourCostPerSquareFoot) {
        this.customerName = customerName;
        this.state = state;
        this.taxRate = taxRate;
        this.productType = productType;
        this.area = area;
        this.costPerSquareFoot = costPerSquareFoot;
        this.labourCostPerSquareFoot = labourCostPerSquareFoot;
    }

    //Constructor for complete order, used in (un)marshalling
    public Order(int orderNo, String customerName, String state, BigDecimal taxRate, String productType, BigDecimal area, BigDecimal costPerSquareFoot, BigDecimal labourCostPerSquareFoot, BigDecimal materialCost, BigDecimal labourCost, BigDecimal tax, BigDecimal total) {
        this.orderNo = orderNo;
        this.customerName = customerName;
        this.state = state;
        this.taxRate = taxRate;
        this.productType = productType;
        this.area = area;
        this.costPerSquareFoot = costPerSquareFoot;
        this.labourCostPerSquareFoot = labourCostPerSquareFoot;
        this.materialCost = materialCost;
        this.labourCost = labourCost;
        this.tax = tax;
        this.total = total;
    }

    public void calculateMaterialCost() {
        this.materialCost = this.area.multiply(this.costPerSquareFoot).setScale(2, RoundingMode.HALF_UP);
    }

    public void calculateLabourCost() {
        this.labourCost = this.area.multiply(this.labourCostPerSquareFoot).setScale(2, RoundingMode.HALF_UP);
    }

    public void calculateTax() {
        this.tax = (this.materialCost.add(this.labourCost)).multiply(this.taxRate.divide(new BigDecimal("100").setScale(2, RoundingMode.HALF_UP))).setScale(2, RoundingMode.HALF_UP);
    }

    public void calculateTotal() {
        this.total = this.materialCost.add(this.labourCost).add(this.tax);
    }

    public String toString() {
        String spacer = "  -  ";
        String spacerDollar = "  -  $";
        return  orderNo + spacer + customerName + spacer + state + spacerDollar + taxRate +
                spacer + productType + spacer + area + spacerDollar + costPerSquareFoot + spacerDollar +
                costPerSquareFoot + spacerDollar +  labourCostPerSquareFoot + spacerDollar  + materialCost +
                spacerDollar + labourCost +spacerDollar + tax + spacerDollar + total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return orderNo == order.orderNo &&
                customerName.equals(order.customerName) &&
                state.equals(order.state) &&
                taxRate.equals(order.taxRate) &&
                productType.equals(order.productType) &&
                area.equals(order.area) &&
                costPerSquareFoot.equals(order.costPerSquareFoot) &&
                labourCostPerSquareFoot.equals(order.labourCostPerSquareFoot) &&
                materialCost.equals(order.materialCost) &&
                labourCost.equals(order.labourCost) &&
                tax.equals(order.tax) &&
                total.equals(order.total);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderNo, customerName, state, taxRate, productType, area, costPerSquareFoot, labourCostPerSquareFoot, materialCost, labourCost, tax, total);
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public BigDecimal getCostPerSquareFoot() {
        return costPerSquareFoot;
    }

    public void setCostPerSquareFoot(BigDecimal costPerSquareFoot) {
        this.costPerSquareFoot = costPerSquareFoot;
    }

    public BigDecimal getLabourCostPerSquareFoot() {
        return labourCostPerSquareFoot;
    }

    public void setLabourCostPerSquareFoot(BigDecimal labourCostPerSquareFoot) {
        this.labourCostPerSquareFoot = labourCostPerSquareFoot;
    }

    public BigDecimal getMaterialCost() {
        return materialCost;
    }

    public void setMaterialCost(BigDecimal materialCost) {
        this.materialCost = materialCost;
    }

    public BigDecimal getLabourCost() {
        return labourCost;
    }

    public void setLabourCost(BigDecimal labourCost) {
        this.labourCost = labourCost;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
