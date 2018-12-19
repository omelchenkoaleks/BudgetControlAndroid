package com.omelchenkoaleks.budgetcontrol.activities.abstracts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.omelchenkoaleks.budgetcontrol.R;
import com.omelchenkoaleks.budgetcontrol.activities.SelectIconActivityBase;
import com.omelchenkoaleks.budgetcontrol.transitions.TransitionSlide;
import com.omelchenkoaleks.budgetcontrol.utils.AppContext;
import com.omelchenkoaleks.budgetcontrol.utils.IconUtils;
import com.omelchenkoaleks.core.interfaces.IconNode;

import java.math.BigDecimal;

// общие поля и реализации для активити редактирования справочных значений
// не содержит моменты, связанные с древовидной структурой
public abstract class BaseEditNodeActivity<T extends IconNode> extends AppCompatActivity {

    protected Toolbar toolbar;
    protected EditText etName;
    protected TextView tvNodeName;
    protected ImageView imgSave;
    protected ImageView imgClose;
    protected ImageView imgNodeIcon;

    protected T node;

    private int layoutId;

    protected TransitionSlide transition;// используется для закрытия активити с анимацией

    public BaseEditNodeActivity(int layoutId) {
        this.layoutId = layoutId;
    }

    protected String newIconName;


    // метод работает с общими компонентами
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId);

        findComponents();


        node = (T)getIntent().getSerializableExtra(AppContext.NODE_OBJECT); // получаем переданный объект для редактирования

        changeTitle();

        changeIcon();


        // анимация перехода по-умолчанию для всех активити редактирования
        transition = new TransitionSlide(this, TransitionSlide.Direction.BOTTOM_TOP);

        createListeners();// должен вызываться после того, как все компоненты определены выше (через findViewById)

    }

    private void findComponents() {
        toolbar = (Toolbar) findViewById(R.id.tlb_edit_actions);
        setSupportActionBar(toolbar);
        etName = (EditText) findViewById(R.id.et_node_name);
        tvNodeName = (TextView) findViewById(R.id.tv_node_name);
        imgSave = (ImageView) findViewById(R.id.img_node_save);
        imgClose = (ImageView) findViewById(R.id.img_node_close);
        imgNodeIcon = (ImageView) findViewById(R.id.img_node_icon);
    }


    private void createListeners() {

        // открытие окна выбора иконки
        imgNodeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BaseEditNodeActivity.this, SelectIconActivityBase.class); // какой акивити хотим вызвать
                startActivityForResult(intent, AppContext.REQUEST_SELECT_ICON,
                        ActivityOptionsCompat.makeSceneTransitionAnimation(BaseEditNodeActivity.this).toBundle()); // устанавливаем анимацию перехода

            }
        });


        // слушатель события при отмене
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transition.finishWithTransition();
            }
        });


    }


    // показать иконку элемента или заглушку, если иконка не указана
    private void changeIcon() {
        if (node.getIconName() == null || IconUtils.getIcon(node.getIconName()) == null) {
            imgNodeIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_empty, null));
        } else {
            imgNodeIcon.setImageDrawable(IconUtils.getIcon(node.getIconName()));
        }
    }

    // в зависимости от типа действия (создание или редактирование) - меняем заголовок
    private void changeTitle(){
        if (node.getName() != null) {
            tvNodeName.setText(R.string.editing);
            etName.setText(node.getName());
        } else {
            tvNodeName.setText(R.string.adding);
            etName.setText("");
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK){

            // обработка результата активити для выбора новой иконки
            if (requestCode== AppContext.REQUEST_SELECT_ICON){
                newIconName = data.getStringExtra(AppContext.ICON_NAME); // получаем переданное имя выбранной иконки

                if (!newIconName.equals(node.getIconName())) { // если иконка изменилась - обновляем отображение
                    imgNodeIcon.setImageDrawable(IconUtils.getIcon(newIconName));// получаем новую иконку из карты иконок
                }
            }
        }

    }



    // конвертация из текста, введенного пользователем - в BigDecimal (для корректного сохранения)
    protected BigDecimal convertString(String value){

        if (value.trim().length()!=0){
            return new BigDecimal(value);
        }

        return BigDecimal.ZERO;

    }



}
