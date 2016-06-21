package ru.bolshakova.shoppinglist;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.Date;
import java.util.GregorianCalendar;

public class toPlanCostsActivity extends AppCompatActivity {

    static Period periodGui; // период, относительно которого выводится главное окно приложения
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_plan_costs);
    }

    public void onClickCancelButton(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickSaveButton(View view) {
        RadioButton nextWeekButton = (RadioButton)findViewById(R.id.nextPeriod);
        RadioButton thisWeekButton = (RadioButton) findViewById(R.id.thisPeriod);
        TextView titleText = (TextView) findViewById(R.id.title);
        EditText sumText = (EditText)findViewById(R.id.forInputSum);
        String sumString = sumText.getText().toString();
        Integer sum = Integer.parseInt(sumString);
        Date date = new Date();
        Period period = new Period(this);
        Date[] dates = period.getCurrentPeriod(date);

        if (!nextWeekButton.isChecked() && !thisWeekButton.isChecked()) {titleText.setText("Выберите неделю");}
        else {
            if (nextWeekButton.isChecked()) {
                date = period.getPeriodFinalDate(date);
                dates = period.getCurrentPeriod(date);
                System.out.println("получила следующий период");
            }

            Costs costs = new Costs(this);
            costs.addPlanCostsToDB(sum, date);
            System.out.println("добавила плановые расходы в базу данных");

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
                    }

    }
}
