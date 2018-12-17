package com.omelchenkoaleks.core.database;

import com.omelchenkoaleks.core.dao.impls.OperationDAOImpl;
import com.omelchenkoaleks.core.dao.impls.SourceDAOImpl;
import com.omelchenkoaleks.core.dao.impls.StorageDAOImpl;
import com.omelchenkoaleks.core.decorator.OperationSync;
import com.omelchenkoaleks.core.decorator.SourceSync;
import com.omelchenkoaleks.core.decorator.StorageSync;

public class Initializer {

    private static OperationSync operationSync;
    private static SourceSync sourceSync;
    private static StorageSync storageSync;


    public static OperationSync getOperationSync() {
        return operationSync;
    }


    public static SourceSync getSourceSync() {
        return sourceSync;
    }


    public static StorageSync getStorageSync() {
        return storageSync;
    }


    public static void load(String driverName, String url){

        SQLiteConnection.init(driverName, url);

        // последовательность создания объектов - важна (сначала справочные значения, потом операции)
        sourceSync = new SourceSync(new SourceDAOImpl());
        storageSync = new StorageSync(new StorageDAOImpl());
        operationSync = new OperationSync(new OperationDAOImpl(sourceSync.getIdentityMap(), storageSync.getIdentityMap()), sourceSync, storageSync);
    }

}
