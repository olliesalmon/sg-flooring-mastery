package com.sg.ollie.dao;

import com.sg.ollie.dto.Tax;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaxFileDaoImplTest {
    TaxFileDao testTaxDao;

    @BeforeEach
    void setUp() {
        String testTaxDirectory = "Tests/Data/";
        String testTaxFile = "testtaxes.txt";

        testTaxDao = new TaxFileDaoImpl(testTaxFile, testTaxDirectory);
    }

    @Test
    void testAdd_Get_Remove_Tax() {
        Tax preMadeTax = new Tax();
        preMadeTax.setStateName("TaxOne");
        preMadeTax.setStateCode("TX1");
        preMadeTax.setTaxRate(new BigDecimal("10"));
        testTaxDao.addTax(preMadeTax);

        Tax gotTax = testTaxDao.getTax("TaxOne");
        assertNotNull(gotTax, "The got tax should not be null");
        assertEquals(preMadeTax, gotTax, "The got tax should be equals to the pre made tax");

        Tax removedTax = testTaxDao.removeTax("TaxOne");
        assertNotNull(removedTax, "The removed tax should not be null");

        gotTax = testTaxDao.getTax("TaxOne");
        assertNull(gotTax, "Getting the tax should now return null");
    }

    @Test
    void getAllTaxes() {
        Tax preMadeTax = new Tax();
        preMadeTax.setStateName("TaxOne");
        preMadeTax.setStateCode("TX1");
        preMadeTax.setTaxRate(new BigDecimal("10"));

        Tax preMadeTax2 = new Tax();
        preMadeTax.setStateName("TaxTwo");
        preMadeTax.setStateCode("TX2");
        preMadeTax.setTaxRate(new BigDecimal("20"));

        testTaxDao.addTax(preMadeTax);
        testTaxDao.addTax(preMadeTax2);

        List<Tax> taxList = testTaxDao.getAllTaxes();
        assertNotNull(taxList, "The list should not be null");
        assertEquals(2, taxList.size(), "The list should contain two taxes");
        assertTrue(taxList.contains(preMadeTax), "The list should contain the first tax");
        assertTrue(taxList.contains(preMadeTax2), "The list should contain the second tax");
    }

    @Test
    void testWriteAndLoadTaxFile() throws IOException {
        Tax preMadeTax = new Tax();
        preMadeTax.setStateName("TaxOne");
        preMadeTax.setStateCode("TX1");
        preMadeTax.setTaxRate(new BigDecimal("10"));
        testTaxDao.addTax(preMadeTax);

        try {
            testTaxDao.writeTaxFile();
        } catch (FilePersistenceException e) {
            fail("The tax file could not be written");
        }
        File testTaxFile = new File("Tests/Data/testtaxes.txt");
        File testTaxFileFilledIn = new File("Tests/Data/testtaxesfilledin.txt");
        assertTrue(FileUtils.contentEquals(testTaxFile, testTaxFileFilledIn), "The program written taxFile should be equal to premade one");

        testTaxDao.removeTax("TaxOne");
        assertNull(testTaxDao.getTax("TaxOne"), "This should be null as the list is empty");

        try {
            testTaxDao.loadTaxFile();
        } catch (FilePersistenceException e) {
            fail("The tax file could not be loaded");
        }

        assertNotNull(testTaxDao.getAllTaxes(), "The list should not have an item inside");
        assertEquals(preMadeTax, testTaxDao.getTax("TaxOne"), "The tax read from file should be the equals the pre made");
    }
}