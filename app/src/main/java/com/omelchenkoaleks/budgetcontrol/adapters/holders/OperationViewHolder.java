package com.omelchenkoaleks.budgetcontrol.adapters.holders;


import android.view.View;
import android.widget.TextView;

import com.omelchenkoaleks.budgetcontrol.R;


// описывает компоненты, которые являются специфичными для списка операций
public class OperationViewHolder extends BaseViewHolder {

    public final TextView tvOperationSubtitle;
    public final TextView tvOperationAmount;
    public final TextView tvOperationTypeTag;
    public final TextView tvOperationConvertCurrency;
    public final TextView tvOperationCurrency;



    public OperationViewHolder(View view) {
        super(view);

        tvOperationSubtitle = (TextView) view.findViewById(R.id.tv_operation_subtitle);
        tvOperationAmount = (TextView) view.findViewById(R.id.tv_operation_amount);
        tvOperationTypeTag = (TextView) view.findViewById(R.id.tv_operation_type_tag);
        tvOperationConvertCurrency = (TextView) view.findViewById(R.id.tv_operation_amount_convert_currency);
        tvOperationCurrency = (TextView) view.findViewById(R.id.tv_operation_currency);

    }
}
