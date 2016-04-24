package org.vzw.beta.homestation.beans;

import java.util.Calendar;

/**
 * Created by user109 on 23/03/2016.
 */
public class Electricity{

    public  String uniqueID;
    private long value;
    private String date;


    public Electricity(long value, String date) {

        uniqueID = Calendar.getInstance().getTimeInMillis()+"";
        this.value = value;
        this.date = date;

    }
    public Electricity(long value, String date, String uniqueID) {
        this.uniqueID = uniqueID;
        this.value = value;
        this.date = date;

    }
    public Electricity(long value, int day, int month, int year) {
        uniqueID = Calendar.getInstance().getTimeInMillis()+"";
        this.value = value;
        this.date = day+"/"+month+"/"+year;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public String getUniqueID() {
        return uniqueID;
    }

    @Override
    public String toString() {
        return "Id:" +getUniqueID()+"  Date  "+ getDate() + "  Value:  "+getValue();
    }
}
