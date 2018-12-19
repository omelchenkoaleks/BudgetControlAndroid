package com.omelchenkoaleks.budgetcontrol.adapters.abstracts;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;

import com.malinskiy.superrecyclerview.swipe.BaseSwipeAdapter;
import com.malinskiy.superrecyclerview.swipe.SimpleSwipeListener;
import com.malinskiy.superrecyclerview.swipe.SwipeLayout;
import com.omelchenkoaleks.budgetcontrol.R;
import com.omelchenkoaleks.budgetcontrol.adapters.holders.BaseViewHolder;
import com.omelchenkoaleks.budgetcontrol.listeners.BaseNodeActionListener;
import com.omelchenkoaleks.budgetcontrol.utils.AppContext;
import com.omelchenkoaleks.budgetcontrol.utils.IconUtils;
import com.omelchenkoaleks.core.decorator.AbstractSync;
import com.omelchenkoaleks.core.interfaces.IconNode;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import jp.wasabeef.recyclerview.animators.BaseItemAnimator;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;

// базовый класс адаптера, который содержит общую реализацию для всех типов списков
public abstract class BaseNodeListAdapter<T extends IconNode, VH extends BaseViewHolder, L extends BaseNodeActionListener<T>> extends BaseSwipeAdapter<VH> {

    protected Comparator comparator;

    protected static final String TAG = BaseNodeListAdapter.class.getName();

    // текущая обновляемая коллекция адаптера - заполняется только нужными объектами для показа в списке
    protected List<T> adapterList = new ArrayList<>();

    // ссылка на этот компонент нужна, чтобы установить анимацию переходов
    protected RecyclerView recyclerView;

    public static BaseItemAnimator animatorChilds; // анимация при открытии дочерних элементов (справа налево)
    public static BaseItemAnimator animatorParents; // анимация при открытии родительских элементов (слево направо)


    // синхронизатор для сохранения, удаления, изменения данных (работа с ядром)
    // связующее звено между ядром и Android UI
    protected AbstractSync sync;

    // для возможности отмены удаления
    protected Snackbar snackbar;

    protected Activity activityContext;

    // текущая редактируемая позиция - это нужно для правильонго обновления элемента из списка
    protected int currentEditPosition;


    protected View itemView;

    // хранит слушателя события при нажатии на элемент списка
    protected L listener;

    // макет для элемента списка
    protected int itemLayout;


    public BaseNodeListAdapter(AbstractSync sync, int itemLayout) {
        this.sync = sync;
        this.itemLayout = itemLayout;
        adapterList = sync.getAll();// по-умолчанию - показывать все записи, если никакой начальный список не передавали

    }

    public BaseNodeListAdapter(AbstractSync sync, int itemLayout, List<T> initialList) {
        this.sync = sync;
        this.itemLayout = itemLayout;
        adapterList = initialList;
    }


    // какой активити открывать при клике - конкретный дочерний класс сам определяет
    protected abstract void openActivityOnClick(T node, int requestCode);


    // определяем макет, где будет находиться список значений
    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);

        activityContext = (Activity) parent.getContext();

        recyclerView = (RecyclerView) parent;

//        createAnimations();

        // этот метод не должен создавать ViewHolder, т.к. конкретные экземпляры холдеров создают дочерние классы
        return null;
    }


    // заполнение каждой позиции из списка
    @Override
    public void onBindViewHolder(VH holder, final int position) {
        super.onBindViewHolder(holder, position);// обязательно нужно вызывать, чтобы проинициализировать все переменные в родительских классах

        final T node = adapterList.get(position);// определяем выбранный пункт

        final SwipeLayout swipeLayout = holder.swipeLayout;

        swipeLayout.setDragEdge(SwipeLayout.DragEdge.Right);// для вызова меню - свайп справа налево
        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);// эффект появления и скрытия

        closeItem(position);

        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                listener.onSwipe(node);
            }
        });


        initListeners(holder, position, node);

        holder.tvNodeName.setText(node.getName());

        // если пользователем не установлена иконка - показываем иконку по-умолчанию
        if (node.getIconName() == null || IconUtils.getIcon(node.getIconName()) == null) {
            holder.imgNodeIcon.setImageDrawable(ResourcesCompat.getDrawable(activityContext.getResources(), R.drawable.ic_empty, null));
        } else {
            holder.imgNodeIcon.setImageDrawable(IconUtils.getIcon(node.getIconName()));

        }

        // вместо itemDecoration - используем itemView в виде разделителя (т.е. itemDecoration не всегда корректно работает при переходах)
        if (position == adapterList.size() - 1) {
            holder.lineSeparator.setVisibility(View.GONE);// для последней записи убираем разделитель снизу
        } else {
            holder.lineSeparator.setVisibility(View.VISIBLE);
        }


    }

    protected void initListeners(VH holder, final int position, final T node) {

        // обработка события при нажатии на любую запись списка
        holder.layoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // при клике на любую область свайп меню, кроме иконок - просто закрыть его
                closeItem(position);

                nodeClickAction(position, node);

            }
        });


        holder.imgSwipeDeleteNode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeItem(position);
                closeSnackBar();
                deleteWithSnackbar(node, position);
            }
        });

        holder.imgSwipeEditNode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runEditActivity(position, node);
            }
        });


    }


    // закрыть все открытые свайпом меню
    public void closeSwipeLayouts() {
        for (SwipeLayout s : getOpenLayouts()) {
            s.close();
        }
    }


    // создание нужных объетов анимации, которые будут использоваться в зависимости от того, куда переходим (родительские или дочерние списки)
