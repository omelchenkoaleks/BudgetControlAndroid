package com.omelchenkoaleks.budgetcontrol.adapters.holders;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.malinskiy.superrecyclerview.swipe.BaseSwipeAdapter;
import com.malinskiy.superrecyclerview.swipe.SwipeLayout;
import com.omelchenkoaleks.budgetcontrol.R;

// поля, которые являются общими для всех справочных значений
public class BaseViewHolder extends BaseSwipeAdapter.BaseSwipeableViewHolder {

    public final TextView tvNodeName;
    public final ViewGroup layoutMain;
    public final ImageView imgNodeIcon;
    public final View lineSeparator;
    public final SwipeLayout swipeLayout;


    // для меню, которое появляется свайпом
    public final ImageView imgSwipeDeleteNode;
    public final ImageView imgSwipeAddChildNode;
    public final ImageView imgSwipeEditNode;



    public BaseViewHolder(View view) {
        super(view);

        // чтобы для каждого компонента не выполнять findViewById - сохраняем ссылки в константы
        tvNodeName = (TextView) view.findViewById(R.id.tv_node_name);
        layoutMain = (RelativeLayout) view.findViewById(R.id.node_main_layout);

        imgSwipeDeleteNode = (ImageView) view.findViewById(R.id.img_swipe_delete_node);
        imgSwipeAddChildNode = (ImageView) view.findViewById(R.id.img_swipe_add_child_node);
        imgSwipeEditNode = (ImageView) view.findViewById(R.id.img_swipe_edit_node);
        imgNodeIcon = (ImageView) view.findViewById(R.id.img_node_icon);

        lineSeparator =  view.findViewById(R.id.line_separator);
        swipeLayout =  (SwipeLayout)view.findViewById(R.id.recyclerview_swipe);

    }

}
