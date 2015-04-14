package com.knpl.simplecalculator.storage;


import com.knpl.simplecalculator.CalculatorApplication;
import com.knpl.simplecalculator.nodes.ConstDefNode;
import com.knpl.simplecalculator.nodes.FuncDefNode;
import com.knpl.simplecalculator.nodes.UserConstDef;
import com.knpl.simplecalculator.nodes.UserFuncDef;
import com.knpl.simplecalculator.parser.Lexer;
import com.knpl.simplecalculator.parser.Parser;
import com.knpl.simplecalculator.storage.UCDContract.UCDColumns;
import com.knpl.simplecalculator.storage.UFDContract.UFDColumns;
import com.knpl.simplecalculator.util.Globals;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CalculatorDb {

	public static void insertUFD(UserFuncDef def) {
		Context context = CalculatorApplication.getCalculatorApplicationContext();
		CalculatorDbHelper dbHelper = new CalculatorDbHelper(context);
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(UFDColumns.COLUMN_NAME_UFD_ID, def.getSignature().getName());
		values.put(UFDColumns.COLUMN_NAME_UFD_DESC, def.getDescription());
		
		db.insert(UFDColumns.TABLE_NAME, null, values);
	}
	
	public static void insertUCD(UserConstDef def) {
		Context context = CalculatorApplication.getCalculatorApplicationContext();
		CalculatorDbHelper dbHelper = new CalculatorDbHelper(context);
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(UCDColumns.COLUMN_NAME_UCD_ID, def.getName());
		values.put(UCDColumns.COLUMN_NAME_UCD_DESC, def.getDescription());
		
		db.insert(UCDColumns.TABLE_NAME, null, values);
	}
	
	public static void deleteUFD(String oldName) {
		Context context = CalculatorApplication.getCalculatorApplicationContext();
		CalculatorDbHelper dbHelper = new CalculatorDbHelper(context);
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		db.delete(UFDColumns.TABLE_NAME, 
				  UFDColumns.COLUMN_NAME_UFD_ID + " = ?",
				  new String[] {oldName});
	}
	
	public static void deleteUCD(String oldName) {
		Context context = CalculatorApplication.getCalculatorApplicationContext();
		CalculatorDbHelper dbHelper = new CalculatorDbHelper(context);
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		db.delete(UCDColumns.TABLE_NAME, 
				  UCDColumns.COLUMN_NAME_UCD_ID + " = ?",
				  new String[] {oldName});
	}
	
	public static void putAllUFDs() {
		Context context = CalculatorApplication.getCalculatorApplicationContext();
		CalculatorDbHelper dbHelper = new CalculatorDbHelper(context);
		
		String[] projection = {UFDColumns.COLUMN_NAME_UFD_DESC};
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(
				 UFDColumns.TABLE_NAME, 
				 projection, null, null, null, null, null);
		
		Globals defs = Globals.getInstance();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			try {
				String description = cursor.getString(
						cursor.getColumnIndexOrThrow(UFDColumns.COLUMN_NAME_UFD_DESC));
				Parser parser = new Parser(new Lexer(description));
				if (parser.funcDef()) {
					defs.putUserFuncDef(
						new UserFuncDef((FuncDefNode)parser.getResult()));
				}
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
			cursor.moveToNext();
		}
	}
	
	public static void putAllUCDs() {
		Context context = CalculatorApplication.getCalculatorApplicationContext();
		CalculatorDbHelper dbHelper = new CalculatorDbHelper(context);
		
		String[] projection = {UCDColumns.COLUMN_NAME_UCD_DESC};
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(
				 UCDColumns.TABLE_NAME, 
				 projection, null, null, null, null, null);
		
		Globals defs = Globals.getInstance();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			try {
				String description = cursor.getString(
						cursor.getColumnIndexOrThrow(UCDColumns.COLUMN_NAME_UCD_DESC));
				Parser parser = new Parser(new Lexer(description));
				if (parser.constDef()) {
					defs.putUserConstDef(
						new UserConstDef((ConstDefNode)parser.getResult()));
				}
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
			cursor.moveToNext();
		}
	}
	
	public static void listUserDefs() {
		Context context = CalculatorApplication.getCalculatorApplicationContext();
		CalculatorDbHelper dbHelper = new CalculatorDbHelper(context);
		
		String[] projection = {
			UFDColumns._ID,
			UFDColumns.COLUMN_NAME_UFD_ID,
			UFDColumns.COLUMN_NAME_UFD_DESC
		};
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		Cursor cursor = db.query(
				 UFDColumns.TABLE_NAME, 
				 projection, null, null, null, null, null);
		
		long id;
		String name;
		String description;
		
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			id   = cursor.getLong(cursor.getColumnIndexOrThrow(UFDColumns._ID));
			name = cursor.getString(cursor.getColumnIndexOrThrow(UFDColumns.COLUMN_NAME_UFD_ID));
			description = cursor.getString(cursor.getColumnIndexOrThrow(UFDColumns.COLUMN_NAME_UFD_DESC));
			
			android.util.Log.d("mytag", id + " " + name + " " + description);
			
			cursor.moveToNext();
		}
		cursor.close();
		android.util.Log.d("mytag", "done");
		
		String[] projection2 = {
				UCDColumns._ID,
				UCDColumns.COLUMN_NAME_UCD_ID,
				UCDColumns.COLUMN_NAME_UCD_DESC
		};
		
		cursor = db.query(
				 UCDColumns.TABLE_NAME, 
				 projection2, null, null, null, null, null);
		
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			id   = cursor.getLong(cursor.getColumnIndexOrThrow(UCDColumns._ID));
			name = cursor.getString(cursor.getColumnIndexOrThrow(UCDColumns.COLUMN_NAME_UCD_ID));
			description = cursor.getString(cursor.getColumnIndexOrThrow(UCDColumns.COLUMN_NAME_UCD_DESC));
			
			android.util.Log.d("mytag", id + " " + name + " " + description);
			
			cursor.moveToNext();
		}
		cursor.close();
		android.util.Log.d("mytag", "done");
	}
}
