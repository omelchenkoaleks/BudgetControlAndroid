package com.omelchenkoaleks.budgetcontrol.activities.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.omelchenkoaleks.budgetcontrol.R;
import com.omelchenkoaleks.budgetcontrol.activities.abstracts.BaseListActivity;
import com.omelchenkoaleks.budgetcontrol.activities.edit.operation.EditConvertOperationActivity;
import com.omelchenkoaleks.budgetcontrol.activities.edit.operation.EditIncomeOperationActivity;
import com.omelchenkoaleks.budgetcontrol.activities.edit.operation.EditOutcomeOperationActivity;
import com.omelchenkoaleks.budgetcontrol.activities.edit.operation.EditTransferOperationActivity;
import com.omelchenkoaleks.budgetcontrol.adapters.OperationListAdapter;
import com.omelchenkoaleks.budgetcontrol.fragments.BaseNodeListFragment;
import com.omelchenkoaleks.budgetcontrol.listeners.BaseNodeActionListener;
import com.omelchenkoaleks.budgetcontrol.transitions.TransitionSlide;
import com.omelchenkoaleks.budgetcontrol.utils.AppContext;
import com.omelchenkoaleks.core.database.Initializer;
import com.omelchenkoaleks.core.decorator.OperationSync;
import com.omelchenkoaleks.core.impls.operations.ConvertOperation;
import com.omelchenkoaleks.core.impls.operations.IncomeOperation;
import com.omelchenkoaleks.core.impls.operations.OutcomeOperation;
import com.omelchenkoaleks.core.impls.operations.TransferOperation;
import com.omelchenkoaleks.core.interfaces.Operation;

// содержит список операций
public class OperationListActivity extends BaseListActivity<Operation, BaseNodeListFragment> {

    private Button btnAddIncome;
    private Button btnAddOutcome;
    private Button btnAddTransfer;
    private Button btnAddConvert;


    public OperationListActivity() {

        BaseNodeListFragment<Operation, OperationListAdapter, BaseNodeActionListener> fragment = new BaseNodeListFragment<>();
        fragment.setAdapter(new OperationListAdapter());

        // обязательно надо проинициализировать
        init(fragment, R.layout.activity_operation_list, R.id.tlb_operation_list_actions);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setToolbarTitle(getResources().getString(R.string.operations));

        setTransition(new TransitionSlide(this, TransitionSlide.Direction.RIGHT_LEFT));

        createDrawer(toolbar);


        btnAddIncome = (Button) findViewById(R.id.btn_add_income);
        btnAddOutcome = (Button) findViewById(R.id.btn_add_outcome);
        btnAddTransfer = (Button) findViewById(R.id.btn_add_transfer);
        btnAddConvert = (Button) findViewById(R.id.btn_add_convert);


        // каждая кнопка создает свой операцию нужного типа
        btnAddIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runAddOperationActivity(EditIncomeOperationActivity.class, new IncomeOperation());
            }
        });

        btnAddOutcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runAddOperationActivity(EditOutcomeOperationActivity.class, new OutcomeOperation());
            }
        });


        btnAddTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runAddOperationActivity(EditTransferOperationActivity.class, new TransferOperation());
            }
        });


        btnAddConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runAddOperationActivity(EditConvertOperationActivity.class, new ConvertOperation());
            }
        });


    }




    // какой активити хотим вызвать для добавления новой операции (в зависимости от типа операции)
    private void runAddOperationActivity(Class activityClass, Operation operation) {
        Intent intent = new Intent(this, activityClass);
        intent.putExtra(AppContext.NODE_OBJECT, operation); // помещаем выбранный объект operation для передачи в активити
        intent.putExtra(AppContext.OPERATION_ACTION, AppContext.OPERATION_ADD);
        startActivityForResult(intent, AppContext.REQUEST_NODE_ADD, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
    }





}


