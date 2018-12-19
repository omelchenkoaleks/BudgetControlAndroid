package com.omelchenkoaleks.core.interfaces;

import com.omelchenkoaleks.core.enums.OperationType;

public interface Source<T extends Source> extends TreeNode<T>{

    OperationType getOperationType();

    void setOperationType(OperationType type);

}
