package ru.bolshakova.shoppinglist.ui.activities;

import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ru.bolshakova.shoppinglist.R;
import ru.bolshakova.shoppinglist.data.storage.SettingStorage;
import ru.bolshakova.shoppinglist.data.storage.SettingsManager;

// активити для выбора контакта для отправки СМС со списком покупок

public class GetContactActivity extends AppCompatActivity {
    private List<String> mContactsList;
    private ListView mContactsListView;
    private String mPhoneNumber;
    private SettingStorage mSettingStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_contact);
        mContactsListView = (ListView)findViewById(R.id.contacts_list);
        mContactsList = new ArrayList<>();
        mSettingStorage = SettingsManager.getINSTANCE().getSettingsStorage();

        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                                   new String[] {ContactsContract.CommonDataKinds.Phone._ID,
                                                   ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                                                   ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
        startManagingCursor(cursor);

        if (cursor.getCount() > 0){
            while (cursor.moveToNext()){
            mContactsList.add(cursor.getString(1)+" "+cursor.getString(2));
        }
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked , mContactsList);
        mContactsListView.setAdapter(adapter);
            mContactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView v = (CheckedTextView) view;
                v.setChecked(true);
                int length = mContactsList.get(position).length();
                mPhoneNumber = mContactsList.get(position).substring(length - 12);
                savePhoneNumber(mPhoneNumber);
                backToSettingsActivity();
                }
            });
    }
}

    private void backToSettingsActivity() {
        showSnackbar("Контакт сохранен");
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    // сохранение выбранного контакта в пользовательских настройках
    private void savePhoneNumber(String mPhoneNumber) {
        mSettingStorage.savePhoneNumberInStorage(mPhoneNumber);

    }
    public void showSnackbar(String message) {
        Snackbar.make(this.findViewById(R.id.container), message, Snackbar.LENGTH_LONG).show();
    }
}
