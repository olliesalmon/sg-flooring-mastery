package com.sg.ollie.dao;

import com.sg.ollie.dto.Order;

import java.util.List;

public interface OrderFileDao {
    boolean checkOrderExistsOnDate(String orderDate) throws NonExistentOrderException;

    Order addOrder(Order orderToAdd, String orderDate) throws FilePersistenceException;

    Order calculateAdditionalOrderFields(Order orderToCalc, String orderDate) throws NonExistentOrderException, FilePersistenceException;

    Order calculateNextOrderNo(Order orderToCalc, String orderDate) throws FilePersistenceException, NonExistentOrderException;

    Order getOrder(int orderNo, String orderDate) throws FilePersistenceException;

    List<Order> getAllOrders(String orderDate) throws FilePersistenceException;

    Order removeOrder(int orderNo, String orderDate) throws FilePersistenceException;

    Order editOrder(Order oldOrder, String orderDate) throws FilePersistenceException;

    void writeBackup() throws FilePersistenceException;

    void loadOrderDays() throws FilePersistenceException;

    void writeOrderDays() throws FilePersistenceException;
}