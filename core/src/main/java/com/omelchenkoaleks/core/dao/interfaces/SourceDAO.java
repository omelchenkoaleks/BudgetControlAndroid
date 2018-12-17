package com.omelchenkoaleks.core.dao.interfaces;

import com.omelchenkoaleks.core.enums.OperationType;
import com.omelchenkoaleks.core.interfaces.Source;

import java.util.List;

public interface SourceDAO extends CommonDAO<Source> {

    // получить список корневыъ элементов деревьев для определенного типа операции
    List<Source> getList(OperationType operationType);
}
