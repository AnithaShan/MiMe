package com.Androitanz.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.Androitanz.Activity.SpreadSheetListActivity;
import com.Androitanz.utils.Constants;
import com.androintanz.authentication.Login;

public class DatabaseOperation {
	static Context context;
	// Database fields
	private static SQLiteDatabase database;
	private static DatabaseHelper dbHelper;
	public static String[] allColumns = { DatabaseHelper.KEY_ID,
			DatabaseHelper.KEY_NAME, DatabaseHelper.KEY_MAIL };

	public DatabaseOperation(Context context) {
		this.context = context;
		dbHelper = new DatabaseHelper(context);
	}

	public static void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public static void close() {
		dbHelper.close();
	}

	public static void fetchingLoginDetails() {

		/*
		 * Giving permission to MIME database
		 */
		open();
		database = dbHelper.getReadableDatabase();
		// String loginDetailsQuery = "SELECT  * FROM "
		// + DatabaseHelper.TABLE_NAME + " WHERE "
		// + DatabaseHelper.KEY_NAME + " = " + " ' " + Login.getGroupId()
		// + " ' " + " AND " + DatabaseHelper.KEY_MAIL + " = " + " ' "
		// + Login.getSelectedAccountName() + " ' ";
		String loginDetailsQuery = " SELECT * FROM "
				+ DatabaseHelper.TABLE_NAME;

		Cursor cursor = database.rawQuery(loginDetailsQuery, null);
		checkingLoginDetails(cursor);
		cursor.close();
		close();
	}

	/**
	 * Fetching login details from login table in MIME databse to retrieve the
	 * spread sheet details from local database
	 * 
	 * @param db
	 */

	static void checkingLoginDetails(Cursor loginDetailsCursor) {

		/**
		 * If the application is open for the first time
		 */
		if (loginDetailsCursor.getCount() == 0) {
			syncingSpreadSheet();

		}
		loginDetailsCursor.moveToFirst();
		while (!loginDetailsCursor.isAfterLast()) {
			Constants.IS_ROW_MATCHING = false;
			System.out
					.println("checking the second time login------------------------------------------------------->"
							+ Constants.IS_ROW_MATCHING);
			if (loginDetailsCursor.getString(1).equals(Login.getGroupId())
					&& (loginDetailsCursor.getString(2).equals(Login
							.getSelectedAccountName()))) {
				// go to main menu
				// fetch from the local database
				Constants.IS_ROW_MATCHING = true;
				break;
			}
			loginDetailsCursor.moveToNext();
		}
		/**
		 * if the group name and email id is not there in the
		 * logindetailsDatabase
		 */
		if (!Constants.IS_ROW_MATCHING) {
			System.out
					.println("checking the enrerer------------------------------------------------------->"
							+ Constants.IS_ROW_MATCHING);
			Constants.IS_ROW_MATCHING = true;
			syncingSpreadSheet();
		}
	}

	/**
	 * The information will be fetched from the spread sheet to be stored in the
	 * local database
	 */
	public static void syncingSpreadSheet() {
		Intent spreadSheetIntent = new Intent(context,
				SpreadSheetListActivity.class);
		context.startActivity(spreadSheetIntent);
	}

	/**
	 * Adding login details in Logintable in MIME database
	 */
	public static void insertLoginDetails() {
		open();

		ContentValues values = new ContentValues();

		values.put(DatabaseHelper.KEY_NAME, Login.getGroupId());
		values.put(DatabaseHelper.KEY_MAIL, Login.getSelectedAccountName());
		database.insert(DatabaseHelper.TABLE_NAME, null, values);
		Log.i(Constants.DATABASE_OPERATION_TAG,
				Constants.INSERTED_DATABASE_OPERATION);
		close();
	}
}
