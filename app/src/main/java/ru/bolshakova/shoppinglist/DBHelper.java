package ru.bolshakova.shoppinglist;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by bolshakova on 12.06.2016.
 */
public class DBHelper extends SQLiteOpenHelper {
    static String DB_NAME = "test_db";
    static int DB_VERSION = 1;

    // первая таблица - периоды (недели) для учета расходов
    static String TABLE_PERIOD = "period";
    static String PERIOD_INITIAL_DATE = "initial_date";
    static String PERIOD_FINAL_DATE = "final_date";

    // вторая таблица - расходы за каждый из недельных периодов
    static String TABLE_COSTS = "costs_table";
    static String PLAN_COSTS = "plan_costs";
    static String FACT_COSTS = "fact_costs";
    static String PLAN_BUYS_COSTS = "plan_buys_costs";

    // третья таблица содержит данные обо всех покупках совершенных и\или запланированных
    // каждой строке в данной таблице соответствует строка из таблицы OOSTS (соответствие по периоду)
    private static String TABLE_BUYS = "buys_table";
    private static String BUY_ITEM = "buy_item";
    private static String BUY_STATUS = "buy_status";
    private static String BUY_IMPORTANCE = "buy_importance";
    private static String BUY_TYPE = "buy_type";
    private static String BUY_UNIT = "buy_unit";
    private static String BUY_QUANTITY = "buy_quantity";
    private static String BUY_UNIT_PRICE = "buy_unit_price";
    private static String BUY_AMOUNT = "buy_amount";
    private static String COSTS_ID = "costs_id";

    // четвертая таблица содержит данные о категориях покупок
    private static String TABLE_BUY_TYPES = "buy_types_table";
    private static String BUY_TYPE_DESCRIPTION = "buy_type_description";

    // пятая таблица содержит данные о видах единиц измерения
    private static String TABLE_UNIT_TYPES = "table_unit_types";
    private static String UNIT_TYPE_DESCRIPTION = "unit_type_description";

    // шестая таблица содержит данные о приоритетах покупок
    private static String TABLE_BUY_IMPORTANCE = "table_buy_importance";
    private static String BUY_IMPORTANCE_DESCRIPTION = "buy_importance_description";

    // массивы для заполнения третьей, четвертой, пятой таблиц
    static String[]types = {"продукты", "алкоголь", "бытовая химия", "одежда и обувь", "прочее" };
    static String [] importance = {"обязательно", "важно", "неважно"};
    static String[] units = {"грамм", "килограмм", "литр", "пакет", "штука", "упаковка", "пачка", "бутылка"};



    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // создаем первую таблицу - периоды
        db.execSQL("CREATE TABLE " + TABLE_PERIOD  + "( _ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PERIOD_INITIAL_DATE + " LONG, "
                + PERIOD_FINAL_DATE + " LONG);");

        // заносим в первую таблицу первый период, начинается с даты первого запуска приложения
        ContentValues cv = new ContentValues();
        Period period = new Period();
        GregorianCalendar date = new GregorianCalendar();
        date = period.cleanDate(date);
        GregorianCalendar date1 = new GregorianCalendar(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
        date1.add(Calendar.DAY_OF_YEAR,7);
        cv.put(PERIOD_INITIAL_DATE, date.getTimeInMillis());
        cv.put(PERIOD_FINAL_DATE, date1.getTimeInMillis());
        db.insert (TABLE_PERIOD, null, cv);
        cv.clear();

        // создаем вторую таблицу - расходы за каждый недельный период
        db.execSQL("CREATE TABLE " + TABLE_COSTS + "( _ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PERIOD_INITIAL_DATE + " LONG, "
                + PLAN_COSTS + " INTEGER, "
                + PLAN_BUYS_COSTS + " INTEGER, "
                + FACT_COSTS + " INTEGER);");
        // заносим во вторую таблицу нулевые расходы в первом периоде
        cv.put(PERIOD_INITIAL_DATE, date.getTimeInMillis());
        cv.put(PLAN_COSTS, 0);
        cv.put(PLAN_BUYS_COSTS, 0);
        cv.put(FACT_COSTS, 0);
        db.insert(TABLE_COSTS, null, cv);
        cv.clear();

        // создаем третью таблицу для хранения планируемых и совершенных покупок
        db.execSQL("CREATE TABLE " + TABLE_BUYS  + "( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
                                                 + BUY_ITEM + " TEXT, "
                                                 + BUY_STATUS + " INTEGER, "
                                                 + BUY_IMPORTANCE + " INTEGER, "
                                                 + BUY_TYPE + " INTEGER, "
                                                 + BUY_UNIT + " INTEGER, "
                                                 + BUY_QUANTITY + " INTEGER, "
                                                 + BUY_UNIT_PRICE + " INTEGER, "
                                                 + BUY_AMOUNT + " INTEGER, "
                                                 + COSTS_ID  + " INTEGER);");

        // создаем четвертую таблицу для хранения видов покупок
        db.execSQL("CREATE TABLE " + TABLE_BUY_TYPES + "( _ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                                                     + BUY_TYPE_DESCRIPTION + " TEXT);");

        // заполняем четвертую таблицу вариантами видов покупок
        ContentValues buyTypes;
        for (int i = 0; i < types.length; i++) {
            buyTypes = new ContentValues();
            buyTypes.put(BUY_TYPE_DESCRIPTION, types[i]);
            db.insert(TABLE_BUY_TYPES, null, buyTypes);
            buyTypes.clear();
        }

        // создаем пятую таблицу для хранения единиц измерения покупок
        db.execSQL("CREATE TABLE " + TABLE_UNIT_TYPES + "( _ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                                                      + UNIT_TYPE_DESCRIPTION + " TEXT);");

        // заполяем пятую таблицу вариантами единиц измерения
        ContentValues unitTypes;
        for (int i = 0; i < units.length; i++) {
            unitTypes = new ContentValues();
            unitTypes.put(UNIT_TYPE_DESCRIPTION, units[i]);
            db.insert(TABLE_UNIT_TYPES, null, unitTypes);
            unitTypes.clear();
        }
        // создаем таблицу для хранения вариантов степени важности покупки
        db.execSQL("CREATE TABLE " + TABLE_BUY_IMPORTANCE + "( _ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                                                          + BUY_IMPORTANCE_DESCRIPTION + " TEXT);");

        // заполняем таблицу вариантами степени важности покупки
        ContentValues importanceTypes;
        for (int i = 0; i < importance.length; i++) {
            importanceTypes = new ContentValues();
            importanceTypes.put(BUY_IMPORTANCE_DESCRIPTION, importance[i]);
            db.insert(TABLE_BUY_IMPORTANCE, null, importanceTypes);
            importanceTypes.clear();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}















