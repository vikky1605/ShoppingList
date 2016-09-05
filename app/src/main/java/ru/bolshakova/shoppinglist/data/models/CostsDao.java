package ru.bolshakova.shoppinglist.data.models;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class CostsDao extends BaseDaoImpl<Costs, Integer> {

    public CostsDao(Class<Costs> dataClass) throws SQLException {
        super(dataClass);
    }

    public CostsDao(ConnectionSource connectionSource, Class<Costs> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }
}
