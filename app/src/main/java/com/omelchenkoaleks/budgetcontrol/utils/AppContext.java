package com.omelchenkoaleks.budgetcontrol.utils;

import android.app.Application;

public class AppContext extends Application {

    public final static String NODE_OBJECT = "NodeObject";

    // на что именно был запрос - для правильной обработки возвращаемого результата
    public final static int REQUEST_NODE_EDIT = 1;
    public final static int REQUEST_NODE_ADD = 2;
    public final static int REQUEST_NODE_ADD_CHILD = 3;
    public final static int REQUEST_SELECT_ICON = 4;

    public static final String DATE_CALENDAR = "DateCalendar";
    public final static String TRANSITION_DIRECTION = "TransitionDirection"; // направление анимации
    public static final String LIST_TYPE = "ListType"; // для фильтрации списка справочных значений


    // режимы работы с операцией
    public final static String OPERATION_ACTION = "OperationAction"; // ключ для Intent
    // значения
    public final static int OPERATION_EDIT = 201;
    public final static int OPERATION_ADD = 202;


    public static final String ICON_NAME = "IconName"; // выбранное имя иконки при редактировании справочного значения

    // режимы работы со справочниками
    public static final String LIST_VIEW_MODE = "ListViewMode"; // ключ для Intent
    // значения
    public static final int SELECT_MODE = 100; // в этом режиме при выборе значения, у которого нет дочерних элементов, будет выполнять возврат этого объекта в активити (нужно при создании или редактировании операции)
    public static final int EDIT_MODE = 101;   // в этом режиме при выборе значения, у которого нет дочерних элементов, будет выполнять редактирование


    @Override
    public void onCreate() {
        super.onCreate();
        IconUtils.fillIcons(this); // один раз при загрузке приложения загружаем иконки
        OperationTypeUtils.init(this); // локализованные обертки для типов операций
        LocaleUtils.defaultLocale = getResources().getConfiguration().locale; // сохранить локаль для всего приложения
    }


}
