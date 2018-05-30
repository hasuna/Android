package com.example.lenovo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {
    String te,s;
    EditText editText;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_layout);
        Intent intent=getIntent();
        te= intent.getStringExtra("editText").toString().trim();
        editText= (EditText) findViewById(R.id.edit);
        editText.setText(te);
        Button button=(Button)findViewById(R.id.button);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
         s=editText.getText().toString();
        switch(view.getId()){
            case R.id.button:
                Intent intent=new Intent();
                intent.putExtra("return_data",s);
                setResult(2,intent);
                finish();
                break;
            default:
                break;
        }
    }
}
