package com.example.adaiboad.realtravelapp.Model;

import android.widget.AutoCompleteTextView;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by Deborah on 14/02/2018.
 */

public class Holiday implements Serializable{
    private String title;
    private String notes;
    private Calendar startDate;
    private Calendar endDate;
    private String location;


    public Holiday( String title, String notes) {
        this.title = title;
        this.notes = notes;
        this.location = location;
        startDate = Calendar.getInstance();
        endDate = Calendar.getInstance();
    }
    // get methods and set methods for each field



    public void setTitle(String title) {
        this.title = title;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setLocation(String location) {this.location = location;}

    public String getTitle() {
        return title;
    }

    public String getNotes() {
        return notes;
    }

    public String getLocation() {
        return location;
    }




    public Calendar getStartDate() {
        return startDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }


    public String formatStartDate(){
        Calendar cal = getStartDate();
        // You cannot use Date class to extract individual Date fields
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);      // 0 to 11
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return ""+day+"/"+(month+1)+"/"+year ;

    }

    public String formatEndDate(){
        Calendar cal = getEndDate();
        // You cannot use Date class to extract individual Date fields
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);      // 0 to 11
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return ""+day+"/"+(month+1)+"/"+year ;

    }




    public void setStartDate(int year, int month, int day) {
        startDate.set(year, month, day);
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }
    public void setEndDate(int year, int month, int day) {
        endDate.set(year, month, day);
    }
    public void setEndDate(Calendar endDate) {
        this.endDate=endDate;
    }





}
