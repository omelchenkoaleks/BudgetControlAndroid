package com.omelchenkoaleks.budgetcontrol.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.omelchenkoaleks.budgetcontrol.R;
import com.omelchenkoaleks.budgetcontrol.activities.list.OperationListActivity;
import com.omelchenkoaleks.budgetcontrol.database.DbConnection;


// окно загрузки при старте приложения - показывается только один раз (имеет параметр noHistory в AndroidManifest- чтобы к этому актвити нельзя было вернять через кнопку Назад
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);


        new Thread() {
            public void run() {
                // загрузка начальных данных (операции, справочники)
                DbConnection.initConnection(getApplicationContext());

                // имитация загрузки
//                imitateLoading();

                // после загрузки переходим на окно со списком операций
                Intent intent = new Intent(SplashActivity.this, OperationListActivity.class);// по-умолчанию после загрузки запускается список операций
                startActivity(intent);
            }
        }.start();


    }

    private void imitateLoading() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
