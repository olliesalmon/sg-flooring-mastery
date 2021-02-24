package com.sg.ollie.service;

import com.sg.ollie.dao.FilePersistenceException;

public interface FileService {
    void loadOrderTaxProductFiles() throws FilePersistenceException;

    void writeBackupFile() throws FilePersistenceException;
}
