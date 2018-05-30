package com.example.login;

/**
 * Created by 王梓豪 on 2017/12/6.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DateBaseHelper extends SQLiteOpenHelper {
    public final static	String DBName="MyDate.db";
    public final static	String TableName="user";
    public static	String Name="name";
    public  final  static	String Pwd="pwd";


    public DateBaseHelper(Context context) {
        super(context, DBName, null, 1);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="create Table "+TableName+"("+"ID integer primary key autoincrement,"+Name+" text,"+Pwd+" text)";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

}