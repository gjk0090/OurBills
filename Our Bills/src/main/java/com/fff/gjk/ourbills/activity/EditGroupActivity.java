package com.fff.gjk.ourbills.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.fff.gjk.ourbills.R;
import com.fff.gjk.ourbills.bean.Friend;
import com.fff.gjk.ourbills.bean.Friend_Group;
import com.fff.gjk.ourbills.bean.Group;
import com.fff.gjk.ourbills.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by gjk on 10/3/15.
 */
public class EditGroupActivity extends Activity {

    private int fromGroupId = -1;
    private Group oriGroup;
    private boolean settledUp;

    private EditText gnameEdit;
    private Spinner gcategorySpinner;
    private LinearLayout friendsLayout;
    private Button resetGroup;
    private Button editGroup;
    private Button deleteGroup;

    private List<Friend_Group> fgs;
    private Set<Integer> fidSet;
    private List<String> gcList;
    private List<Friend> friendList;
    private List<CheckBox> checkBoxs = new ArrayList<CheckBox>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group);

        Bundle b = this.getIntent().getExtras();
        fromGroupId = (Integer) b.get("fromGroupId");

        oriGroup = MainActivity.mgr.getGroupById(fromGroupId);

        //find all friends id
        fgs = MainActivity.mgr.getFriend_GroupByGroupId(fromGroupId);
        fidSet = new HashSet<Integer>();
        for(Friend_Group fg : fgs){
            fidSet.add(fg.fid);
        }

        //check group settled up
        double debt = 0;
        for (Friend_Group fg : fgs){
            if(fg.balance>Constants.settledThreshold){debt += fg.balance;}
        }
        if (debt<Constants.settledThreshold&&debt>-Constants.settledThreshold){
            settledUp = true;
        }

        //text edit
        gnameEdit = (EditText) findViewById(R.id.edit_group_name);
        gnameEdit.setText(oriGroup.gname);

        //spinner
        gcategorySpinner = (Spinner) findViewById(R.id.edit_group_category);
        gcList = MainActivity.mgr.getAllGroupCategory();
        ArrayAdapter<String> categoryAdapter;
        categoryAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, gcList);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gcategorySpinner.setAdapter(categoryAdapter);
        gcategorySpinner.setSelection(oriGroup.gcid - 1);

        //check boxs
        friendList = MainActivity.mgr.getAllFriends();
        friendsLayout = (LinearLayout) findViewById(R.id.edit_group_friends);

        checkBoxs = new ArrayList<CheckBox>();
        for(int i = 0; i < friendList.size(); i++) {
            CheckBox checkBox = (CheckBox)getLayoutInflater().inflate(R.layout.list_item_checkbox, null);
            checkBox.setText(friendList.get(i).fname);

            //check existing friends
            int currFid = friendList.get(i).fid;
            Friend_Group currFg = new Friend_Group();
            for(Friend_Group fg : fgs){
                if(fg.fid == currFid) {
                    currFg = fg;
                    break;
                }
            }
            if(fidSet.contains(currFid)){
                checkBox.setChecked(true);
                //disable existing friends which is not settled up
                if (currFg.balance>Constants.settledThreshold || currFg.balance<-Constants.settledThreshold) {
                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton v, boolean isChecked) {
                            if(!isChecked) {
                                CheckBox checkBox = (CheckBox) v;
                                Toast.makeText(getApplicationContext(), "Friend not settled up, can not delete friend.", Toast.LENGTH_SHORT).show();
                                checkBox.setChecked(true);
                            }
                        }
                    });

                }

            }
            checkBoxs.add(checkBox);
            friendsLayout.addView(checkBox, i);

        }

        resetGroup = (Button) findViewById(R.id.reset_group);
        editGroup = (Button) findViewById(R.id.edit_group);
        deleteGroup = (Button) findViewById(R.id.delete_group);


        resetGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gnameEdit.setText(oriGroup.gname);
                gcategorySpinner.setSelection(oriGroup.gcid - 1);

                for(int i = 0; i < friendList.size(); i++) {
                    if(fidSet.contains(friendList.get(i).fid)){
                        checkBoxs.get(i).setChecked(true);
                    }else{
                        checkBoxs.get(i).setChecked(false);
                    }

                }

            }
        });

        editGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get group name
                String gname = gnameEdit.getText().toString();
                if(gname == null || gname.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please enter group name.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!oriGroup.gname.equals(gname) && MainActivity.mgr.getGidByGname(gname)!=-1){
                    Toast.makeText(getApplicationContext(),"Group name already exist!",Toast.LENGTH_SHORT).show();
                    return;
                }
                //get group category id
                int gcid = MainActivity.mgr.getGcidByGcname(gcategorySpinner.getSelectedItem().toString());

                //validate friends number
                List<Integer> newFids = new ArrayList<Integer>();
                for(int i=0; i<checkBoxs.size(); i++){
                    if(checkBoxs.get(i).isChecked()){
                        newFids.add(MainActivity.mgr.getFidByFname(checkBoxs.get(i).getText().toString()));
                    }
                }
                if(newFids.size() == 0){
                    Toast.makeText(getApplicationContext(), "Please select at least one friend.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //update group
                int ok = MainActivity.mgr.editGroup(new Group(fromGroupId, gname, gcid));
                if(ok != 1){
                    Toast.makeText(getApplicationContext(), "Edit group failed for unknown reason.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //add all new friends
                for(int fid : newFids){
                    if(!fidSet.contains(fid)) {
                        MainActivity.mgr.addFriendToGroup(fid, fromGroupId);
                    }else{
                        fidSet.remove(fid);
                    }
                }

                //delete friends
                MainActivity.mgr.deleteAllFriendInGroup(fromGroupId, fidSet);

                Toast.makeText(getApplicationContext(), "Edit group "+gname+" success!", Toast.LENGTH_SHORT).show();

                //finish activity
                HashMap<String, Object> groupMap = new HashMap<String, Object>();
                groupMap.put("gid",fromGroupId);
                groupMap.put("gname",gname);
                Intent i = new Intent();
                i.putExtra("groupMap", groupMap);
                EditGroupActivity.this.setResult(RESULT_OK, i);
                EditGroupActivity.this.finish();
            }
        });

        deleteGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if(!settledUp){
                    Toast.makeText(getApplicationContext(), "Group not settled up, can not delete group.", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                new AlertDialog.Builder(EditGroupActivity.this).setTitle("Danger!")
                        .setMessage("Are you sure you want to delete this group?")
                        .setIcon(R.drawable.ic_launcher)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(getApplicationContext(), "Function not implemented yet :)", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        }).show();
            }

        });

    }


}
