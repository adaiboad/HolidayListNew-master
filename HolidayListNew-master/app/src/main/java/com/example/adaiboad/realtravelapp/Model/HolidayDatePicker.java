package com.example.adaiboad.realtravelapp.Model;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by adaiboad on 23/02/18.
 */

public class HolidayDatePicker extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {


    private Calendar startDate;
    private Calendar endDate;
    private Button dateButton;
    private boolean isStartDate;


    public void setHoliday(Calendar startDate, Calendar endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }


    public void setDateButton(Button dateButton) {
        this.dateButton = dateButton;
    }

    public void setStartDate(boolean startDate) {
        isStartDate = startDate;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user

        if(isStartDate) {
            startDate.set(year, month, day);
            dateButton.setText(""+day+"/"+(month+1)+"/"+year );


        }
        else
        {
            endDate.set(year, month, day);
            dateButton.setText(""+day+"/"+(month+1)+"/"+year );
        }
    }


}
