package com.fff.gjk.ourbills.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fff.gjk.ourbills.bean.Bill;
import com.fff.gjk.ourbills.bean.Friend;
import com.fff.gjk.ourbills.bean.Friend_Bill;
import com.fff.gjk.ourbills.bean.Friend_Group;
import com.fff.gjk.ourbills.bean.Group;

/**
 * Created by gjk on 3/3/14.
 */
public class DBManager {

    private DBHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context) {
        helper = new DBHelper(context);
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }









    //INSERT

    public void addGroups(List<Group> groups) {
        db.beginTransaction();  //开始事务
        try {
            for (Group group : groups) {
                db.execSQL("INSERT INTO groups VALUES(null, ?, ?, ?)", new Object[]{group.gname, group.gcid, "null"});
            }
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    public int addGroup(Group group) {
        db.beginTransaction();  //开始事务
        int gid = -1;
        try {
            db.execSQL("INSERT INTO groups VALUES(null, ?, ?, ?)", new Object[]{group.gname, group.gcid, "null"});

            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
        Cursor c = db.rawQuery("SELECT gid FROM groups WHERE gname = '" + group.gname + "'", null);
        c.moveToNext();
        gid = c.getInt(c.getColumnIndex("gid"));
        c.close();
        return gid;
    }

    public void addFriend(Friend friend) {
        db.beginTransaction();  //开始事务
        try {
            db.execSQL("INSERT INTO friends VALUES(null, ?, ?, ?, ?)", new Object[]{friend.fname, friend.gender, friend.email, "null"});
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    public void addFriendToGroup(int friendId, int groupId){
        db.beginTransaction();  //开始事务
        try {
            db.execSQL("INSERT INTO friend_group VALUES(?, ?, 0)", new Object[]{friendId, groupId});
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    public void addBill(Bill bill){
        db.beginTransaction();  //开始事务
        try {
            db.execSQL("INSERT INTO bills VALUES(null, ?, ?, ?, ?, ?, ?, ?)", new Object[]{bill.gid, bill.title, bill.amount, bill.isSimple, bill.bcid, bill.date, "null"});
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }


    public void addFriendToBill(int fid, int bid, double pay, double owe) {
        db.beginTransaction();  //开始事务
        try {
            db.execSQL("INSERT INTO friend_bill VALUES(?, ?, ?, ?)", new Object[]{fid,bid,pay,owe});
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }



















    //SELECT

    public List<Group> getAllGroups() {
        ArrayList<Group> groups = new ArrayList<Group>();
        Cursor c = db.rawQuery("SELECT * FROM groups", null);
        while (c.moveToNext()) {
            Group group = new Group();
            group.gid = c.getInt(c.getColumnIndex("gid"));
            group.gname = c.getString(c.getColumnIndex("gname"));
            group.gcid = c.getInt(c.getColumnIndex("gcid"));
            group.pic = c.getString(c.getColumnIndex("pic"));
            groups.add(group);
        }
        c.close();
        return groups;
    }


    public List<Friend> getAllFriends() {
        ArrayList<Friend> friends = new ArrayList<Friend>();
        Cursor c = db.rawQuery("SELECT * FROM friends", null);
        while (c.moveToNext()) {
            Friend friend = new Friend();
            friend.fid = c.getInt(c.getColumnIndex("fid"));
            friend.fname = c.getString(c.getColumnIndex("fname"));
            friend.gender = c.getString(c.getColumnIndex("gender"));
            friend.email = c.getString(c.getColumnIndex("email"));
            friend.pic = c.getString(c.getColumnIndex("pic"));
            friends.add(friend);
        }
        c.close();
        return friends;
    }

    public List<String> getAllGroupCategory() {

        ArrayList<String> list = new ArrayList<String>();
        Cursor c = db.rawQuery("SELECT * FROM gcategory", null);
        while (c.moveToNext()) {
            list.add(c.getString(c.getColumnIndex("gcname")));
        }
        c.close();
        return list;
    }

    public List<String> getAllBillCategory(){
        ArrayList<String> list = new ArrayList<String>();
        Cursor c = db.rawQuery("SELECT * FROM bcategory", null);
        while (c.moveToNext()) {
            list.add(c.getString(c.getColumnIndex("bcname")));
        }
        c.close();
        return list;
    }

    public String getGroupCategoryNameById(int gcid){
        String gcname="";
        Cursor c = db.rawQuery("SELECT gcname FROM gcategory WHERE gcid = "+gcid,null);
        while (c.moveToNext()) {
            gcname = c.getString(c.getColumnIndex("gcname"));
        }
        c.close();
        return gcname;
    }

    public Friend getFriendById(int fid){
        Friend friend = new Friend();

        Cursor c = db.rawQuery("SELECT * FROM friends WHERE fid = "+fid,null);
        while (c.moveToNext()) {
            friend.fid = c.getInt(c.getColumnIndex("fid"));
            friend.fname = c.getString(c.getColumnIndex("fname"));
            friend.gender = c.getString(c.getColumnIndex("gender"));
            friend.email = c.getString(c.getColumnIndex("email"));
            friend.pic = c.getString(c.getColumnIndex("pic"));
        }
        c.close();
        return friend;
    }


    public List<Friend_Group> getFriend_GroupByGroupId(int gid){

        ArrayList<Friend_Group> fgs = new ArrayList<Friend_Group>();

        Cursor c = db.rawQuery("SELECT * FROM friend_group WHERE gid = "+gid,null);
        while (c.moveToNext()) {
            Friend_Group fg = new Friend_Group();
            fg.fid = c.getInt(c.getColumnIndex("fid"));
            fg.gid = c.getInt(c.getColumnIndex("gid"));
            fg.balance = c.getDouble(c.getColumnIndex("balance"));

            fgs.add(fg);
        }
        c.close();
        return fgs;
    }

    public List<Friend_Bill> getFriend_BillByBillId(int bid){

        ArrayList<Friend_Bill> fbs = new ArrayList<Friend_Bill>();

        Cursor c = db.rawQuery("SELECT * FROM friend_bill WHERE bid = "+bid,null);
        while (c.moveToNext()) {
            Friend_Bill fb = new Friend_Bill();
            fb.fid = c.getInt(c.getColumnIndex("fid"));
            fb.bid = c.getInt(c.getColumnIndex("bid"));
            fb.pay = c.getDouble(c.getColumnIndex("pay"));
            fb.owe = c.getDouble(c.getColumnIndex("owe"));

            fbs.add(fb);
        }
        c.close();
        return fbs;
    }

    public List<Friend_Bill> getFriend_BillByFid(int fid){

        ArrayList<Friend_Bill> fbs = new ArrayList<Friend_Bill>();

        Cursor c = db.rawQuery("SELECT * FROM friend_bill WHERE fid = "+fid,null);
        while (c.moveToNext()) {
            Friend_Bill fb = new Friend_Bill();
            fb.fid = c.getInt(c.getColumnIndex("fid"));
            fb.bid = c.getInt(c.getColumnIndex("bid"));
            fb.pay = c.getDouble(c.getColumnIndex("pay"));
            fb.owe = c.getDouble(c.getColumnIndex("owe"));

            fbs.add(fb);
        }
        c.close();
        return fbs;
    }

    public Group getGroupById(int gid){
        Group group = new Group();

        Cursor c = db.rawQuery("SELECT * FROM groups WHERE gid = "+gid,null);
        while (c.moveToNext()) {
            group.gid = c.getInt(c.getColumnIndex("gid"));
            group.gname = c.getString(c.getColumnIndex("gname"));
            group.gcid = c.getInt(c.getColumnIndex("gcid"));
            group.pic = c.getString(c.getColumnIndex("pic"));
        }
        c.close();
        return group;
    }

    public List<Friend_Group> getFriend_GroupByFriendId(int fid){

        ArrayList<Friend_Group> fgs = new ArrayList<Friend_Group>();

        Cursor c = db.rawQuery("SELECT * FROM friend_group WHERE fid = "+fid,null);
        while (c.moveToNext()) {
            Friend_Group fg = new Friend_Group();
            fg.fid = c.getInt(c.getColumnIndex("fid"));
            fg.gid = c.getInt(c.getColumnIndex("gid"));
            fg.balance = c.getDouble(c.getColumnIndex("balance"));

            fgs.add(fg);
        }
        c.close();
        return fgs;
    }

    public List<Bill> getBillsByGroupId(int gid){

        ArrayList<Bill> bills = new ArrayList<Bill>();

        Cursor c = db.rawQuery("SELECT * FROM bills WHERE gid = "+gid,null);
        while (c.moveToNext()) {
            Bill bill = new Bill();
            bill.bid = c.getInt(c.getColumnIndex("bid"));
            bill.gid = c.getInt(c.getColumnIndex("gid"));
            bill.title = c.getString(c.getColumnIndex("title"));
            bill.amount = c.getDouble(c.getColumnIndex("amount"));
            bill.isSimple = c.getInt(c.getColumnIndex("isSimple"));
            bill.bcid = c.getInt(c.getColumnIndex("bcid"));
            bill.date = c.getString(c.getColumnIndex("date"));
            bill.pic = c.getString(c.getColumnIndex("pic"));

            bills.add(bill);
        }
        c.close();
        return bills;
    }

    public int getLastBillId(){
        int bid=-1;
        Cursor c = db.rawQuery("SELECT MAX(bid) AS bid FROM bills",null);
        while (c.moveToNext()) {
            bid = c.getInt(c.getColumnIndex("bid"));
        }
        c.close();
        return bid;
    }

    public double getBalance(Friend_Group fg){
        double balance = 0;
        Cursor c = db.rawQuery("SELECT * FROM friend_group WHERE gid = "+fg.gid+" AND fid = "+ fg.fid,null);
        while (c.moveToNext()){
            balance = c. getDouble(c.getColumnIndex("balance"));
        }
        c.close();
        return balance;
    }

    public double getTotalBalanceByFid(int fid){
        double balance = 0;
        Cursor c = db.rawQuery("SELECT SUM(balance) AS sum FROM friend_group WHERE fid = "+ fid,null);
        while (c.moveToNext()){
            balance = c. getDouble(c.getColumnIndex("sum"));
        }
        c.close();
        return balance;
    }

    public Bill getBillById(int bid){
        Bill bill = new Bill();
        Cursor c = db.rawQuery("SELECT * FROM bills WHERE bid = "+bid,null);
        while (c.moveToNext()) {
            bill.bid = c.getInt(c.getColumnIndex("bid"));
            bill.gid = c.getInt(c.getColumnIndex("gid"));
            bill.title = c.getString(c.getColumnIndex("title"));
            bill.amount = c.getDouble(c.getColumnIndex("amount"));
            bill.isSimple = c.getInt(c.getColumnIndex("isSimple"));
            bill.bcid = c.getInt(c.getColumnIndex("bcid"));
            bill.date = c.getString(c.getColumnIndex("date"));
            bill.pic = c.getString(c.getColumnIndex("pic"));
        }
        c.close();
        return bill;
    }

    public int getGidByGname(String gname){
        int gid = -1;
        Cursor c = db.rawQuery("SELECT * FROM groups WHERE gname = '"+gname+"'",null);
        while (c.moveToNext()) {
            gid = c.getInt(c.getColumnIndex("gid"));
        }
        c.close();
        return gid;
    }

    public int getBcidByBcname(String bcname){
        int bcid = -1;
        Cursor c = db.rawQuery("SELECT * FROM bcategory WHERE bcname = '"+bcname+"'",null);
        while (c.moveToNext()) {
            bcid = c.getInt(c.getColumnIndex("bcid"));
        }
        c.close();
        return bcid;
    }

    public int getGcidByGcname(String gcname){
        int gcid = -1;
        Cursor c = db.rawQuery("SELECT * FROM gcategory WHERE gcname = '"+gcname+"'",null);
        while (c.moveToNext()) {
            gcid = c.getInt(c.getColumnIndex("gcid"));
        }
        c.close();
        return gcid;
    }

    public int getFidByFname(String fname){
        int fid = -1;
        Cursor c = db.rawQuery("SELECT * FROM friends WHERE fname = '"+fname+"'",null);
        while (c.moveToNext()) {
            fid = c.getInt(c.getColumnIndex("fid"));
        }
        c.close();
        return fid;
    }

    public int getFidByEmail(String email){
        int fid = -1;
        Cursor c = db.rawQuery("SELECT * FROM friends WHERE email = '"+email+"'",null);
        while (c.moveToNext()) {
            fid = c.getInt(c.getColumnIndex("fid"));
        }
        c.close();
        return fid;
    }













    //UPDATE

    public void updateBalance(Friend_Group fg) {

        ContentValues cv = new ContentValues();

        cv.put("balance", this.getBalance(fg) + fg.balance);

        String where = "fid="+fg.fid+" AND gid="+fg.gid;

        db.update("friend_group", cv, where, null);
    }

    public void editFriend(Friend f){
        ContentValues cv = new ContentValues();

        cv.put("fname", f.fname);
        cv.put("email", f.email);
        cv.put("gender", f.gender);

        String where = "fid="+f.fid;

        db.update("friends", cv, where, null);

    }

    public int editGroup(Group g){

        ContentValues cv = new ContentValues();

        cv.put("gname", g.gname);
        cv.put("gcid", g.gcid);

        String where = "gid="+g.gid;

        return db.update("groups", cv, where, null);
    }








    //DELETE
    public int deleteAllFriendInGroup(int gid, Collection<Integer> fids){
        db.beginTransaction();  //开始事务

        for(int fid : fids) {

            int i = db.delete("friend_group", "gid = ? AND fid = ?", new String[]{String.valueOf(gid), String.valueOf(fid)});
        }
        db.setTransactionSuccessful();  //设置事务成功完成
        db.endTransaction();    //结束事务

        return 0;
    }







    /**
     * close database
     */
    public void closeDB() {
        db.close();
    }

    public void cleanDB (){
        helper.getWritableDatabase().execSQL("DROP TABLE IF EXISTS "+"groups");
        helper.getWritableDatabase().execSQL("DROP TABLE IF EXISTS "+"friends");
        helper.getWritableDatabase().execSQL("DROP TABLE IF EXISTS "+"friend_group");
        helper.getWritableDatabase().execSQL("DROP TABLE IF EXISTS "+"friend_bill");
        helper.getWritableDatabase().execSQL("DROP TABLE IF EXISTS "+"bills");
        helper.getWritableDatabase().execSQL("DROP TABLE IF EXISTS "+"gcategory");
        helper.getWritableDatabase().execSQL("DROP TABLE IF EXISTS "+"bcategory");

        helper.onCreate(helper.getWritableDatabase());
    }

}
