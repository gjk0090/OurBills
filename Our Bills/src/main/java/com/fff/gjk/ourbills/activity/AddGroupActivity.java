package com.fff.gjk.ourbills.activity;

import android.app.Activity;
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
                String gname = gnameEdit.getText().toString();
                int gcid = MainActivity.mgr.getGcidByGcname(gcategorySpinner.getSelectedItem().toString());
                //Toast.makeText(AddGroupActivity.this,gname+gcid,Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_group, menu);
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
