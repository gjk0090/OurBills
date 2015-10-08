package com.fff.gjk.ourbills.bean;

/**
 * Created by gjk on 3/3/14.
 */
public class Group {

    public int gid = -1;
    public String gname;
    public int gcid;
    public String pic;

    public Group() {
    }

    public Group(int gid, String gname, int gcid) {
        this.gid = gid;
        this.gname = gname;
        this.gcid = gcid;
    }

    public Group(String gname, int gcid) {
        this.gname = gname;
        this.gcid = gcid;
    }
}
