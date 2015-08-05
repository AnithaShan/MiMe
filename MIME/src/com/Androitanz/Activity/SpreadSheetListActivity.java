package com.Androitanz.Activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.widget.TextView;

import com.Androitanz.db.DatabaseOperation;
import com.Androitanz.utils.Constants;
import com.androintanz.authentication.Authentication;
import com.androintanz.authentication.Login;
import com.pras.SpreadSheet;
import com.pras.SpreadSheetFactory;

/**
 * The Spreedsheets are retreived from the selected account and matches with the
 * selected spread sheet
 * 
 * @author Anitha
 * 
 */
public class SpreadSheetListActivity extends Activity {
	ArrayList<SpreadSheet> spreadSheets;
	TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tv = new TextView(this.getApplicationContext());

		// Init and Read SpreadSheet list from Google Server
		runOnUiThread(new Runnable() {
			public void run() {
				new MyTask().execute(null);
			}
		});
	}

	private class MyTask extends AsyncTask {

		Dialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new Dialog(SpreadSheetListActivity.this);
			dialog.setTitle("Please wait");
			TextView tv = new TextView(
					SpreadSheetListActivity.this.getApplicationContext());
			tv.setText("Featching SpreadSheet list from your account...");
			dialog.setContentView(tv);
			dialog.show();
		}

		@Override
		protected Object doInBackground(Object... params) {
			// Read Spread Sheet list from the server.
			SpreadSheetFactory factory = SpreadSheetFactory
					.getInstance(new Authentication(
							SpreadSheetListActivity.this));
			spreadSheets = factory.getAllSpreadSheets();
			Looper.myLooper().prepare();
			spreadSheets = factory.getAllSpreadSheets(true, null, false);

			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			if (dialog.isShowing())
				dialog.cancel();

			if (spreadSheets == null || spreadSheets.size() == 0) {
				tv.setText("No spreadsheet exists in your account...");
				setContentView(tv);
			} else {

				// "  spreadsheets exists in your account...");

				for (int i = 0; i < spreadSheets.size(); i++) {
					Constants.IS_ROW_MATCHING = false;
					SpreadSheet sp = spreadSheets.get(i);

					if (sp.getTitle().equals(Login.getGroupId())) {
						Constants.IS_ROW_MATCHING = true;
						/*
						 * group id is matched so adding the details to login
						 * database
						 */
						DatabaseOperation.insertLoginDetails();
						/*
						 * GroupId matched,so calling activity to display the
						 * worksheet list
						 */
						Intent intent = new Intent(
								SpreadSheetListActivity.this,
								SpreadSheetDetailsActivity.class);
						intent.putExtra("sp_id", i);

						startActivity(intent);
					}

				}
				if (!Constants.IS_ROW_MATCHING) {
					tv.setText("No spreadsheet exists in your account...");
					setContentView(tv);
				}
			}
		}
	}

}
