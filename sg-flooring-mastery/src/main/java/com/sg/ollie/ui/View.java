package com.sg.ollie.ui;

import com.sg.ollie.dto.Order;
import com.sg.ollie.dto.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class View {
    private final UserIO io;

    @Autowired
    public View(UserIO io) {
        this.io = io;
    }

    public int displayMenuAndGetUserChoice() {
        io.print("==== Flooring Program ====");
        io.print("Please choose from the following options: ");
        io.print("1. Display orders on specific day");
        io.print("2. Add order");
        io.print("3. Edit order");
        io.print("4. Remove Order");
        io.print("5. Export data");
        io.print("6. Exit program");
        return(io.readInt("Please enter the number for your selection: "));
    }

    public void displayOrderHeader() {
        io.print("OrderNo - CustomerName - State - TaxRate - ProductType - Area.SqFt - Cost.SqFt - Labour.SqFt" +
                " - MaterialCost - LabourCost - Tax - Total");
    }

    public void displaySingleOrder(Order order) {
        displayOrderHeader();
        io.print(order.toString());
    }

    public void displayListOrders(List<Order> orderList) {
        displayOrderHeader();
        for (Order currentOrder : orderList) {
            io.print(currentOrder.toString());
        }
        io.readString("Press enter to continue: ");
    }

    public int getOrderNo() {
        int orderNo;
        do {
             orderNo = io.readInt("Enter the order number");
        }
        while (orderNo < 0);
        return orderNo;
    }

    public String getAnyOrderDate() {
        return io.readLocalDate("Enter the order date");
    }

    public String getNewOrderDate() {
        return io.readFutureLocalDate("Enter the date of your order " +
                "in the format dd/mm/yyyy (from tomorrow onwards):");
    }

    public String getNewOrderCustomerName() {
        String name;
        while (true) {
             name = io.readString("Enter the customer name using only letters, numbers, commas and full-stops:");
            Pattern p = Pattern.compile("[^a-z0-9,. ]", Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(name);
            if (m.find()) {
                io.print("The name must only include letters, numbers, commas and full-stops");
                continue;
            }
            if (name.isBlank()) {
                io.print("The customer name can not be blank");
                continue;
            }
            break;
        }
        return name;
    }

    public String getNewOrderState() {
        return io.readString("Enter the orders state name:");
    }

    public Product displayProductSelectionAndGetSelection(List<Product> productList) {
        int count = 1;
        io.print("Available Products:");
        io.print(" Product | Cost.SqFt | Labour.SqFt");
        for (Product currentProduct : productList) {
            io.print(count + ". " +currentProduct.toString());
            count++;
        }
        int productChoiceInt = io.readInt("Enter the index of your product choice", 1, productList.size());
        return productList.get(productChoiceInt - 1);
    }
    public BigDecimal getNewOrderArea() {
        return io.readCurrency("Enter the area required of your selected product" +
                " in SqFt. (the minimum order is 100SqFt):", 100, 1000000);
    }

    public String getEditOrderCustomerName(String oldName) {
        io.print("Old value: " + oldName);
        return getNewOrderCustomerName();
    }

    public void displayEditOrderOldState(String oldState) {
        io.print("Old value: " + oldState);
    }

    public void displayEditOrderOldProduct(String productType) {
        io.print("Old value: " + productType);
    }

    public BigDecimal getEditOrderArea(BigDecimal oldArea) {
        io.print("Old value: " + oldArea);
        return getNewOrderArea();
    }

    public void pressEnterToContinue() {
        io.readString("Press enter to continue: ");
    }

    public int getYesOrNoSelection() {
        io.print("Would you like to proceed? ");
        return io.readInt("1 = Yes  |  0 = No:", 0, 1);
    }

    public void displayErrorMessage(String errorMessage) {
        io.print(errorMessage);
        pressEnterToContinue();
    }
    public void displayUnknownCommand() {
        io.print("Unknown command, please enter one of the options");
    }

    public void displayDisplayOrdersBanner() {
        io.print("=== Display Orders ===");
    }

    public void displayAddOrderBanner() {
        io.print("=== Add Order ===");
    }

    public void displayEditOrderBanner() {
        io.print("=== Edit Order ===");
    }

    public void displayRemoveOrderBanner() {
        io.print("=== Remove Order ===");
    }

    public int displayExportDataBanner() {
        io.print("=== Write BackUp ===");
        io.print("This will create a backup of all orders and place it in the BackUp directory");
        return getYesOrNoSelection();
    }

    public void displayOperationSuccess() {
        io.print("The operation was successful");
        pressEnterToContinue();
    }

    public void displayAbortOperation() {
        io.print("The operation has been aborted");
        pressEnterToContinue();
    }

    public void displayThankYouMessage() {
        io.print("Thank you, goodbye");
    }
}