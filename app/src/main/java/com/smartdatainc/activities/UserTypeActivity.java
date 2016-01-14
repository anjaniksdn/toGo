package com.smartdatainc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.smartdatainc.toGo.R;
import com.smartdatainc.utils.Constants;

public class UserTypeActivity extends AppActivity implements OnClickListener{
    // RadioGroup selectusertype;
    // RadioButton selectuser;
    // Button submitusertype;
    Button interpreterbutton,userbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uer_type);
        //selectusertype = (RadioGroup)findViewById(R.id.selectusertype);
        //submitusertype = (Button)findViewById(R.id.submitusertype);

        interpreterbutton =  (Button)findViewById(R.id.interpreterbutton);
        userbutton =  (Button)findViewById(R.id.userbutton);
        interpreterbutton.setOnClickListener(this);
        userbutton.setOnClickListener(this);
        setActionBar(Constants.APPHEADER);
    }



    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id)
        {
            case R.id.interpreterbutton:
               /* int selectedId = selectusertype.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                selectuser = (RadioButton) findViewById(selectedId);*/
                Intent intentObj = new Intent(UserTypeActivity.this, LoginActivity.class);
                Bundle bundle =  new Bundle();
                bundle.putString("usertype", "interpreter");
                intentObj.putExtra("userbundle", bundle);
                startActivity(intentObj);
                UserTypeActivity.this.finish();
                /*Toast.makeText(UserTypeActivity.this,
                        " Interpreter", Toast.LENGTH_SHORT).show();*/
                break;
            case R.id.userbutton:
               /* int selectedId = selectusertype.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                selectuser = (RadioButton) findViewById(selectedId);*/
                Intent intentObjuser = new Intent(UserTypeActivity.this, LoginActivity.class);
                Bundle userbundle =  new Bundle();
                userbundle.putString("usertype","user");
                intentObjuser.putExtra("userbundle",userbundle);
                startActivity(intentObjuser);
                UserTypeActivity.this.finish();
               /* Toast.makeText(UserTypeActivity.this,
                        "userbutton", Toast.LENGTH_SHORT).show();*/
                break;
        }


    }
}
