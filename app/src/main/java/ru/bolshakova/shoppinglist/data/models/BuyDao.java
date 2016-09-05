package ru.bolshakova.shoppinglist.data.models;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class BuyDao extends BaseDaoImpl<Buy, Integer> {

    public BuyDao(Class<Buy> dataClass) throws SQLException {
        super(dataClass);
    }

    public BuyDao(ConnectionSource connectionSource, Class<Buy> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }
}
