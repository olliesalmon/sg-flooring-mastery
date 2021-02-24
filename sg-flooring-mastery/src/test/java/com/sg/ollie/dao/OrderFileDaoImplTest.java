package com.sg.ollie.dao;

import com.sg.ollie.dto.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderFileDaoImplTest {
    OrderFileDao testOrderFileDao;
    String testOrderDaysFileDirectory;
    String testOrderDaysFile;

    @BeforeEach
    void setUp() throws Exception{
        String testOrderDirectory = "Tests/Orders/";
        String testBackupDirectory = "Tests/Backup/";
        testOrderDaysFileDirectory = "Tests/Data/";
        testOrderDaysFile = "testorderdaysfilefilledin.txt";
        String testBackupFile = "testbackupfile.txt";


        new FileWriter(testBackupDirectory + testBackupFile);
        testOrderFileDao = new OrderFileDaoImpl(testOrderDirectory, testBackupDirectory, testOrderDaysFileDirectory,
                testBackupFile, testOrderDaysFile);
    }

    @Test
    void testAdd_Check_GetOrder() throws FilePersistenceException, NonExistentOrderException {
        Order preMadeOrder = new Order(1, "Customer1", "Texas", new BigDecimal("4.45"),
                "Carpet", new BigDecimal("100"), new BigDecimal("2.25"), new BigDecimal("2.10"),
                new BigDecimal("225.00"), new BigDecimal("210.00"), new BigDecimal("19.36"), new BigDecimal("454.36"));
        String testDate = "11242020";
        assertFalse(testOrderFileDao.checkOrderExistsOnDate(testDate), "The order should not exist as map is empty");

        Order addedOrder = testOrderFileDao.addOrder(preMadeOrder, testDate);

        assertTrue(testOrderFileDao.checkOrderExistsOnDate(testDate));
        assertEquals(preMadeOrder, addedOrder, "The added order should have extra fields filled in and be the same as the premade order");
        assertNotNull(addedOrder, "The order has been added and should not have returned null ");

        Order gotOrder = testOrderFileDao.getOrder(1, testDate);
        assertEquals(preMadeOrder, gotOrder, "The got order should have extra fields filled in and be the same as the pre-made order");
        assertNotNull(gotOrder, "The order should have been returned and so should not be null");

        preMadeOrder.setCustomerName("Customer2");
        preMadeOrder.setOrderNo(2);
        addedOrder = testOrderFileDao.addOrder(preMadeOrder, testDate);
        assertEquals(2, addedOrder.getOrderNo(), "The second order should have be added with a order number of 2");
    }

    @Test
    void testCalculateAdditionalFields() throws NonExistentOrderException, FilePersistenceException {
        Order orderMissingFields = new Order("Customer1", "Texas", new BigDecimal("4.45"),
                "Carpet", new BigDecimal("100"), new BigDecimal("2.25"), new BigDecimal("2.10"));
        Order preMadeOrder = new Order(1, "Customer1", "Texas", new BigDecimal("4.45"),
                "Carpet", new BigDecimal("100"), new BigDecimal("2.25"), new BigDecimal("2.10"),
                new BigDecimal("225.00"), new BigDecimal("210.00"), new BigDecimal("19.36"), new BigDecimal("454.36"));
        String orderDate  ="11272020";

        testOrderFileDao.calculateAdditionalOrderFields(orderMissingFields, orderDate);
        testOrderFileDao.calculateNextOrderNo(orderMissingFields, orderDate);

        assertEquals(orderMissingFields, preMadeOrder,"The orders fields have been calculated");
        assertEquals(1,orderMissingFields.getOrderNo(), "The order no should now be 1");
    }

    @Test
    void testAddAndGetAllOrders() throws FilePersistenceException {
        Order preMadeOrder = new Order(1, "Customer1", "Texas", new BigDecimal("4.45"),
                "Carpet", new BigDecimal("100"), new BigDecimal("2.25"), new BigDecimal("2.10"),
                new BigDecimal("225.00"), new BigDecimal("210.00"), new BigDecimal("19.36"), new BigDecimal("454.36"));

        Order preMadeOrder2 = new Order(2, "Customer2", "Texas", new BigDecimal("4.45"),
                "Carpet", new BigDecimal("100"), new BigDecimal("2.25"), new BigDecimal("2.10"),
                new BigDecimal("225.00"), new BigDecimal("210.00"), new BigDecimal("19.36"), new BigDecimal("454.36"));
        String testDate = "11242020";

        List<Order> preMadeList = new ArrayList<>();
        preMadeList.add(preMadeOrder);
        preMadeList.add(preMadeOrder2);

        testOrderFileDao.addOrder(preMadeOrder, testDate);
        testOrderFileDao.addOrder(preMadeOrder2, testDate);

        List<Order> getAllList = testOrderFileDao.getAllOrders(testDate);
        assertNotNull(getAllList, "The list should not be null");
        assertEquals(2, getAllList.size(), "The list should have two orders in");
        assertEquals(preMadeList, getAllList, "The list should equal the pre-made list");
        assertTrue(getAllList.contains(preMadeOrder), "The list should contain the first order");
        assertTrue(getAllList.contains(preMadeOrder2), "The list should contain the second order");
    }

    @Test
    void testRemoveOrder() throws FilePersistenceException {
        Order preMadeOrder = new Order(1, "Customer1", "Texas", new BigDecimal("4.45"),
                "Carpet", new BigDecimal("100"), new BigDecimal("2.25"), new BigDecimal("2.10"),
                new BigDecimal("225.00"), new BigDecimal("210.00"), new BigDecimal("19.36"), new BigDecimal("454.36"));
        String testDate = "11242020";

        testOrderFileDao.addOrder(preMadeOrder, testDate);
        assertEquals(preMadeOrder, testOrderFileDao.getOrder(1, testDate), "The order should have been added");

        Order removedOrder = testOrderFileDao.removeOrder(1, testDate);
        assertNotNull(removedOrder, "The removed order should not be null");
        assertEquals(preMadeOrder, removedOrder, "The removed order should be equal to when it was added");
        assertThrows(FilePersistenceException.class, () ->testOrderFileDao.getOrder(1, testDate));
    }

    @Test
    void testEditOrder() throws FilePersistenceException {
        Order preMadeOrder = new Order(1, "Customer1", "Texas", new BigDecimal("4.45"),
                "Carpet", new BigDecimal("100"), new BigDecimal("2.25"), new BigDecimal("2.10"),
                new BigDecimal("225.00"), new BigDecimal("210.00"), new BigDecimal("19.36"), new BigDecimal("454.36"));
        String testDate = "11242020";

        Order preMadeOrder2 = new Order(1, "Customer2", "Texas", new BigDecimal("4.45"),
                "Carpet", new BigDecimal("100"), new BigDecimal("2.25"), new BigDecimal("2.10"),
                new BigDecimal("225.00"), new BigDecimal("210.00"), new BigDecimal("19.36"), new BigDecimal("454.36"));

        testOrderFileDao.addOrder(preMadeOrder, testDate);
        Order editedOrder = testOrderFileDao.editOrder(preMadeOrder2, testDate);
        assertEquals(preMadeOrder2, editedOrder, "The edited order should be equal to the 2nd Pre-made order");
        assertEquals(1, testOrderFileDao.getAllOrders(testDate).size(), "There should still only be one order");
        }

    @Test
    void testWriteBackup() throws FilePersistenceException, IOException {
        Order preMadeOrder = new Order(1, "Customer1", "Texas", new BigDecimal("4.45"),
                "Carpet", new BigDecimal("100"), new BigDecimal("2.25"), new BigDecimal("2.10"),
                new BigDecimal("225.00"), new BigDecimal("210.00"), new BigDecimal("19.36"), new BigDecimal("454.36"));

        Order preMadeOrder2 = new Order(1, "Customer2", "Texas", new BigDecimal("4.45"),
                "Carpet", new BigDecimal("100"), new BigDecimal("2.25"), new BigDecimal("2.10"),
                new BigDecimal("225.00"), new BigDecimal("210.00"), new BigDecimal("19.36"), new BigDecimal("454.36"));

        testOrderFileDao.addOrder(preMadeOrder, "11242020");
        testOrderFileDao.addOrder(preMadeOrder2, "11252020");

        try {
            testOrderFileDao.writeBackup();
        }
        catch (FilePersistenceException e) {
            fail("The inventory could not be written");
        }

        File testBackup = new File("Tests/Backup/testbackupfile.txt");
        File testBackupFilledIn = new File("Tests/Backup/testbackupfilefilledin.txt");
        assertTrue(FileUtils.contentEquals(testBackup, testBackupFilledIn), "The program written inventory should match the pre-made one");
    }

    @Test
    void loadOrderDays() throws FilePersistenceException, IOException {

        testOrderFileDao.loadOrderDays();
        try {
            assertTrue(testOrderFileDao.checkOrderExistsOnDate("11242020"), "The date should now be loaded in the list");
        } catch (NonExistentOrderException e) {
            fail("The order should be in the list and so this error should not have been thrown");
        }

        new FileWriter(testOrderDaysFileDirectory + testOrderDaysFile);

        try {
            testOrderFileDao.writeOrderDays();
        } catch (FilePersistenceException e) {
            fail("error should not have been thrown");
        }

        File testOrderDay = new File("Tests/Backup/testorderdaysfile.txt");
        File testOrderDayFilledIn = new File("Tests/Backup/testorderdaysfilefilledin.txt");
        assertTrue(FileUtils.contentEquals(testOrderDay, testOrderDayFilledIn), "The program written inventory should match the pre-made one");
    }
}