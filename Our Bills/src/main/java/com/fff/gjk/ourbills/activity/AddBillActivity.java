package com.fff.gjk.ourbills.activity;

import android.app.Activity;
import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.fff.gjk.ourbills.bean.Bill;
import com.fff.gjk.ourbills.bean.Friend_Group;
import com.fff.gjk.ourbills.bean.Group;
import com.fff.gjk.ourbills.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class AddBillActivity extends Activity implements ActionBar.OnNavigationListener {

    /**
     * The serialization (saved instance state) Bundle key representing the
     * current dropdown position.
     */
    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

    public int fromGroupId = -1;

    private AddbillSimple addbillSimple;

    final Calendar c = Calendar.getInstance();
    public int mYear = c.get(Calendar.YEAR);
    public int mMonth = c.get(Calendar.MONTH);
    public int mDay = c.get(Calendar.DAY_OF_MONTH);

    public static List<Group> groups;
    private List<String> groupList = new ArrayList<String>();

    public static List<String> bcategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);

        // Set up the action bar to show a dropdown list.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        //要把app图标用作Up按钮
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Set up the dropdown list navigation in the action bar.
        actionBar.setListNavigationCallbacks(
                // Specify a SpinnerAdapter to populate the dropdown list.
                new ArrayAdapter<String>(
                        actionBar.getThemedContext(),
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1,
                        new String[] {
                                "Simple Bill",
                                "Meal Bill",
                                "Freestyle Bill",
                        }),
                this);

        Bundle b = this.getIntent().getExtras();
        fromGroupId = (Integer) b.get("fromGroupId");

        addbillSimple = new AddbillSimple();

        groups = MainActivity.mgr.getAllGroups();
        for (Group group : groups){
            groupList.add(group.gname);
        }

        bcategory = MainActivity.mgr.getAllBillCategory();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore the previously serialized current dropdown position.
        if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
            getActionBar().setSelectedNavigationItem(
                    savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Serialize the current dropdown position.
        outState.putInt(STATE_SELECTED_NAVIGATION_ITEM,
                getActionBar().getSelectedNavigationIndex());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_bill, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            //Intent i = NavUtils.getParentActivityIntent(this);
            //HashMap<String, Object> groupMap = new HashMap<String, Object>();
            //groupMap.put("gid",fromGroupId);
            //groupMap.put("gname",MainActivity.mgr.getGroupById(fromGroupId).gname);
            //i.putExtra("groupMap",groupMap);
            //NavUtils.navigateUpTo(this, i);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(int position, long id) {
        // When the given dropdown item is selected, show its contents in the
        // container view.
        switch (position){
            case 0: getFragmentManager().beginTransaction().replace(R.id.container, addbillSimple).commit();
            case 1: getFragmentManager().beginTransaction().replace(R.id.container, addbillSimple).commit();
            case 2: getFragmentManager().beginTransaction().replace(R.id.container, addbillSimple).commit();
        }

        return true;
    }


    public List<String> getFriendList(int groupId){

        List<String> friendList = new ArrayList<String>();

        List<Friend_Group> fgs = MainActivity.mgr.getFriend_GroupByGroupId(groupId);

        for(Friend_Group fg : fgs){
            friendList.add(MainActivity.mgr.getFriendById(fg.fid).fname);
        }

        return friendList;
    }

    private String buildDateString(){
        String date = String.valueOf(new StringBuilder().append(mYear).append("/")
                .append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append("/")
                .append((mDay < 10) ? "0" + mDay : mDay));
        return date;
    }

    public void addBill(Bill bill, HashMap<Integer, Double> pay, HashMap<Integer, Double> owe){

        MainActivity.mgr.addBill(bill);

        int bid = MainActivity.mgr.getLastBillId();

        ArrayList<Friend_Group> fgs = new ArrayList<Friend_Group>();

        Set paySet = pay.entrySet();
        for(Iterator iter = paySet.iterator(); iter.hasNext();)
        {
            Map.Entry entry = (Map.Entry)iter.next();

            int fid = Integer.parseInt((String)entry.getKey().toString());
            double payAmount = Double.valueOf((String) entry.getValue().toString());

            MainActivity.mgr.addFriendToBill(fid,bid,payAmount,0);

            Friend_Group fg = new Friend_Group(fid,bill.gid,payAmount);
            fgs.add(fg);
        }

        Set oweSet = owe.entrySet();
        for(Iterator iter = oweSet.iterator(); iter.hasNext();)
        {
            Map.Entry entry = (Map.Entry)iter.next();

            int fid = Integer.parseInt((String)entry.getKey().toString());
            double oweAmount = Double.valueOf((String)entry.getValue().toString());

            MainActivity.mgr.addFriendToBill(fid,bid,0,oweAmount);

            Friend_Group fg = new Friend_Group(fid,bill.gid,0-oweAmount);
            fgs.add(fg);
        }

        for(Friend_Group fg : fgs){
            MainActivity.mgr.updateBalance(fg);
        }
    }




























    public static class AddbillSimple extends Fragment {

        private AddBillActivity mActivity;

        private  int splitCount = 0;
        private List<String> friendList = new ArrayList<String>();

        private EditText title_edit;
        private EditText amount_edit;

        private EditText date_edit;
        private Button choose_date;

        private Spinner groupSpinner;
        private Spinner paySpinner;
        private Spinner categorySpinner;

        private ArrayAdapter<String> groupAdapter;
        private ArrayAdapter<String> categoryAdapter;
        private ArrayAdapter<String> payAdapter;

        private ListView splitList;
        private LinearLayout linearLayout;
        private List<CheckBox> checkBoxs = new ArrayList<CheckBox>();

        private Button submit;

        public AddbillSimple() {
        }


        @Override
        public void onAttach(Activity activity)
        {
            if (activity instanceof AddBillActivity)
            {
                mActivity = (AddBillActivity) activity;
            }
            super.onAttach(activity);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_add_bill_simple, container, false);


            friendList = mActivity.getFriendList(mActivity.fromGroupId);

            //spinner

            groupSpinner = (Spinner) view.findViewById(R.id.group_spinner);
            paySpinner = (Spinner) view.findViewById(R.id.pay_spinner);
            categorySpinner = (Spinner) view.findViewById(R.id.category_spinner);

            /* 准备数据源 M , 用集合进行保存  */

            //第一步：添加一个下拉列表项的list，这里添加的项就是下拉列表的菜单项

            //第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。
            groupAdapter = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_spinner_item, mActivity.groupList);
            categoryAdapter = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_spinner_item, bcategory);
            payAdapter = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_spinner_item, friendList);

            //第三步：为适配器设置下拉列表下拉时的菜单样式。
            groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            payAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            //第四步：将适配器添加到下拉列表上
            groupSpinner.setAdapter(groupAdapter);
            paySpinner.setAdapter(payAdapter);
            categorySpinner.setAdapter(categoryAdapter);

            //set default
            groupSpinner.setSelection(mActivity.fromGroupId -1);

            //第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中
            groupSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mActivity.fromGroupId = groups.get(position).gid;
                    friendList = mActivity.getFriendList(mActivity.fromGroupId);

                    payAdapter = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_spinner_item, friendList);
                    payAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    paySpinner.setAdapter(payAdapter);

                    linearLayout.removeAllViews();
                    checkBoxs = new ArrayList<CheckBox>();
                    for(int i = 0; i < friendList.size(); i++) {
                        CheckBox checkBoxLayout = (CheckBox) mActivity.getLayoutInflater().inflate(R.layout.list_item_checkbox, null);
                        checkBoxs.add(checkBoxLayout);
                        checkBoxs.get(i).setText(friendList.get(i));

                        //实现了在main主布局中，通过LinearLayout在for循环中添加checkbox。
                        linearLayout.addView(checkBoxLayout, i);

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            //split list
            linearLayout = (LinearLayout) view.findViewById(R.id.split_layout);

            //date
            date_edit = (EditText) view.findViewById(R.id.date_edit);
            date_edit.setText(mActivity.buildDateString());
            choose_date = (Button) view.findViewById(R.id.choose_date);

            final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
                    mActivity.mYear = year;
                    mActivity.mMonth = monthOfYear;
                    mActivity.mDay = dayOfMonth;
                    date_edit.setText(mActivity.buildDateString());
                }
            };

            choose_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DatePickerDialog(getActivity(), mDateSetListener, mActivity.mYear, mActivity.mMonth, mActivity.mDay).show();
                }
            });

            //title & amount
            title_edit = (EditText) view.findViewById(R.id.title_edit);
            amount_edit = (EditText) view.findViewById(R.id.amount_edit);



            //submit
            submit = (Button) view.findViewById(R.id.submit_simple);
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(checkComplete()){
                        Bill bill = new Bill();
                        bill.title = String.valueOf(title_edit.getText());
                        bill.amount = Double.parseDouble(amount_edit.getText().toString());
                        bill.date = String.valueOf(date_edit.getText());
                        bill.gid = MainActivity.mgr.getGidByGname(groupSpinner.getSelectedItem().toString());
                        bill.bcid = MainActivity.mgr.getBcidByBcname(categorySpinner.getSelectedItem().toString());
                        bill.isSimple = 1;

                        HashMap<Integer, Double> pay = new HashMap<Integer, Double>();
                        pay.put(MainActivity.mgr.getFidByFname(paySpinner.getSelectedItem().toString()),bill.amount);

                        HashMap<Integer, Double> owe = new HashMap<Integer, Double>();
                        for(CheckBox cb : checkBoxs){
                            if(cb.isChecked()){
                                owe.put(MainActivity.mgr.getFidByFname(cb.getText().toString()),bill.amount/splitCount);
                            }
                        }

                        mActivity.addBill(bill,pay,owe);

                        Toast.makeText(getActivity(),"Bill added !",Toast.LENGTH_SHORT).show();

                        HashMap<String, Object> groupMap = new HashMap<String, Object>();
                        groupMap.put("gid",bill.gid);
                        groupMap.put("gname",MainActivity.mgr.getGroupById(bill.gid).gname);
                        Intent i = new Intent();
                        i.putExtra("groupMap", groupMap);
                        getActivity().setResult(RESULT_OK,i);
                        getActivity().finish();

                    }
                }
            });

            return view;
        }

        private boolean checkComplete() {
            if (title_edit.getText().length()==0){Toast.makeText(getActivity(),"Please input title",Toast.LENGTH_SHORT).show();return false;}
            if (amount_edit.getText().length()==0){Toast.makeText(getActivity(),"Please input amount",Toast.LENGTH_SHORT).show();return false;}

            splitCount = 0;
            for(CheckBox cb : checkBoxs){
                if(cb.isChecked()){splitCount++;}
            }
            if (splitCount == 0){
                Toast.makeText(getActivity(),"Please select at least one friend to split the bill!",Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;

        }


    }






}
