package ru.bolshakova.shoppinglist.ui.activities;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.bolshakova.shoppinglist.R;
import ru.bolshakova.shoppinglist.adapters.RecyclerViewAdapter;
import ru.bolshakova.shoppinglist.data.models.Buy;
import ru.bolshakova.shoppinglist.data.models.Costs;
import ru.bolshakova.shoppinglist.data.models.Period;
import ru.bolshakova.shoppinglist.data.storage.SettingStorage;
import ru.bolshakova.shoppinglist.data.storage.SettingsManager;
import ru.bolshakova.shoppinglist.ui.fragments.BuyCardFragment;
import ru.bolshakova.shoppinglist.ui.fragments.ShowCostsDetailsFragment;
import ru.bolshakova.shoppinglist.ui.fragments.ToAddBuyFragment;
import ru.bolshakova.shoppinglist.ui.fragments.ToPlanCostsFragment;
import ru.bolshakova.shoppinglist.data.dataBase.DataBaseManager;
import ru.bolshakova.shoppinglist.utils.ConstantsManager;
import ru.bolshakova.shoppinglist.utils.CustomOnClickListener;
import ru.bolshakova.shoppinglist.utils.CustomTextView;
import ru.bolshakova.shoppinglist.utils.SwipeableRecyclerViewTouchListener;

