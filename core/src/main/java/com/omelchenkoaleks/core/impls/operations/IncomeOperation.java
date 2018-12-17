package com.omelchenkoaleks.core.impls.operations;

import com.omelchenkoaleks.core.abstracts.AbstractOperation;
import com.omelchenkoaleks.core.enums.OperationType;
import com.omelchenkoaleks.core.interfaces.Source;
import com.omelchenkoaleks.core.interfaces.Storage;

import java.math.BigDecimal;
import java.util.Currency;

// TODO: для всех классов создать конструкторы без поля id, т.к. у  нас будет autoincrement
public class IncomeOperation extends AbstractOperation {

    public IncomeOperation() {
        super(OperationType.INCOME);
    }


    private Source fromSource; // откула пришли деньги
    private Storage toStorage; // куда положили деньги
    private BigDecimal fromAmount; // сумма получения
    private Currency fromCurrency; // в какой валюте получили деньги

    public Source getFromSource() {
        return fromSource;
    }

    public void setFromSource(Source fromSource) {
        this.fromSource = fromSource;
    }

    public Storage getToStorage() {
        return toStorage;
    }

    public void setToStorage(Storage toStorage) {
        this.toStorage = toStorage;
    }

    public BigDecimal getFromAmount() {
        return fromAmount;
    }

    public void setFromAmount(BigDecimal fromAmount) {
        this.fromAmount = fromAmount;
    }

    public Currency getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(Currency fromCurrency) {
        this.fromCurrency = fromCurrency;
    }
}
