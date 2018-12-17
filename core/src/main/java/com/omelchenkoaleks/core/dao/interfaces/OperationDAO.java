package com.omelchenkoaleks.core.dao.interfaces;

import com.omelchenkoaleks.core.enums.OperationType;
import com.omelchenkoaleks.core.interfaces.Operation;

import java.util.List;

public interface OperationDAO extends CommonDAO<Operation> {

    List<Operation> getList(OperationType operationType);
}
