package ru.bolshakova.shoppinglist.data.dataBase;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import ru.bolshakova.shoppinglist.data.models.Buy;
import ru.bolshakova.shoppinglist.data.models.Costs;
import ru.bolshakova.shoppinglist.data.models.Period;

public class DataBaseManager {

    private static Dao<Period,Integer> periodDao;
    private static Dao<Costs, Integer> costsDao;
    private static Dao<Buy, Integer> buyDao;

    private static DataBaseManager instance = null;
    private DataBaseHelper helper;
    private List<Period> mPeriodList;

    public static DataBaseManager getInstance() {
        if (instance == null)
            instance = new DataBaseManager();
        return instance;
    }

    public void init(Context context) {
        if (helper == null)
            helper = OpenHelperManager.getHelper(context, ru.bolshakova.shoppinglist.data.dataBase.DataBaseHelper.class);
            }
    public void release () {
        if (helper != null) OpenHelperManager.releaseHelper();
    }

    private DataBaseManager() {}

    public DataBaseHelper getHelper() {
        return helper;
    }

    // метод возвращает период из БД, если он существует или вызывает метод по созданию новых периодов
    public Period getCurrentPeriod(Date date) {
        periodDao = DataBaseManager.getInstance().getHelper().getPeriodDao();
        costsDao = DataBaseManager.getInstance().getHelper().getCostsDao();
        Long time = date.getTime();
        Period periodForActivity = new Period();
        mPeriodList = this.getAllPeriodsFromDb();
        int dbSize = mPeriodList.size();

        // если заданная дата раньше начала первого периода в базе, возвращаем первый период в базе
        if (time < mPeriodList.get(0).getPeriodInitialDate())
            periodForActivity = mPeriodList.get(0);

        // если заданная дата позже конца последнего периода в базе, создаем новые периоды
        if (time > mPeriodList.get(dbSize-1).getPeriodFinalDate()) {
            periodForActivity = mPeriodList.get(dbSize - 1);
            while (!checkPeriod(periodForActivity, time)) {
                periodForActivity = this.addNewPeriodToDb(periodForActivity.getPeriodFinalDate());
            }
        }
         // если заданная дата содержится в одном из периодов базы, ищем нужный период
        if (time > mPeriodList.get(0).getPeriodInitialDate() && time < mPeriodList.get(dbSize-1).getPeriodFinalDate()) {
            for (int i = 0; i<dbSize; i++) {
                boolean check = false;
                int j = dbSize-1-i;
                if (time > mPeriodList.get(j).periodInitialDate && time < mPeriodList.get(j).periodFinalDate) {
                    periodForActivity = mPeriodList.get(j);
                    check = true;
                    }
                if (check) break;
            }
        }
        return periodForActivity;
    }

    private Period addNewPeriodToDb(Long date) {
            Period period = new Period();
            period.setPeriodInitialDate(date);
            period.setPeriodFinalDate(date + 604800000);
            this.createPeriodInDb(period);
        return period;
        }

    // метож проверяет, попадает ли заданная дата в период
    private boolean checkPeriod (Period period, Long time) {
        boolean checkPeriod = false;
        if (time > period.getPeriodInitialDate() &&
                time < period.getPeriodFinalDate()) {
            checkPeriod = true;
        }
        return checkPeriod;
    }

