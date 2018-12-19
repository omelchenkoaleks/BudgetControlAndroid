package com.omelchenkoaleks.budgetcontrol.activities.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.omelchenkoaleks.budgetcontrol.R;
import com.omelchenkoaleks.budgetcontrol.activities.abstracts.TreeListActivity;
import com.omelchenkoaleks.budgetcontrol.activities.edit.EditSourceActivity;
import com.omelchenkoaleks.budgetcontrol.adapters.SourceNodeAdapter;
import com.omelchenkoaleks.budgetcontrol.fragments.TreeNodeListFragment;
import com.omelchenkoaleks.budgetcontrol.utils.AppContext;
import com.omelchenkoaleks.core.database.Initializer;
import com.omelchenkoaleks.core.decorator.SourceSync;
import com.omelchenkoaleks.core.enums.OperationType;
import com.omelchenkoaleks.core.impls.DefaultSource;
import com.omelchenkoaleks.core.interfaces.Source;

// список категорий
public class SourceListActivity extends TreeListActivity<Source> {

    // табы для фильтрации по типам
    private TabLayout tabLayout;

    private OperationType defaultSourceType;// для автоматического проставления типа (доход, расход) при создании нового элемента

    private OperationType listFilterType; // параметр, если нужно показать категории только одного типа (передается извне)



    public SourceListActivity() {

        TreeNodeListFragment<Source> fragment = new TreeNodeListFragment<>();

        // обязательно надо проинициализировать
        init(fragment, R.layout.activity_source_list, R.id.tlb_tree_list_actions);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listFilterType = OperationType.getType(getIntent().getIntExtra(AppContext.LIST_TYPE, -1));

        defaultSourceType = listFilterType;// чтобы при создании нового элемента - сразу указывал нужный тип


        if (listFilterType ==null) {
            fragment.setAdapter(new SourceNodeAdapter(mode));
        }else{
            // если параметр был передан - показываем только категории нужного типа (используется при редактировании операции)
            fragment.setAdapter(new SourceNodeAdapter(mode, Initializer.getSourceSync().getList(listFilterType)));
        }


        setToolbarTitle(getResources().getString(R.string.sources));

    }

    @Override
    protected void initToolbar() {
        super.initToolbar();

        tabLayout = (TabLayout) findViewById(R.id.tabs);

        if (mode == AppContext.EDIT_MODE) {
            tabLayout.setVisibility(View.VISIBLE);
            initTabs();
        }else{
            tabLayout.setVisibility(View.GONE);
        }


    }

    @Override
    protected void initListeners() {
        super.initListeners();

        // при нажатии на кнопку добавления элемента
        iconAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Source source = new DefaultSource();

                // если пользователь выбрал таб и создает новый элемент - сразу прописываем тип
                if (defaultSourceType != null) {
                    source.setOperationType(defaultSourceType);
                }


                Intent intent = new Intent(SourceListActivity.this, EditSourceActivity.class); // какой акивити хоти вызвать
                intent.putExtra(AppContext.NODE_OBJECT, source); // помещаем выбранный объект node для передачи в активити
                startActivityForResult(intent, AppContext.REQUEST_NODE_ADD, ActivityOptionsCompat.makeSceneTransitionAnimation(SourceListActivity.this).toBundle()); // REQUEST_NODE_EDIT - индикатор, кто является инициатором); // REQUEST_NODE_ADD - индикатор, кто является инициатором

            }
        });


    }




    private void initTabs() {

        tabLayout.getTabAt(0).setText(R.string.tab_all);
        tabLayout.getTabAt(1).setText(R.string.tab_income);
        tabLayout.getTabAt(2).setText(R.string.tab_outcome);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                // при переключении табов - сбросить название и убрать стрелку возврата к родительским элементам
                icBack.setVisibility(View.INVISIBLE);
                toolbarTitle.setText(R.string.sources);

                showRootNodes();// показать корневые элементы для выбранного типа (все, доход, расход)


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }

        });



    }

    @Override
    protected void showRootNodes() {
        super.showRootNodes();

        if (mode == AppContext.EDIT_MODE) {

            switch (tabLayout.getSelectedTabPosition()) {
                case 0:// все
                    fragment.refreshList(Initializer.getSourceSync().getAll());
                    defaultSourceType = null;
                    break;
                case 1:// доход
                    fragment.refreshList(Initializer.getSourceSync().getList(OperationType.INCOME));
                    defaultSourceType = OperationType.INCOME;
                    break;
                case 2: // расход
                    fragment.refreshList(Initializer.getSourceSync().getList(OperationType.OUTCOME));
                    defaultSourceType = OperationType.OUTCOME;
                    break;
            }
        }else{
            fragment.refreshList(Initializer.getSourceSync().getList(listFilterType));
        }


    }

    @Override
    protected void showParentNodes() {

        if (selectedNode==null || selectedNode.getParent() == null) {
            if (listFilterType != null){
                defaultSourceType = listFilterType;// если ранее был установлен общие фильтр по типу - используем его
            }else{
                defaultSourceType = null; // если выше нет родительского элемента - значит находимся в корне, сбрасываем тип
            }
        }

        super.showParentNodes();

    }

    @Override
    public void onSwipe(Source node) {
        super.onSwipe(node);
        defaultSourceType =node.getOperationType();// сохраняем тип элемента, над которым сделали свайп, чтобы при создании нового элемента - автоматически прописывать его как родителя
    }

    @Override
    public void onShowChilds(Source node) {
        super.onShowChilds(node);
        defaultSourceType =node.getOperationType();// сохраняем тип элемента, для которого показываем дочерние элементы, чтобы при создании нового элемента - автоматически прописывать его как родителя
    }


}
