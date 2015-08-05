package com.Androitanz.Provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * The provider creation,fetching and displaying
 * 
 * @author Anitha
 * 
 */
public class MimeProvider extends ContentProvider {
	/**
	 * The instance sqlitedatabase
	 */
	private SQLiteDatabase sqlDB;
	/**
	 * The instance for DatabaseHelper
	 */
	private DatabaseHelper dbHelper;
	/**
	 * Database name for MIME app
	 */
	private static final String DATABASE_NAME = "Friends.db";
	/**
	 * Database version that we are using
	 */

	private static final int DATABASE_VERSION = 1;
	/**
	 * The table name using to store the information
	 */

	private static final String TABLE_NAME = "Friends";

	private static final String TAG = "AndrointanZProvider";

	/**
	 * 
	 * @author Lukshanu
	 * 
	 */
	public static class ProviderDetails implements BaseColumns {
		public static final String AUTHORITY = "com.Androitanz.Provider.MimeProvider";

		// BaseColumn contains _id.

		public final static Uri CONTENT_URI = Uri
				.parse("content://com.Androitanz.Provider.MimeProvider");

		// Table column
		public static final String FRIENDS_NAME = "FRIENDS_NAME";
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// create table to store user names
			db.execSQL("Create table "
					+ TABLE_NAME
					+ "( _id INTEGER PRIMARY KEY AUTOINCREMENT, FRIENDS_NAME TEXT);");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(db);
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub

		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues contentValues) {
		// get database to insert records
		sqlDB = dbHelper.getWritableDatabase();
		// insert record in user table and get the row number of recently
		long rowId = sqlDB.insert(TABLE_NAME, "", contentValues);
		if (rowId > 0) {
			Uri rowUri = ContentUris.appendId(
					ProviderDetails.CONTENT_URI.buildUpon(), rowId).build();
			getContext().getContentResolver().notifyChange(rowUri, null);
			return rowUri;
		}
		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public boolean onCreate() {
		dbHelper = new DatabaseHelper(getContext());
		return (dbHelper == null) ? false : true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		qb.setTables(TABLE_NAME);
		Cursor c = qb.query(db, projection, selection, null, null, null,
				sortOrder);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}