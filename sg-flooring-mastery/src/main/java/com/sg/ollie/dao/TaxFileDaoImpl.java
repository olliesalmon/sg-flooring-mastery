package com.sg.ollie.dao;

import com.sg.ollie.dto.Tax;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

@Component
public class TaxFileDaoImpl implements TaxFileDao {
    public final String DIRECTORY;
    public final String TAX_FILE;
    public static final String DELIMITER = ",";
    private final Map<String, Tax> taxes = new HashMap<>();

    public TaxFileDaoImpl() {
        DIRECTORY = "Data/";
        TAX_FILE = DIRECTORY + "Taxes.txt";
    }

    public TaxFileDaoImpl(String taxFile, String directory) {
        DIRECTORY = directory;
        TAX_FILE = taxFile;
    }

    @Override
    public Tax addTax(Tax taxToAdd) {
        return taxes.put(taxToAdd.getStateName(), taxToAdd);
    }

    @Override
    public Tax getTax(String stateName) {
        return taxes.get(stateName);
    }

    @Override
    public List<Tax> getAllTaxes() {
        return new ArrayList<>(taxes.values());
    }

    @Override
    public Tax removeTax(String stateName) {
        return taxes.remove(stateName);
    }

    @Override
    public void loadTaxFile() throws FilePersistenceException {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new BufferedReader(new FileReader(TAX_FILE)));
            while (scanner.hasNextLine()) {
                String currentLine = scanner.nextLine();
                Tax currentTax = unmarshallTax(currentLine);
                taxes.put(currentTax.getStateName(), currentTax);
            }
        } catch (FileNotFoundException e) {
            throw new FilePersistenceException("Tax data could not be read from the tax Data file");
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    @Override
    public void writeTaxFile() throws FilePersistenceException {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new FileWriter(TAX_FILE));
            for (Tax currentTax : taxes.values()) {
                String taxAsString = marshallTax(currentTax);
                out.println(taxAsString);
                out.flush();
            }
        } catch (IOException e) {
            throw new FilePersistenceException("tax data could not be written to file");
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public Tax unmarshallTax(String taxAsString) {
        Object[] splitString = taxAsString.split(DELIMITER);
        Tax taxFromFile = new Tax();
        taxFromFile.setStateName((String) splitString[0]);
        taxFromFile.setStateCode((String) splitString[1]);
        taxFromFile.setTaxRate(new BigDecimal((String) splitString[2]));

        return taxFromFile;
    }

    public String marshallTax(Tax taxToMarshall) {
        return taxToMarshall.getStateName() + DELIMITER + taxToMarshall.getStateCode()
                + DELIMITER + taxToMarshall.getTaxRate();
    }
}