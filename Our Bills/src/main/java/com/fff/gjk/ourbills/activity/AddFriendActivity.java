package com.fff.gjk.ourbills.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.fff.gjk.ourbills.bean.Friend;
import com.fff.gjk.ourbills.R;

public class AddFriendActivity extends Activity {

    private ImageView img;
    private EditText editName, editEmail;
    private RadioGroup radioGender;
    private String gender = "male";

    private Button submitFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        img=(ImageView) findViewById(R.id.add_friend_img);

        editName = (EditText) findViewById(R.id.add_friend_name);
        editEmail = (EditText) findViewById(R.id.add_friend_email);


        submitFriend=(Button) findViewById(R.id.submit_friend);
        submitFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean ok = false;

                String name = editName.getText().toString();
                if(name == null || name.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please enter friend name.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(MainActivity.mgr.getFidByFname(name)==-1){ok = true;}

                String email = editEmail.getText().toString();

                if(ok) {
                    MainActivity.mgr.addFriend(new Friend(name, gender, email));
                    Toast.makeText(getApplicationContext(), "Add friend "+name+" success!", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent();
                    AddFriendActivity.this.setResult(RESULT_OK, i);
                    AddFriendActivity.this.finish();
                }else{
                    Toast.makeText(AddFriendActivity.this,"Friend already exist!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //radioGroup
        radioGender=(RadioGroup)findViewById(R.id.add_friend_gender);

        radioGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.add_friend_male:
                        //Toast.makeText(Add_Friend.this,"male",Toast.LENGTH_SHORT).show();
                        img.setImageResource(R.drawable.male);
                        gender="male";
                        break;
                    case R.id.add_friend_female:
                        //Toast.makeText(Add_Friend.this,"female",Toast.LENGTH_SHORT).show();
                        img.setImageResource(R.drawable.female);
                        gender="female";
                        break;
                    default:
                        Toast.makeText(AddFriendActivity.this,"bug",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.add_friend, menu);
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
