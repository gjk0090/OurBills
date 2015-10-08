package com.fff.gjk.ourbills.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.fff.gjk.ourbills.R;
import com.fff.gjk.ourbills.bean.Friend;
import com.fff.gjk.ourbills.bean.Group;

import java.util.*;

public class AddGroupActivity extends Activity {

    private EditText gnameEdit;
    private Spinner gcategorySpinner;
    private LinearLayout friendsLayout;
    private Button addGroup;

    private List<String> gcList;
    private List<Friend> friendList;
    private List<CheckBox> checkBoxs = new ArrayList<CheckBox>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);

        gnameEdit = (EditText) findViewById(R.id.add_group_name);

        gcategorySpinner = (Spinner) findViewById(R.id.add_group_category);
        gcList = MainActivity.mgr.getAllGroupCategory();
        ArrayAdapter<String> categoryAdapter;
        categoryAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, gcList);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gcategorySpinner.setAdapter(categoryAdapter);

        friendList = MainActivity.mgr.getAllFriends();
        friendsLayout = (LinearLayout) findViewById(R.id.add_group_friends);

        checkBoxs = new ArrayList<CheckBox>();
        for(int i = 0; i < friendList.size(); i++) {
            CheckBox checkBox = (CheckBox)getLayoutInflater().inflate(R.layout.list_item_checkbox, null);
            checkBox.setText(friendList.get(i).fname);
            checkBoxs.add(checkBox);

            //实现了在main主布局中，通过LinearLayout在for循环中添加checkbox。
            friendsLayout.addView(checkBox, i);

        }

        addGroup = (Button) findViewById(R.id.submit_group);
        addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validate name
                String gname = gnameEdit.getText().toString();
                if(gname == null || gname.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please enter group name.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(MainActivity.mgr.getGidByGname(gname)!=-1){
                    Toast.makeText(getApplicationContext(),"Group already exist!",Toast.LENGTH_SHORT).show();
                    return;
                }

                //validate friends number
                List<Integer> fids = new ArrayList<Integer>();
                for(int i=0; i<checkBoxs.size(); i++){
                    if(checkBoxs.get(i).isChecked()){
                        fids.add(MainActivity.mgr.getFidByFname(checkBoxs.get(i).getText().toString()));
                    }
                }
                if(fids.size() == 0){
                    Toast.makeText(getApplicationContext(), "Please select at least one friend.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //get category id
                int gcid = MainActivity.mgr.getGcidByGcname(gcategorySpinner.getSelectedItem().toString());

                //insert group
                int gid = MainActivity.mgr.addGroup(new Group(gname, gcid));
                if(gid == -1){
                    Toast.makeText(AddGroupActivity.this, "Create group failed for unknown reason.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //insert friends
                for(int fid : fids){
                    MainActivity.mgr.addFriendToGroup(fid, gid);
                }

                Toast.makeText(getApplicationContext(), "Add group "+gname+" success!", Toast.LENGTH_SHORT).show();


                Intent i = new Intent();
                AddGroupActivity.this.setResult(RESULT_OK, i);
                AddGroupActivity.this.finish();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.add_group, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
