package ru.bolshakova.shoppinglist.data.models;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;


@DatabaseTable(tableName = "buy")
public class Buy implements Serializable {
    public static final String INITIAL_DATE_BUY = "initial_date_buy_period";
    public static final String STATUS_BUY = "status_buy";

    @DatabaseField(generatedId = true)
    public Integer id;

    @DatabaseField(columnName = "initial_date_buy_period", canBeNull = false)
    public Long initialDate;

    @DatabaseField(columnName = "item_buy", dataType = DataType.STRING, canBeNull = false)
    public String itemBuy;

    @DatabaseField(columnName = "type_buy", dataType = DataType.STRING)
    public String typeBuy;

    @DatabaseField(columnName = "unit_buy", dataType = DataType.STRING)
    public String unitBuy;

    @DatabaseField(columnName = "quantity_buy")
    public int quantityBuy;

    @DatabaseField(columnName = "unit_price_buy")
    public int unitPriceBuy;

    @DatabaseField(columnName = "amount_buy", canBeNull = false)
    public int amountBuy;

    @DatabaseField(columnName = "status_buy", dataType = DataType.BOOLEAN, canBeNull = false)
    public boolean statusBuy;

    @DatabaseField(columnName = "importance_buy", dataType = DataType.STRING)
    public String importanceBuy;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(Long initialDate) {
        this.initialDate = initialDate;
    }

    public String getItemBuy() {
        return itemBuy;
    }

    public void setItemBuy(String itemBuy) {
        this.itemBuy = itemBuy;
    }

    public boolean getStatusBuy() {
        return statusBuy;
    }

    public void setStatusBuy(boolean statusBuy) {
        this.statusBuy = statusBuy;
    }

    public String getImportanceBuy() {
        return importanceBuy;
    }

    public void setImportanceBuy(String importanceBuy) {
        this.importanceBuy = importanceBuy;
    }

    public String getTypeBuy() {
        return typeBuy;
    }

    public void setTypeBuy(String typeBuy) {
        this.typeBuy = typeBuy;
    }

    public String getUnitBuy() {
        return unitBuy;
    }

    public void setUnitBuy(String unitBuy) {
        this.unitBuy = unitBuy;
    }

    public int getQuantityBuy() {
        return quantityBuy;
    }

    public void setQuantityBuy(int quantityBuy) {
        this.quantityBuy = quantityBuy;
    }

    public int getUnitPriceBuy() {
        return unitPriceBuy;
    }

    public void setUnitPriceBuy(int unitPriceBuy) {
        this.unitPriceBuy = unitPriceBuy;
    }

    public int getAmountBuy() {
        return amountBuy;
    }

    public void setAmountBuy(int amountBuy) {
        this.amountBuy = amountBuy;
    }
}
