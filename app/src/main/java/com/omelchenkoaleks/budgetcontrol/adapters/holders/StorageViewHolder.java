package com.omelchenkoaleks.budgetcontrol.adapters.holders;

import android.view.View;
import android.widget.TextView;

import com.omelchenkoaleks.budgetcontrol.R;
// описывает компоненты, которые являются специфичными для списка счетов
public class StorageViewHolder extends TreeViewHolder {


    public final TextView tvAmount;


    public StorageViewHolder(View view) {
        super(view);
        tvAmount = (TextView) view.findViewById(R.id.tv_node_amount);
        tvAmount.setVisibility(View.VISIBLE);

    }

}