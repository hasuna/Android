package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.CheckBox;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends Activity {
    private Button mReturnButton;
    EditText etname,etpwd;
    Button btLogin,btReg;
    DateBaseHelper dbHelper;
    SQLiteDatabase db;
    private SharedPreferences pref;
    private CheckBox rememberPass;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            etname = (EditText) findViewById(R.id.editText1);
            etpwd = (EditText) findViewById(R.id.editText2);
            btLogin = (Button) findViewById(R.id.btLogin);
            btReg = (Button) findViewById(R.id.btReg);
            mReturnButton = (Button)findViewById(R.id.btReg) ;
            ImageView image = (ImageView) findViewById(R.id.logo);
            image.setImageResource(R.drawable.tp);

            pref = PreferenceManager.getDefaultSharedPreferences(this);
            rememberPass = (CheckBox) findViewById(R.id.Login_Remember);
            boolean isRemember = pref.getBoolean("remember_password", false);
             if (isRemember) {
            // 将账号和密码都设置到文本框中
                 String account = pref.getString("account", "");
                 String password = pref.getString("password", "");
                 etname.setText(account);
                 etpwd.setText(password);
            rememberPass.setChecked(true);
        }
                    btReg.setOnClickListener(new OnClickListener() {///注册按钮
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this,register.class) ;
                    startActivity(intent);
                    finish();

                }
            });
            btLogin.setOnClickListener(new OnClickListener() {///登录按钮

                @Override
                public void onClick(View v) {
                    if (isUserNameAndPwdValid()){
                    String name = etname.getText().toString().trim();
                    String pwd = etpwd.getText().toString().trim();
                        editor = pref.edit();
                        if (rememberPass.isChecked()) { // 检查复选框是否被选中
                            editor.putBoolean("remember_password", true);
                            editor.putString("account", name);
                            editor.putString("password", pwd);
                            File file = new File("data/data/com.example.login/info.txt");
                            try {

                                FileOutputStream fos = new FileOutputStream(file);
                                //写入用户名和密码，以name##passwd的格式写入
                                fos.write((name + "##" + pwd).getBytes());
                                //关闭输出流
                                fos.close();
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } else {
                            editor.clear();
                        }
                        editor.apply();
                    dbHelper = new DateBaseHelper(getBaseContext());
                    db = dbHelper.getReadableDatabase();
                    Cursor c = db.query(dbHelper.TableName, new String[]{dbHelper.Name, dbHelper.Pwd}, dbHelper.Name + "=?  and " + dbHelper.Pwd + "=?", new String[]{name, pwd}, null, null, null);
                    if (c.getCount() > 0) {

                        Intent intent = new Intent(MainActivity.this,User.class) ;
                        startActivity(intent);
                        finish();
                        Toast.makeText(getApplicationContext(), "登录成功！", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "登陆失败！请输入正确的用户名或密码", Toast.LENGTH_SHORT).show();

                    }

                }
                }
            });

        }


    public boolean isUserNameAndPwdValid() {
        if (etname.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.account_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (etpwd.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.pwd_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}