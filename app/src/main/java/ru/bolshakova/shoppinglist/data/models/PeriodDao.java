package ru.bolshakova.shoppinglist.data.models;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class PeriodDao extends BaseDaoImpl<Period, Integer> {

    public PeriodDao(Class<Period> dataClass) throws SQLException {
        super(dataClass);
    }

    public PeriodDao(ConnectionSource connectionSource, Class<Period> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

 }
