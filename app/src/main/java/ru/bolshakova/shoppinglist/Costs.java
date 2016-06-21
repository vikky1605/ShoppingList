package ru.bolshakova.shoppinglist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;


/**
 * Created by bolshakova on 11.06.2016.
 */

public class Costs {

    public Context costsContext;

    Costs(Context context) {
        this.costsContext = context;
    }

    // метод возвращает сумму плановых расходов на текущий период
    public int getPlanCosts(Date date) {
        int planCosts = 0;
        Period period = new Period(this.costsContext);
        Date[] dates = period.getCurrentPeriod(date);
        String strDate = "" + dates[0].getTime();

        DBHelper dbHelper = new DBHelper(this.costsContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(dbHelper.TABLE_COSTS, new String[]{dbHelper.PLAN_COSTS},
                dbHelper.PERIOD_INITIAL_DATE + "=?", new String[]{strDate}, null, null, null);

        if (cursor.moveToFirst()) {
            planCosts = cursor.getInt(0);
            cursor.close();
        } else {
            ContentValues cv = new ContentValues();
            cv.put(dbHelper.PLAN_COSTS, 0);
            db.insert(dbHelper.TABLE_COSTS, null, cv);
        }
        return planCosts;
    }

    // метод добавляет плановые расходы в период, соответствующий заданной дате
    public void addPlanCostsToDB(int planCosts, Date date) {
        Period period = new Period(this.costsContext);
        Date[] dates = period.getCurrentPeriod(date);
        System.out.println("добавил новый период");
        String strDate = "" + dates[0].getTime();

        DBHelper dbHelper = new DBHelper(this.costsContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(dbHelper.PLAN_COSTS, planCosts);
        db.update(dbHelper.TABLE_COSTS, cv, dbHelper.PERIOD_INITIAL_DATE + "=?", new String[]{strDate});
    }
}
