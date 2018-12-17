package com.omelchenkoaleks.budgetcontrol;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class AppContext extends Application {

    private static final String TAG = AppContext.class.getName();

    private static final String DB_NAME = "money.db";
    private static String dbFolder;
    private static String dbPath;

    @Override
    public void onCreate() {
        super.onCreate();

        CheckDbExist(this);
    }

    // если нет файла БД - скопировать его из папки assets
    private static void CheckDbExist(Context context) {

        // определить папку с данными приложения
        dbFolder = context.getApplicationInfo().dataDir + "/" + "databases/";

        dbPath = dbFolder + DB_NAME;
        if (!checkDataBaseExists()) {
            copyDataBase(context);
        }
    }

    private static void copyDataBase(Context context) {
        try (
                // что копируем
                InputStream sourceFile = context.getAssets().open(DB_NAME);
                // куда копируем
                OutputStream destinationFolder = new FileOutputStream(dbPath)
        ) {

            // создаем папку databases
            File databaseFolder = new File(dbFolder);
            databaseFolder.mkdir();

            // копируем по байтам весь файл стандартным способом Java I/0
            byte[] buffer = new byte[1024];
            int length;
            while ((length = sourceFile.read(buffer)) > 0) {
                destinationFolder.write(buffer, 0, length);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private static boolean checkDataBaseExists() {
        File dbFile = new File((dbPath));
        return dbFile.exists();
    }

}
