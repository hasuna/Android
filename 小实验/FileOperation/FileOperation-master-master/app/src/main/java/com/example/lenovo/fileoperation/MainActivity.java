package com.example.lenovo.fileoperation;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText text1,text2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        Button button= (Button) findViewById(R.id.button);
        Button button1=(Button) findViewById(R.id.restore_button);
         text1=(EditText) findViewById(R.id.text1);
         text2= (EditText) findViewById(R.id.text2);
        button.setOnClickListener(this);
        button1.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button:
                String name=text2.getText().toString();
                String number=text1.getText().toString();
               /* SharedPreferences.Editor editor
                        =getSharedPreferences("data",MODE_PRIVATE).edit();
                editor.putString("学号",number);
                editor.putString("姓名",name);
                editor.apply(); */
                Save(name,number);
                break;
            case R.id.restore_button:
              /*  SharedPreferences pref
                        =getSharedPreferences("data",MODE_PRIVATE);
                String names=pref.getString("姓名","无");
                String numbers=pref.getString("学号","无");
                String s="姓名："+names+"\n学号："+numbers;
                Toast.makeText(MainActivity.this,s,Toast.LENGTH_SHORT).show();
                */
                String s=read();
                Toast.makeText(MainActivity.this,s,Toast.LENGTH_SHORT).show();
            default:
                break;
        }
    }

    private String  read() {
        FileInputStream in=null;
        BufferedReader reader=null;
        StringBuilder content=new StringBuilder();
        try{
            in=openFileInput("data");
            reader=new BufferedReader(new InputStreamReader(in));
            String line="";
            while ((line=reader.readLine())!=null){
                content.append(line+"   ");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return  content.toString();
    }

    private void Save(String name,String number) {
        FileOutputStream out=null;
        BufferedWriter writer=null;
        try{
            out=openFileOutput("data", Context.MODE_PRIVATE);
            writer=new BufferedWriter(new OutputStreamWriter(out));
            writer.write("学号："+number+"\n姓名："+name);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                if(writer!=null){
                    writer.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }


}
