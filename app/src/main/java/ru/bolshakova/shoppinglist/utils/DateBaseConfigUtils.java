package ru.bolshakova.shoppinglist.utils;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import ru.bolshakova.shoppinglist.data.models.Buy;
import ru.bolshakova.shoppinglist.data.models.Costs;
import ru.bolshakova.shoppinglist.data.models.Period;

public class DateBaseConfigUtils extends OrmLiteConfigUtil {


    private static final Class<?>[] classes = new Class[]{
            Period.class, Costs.class, Buy.class
    };

    public static void main(String[] args) throws SQLException, IOException {
        writeConfigFile(new File("app/src/main/res/raw/ormlite_config.txt"), classes);
    }

}


