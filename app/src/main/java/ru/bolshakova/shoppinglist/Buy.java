package ru.bolshakova.shoppinglist;

import java.util.GregorianCalendar;

/**
 * Created by bolshakova on 11.06.2016.
 */
public class Buy {
    String buyItem; // покупка
    boolean buyStatus; // состояние покупки (план - false, факт - true)
    GregorianCalendar buyDate; // дата покупки
    String buyImportance; // приоритет покупки
    String buyType; // вид покупки
    String buyUnit; // единица измерения товара (штука, кг, пачка, упаковка)
    int buyQuantity; // количество товара
    int buyUnitPrice; // цена за единицу товара
    int buyAmount; // стоимость покупки

    static String[]types = {"продукты", "алкоголь", "бытовая химия", "одежда и обувь", "прочее" };
    static String [] importance = {"обязательно", "важно", "неважно"};
    static String[] units = {"грамм", "килограмм", "литр", "пакет", "штука", "упаковка", "пачка", "бутылка"};
    static String[] status = {"планируемая", "совершенная"};


    // конструктор для планируемых покупок (нет даты покупки)
    public Buy (String buyItem, String buyType, String buyUnit, String buyImportance, int buyQuantity, int buyUnitPrice) {
        this.buyItem = buyItem;
        this.buyType = buyType;
        this.buyImportance = buyImportance;
        this.buyUnit = buyUnit;
        this.buyQuantity = buyQuantity;
        this.buyUnitPrice = buyUnitPrice;
        this.buyAmount = buyQuantity*buyUnitPrice;
        this.buyStatus = false;
    }

    // метож переводит покупку из статуса запланированная в статус фактическая и обновляет стоимость покупки и кол-во
    public void setBuyDate (GregorianCalendar date, int buyQuantity, int buyUnitPrice) {
        this.buyDate = date;
        this.buyStatus = true;
        this.buyQuantity = buyQuantity;
        this.buyUnitPrice = buyUnitPrice;
        this.buyAmount = buyQuantity*buyUnitPrice;
    }

}
