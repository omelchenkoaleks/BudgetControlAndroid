package com.omelchenkoaleks.core.interfaces;

import com.omelchenkoaleks.core.enums.OperationType;

import java.util.Calendar;

public interface Operation extends IconNode {

    long getId();

    void setId(long id);

    OperationType getOperationType();

    Calendar getDateTime();

    String getDescription();

}
