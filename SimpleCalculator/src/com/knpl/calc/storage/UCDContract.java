package com.knpl.calc.storage;

import android.provider.BaseColumns;

public final class UCDContract {
	
	public UCDContract() {
	}
	
	public static abstract class UCDColumns implements BaseColumns {
		public static final String TABLE_NAME = "table_ucds";
		public static final String COLUMN_NAME_UCD_ID = "ucd_id";
		public static final String COLUMN_NAME_UCD_DESC = "ucd_desc";
	}
}