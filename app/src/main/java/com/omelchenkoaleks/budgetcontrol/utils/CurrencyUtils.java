package com.omelchenkoaleks.budgetcontrol.utils;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;


public class CurrencyUtils {

    public static Currency defaultCurrency = Currency.getInstance("RUB");

    public static List<Currency> globalCurrencies = new ArrayList<>();


    static {
        globalCurrencies.add(Currency.getInstance("RUB"));
        globalCurrencies.add(Currency.getInstance("USD"));
        globalCurrencies.add(Currency.getInstance("KZT"));

    }







}
