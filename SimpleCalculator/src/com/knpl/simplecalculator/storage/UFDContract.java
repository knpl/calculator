package com.knpl.simplecalculator.storage;

import android.provider.BaseColumns;

public final class UFDContract {
	
	public UFDContract() {
	}
	
	public static abstract class UFDColumns implements BaseColumns {
		public static final String TABLE_NAME = "table_ufds";
		public static final String COLUMN_NAME_UFD_ID = "ufd_id";
		public static final String COLUMN_NAME_UFD_DESC = "ufd_desc";
	}
}
