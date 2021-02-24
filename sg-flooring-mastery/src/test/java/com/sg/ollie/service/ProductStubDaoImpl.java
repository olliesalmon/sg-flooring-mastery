package com.sg.ollie.service;

import com.sg.ollie.dao.FilePersistenceException;
import com.sg.ollie.dao.ProductFileDao;
import com.sg.ollie.dto.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProductStubDaoImpl implements ProductFileDao {
    public Product onlyProduct;

    public ProductStubDaoImpl() {
        onlyProduct = new Product();
        onlyProduct.setProductType("ProductOne");
        onlyProduct.setCostPerSquareFoot(new BigDecimal("10"));
        onlyProduct.setLabourCostPerSquareFoot(new BigDecimal("10"));
    }

    @Override
    public Product addProduct(Product productToAdd) {
        if (productToAdd.getProductType().equals(onlyProduct.getProductType())) {
            return onlyProduct;
        }
        else return null;
    }

    @Override
    public Product getProduct(String productType) {
        if (productType.equals(onlyProduct.getProductType())) {
            return onlyProduct;
        }
        else return null;
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        productList.add(onlyProduct);
        return productList;
    }

    @Override
    public Product removeProduct(String productType) {
        if (productType.equals(onlyProduct.getProductType())) {
            return onlyProduct;
        }
        else return null;
    }

    @Override
    public void loadProductFile() throws FilePersistenceException {
        //void so does not return anything
    }

    @Override
    public void writeProductFile() throws FilePersistenceException {
        //void so does not return anything
    }
}