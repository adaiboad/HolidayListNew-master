package com.example.adaiboad.realtravelapp.Adapters;

import com.example.adaiboad.realtravelapp.Model.Holiday;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by manny on 4/14/2018.
 */
public class HolidayData{

    private static List<Holiday> holidays = new ArrayList<Holiday>();
    public static  void addHoliday(Holiday holiday){
        holidays.add(holiday);

    }
    public static List<Holiday> getHolidays(){
        return holidays;
    }

    public static Holiday getHoliday(int index)
    {
        return holidays.get(index);
    }

    public static int getSize(){
        return holidays.size();
    }
}