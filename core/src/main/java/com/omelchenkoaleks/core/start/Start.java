package com.omelchenkoaleks.core.start;

import com.omelchenkoaleks.core.dao.impls.OperationDAOImpl;
import com.omelchenkoaleks.core.dao.impls.SourceDAOImpl;
import com.omelchenkoaleks.core.dao.impls.StorageDAOImpl;
import com.omelchenkoaleks.core.decorator.OperationSync;
import com.omelchenkoaleks.core.decorator.SourceSync;
import com.omelchenkoaleks.core.decorator.StorageSync;

public class Start {

    private static StorageSync storageSync = new StorageSync(new StorageDAOImpl());
    private static SourceSync sourceSync = new SourceSync(new SourceDAOImpl());
    private static OperationSync operationSync = new OperationSync(new OperationDAOImpl(sourceSync.getIdentityMap(), storageSync.getIdentityMap()), sourceSync, storageSync);


    public static void main(String[] args) {

//        try {
//
//
////            testOutcome();
////
////            testIncome();
////
////
////            testTransfer();
////
////
////            testConvert();
//
//            int id=40;
//
////            ((IncomeOperation)operationSync.get(id)).setFromAmount(new BigDecimal(100));
////            ((IncomeOperation)operationSync.get(id)).setFromCurrency(Currency.getInstance("RUB"));
////            ((IncomeOperation)operationSync.get(id)).setToStorage(storageSync.get(10));
//            operationSync.update(operationSync.get(id));
//
//            operationSync.getAll();
//
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//
    }
//
//    private static void testOutcome() throws CurrencyException {
//        OutcomeOperation operation = new OutcomeOperation();
//        operation.setFromCurrency(storageSync.get(9).getCurrency("RUB"));
//        operation.setFromAmount(new BigDecimal(900));
//        operation.setFromStorage(storageSync.get(9));
//        operation.setToSource(sourceSync.get(13));
//        operation.setDateTime(Calendar.getInstance());
//        operation.setDescription("test desc");
//
//        operationSync.add(operation);
//    }
//
//    private static void testIncome() throws CurrencyException {
//        IncomeOperation operation = new IncomeOperation();
//        operation.setFromCurrency(storageSync.get(9).getCurrency("RUB"));
//        operation.setFromAmount(new BigDecimal(10));
//        operation.setToStorage(storageSync.get(9));
//        operation.setFromSource(sourceSync.get(10));
//        operation.setDateTime(Calendar.getInstance());
//        operation.setDescription("test desc income");
//
//        operationSync.add(operation);
//
//    }
//
//    private static void testTransfer() throws CurrencyException {
//        TransferOperation operation = new TransferOperation();
//        operation.setFromCurrency(storageSync.get(9).getCurrency("RUB"));
//        operation.setFromAmount(new BigDecimal(3));
//        operation.setFromStorage(storageSync.get(9));
//        operation.setToStorage(storageSync.get(10));
//        operation.setDateTime(Calendar.getInstance());
//        operation.setDescription("test desc transfer");
//
//        operationSync.add(operation);
//
//    }
//
//    private static void testConvert() throws CurrencyException {
//        Storage s1 = storageSync.get(9);
//        Storage s2 = storageSync.get(10);
//
//        ConvertOperation operation = new ConvertOperation();
//
//        operation.setFromCurrency(s1.getCurrency("RUB"));
//        operation.setFromAmount(new BigDecimal(800));
//        operation.setFromStorage(s1);
//
//        operation.setToCurrency(s2.getCurrency("USD"));
//        operation.setToAmount(new BigDecimal(200));
//        operation.setToStorage(s2);
//
//
//        operation.setDateTime(Calendar.getInstance());
//        operation.setDescription("test desc transfer");
//
//        operationSync.add(operation);
//
//
//    }
//
//    private static void testStorage() {
//        Storage parentStorage = storageSync.get(10);
//
//
//        DefaultStorage storage = new DefaultStorage("def store");
//
//
//        try {
//            storage.addCurrency(Currency.getInstance("USD"), new BigDecimal(145));
//            storage.addCurrency(Currency.getInstance("RUB"), new BigDecimal(100));
//
//            storage.setParent(parentStorage);
//
//            storageSync.add(storage);
//
//
//
//            //storageSync.deleteCurrency(storage, Currency.getInstance("USD"));
//
//
//            storage.setName("test 2");
//
//            storageSync.update(storage);
//
//
//
//
//        } catch (CurrencyException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static DefaultSource testSource() {
//        Source parentSource = sourceSync.get(4);
//
//        DefaultSource s = new DefaultSource("test source 2");
//        s.setOperationType(OperationType.OUTCOME);
//        s.setParent(parentSource);
//
//        sourceSync.add(s);
//        System.out.println("sourceSync = " + sourceSync.getAll());
//        return s;
//    }

}

