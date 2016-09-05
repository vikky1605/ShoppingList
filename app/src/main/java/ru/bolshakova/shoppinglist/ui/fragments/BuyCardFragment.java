package ru.bolshakova.shoppinglist.ui.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.bolshakova.shoppinglist.R;
import ru.bolshakova.shoppinglist.ui.activities.MainActivity;
import ru.bolshakova.shoppinglist.data.dataBase.DataBaseManager;
import ru.bolshakova.shoppinglist.data.models.Buy;
import ru.bolshakova.shoppinglist.data.storage.SettingsManager;

public class BuyCardFragment extends Fragment implements View.OnClickListener {

    private ImageView mDeleteBuyView, mChangeBuyView, mMoveBuyToNextPeriodView;
    private CheckBox mStatusBuyEnterCheckBox;
    private List<View> mSettingsBuyViews;
    private LinearLayout mContainerForBuyDetails;
    private List<Boolean> mSettingsBuy;
    private DataBaseManager mDataBaseManager;
    private MainActivity mActivity;
    private Buy mBuy;
    private TextView mImportance, mUnitBuy, mItemBuy;
    private EditText  mUnitPriceBuy, mQuantityBuy, mAmountBuy;
    private Spinner  mImportanceBuy;
    private View mView;

    public BuyCardFragment() {

    }

    public Buy getBuy() {
        return mBuy;
    }

    public void setBuy(Buy mBuy, MainActivity activity) {
        this.mBuy = mBuy;
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
        mView = inflater.inflate(R.layout.fragment_buy_card, container, false);
        mDeleteBuyView = (ImageView)mView.findViewById(R.id.delete_buy);
        mChangeBuyView = (ImageView) mView.findViewById(R.id.change_buy);
        mMoveBuyToNextPeriodView = (ImageView)mView.findViewById(R.id.move_buy_to_next_period);
        mStatusBuyEnterCheckBox = (CheckBox)mView.findViewById(R.id.status_buy_enter);
        mContainerForBuyDetails = (LinearLayout)mView.findViewById(R.id.container_for_buy_details);
        mItemBuy = (TextView)mView.findViewById(R.id.item_buy_enter);
        mUnitBuy = (TextView)mView.findViewById(R.id.unit_buy_enter);
        mQuantityBuy = (EditText)mView.findViewById(R.id.quantity_buy_enter);
        mUnitPriceBuy = (EditText)mView.findViewById(R.id.price_by_unit_enter);
        mAmountBuy =  (EditText)mView.findViewById(R.id.amount_buy_enter);
        mImportance = (TextView) mView.findViewById(R.id.importance);
        mImportanceBuy = (Spinner)mView.findViewById(R.id.importance_buy_enter);

        mDeleteBuyView.setOnClickListener(this);
        mChangeBuyView.setOnClickListener(this);
        mMoveBuyToNextPeriodView.setOnClickListener(this);


        mSettingsBuyViews = new ArrayList<>();
        mSettingsBuy = SettingsManager.getINSTANCE().getSettingsStorage().loadBuysSettings();
        mDataBaseManager = DataBaseManager.getInstance();
        mSettingsBuyViews = new ArrayList<>();
        mSettingsBuyViews.add(0, mItemBuy);
        mSettingsBuyViews.add(1,null);
        mSettingsBuyViews.add(2, mUnitBuy);
        mSettingsBuyViews.add(3, mQuantityBuy);
        mSettingsBuyViews.add(4, mUnitPriceBuy);
        mSettingsBuyViews.add(5, mAmountBuy);
        mSettingsBuyViews.add(6, mImportanceBuy);

        removeEditTextsInFragment();
        showBuyDetails();

        return mView;
    }

    private void showBuyDetails() {
       if (mSettingsBuyViews.get(0) != null && mBuy.getItemBuy() !=null) mItemBuy.setText(mBuy.getItemBuy());
       if (mSettingsBuyViews.get(2) != null && mBuy.getUnitBuy() !=null) mUnitBuy.setText(mBuy.getUnitBuy());
       if (mSettingsBuyViews.get(3) != null) mQuantityBuy.setText(String.valueOf(mBuy.getQuantityBuy()));
       if (mSettingsBuyViews.get(4) != null) mUnitPriceBuy.setText(String.valueOf(mBuy.getUnitPriceBuy()));
       if (mSettingsBuyViews.get(5) != null) mAmountBuy.setText(String.valueOf(mBuy.getAmountBuy()));
       if (mSettingsBuyViews.get(6) != null && mBuy.getImportanceBuy() !=null)
           mImportance.setText(mBuy.getImportanceBuy()+" важность");
        mStatusBuyEnterCheckBox.setChecked(mBuy.getStatusBuy());
    }

    private void removeEditTextsInFragment() {
        LinearLayout ll;

        if (!mSettingsBuy.get(2)) {
            ll = (LinearLayout) mContainerForBuyDetails.findViewById(R.id.container_for_buy_unit);
            ll.removeAllViews();
        }
        if (!mSettingsBuy.get(4)) {
            ll = (LinearLayout) mContainerForBuyDetails.findViewById(R.id.container_for_unit_buy_price);
            ll.removeAllViews();
        }
        if (!mSettingsBuy.get(6)) {
            ll = (LinearLayout)mContainerForBuyDetails.findViewById(R.id.container_for_buy_importance);
            ll.removeAllViews();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delete_buy: {
                mDataBaseManager.deleteBuyFromDB(mBuy);
                showSnackbar("покупка удалена из списка");
                break;
            }
            case R.id.change_buy: {
                mBuy = takeChangedBuy(mBuy);
                mDataBaseManager.updateBuyInDb(mBuy);
                showSnackbar("данные о покупке изменены");
                break;
            }
            case R.id.move_buy_to_next_period: {
                mDataBaseManager.moveBuyToNextPeriodInDb(mBuy);
                showSnackbar("покупка перенесена на следующую неделю");
                break;
            }
        }
    }

    private Buy takeChangedBuy(Buy buy) {
        Buy changedBuy = buy;

        if (mSettingsBuy.get(3)) changedBuy.setQuantityBuy(Integer.parseInt(mQuantityBuy.getText().toString()));
        else changedBuy.setQuantityBuy(1);
        if (mSettingsBuy.get(4)) changedBuy.setUnitPriceBuy(Integer.parseInt(mUnitPriceBuy.getText().toString()));
        else changedBuy.setUnitPriceBuy(Integer.parseInt(mAmountBuy.getText().toString()));
        changedBuy.setAmountBuy(Integer.parseInt(mAmountBuy.getText().toString()));
        if (mSettingsBuy.get(6) && !mImportanceBuy.getSelectedItem().equals("изменить?")) changedBuy.setImportanceBuy(mImportanceBuy.getSelectedItem().toString());
        changedBuy.setStatusBuy(mStatusBuyEnterCheckBox.isChecked());
        return changedBuy;
    }
    public void showSnackbar(String message) {
        Snackbar.make(mView, message, Snackbar.LENGTH_LONG).show();

    }
}
