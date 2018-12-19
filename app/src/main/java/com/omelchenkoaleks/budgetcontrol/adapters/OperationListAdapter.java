package com.omelchenkoaleks.budgetcontrol.adapters;

import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;

import com.omelchenkoaleks.budgetcontrol.R;
import com.omelchenkoaleks.budgetcontrol.activities.edit.operation.EditConvertOperationActivity;
import com.omelchenkoaleks.budgetcontrol.activities.edit.operation.EditIncomeOperationActivity;
import com.omelchenkoaleks.budgetcontrol.activities.edit.operation.EditOutcomeOperationActivity;
import com.omelchenkoaleks.budgetcontrol.activities.edit.operation.EditTransferOperationActivity;
import com.omelchenkoaleks.budgetcontrol.adapters.abstracts.BaseNodeListAdapter;
import com.omelchenkoaleks.budgetcontrol.adapters.holders.OperationViewHolder;
import com.omelchenkoaleks.budgetcontrol.comparators.OperationDateComparator;
import com.omelchenkoaleks.budgetcontrol.listeners.BaseNodeActionListener;
import com.omelchenkoaleks.budgetcontrol.utils.AppContext;
import com.omelchenkoaleks.budgetcontrol.utils.ColorUtils;
import com.omelchenkoaleks.budgetcontrol.utils.IconUtils;
import com.omelchenkoaleks.budgetcontrol.utils.LocaleUtils;
import com.omelchenkoaleks.budgetcontrol.utils.OperationTypeUtils;
import com.omelchenkoaleks.core.database.Initializer;
import com.omelchenkoaleks.core.impls.operations.ConvertOperation;
import com.omelchenkoaleks.core.impls.operations.IncomeOperation;
import com.omelchenkoaleks.core.impls.operations.OutcomeOperation;
import com.omelchenkoaleks.core.impls.operations.TransferOperation;
import com.omelchenkoaleks.core.interfaces.Operation;

import java.util.Calendar;

// адаптер для заполнения списка операций
public class OperationListAdapter extends BaseNodeListAdapter<Operation, OperationViewHolder, BaseNodeActionListener<Operation>> {

    private static final String TAG = OperationListAdapter.class.getName();


    public OperationListAdapter() {
        super(Initializer.getOperationSync(), R.layout.operation_item);

        comparator = OperationDateComparator.getInstance();
    }


    // открытие нужного активити для редактирования операции нужного типа
    @Override
    protected void openActivityOnClick(Operation node, int requestCode) {

        Operation operation = null;
        Class activityClass = null;

        switch (requestCode){

            case AppContext.REQUEST_NODE_ADD:
                break;

            case AppContext.REQUEST_NODE_EDIT:

                switch (node.getOperationType()){
                    case INCOME:
                        activityClass = EditIncomeOperationActivity.class;
                        break;
                    case OUTCOME:
                        activityClass = EditOutcomeOperationActivity.class;
                        break;
                    case TRANSFER:
                        activityClass = EditTransferOperationActivity.class;
                        break;
                    case CONVERT:
                        activityClass = EditConvertOperationActivity.class;
                        break;
                }

                operation = node;
                break;
        }





        Intent intent = new Intent(activityContext, activityClass); // какой акивити хотим вызвать
        intent.putExtra(AppContext.NODE_OBJECT, operation); // помещаем выбранный объект operation для передачи в активити
        intent.putExtra(AppContext.OPERATION_ACTION, AppContext.OPERATION_EDIT); // режим редактирования
        (activityContext).startActivityForResult(intent, requestCode, ActivityOptionsCompat.makeSceneTransitionAnimation(activityContext).toBundle()); // устанавливаем анимацию перехода

    }

