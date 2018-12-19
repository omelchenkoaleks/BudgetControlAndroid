package com.omelchenkoaleks.budgetcontrol.adapters;

import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.ViewGroup;

import com.omelchenkoaleks.budgetcontrol.activities.edit.EditSourceActivity;
import com.omelchenkoaleks.budgetcontrol.adapters.abstracts.TreeNodeListAdapter;
import com.omelchenkoaleks.budgetcontrol.adapters.holders.SourceViewHolder;
import com.omelchenkoaleks.budgetcontrol.utils.AppContext;
import com.omelchenkoaleks.core.database.Initializer;
import com.omelchenkoaleks.core.impls.DefaultSource;
import com.omelchenkoaleks.core.interfaces.Source;

import java.util.List;

// адаптер для заполнения списка категорий
public class SourceNodeAdapter extends TreeNodeListAdapter<Source, SourceViewHolder> {

    private static final String TAG = SourceNodeAdapter.class.getName();

    public SourceNodeAdapter(int mode) {
        super(mode, Initializer.getSourceSync(), Initializer.getSourceSync().getAll());
    }

    public SourceNodeAdapter(int mode, List<Source> initialList) {
        super(mode, Initializer.getSourceSync(), initialList);

    }


    @Override
    public SourceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        super.onCreateViewHolder(parent, viewType);// обязательно нужно вызывать
        return new SourceViewHolder(itemView);
    }

    @Override
    protected void openActivityOnClick(Source source, int requestCode) {

        Source s = null;

        // если будет передан любой другой requestCode - тогда просто передавать объект в intent как есть
        switch (requestCode){
            case AppContext.REQUEST_NODE_ADD_CHILD: // для редактирования создаем новый пустой объект
                s = new DefaultSource();
                s.setOperationType(source.getOperationType());
                break;

            default:s=source;
        }



        Intent intent = new Intent(activityContext, EditSourceActivity.class); // какой акивити хотим вызвать
        intent.putExtra(AppContext.NODE_OBJECT, s); // помещаем выбранный объект node для передачи в активити
        activityContext.startActivityForResult(intent, requestCode, ActivityOptionsCompat.makeSceneTransitionAnimation(activityContext).toBundle()); // устанавливаем анимацию перехода

    }




}