    // получение всех периодов из базы данных
    private List<Period> getAllPeriodsFromDb() {
        periodDao = DataBaseManager.getInstance().getHelper().getPeriodDao();
        List<Period> periodList = new ArrayList<>();
        try {
            periodList = periodDao.queryForAll();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return periodList;
    }
    // метод проверяет, пустая ли база данных. Вызывается при первом запуске приложения
    public boolean checkEmptyDb() {
        boolean checkEmptyDb = false;
        if (this.getAllPeriodsFromDb().size()==0) checkEmptyDb = true;
        return checkEmptyDb;
    }
    // метод создает первый период в базе данных. Вызывается один раз при первом запуске приложения
    public Period createFirstPeriodInDb() {
        periodDao = DataBaseManager.getInstance().getHelper().getPeriodDao();
        costsDao = DataBaseManager.getInstance().getHelper().getCostsDao();
        GregorianCalendar date = new GregorianCalendar();
        date = Period.cleanDate(date);
        GregorianCalendar date1 = new GregorianCalendar(date.get(Calendar.YEAR), date.get(Calendar.MONTH),
                                                        date.get(Calendar.DAY_OF_MONTH));
        date1.add(Calendar.DAY_OF_YEAR, 7);
        Period period = new Period(date.getTimeInMillis(), date1.getTimeInMillis());
        Costs costs = new Costs(period.getPeriodInitialDate());
        this.createOrUpdateCostsInDb(costs);
        period.setCosts(new ru.bolshakova.shoppinglist.data.models.Costs());
        this.createPeriodInDb(period);

        return period;
    }
    // записывает период в базу данных
    private void createPeriodInDb(Period period) {
            Costs costs = new Costs(period.getPeriodInitialDate());
            period.setCosts(costs);
            this.createOrUpdateCostsInDb(costs);
        try {
            periodDao.create(period);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createOrUpdateCostsInDb(Costs costs) {
        try {
            costsDao.createOrUpdate(costs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // обновдение данных о пдановых расходах в базе данных
    public void updatePlanCostsInDb(Long periodInitialDate, int planCostsSum)  {
        List<Costs> costsList = getCostsByPeriod(periodInitialDate);
        Costs costs = costsList.get(0);
        costs.setPlanCosts(planCostsSum);
        createOrUpdateCostsInDb(costs);

        }

    // получение данных о расходах за период из базы данных
    public List<Costs> getCostsByPeriod(Long periodInitialDate)  {
        List <Costs> costsList = new ArrayList<>();
        try {
            costsDao = DataBaseManager.getInstance().getHelper().getCostsDao();
            QueryBuilder<Costs, Integer> queryBuilder = costsDao.queryBuilder();
            queryBuilder.where().eq(Costs.INITIAL_DATE_COSTS, periodInitialDate);
            PreparedQuery<Costs> preparedQuery = queryBuilder.prepare();
            costsList = costsDao.query(preparedQuery);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return costsList;
    }

    // получение текущего баланса за период из базы данных
    public int getCurrentBalance(Long periodInitialDate) {
        List<Costs> costsList = getCostsByPeriod(periodInitialDate);
        Costs costs = costsList.get(0);
        int balance = costs.planCosts - costs.factCosts - costs.planBuysCosts;
        return balance;
    }

    // создание новой покупки в базе данных
    public void createBuyInDb(Buy buy) {
        buyDao = DataBaseManager.getInstance().getHelper().getBuyDao();
        try {
            buyDao.create(buy);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        updateFactOrPlanBuyCosts(buy.getInitialDate());
    }

    // получение списка покупок за период из базы данных
    public List<Buy> getBuyList(Long periodInitialDate) {
        List<Buy> buyList = new ArrayList<>();
        try {
            buyDao = DataBaseManager.getInstance().getHelper().getBuyDao();
            QueryBuilder<Buy, Integer> queryBuilder = buyDao.queryBuilder();
            queryBuilder.where().eq(Buy.INITIAL_DATE_BUY, periodInitialDate);
            PreparedQuery<Buy> preparedQuery = queryBuilder.prepare();
            buyList = buyDao.query(preparedQuery);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return buyList;
    }

    // получение списка плановых покупок за период из базы данных
    public List<Buy> getPlanBuyList(Long periodInitialDate) {
        List<Buy> planBuyList = new ArrayList<>();
        try {
            buyDao = DataBaseManager.getInstance().getHelper().getBuyDao();
            QueryBuilder<Buy, Integer> queryBuilder = buyDao.queryBuilder();
            Where<Buy, Integer> where = queryBuilder.where();
            where.and(
                    where.eq(Buy.INITIAL_DATE_BUY, periodInitialDate),
                    where.eq(Buy.STATUS_BUY, false));
            PreparedQuery<Buy> preparedQuery = queryBuilder.prepare();
            planBuyList = buyDao.query(preparedQuery);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return planBuyList;
    }

    // удаление покупки в базе данных
    public void deleteBuyFromDB(Buy buy) {
        Integer buyId = buy.getId();
        buyDao = DataBaseManager.getInstance().getHelper().getBuyDao();
        try {
            Buy buyForDelete = buyDao.queryForId(buyId);
            buyDao.delete(buyForDelete);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        updateFactOrPlanBuyCosts(buy.getInitialDate());
    }

    // изменение данных о покупке в базе данных
    public void updateBuyInDb(Buy buy) {
        buyDao = DataBaseManager.getInstance().getHelper().getBuyDao();
        try {
            buyDao.createOrUpdate(buy);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        updateFactOrPlanBuyCosts(buy.getInitialDate());

    }

    // обновдение данных о фактических и плановых расходах в базе данных
    public void updateFactOrPlanBuyCosts(Long periodInitialDate) {
        List <Buy> buyList = getBuyList(periodInitialDate);
        Costs costs = new Costs();
        if (getCostsByPeriod(periodInitialDate).size() != 0) {
            costs = getCostsByPeriod(periodInitialDate).get(0);
        }
        int factCosts = 0;
        int planBuyCosts = 0;
        for (int i = 0; i < buyList.size(); i++) {
            if (buyList.get(i).getStatusBuy()) {
                factCosts = factCosts + buyList.get(i).getAmountBuy();
            }
            else {
                planBuyCosts = planBuyCosts + buyList.get(i).getAmountBuy();
            }
        }
        costs.setFactCosts(factCosts);
        costs.setPlanBuysCosts(planBuyCosts);
        createOrUpdateCostsInDb(costs);
    }

    // перенос покупки на следующий период
    public void moveBuyToNextPeriodInDb (Buy buy) {
        Period period = new Period();
        Date date = new Date(period.calculatePeriodFinalDate(buy.getInitialDate()+1));
        period = getCurrentPeriod(date);
        Long newInitialDate = period.calculatePeriodFinalDate(buy.getInitialDate());
        buy.setInitialDate(newInitialDate);
        updateBuyInDb(buy);
        updateFactOrPlanBuyCosts(newInitialDate);
        updateFactOrPlanBuyCosts(period.calculatePeriodInintialDate(newInitialDate));
    }
}
