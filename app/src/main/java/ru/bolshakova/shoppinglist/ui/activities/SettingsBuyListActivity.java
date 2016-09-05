package ru.bolshakova.shoppinglist.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import ru.bolshakova.shoppinglist.R;
import ru.bolshakova.shoppinglist.data.storage.SettingStorage;
import ru.bolshakova.shoppinglist.data.storage.SettingsManager;
import ru.bolshakova.shoppinglist.utils.ConstantsManager;

// активити для выбора настроек вывода списка покупок
public class SettingsBuyListActivity extends AppCompatActivity implements View.OnClickListener{

    private RadioButton mPlanBuyListBtn, mAllBuyListBtn;
    private TextView mBackToSettingsView;
    private SettingStorage mSettingStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_buy_list);
        mPlanBuyListBtn = (RadioButton)findViewById(R.id.plan_buys_btn);
        mAllBuyListBtn = (RadioButton)findViewById(R.id.all_buys_btn);
        mBackToSettingsView = (TextView)findViewById(R.id.back_to_settings);

        mSettingStorage = SettingsManager.getINSTANCE().getSettingsStorage();
        mBackToSettingsView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        saveSettingsBuyList();
        Intent intent = new Intent (this, SettingsActivity.class);
        startActivity(intent);
    }

    // сохранение в пользовательских настройках параметров вывода списка покупок
    private void saveSettingsBuyList() {
        if (mPlanBuyListBtn.isChecked()) mSettingStorage.saveBuyListParameters(ConstantsManager.OUTPUT_PLAN_BUYS);
        else mSettingStorage.saveBuyListParameters(ConstantsManager.OUTPUT_ALL_BUYS);
        }
}
