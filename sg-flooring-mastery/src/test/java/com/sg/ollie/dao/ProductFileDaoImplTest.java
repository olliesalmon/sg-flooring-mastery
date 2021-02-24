package com.sg.ollie.dao;

import com.sg.ollie.dto.Product;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductFileDaoImplTest {
    ProductFileDao testProductDao;

    @BeforeEach
    void setUp() {
        String testProductDirectory = "Tests/Data/";
        String testProductFile = "testproducts.txt";

        testProductDao = new ProductFileDaoImpl(testProductFile, testProductDirectory);
    }

    @Test
    void testAdd_Get_Remove_Product() {
        Product preMadeProduct = new Product();
        preMadeProduct.setProductType("ProductOne");
        preMadeProduct.setCostPerSquareFoot(new BigDecimal("10"));
        preMadeProduct.setLabourCostPerSquareFoot(new BigDecimal("10"));
        testProductDao.addProduct(preMadeProduct);

        Product gotProduct = testProductDao.getProduct("ProductOne");
        assertNotNull(gotProduct, "The got product should not be null");
        assertEquals(preMadeProduct, gotProduct, "The got product should be equals to the pre made product");

        Product removedProduct = testProductDao.removeProduct("ProductOne");
        assertNotNull(removedProduct, "The removed product should not be null");

        gotProduct = testProductDao.getProduct("ProductOne");
        assertNull(gotProduct, "Getting the product should now return null");
    }

    @Test
    void getAllProducts() {
        Product preMadeProduct = new Product();
        preMadeProduct.setProductType("ProductOne");
        preMadeProduct.setCostPerSquareFoot(new BigDecimal("10"));
        preMadeProduct.setLabourCostPerSquareFoot(new BigDecimal("10"));

        Product preMadeProduct2 = new Product();
        preMadeProduct.setProductType("ProductTwo");
        preMadeProduct.setCostPerSquareFoot(new BigDecimal("20"));
        preMadeProduct.setLabourCostPerSquareFoot(new BigDecimal("20"));

        testProductDao.addProduct(preMadeProduct);
        testProductDao.addProduct(preMadeProduct2);

        List<Product> productList = testProductDao.getAllProducts();
        assertNotNull(productList, "The list should not be null");
        assertEquals(2, productList.size(), "The list should contain two products");
        assertTrue(productList.contains(preMadeProduct), "The list should contain the first product");
        assertTrue(productList.contains(preMadeProduct2), "The list should contain the second product");
       }

    @Test
    void testWriteAndLoadProductFile() throws IOException {
        Product preMadeProduct = new Product();
        preMadeProduct.setProductType("ProductOne");
        preMadeProduct.setCostPerSquareFoot(new BigDecimal("10"));
        preMadeProduct.setLabourCostPerSquareFoot(new BigDecimal("10"));
        testProductDao.addProduct(preMadeProduct);

        try {
            testProductDao.writeProductFile();
        }
        catch (FilePersistenceException e) {
            fail("The product file could not be written");
        }
        File testProductFile = new File("Tests/Data/testproducts.txt");
        File testProductFileFilledIn = new File("Tests/Data/testproductsfilledin.txt");
        assertTrue(FileUtils.contentEquals(testProductFile, testProductFileFilledIn), "The program written productFile should be equal to premade one");

        testProductDao.removeProduct("ProductOne");
        assertNull(testProductDao.getProduct("ProductOne"), "This should be null as the list is empty");

        try {
            testProductDao.loadProductFile();
        } catch (FilePersistenceException e) {
            fail("The product file could not be loaded");
        }

        assertNotNull(testProductDao.getAllProducts(), "The list should not have an item inside");
        assertEquals(preMadeProduct, testProductDao.getProduct("ProductOne"), "The product read from file should be the equals the pre made");
    }
}