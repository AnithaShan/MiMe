package com.Androitanz.Activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pras.SpreadSheet;
import com.pras.SpreadSheetFactory;
import com.pras.WorkSheet;

/**
 * The worksheet are retreived
 * 
 * @author Anitha
 * 
 */
public class SpreadSheetDetailsActivity extends Activity {
	ListView list;
	int spID = -1;
	ArrayList<WorkSheet> workSheets;
	TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		spID = intent.getIntExtra("sp_id", -1);

		if (spID == -1) {
			finish();
			return;
		}

		list = new ListView(this.getApplicationContext());
		tv = new TextView(this.getApplicationContext());

		new MyTask().execute(null);
	}

	private class MyTask extends AsyncTask {

		Dialog dialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog = new Dialog(SpreadSheetDetailsActivity.this);
			dialog.setTitle("Please wait");
			TextView tv = new TextView(
					SpreadSheetDetailsActivity.this.getApplicationContext());
			tv.setText("Featching SpreadSheet details...");
			dialog.setContentView(tv);
			dialog.show();
		}

		@Override
		protected Object doInBackground(Object... params) {
			// Read Spread Sheet list from the server.
			SpreadSheetFactory factory = SpreadSheetFactory.getInstance();
			// Read from local Cache
			ArrayList<SpreadSheet> sps = factory.getAllSpreadSheets(false);
			SpreadSheet sp = sps.get(spID);
			workSheets = sp.getAllWorkSheets();
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (dialog.isShowing())
				dialog.cancel();

			if (workSheets == null || workSheets.size() == 0) {
				tv.setText("No spreadsheet exists in your account...");
				setContentView(tv);
			} else {
				// tv.setText(spreadSheets.size() +
				// "  spreadsheets exists in your account...");
				ArrayAdapter<String> arrayAdaper = new ArrayAdapter<String>(
						SpreadSheetDetailsActivity.this.getApplicationContext(),
						android.R.layout.simple_list_item_1);
				for (int i = 0; i < workSheets.size(); i++) {
					WorkSheet wk = workSheets.get(i);
					arrayAdaper.add(wk.getTitle());
				}
				Log.i("Prasanta",
						"Number of entries..." + arrayAdaper.getCount());
				list.addHeaderView(tv);
				list.setAdapter(arrayAdaper);
				tv.setText("Number of WorkSheets (" + workSheets.size() + ")");

				list.setOnItemClickListener(new OnItemClickListener() {

					public void onItemClick(AdapterView<?> adapterView,
							View view, int position, long id) {
						// Show details of the SpreadSheet
						if (position == 0)
							return;

						Toast.makeText(
								SpreadSheetDetailsActivity.this
										.getApplicationContext(),
								"Showing WorkSheet details, please wait...",
								Toast.LENGTH_LONG).show();

						// Start SP Details Activity
						Intent i = new Intent(SpreadSheetDetailsActivity.this,
								WorkSheetDetailsActivity.class);
						i.putExtra("wk_id", position - 1);
						i.putExtra("sp_id", spID);
						startActivity(i);
					}
				});
				setContentView(list);
			}
		}
	}
}
