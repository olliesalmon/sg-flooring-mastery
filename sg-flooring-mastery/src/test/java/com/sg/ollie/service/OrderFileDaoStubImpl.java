package com.sg.ollie.service;

import com.sg.ollie.dao.FilePersistenceException;
import com.sg.ollie.dao.NonExistentOrderException;
import com.sg.ollie.dao.OrderFileDao;
import com.sg.ollie.dto.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderFileDaoStubImpl implements OrderFileDao {
    public String onlyOrderDate;
    public Order onlyIncompleteOrder;
    public Order onlyOrderWithoutNo;
    public Order onlyOrder;

    public Order editedOrder;

    public OrderFileDaoStubImpl() {
        onlyOrderDate = "11242020";
        onlyIncompleteOrder = new Order("Customer1", "Texas", new BigDecimal("4.45"),
                "Carpet", new BigDecimal("100"), new BigDecimal("2.25"), new BigDecimal("2.10"));

        onlyOrderWithoutNo = new Order(1, "Customer1", "Texas", new BigDecimal("4.45"),
                "Carpet", new BigDecimal("100"), new BigDecimal("2.25"), new BigDecimal("2.10"),
                new BigDecimal("225.00"), new BigDecimal("210.00"), new BigDecimal("19.36"), new BigDecimal("454.36"));
        onlyOrderWithoutNo.setOrderNo(0);

        onlyOrder = new Order(1, "Customer1", "Texas", new BigDecimal("4.45"),
                "Carpet", new BigDecimal("100"), new BigDecimal("2.25"), new BigDecimal("2.10"),
                new BigDecimal("225.00"), new BigDecimal("210.00"), new BigDecimal("19.36"), new BigDecimal("454.36"));

        editedOrder = new Order(1, "Customer2", "Texas", new BigDecimal("4.45"),
                "Carpet", new BigDecimal("100"), new BigDecimal("2.25"), new BigDecimal("2.10"),
                new BigDecimal("225.00"), new BigDecimal("210.00"), new BigDecimal("19.36"), new BigDecimal("454.36"));
    }

    @Override
    public boolean checkOrderExistsOnDate(String orderDate) {
        if (orderDate.equals(onlyOrderDate)) {
            return true;
        }
        else return false;
    }

    @Override
    public Order addOrder(Order orderToAdd, String orderDate) throws FilePersistenceException {
        if (orderToAdd.getOrderNo() == onlyOrder.getOrderNo() && orderDate.equals(onlyOrderDate)) {
            return onlyOrder;
        }
        else return null;
    }

    @Override
    public Order calculateAdditionalOrderFields(Order orderToCalc, String orderDate) {
        if (orderToCalc.getCustomerName().equals(onlyOrderWithoutNo.getCustomerName()) &
                orderToCalc.getTaxRate().equals(onlyOrderWithoutNo.getTaxRate())) {
            return onlyOrderWithoutNo;
        }
        else return null;
    }

    @Override
    public Order calculateNextOrderNo(Order orderToCalc, String orderDate) throws FilePersistenceException, NonExistentOrderException {
        if (orderToCalc.equals(onlyOrderWithoutNo)) {
            return onlyOrder;
        }
        else return null;
    }

    @Override
    public Order getOrder(int orderNo, String orderDate) throws FilePersistenceException {
        if (orderNo == onlyOrder.getOrderNo() && orderDate.equals(onlyOrderDate)) {
            return onlyOrder;
        }
        else return null;
    }

    @Override
    public List<Order> getAllOrders(String orderDate) throws FilePersistenceException {
        if (orderDate.equals(onlyOrderDate)) {
            List<Order> orderList = new ArrayList<>();
            orderList.add(onlyOrder);
            return orderList;
        }
        else return null;
    }

    @Override
    public Order removeOrder(int orderNo, String orderDate) throws FilePersistenceException {
        if (orderNo == onlyOrder.getOrderNo() && orderDate.equals(onlyOrderDate)) {
            return onlyOrder;
        }
        else return null;
    }

    @Override
    public Order editOrder(Order newOrder, String orderDate) throws FilePersistenceException {
        if (newOrder.equals(editedOrder) && orderDate.equals(onlyOrderDate)) {
            return editedOrder;
        }
        else return null;
    }

    @Override
    public void writeBackup() throws FilePersistenceException {
        //void so does not return anything
    }

    @Override
    public void loadOrderDays() throws FilePersistenceException {
        //void so does not return anything
    }

    @Override
    public void writeOrderDays() throws FilePersistenceException {
        //void so does not return anything
    }
}