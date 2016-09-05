package ru.bolshakova.shoppinglist.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import ru.bolshakova.shoppinglist.R;
import ru.bolshakova.shoppinglist.utils.ConstantsManager;

// активити для настроек приложения
public class SettingsActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView mInputBuyParametersView, mOutputBuyListParametersView,
                     mGetContactView, mBackView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mInputBuyParametersView = (TextView)findViewById(R.id.input_buys_parameters);
        mOutputBuyListParametersView = (TextView)findViewById(R.id.output_buy_list_parameters);
        mGetContactView = (TextView)findViewById(R.id.get_contact_for_SMS);
        mBackView = (TextView)findViewById(R.id.back);

        mInputBuyParametersView.setOnClickListener(this);
        mOutputBuyListParametersView.setOnClickListener(this);
        mGetContactView.setOnClickListener(this);
        mBackView.setOnClickListener(this);

    }
    // обработка переходов к деталям настроек
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back: {
                Intent intent = new Intent (this, MainActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.input_buys_parameters: {
                Intent intent = new Intent (this, SettingsBuysActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.output_buy_list_parameters: {
                Intent intent = new Intent (this, SettingsBuyListActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.get_contact_for_SMS: {
                if (Integer.valueOf(Build.VERSION.SDK_INT) >=23) {
                    checkPermissionToContact();
                }
                else {
                    Intent intent = new Intent(this, GetContactActivity.class);
                    startActivity(intent);
                }
                break;
            }
        }
    }

    // проверка наличия разрешения на доступ к контактам и отправке СМС
    private void checkPermissionToContact() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS},
                    ConstantsManager.SEND_SMS_PERMISSION_CODE);
            Snackbar.make(this.findViewById(R.id.container), "Для корректной работы необходимо дать требуемые разрешения",
                    Snackbar.LENGTH_LONG).setAction("Разрешить", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openApplicationSettings();
                }
            }).show();
        }
    }
    public void openApplicationSettings() {
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(appSettingsIntent, ConstantsManager.SEND_SMS_PERMISSION_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ConstantsManager.SEND_SMS_PERMISSION_CODE) {
            if (resultCode == RESULT_OK && data != null)  {
                Intent intent = new Intent(this, GetContactActivity.class);
                startActivity(intent);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ConstantsManager.SEND_SMS_PERMISSION_CODE && grantResults.length == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(this, GetContactActivity.class);
                startActivity(intent);
            }
        }
    }
}

