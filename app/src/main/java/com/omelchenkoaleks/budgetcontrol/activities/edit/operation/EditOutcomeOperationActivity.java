package com.omelchenkoaleks.budgetcontrol.activities.edit.operation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.omelchenkoaleks.budgetcontrol.R;
import com.omelchenkoaleks.budgetcontrol.activities.abstracts.BaseEditOperationActivity;
import com.omelchenkoaleks.budgetcontrol.activities.list.SourceListActivity;
import com.omelchenkoaleks.budgetcontrol.activities.list.StorageListActivity;
import com.omelchenkoaleks.budgetcontrol.transitions.TransitionSlide;
import com.omelchenkoaleks.budgetcontrol.utils.AppContext;
import com.omelchenkoaleks.budgetcontrol.utils.ColorUtils;
import com.omelchenkoaleks.budgetcontrol.utils.IconUtils;
import com.omelchenkoaleks.budgetcontrol.utils.OperationTypeUtils;
import com.omelchenkoaleks.core.enums.OperationType;
import com.omelchenkoaleks.core.impls.operations.OutcomeOperation;
import com.omelchenkoaleks.core.interfaces.Source;
import com.omelchenkoaleks.core.interfaces.Storage;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;


// отвечает за добавление и редактирование операции Расход
public class EditOutcomeOperationActivity extends BaseEditOperationActivity<OutcomeOperation> {


    protected TextView tvOperationSource;
    protected ViewGroup layoutOperationSource;
    protected ImageView icOperationSource;

    protected TextView tvOperationStorage;
    protected ViewGroup layoutOperationStorage;
    protected ImageView icOperationStorage;


    protected EditText etOperationAmount;
    protected Spinner spnCurrency;


    public EditOutcomeOperationActivity() {
        super(R.layout.activity_edit_outcome_operation);// какой макет будет использоваться
    }


    // список возможных валют для операции
    private List<Currency> currencyList = new ArrayList<>();
    private ArrayAdapter<Currency> currencyAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initComponents();

        tvOperationType.setText(OperationTypeUtils.outcomeType.toString());
        tvOperationType.setBackgroundColor(getBaseContext().getColor(ColorUtils.outcomeColor));

        if (actionType == AppContext.OPERATION_EDIT) {
            setValues();
        }


        // для выбора справочного значения storage
        layoutOperationStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentNodeSelect = tvOperationStorage;

                Intent intent = new Intent(EditOutcomeOperationActivity.this, StorageListActivity.class);

                // какую анимацию использовать при открытии
                intent.putExtra(AppContext.TRANSITION_DIRECTION, TransitionSlide.Direction.RIGHT_LEFT);

