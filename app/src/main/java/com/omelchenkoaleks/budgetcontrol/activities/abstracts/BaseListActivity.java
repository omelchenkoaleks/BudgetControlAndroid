package com.omelchenkoaleks.budgetcontrol.activities.abstracts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.omelchenkoaleks.budgetcontrol.R;
import com.omelchenkoaleks.budgetcontrol.fragments.BaseNodeListFragment;
import com.omelchenkoaleks.budgetcontrol.listeners.BaseNodeActionListener;
import com.omelchenkoaleks.budgetcontrol.transitions.TransitionSlide;
import com.omelchenkoaleks.budgetcontrol.utils.AppContext;
import com.omelchenkoaleks.core.interfaces.IconNode;

import static com.omelchenkoaleks.budgetcontrol.utils.AppContext.NODE_OBJECT;
import static com.omelchenkoaleks.budgetcontrol.utils.AppContext.REQUEST_NODE_ADD;
import static com.omelchenkoaleks.budgetcontrol.utils.AppContext.REQUEST_NODE_EDIT;

// базовый класс для любого плоского списка справочных значений
public abstract class BaseListActivity<T extends IconNode, F extends BaseNodeListFragment> extends BaseDrawerActivity implements BaseNodeActionListener<T> {


    protected ImageView iconAdd;
    protected Toolbar toolbar;
    protected TextView toolbarTitle;

    protected String defaultToolbarTitle; // имя окна (для разных списков будет подставляться свое название)

    protected F fragment;


    private int listLayoutId; // основной контент списка
    private int toolbarLayoutId; // панель действий, который можно менять в зависимости от создаваемого списка



    // метод работает со специфичными компонентами
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(listLayoutId);

        initFragment();

        initToolbar();

        initListeners();

        // если было передано направление - меняем анимацию
        TransitionSlide.Direction direction = (TransitionSlide.Direction)getIntent().getSerializableExtra(AppContext.TRANSITION_DIRECTION);

        if (direction!=null){
            setTransition(new TransitionSlide(this, direction));
        }

    }

    private void initFragment() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.node_list_fragment, fragment);
        fragmentTransaction.commit();
    }


    // здесь можно инициализировать слушатели
    protected void initListeners(){

    }


    public void setToolbarTitle(String title) {
        toolbarTitle.setText(title);
        defaultToolbarTitle = title;
    }

    // обязательно нужно инициализировать эти поля, иначе ничего не заработает, т.к. все на них основывается
    protected void init(F fragment, int listLayoutId, int toolbarLayoutId) {
        this.fragment = fragment;
        this.listLayoutId = listLayoutId;
        this.toolbarLayoutId = toolbarLayoutId;
    }


    // все компоненты тулбара нужно инициализировать здесь
    protected void initToolbar() {
        toolbar = (Toolbar) findViewById(toolbarLayoutId);
        setSupportActionBar(toolbar);

        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);

        toolbarTitle.setText(defaultToolbarTitle);

        iconAdd = (ImageView) findViewById(R.id.ic_add_node);

    }


    // сюда возврашается результат после редактирования или добавление элемента
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        T node;

        if (resultCode == RESULT_OK) { // если была нажата кнопка сохранения

            switch (requestCode) {
                case REQUEST_NODE_EDIT:
                    fragment.updateNode((T) data.getSerializableExtra(NODE_OBJECT));// отправляем полученный объект на обновление в коллекции и БД
                    break;

                case REQUEST_NODE_ADD:
                    node = (T) data.getSerializableExtra(NODE_OBJECT);
                    addAction(node); // отправляем полученный объект на добавление в коллекции и БД
                    break;
            }

        }

    }

    // данный метод специально выделен как отдельный, чтобы его можно было переопределять в дочерних классах по необходимости
    protected void addAction(T node){
        fragment.addNode(node);// отправляем на добавление новый объект
    }


    // методы слушателя, можно оставить пустыми, чтобы в дочерних классах переопределять только необходимые методы
    @Override
    public void onAdd(T node) {}

    @Override
    public void onSwipe(T node) {}

    @Override
    public void onDelete(T node) {}

    @Override
    public void onUpdate(T node) {}
}
