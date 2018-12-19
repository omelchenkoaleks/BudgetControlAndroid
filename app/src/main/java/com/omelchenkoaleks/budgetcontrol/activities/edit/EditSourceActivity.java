package com.omelchenkoaleks.budgetcontrol.activities.edit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.omelchenkoaleks.budgetcontrol.R;
import com.omelchenkoaleks.budgetcontrol.activities.abstracts.BaseEditNodeActivity;
import com.omelchenkoaleks.budgetcontrol.objects.LocalizedOperationType;
import com.omelchenkoaleks.budgetcontrol.utils.AppContext;
import com.omelchenkoaleks.budgetcontrol.utils.OperationTypeUtils;
import com.omelchenkoaleks.core.enums.OperationType;
import com.omelchenkoaleks.core.interfaces.Source;

import java.util.ArrayList;
import java.util.List;

// отвечает за добавление и редактирование категории
public class EditSourceActivity extends BaseEditNodeActivity<Source> {

    private Spinner spSourceType;
    private ArrayAdapter<LocalizedOperationType> spAdapter; // локализованные названия типов операций

    public EditSourceActivity() {
        super(R.layout.activity_edit_source);// какой макет будет использоваться
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // создает компонент выпадающего списка с типами
        createTypesSpinner();


        // слушатель события при сохранении
        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newName = etName.getText().toString();

                // не давать сохранять пустое значение
                if (newName.trim().length() == 0) {
                    Toast.makeText(EditSourceActivity.this, R.string.enter_name, Toast.LENGTH_SHORT).show();
                    return;
                }

                // чтобы лишний раз не сохранять - проверяем, были ли изменены данные, если нет, то при сохранении просто закрываем активити
                if (edited(node, newName, newIconName)) {

                    node.setName(newName);
                    node.setOperationType(((LocalizedOperationType) spSourceType.getSelectedItem()).getOperationType());

                    if (newIconName != null) {
                        node.setIconName(newIconName);
                    }

                    Intent intent = new Intent();
                    intent.putExtra(AppContext.NODE_OBJECT, node);// передаем отредактированный объект, который нужно сохранить в БД
                    setResult(RESULT_OK, intent);

                    transition.finishWithTransition();
                }


            }
        });


    }


    // создает компонент выпадающего списка с значениями типов операций
    private void createTypesSpinner() {
        spSourceType = (Spinner) findViewById(R.id.sp_source_type);


        List<LocalizedOperationType> listTypes = new ArrayList<>(2);
        listTypes.add(OperationTypeUtils.incomeType);
        listTypes.add(OperationTypeUtils.outcomeType);


        spAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listTypes);// нам нужны только доход и расход для категорий
        spSourceType.setAdapter(spAdapter);

        if (node.getOperationType()!=null) {// при редактировании объекта - это поле будет заполнено и недоступно для изменения
            spSourceType.setEnabled(false);
            spSourceType.setSelection(OperationType.getList().indexOf(node.getOperationType()));
        }else{
            spSourceType.setEnabled(true);
        }
    }


    // для проверки, изменились ли данные элемента
    protected boolean edited(Source node, String newName, String newIconName){
        // описываем условия, при которых будет считаться, что произошло редактирование - чтобы сохранять только при изменении данных и не делать лишних запросов в БД
        // если хотя бы одно из условий равно true - значит запись была отредактирована
        return (node.getName() == null && newName != null) || // если имя объекта было пустым и пользователь ввел новое имя
                (node.getIconName() == null && newIconName != null) || // если иконка объекта не была указана и пользователь выбрал иконку из списка
                (node.getName() != null && newName != null && !node.getName().equals(newName)) || // если название было заполнено и пользователь ввел новое имя, отличное от старого
                (node.getIconName() != null && newIconName != null && !node.getIconName().equals(newIconName)); // если пользователь выбрал новую иконку

    }




}
