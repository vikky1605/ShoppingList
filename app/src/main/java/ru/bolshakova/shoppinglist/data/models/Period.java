package ru.bolshakova.shoppinglist.data.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import ru.bolshakova.shoppinglist.data.dataBase.DataBaseHelper;

@DatabaseTable(tableName = "period")
public class Period implements Serializable {
    private DataBaseHelper mDataBaseHelper;

    @DatabaseField(id = true, columnName = "initial_date", unique = true, canBeNull = false)
    public Long periodInitialDate;

    @DatabaseField(columnName = "final_date", unique = true, canBeNull = false)
    public Long periodFinalDate;

    @DatabaseField(columnName = "costs_Period", foreign = true, foreignAutoRefresh = true)
    public Costs costs;

    public Period(){};


    public Period(Long periodInitialDate, Long periodFinalDate) {
        this.periodInitialDate = periodInitialDate;
        this.periodFinalDate = periodFinalDate;
    }

    public Long getPeriodInitialDate() {
        return periodInitialDate;
    }

    public void setPeriodInitialDate(Long periodInitialDate) {
        this.periodInitialDate = periodInitialDate;
    }

    public Long getPeriodFinalDate() {
        return periodFinalDate;
    }

    public void setPeriodFinalDate(Long periodFinalDate) {
        this.periodFinalDate = periodFinalDate;
    }

    public Costs getCosts() {
        return costs;
    }

    public void setCosts(Costs costs) {
        this.costs = costs;
    }

    public Long calculatePeriodFinalDate(Long date) {
        Long date1 = date + 604800000;
        return date1;
    }

    public Long calculatePeriodInintialDate(Long date) {
        Long date1 = date - 604800000;
        return date1;
    }

    // метод возвращает начальную дату периода, который заканчивается в заданную дату
    public Date getPeriodInitialDate(Date date) {
        long time = date.getTime();
        time = time - 604800000;
        date = new Date(time);
        return date;
    }

    public static GregorianCalendar cleanDate (GregorianCalendar date) {
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        return date;
    }
 }
