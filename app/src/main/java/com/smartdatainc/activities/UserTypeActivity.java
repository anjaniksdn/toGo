package com.smartdatainc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.smartdatainc.toGo.R;
import com.smartdatainc.utils.Constants;

public class UserTypeActivity extends AppActivity implements OnClickListener {
    TextView interpreterbutton, userbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uer_type);
        interpreterbutton = (TextView) findViewById(R.id.interpreterbutton);
        userbutton = (TextView) findViewById(R.id.userbutton);
        interpreterbutton.setOnClickListener(this);
        userbutton.setOnClickListener(this);
        setActionBar(Constants.APPHEADER);

    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.interpreterbutton:
                Intent intentObj = new Intent(UserTypeActivity.this, LoginActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("usertype", "interpreter");
                intentObj.putExtra("userbundle", bundle);
                startActivity(intentObj);
                //UserTypeActivity.this.finish();
                break;
            case R.id.userbutton:
                Intent intentObjuser = new Intent(UserTypeActivity.this, LoginActivity.class);
                Bundle userbundle = new Bundle();
                userbundle.putString("usertype", "user");
                intentObjuser.putExtra("userbundle", userbundle);
                startActivity(intentObjuser);
                //UserTypeActivity.this.finish();
                break;
        }


    }
}
