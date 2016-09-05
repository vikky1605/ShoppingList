package ru.bolshakova.shoppinglist.data.models;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;


@DatabaseTable(tableName = "costs")
public class Costs implements Serializable {
    public static final String INITIAL_DATE_COSTS = "initial_date_costs";

    @DatabaseField(generatedId = true)
    public int id;

    @DatabaseField(columnName = "initial_date_costs", canBeNull = false, unique = true)
    public Long initialDateCosts;

    @DatabaseField(columnName = "plan_costs", dataType = DataType.INTEGER)
    public int planCosts;

    @DatabaseField(columnName = "fact_costs", dataType = DataType.INTEGER)
    public int factCosts;

    @DatabaseField(columnName = "plan_buy_costs", dataType = DataType.INTEGER)
    public int planBuysCosts;

    public Costs(){}


    public Costs(Long initialDateCosts) {
        this.initialDateCosts = initialDateCosts;
        this.planCosts = 0;
    }

    public int getPlanCosts() {
        return planCosts;
    }

    public void setPlanCosts(int planCosts) {
        this.planCosts = planCosts;
    }

    public Long getInitialDateCosts() {
        return initialDateCosts;
    }

    public void setInitialDateCosts(Long initialDateCosts) {
        this.initialDateCosts = initialDateCosts;
    }

    public int getFactCosts() {
        return factCosts;
    }

    public void setFactCosts(int factCosts) {
        this.factCosts = factCosts;
    }

    public int getPlanBuysCosts() {
        return planBuysCosts;
    }

    public void setPlanBuysCosts(int planBuysCosts) {
        this.planBuysCosts = planBuysCosts;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

