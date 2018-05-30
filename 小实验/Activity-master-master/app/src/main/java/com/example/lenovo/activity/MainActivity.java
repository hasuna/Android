package com.example.lenovo.activity;

import android.app.Activity;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_layout);
        Button button= (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
}

    @Override
    public void onClick(View view) {
        editText= (EditText) findViewById(R.id.edit);
        String ed=editText.getText().toString();
        switch (view.getId()) {
            case R.id.button:
                Intent intent= new Intent(MainActivity.this,SecondActivity.class);
                intent.putExtra("editText",ed);
                startActivityForResult(intent,1);
                break;
            default:
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
       super.onActivityResult(requestCode,resultCode,data);
          if (resultCode == 2&&requestCode==1) {
              String returnedData = data.getStringExtra("return_data");
              editText.setText(returnedData);
          }

    }

}
