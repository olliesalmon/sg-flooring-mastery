package com.sg.ollie.controller;

import com.sg.ollie.dao.FilePersistenceException;
import com.sg.ollie.dao.NonExistentOrderException;
import com.sg.ollie.dto.Order;
import com.sg.ollie.dto.Product;
import com.sg.ollie.dto.Tax;
import com.sg.ollie.service.FileService;
import com.sg.ollie.service.MemoryService;
import com.sg.ollie.service.NoProductsAvailableException;
import com.sg.ollie.service.NonExistentTaxProfile;
import com.sg.ollie.ui.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Controller {
    private final int YES_SELECTION = 1;
    private final int NO_SELECTION = 2;
    private final View view;
    private final FileService fileService;
    private final MemoryService memoryService;

    @Autowired
    public Controller(View view, FileService fileService, MemoryService memoryService) {
        this.view = view;
        this.fileService = fileService;
        this.memoryService = memoryService;
    }

    public void run() throws FilePersistenceException {
        fileService.loadOrderTaxProductFiles();
        boolean continueRunning = true;
        int menuSelection;
        while (continueRunning) {
            try {
                menuSelection = view.displayMenuAndGetUserChoice();
                switch (menuSelection) {
                    case 1:
                        displayOrders();
                        break;
                    case 2:
                        addOrder();
                        break;
                    case 3:
                        editOrder();
                        break;
                    case 4:
                        removeOrder();
                        break;
                    case 5:
                        exportData();
                        break;
                    case 6:
                        continueRunning = false;
                        break;
                    default:
                        view.displayUnknownCommand();
                }
            } catch (NonExistentOrderException | FilePersistenceException | NoProductsAvailableException e) {
                view.displayErrorMessage(e.getMessage());
            }
        }
        view.displayThankYouMessage();
    }

    private void displayOrders() throws NonExistentOrderException, FilePersistenceException {
        view.displayDisplayOrdersBanner();
        String orderDate = view.getAnyOrderDate();
        memoryService.checkOrderExistsOnDate(orderDate);
        List<Order> orderList = memoryService.getAllOrders(orderDate);
        view.displayListOrders(orderList);
    }

    private void addOrder() throws FilePersistenceException, NoProductsAvailableException, NonExistentOrderException {
        view.displayAddOrderBanner();
        String orderDate = view.getNewOrderDate();
        Order orderToAdd = new Order();
        orderToAdd.setCustomerName(view.getNewOrderCustomerName());

        orderToAdd = getAndValidateTaxProfile(orderToAdd);
        orderToAdd = getProduct(orderToAdd);

        orderToAdd.setArea(view.getNewOrderArea());
        orderToAdd = memoryService.calculateRemainingFields(orderToAdd, orderDate);
        orderToAdd = memoryService.calculateNextOrderNo(orderToAdd, orderDate);

        view.displaySingleOrder(orderToAdd);
        if (view.getYesOrNoSelection() == YES_SELECTION) {
            memoryService.addOrder(orderToAdd, orderDate);
            view.displayOperationSuccess();
        } else view.displayAbortOperation();
    }

    private Order getAndValidateTaxProfile(Order orderToAdd) {
        String stateName;
        Tax taxProfile;
        while (true) {
            stateName = view.getNewOrderState();
            try {
                taxProfile = memoryService.getTax(stateName);
                break;
            } catch (NonExistentTaxProfile e) {
                view.displayErrorMessage(e.getMessage());
            }
        }
        orderToAdd.setState(taxProfile.getStateName());
        orderToAdd.setTaxRate(taxProfile.getTaxRate());
        return orderToAdd;
    }

    private Order getProduct(Order orderToAdd) throws NoProductsAvailableException {
        List<Product> productList = memoryService.getAllProducts();
        Product productChoice = view.displayProductSelectionAndGetSelection(productList);
        orderToAdd.setProductType(productChoice.getProductType());
        orderToAdd.setCostPerSquareFoot(productChoice.getCostPerSquareFoot());
        orderToAdd.setLabourCostPerSquareFoot(productChoice.getLabourCostPerSquareFoot());
        return orderToAdd;
    }

    private void editOrder() throws NonExistentOrderException, FilePersistenceException, NoProductsAvailableException {
        view.displayEditOrderBanner();
        String orderDate = view.getAnyOrderDate();
        int orderNo = view.getOrderNo();
        Order oldOrder = memoryService.getOrder(orderNo, orderDate);

        oldOrder.setCustomerName(view.getEditOrderCustomerName(oldOrder.getCustomerName()));
        view.displayEditOrderOldState(oldOrder.getState());
        oldOrder = getAndValidateTaxProfile(oldOrder);
        view.displayEditOrderOldProduct(oldOrder.getProductType());
        oldOrder = getProduct(oldOrder);
        oldOrder.setArea(view.getEditOrderArea(oldOrder.getArea()));
        oldOrder = memoryService.calculateRemainingFields(oldOrder, orderDate);

        view.displaySingleOrder(oldOrder);
        while (true) {
            int selection = view.getYesOrNoSelection();
            if (selection == YES_SELECTION) {
                memoryService.editOrder(oldOrder, orderDate);
                view.displayOperationSuccess();
                break;
            } else if (selection == NO_SELECTION) {
                view.displayAbortOperation();
                break;
            }
        }
    }

    private void removeOrder() throws NonExistentOrderException, FilePersistenceException {
        view.displayRemoveOrderBanner();
        String orderDate = view.getAnyOrderDate();
        int orderNo = view.getOrderNo();
        Order orderToRemove = memoryService.getOrder(orderNo, orderDate);

        view.displaySingleOrder(orderToRemove);
        while (true) {
            int selection = view.getYesOrNoSelection();
            if (selection == YES_SELECTION) {
                memoryService.removeOrder(orderNo, orderDate);
                view.displayOperationSuccess();
                break;
            } else if (selection == NO_SELECTION) {
                view.displayAbortOperation();
                break;
            }
        }
    }

    private void exportData() throws FilePersistenceException {
        while (true) {
            int selection = view.displayExportDataBanner();
            if (selection == YES_SELECTION) {
                fileService.writeBackupFile();
                view.displayOperationSuccess();
                break;
            } else if (selection == NO_SELECTION) {
                view.displayAbortOperation();
                break;
            }
        }
    }
}