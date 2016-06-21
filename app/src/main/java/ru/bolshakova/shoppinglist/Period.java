package ru.bolshakova.shoppinglist;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by bolshakova on 11.06.2016.
 */
public class Period {

    public Context contextPeriod;
    public int idPeriod;

    Period () {}

    public Period (Context context) {
        this.contextPeriod = context;
    }

    // метод возвращает массив из начальной и конечной дат периода, содержащего заданную дату
    public Date[] getCurrentPeriod (Date today) {
        DBHelper dbHelper = new DBHelper(this.contextPeriod);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Date date = new Date();
        Date date1 = new Date();
        while (!this.checkPeriod(today)) { this.addNewPeriodToDB(); System.out.println("добавил новый период"); }

        Cursor cursor = db.query(DBHelper.TABLE_PERIOD, new String[]{DBHelper.PERIOD_INITIAL_DATE, DBHelper.PERIOD_FINAL_DATE}, null, null, null, null, null);
        cursor.moveToLast();

        boolean check = true;
        while (check) {
            date = new Date (cursor.getLong(0));
            date1 = new Date(cursor.getLong(1));
            if (today.before(date1) && today.after(date)) { check = false; }
            else cursor.moveToPrevious(); }

        cursor.close();

        Date[ ] dates = {date,date1};
        return dates;
    }

    // метол добавляет новый период в базу данных (период, следующий за последним занесенным)
    public  void addNewPeriodToDB () {
        DBHelper dbHelper = new DBHelper(this.contextPeriod);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(DBHelper.TABLE_PERIOD, new String[]{DBHelper.PERIOD_INITIAL_DATE, DBHelper.PERIOD_FINAL_DATE}, null, null, null, null, null);
        cursor.moveToLast();
        Date date = new Date(cursor.getLong(1));
        cursor.close();
        Date date1 = this.getPeriodFinalDate(date);
        ContentValues cv = new ContentValues();
        cv.put(dbHelper.PERIOD_INITIAL_DATE, date.getTime());
        cv.put(dbHelper.PERIOD_FINAL_DATE, date1.getTime());
        db.insert(dbHelper.TABLE_PERIOD, null, cv);
    }

    // метод возвращает последнюю дату периода, начинающегося с заданной даты
    public Date getPeriodFinalDate(Date date) {
        long time = date.getTime();
        time = time + 604800000;
        date = new Date(time);
        return date;
    }

    // метод возвращает начальную дату периода, который заканчивается в заданную дату
    public Date getPeriodInitialDate(Date date) {
        long time = date.getTime();
        time = time - 604800000;
        date = new Date(time);
        return date;
    }

    // метод проверяет, есть ли в базе данных период, содержащмй заданную дату
    public boolean checkPeriod (Date today) {
        boolean check = false;
        DBHelper dbHelper = new DBHelper(this.contextPeriod);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(DBHelper.TABLE_PERIOD, new String[]{DBHelper.PERIOD_INITIAL_DATE, DBHelper.PERIOD_FINAL_DATE}, null, null, null, null, null);
        cursor.moveToFirst();
        Date date = new Date(cursor.getLong(0));
        cursor.moveToLast();
        Date date1 = new Date(cursor.getLong(1));
        cursor.close();
        if (today.after(date) && today.before(date1)) check = true;
        return check;

    }

    // метод очищает дату, то есть отбрасывает часы, минуты, секунды и милисекунды
    public GregorianCalendar cleanDate (GregorianCalendar date) {
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        return date;
    }
}

