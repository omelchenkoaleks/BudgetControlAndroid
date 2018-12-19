package com.omelchenkoaleks.budgetcontrol.listeners;

import android.support.v7.widget.RecyclerView;
// интерфейс для слушателя события при нажатии на элемент списка
public interface BaseNodeActionListener<T> {

    void onSwipe(T node); // появление свайп меню для записи

    void onDelete(T node);

    void onAdd(T node);

    void onUpdate(T node);


}
