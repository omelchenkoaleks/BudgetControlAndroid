package com.omelchenkoaleks.core.dao.interfaces;

import com.omelchenkoaleks.core.enums.OperationType;
import com.omelchenkoaleks.core.interfaces.Source;

import java.util.List;

public interface SourceDAO extends CommonDAO<Source> {

    String SOURCE_TABLE = "source";

    List<Source> getList(OperationType operationType);// получить список корневых элементов деревьев для определенного типа операции
    int getRefCount(Source source);


}
