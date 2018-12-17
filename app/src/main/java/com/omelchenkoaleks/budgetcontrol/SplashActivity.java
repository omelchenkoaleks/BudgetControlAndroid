package com.omelchenkoaleks.budgetcontrol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.omelchenkoaleks.budgetcontrol.activities.MainActivity;
import com.omelchenkoaleks.budgetcontrol.app.DbConnection;

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
                imitateLoading();

                // после загрузки переходим на окно со списком операций
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//                Intent intent = new Intent(SplashActivity.this, OperationListActivity.class);// по-умолчанию после загрузки запускается список операций
                startActivity(intent);
            }
        }.start();
    }

    private void imitateLoading() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
