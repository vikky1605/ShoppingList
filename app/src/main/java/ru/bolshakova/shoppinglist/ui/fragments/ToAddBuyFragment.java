package ru.bolshakova.shoppinglist.ui.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.support.design.widget.Snackbar;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import ru.bolshakova.shoppinglist.R;
import ru.bolshakova.shoppinglist.ui.activities.MainActivity;
import ru.bolshakova.shoppinglist.data.dataBase.DataBaseManager;
import ru.bolshakova.shoppinglist.data.models.Buy;
import ru.bolshakova.shoppinglist.data.storage.SettingsManager;

public class ToAddBuyFragment extends Fragment implements View.OnClickListener {

    private ImageView mSaveBuyButton;
    private CheckBox mStatusBuyEnterCheckBox;
    private List<View> mSettingsBuyViews;
    private List<LinearLayout> mContainerForInputFields;
    private LinearLayout mContainerForInput;
    private Long mPeriodInitialDate;
    private List<Boolean> mSettingsBuy;
    private DataBaseManager mDataBaseManager;
    private MainActivity mActivity;
    InputMethodManager imm;
    private EditText mItemBuy, mUnitPriceBuy, mQuantityBuy, mAmountBuy;
    private Spinner mTypeBuy, mUnitBuy, mImportanceBuy;
    View view;

    public ToAddBuyFragment() {
    }

    public void setPeriodInitialDate(Long mPeriodInitialDate, MainActivity activity) {
        this.mPeriodInitialDate = mPeriodInitialDate;
        this.mActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_to_add_buy,  container, false);
        imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mSaveBuyButton = (ImageView)view.findViewById(R.id.save_button);
        mStatusBuyEnterCheckBox = (CheckBox)view.findViewById(R.id.status_buy_enter);
        mContainerForInput = (LinearLayout)view.findViewById(R.id.container_for_input);

        mSaveBuyButton.setOnClickListener(this);

        mSettingsBuyViews = new ArrayList<>();
        mSettingsBuy = SettingsManager.getINSTANCE().getSettingsStorage().loadBuysSettings();
        mDataBaseManager = DataBaseManager.getInstance();
        mItemBuy = (EditText)view.findViewById(R.id.item_buy_enter);
        mTypeBuy = (Spinner) view.findViewById(R.id.type_buy_enter);
        mUnitBuy = (Spinner) view.findViewById(R.id.unit_buy_enter);
        mQuantityBuy = (EditText)view.findViewById(R.id.quantity_buy_enter);
        mUnitPriceBuy = (EditText)view.findViewById(R.id.price_by_unit_enter);
        mAmountBuy =  (EditText)view.findViewById(R.id.amount_buy_enter);
        mImportanceBuy = (Spinner)view.findViewById(R.id.importance_buy_enter);

        mSettingsBuyViews = new ArrayList<>();
        mSettingsBuyViews.add(0, mItemBuy);
        mSettingsBuyViews.add(1, mTypeBuy);
        mSettingsBuyViews.add(2, mUnitBuy);
        mSettingsBuyViews.add(3, mQuantityBuy);
        mSettingsBuyViews.add(4, mUnitPriceBuy);
        mSettingsBuyViews.add(5, mAmountBuy);
        mSettingsBuyViews.add(6, mImportanceBuy);
        mSettingsBuyViews.add(7, view.findViewById(R.id.text_for_buy_item));
        mSettingsBuyViews.add(8, view.findViewById(R.id.text_buy_type));
        mSettingsBuyViews.add(9, view.findViewById(R.id.text_for_buy_unit));
        mSettingsBuyViews.add(10,view.findViewById(R.id.text_for_buy_quantity));
        mSettingsBuyViews.add(11,view.findViewById(R.id.text_for_unit_buy_price));
        mSettingsBuyViews.add(12,view.findViewById(R.id.text_for_buy_amount));
        mSettingsBuyViews.add(13,view.findViewById(R.id.text_for_buy_importance));
        mContainerForInputFields = new ArrayList<>();
        mContainerForInputFields.add(0, (LinearLayout) mContainerForInput.findViewById(R.id.container_for_buy_item));
        mContainerForInputFields.add(1, (LinearLayout) mContainerForInput.findViewById(R.id.container_for_buy_type));
        mContainerForInputFields.add(2, (LinearLayout) mContainerForInput.findViewById(R.id.container_for_buy_unit));
        mContainerForInputFields.add(3, (LinearLayout) mContainerForInput.findViewById(R.id.container_for_buy_quantity));
        mContainerForInputFields.add(4, (LinearLayout) mContainerForInput.findViewById(R.id.container_for_unit_buy_price));
        mContainerForInputFields.add(5, (LinearLayout) mContainerForInput.findViewById(R.id.container_for_buy_amount));
        mContainerForInputFields.add(6, (LinearLayout) mContainerForInput.findViewById(R.id.container_for_buy_importance));
        mSettingsBuyViews.get(0).setFocusable(true);

