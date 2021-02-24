package com.sg.ollie.service;

import com.sg.ollie.dao.*;
import com.sg.ollie.dto.Order;
import com.sg.ollie.dto.Product;
import com.sg.ollie.dto.Tax;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MemoryServiceImpl implements MemoryService {
    private final OrderFileDao orderFileDao;
    private final TaxFileDao taxFileDao;
    private final ProductFileDao productFileDao;

    @Autowired
    public MemoryServiceImpl(OrderFileDao orderFileDao, TaxFileDao taxFileDao, ProductFileDao productFileDao) {
        this.orderFileDao = orderFileDao;
        this.taxFileDao = taxFileDao;
        this.productFileDao = productFileDao;
    }

    @Override
    public Order addOrder(Order orderToAdd, String orderDate) throws FilePersistenceException {
        return orderFileDao.addOrder(orderToAdd, orderDate);
    }

    @Override
    public Order getOrder(int orderNo, String orderDate) throws FilePersistenceException, NonExistentOrderException {
        if (orderFileDao.getOrder(orderNo, orderDate) != null) {
            return orderFileDao.getOrder(orderNo, orderDate);
        }
        else throw new NonExistentOrderException("There is no order on the given date with the given number");
    }

    @Override
    public List<Order> getAllOrders(String orderDate) throws FilePersistenceException {
        return orderFileDao.getAllOrders(orderDate);
    }

    @Override
    public boolean checkOrderExistsOnDate(String orderDate) throws NonExistentOrderException {
        if (orderFileDao.checkOrderExistsOnDate(orderDate)) {
            return orderFileDao.checkOrderExistsOnDate(orderDate);
        }
        else throw new NonExistentOrderException("There is no orders on the given date");
    }

    @Override
    public Tax getTax(String stateName) throws NonExistentTaxProfile {
        if (taxFileDao.getTax(stateName) != null) {
            return taxFileDao.getTax(stateName);
        }
        else throw new NonExistentTaxProfile("The given state name is not registered in the system");
    }

    @Override
    public List<Product> getAllProducts() throws NoProductsAvailableException {
        if (!productFileDao.getAllProducts().isEmpty()) {
            return productFileDao.getAllProducts();
        }
        else throw new NoProductsAvailableException("There are currently no products available");
    }

    @Override
    public Order calculateRemainingFields(Order incompleteOrder, String orderDate) throws NonExistentOrderException, FilePersistenceException {
        return orderFileDao.calculateAdditionalOrderFields(incompleteOrder,orderDate);
    }

    @Override
    public Order calculateNextOrderNo(Order orderToCalc, String orderDate) throws NonExistentOrderException, FilePersistenceException {
        return orderFileDao.calculateNextOrderNo(orderToCalc, orderDate);
    }

    @Override
    public Order editOrder(Order newOrder, String orderDate) throws FilePersistenceException {
        return orderFileDao.editOrder(newOrder, orderDate);
    }

    @Override
    public Order removeOrder(int orderNo, String orderDate) throws FilePersistenceException {
        return orderFileDao.removeOrder(orderNo, orderDate);
    }
}