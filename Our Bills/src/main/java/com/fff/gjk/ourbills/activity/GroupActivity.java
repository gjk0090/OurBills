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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.fff.gjk.ourbills.bean.Bill;
import com.fff.gjk.ourbills.bean.Friend;
import com.fff.gjk.ourbills.bean.Friend_Bill;
import com.fff.gjk.ourbills.bean.Friend_Group;
import com.fff.gjk.ourbills.R;

public class GroupActivity extends Activity implements ActionBar.TabListener {

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

    private GroupBalanceFragment groupBalanceFragment;
    private GroupBillsFragment groupBillsFragment;

    private int groupId = 1;
    private String groupName = MainActivity.mgr.getGroupById(1).gname;

    java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        //从main获得数据
        try{
            Bundle b = this.getIntent().getExtras();

            HashMap<String, Object> groupMap = (HashMap<String, Object>) b.get("groupMap");

            groupId = Integer.valueOf(groupMap.get("gid").toString());
            groupName = groupMap.get("gname").toString();
        }catch(Exception e) {}

        setTitle(groupName.toUpperCase());

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        //要把app图标用作Up按钮
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager_group);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.group, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.add_bill) {
            Intent i = new Intent(GroupActivity.this, AddBillActivity.class);
            i.putExtra("groupId",groupId);
            startActivityForResult(i, 0);
            return true;
        }

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        //requestCode:当前页面上有两个跳转按钮时，都用startActivityForResult，是为了判断哪个按钮跳转后返回回来的
        switch(resultCode){
            case RESULT_OK:
                Bundle b = data.getExtras();
                HashMap<String, Object> groupMap = (HashMap<String, Object>) b.get("groupMap");

                groupId = Integer.valueOf(groupMap.get("gid").toString());
                groupName = groupMap.get("gname").toString();
                setTitle(groupName.toUpperCase());

                //mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
                //mViewPager.setAdapter(mSectionsPagerAdapter);
                mSectionsPagerAdapter.notifyDataSetChanged();

        }
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
                    groupBalanceFragment = new GroupBalanceFragment();
                    return groupBalanceFragment;
                case 1:
                    groupBillsFragment = new GroupBillsFragment();
                    return groupBillsFragment;
            }
            return null;
        }



        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }


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
                    return "group balance".toUpperCase(l);
                case 1:
                    return "group bills".toUpperCase(l);
            }
            return null;
        }
    }























    public class GroupBalanceFragment extends Fragment {

        ListView listView_Group_Balance;

        public GroupBalanceFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_group_balance, container, false);
            listView_Group_Balance = (ListView) view.findViewById(R.id.listView_group_balance);

            showFriendAndBalance(view);

            listView_Group_Balance.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    HashMap<String, Object> friendMap = (HashMap<String, Object>) parent.getItemAtPosition(position);

                    Intent i = new Intent(GroupActivity.this, FriendActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //http://www.eoeandroid.com/thread-205458-1-1.html
                    i.putExtra("friendMap", friendMap);
                    getActivity().startActivity(i);

                    //Toast.makeText(getActivity(),"position:" + position + "  info:" + friendMap.get("fid") + " " + friendMap.get("fname") + " " + friendMap.get("balance"),Toast.LENGTH_SHORT).show();
                }
            });

            return view;
        }

        public void showFriendAndBalance(View view) {

            List<Friend_Group> fgs = MainActivity.mgr.getFriend_GroupByGroupId(groupId);

            ArrayList<Map<String, Object>> fglist = new ArrayList<Map<String, Object>>();

            for (Friend_Group fg : fgs) {

                Friend friend =  MainActivity.mgr.getFriendById(fg.fid);

                String balanceInfo = "";
                if (fg.balance<0.001&&fg.balance>-0.001){
                    balanceInfo = "Settled Up";
                }else{
                    balanceInfo = "Balance $ "+df.format(fg.balance);
                }

                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("fid", fg.fid);
                map.put("img", (friend.gender.equals("male")? R.drawable.male:R.drawable.female));
                map.put("fname",friend.fname);
                map.put("balance", balanceInfo);
                fglist.add(map);
            }

            SimpleAdapter adapter = new SimpleAdapter(view.getContext(), fglist, R.layout.list_item_with_pic,
                    new String[]{"img", "fname", "balance"}, new int[]{R.id.img, android.R.id.text1, android.R.id.text2});

            listView_Group_Balance.setAdapter(adapter);

        }

    }
























    public class GroupBillsFragment extends Fragment {

        ListView listView_Group_Bills;

        public GroupBillsFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_group_bills, container, false);
            listView_Group_Bills = (ListView) view.findViewById(R.id.listView_group_bills);

            showBillsInGroup(view);

            listView_Group_Bills.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    HashMap<String, Object> billMap = (HashMap<String, Object>) parent.getItemAtPosition(position);

                    Toast.makeText(getActivity(), "position:" + position + "  bill id & title:" + billMap.get("bid") + " " + billMap.get("title"), Toast.LENGTH_SHORT).show();
                }
            });

            return view;
        }

        public void showBillsInGroup(View view) {

            List<Bill> bills = MainActivity.mgr.getBillsByGroupId(groupId);

            ArrayList<Map<String, Object>> billlist = new ArrayList<Map<String, Object>>();

            for (Bill bill : bills) {

                List<Friend_Bill> fbs = MainActivity.mgr.getFriend_BillByBillId(bill.bid);

                String payer="";
                int oweCount=0;

                for(Friend_Bill fb : fbs){
                    if (fb.pay>0.001){payer += MainActivity.mgr.getFriendById(fb.fid).fname+" ";}
                    if (fb.owe>0.001){oweCount += 1;}
                }

                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("bid", bill.bid);
                map.put("img", R.drawable.bill);
                map.put("title", bill.title);
                map.put("splitinfo", "paid by "+payer+"  split by "+oweCount);
                map.put("amount", "$ "+bill.amount);
                map.put("date", bill.date);
                billlist.add(map);
            }

            SimpleAdapter adapter = new SimpleAdapter(view.getContext(), billlist, R.layout.list_item_with_pic,
                    new String[]{"img", "title", "splitinfo", "date", "amount"}, new int[]{R.id.img, android.R.id.text1, android.R.id.text2, R.id.text4, R.id.text3});

            listView_Group_Bills.setAdapter(adapter);

        }

    }
}
