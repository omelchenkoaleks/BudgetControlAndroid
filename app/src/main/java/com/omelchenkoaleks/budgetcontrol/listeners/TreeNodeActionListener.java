package com.omelchenkoaleks.budgetcontrol.listeners;

// интерфейс для слушателя события при появлении popup меню
// нужен для древовидных списков, где есть дочерние списки
public interface TreeNodeActionListener<T> extends BaseNodeActionListener<T> {

    void returnNodeToOperationActivity(T node); // вызывается, когда справочник находится в режиме выбора (при редактировании операции), чтобы вернуть выбранное значение

    void onShowChilds(T node);

}
