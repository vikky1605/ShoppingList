package ru.bolshakova.shoppinglist;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends Activity {

    static Period periodGui; // период, относительно которого выводится главное окно приложения
    public static SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Period period = new Period(this);
        Date date = new Date();
        Date[] dates = period.getCurrentPeriod(date);
        String text = "с " + df.format(dates[0]) + " по " + df.format(dates[1]);
        // выводим текущий период
        TextView textPeriod = (TextView)findViewById(R.id.period);
        textPeriod.setText(text.toString());
        // выводим текущее состояние баланса
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String strDate = ""+ dates[0].getTime();
        Cursor cursor = db.query(dbHelper.TABLE_COSTS,
                                 new String[] {dbHelper.PLAN_COSTS, dbHelper.PLAN_BUYS_COSTS, dbHelper.FACT_COSTS},
                                 dbHelper.PERIOD_INITIAL_DATE + " = ?", new String[] {strDate},
                                 null, null, null);
        cursor.moveToFirst();
        int balance = cursor.getInt(0) - cursor.getInt(1) - cursor.getInt(2);
        Button textBalance = (Button)findViewById(R.id.balanse);
        textBalance.setText(balance + " рублей");
    }

    public void onClickToPlanCosts(View view) {
        Intent intent = new Intent(this, toPlanCostsActivity.class);
        startActivity(intent);
    }

    public void onClickNextPeriod(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        Date date = new Date();
        Period period = new Period(this);
        date = period.getPeriodFinalDate(date);
        Date[] dates = period.getCurrentPeriod(date);

    }
}
