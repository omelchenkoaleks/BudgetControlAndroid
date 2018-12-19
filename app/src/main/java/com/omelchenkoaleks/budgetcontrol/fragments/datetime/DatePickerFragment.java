package com.omelchenkoaleks.budgetcontrol.fragments.datetime;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.omelchenkoaleks.budgetcontrol.utils.AppContext;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {

    private static Calendar calendar;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        calendar = (Calendar) getArguments().getSerializable(AppContext.DATE_CALENDAR);

        if (calendar == null){
            calendar = Calendar.getInstance();
        }

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener)getActivity(), year, month, day);

    }


    public void show(FragmentManager fragmentManager, String dateCalendar) {
    }
}

