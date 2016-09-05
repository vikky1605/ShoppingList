package ru.bolshakova.shoppinglist.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

import ru.bolshakova.shoppinglist.R;
import ru.bolshakova.shoppinglist.data.storage.SettingsManager;

// активити для настройки параметров ввода покупок
public class SettingsBuysActivity extends AppCompatActivity implements View.OnClickListener {

    private List<CheckBox> mCheckBoxes;
    private Button mEnterButton;
    private List<Boolean> mBuysSettings;
    private SettingsManager mSettingsManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_buys);

        mCheckBoxes = new ArrayList<>();
        mCheckBoxes.add(0,(CheckBox)findViewById(R.id.item_buy_check));
        mCheckBoxes.add(1,(CheckBox)findViewById(R.id.buy_type_check));
        mCheckBoxes.add(2,(CheckBox)findViewById(R.id.unit_buy_check));
        mCheckBoxes.add(3,(CheckBox)findViewById(R.id.count_buy_check));
        mCheckBoxes.add(4,(CheckBox)findViewById(R.id.price_by_unit_check));
        mCheckBoxes.add(5,(CheckBox)findViewById(R.id.amount_buy_check));
        mCheckBoxes.add(6,(CheckBox)findViewById(R.id.importance_buy_check));

        mCheckBoxes.get(0).setEnabled(false);
        mCheckBoxes.get(5).setEnabled(false);

        mBuysSettings = new ArrayList<>();
        mSettingsManager = SettingsManager.getINSTANCE();

        mEnterButton = (Button) findViewById(R.id.enter_button);
        mEnterButton.setOnClickListener(this);

        loadCheckBoxesChecked();

    }

    private void loadCheckBoxesChecked() {
        mBuysSettings = mSettingsManager.getSettingsStorage().loadBuysSettings();
        for (int i = 0; i < mCheckBoxes.size(); i++) {
            mCheckBoxes.get(i).setChecked(mBuysSettings.get(i));
        }
    }

    @Override
    public void onClick(View v) {
        for (int i = 0; i < mCheckBoxes.size(); i++) {
            mBuysSettings.add(i, mCheckBoxes.get(i).isChecked());
        }
        mSettingsManager.getSettingsStorage().saveBuysSettings(mBuysSettings);
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
    }

