package com.omelchenkoaleks.budgetcontrol.adapters.abstracts;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.malinskiy.superrecyclerview.swipe.SwipeLayout;
import com.omelchenkoaleks.budgetcontrol.R;
import com.omelchenkoaleks.budgetcontrol.adapters.holders.TreeViewHolder;
import com.omelchenkoaleks.budgetcontrol.listeners.TreeNodeActionListener;
import com.omelchenkoaleks.budgetcontrol.utils.AppContext;
import com.omelchenkoaleks.core.decorator.AbstractSync;
import com.omelchenkoaleks.core.interfaces.TreeNode;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

// адаптер для списка, который поддерживает древовидное представление
public abstract class TreeNodeListAdapter<T extends TreeNode, VH extends TreeViewHolder> extends BaseNodeListAdapter<T, VH, TreeNodeActionListener<T>> {


    protected static final String TAG = TreeNodeListAdapter.class.getName();

    // макет по-умолчанию, который применяется для списков справочных значений
    private static final int layoutId = R.layout.node_item;

    // хранит режим работы со справочником
    private int mode;


    public TreeNodeListAdapter(int mode, AbstractSync sync) {
        super(sync, layoutId);
        this.mode = mode;
    }

    public TreeNodeListAdapter(int mode, AbstractSync sync, List<T> initialList) {
        super(sync, layoutId, initialList);
        this.mode = mode;
    }


    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        super.onCreateViewHolder(parent, viewType);
        return null;// этот метод не должен создавать ViewHolder, т.к. конкретные экземпляры холдеров создают дочерние классы
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        super.onBindViewHolder(holder, position);// обязательно нужно вызывать, чтобы проинициализировать все переменные в родительских классах

        final T node = adapterList.get(position);// определяем выбранный пункт


        // показать кол-во дочерних элементов, если они есть
        if (node.hasChilds()) {
            holder.tvChildCount.setText(String.valueOf(node.getChilds().size()));
            holder.tvChildCount.setBackgroundColor(ContextCompat.getColor(activityContext, R.color.colorGray));
            holder.imgSwipeDeleteNode.setVisibility(View.GONE);
        } else {
            holder.tvChildCount.setText("");
            holder.tvChildCount.setBackground(null);// чтобы не рисовал пустую закрашенную область
            holder.imgSwipeDeleteNode.setVisibility(View.VISIBLE);
        }


        // если у справочного значения есть дочерние пункты - показать стрелку для перехода в дочерний список
        if (node.hasChilds()) {
            holder.layoutShowChilds.setVisibility(View.VISIBLE);
            holder.layoutShowChilds.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    refreshList((List<T>) node.getChilds(), node);
                }
            });
        } else {
            holder.layoutShowChilds.setVisibility(View.INVISIBLE);
        }


    }


    private void refreshList(List<T> list, T selectedNode) {
        refreshList(list, animatorChilds);// перейти в список дочерних элементов
        listener.onShowChilds(selectedNode);// уведомляем слушателя
    }


    @Override
    protected void deleteWithSnackbar(T node, int position) {
        if (node.getRefCount() > 0) {// не даем удалять запись, если на нее есть ссылки из операций
            closeSwipeLayouts();
            closeSnackBar();
            Toast.makeText(activityContext, R.string.has_operations, Toast.LENGTH_SHORT).show();
        } else {
            super.deleteWithSnackbar(node, position); // стандартное удаление
        }
    }


    // запустить актвити для создания дочернего элемента
    protected void runAddChildActivity(T node) {
        closeSnackBar();

        // какой именно активити открывать - реализовывает дочерний адаптер, главное передать правильный requestCode
        openActivityOnClick(node, AppContext.REQUEST_NODE_ADD_CHILD);
    }


    // вставка дочернего элемента
    public void insertChildNode(T node) {
        try {
            sync.add(node);

            List<T> list = (List<T>) node.getParent().getChilds(); // после вставки дочернего элемента - показываем весь список дочерних элементов

            if (comparator != null) {
                Collections.sort(list, comparator);
            }

            // загрузить все дочерние элементы родителя, среди которых и новый добавленный
            refreshList(list, (T) node.getParent());

            recyclerView.scrollToPosition(list.size() - 1);// перейти в позицию вставленного значения


        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }
    }


    protected void initListeners(VH holder, final int position, final T node) {
        super.initListeners(holder, position, node);

        final SwipeLayout swipeLayout = holder.swipeLayout;

        swipeLayout.setDragEdge(SwipeLayout.DragEdge.Right);// для вызова меню - свайп справа налево
        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);// эффект появления и скрытия

        holder.imgSwipeAddChildNode.setVisibility(View.VISIBLE);

        holder.imgSwipeAddChildNode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runAddChildActivity(node);
            }
        });


    }


    // выполняет действия при клике на значении справочника - в зависимости от режима
    // либо возвращает выбранный объект (при создании или редактировании операции) (SELECT_MODE)
    // либо открывает на редактирование (EDIT_MODE)
    @Override
    protected void nodeClickAction(int position, T node) {
        if (mode == AppContext.EDIT_MODE) {
            super.nodeClickAction(position, node);// выполняем стандартный функционал - открытие элемента на редактирование
        } else if (mode == AppContext.SELECT_MODE) {
            listener.returnNodeToOperationActivity(node);// уведомляем слушателя (в данном случае - активити), чтобы он отправил выбранный объект через Intent
        }

    }
}


