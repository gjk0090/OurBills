package com.fff.gjk.ourbills.bean;

/**
 * Created by gjk on 3/6/14.
 */
public class Bill {
    public int bid = -1;
    public int gid = -1;
    public String title;
    public double amount = 0;
    public int isSimple = -1;
    public int bcid = -1;
    public String date;
    public String pic;

    public Bill(){

    }

    public Bill(int gid, String title, double amount, int isSimple, int bcid, String date){
        this.gid = gid;
        this.title = title;
        this.amount = amount;
        this.isSimple = isSimple;
        this.bcid = bcid;
        this.date = date;
    }


}
//(bid INTEGER , gid INTEGER NOT NULL, title VARCHAR NOT NULL, amount DOUBLE NOT NULL, isSimple INTEGER NOT NULL, bcid INTEGER, date VARCHAR NOT NULL, pic VARCHAR);
