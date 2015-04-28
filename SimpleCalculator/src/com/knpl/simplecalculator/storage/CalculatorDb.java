package com.knpl.simplecalculator.storage;


import com.knpl.simplecalculator.CalculatorApplication;
import com.knpl.simplecalculator.nodes.UserConstDef;
import com.knpl.simplecalculator.nodes.UserFuncDef;
import com.knpl.simplecalculator.storage.UCDContract.UCDColumns;
import com.knpl.simplecalculator.storage.UFDContract.UFDColumns;
import com.knpl.simplecalculator.util.Globals;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CalculatorDb {

	public static boolean insertUFD(UserFuncDef def) {
		try {
			Context context = CalculatorApplication.getCalculatorApplicationContext();
			CalculatorDbHelper dbHelper = new CalculatorDbHelper(context);
			
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			
			ContentValues values = new ContentValues();
			values.put(UFDColumns.COLUMN_NAME_UFD_ID, def.getSignature().getName());
			values.put(UFDColumns.COLUMN_NAME_UFD_DESC, def.getSource());
			
			db.insert(UFDColumns.TABLE_NAME, null, values);
			return true;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
	public static boolean insertUCD(UserConstDef def) {
		try {
			Context context = CalculatorApplication.getCalculatorApplicationContext();
			CalculatorDbHelper dbHelper = new CalculatorDbHelper(context);
			
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			
			ContentValues values = new ContentValues();
			values.put(UCDColumns.COLUMN_NAME_UCD_ID, def.getName());
			values.put(UCDColumns.COLUMN_NAME_UCD_DESC, def.getSource());
			
			db.insert(UCDColumns.TABLE_NAME, null, values);
			return true;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
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
	
	public static void loadUserDefs() {
		Context context = CalculatorApplication.getCalculatorApplicationContext();
		CalculatorDbHelper dbHelper = new CalculatorDbHelper(context);
		Globals defs = Globals.getInstance();
		
		String[] projection;
		SQLiteDatabase db;
		Cursor cursor;
		
		// Load all user defined functions.
		projection = new String[]{UFDColumns.COLUMN_NAME_UFD_DESC};
		db = dbHelper.getReadableDatabase();
		cursor = db.query(UFDColumns.TABLE_NAME, 
						  projection, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			try {
				String description = cursor.getString(
						cursor.getColumnIndexOrThrow(UFDColumns.COLUMN_NAME_UFD_DESC));
				defs.putUserFuncDef(UserFuncDef.fromSource(description));
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
			cursor.moveToNext();
		}
		
		// Load all user defined constants.
		projection = new String[]{UCDColumns.COLUMN_NAME_UCD_DESC};
		db = dbHelper.getReadableDatabase();
		cursor = db.query(UCDColumns.TABLE_NAME, 
						  projection, null, null, null, null, null);
		
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			try {
				String description = cursor.getString(
						cursor.getColumnIndexOrThrow(UCDColumns.COLUMN_NAME_UCD_DESC));
				defs.putUserConstDef(UserConstDef.fromSource(description));
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
			cursor.moveToNext();
		}
		
		// Completely remove all user definitions that can't be resolved. 
		for (UserFuncDef ufd : defs.getUserFuncDefs()) {
			try {
				ufd.resolve();
			}
			catch (Exception ex) {
				String name = ufd.getSignature().getName();
				defs.removeUserFuncDef(name);
				deleteUFD(name);
			}
		}
		
		for (UserConstDef ucd : defs.getUserConstDefs()) {
			try {
				ucd.resolve();
			}
			catch (Exception ex) {
				String name = ucd.getName();
				defs.removeUserFuncDef(name);
				deleteUFD(name);
			}
		}	
	}
	
//	public static void putAllUFDs() {
//		Context context = CalculatorApplication.getCalculatorApplicationContext();
//		CalculatorDbHelper dbHelper = new CalculatorDbHelper(context);
//		
//		String[] projection = {UFDColumns.COLUMN_NAME_UFD_DESC};
//		SQLiteDatabase db = dbHelper.getReadableDatabase();
//		Cursor cursor = db.query(
//				 UFDColumns.TABLE_NAME, 
//				 projection, null, null, null, null, null);
//		
//		Globals defs = Globals.getInstance();
//		cursor.moveToFirst();
//		while (!cursor.isAfterLast()) {
//			try {
//				String description = cursor.getString(
//						cursor.getColumnIndexOrThrow(UFDColumns.COLUMN_NAME_UFD_DESC));
//				
//				defs.putUserFuncDef(UserFuncDef.fromSource(description));
//			}
//			catch (Exception ex) {
//				ex.printStackTrace();
//			}
//			cursor.moveToNext();
//		}
//	}
//	
//	public static void putAllUCDs() {
//		Context context = CalculatorApplication.getCalculatorApplicationContext();
//		CalculatorDbHelper dbHelper = new CalculatorDbHelper(context);
//		
//		String[] projection = {UCDColumns.COLUMN_NAME_UCD_DESC};
//		SQLiteDatabase db = dbHelper.getReadableDatabase();
//		Cursor cursor = db.query(
//				 UCDColumns.TABLE_NAME, 
//				 projection, null, null, null, null, null);
//		
//		Globals defs = Globals.getInstance();
//		cursor.moveToFirst();
//		while (!cursor.isAfterLast()) {
//			try {
//				String description = cursor.getString(
//						cursor.getColumnIndexOrThrow(UCDColumns.COLUMN_NAME_UCD_DESC));
//				defs.putUserConstDef(UserConstDef.fromSource(description));
//			}
//			catch (Exception ex) {
//				ex.printStackTrace();
//			}
//			cursor.moveToNext();
//		}
//	}
	
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
