package com.omelchenkoaleks.core.abstracts;

import java.util.Calendar;

import com.omelchenkoaleks.core.enums.OperationType;
import com.omelchenkoaleks.core.interfaces.Operation;

public abstract class AbstractOperation implements Operation, Comparable<Operation>{

    private long id;
    private Calendar dateTime; // дата и время выполнения операции (подставлять автоматически при создании, но можно будет изменять в любое время)
    private String description; // доп. информация, которую вводит пользователь
    private OperationType operationType;// тип операции (доход, расход, перевод, конвертация)

    public AbstractOperation(OperationType operationType) {
        this.operationType = operationType;
    }

    public AbstractOperation(long id, Calendar dateTime, String description, OperationType operationType) {
        this.id = id;
        this.dateTime = dateTime;
        this.description = description;
        this.operationType = operationType;
    }

    public AbstractOperation(long id, OperationType operationType) {
        this.id = id;
        this.operationType = operationType;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public Calendar getDateTime() {
        return dateTime;
    }

    public void setDateTime(Calendar dateTime) {
        this.dateTime = dateTime;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    @Override
    public int compareTo(Operation o) {
        return o.getDateTime().compareTo(dateTime);
    }
}
