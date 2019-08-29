package com.example.guitarautoscroll;

import java.util.Date;

public class Tab {

    private Date date;
    private String title;
    private String path;

    public Tab(String title, Date date,String path){
        this.title=title;
        this.date=date;
        this.path=path;
    }

    public Date getDate() {return date;}

    public void setDate(Date date){this.date = date;}

    public String getTitle(){return title;}

    public void setTitle(String title){this.title=title;}

    public String getPath(){return path;}

    public void setPath(String path){this.path=path;}
}