// основной экран приложения
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

   public static SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
   private CustomTextView mPeriodView, mBalanceView, mResultView;
   private ImageView mToPlanCostsView, mToNextPeriodView, mToPreviousPeriodView,
            mToShowCostsDetailsView, mSettingsBuysView, mAddBuyView, mSendSmsView;
   private Toolbar mToolbar;
   public FrameLayout containerForFragment, containerForPlanFragment;
   private Date mDate;
   private int mBalance;
   private Period mPeriodForActivity;
   private DataBaseManager mDataBaseManager;
   private FragmentTransaction mFragmentTransaction;
   private int mCheckShowFragment;
   private SettingStorage mSettingStorage;
   private List<Buy> mBuyList;
   private Buy mBuy;
   private RecyclerViewAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDataBaseManager = DataBaseManager.getInstance();
        mSettingStorage = SettingsManager.getINSTANCE().getSettingsStorage();

        mPeriodView = (CustomTextView) findViewById(R.id.period);
        mBalanceView = (CustomTextView) findViewById(R.id.balanse);
        mResultView = (CustomTextView) findViewById(R.id.result);
        mToPlanCostsView = (ImageView) findViewById(R.id.to_plan_costs);
        mToNextPeriodView = (ImageView) findViewById(R.id.to_next_period);
        mToPreviousPeriodView = (ImageView) findViewById(R.id.to_previous_period);
        mSettingsBuysView = (ImageView) findViewById(R.id.settings_buys);
        mToShowCostsDetailsView = (ImageView) findViewById(R.id.show_costs_details);
        mAddBuyView = (ImageView) findViewById(R.id.add_buy);
        mSendSmsView = (ImageView) findViewById(R.id.send_sms);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        containerForFragment = (FrameLayout) findViewById(R.id.container_for_fragment);
        containerForPlanFragment = (FrameLayout) findViewById(R.id.container_for_plan_fragment);

        if (Integer.valueOf(Build.VERSION.SDK_INT) <23) {
        setSupportActionBar(mToolbar);
        }

        mDate = new Date();
        mCheckShowFragment = 0;

        if (savedInstanceState != null) {
            mDate = new Date(savedInstanceState.getLong(ConstantsManager.SAVED_INSTANCE_DATE));
            mCheckShowFragment = savedInstanceState.getInt(ConstantsManager.SAVED_INSTANCE_FRAGMENT);
            if (mCheckShowFragment == 3 || mCheckShowFragment == 4) {
                mAddBuyView.setImageResource(R.drawable.ic_check_circle_red_24dp);
            }
            if (mCheckShowFragment == 1) mToPlanCostsView.setImageResource(R.drawable.ic_done_white_24dp);
            if (mCheckShowFragment == 2) mToShowCostsDetailsView.setImageResource(R.drawable.ic_clear_white_24dp);
        }

        mToPlanCostsView.setOnClickListener(this);
        mToNextPeriodView.setOnClickListener(this);
        mToPreviousPeriodView.setOnClickListener(this);
        mToShowCostsDetailsView.setOnClickListener(this);
        mSettingsBuysView.setOnClickListener(this);
        mAddBuyView.setOnClickListener(this);
        mSendSmsView.setOnClickListener(this);

        loadDateForActivity();
        updateDataForActivity();
    }

    // вывод на экран приложения данных о текущем балансе расходов и списка покупок
    public void updateDataForActivity() {
        loadBalanceForActitvity();
        mBuyList = getBuyList();
        loadBuyList(mBuyList);
    }

    // получение данных для списка покупок из базы данных
    public List<Buy> getBuyList() {
        int buyListSetting = mSettingStorage.loadBuyListParameters();
        switch (buyListSetting) {
            case ConstantsManager.OUTPUT_PLAN_BUYS: {
                mBuyList = mDataBaseManager.getPlanBuyList(mPeriodForActivity.getPeriodInitialDate());
                break;
            }
            case ConstantsManager.OUTPUT_ALL_BUYS: {
                mBuyList = mDataBaseManager.getBuyList(mPeriodForActivity.getPeriodInitialDate());
                break;
            }
        }
        return mBuyList;
    }

    // вывод на экран приложения списка покупок
    public void loadBuyList(List<Buy> buyList) {
        mBuyList = buyList;
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new RecyclerViewAdapter(mBuyList, new CustomOnClickListener() {
            @Override
            public void onBuyItemClickListener(int position) {
                mBuy = mBuyList.get(position);
                if (mBuy != null) {
                    manageFragment(mCheckShowFragment, ConstantsManager.KEY_FRAGMENT_TO_BUY_CARD);
                }
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(itemAnimator);
        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(recyclerView,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipeLeft(int position) {
                                return true;
                            }

                            @Override
                            public boolean canSwipeRight(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    deleteBuy(mBuyList.get(position));
                                    mAdapter.notifyItemRemoved(position);
                                }
                                mAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    deleteBuy(mBuyList.get(position));
                                    mAdapter.notifyItemRemoved(position);
                                }
                                mAdapter.notifyDataSetChanged();
                            }
                        });

        recyclerView.addOnItemTouchListener(swipeTouchListener);
    }


    // вывод на экран приложения данных о текущем балансе
    public void loadBalanceForActitvity() {
        mBalance = mDataBaseManager.getCurrentBalance(mPeriodForActivity.getPeriodInitialDate());
        if (mBalance >= 0) {
            mResultView.setText("Остаток");
            mResultView.setTextColor(getResources().getColor(R.color.white));
            mBalanceView.setTextColor(getResources().getColor(R.color.white));
        }
        else {
            mResultView.setText("Перерасход");
            mBalanceView.setTextColor(getResources().getColor(R.color.colorAccent));
            mResultView.setTextColor(getResources().getColor(R.color.colorAccent));

        }
        mBalanceView.setText(mBalance + " рублей");

    }

    // сохранение данных о текущем периоде и загруженном фрагменте при повороте экрана
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ConstantsManager.SAVED_INSTANCE_FRAGMENT, mCheckShowFragment);
        Long activityDate = mDate.getTime();
        outState.putLong(ConstantsManager.SAVED_INSTANCE_DATE, activityDate);
    }

    // вывод на экран приложения периода для планирования
    private void loadDateForActivity() {
        if (mDataBaseManager.checkEmptyDb()) {
            mPeriodForActivity = mDataBaseManager.createFirstPeriodInDb();
        } else {
            mPeriodForActivity = mDataBaseManager.getCurrentPeriod(mDate);
        }
        Date date1 = new Date(mPeriodForActivity.getPeriodInitialDate());
        Date date2 = new Date(mPeriodForActivity.getPeriodFinalDate());
        mPeriodView.setText("с " + df.format(date1) + " по " + df.format(date2));
    }


    // обработка кликов по объектам активити
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_buy: {
                if (mCheckShowFragment == ConstantsManager.KEY_FRAGMENT_TO_BUY_CARD) {
                    manageFragment(mCheckShowFragment, ConstantsManager.KEY_FRAGMENT_TO_BUY_CARD);
                }
                else manageFragment(mCheckShowFragment, ConstantsManager.KEY_FRAGMENT_TO_ADD_BUY);
                break;
            }
            case R.id.to_plan_costs: {
                manageFragment(mCheckShowFragment,ConstantsManager.KEY_FRAGMENT_TO_PLAN);
                break;
            }
            case R.id.show_costs_details: {
                manageFragment(mCheckShowFragment, ConstantsManager.KEY_FRAGMENT_DETAIL_COSTS);
                break;
            }
            case R.id.to_next_period: {
                mDate = new Date(mDate.getTime() + 604800000);
                loadDateForActivity();
                updateDataForActivity();
                break;
            }
            case R.id.to_previous_period: {
                mDate = new Date(mDate.getTime() - 604800000);
                loadDateForActivity();
                updateDataForActivity();
                break;
            }
            case R.id.settings_buys: {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.send_sms: {
                sendSms();
                break;
            }
        }
    }

    // олучение разрешений и отправка СМС со списком покупок
    private void sendSms() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            String message = makeTextSms();
            SmsManager sms = SmsManager.getDefault();
            String phoneNumber = mSettingStorage.loadPhoneNumber();
            if (phoneNumber.equals("")) {
                showSnackbar("выберите контакт для СМС через настройки приложения");
            } else {
                ArrayList<String> dividedText = sms.divideMessage(message);
                for (String shortMessage : dividedText) {
                    sms.sendTextMessage(phoneNumber,null,shortMessage,null,null);
                }
            }
        }
            else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS},
                        ConstantsManager.SEND_SMS_PERMISSION_CODE);
                Snackbar.make(containerForFragment, "Для корректной работы необходимо дать требуемые разрешения",
                        Snackbar.LENGTH_LONG).setAction("Разрешить", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openApplicationSettings();
                    }
                }).show();
            }
        }
    public void openApplicationSettings() {
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(appSettingsIntent, ConstantsManager.SEND_SMS_PERMISSION_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ConstantsManager.SEND_SMS_PERMISSION_CODE) {
            if (resultCode == RESULT_OK && data != null)  sendSms();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ConstantsManager.SEND_SMS_PERMISSION_CODE && grantResults.length == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                sendSms();
            }
        }
    }

    // формирование текста СМС со списком покупок
    private String makeTextSms() {
        String message = "";
        for (Buy buy : mBuyList) {
            if (!buy.getStatusBuy()) {
                message = message + buy.getItemBuy() + " " + buy.getQuantityBuy() + " " +
                                    buy.getUnitBuy() + "; ";
            }
        }
        return message;
    }

    // сохранение введенных данных о плане расходов
    private void savePlanCosts() {
        EditText forInputPlanCostsView = (EditText) findViewById(R.id.forInputSum);
        if (!forInputPlanCostsView.getText().toString().equals("")) {
            int planCostsSum = Integer.parseInt(forInputPlanCostsView.getText().toString());
            mDataBaseManager.updatePlanCostsInDb(mPeriodForActivity.getPeriodInitialDate(), planCostsSum);
            mBalance = mDataBaseManager.getCurrentBalance(mPeriodForActivity.getPeriodInitialDate());
            loadBalanceForActitvity();
        }
    }

    // удаление фрагмента
    public void removeFragment() {
        mFragmentTransaction = getFragmentManager().beginTransaction();
        mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        if (getFragmentManager().findFragmentById(R.id.container_for_fragment) !=null) {
        mFragmentTransaction.hide(getFragmentManager().findFragmentById(R.id.container_for_fragment));
        mFragmentTransaction.remove(getFragmentManager().findFragmentById(R.id.container_for_fragment)).commit();
        }
        if (getFragmentManager().findFragmentById(R.id.container_for_plan_fragment) != null) {
            mFragmentTransaction.hide(getFragmentManager().findFragmentById(R.id.container_for_plan_fragment));
            mFragmentTransaction.remove(getFragmentManager().findFragmentById(R.id.container_for_plan_fragment)).commit();
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    // загрузка фрагмента
    private void showFragment(int keyFragment) {

        mFragmentTransaction = getFragmentManager().beginTransaction();
        mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        switch (keyFragment) {
            case ConstantsManager.KEY_FRAGMENT_TO_PLAN: {
                ToPlanCostsFragment fragment = new ToPlanCostsFragment();
                mFragmentTransaction.add(R.id.container_for_plan_fragment, fragment).commit();
                break;
            }
            case ConstantsManager.KEY_FRAGMENT_DETAIL_COSTS: {
                ShowCostsDetailsFragment fragment = new ShowCostsDetailsFragment();
                setCostsDetails(fragment);
                mFragmentTransaction.add(R.id.container_for_plan_fragment, fragment).commit();
                break;
            }
            case ConstantsManager.KEY_FRAGMENT_TO_ADD_BUY: {
                ToAddBuyFragment fragment = new ToAddBuyFragment();
                fragment.setPeriodInitialDate(mPeriodForActivity.getPeriodInitialDate(), this);
                mFragmentTransaction.add(R.id.container_for_fragment, fragment).commit();
                break;
            }
            case ConstantsManager.KEY_FRAGMENT_TO_BUY_CARD: {
                BuyCardFragment fragment = new BuyCardFragment();
                fragment.setBuy(mBuy, this);
                if (fragment != null) {
                    mFragmentTransaction.add(R.id.container_for_fragment, fragment).commit();
                }
                break;
            }
        }
    }

    // вывод на экран подробностей расходов за период
    private void setCostsDetails(ShowCostsDetailsFragment fragment) {
        List<Costs> costsList = mDataBaseManager.getCostsByPeriod(mPeriodForActivity.getPeriodInitialDate());
        fragment.setmPlanCosts(costsList.get(0).planCosts);
        fragment.setmFactCosts(costsList.get(0).factCosts);
        fragment.setmPlanBuyCosts(costsList.get(0).planBuysCosts);
    }

    public void showSnackbar(String message) {
        Snackbar.make(containerForFragment, message, Snackbar.LENGTH_LONG).show();
    }


    // удаление покупки из базы данных и из списка на экране, корректировка текущего баланса
    public void deleteBuy(Buy buy) {
        mDataBaseManager.deleteBuyFromDB(buy);
        mBuyList.remove(buy);
        mBalance = mBalance + buy.getAmountBuy();
        mBalanceView.setText(mBalance + " рублей");
    }

    // метож для управдения фрагментами
    private void manageFragment (int checkShowFragment, int keyManageFragment) {
        switch (keyManageFragment) {
            case ConstantsManager.KEY_FRAGMENT_TO_PLAN: {
                if (checkShowFragment == ConstantsManager.KEY_FRAGMENT_TO_PLAN) {
                    savePlanCosts();
                    mCheckShowFragment = ConstantsManager.KEY_NO_FRAGMENT;
                    mToPlanCostsView.setImageResource(R.drawable.ic_edit_white_24dp);
                }
                if (checkShowFragment != ConstantsManager.KEY_NO_FRAGMENT) {
                    removeFragment();
                }
                if (checkShowFragment != ConstantsManager.KEY_FRAGMENT_TO_PLAN) {
                    showFragment(ConstantsManager.KEY_FRAGMENT_TO_PLAN);
                    mToPlanCostsView.setImageResource(R.drawable.ic_done_white_24dp);
                    mAddBuyView.setImageResource(R.drawable.ic_add_circle_red_24dp);
                    mCheckShowFragment = ConstantsManager.KEY_FRAGMENT_TO_PLAN;
                }
                break;
            }
            case ConstantsManager.KEY_FRAGMENT_DETAIL_COSTS: {
                if (checkShowFragment == ConstantsManager.KEY_FRAGMENT_DETAIL_COSTS) {
                    mToShowCostsDetailsView.setImageResource(R.drawable.ic_visibility_white_24dp);
                    mCheckShowFragment = ConstantsManager.KEY_NO_FRAGMENT;
                }
                if (checkShowFragment != ConstantsManager.KEY_NO_FRAGMENT) {
                    removeFragment();
                }
                if (checkShowFragment != ConstantsManager.KEY_FRAGMENT_DETAIL_COSTS) {
                    showFragment(ConstantsManager.KEY_FRAGMENT_DETAIL_COSTS);
                    mToShowCostsDetailsView.setImageResource(R.drawable.ic_clear_white_24dp);
                    mAddBuyView.setImageResource(R.drawable.ic_add_circle_red_24dp);
                    mCheckShowFragment = ConstantsManager.KEY_FRAGMENT_DETAIL_COSTS;
                }
                break;
            }
            // тут обрабатывается нажатие на основную кнопку, если не открыт фрагмент редактирования покупки
            case ConstantsManager.KEY_FRAGMENT_TO_ADD_BUY: {
                if (checkShowFragment == ConstantsManager.KEY_FRAGMENT_TO_ADD_BUY) {
                    mAddBuyView.setImageResource(R.drawable.ic_add_circle_red_24dp);
                    mCheckShowFragment = ConstantsManager.KEY_NO_FRAGMENT;
                    mBuyList = getBuyList();
                    loadBuyList(mBuyList);
                    loadBalanceForActitvity();
                }
                if (checkShowFragment != ConstantsManager.KEY_NO_FRAGMENT) {
                    removeFragment();
                }
                if (checkShowFragment != ConstantsManager.KEY_FRAGMENT_TO_ADD_BUY) {
                    showFragment(ConstantsManager.KEY_FRAGMENT_TO_ADD_BUY);
                    mAddBuyView.setImageResource(R.drawable.ic_check_circle_red_24dp);
                    mCheckShowFragment = ConstantsManager.KEY_FRAGMENT_TO_ADD_BUY;
                }
                break;
            }
            //  здесь обрабатывается нажатие на основную кнопку, если открыт фрагмент редактирования покупки
            // или нажатие на покупку для редактирования
            case ConstantsManager.KEY_FRAGMENT_TO_BUY_CARD: {
                if (checkShowFragment == ConstantsManager.KEY_FRAGMENT_TO_BUY_CARD) {
                    mAddBuyView.setImageResource(R.drawable.ic_add_circle_red_24dp);
                    mCheckShowFragment = ConstantsManager.KEY_NO_FRAGMENT;
                    mBuyList = getBuyList();
                    loadBuyList(mBuyList);
                    loadBalanceForActitvity();
                }
                if (checkShowFragment != ConstantsManager.KEY_NO_FRAGMENT) {
                    removeFragment();
                }
                if (checkShowFragment != ConstantsManager.KEY_FRAGMENT_TO_BUY_CARD) {
                    showFragment(ConstantsManager.KEY_FRAGMENT_TO_BUY_CARD);
                    mAddBuyView.setImageResource(R.drawable.ic_check_circle_red_24dp);
                    mCheckShowFragment = ConstantsManager.KEY_FRAGMENT_TO_BUY_CARD;
                }
                break;
            }
        }
    }
}




























