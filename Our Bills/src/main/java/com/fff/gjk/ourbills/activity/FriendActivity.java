package com.fff.gjk.ourbills.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.fff.gjk.ourbills.bean.Bill;
import com.fff.gjk.ourbills.bean.Friend_Bill;
import com.fff.gjk.ourbills.bean.Friend_Group;
import com.fff.gjk.ourbills.bean.Group;
import com.fff.gjk.ourbills.R;

public class FriendActivity extends Activity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    private FriendBalanceFragment friendBalanceFragment;
    private FriendBillsFragment friendBillsFragment;

    private int friendId;
    private String friendName;

    java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        //从main获得数据
        Bundle b = this.getIntent().getExtras();
        HashMap<String, Object> friendMap = (HashMap<String, Object>) b.get("friendMap");

        friendId = Integer.valueOf(friendMap.get("fid").toString());
        friendName = friendMap.get("fname").toString();
        setTitle(friendName.toUpperCase());

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        //要把app图标用作Up按钮
        actionBar.setDisplayHomeAsUpEnabled(true);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager_friend);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }

        Intent i = new Intent();
        this.setResult(RESULT_OK, i);

    }


    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){

        //requestCode:当前页面上有两个跳转按钮时，都用startActivityForResult，是为了判断哪个按钮跳转后返回回来的
        switch(resultCode){
            case RESULT_OK:

                friendName = MainActivity.mgr.getFriendById(friendId).fname;
                setTitle(friendName.toUpperCase());

                getActionBar().removeAllTabs();
                for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
                    getActionBar().addTab(
                            getActionBar().newTab()
                                    .setText(mSectionsPagerAdapter.getPageTitle(i))
                                    .setTabListener(this));
                }

                //mSectionsPagerAdapter.notifyDataSetChanged();
        }
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.friend, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.edit_friend_button) {
            Intent i = new Intent(FriendActivity.this, EditFriendActivity.class);
            i.putExtra("fid",friendId);
            startActivityForResult(i, 0);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }









    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a AddbillSimple (defined as a static inner class below).
            switch (position) {
                case 0:
                    friendBalanceFragment = new FriendBalanceFragment();
                    return friendBalanceFragment;
                case 1:
                    friendBillsFragment = new FriendBillsFragment();
                    return friendBillsFragment;
            }
            return null;        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return friendName+"'s blalance".toUpperCase(l);
                case 1:
                    return friendName+"'s bills".toUpperCase(l);
            }
            return null;
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }
    }


















    public class FriendBalanceFragment extends Fragment {

        ListView listView_Friend_Balance;

        public FriendBalanceFragment(){}

        public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){

            View view = inflater.inflate(R.layout.fragment_friend_balance, container, false);

            listView_Friend_Balance = (ListView) view.findViewById(R.id.listView_friend_balance);

            showGroupAndBalance(view);

            listView_Friend_Balance.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    HashMap<String, Object> groupMap = (HashMap<String, Object>) parent.getItemAtPosition(position);

                    Intent i = new Intent(FriendActivity.this, GroupActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //http://www.eoeandroid.com/thread-205458-1-1.html
                    i.putExtra("groupMap", groupMap);
                    getActivity().startActivity(i);

                    //Toast.makeText(getActivity(),"position:" + position + "  info:" + friendMap.get("fid") + " " + friendMap.get("fname") + " " + friendMap.get("balance"),Toast.LENGTH_SHORT).show();
                }
            });

            return view;
        }

        public void showGroupAndBalance(View view) {

            List<Friend_Group> fgs = MainActivity.mgr.getFriend_GroupByFriendId(friendId);

            ArrayList<Map<String, Object>> fglist = new ArrayList<Map<String, Object>>();

            for (Friend_Group fg : fgs) {

                Group group =  MainActivity.mgr.getGroupById(fg.gid);

                String balanceInfo = "";
                if (fg.balance<0.001&&fg.balance>-0.001){
                    balanceInfo = "Settled Up";
                }else{
                    balanceInfo = "Balance $ "+df.format(fg.balance);
                }

                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("gid", fg.gid);
                map.put("img", R.drawable.icon);
                map.put("gname",group.gname);
                map.put("balance", balanceInfo);
                fglist.add(map);
            }

            SimpleAdapter adapter = new SimpleAdapter(view.getContext(), fglist, R.layout.list_item_with_pic,
                    new String[]{"img", "gname", "balance"}, new int[]{R.id.img, android.R.id.text1, android.R.id.text2});

            listView_Friend_Balance.setAdapter(adapter);

        }
    }


























    public class FriendBillsFragment extends Fragment {


        ListView listView_Friend_Bills;
        ExpandableListView ex_listView_Friend_Bills;

        List<Map<String, String >> group=new ArrayList<Map<String,String>>();
        List<List<Map<String , Object >>> child=new ArrayList<List<Map<String,Object>>>();

        public FriendBillsFragment(){}

        public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_friend_bills, container, false);
            listView_Friend_Bills = (ListView) view.findViewById(R.id.listView_friend_bills);

            ArrayList<Map<String, Object>> data = prepareData();//"data" is not used

            //MyAdapter adapter=new MyAdapter(data); //for another list
            //listView_Friend_Bills.setAdapter(adapter); //for another list

            ex_listView_Friend_Bills = (ExpandableListView) view.findViewById(R.id.ex_listView_friend_bills);
            myExAdapter myAdapter=new myExAdapter(getActivity(),group,child);
            ex_listView_Friend_Bills.setAdapter(myAdapter);

            return view;

            /*
            //for dynamic
            LinearLayout linearlayout = (LinearLayout) inflater.inflate(R.layout.fragment_friend_bills, container,false);
            Button btn1=new Button(linearlayout.getContext());
            btn1.setText("Button1");
            linearlayout.addView(btn1);
            return linearlayout;
            */

        }

        public ArrayList<Map<String, Object>> prepareData(){

            List<Friend_Bill> fbs = MainActivity.mgr.getFriend_BillByFid(friendId);

            ArrayList<Map<String, Object>> rawData = new ArrayList<Map<String, Object>>();

            for(Friend_Bill fb : fbs){
                String info = "";
                if (fb.pay>0.001){info = "You paid $ "+fb.pay;}
                if (fb.owe>0.001){info = "You owed $ "+fb.owe;}
                //if (fb.pay>0.001&&fb.owe>0.001){info = "You paid $ "+fb.pay+" & owed $ "+fb.owe;}

                Bill bill = MainActivity.mgr.getBillById(fb.bid);
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("isTitle",false);
                map.put("bid", fb.bid);
                map.put("gid", bill.gid);
                map.put("img", R.drawable.bill);
                map.put("title", bill.title);
                map.put("info", info);
                map.put("amount", "$ "+bill.amount);
                map.put("date", bill.date);
                rawData.add(map);
            }

            List<Friend_Group> fgs = MainActivity.mgr.getFriend_GroupByFriendId(friendId);
            ArrayList<Integer> groupId = new ArrayList<Integer>();
            for (Friend_Group fg : fgs){groupId.add(fg.gid);}

            ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

            /*
            //prepare "data" list, not used
            for (int gid : groupId){
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("isTitle",true);
                map.put("title",MainActivity.mgr.getGroupById(gid).gname);
                data.add(map);

                for (Map<String, Object> map2 : rawData){
                    if (gid == map2.get("gid")){
                        data.add(map2);
                    }
                }
            }
            */

            for (int gid : groupId) {
                Map<String , String > map=new HashMap<String, String>();
                map.put("title", MainActivity.mgr.getGroupById(gid).gname);
                group.add(map);
                List<Map<String, Object >> childData=new ArrayList<Map<String,Object>>();
                for (Map<String, Object> map2 : rawData) {

                    if (gid == (Integer)map2.get("gid")){
                        childData.add(map2);
                    }
                }
                child.add(childData);
            }

            return data;//not used
        }
    }


















    private class MyAdapter extends BaseAdapter {

        ArrayList<Map<String, Object>> data;

        public MyAdapter(ArrayList<Map<String, Object>> data){
            this.data = data;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
        @Override
        public boolean isEnabled(int position) {
            // TODO Auto-generated method stub
            HashMap<String, Object> map = (HashMap<String, Object>)getItem(position);
            if(true == (boolean)map.get("isTitle")){
                return false;
            }
            return super.isEnabled(position);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View view=convertView;

            HashMap<String, Object> map = (HashMap<String, Object>)getItem(position);

            if(true == (boolean)map.get("isTitle")){
                view=LayoutInflater.from(getApplicationContext()).inflate(R.layout.list_tag, null);
                TextView text=(TextView) view.findViewById(R.id.text1);
                text.setText((CharSequence) map.get("title"));

            }else{
                view=LayoutInflater.from(getApplicationContext()).inflate(R.layout.list_item_with_pic, null);
                TextView text1=(TextView) view.findViewById(android.R.id.text1);
                TextView text2=(TextView) view.findViewById(android.R.id.text2);
                TextView text3=(TextView) view.findViewById(R.id.text3);
                TextView text4=(TextView) view.findViewById(R.id.text4);
                ImageView img =(ImageView) view.findViewById(R.id.img);

                text1.setText(map.get("title").toString());
                text2.setText(map.get("info").toString());
                text3.setText(map.get("amount").toString());
                text4.setText(map.get("date").toString());
                img.setImageResource((Integer) map.get("img"));
            }

            return view;
        }

    }


    class myExAdapter extends BaseExpandableListAdapter {
        LayoutInflater inflater;
        List<Map<String, String >> group=new ArrayList<Map<String,String>>();
        List<List<Map<String , Object >>> child=new ArrayList<List<Map<String,Object>>>();

        public myExAdapter(Context context, List<Map<String, String >> group, List<List<Map<String , Object >>> child) {
            this.child = child;
            this.group = group;
        }

        public myExAdapter(Context context){}

        public Object getChild(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return child.get(groupPosition).get(childPosition);
        }

        public long getChildId(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return childPosition;
        }

        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            if(convertView==null){
                inflater=(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView=inflater.inflate(R.layout.list_item_with_pic, null);

                TextView text1=(TextView) convertView.findViewById(android.R.id.text1);
                TextView text2=(TextView) convertView.findViewById(android.R.id.text2);
                TextView text3=(TextView) convertView.findViewById(R.id.text3);
                TextView text4=(TextView) convertView.findViewById(R.id.text4);
                ImageView img =(ImageView) convertView.findViewById(R.id.img);

                text1.setText(child.get(groupPosition).get(childPosition).get("title").toString());
                text2.setText(child.get(groupPosition).get(childPosition).get("info").toString());
                text3.setText(child.get(groupPosition).get(childPosition).get("amount").toString());
                text4.setText(child.get(groupPosition).get(childPosition).get("date").toString());
                img.setImageResource(Integer.parseInt(child.get(groupPosition).get(childPosition).get("img").toString()));
            }
            return convertView;
        }

        public int getChildrenCount(int groupPosition) {
            // TODO Auto-generated method stub
            return child.get(groupPosition).size();
        }

        public Object getGroup(int groupPosition) {
            // TODO Auto-generated method stub
            return group.get(groupPosition);
        }

        public int getGroupCount() {
            // TODO Auto-generated method stub
            return group.size();
        }

        public long getGroupId(int groupPosition) {
            // TODO Auto-generated method stub
            return groupPosition;
        }

        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            if(convertView==null){
                inflater=(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView=inflater.inflate(R.layout.list_tag, null);
                TextView group_tv=(TextView) convertView.findViewById(R.id.text2);
                group_tv.setText(group.get(groupPosition).get("title"));
            }
            return convertView;
        }

        public boolean hasStableIds() {
            // TODO Auto-generated method stub
            return true;
        }

        public boolean isChildSelectable(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return true;
        }

    }

}


