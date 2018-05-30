package com.example.login;

/**
 * Created by 王梓豪 on 2017/12/5.
 */


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class User extends AppCompatActivity {
    private Button mReturnButton;
    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);
        mReturnButton = (Button)findViewById(R.id.returnback);
    }
    public void back_to_login(View view) {
        Intent intent3 = new Intent(User.this,MainActivity.class) ;
        startActivity(intent3);
        finish();
    }
    public void tonext(View view) {
        Intent intent4 = new Intent(User.this,lianxiren.class) ;
        startActivity(intent4);
        finish();
    }
    public void toupdata(View view) {
        Intent intent4 = new Intent(User.this,updata.class) ;
        startActivity(intent4);
        finish();
    }

}

