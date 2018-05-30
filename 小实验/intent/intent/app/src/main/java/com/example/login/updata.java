package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class updata extends Activity {
    private Button mReturnButton;
    EditText etname,etpwd,etpwd1;
    Button btLogin,btReg;
    DateBaseHelper dbHelper;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.updata);
        etname = (EditText) findViewById(R.id.editText1);
        etpwd = (EditText) findViewById(R.id.editText2);
        etpwd1 = (EditText) findViewById(R.id.editText3);
        btLogin = (Button) findViewById(R.id.btLogin);
        btReg = (Button) findViewById(R.id.btReg);
        mReturnButton = (Button)findViewById(R.id.btReg);
        ImageView image = (ImageView) findViewById(R.id.logo);
        image.setImageResource(R.drawable.t2);

        btReg.setOnClickListener(new OnClickListener() {///注册按钮
            @Override
            public void onClick(View v) {

                if (isUserNameAndPwdValid()) {
                    String name = etname.getText().toString().trim();
                    String pwd = etpwd.getText().toString().trim();
                    String pwd1 = etpwd1.getText().toString().trim();
                    if(pwd.equals(pwd1)==false){
                        Toast.makeText(getApplicationContext(), "两次密码不一致，请重新输入！", Toast.LENGTH_SHORT).show();
                        return ;
                    }
                    else
                        dbHelper = new DateBaseHelper(getBaseContext());
                    db = dbHelper.getWritableDatabase();
                    ContentValues cv = new ContentValues();
                    cv.put(dbHelper.Name, name);
                    cv.put(dbHelper.Pwd, pwd);
                    db.insert(dbHelper.TableName, null, cv);
//                    User.dbUpdatePassword(name,pwd);
                    Toast.makeText(getApplicationContext(), "修改成功！", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(updata.this,User.class) ;
                    startActivity(intent);
                    finish();
                }

            }
        });
    }


    public boolean isUserNameAndPwdValid() {
        if (etname.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.account_empty), Toast.LENGTH_SHORT).show();
            return false;
        } else if (etpwd.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.pwd_empty), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    public void toUser(View view) {
        Intent intent5 = new Intent(updata.this,User.class) ;
        startActivity(intent5);
        finish();
    }


}