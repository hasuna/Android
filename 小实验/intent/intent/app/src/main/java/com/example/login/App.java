package com.example.login;

/**
 * Created by 王梓豪 on 2017/12/20.
 */

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class App extends Activity {
    private static final String TAG="App";
    ListView listView;
    ListAdapter adapter;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lianxiren);
        LinearLayout linearLayout=new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams param=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);

        listView=new ListView(this);

        linearLayout.addView(listView,param);

        this.setContentView(linearLayout);

        //从数据库获取联系人姓名和电话号码
        Cursor cur=this.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, null,null,null);
        adapter=new SimpleCursorAdapter(this,android.R.layout.simple_list_item_2,cur,new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,ContactsContract.CommonDataKinds.Phone.NUMBER},new int[]{android.R.id.text1,android.R.id.text2});
        this.startManagingCursor(cur);
        listView.setAdapter(adapter);
        listView.setEmptyView(findViewById(R.id.contacts_view));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                // TODO Auto-generated method stub
			String[] names=((CursorWrapper)listView.getItemAtPosition(position)).getColumnNames();
                //从指针的封装类中获得选中项的电话号码并拨号
                CursorWrapper wrapper=(CursorWrapper)listView.getItemAtPosition(position);
                int columnIndex=wrapper.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                if(!wrapper.isNull(columnIndex)){
                    String number=wrapper.getString(columnIndex);
                    Log.d(TAG,"number="+number);
                    //				//判断电话号码的有效性
                    if(PhoneNumberUtils.isGlobalPhoneNumber(number)){
                        Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel://"+ number));
                        startActivity(intent);
                    }
                }
            }
        });
    }
    public void ret(View view) {
        Intent intent5 = new Intent(App.this,User.class) ;
        startActivity(intent5);
        finish();
    }
}
