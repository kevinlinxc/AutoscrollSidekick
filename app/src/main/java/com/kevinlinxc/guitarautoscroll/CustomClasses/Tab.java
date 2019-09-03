package com.kevinlinxc.guitarautoscroll.CustomClasses;

import java.io.Serializable;
import java.util.Date;

public class Tab implements Serializable {

    private Date date;
    private String title;
    private String path;
    private String directoryPath;
    private int pixelSpeed;

    public Tab(String title, Date date,String path, String directoryPath, int pixelSpeed){
        this.title=title;
        this.date=date;
        this.path=path;
        this.directoryPath=directoryPath;
        this.pixelSpeed = pixelSpeed;
    }

    public Date getDate() {return date;}

    public void setDate(Date date){this.date = date;}

    public String getTitle(){return title;}

    public void setTitle(String title){this.title=title;}

    public String getPath(){return path;}

    public void setPath(String path){this.path=path;}

    public String getDirectoryPath(){return directoryPath;}

    public void setDirectoryPath(String directoryPath){this.directoryPath=directoryPath;}

    public int getPixelSpeed(){return pixelSpeed;}

    public void setPixelSpeed(int pixelSpeed){this.pixelSpeed=pixelSpeed;}
}
