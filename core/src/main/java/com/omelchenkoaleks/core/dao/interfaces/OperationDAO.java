package com.omelchenkoaleks.core.dao.interfaces;

import com.omelchenkoaleks.core.enums.OperationType;
import com.omelchenkoaleks.core.interfaces.Operation;

import java.util.List;

public interface OperationDAO extends CommonDAO<Operation> {

    String OPERATION_TABLE = "operation";

    List<Operation> getList(OperationType operationType);// получить список операций определенного типа

}
