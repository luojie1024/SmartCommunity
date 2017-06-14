package com.way.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
/**
 * 数据库助手类
 * @author Administrator
 *
 */

public class DatebaseHelper extends SQLiteOpenHelper {

	public static final String DB_NAME ="gizdb.db";
	public static final int VERSION = 4;
	private static final String CREATE_TABLE_GIZ ="create table giz(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,address TEXT,bindgiz TEXT,userid TEXT,flag INTEGER)";
	private static final String CREATE_TABLE_ALERT ="create table alert(_id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,time TEXT,bindgiz TEXT,userid TEXT,flag INTEGER)";
	private static final String CREATE_TABLE_AIRMES="create table airmes(_id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,brand INTEGER,temperature INTEGER,mode INTEGER,speed INTEGER,direction INTEGER,bindgiz TEXT,userid TEXT,flag INTEGER)";
    private static final String CREATE_TABLE_CURTAIN="create table curtain(_id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,address TEXT,bindgiz TEXT,userid TEXT,flag INTEGER)";
    private static final String DROP_TABLE_GIZ="DROP TABLE IF EXISTS giz";
	private static final String DROP_TABLE_ALERT="DROP TABLE IF EXISTS alert";
	private static final String DROP_TABLE_AIRMES="DROP TABLE IF EXISTS airmes";
    private static final String DROP_TABLE_CURTAIN="DROP TABLE IF EXISTS curtain";


	public DatebaseHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
		// TODO Auto-generated constructor stub
	}

	//如果数据库不存在，那么会执行该方法
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		try {
		db.execSQL(CREATE_TABLE_GIZ);
		db.execSQL(CREATE_TABLE_ALERT);
		db.execSQL(CREATE_TABLE_AIRMES);
        db.execSQL(CREATE_TABLE_CURTAIN);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("==", "数据库创建失败");
		}
		
	}

	//数据库版本更新后会执行该方法
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		try {
		db.execSQL(DROP_TABLE_GIZ);
		db.execSQL(CREATE_TABLE_GIZ);
		db.execSQL(DROP_TABLE_ALERT);
		db.execSQL(CREATE_TABLE_ALERT);
		db.execSQL(DROP_TABLE_AIRMES);
		db.execSQL(CREATE_TABLE_AIRMES);
        db.execSQL(DROP_TABLE_CURTAIN);
        db.execSQL(CREATE_TABLE_CURTAIN);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("==", "数据库更新版本失败");
			
		}
		
		
	}

}
