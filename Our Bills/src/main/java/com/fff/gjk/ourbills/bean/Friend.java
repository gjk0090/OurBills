package com.fff.gjk.ourbills.bean;

/**
 * Created by gjk on 3/4/14.
 */
public class Friend {

    public int fid = -1;
    public String fname;
    public String gender;
    public String email;
    public String pic;

    public Friend(){

    }

    public Friend(String fname, String gender, String email) {
        this.fname = fname;
        this.gender = gender;
        this.email = email;
    }

    @Override
    public boolean equals(Object o){
        Friend f = (Friend) o;
        return fname.equals(f.fname) && gender.equals(f.gender) && email.equals(f.email);
    }

}
