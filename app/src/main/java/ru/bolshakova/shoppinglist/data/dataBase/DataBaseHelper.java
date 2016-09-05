package ru.bolshakova.shoppinglist.data.dataBase;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.util.ArrayList;
import java.util.List;

import ru.bolshakova.shoppinglist.data.models.Buy;
import ru.bolshakova.shoppinglist.data.models.Costs;
import ru.bolshakova.shoppinglist.data.models.Period;

public class DataBaseHelper extends OrmLiteSqliteOpenHelper {


    private final static String DATABASE_NAME = "costs_db";
    private final static int DATABASE_VERSION = 1;

    private Dao<Buy, Integer> buyDao = null;
    private Dao<Costs, Integer> costsDao = null;
    private Dao<Period, Integer> periodDao = null;


    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Buy.class);
            TableUtils.createTable(connectionSource, Costs.class);
            TableUtils.createTable(connectionSource, Period.class);
                    }
        catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            List<String> allSql = new ArrayList<String>();
            switch (oldVersion) {
                case 1:
                    //allSql.add("alter table AdData add column `new_col` VARCHAR");
                    //allSql.add("alter table AdData add column `new_col2` VARCHAR");
            }
            for (String sql : allSql) {
                database.execSQL(sql);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Dao<Buy, Integer> getBuyDao() {
        if (null == buyDao) {
            try {
                buyDao = getDao(Buy.class);
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return buyDao;
    }

    public Dao<Costs, Integer> getCostsDao() {
        if (null == costsDao) {
            try {
                costsDao = getDao(Costs.class);
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return costsDao;
    }

    public Dao<Period, Integer> getPeriodDao() {
        try {
            if (periodDao == null) {
                periodDao = getDao(Period.class);
            }
        }
            catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        return periodDao;
    }

    @Override
    public void close() {
        super.close();
        periodDao = null;
        costsDao = null;
        buyDao = null;
    }
}
