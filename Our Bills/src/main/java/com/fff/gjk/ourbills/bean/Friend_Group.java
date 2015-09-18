package com.fff.gjk.ourbills.bean;

/**
 * Created by gjk on 3/6/14.
 */
public class Friend_Group {

    public int fid = -1;
    public int gid = -1;
    public double balance = 0;

    public Friend_Group() {
    }

    public Friend_Group(int fid, int gid, double balance) {
        this.fid = fid;
        this.gid = gid;
        this.balance = balance;
    }

}
