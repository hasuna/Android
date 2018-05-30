package com.example.vhjksry.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    private String[] data={"wsj,1506","2015011xxx，男"};
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayAdapter<String> adapter =new ArrayAdapter<String>(
              MainActivity.this,android.R.layout.simple_list_item_1,data);
        ListView listview =(ListView) findViewById(R.id.list_view);
        listview.setAdapter(adapter);

    }
}
