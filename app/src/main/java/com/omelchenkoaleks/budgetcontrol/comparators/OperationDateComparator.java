package com.omelchenkoaleks.budgetcontrol.comparators;

import com.omelchenkoaleks.core.interfaces.Operation;

import java.util.Comparator;

public class OperationDateComparator implements Comparator<Operation>{

    private static OperationDateComparator instance;

    public static OperationDateComparator getInstance() {
        return instance == null ? instance = new OperationDateComparator() : instance;
    }

    private OperationDateComparator(){}


    @Override
    public int compare(Operation o1, Operation o2) {
        return o2.getDateTime().compareTo(o1.getDateTime());
    }

}