    @Override
    public OperationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        super.onCreateViewHolder(parent, viewType);// обязательно нужно вызывать
        return new OperationViewHolder(itemView);
    }



    // этот метод устанавливает только специфичные данные для элемента списка
    @Override
    public void onBindViewHolder(OperationViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);// не забывать вызывать, чтобы заполнить общие компоненты

        final Operation operation = adapterList.get(position);// определяем выбранный пункт

        String subTitle;

        // если год операции совпадает с текущим - показывать просто дату (без года)
        if (operation.getDateTime().get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)){
            subTitle= DateUtils.formatDateTime(activityContext, operation.getDateTime().getTimeInMillis(), DateUtils.FORMAT_SHOW_DATE);
        }else{
            subTitle= DateUtils.formatDateTime(activityContext, operation.getDateTime().getTimeInMillis(), DateUtils.FORMAT_ABBREV_ALL);
        }

        // добавить отображение времени
        subTitle+=", "+DateUtils.formatDateTime(activityContext, operation.getDateTime().getTimeInMillis(), DateUtils.FORMAT_SHOW_TIME);

        holder.tvOperationSubtitle.setText(subTitle);


        holder.tvOperationConvertCurrency.setVisibility(View.GONE);// по-умолчанию компонент, который показывает валюту конвертации - не видим, он становится видимым только для операции обмена

        String amountTitle = null;
        // поля для каждого типа операции отличаются, поэтому заполняем все отдельно
        switch (operation.getOperationType()){
            case INCOME:
                IncomeOperation incomeOperation = (IncomeOperation) operation;

                amountTitle = incomeOperation.getFromAmount().toString();
                holder.tvOperationCurrency.setText(incomeOperation.getFromCurrency().getSymbol(LocaleUtils.defaultLocale)); // валюту показываем в нужной локали
                holder.tvOperationTypeTag.setText(OperationTypeUtils.incomeType.toString());
                holder.tvOperationTypeTag.setBackgroundColor(activityContext.getColor(ColorUtils.incomeColor));
                holder.tvNodeName.setText(incomeOperation.getFromSource().getName() + " -> " + incomeOperation.getToStorage().getName());

                // если пользователем не установлена иконка - показываем иконку по-умолчанию
                if (incomeOperation.getFromSource().getIconName() == null || IconUtils.getIcon(incomeOperation.getFromSource().getIconName()) == null) {
                    holder.imgNodeIcon.setImageDrawable(ResourcesCompat.getDrawable(activityContext.getResources(), R.drawable.ic_empty, null));
                } else {
                    holder.imgNodeIcon.setImageDrawable(IconUtils.getIcon(incomeOperation.getFromSource().getIconName()));

                }

                break;

            case OUTCOME:
                OutcomeOperation outcomeOperation = (OutcomeOperation) operation;

                amountTitle = outcomeOperation.getFromAmount().toString();
                holder.tvOperationCurrency.setText(outcomeOperation.getFromCurrency().getSymbol(LocaleUtils.defaultLocale)); // валюту показываем в нужной локали
                holder.tvOperationTypeTag.setText(OperationTypeUtils.outcomeType.toString());
                holder.tvOperationTypeTag.setBackgroundColor(activityContext.getColor(ColorUtils.outcomeColor));
                holder.tvNodeName.setText(outcomeOperation.getFromStorage().getName() + " -> " + outcomeOperation.getToSource().getName());

                // если пользователем не установлена иконка - показываем иконку по-умолчанию
                if (outcomeOperation.getFromStorage().getIconName() == null || IconUtils.getIcon(outcomeOperation.getFromStorage().getIconName()) == null) {
                    holder.imgNodeIcon.setImageDrawable(ResourcesCompat.getDrawable(activityContext.getResources(), R.drawable.ic_empty, null));
                } else {
                    holder.imgNodeIcon.setImageDrawable(IconUtils.getIcon(outcomeOperation.getFromStorage().getIconName()));

                }

                break;

            case TRANSFER:
                TransferOperation transferOperation = (TransferOperation) operation;

                holder.tvOperationCurrency.setText(transferOperation.getFromCurrency().getSymbol(LocaleUtils.defaultLocale)); // валюту показываем в нужной локали
                amountTitle = transferOperation.getFromAmount().toString();
                holder.tvOperationTypeTag.setText(OperationTypeUtils.transferType.toString());
                holder.tvOperationTypeTag.setBackgroundColor(activityContext.getColor(ColorUtils.transferColor));
                holder.tvNodeName.setText(transferOperation.getFromStorage().getName() + " -> " + transferOperation.getToStorage().getName());

                // если пользователем не установлена иконка - показываем иконку по-умолчанию
                if (transferOperation.getToStorage().getIconName() == null || IconUtils.getIcon(transferOperation.getToStorage().getIconName()) == null) {
                    holder.imgNodeIcon.setImageDrawable(ResourcesCompat.getDrawable(activityContext.getResources(), R.drawable.ic_empty, null));
                } else {
                    holder.imgNodeIcon.setImageDrawable(IconUtils.getIcon(transferOperation.getToStorage().getIconName()));

                }

                break;

            case CONVERT:
                ConvertOperation convertOperation = (ConvertOperation) operation;

                holder.tvOperationCurrency.setText(convertOperation.getToCurrency().getSymbol(LocaleUtils.defaultLocale)); // валюту показываем в нужной локали
                holder.tvOperationTypeTag.setText(OperationTypeUtils.convertType.toString());
                holder.tvOperationTypeTag.setBackgroundColor(activityContext.getColor(ColorUtils.convertColor));
                holder.tvNodeName.setText(convertOperation.getFromStorage().getName() + " -> " + convertOperation.getToStorage().getName());


                holder.tvOperationConvertCurrency.setVisibility(View.VISIBLE);
                holder.tvOperationConvertCurrency.setText(convertOperation.getFromAmount().toString()+" "+convertOperation.getFromCurrency().getSymbol(LocaleUtils.defaultLocale));

                amountTitle = convertOperation.getToAmount().toString();

                holder.tvOperationCurrency.setText(convertOperation.getToCurrency().getSymbol(LocaleUtils.defaultLocale));

                // если пользователем не установлена иконка - показываем иконку по-умолчанию
                if (convertOperation.getToStorage().getIconName() == null || IconUtils.getIcon(convertOperation.getToStorage().getIconName()) == null) {
                    holder.imgNodeIcon.setImageDrawable(ResourcesCompat.getDrawable(activityContext.getResources(), R.drawable.ic_empty, null));
                } else {
                    holder.imgNodeIcon.setImageDrawable(IconUtils.getIcon(convertOperation.getToStorage().getIconName()));

                }

                break;
        }


        holder.tvOperationAmount.setText(amountTitle); // amountTitle может браться из разных полей, в зависимости от типа операции - поэтому заполняем его в последнюю очередь


    }


}
