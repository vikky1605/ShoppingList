package ru.bolshakova.shoppinglist.data.storage;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import ru.bolshakova.shoppinglist.utils.MyApplication;
import ru.bolshakova.shoppinglist.utils.ConstantsManager;

public class SettingStorage {
    private SharedPreferences mSharedPreferences;
    public static String[] SETTINGS_BUY = {ConstantsManager.CHECK_ITEM_BUY, ConstantsManager.CHECK_TYPE_BUY,
                                           ConstantsManager.CHECK_UNIT_BUY, ConstantsManager.CHECK_COUNT_BUY,
                                           ConstantsManager.CHECK_PRICE_BY_UNIT_BUY, ConstantsManager.CHECK_AMOUNT_BUY,
                                           ConstantsManager.CHECK_IMPORTANCE_BUY};

    public SettingStorage() {
        mSharedPreferences = MyApplication.getSharedPreferences();

    }

    public void saveBuysSettings (List<Boolean> buysSettings) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        for (int i = 0; i < SETTINGS_BUY.length; i++) {
            editor.putBoolean(SETTINGS_BUY[i], buysSettings.get(i));
        }
        editor.apply();
    }

    public List<Boolean> loadBuysSettings() {
        List<Boolean> buysSettings = new ArrayList<>();

        buysSettings.add(mSharedPreferences.getBoolean(ConstantsManager.CHECK_ITEM_BUY, true));
        buysSettings.add(mSharedPreferences.getBoolean(ConstantsManager.CHECK_TYPE_BUY, true));
        buysSettings.add(mSharedPreferences.getBoolean(ConstantsManager.CHECK_UNIT_BUY, true));
        buysSettings.add(mSharedPreferences.getBoolean(ConstantsManager.CHECK_COUNT_BUY, true));
        buysSettings.add(mSharedPreferences.getBoolean(ConstantsManager.CHECK_PRICE_BY_UNIT_BUY, true));
        buysSettings.add(mSharedPreferences.getBoolean(ConstantsManager.CHECK_AMOUNT_BUY, true));
        buysSettings.add(mSharedPreferences.getBoolean(ConstantsManager.CHECK_IMPORTANCE_BUY, true));

        return buysSettings;
    }

    public void saveBuyListParameters(int parameters) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(ConstantsManager.OUTPUT_BUY_LIST_PARAMETERS, parameters);
        editor.apply();
    }

    public int loadBuyListParameters() {
        int parameters = mSharedPreferences.getInt(ConstantsManager.OUTPUT_BUY_LIST_PARAMETERS, 0);
        return parameters;
    }

    public void savePhoneNumberInStorage(String phoneNumber) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantsManager.PHONE_NUMBER, phoneNumber );
        editor.apply();
        }

    public String loadPhoneNumber () {
        String phoneNumber = mSharedPreferences.getString(ConstantsManager.PHONE_NUMBER, "");
        return phoneNumber;
    }
}
