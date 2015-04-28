package com.knpl.calc.storage;


import com.knpl.calc.storage.UCDContract.UCDColumns;
import com.knpl.calc.storage.UFDContract.UFDColumns;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CalculatorDbHelper extends SQLiteOpenHelper {
	
	private static final String SQL_CREATE_UFDS =
			"CREATE TABLE " + UFDColumns.TABLE_NAME + " (" +
			UFDColumns._ID + " INTEGER PRIMARY KEY, " +
			UFDColumns.COLUMN_NAME_UFD_ID + " TEXT, " + 
			UFDColumns.COLUMN_NAME_UFD_DESC + " TEXT )";
	
	private static final String SQL_DELETE_UFDS =
			"DROP TABLE IF EXISTS " + UFDColumns.TABLE_NAME;
	
	private static final String SQL_CREATE_UCDS =
			"CREATE TABLE " + UCDColumns.TABLE_NAME + " (" +
			UCDColumns._ID + " INTEGER PRIMARY KEY, " +
			UCDColumns.COLUMN_NAME_UCD_ID + " TEXT, " + 
			UCDColumns.COLUMN_NAME_UCD_DESC + " TEXT )";
	
	private static final String SQL_DELETE_UCDS =
			"DROP TABLE IF EXISTS " + UCDColumns.TABLE_NAME;
	
	public static final String DATABASE_NAME = "UserFuncDefs.db";
	public static final int DATABASE_VERSION = 7;
	
	public CalculatorDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_UFDS);
		db.execSQL(SQL_CREATE_UCDS);
		android.util.Log.d("mytag", "DB created!");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		android.util.Log.d("mytag", "DB upgrade! " + oldVersion + " -> " + newVersion);
		db.execSQL(SQL_DELETE_UCDS);
		db.execSQL(SQL_DELETE_UFDS);
		onCreate(db);
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}
}
