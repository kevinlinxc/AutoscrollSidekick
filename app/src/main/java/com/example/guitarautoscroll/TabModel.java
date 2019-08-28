package com.example.guitarautoscroll;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

public class TabModel implements Comparable<TabModel>, Serializable {

    public Date date = null;
    public String title = null;


    public Date getDate() {return date;}

    public void setDate(Date date){this.date = date;}

    public String getTitle(){return title;}

    public void setTitle(String title){this.title=title;}

    //constructor
    public TabModel(Date date, String title){
        this.date=date;
        this.title=title;
    }

    // Comparable

    @Override
    public int compareTo(@NonNull TabModel tabModel) {
        return this.title.compareTo(tabModel.title);
    }
}

