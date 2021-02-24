package com.sg.ollie.dao;

import com.sg.ollie.dto.Tax;

import java.util.List;

public interface TaxFileDao {
    Tax addTax(Tax taxToAdd);

    Tax getTax(String stateName);

    List<Tax> getAllTaxes();

    Tax removeTax(String stateName);

    void loadTaxFile() throws FilePersistenceException;

    void writeTaxFile() throws FilePersistenceException;
}