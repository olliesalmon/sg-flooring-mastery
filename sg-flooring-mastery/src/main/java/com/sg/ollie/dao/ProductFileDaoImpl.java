package com.sg.ollie.dao;

import com.sg.ollie.dto.Product;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

@Component
public class ProductFileDaoImpl implements ProductFileDao {
    public final String DIRECTORY;
    public final String PRODUCT_FILE;
    public static final String DELIMITER = ",";
    private final Map<String, Product> products = new HashMap<>();

    public ProductFileDaoImpl() {
        DIRECTORY = "Data/";
        PRODUCT_FILE = DIRECTORY + "Products.txt";
    }
    public ProductFileDaoImpl(String productFile, String directory) {
        DIRECTORY = directory;
        PRODUCT_FILE = productFile;
    }

    @Override
    public Product addProduct(Product productToAdd) {
        return products.put(productToAdd.getProductType(), productToAdd);
    }

    @Override
    public Product getProduct(String productType) {
        return products.get(productType);
    }

    @Override
    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }

    @Override
    public Product removeProduct(String productType) {
        return products.remove(productType);
    }

    @Override
    public void loadProductFile() throws FilePersistenceException {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new BufferedReader(new FileReader(PRODUCT_FILE)));
            while (scanner.hasNextLine()) {
                String currentLine = scanner.nextLine();
                Product currentProduct = unmarshallProduct(currentLine);
                products.put(currentProduct.getProductType(), currentProduct);
            }
        } catch (FileNotFoundException e) {
            throw new FilePersistenceException("Product data could not be read from the Product Data file");
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    @Override
    public void writeProductFile() throws FilePersistenceException {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new FileWriter(PRODUCT_FILE));
            for (Product currentProduct : products.values()) {
                String productAsString = marshallProduct(currentProduct);
                out.println(productAsString);
                out.flush();
            }
        } catch (IOException e) {
            throw new FilePersistenceException("Product data could not be written to file");
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public Product unmarshallProduct(String productAsString) {
        Object[] splitString = productAsString.split(DELIMITER);
        Product productFromFile = new Product();
        productFromFile.setProductType((String) splitString[0]);
        productFromFile.setCostPerSquareFoot(new BigDecimal((String) splitString[1]));
        productFromFile.setLabourCostPerSquareFoot(new BigDecimal((String) splitString[2]));

        return productFromFile;
    }

    public String marshallProduct(Product productToMarshall) {
        return productToMarshall.getProductType() + DELIMITER + productToMarshall.getCostPerSquareFoot()
                + DELIMITER + productToMarshall.getLabourCostPerSquareFoot();
    }
}