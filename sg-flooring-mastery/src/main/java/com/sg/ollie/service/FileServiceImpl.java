package com.sg.ollie.service;

import com.sg.ollie.dao.FilePersistenceException;
import com.sg.ollie.dao.OrderFileDao;
import com.sg.ollie.dao.ProductFileDao;
import com.sg.ollie.dao.TaxFileDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FileServiceImpl implements FileService {
    private final OrderFileDao orderFileDao;
    private final TaxFileDao taxFileDao;
    private final ProductFileDao productFileDao;

    @Autowired
    public FileServiceImpl(OrderFileDao orderFileDao, TaxFileDao taxFileDao, ProductFileDao productFileDao) {
        this.orderFileDao = orderFileDao;
        this.taxFileDao = taxFileDao;
        this.productFileDao = productFileDao;
    }

    @Override
    public void loadOrderTaxProductFiles() throws FilePersistenceException {
        orderFileDao.loadOrderDays();
        taxFileDao.loadTaxFile();
        productFileDao.loadProductFile();
    }

    @Override
    public void writeBackupFile() throws FilePersistenceException {
        orderFileDao.writeBackup();
    }
}
