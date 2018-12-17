//package com.omelchenkoaleks.budgetcontrol.list;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.design.widget.CoordinatorLayout;
//import android.support.design.widget.FloatingActionButton;
//import android.support.v4.app.ActivityOptionsCompat;
//import android.support.v7.widget.RecyclerView;
//import android.view.Gravity;
//import android.view.Menu;
//import android.view.View;
//import android.view.animation.AnimationUtils;
//import android.widget.Toast;
//
//import com.omelchenkoaleks.budgetcontrol.R;
//import com.omelchenkoaleks.budgetcontrol.app.AppContext;
//import com.omelchenkoaleks.core.database.Initializer;
//import com.omelchenkoaleks.core.decorator.OperationSync;
//import com.omelchenkoaleks.core.impls.operations.ConvertOperation;
//import com.omelchenkoaleks.core.impls.operations.IncomeOperation;
//import com.omelchenkoaleks.core.impls.operations.OutcomeOperation;
//import com.omelchenkoaleks.core.impls.operations.TransferOperation;
//import com.omelchenkoaleks.core.interfaces.Operation;
//
//import com.github.clans.fab.FloatingActionButton;
//import com.github.clans.fab.FloatingActionMenu;
//import com.miguelcatalan.materialsearchview.MaterialSearchView;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.design.widget.CoordinatorLayout;
//import android.support.design.widget.FloatingActionButton;
//import android.support.v4.app.ActivityOptionsCompat;
//import android.support.v7.widget.RecyclerView;
//import android.view.Gravity;
//import android.view.Menu;
//import android.view.View;
//import android.view.animation.AnimationUtils;
//import android.widget.Toast;
//
//import com.omelchenkoaleks.budgetcontrol.R;
//import com.omelchenkoaleks.budgetcontrol.app.AppContext;
//import com.omelchenkoaleks.core.database.Initializer;
//import com.omelchenkoaleks.core.decorator.OperationSync;
//import com.omelchenkoaleks.core.impls.operations.ConvertOperation;
//import com.omelchenkoaleks.core.impls.operations.IncomeOperation;
//import com.omelchenkoaleks.core.impls.operations.OutcomeOperation;
//import com.omelchenkoaleks.core.impls.operations.TransferOperation;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.design.widget.CoordinatorLayout;
//import android.support.design.widget.FloatingActionButton;
//import android.support.v4.app.ActivityOptionsCompat;
//import android.support.v7.widget.RecyclerView;
//import android.view.Gravity;
//import android.view.Menu;
//import android.view.View;
//import android.view.animation.AnimationUtils;
//import android.widget.Toast;
//
//import com.omelchenkoaleks.budgetcontrol.R;
//import com.omelchenkoaleks.budgetcontrol.app.AppContext;
//import com.omelchenkoaleks.core.database.Initializer;
//import com.omelchenkoaleks.core.decorator.OperationSync;
//import com.omelchenkoaleks.core.impls.operations.ConvertOperation;
//import com.omelchenkoaleks.core.impls.operations.IncomeOperation;
//import com.omelchenkoaleks.core.impls.operations.OutcomeOperation;
//import com.omelchenkoaleks.core.impls.operations.TransferOperation;
//
//// содержит список операций
//public class OperationListActivity extends BaseListActivity<Operation, BaseNodeListFragment, OperationSync> {
//
//
//    private FloatingActionMenu menuCreateOperation;
//    private FloatingActionButton fabIncome;
//    private FloatingActionButton fabOutcome;
//    private FloatingActionButton fabTransfer;
//    private FloatingActionButton fabConvert;
//
//
//    public OperationListActivity() {
//
//        BaseNodeListFragment<Operation, OperationListAdapter, BaseNodeActionListener> fragment = new BaseNodeListFragment<>();
//
//        // обязательно надо проинициализировать
//        init(fragment, R.layout.activity_operation_list, R.id.tlb_operation_list_actions, Initializer.getOperationSync());
//
//    }
//
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        fragment.setAdapter(new OperationListAdapter()); // coordView передаем, чтобы snackbar при показе не закрывал floating action button
//
//        setToolbarTitle(getResources().getString(R.string.operations));
//
//        setSearchTitle(getResources().getString(R.string.operation_search_hint));
//
//        setTransition(new TransitionSlide(this, TransitionSlide.Direction.RIGHT_LEFT));
//
//        createDrawer(toolbar);
//
//        menuCreateOperation.setMenuButtonShowAnimation(AnimationUtils.loadAnimation(this, R.anim.show_from_bottom));
//        menuCreateOperation.setMenuButtonHideAnimation(AnimationUtils.loadAnimation(this, R.anim.hide_to_bottom));
//
//    }
//
//
//    // какой активити хотим вызвать для добавления новой операции (в зависимости от типа операции)
//    private void runAddOperationActivity(Class activityClass, Operation operation) {
//        Intent intent = new Intent(this, activityClass);
//        intent.putExtra(AppContext.NODE_OBJECT, operation); // помещаем выбранный объект operation для передачи в активити
//        intent.putExtra(AppContext.OPERATION_ACTION, AppContext.OPERATION_ADD);
//        startActivityForResult(intent, AppContext.REQUEST_NODE_ADD, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
//    }
//
//
//    @Override
//    protected void initSearchView(Menu menu) {
//
//        super.initSearchView(menu);
//
//
//        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//
//                searchView.clearFocus();// чтобы только 1 раз вызывал метод onQueryTextSubmit
//
//                if (query.trim().length() == 0) {
//                    Toast.makeText(OperationListActivity.this, R.string.enter_search_string, Toast.LENGTH_SHORT).show();
//                    return false;
//                }
//
//
//                search(query);
//
//                return true;
//
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
//
//        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
//            @Override
//            public void onSearchViewShown() {
//                menuCreateOperation.hideMenu(true);
//            }
//
//            @Override
//            public void onSearchViewClosed() {
//                menuCreateOperation.showMenu(true);
//                if (asyncSearch != null && asyncSearch.isSearched()) {// если был поиск и список был обновлен
//                    fragment.refreshList(Initializer.getOperationSync().getAll(), false);// возвращаем обратно старый список, который был до поиска
//                }
//
//            }
//        });
//
//
//    }
//
//
//    @Override
//    protected void initListeners() {
//
//        super.initListeners();
//
//        menuCreateOperation.setOnMenuButtonClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                menuCreateOperation.toggle(true);
//            }
//        });
//
//
//        // каждая кнопка создает свой операцию нужного типа
//        fabIncome.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                runAddOperationActivity(EditIncomeOperationActivity.class, new IncomeOperation());
//            }
//        });
//
//        fabOutcome.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                runAddOperationActivity(EditOutcomeOperationActivity.class, new OutcomeOperation());
//            }
//        });
//
//
//        fabTransfer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                runAddOperationActivity(EditTransferOperationActivity.class, new TransferOperation());
//            }
//        });
//
//
//        fabConvert.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                runAddOperationActivity(EditConvertOperationActivity.class, new ConvertOperation());
//            }
//        });
//
//
//    }
//
//    @Override
//    protected void findComponents() {
//        super.findComponents();
//        menuCreateOperation = (FloatingActionMenu) findViewById(R.id.fab_menu_create_operation);
//        menuCreateOperation.setClosedOnTouchOutside(true); // если нажмем на любую область - меню закроется
//
//        CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT);
//        params.gravity = Gravity.CENTER_HORIZONTAL;
//
//        menuCreateOperation.setLayoutParams(params);
//
//        fabIncome = (FloatingActionButton) findViewById(R.id.fab_create_operation_income);
//        fabOutcome = (FloatingActionButton) findViewById(R.id.fab_create_operation_outcome);
//        fabTransfer = (FloatingActionButton) findViewById(R.id.fab_create_operation_transfer);
//        fabConvert = (FloatingActionButton) findViewById(R.id.fab_create_operation_convert);
//
//
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (menuCreateOperation.isOpened()) {
//            menuCreateOperation.toggle(true);
//        }
//    }
//
//    @Override
//    public void onShowSnackBar() {
//        super.onShowSnackBar();
//        menuCreateOperation.hideMenu(true);
//    }
//
//    @Override
//    public void onHideSnackBar() {
//        super.onHideSnackBar();
//        menuCreateOperation.showMenu(true);
//    }
//
//
//    @Override
//    public void onScroll(RecyclerView recyclerView, int dx, int dy) {
//        if (dy > 0) {// скорллинг вверх
//            menuCreateOperation.hideMenu(true);
//        } else if (dy <= 0) { // скорллинг вниз
//            menuCreateOperation.showMenu(true);
//        }
//    }
//
//
//
//}
//
//
