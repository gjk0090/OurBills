package com.fff.gjk.ourbills.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.fff.gjk.ourbills.R;
import com.fff.gjk.ourbills.bean.Friend;
import com.fff.gjk.ourbills.util.Constants;
import com.fff.gjk.ourbills.util.TestDBHelper;

public class EditFriendActivity extends Activity {

    private int fid;
    private Friend oriFriend;

    private ImageView img;
    private EditText editName, editEmail;
    private RadioGroup radioGender;
    private RadioButton radioMale, radioFemale;
    private Button resetFriend, editFriend, removeFriend;

    private String gender = "male";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_friend);

        Bundle b = this.getIntent().getExtras();
        fid = (Integer) b.get("fid");

        oriFriend = MainActivity.mgr.getFriendById(fid);

        img=(ImageView) findViewById(R.id.edit_friend_img);

        editName = (EditText) findViewById(R.id.edit_friend_name);
        editName.setText(oriFriend.fname);
        editEmail = (EditText) findViewById(R.id.edit_friend_email);
        editEmail.setText(oriFriend.email);

        radioMale = (RadioButton) findViewById(R.id.edit_friend_male);
        radioFemale = (RadioButton) findViewById(R.id.edit_friend_female);

        if (oriFriend.gender.equals("male")){
            gender="male";
            radioMale.setChecked(true);
            img.setImageResource(R.drawable.male);
        }else {
            gender="female";
            radioFemale.setChecked(true);
            img.setImageResource(R.drawable.female);
        }


        editFriend=(Button) findViewById(R.id.edit_friend);
        resetFriend=(Button) findViewById(R.id.reset_friend);
        removeFriend=(Button) findViewById(R.id.remove_friend);

        editFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = editName.getText().toString();
                if(name == null || name.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please enter friend name.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!oriFriend.fname.equals(name) && MainActivity.mgr.getFidByFname(name)!=-1){
                    Toast.makeText(getApplicationContext(),"Friend name already exist!",Toast.LENGTH_SHORT).show();
                    return;
                }

                String email = editEmail.getText().toString();

                Friend newFriend = new Friend(name, gender, email);
                newFriend.fid = fid;

                if(newFriend.equals(oriFriend)){
                    Toast.makeText(EditFriendActivity.this, "Nothing changed!", Toast.LENGTH_SHORT).show();
                    return;
                }

                int tempId = MainActivity.mgr.getFidByFname(name);
                if(tempId==-1||tempId==fid){

                    MainActivity.mgr.editFriend(newFriend);
                    Toast.makeText(getApplicationContext(), "Edit friend "+name+" success!", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent();
                    EditFriendActivity.this.setResult(RESULT_OK, i);
                    EditFriendActivity.this.finish();
                }else{
                    Toast.makeText(EditFriendActivity.this, "Friend name exist!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        resetFriend.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                editName.setText(oriFriend.fname);
                editEmail.setText(oriFriend.email);
                if (oriFriend.gender.equals("male")){
                    gender="male";
                    radioMale.setChecked(true);
                }else {
                    gender="female";
                    radioFemale.setChecked(true);
                }
            }
        });

        removeFriend.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //check if settled up
                double sum = MainActivity.mgr.getTotalBalanceByFid(fid);
                if (sum > Constants.settledThreshold || sum < -Constants.settledThreshold){
                    Toast.makeText(getApplicationContext(), "Friend not settle up, can not delete friend.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //confirming dialog
                new AlertDialog.Builder(EditFriendActivity.this).setTitle("Danger!")
                        .setMessage("Are you sure you want to delete this friend?")
                        .setIcon(R.drawable.ic_launcher)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(EditFriendActivity.this, "Function not implemented yet :)", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        }).show();
            }
        });

        //radioGroup
        radioGender=(RadioGroup)findViewById(R.id.edit_friend_gender);

        radioGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.edit_friend_male:
                        //Toast.makeText(Add_Friend.this,"male",Toast.LENGTH_SHORT).show();
                        img.setImageResource(R.drawable.male);
                        gender="male";
                        break;
                    case R.id.edit_friend_female:
                        //Toast.makeText(Add_Friend.this,"female",Toast.LENGTH_SHORT).show();
                        img.setImageResource(R.drawable.female);
                        gender="female";
                        break;
                    default:
                        Toast.makeText(EditFriendActivity.this,"bug",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.edit_friend, menu);
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
