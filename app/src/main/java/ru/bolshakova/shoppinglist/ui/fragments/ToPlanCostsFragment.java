package ru.bolshakova.shoppinglist.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import ru.bolshakova.shoppinglist.R;


public class ToPlanCostsFragment extends Fragment {
    InputMethodManager imm;
    View view;


    public ToPlanCostsFragment() {
            }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_to_plan_costs,  container, false);
        imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
