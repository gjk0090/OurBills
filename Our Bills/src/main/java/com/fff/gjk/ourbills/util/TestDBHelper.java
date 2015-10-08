package com.fff.gjk.ourbills.util;

import android.widget.Toast;

import com.fff.gjk.ourbills.activity.MainActivity;
import com.fff.gjk.ourbills.bean.Bill;
import com.fff.gjk.ourbills.bean.Friend;
import com.fff.gjk.ourbills.bean.Friend_Group;
import com.fff.gjk.ourbills.bean.Group;
import com.fff.gjk.ourbills.dao.DBManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by gjk on 10/2/15.
 */
public class TestDBHelper {

    private static DBManager mgr = MainActivity.mgr;

    public static void addDB() {

        mgr.addGroup(new Group("Miami Trip", 1));
        mgr.addGroup(new Group("PR Trip", 2));
        mgr.addGroup(new Group("Yellowstone Trip", 3));
        mgr.addGroup(new Group("heaven trip", 4));
        mgr.addGroup(new Group("hell trip", 1));

        mgr.addFriend(new Friend("gjk","male","gjk0090@126"));
        mgr.addFriend(new Friend("小红","female","gjk0090@163"));
        mgr.addFriend(new Friend("小明","male","gjk0090@qq"));
        mgr.addFriend(new Friend("小美","female","gjk0090@gmail"));
        mgr.addFriend(new Friend("小刚","male","gjk0090@pitt"));

        mgr.addFriendToGroup(1,1);
        mgr.addFriendToGroup(2,1);
        mgr.addFriendToGroup(3,1);
        mgr.addFriendToGroup(4,1);
        mgr.addFriendToGroup(5,1);

        mgr.addFriendToGroup(2,2);
        mgr.addFriendToGroup(3,2);
        mgr.addFriendToGroup(4,2);
        mgr.addFriendToGroup(3,3);
        mgr.addFriendToGroup(4,3);
        mgr.addFriendToGroup(4,4);
        mgr.addFriendToGroup(5,4);
        mgr.addFriendToGroup(5,5);
        mgr.addFriendToGroup(1,5);

        HashMap<Integer, Double> pay = new HashMap<Integer, Double>();
        pay.put(1,60.3);
        HashMap<Integer, Double> owe = new HashMap<Integer, Double>();
        owe.put(1,20.1);
        owe.put(2,20.1);
        owe.put(3,20.1);
        addBill(new Bill(1, "breakfast", 60.3, 1, 1, "2014/03/07"), pay, owe);

        pay = new HashMap<Integer, Double>();
        pay.put(2,30.3);
        owe = new HashMap<Integer, Double>();
        owe.put(1,10.1);
        owe.put(2,10.1);
        owe.put(3,10.1);
        addBill(new Bill(1, "lunch", 30.3, 1, 2, "2014/03/07"), pay, owe);

        pay = new HashMap<Integer, Double>();
        pay.put(3,20.3);
        owe = new HashMap<Integer, Double>();
        owe.put(1,10.2);
        owe.put(2,10.1);
        addBill(new Bill(1, "dinner", 20.3, 1, 3, "2014/03/07"), pay, owe);

        pay = new HashMap<Integer, Double>();
        pay.put(2,360.3);
        owe = new HashMap<Integer, Double>();
        owe.put(1,120.1);
        owe.put(2,120.1);
        owe.put(3,120.1);
        addBill(new Bill(1, "flight", 360.3, 1, 4, "2014/03/07"), pay, owe);

    }

    public static void addBill(Bill bill, HashMap<Integer, Double> pay, HashMap<Integer, Double> owe){

        mgr.addBill(bill);

        int bid = mgr.getLastBillId();

        ArrayList<Friend_Group> fgs = new ArrayList<Friend_Group>();

        Set paySet = pay.entrySet();
        for(Iterator iter = paySet.iterator(); iter.hasNext();)
        {
            Map.Entry entry = (Map.Entry)iter.next();

            int fid = Integer.parseInt((String)entry.getKey().toString());
            double payAmount = Double.valueOf((String) entry.getValue().toString());

            mgr.addFriendToBill(fid,bid,payAmount,0);

            Friend_Group fg = new Friend_Group(fid,bill.gid,payAmount);
            fgs.add(fg);
        }

        Set oweSet = owe.entrySet();
        for(Iterator iter = oweSet.iterator(); iter.hasNext();)
        {
            Map.Entry entry = (Map.Entry)iter.next();

            int fid = Integer.parseInt((String)entry.getKey().toString());
            double oweAmount = Double.valueOf((String)entry.getValue().toString());

            mgr.addFriendToBill(fid,bid,0,oweAmount);

            Friend_Group fg = new Friend_Group(fid,bill.gid,0-oweAmount);
            fgs.add(fg);
        }

        for(Friend_Group fg : fgs){
            mgr.updateBalance(fg);
        }
    }

    public static void cleanDB(){
        mgr.cleanDB();
    }

}
