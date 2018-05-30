package com.example.login;

/**
 * Created by 王梓豪 on 2017/12/6.
 */

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.util.Log;
import android.telephony.PhoneNumberUtils;
import java.util.ArrayList;
import java.util.List;
import android.net.Uri;

public class lianxiren extends AppCompatActivity {
    private Button mReturnButton;
    ListAdapter adapter;
    List<String> contactsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lianxiren );
        ListView contactsView = (ListView) findViewById(R.id.contacts_view);

        adapter = new ArrayAdapter<String>(this, android.R.layout. simple_list_item_1, contactsList);
        contactsView.setAdapter(adapter);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.READ_CONTACTS }, 1);
        } else {
            readContacts();
                    contactsView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                        ListView contactsView = (ListView) findViewById(R.id.contacts_view);
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                // TODO Auto-generated method stub
                String[] names=((CursorWrapper)contactsView.getItemAtPosition(position)).getColumnNames();
                //从指针的封装类中获得选中项的电话号码并拨号
                CursorWrapper wrapper=(CursorWrapper)contactsView.getItemAtPosition(position);
                int columnIndex=wrapper.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                if(!wrapper.isNull(columnIndex)){
                    String number=wrapper.getString(columnIndex);
                    Log.d("","number="+number);
                    if(PhoneNumberUtils.isGlobalPhoneNumber(number)){
                        Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel://"+ number));
                        startActivity(intent);
                    }
                }
            }
        });
        }
        mReturnButton = (Button)findViewById(R.id.returnto);
    }

    private void readContacts() {
        ListView contactsView = (ListView) findViewById(R.id.contacts_view);
        Cursor cur=this.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, null,null,null);
        adapter=new SimpleCursorAdapter(this,android.R.layout.simple_list_item_2,cur,new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER},new int[]{android.R.id.text1,android.R.id.text2});
        this.startManagingCursor(cur);
        contactsView.setAdapter(adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    readContacts();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
    public void ret(View view) {
        Intent intent5 = new Intent(lianxiren.this,User.class) ;
        startActivity(intent5);
        finish();
    }
}
