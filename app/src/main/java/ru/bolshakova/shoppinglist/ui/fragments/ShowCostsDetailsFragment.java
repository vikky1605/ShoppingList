package ru.bolshakova.shoppinglist.ui.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.bolshakova.shoppinglist.R;
import ru.bolshakova.shoppinglist.utils.CustomTextView;


public class ShowCostsDetailsFragment extends Fragment {

    private int mPlanCosts;
    private int mFactCosts;
    private int mPlanBuyCosts;

     public ShowCostsDetailsFragment() {
            }

    public void setmPlanCosts(int mPlanCosts) {
        this.mPlanCosts = mPlanCosts;
    }

    public void setmFactCosts(int mFactCosts) {
        this.mFactCosts = mFactCosts;
    }

    public void setmPlanBuyCosts(int mPlanBuyCosts) {
        this.mPlanBuyCosts = mPlanBuyCosts;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_costs_details, container, false);
        CustomTextView planCostsView = (CustomTextView)view.findViewById(R.id.plan_costs);
        CustomTextView factCostsView = (CustomTextView)view.findViewById(R.id.fact_costs);
        CustomTextView planBuyCostsView = (CustomTextView)view.findViewById(R.id.plan_buy_costs);
        planCostsView.setText(mPlanCosts + " рублей");
        factCostsView.setText(mFactCosts + " рублей");
        planBuyCostsView.setText(mPlanBuyCosts + " рублей");

        return view;
    }
    }