//    private void createAnimations() {
//
//        animatorParents = new BaseItemAnimator() {
//
//            @Override
//            protected void animateRemoveImpl(final RecyclerView.ViewHolder holder) {
//                ViewCompat.animate(holder.itemView)
//                        .translationX(holder.itemView.getRootView().getWidth())
//                        .setDuration(getMoveDuration())
//                        .setInterpolator(mInterpolator)
//                        .setListener(new DefaultRemoveVpaListener(holder))
//                        .setStartDelay(getRemoveDelay(holder))
//                        .start();
//            }
//
//            @Override
//            protected void preAnimateAddImpl(RecyclerView.ViewHolder holder) {
//                ViewCompat.setTranslationX(holder.itemView, -holder.itemView.getRootView().getWidth());
//            }
//
//            @Override
//            protected void animateAddImpl(final RecyclerView.ViewHolder holder) {
//                ViewCompat.animate(holder.itemView)
//                        .translationX(0)
//                        .setDuration(getMoveDuration())
//                        .setInterpolator(mInterpolator)
//                        .setListener(new DefaultAddVpaListener(holder))
//                        .setStartDelay(getAddDelay(holder))
//                        .start();
//            }
//        };
//
//
//        animatorChilds = new BaseItemAnimator() {
//
//
//            @Override
//            protected void animateRemoveImpl(final RecyclerView.ViewHolder holder) {
//
//                ViewCompat.animate(holder.itemView)
//                        .translationX(-holder.itemView.getRootView().getWidth())
//                        .setDuration(getMoveDuration())
//                        .setInterpolator(mInterpolator)
//                        .setListener(new DefaultRemoveVpaListener(holder))
//                        .setStartDelay(getRemoveDelay(holder))
//                        .start();
//
//            }
//
//            @Override
//            protected void preAnimateAddImpl(RecyclerView.ViewHolder holder) {
//                ViewCompat.setTranslationX(holder.itemView, holder.itemView.getRootView().getWidth());
//            }
//
//            @Override
//            protected void animateAddImpl(final RecyclerView.ViewHolder holder) {
//                ViewCompat.animate(holder.itemView)
//                        .translationX(0)
//                        .setDuration(getMoveDuration())
//                        .setInterpolator(mInterpolator)
//                        .setListener(new DefaultAddVpaListener(holder))
//                        .setStartDelay(getAddDelay(holder))
//                        .start();
//            }
//
//
//        };
//
//
//    }


    @Override
    public int getItemCount() {
        return adapterList.size();
    }


    // закрыть snackbar, если в данный момент открыт - при закрытии будет фактически удален объект (т.к. не отменили удаление)
    // чтобы не было ошибок с удалением объектов - snackbar нужно всегда закрывать, если во время его показа пользователь начинает выполнять другие действия
    protected void closeSnackBar() {
        if (snackbar != null && snackbar.isShown()) {
            snackbar.dismiss();
        }
    }

    // действие по-умолчанию при нажатии на элемент (метод может переопределяться в дочерних классах по необходимости
    protected void nodeClickAction(int position, T node) {
        runEditActivity(position, node); // поведение по-умолчанию при нажатии на элемент - открытие его на редактирование
    }


    // открыть значение на редактирование
    protected void runEditActivity(int position, T node) {

        closeSnackBar();
        currentEditPosition = position; // запомнить редактируемую позицию, чтобы потом обновлять ее в списке

        // openActivityOnClick  - оставляем на реализацию в дочерние адаптеры (какой именно активити открывать), главное передать правильный requestCode
        openActivityOnClick(node, AppContext.REQUEST_NODE_EDIT);
    }


    // удалить с возможностью отмены удаления
    protected void deleteWithSnackbar(final T node, final int position) {

        closeSnackBar();// елсли ранее был открыт snackbar для другого элемента - закрываем его, чтобы открыть новый, для текущего элемента

        // сначала удаляем запись просто из текушей коллекции адаптера (без удалении из базы) - даем сначала пользователю шанс отменить
        adapterList.remove(node);
//                        notifyItemRemoved(position);
        // TODO реализовать обновление position для всех элементов - т.к. есть некоторые глюки при обновлении позиций, если вызывать notifyItemRemoved, тогда как notifyDataSetChanged правильно обновляет индексы для всех записей списка

        notifyDataSetChanged(); // чтобы правильно обновлял позиции элементов


        // показываем Snackbar, чтобы дать пользователю возможность отменить действие
        snackbar = Snackbar.make(recyclerView, R.string.deleted, Snackbar.LENGTH_LONG); // для возможности отменить удаление

        snackbar.setAction(R.string.undo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {// если нажали на ссылку ОТМЕНИТЬ
                closeSwipeLayouts();
                adapterList.add(position, node);// возвращаем обратно
//                                notifyItemInserted(position);
                listener.onAdd(node);
                notifyDataSetChanged();

            }
        }).setCallback(new Snackbar.Callback() {// для того, чтобы могли отловить момент, когда SnackBar исчезнет (т.е. ползователь не успел отменить удаление)

            @Override
            public void onDismissed(Snackbar snackbar, int event) {

                if (event != DISMISS_EVENT_ACTION) {// если не была нажата ссылка отмены
                    deleteNode(node, position); // удаляем из-базы и коллекции ядра, обновляем список
                }

            }
        }).show();


    }


    // метод обновления данных может вызываться из фрагмента, где используется данный адаптер
    public void refreshList(final List<T> list, RecyclerView.ItemAnimator animator) {

        // для анимации передвижаения списка - сначала нужно удалить все элементы
        if (list.isEmpty()){
            int range = adapterList.size();
            notifyItemRangeRemoved(0, range);
            return;
        }

        closeSwipeLayouts();

        if (snackbar != null && snackbar.isShown()) {
            closeSnackBar();
        }

        int range = adapterList.size();
        notifyItemRangeRemoved(0, range);

        // ставит требуемую анимацию для перехода (в зависимости от того, какие элементы открываем, дочерние или родительские)
        recyclerView.setItemAnimator(animator);


        adapterList = list;
        notifyItemRangeInserted(0, adapterList.size()); // вместе с методом notifyItemRangeRemoved дает эффект анимации - передвижения списка туда и обратно

    }


    // вставка нового корневого элемента
    public void addNode(T node) {
        try {
            // TODO переделать, чтобы при вставке объект сразу попадал в нужный индекс (чтобы не сортировать каждый раз)

            sync.add(node);// сначала вставляем в БД и общую коллекцию

            if (comparator!=null) {
                Collections.sort(adapterList, comparator);
            }

//            notifyItemInserted(adapterList.size() - 1);// вставка в последнюю позицию
//            recyclerView.scrollToPosition(adapterList.size() - 1);

            listener.onAdd(node); // уведомляем слушателя (нужно например для списка source - чтобы сразу обновлять общий баланс

            notifyDataSetChanged();

        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }
    }


    // обновление значения
    public void updateNode(T node) {
        try {
            sync.update(node);

            if (comparator!=null) {
                Collections.sort(adapterList, comparator);
            }

            listener.onUpdate(node); // уведомляем слушателя (нужно например для списка source - чтобы сразу обновлять общий баланс

//            notifyItemChanged(currentEditPosition);
            notifyDataSetChanged();

        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }

    }


    // удаляет записи и обновляет список
    public boolean deleteNode(T node, int position) {
        try {

            // ранее уже удалили запись из списка - поэтому не вызываем  notifyItemRemoved(position), а просто удаляем из БД
            sync.delete(node);
            notifyDataSetChanged(); // этот метод также нужен, если пользователь нажмет отмену удаления и успеет перейти в другой список

            listener.onDelete(node); // уведомляем слушателя (нужно например для списка source - чтобы сразу обновлять общий баланс

            return true;

        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }

        return false;
    }


    public void setListener(L listener) {
        this.listener = listener;
    }

    public void setContext(Activity activityContext) {
        this.activityContext = activityContext;
    }

    public List<T> getAdapterList() {
        return adapterList;
    }



}