        removeEditTextsInFragement();
        return view;
    }

    private void removeEditTextsInFragement() {
        for (int i = 0; i < mSettingsBuy.size(); i++) {
            if (!mSettingsBuy.get(i)) {
                mContainerForInputFields.get(i).removeView(mSettingsBuyViews.get(i));
                mContainerForInputFields.get(i).removeView(mSettingsBuyViews.get(i+7));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.save_button: {
                if (mItemBuy.getText().toString().equals("") ||
                    mAmountBuy.getText().toString().equals("")) {
                    showSnackbar("Введите наименование покупки и ее стоимость, иначе покупка не будет сохранена");
                }
                else {
                    saveBuyInDb();
                    showSnackbar("Покупка сохранена в список");
                    cleanInputField();
                }
                break;
            }
        }
   }

    private void cleanInputField() {
        mItemBuy.setText("");
        if (mSettingsBuy.get(1)) mTypeBuy.setSelection(0);
        if (mSettingsBuy.get(2)) mUnitBuy.setSelection(0);
        if (mSettingsBuy.get(3)) mUnitPriceBuy.setText("0");
        if (mSettingsBuy.get(4)) mQuantityBuy.setText("1");
        mAmountBuy.setText("");
        if (mSettingsBuy.get(5)) mImportanceBuy.setSelection(0);
        mStatusBuyEnterCheckBox.setChecked(false);
    }


    public void showSnackbar(String message) {
            Snackbar.make(mActivity.containerForFragment, message, Snackbar.LENGTH_LONG).show();

        }

    private void saveBuyInDb() {
        Buy buy = new Buy();
        buy.setInitialDate(mPeriodInitialDate);
        buy.setItemBuy(mItemBuy.getText().toString());
        if (!mSettingsBuy.get(1) || mTypeBuy.getSelectedItemId() == 0) buy.setTypeBuy("прочее");
            else buy.setTypeBuy(mTypeBuy.getSelectedItem().toString());
        if (!mSettingsBuy.get(2) || mUnitBuy.getSelectedItemId() == 0) buy.setUnitBuy("шт.");
            else buy.setUnitBuy(mUnitBuy.getSelectedItem().toString());
        if (!mSettingsBuy.get(3) || mQuantityBuy.getText()==null) buy.setQuantityBuy(1);
            else buy.setQuantityBuy(Integer.parseInt(mQuantityBuy.getText().toString()));
        if (!mSettingsBuy.get(4) || mUnitPriceBuy.getText()==null) buy.setUnitPriceBuy(Integer.parseInt(mAmountBuy.getText().toString()));
            else buy.setUnitPriceBuy(Integer.parseInt(mUnitPriceBuy.getText().toString()));
        buy.setAmountBuy(Integer.parseInt(mAmountBuy.getText().toString()));
        if (!mSettingsBuy.get(6) || mImportanceBuy.getSelectedItemId() == 0) buy.setImportanceBuy("средняя");
            else buy.setImportanceBuy(mImportanceBuy.getSelectedItem().toString());
        buy.setStatusBuy(mStatusBuyEnterCheckBox.isChecked());
        mDataBaseManager.createBuyInDb(buy);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
