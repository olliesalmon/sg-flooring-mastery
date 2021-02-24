package com.sg.ollie.dao;

import com.sg.ollie.dto.Product;

import java.util.List;

public interface ProductFileDao {
    Product addProduct(Product productToAdd);

    Product getProduct(String productType);

    List<Product> getAllProducts();

    Product removeProduct(String productType);

    void loadProductFile() throws FilePersistenceException;

    void writeProductFile() throws FilePersistenceException;
}