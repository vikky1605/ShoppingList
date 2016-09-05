package ru.bolshakova.shoppinglist.utils;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.facebook.stetho.Stetho;

import ru.bolshakova.shoppinglist.data.dataBase.DataBaseManager;

public class MyApplication extends Application {

    public static SharedPreferences sSharedPreferences;

    public static SharedPreferences getSharedPreferences() {
        return sSharedPreferences;
    }

    public static void setSharedPreferences(SharedPreferences sSharedPreferences) {
        MyApplication.sSharedPreferences = sSharedPreferences;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        DataBaseManager.getInstance().init(getApplicationContext());
        sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        DataBaseManager.getInstance().release();
    }
}
