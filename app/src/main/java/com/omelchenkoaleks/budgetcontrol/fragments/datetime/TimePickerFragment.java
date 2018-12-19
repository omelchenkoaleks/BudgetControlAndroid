package com.omelchenkoaleks.budgetcontrol.fragments.datetime;

import android.app.Dialog;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.omelchenkoaleks.budgetcontrol.utils.AppContext;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment  {

    private static Calendar calendar;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        calendar = (Calendar) getArguments().getSerializable(AppContext.DATE_CALENDAR);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), (TimePickerDialog.OnTimeSetListener)getActivity(), hour, minute, true);
    }


    public void show(FragmentManager fragmentManager, String dateCalendar) {
    }
}