                // передаем SELECT_MODE, чтобы выбранный объект возвращался обратно в этот активити
                intent.putExtra(AppContext.LIST_VIEW_MODE, AppContext.SELECT_MODE);
                startActivityForResult(intent, REQUEST_SELECT_STORAGE_FROM, ActivityOptionsCompat.makeSceneTransitionAnimation(EditOutcomeOperationActivity.this).toBundle());


            }


        });


        // для выбора справочного значения source
        layoutOperationSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentNodeSelect = tvOperationSource;

                Intent intent = new Intent(EditOutcomeOperationActivity.this, SourceListActivity.class);

                // какую анимацию использовать при открытии
                intent.putExtra(AppContext.TRANSITION_DIRECTION, TransitionSlide.Direction.RIGHT_LEFT);

                // передаем SELECT_MODE, чтобы выбранный объект возвращался обратно в этот активити
                intent.putExtra(AppContext.LIST_VIEW_MODE, AppContext.SELECT_MODE);

                // параметр для фильтрации списка по типу (чтобы не все source показывал, а только по этому типу)
                intent.putExtra(AppContext.LIST_TYPE, OperationType.OUTCOME.getId());// передаем параметр, который позволит выбирать значение и возвращать его
                startActivityForResult(intent, REQUEST_SELECT_SOURCE_TO, ActivityOptionsCompat.makeSceneTransitionAnimation(EditOutcomeOperationActivity.this).toBundle());


            }


        });


        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkValues()) {
                    return;
                }

                operation.setFromAmount(convertString(etOperationAmount.getText().toString()));

                operation.setDescription(etOperationDesc.getText().toString());
                operation.setDateTime(calendar);
                operation.setFromCurrency((Currency) spnCurrency.getSelectedItem());


                Intent intent = new Intent();
                intent.putExtra(AppContext.TRANSITION_DIRECTION, TransitionSlide.Direction.RIGHT_LEFT);
                intent.putExtra(AppContext.NODE_OBJECT, operation);// сюда попадает уже отредактированный объект, который нужно сохранить в БД
                setResult(RESULT_OK, intent);


                transition.finishWithTransition();// закрыть активити с анимацией

            }


        });


    }

    private void setValues() {

        initCurrencySpinner();

        tvOperationSource.setText(operation.getToSource().getName().toUpperCase());
        tvOperationStorage.setText(operation.getFromStorage().getName().toUpperCase());
        etOperationAmount.setText(operation.getFromAmount().toString());

        icOperationSource.setImageDrawable(IconUtils.getIcon(operation.getToSource().getIconName()));
        icOperationStorage.setImageDrawable(IconUtils.getIcon(operation.getFromStorage().getIconName()));
    }

    private void initComponents() {
        icOperationSource = (ImageView) findViewById(R.id.ic_operation_source_selected);
        icOperationStorage = (ImageView) findViewById(R.id.ic_operation_storage_selected);

        tvOperationSource = (TextView) findViewById(R.id.tv_operation_source_selected);
        tvOperationStorage = (TextView) findViewById(R.id.tv_operation_storage_selected);
        etOperationAmount = (EditText) findViewById(R.id.et_operation_amount_selected);

        layoutOperationSource = (ViewGroup) findViewById(R.id.layout_operation_source);
        layoutOperationStorage = (ViewGroup) findViewById(R.id.layout_operation_storage);

        spnCurrency = (Spinner) findViewById(R.id.spn_currency);
        currencyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, currencyList);
        spnCurrency.setAdapter(currencyAdapter);

    }


    // проверяет, заполнены ли обязательные значения
    private boolean checkValues() {


        // не давать сохранять пустое значение
        if (etOperationAmount.getText().length() == 0) {
            Toast.makeText(EditOutcomeOperationActivity.this, R.string.enter_name, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etOperationAmount.getText().length() == 0) {
            Toast.makeText(EditOutcomeOperationActivity.this, R.string.enter_amount, Toast.LENGTH_SHORT).show();
            return false;
        }


        if (operation.getToSource() == null) {
            Toast.makeText(EditOutcomeOperationActivity.this, R.string.select_source_to, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (operation.getFromStorage() == null) {
            Toast.makeText(EditOutcomeOperationActivity.this, R.string.select_storage_from, Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;// если все проверки прошли успешно - возвращается true

    }


    // заполнить выпадающий список значениями валют
    private void initCurrencySpinner() {
        currencyList.clear();
        currencyList.addAll(operation.getFromStorage().getAvailableCurrencies());

        currencyAdapter.clear();
        currencyAdapter.addAll(operation.getFromStorage().getAvailableCurrencies());

        currencyAdapter.notifyDataSetChanged();
        spnCurrency.setSelection(currencyList.indexOf(operation.getFromCurrency()));
    }


    // сюда попадаем, когда возвращается результат выбора какого либо справочного значения
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case REQUEST_SELECT_STORAGE_FROM: // если выбирали storage
                    Storage storage = (Storage) data.getSerializableExtra(AppContext.NODE_OBJECT);
                    operation.setFromStorage(storage);
                    icOperationStorage.setImageDrawable(IconUtils.getIcon(storage.getIconName()));
                    currentNodeSelect.setText(storage.getName().toUpperCase());

                    updateCurrencyList(storage, currencyList, currencyAdapter, spnCurrency);


                    break;


                case REQUEST_SELECT_SOURCE_TO: // если выбирали source
                    Source source = (Source) data.getSerializableExtra(AppContext.NODE_OBJECT);
                    operation.setToSource(source);
                    icOperationSource.setImageDrawable(IconUtils.getIcon(source.getIconName()));
                    currentNodeSelect.setText(source.getName().toUpperCase());
                    break;


            }


        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (operation.getFromStorage() != null) {
            updateCurrencyList(operation.getFromStorage(), currencyList, currencyAdapter, spnCurrency);
        }


    }
}
