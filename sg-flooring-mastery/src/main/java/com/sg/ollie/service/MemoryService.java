package com.sg.ollie.service;

import com.sg.ollie.dao.FilePersistenceException;
import com.sg.ollie.dao.NonExistentOrderException;
import com.sg.ollie.dto.Order;
import com.sg.ollie.dto.Product;
import com.sg.ollie.dto.Tax;

import java.time.LocalDate;
import java.util.List;

public interface MemoryService {

    Order addOrder(Order orderToAdd, String orderDate) throws FilePersistenceException;

    Order getOrder(int orderNo, String orderDate) throws FilePersistenceException, NonExistentOrderException;

    List<Order> getAllOrders(String orderDate) throws FilePersistenceException;

    boolean checkOrderExistsOnDate(String orderDate) throws NonExistentOrderException;

    Tax getTax(String stateName) throws NonExistentTaxProfile;

    List<Product> getAllProducts() throws NoProductsAvailableException;

    Order calculateRemainingFields(Order incompleteOrder, String orderDate) throws NonExistentOrderException, FilePersistenceException;

    Order calculateNextOrderNo(Order orderToCalc, String orderDate) throws NonExistentOrderException, FilePersistenceException;

    Order editOrder(Order oldOrder, String orderDate) throws FilePersistenceException;

    Order removeOrder(int orderNo, String orderDate) throws FilePersistenceException;
}
