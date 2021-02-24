package com.sg.ollie.service;

import com.sg.ollie.dao.FilePersistenceException;
import com.sg.ollie.dao.NonExistentOrderException;
import com.sg.ollie.dto.Order;
import com.sg.ollie.dto.Tax;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MemoryServiceImplTest {
    private MemoryService testMemoryService;

    public MemoryServiceImplTest() {
        AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext();
        appContext.scan("com.sg.ollie.service");
        appContext.refresh();

        testMemoryService = appContext.getBean("memoryServiceImpl", MemoryServiceImpl.class);
    }

    @Test
    void testAdd_GetOrder() throws FilePersistenceException, NonExistentOrderException {
        String onlyOrderDate = "11242020";

        Order onlyOrder = new Order(1, "Customer1", "Texas", new BigDecimal("4.45"),
                "Carpet", new BigDecimal("100"), new BigDecimal("2.25"), new BigDecimal("2.10"),
                new BigDecimal("225.00"), new BigDecimal("210.00"), new BigDecimal("19.36"), new BigDecimal("454.36"));

        Order addedOrder = testMemoryService.addOrder(onlyOrder, onlyOrderDate);
        assertNotNull(addedOrder, "Should not return null");
        assertEquals(onlyOrder, addedOrder, "The order added should be equal to the original");

        try {
            Order gotOrder = testMemoryService.getOrder(1, onlyOrderDate);
            assertEquals(onlyOrder, gotOrder, "The got order should be equals to the original");

        } catch (NonExistentOrderException e ) {
            fail("The order should be present and so this should not have been thrown");
        }
    }

    @Test
    void test_Check_GetAllOrder() throws FilePersistenceException, NonExistentOrderException {
        String onlyOrderDate = "11242020";
        Order onlyOrder = new Order(1, "Customer1", "Texas", new BigDecimal("4.45"),
                "Carpet", new BigDecimal("100"), new BigDecimal("2.25"), new BigDecimal("2.10"),
                new BigDecimal("225.00"), new BigDecimal("210.00"), new BigDecimal("19.36"), new BigDecimal("454.36"));
        List<Order> orderList = new ArrayList<>();
        orderList.add(onlyOrder);

        Order addedOrder = testMemoryService.addOrder(onlyOrder, onlyOrderDate);
        assertTrue(testMemoryService.checkOrderExistsOnDate(onlyOrderDate), "The date should contain an order");

        List<Order> gotAll = testMemoryService.getAllOrders(onlyOrderDate);
        assertNotNull(gotAll, "The list should not be null");
        assertTrue(gotAll.contains(onlyOrder), "The list should contain the order");
        assertEquals(orderList, gotAll, "The two lists should be the same");
    }

    @Test
    void testGetTax() throws NonExistentTaxProfile {
        Tax onlyTax = new Tax();
        onlyTax.setStateName("TaxOne");
        onlyTax.setStateCode("TX1");
        onlyTax.setTaxRate(new BigDecimal("10"));

        Tax gotTax = testMemoryService.getTax("TaxOne");
        assertNotNull(gotTax, "The Tax profile should not be null");
        assertEquals(onlyTax, gotTax, "The tax profiles should be equal");
    }

    @Test
    void testCalculateRemainingFields() throws NonExistentOrderException, FilePersistenceException {
        String onlyOrderDate = "11242020";
        Order onlyIncompleteOrder = new Order("Customer1", "Texas", new BigDecimal("4.45"),
                "Carpet", new BigDecimal("100"), new BigDecimal("2.25"), new BigDecimal("2.10"));
        Order onlyOrderWithoutNo = new Order(1, "Customer1", "Texas", new BigDecimal("4.45"),
                "Carpet", new BigDecimal("100"), new BigDecimal("2.25"), new BigDecimal("2.10"),
                new BigDecimal("225.00"), new BigDecimal("210.00"), new BigDecimal("19.36"), new BigDecimal("454.36"));
        onlyOrderWithoutNo.setOrderNo(0);

        Order filledInOrder = testMemoryService.calculateRemainingFields(onlyIncompleteOrder, onlyOrderDate);
        assertEquals(onlyOrderWithoutNo, filledInOrder, "The fields should have been filled in so the orders should be equal");
    }

    @Test
    void testCalculateNextOrderNo() throws NonExistentOrderException, FilePersistenceException {
        String onlyOrderDate = "11242020";
        Order onlyOrderWithoutNo = new Order(1, "Customer1", "Texas", new BigDecimal("4.45"),
                "Carpet", new BigDecimal("100"), new BigDecimal("2.25"), new BigDecimal("2.10"),
                new BigDecimal("225.00"), new BigDecimal("210.00"), new BigDecimal("19.36"), new BigDecimal("454.36"));
        onlyOrderWithoutNo.setOrderNo(0);

        Order onlyOrder = new Order(1, "Customer1", "Texas", new BigDecimal("4.45"),
                "Carpet", new BigDecimal("100"), new BigDecimal("2.25"), new BigDecimal("2.10"),
                new BigDecimal("225.00"), new BigDecimal("210.00"), new BigDecimal("19.36"), new BigDecimal("454.36"));

        Order completedOrder = testMemoryService.calculateNextOrderNo(onlyOrderWithoutNo, onlyOrderDate);

        assertEquals(1, completedOrder.getOrderNo(), "The new order no should be one");
        assertEquals(onlyOrder, completedOrder, "The orders should now be the same - filled in");
    }

    @Test
    void testEditOrder() throws FilePersistenceException {
        String onlyOrderDate = "11242020";
        Order editedOrder = new Order(1, "Customer2", "Texas", new BigDecimal("4.45"),
                "Carpet", new BigDecimal("100"), new BigDecimal("2.25"), new BigDecimal("2.10"),
                new BigDecimal("225.00"), new BigDecimal("210.00"), new BigDecimal("19.36"), new BigDecimal("454.36"));

        Order returnedEditedOrder = testMemoryService.editOrder(editedOrder, onlyOrderDate);
        assertNotNull(returnedEditedOrder, "The returned order should not be null");
        assertEquals(editedOrder, returnedEditedOrder, "The edited order should have been returned");
    }

    @Test
    void removeOrder() throws FilePersistenceException {
        String onlyOrderDate = "11242020";
        Order onlyOrder = new Order(1, "Customer1", "Texas", new BigDecimal("4.45"),
                "Carpet", new BigDecimal("100"), new BigDecimal("2.25"), new BigDecimal("2.10"),
                new BigDecimal("225.00"), new BigDecimal("210.00"), new BigDecimal("19.36"), new BigDecimal("454.36"));

        Order removedOrder = testMemoryService.removeOrder(1, onlyOrderDate);
        assertNotNull(removedOrder, "The removed order should not be null");
        assertEquals(onlyOrder, removedOrder, "The two orders should match");
    }
}