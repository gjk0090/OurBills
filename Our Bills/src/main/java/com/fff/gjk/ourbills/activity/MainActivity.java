package com.fff.gjk.ourbills.activity;

import java.util.*;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
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
import android.widget.SimpleAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.fff.gjk.ourbills.bean.Bill;
import com.fff.gjk.ourbills.bean.Friend;
import com.fff.gjk.ourbills.bean.Friend_Group;
import com.fff.gjk.ourbills.bean.Group;
import com.fff.gjk.ourbills.dao.DBManager;
import com.fff.gjk.ourbills.R;
import com.fff.gjk.ourbills.util.Constants;
import com.fff.gjk.ourbills.util.TestDBHelper;

public class MainActivity extends Activity implements ActionBar.TabListener {

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

    public static DBManager mgr;
    private MainGroupsFragment mainGroupsFragment;
    private MainFriendsFragment mainFriendsFragment;
    java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");



    @Override
    protected void onDestroy() {
        super.onDestroy();
        //应用的最后一个Activity关闭时应释放DB
        mgr.closeDB();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        //初始化DBManager
        mgr = new DBManager(this);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the 2
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager_main);
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
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.add_bill) {
            if(mgr.getAllGroups().size()!=0) {
                Intent i = new Intent(MainActivity.this, AddBillActivity.class);
                i.putExtra("fromGroupId", 1);
                startActivityForResult(i, 0);
            }else{
                Toast.makeText(this,"Please add a group with friends first!",Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        if (id == R.id.add_friend) {
            Intent i = new Intent(MainActivity.this, AddFriendActivity.class);
            startActivityForResult(i, 0);
        }

        if (id == R.id.add_group) {
            if(mgr.getAllFriends().size() != 0) {
                Intent i = new Intent(MainActivity.this, AddGroupActivity.class);
                startActivityForResult(i, 0);
            }else{
                Toast.makeText(this,"Please add at least one friend first!",Toast.LENGTH_SHORT).show();
            }
        }





        if (id == R.id.action_settings) {
            return true;
        }

//        if (id == R.id.add_DB) {
//            TestDBHelper.addDB();
//            Toast.makeText(this, "added!", Toast.LENGTH_SHORT).show();
//
//
//            //update fragment
//            mSectionsPagerAdapter.notifyDataSetChanged();
//            return true;
//        }
        if (id == R.id.clear_DB) {
            new AlertDialog.Builder(this).setTitle("Danger!")
                    .setMessage("Do you want to clear all data？")
                    .setIcon(R.drawable.ic_launcher)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            setResult(RESULT_OK);//确定按钮事件
                            TestDBHelper.cleanDB();

                            //update fragment
                            //mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
                            //mViewPager.setAdapter(mSectionsPagerAdapter);
                            mSectionsPagerAdapter.notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //取消按钮事件
                        }
                    }).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){

        //requestCode:当前页面上有两个跳转按钮时，都用startActivityForResult，是为了判断哪个按钮跳转后返回回来的
        switch(resultCode){
            case RESULT_OK:
                //mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
                //mViewPager.setAdapter(mSectionsPagerAdapter);
                //mViewPager.setCurrentItem(getActionBar().getSelectedTab().getPosition());
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
            // Return a MainGroupsFragment (defined as a static inner class below).

            //Toast.makeText(MainActivity.this,"getItem",Toast.LENGTH_SHORT).show();

            switch (position) {
                case 0:
                    mainGroupsFragment = new MainGroupsFragment();
                    return mainGroupsFragment;
                case 1:
                    mainFriendsFragment = new MainFriendsFragment();
                    return mainFriendsFragment;
            }
            return null;
        }




          //this method will be called after notifyDataSetChanged(), add new data to old fragment here
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            Toast.makeText(MainActivity.this,"instantiate",Toast.LENGTH_SHORT).show();
//            return super.instantiateItem(container, position);
//        }


        //this method will destroy and then recreate(?) the fragment after notifyDataSetChanged()
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
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
            }
            return null;
        }
    }






















    public static class MainGroupsFragment extends Fragment {

        private MainActivity mActivity;
        private ListView listView;

        public MainGroupsFragment() {
        }

        @Override
        public void onAttach(Activity activity)
        {
            if (activity instanceof MainActivity)
            {
                mActivity = (MainActivity) activity;
            }
            super.onAttach(activity);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

            //Toast.makeText(getActivity(),"group fragment onCreateView",Toast.LENGTH_SHORT).show();

            View view = inflater.inflate(R.layout.fragment_main_groups, container, false);
            listView = (ListView) view.findViewById(R.id.listView_groups);


            showGroups(view);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    HashMap<String, Object> groupMap = (HashMap<String, Object>) parent.getItemAtPosition(position);

                    Intent i = new Intent(mActivity, GroupActivity.class);
                    i.putExtra("groupMap", groupMap);
                    getActivity().startActivityForResult(i, 0);

                    //Toast.makeText(getActivity(),"position:" + position + "  group id & name:" + groupMap.get("gid") + " " + groupMap.get("gname"),Toast.LENGTH_SHORT).show();
                }
            });

            return view;
        }

        public void showGroups(View view) {

            List<Group> groups = MainActivity.mgr.getAllGroups();

            ArrayList<Map<String, Object>> grouplist = new ArrayList<Map<String, Object>>();

            for (Group group : groups) {

                double debt = 0;
                String status = "";
                List<Friend_Group> fgs = mgr.getFriend_GroupByGroupId(group.gid);
                for (Friend_Group fg : fgs){
                    if(fg.balance>Constants.settledThreshold){debt += fg.balance;}
                }
                if (debt<Constants.settledThreshold&&debt>-Constants.settledThreshold){
                    status = "Settled Up";
                }else{
                    status = "Debt $ " + mActivity.df.format(debt);
                }

                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("gid", group.gid);
                map.put("gname", group.gname);
                map.put("status",status);
                map.put("gcategory", MainActivity.mgr.getGroupCategoryNameById(group.gcid));
                grouplist.add(map);
            }

            SimpleAdapter adapter = new SimpleAdapter(view.getContext(), grouplist, R.layout.list_item_with_pic,
                    new String[]{"gname", "status", "gcategory"}, new int[]{android.R.id.text1, android.R.id.text2, R.id.text3});

            listView.setAdapter(adapter);

        }


