package ru.bolshakova.shoppinglist.utils;

/**
 * Created by bolshakova on 29.07.2016.
 */
public interface ConstantsManager {

    int KEY_NO_FRAGMENT = 0;
    int KEY_FRAGMENT_TO_PLAN = 1;
    int KEY_FRAGMENT_DETAIL_COSTS = 2;
    int KEY_FRAGMENT_TO_ADD_BUY = 3;
    int KEY_FRAGMENT_TO_BUY_CARD = 4;

    String CHECK_ITEM_BUY = "наименование покупки";
    String CHECK_TYPE_BUY = "категория покупки";
    String CHECK_UNIT_BUY = "единица измерения";
    String CHECK_COUNT_BUY = "количество";
    String CHECK_PRICE_BY_UNIT_BUY = "цена за единицу";
    String CHECK_AMOUNT_BUY = "сумма покупки";
    String CHECK_IMPORTANCE_BUY = "приоритет покупки";

    String OUTPUT_BUY_LIST_PARAMETERS = "OUTPUT_BUY_LIST_PARAMETERS";
    int OUTPUT_PLAN_BUYS = 0;
    int OUTPUT_ALL_BUYS = 1;

    String PHONE_NUMBER = "PHONE_NUMBER";
    int SEND_SMS_PERMISSION_CODE = 100;

    String SAVED_INSTANCE_DATE = "SAVED_INSTANCE_DATE" ;
    String SAVED_INSTANCE_FRAGMENT = "SAVED_INSTANCE_FRAGMENT" ;
}
