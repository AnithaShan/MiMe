package com.Androitanz.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	// Database Version
	private static final int DATABASE_VERSION = 1;
	// Database Name
	public static final String DATABASE_NAME = "MIME";
	// table name
	public static final String TABLE_NAME = "loginTable";

	// Table Columns names
	public static final String KEY_ID = "id";

	public static final String KEY_NAME = "GroupName";

	public static final String KEY_MAIL = "MailId";

	private static Context con;

	static SQLiteDatabase db;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		con = context;
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_SONGS_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID
				+ " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT," + KEY_MAIL
				+ " TEXT " + " )";

		db.execSQL(CREATE_SONGS_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// // Drop older table if existed
		// db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

		// Create tables again
		// onCreate(db);

	}

}