//        @Override
//        public void onAttach(Activity activity) {
//            super.onAttach(activity);
//            Toast.makeText(getActivity(),"group fragment onAttach",Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            Toast.makeText(getActivity(),"group fragment onCreate",Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onActivityCreated(Bundle savedInstanceState) {
//            super.onActivityCreated(savedInstanceState);
//            Toast.makeText(getActivity(),"group fragment onActivityCreated",Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onStart() {
//            super.onStart();
//            Toast.makeText(getActivity(),"group fragment onStart",Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onResume() {
//            super.onResume();
//            Toast.makeText(getActivity(),"group fragment onResume",Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onPause() {
//            super.onPause();
//            Toast.makeText(getActivity(),"group fragment onPause",Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onStop() {
//            super.onStop();
//            Toast.makeText(getActivity(),"group fragment onStop",Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onDestroyView() {
//            super.onDestroyView();
//            Toast.makeText(getActivity(),"group fragment onDestroyView",Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onDestroy() {
//            super.onDestroy();
//            Toast.makeText(getActivity(),"group fragment onDestroy",Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onDetach() {
//            super.onDetach();
//            Toast.makeText(getActivity(),"group fragment onDetach",Toast.LENGTH_SHORT).show();
//        }

    }























    public static class MainFriendsFragment extends Fragment {

        private MainActivity mActivity;

        ListView listView2;

        public MainFriendsFragment() {
        }

        @Override
        public void onAttach(Activity activity)
        {
            if (activity instanceof MainActivity)
            {
                mActivity = (MainActivity) activity;
            }
            super.onAttach(activity);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_main_friends, container, false);
            listView2 = (ListView) view.findViewById(R.id.listView_friends);
            showFriends(view);

            listView2.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    HashMap<String, Object> friendMap = (HashMap<String, Object>) parent.getItemAtPosition(position);

                    Intent i = new Intent(mActivity, FriendActivity.class);
                    i.putExtra("friendMap", friendMap);
                    getActivity().startActivityForResult(i, 0);

                    //Toast.makeText(getActivity(),"position:" + position + "  friend id & name:" + friendMap.get("fid") + " " + friendMap.get("fname"),Toast.LENGTH_SHORT).show();
                }
            });

            return view;
        }

        public void showFriends(View view) {

            List<Friend> friends = MainActivity.mgr.getAllFriends();
            ArrayList<Map<String, Object>> friendlist = new ArrayList<Map<String, Object>>();
            for (Friend friend : friends) {

                double sum = mgr.getTotalBalanceByFid(friend.fid);

                String balanceInfo = "";
                if (sum< Constants.settledThreshold&&sum>-Constants.settledThreshold){
                    balanceInfo = "Settled Up";
                }else{
                    balanceInfo = "Total Balance $ " + mActivity.df.format(sum);
                }

                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("fid", friend.fid);
                map.put("img", (friend.gender.equals("male")? R.drawable.male:R.drawable.female));
                map.put("fname", friend.fname);
                map.put("balance", balanceInfo);
                //map.put("info", friend.gender+" "+friend.email);
                map.put("info", "");
                friendlist.add(map);
            }
            SimpleAdapter adapter = new SimpleAdapter(view.getContext(), friendlist, R.layout.list_item_with_pic,
                    new String[]{"img", "fname", "balance", "info"}, new int[]{R.id.img, android.R.id.text1, android.R.id.text2, R.id.text3});

            listView2.setAdapter(adapter);

        }

    }


}
