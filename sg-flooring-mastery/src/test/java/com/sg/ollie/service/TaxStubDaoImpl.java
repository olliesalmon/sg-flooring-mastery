package com.sg.ollie.service;

import com.sg.ollie.dao.FilePersistenceException;
import com.sg.ollie.dao.TaxFileDao;
import com.sg.ollie.dto.Tax;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class TaxStubDaoImpl implements TaxFileDao {
    Tax onlyTax;

    public TaxStubDaoImpl() {
        onlyTax = new Tax();
        onlyTax.setStateName("TaxOne");
        onlyTax.setStateCode("TX1");
        onlyTax.setTaxRate(new BigDecimal("10"));
    }

    @Override
    public Tax addTax(Tax taxToAdd) {
        if (taxToAdd.equals(onlyTax)) {
            return onlyTax;
        }
        else return null;
    }

    @Override
    public Tax getTax(String stateName) {
        if (stateName.equals(onlyTax.getStateName())) {
            return onlyTax;
        }
        else return null;
    }

    @Override
    public List<Tax> getAllTaxes() {
        List<Tax> taxList = new ArrayList<>();
        taxList.add(onlyTax);
        return taxList;
    }

    @Override
    public Tax removeTax(String stateName) {
        if (stateName.equals(onlyTax.getStateName())) {
            return onlyTax;
        }
        else return null;
    }

    @Override
    public void loadTaxFile() throws FilePersistenceException {
        //void so does not return anything
    }

    @Override
    public void writeTaxFile() throws FilePersistenceException {
        //void so does not return anything
    }
}