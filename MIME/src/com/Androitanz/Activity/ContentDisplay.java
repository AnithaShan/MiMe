package com.Androitanz.Activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.Androitanz.Provider.MimeProvider.ProviderDetails;
import com.Androitanz.db.DatabaseOperation;
import com.androintanz.authentication.Login;

/**
 * The Screen to fetch the name to store it in the provider and database
 * 
 * @author Anitha
 * 
 */
public class ContentDisplay extends Activity {
	/**
	 * button used to open the login page
	 */
	private Button loginButton;
	/**
	 * Instance to show popup window
	 */
	PopupWindow popUp;
	LinearLayout layout;
	/**
	 * The listview instance to display the all acount details
	 */
	ListView listViewAcountsId;
	LayoutParams params;
	/**
	 * The application context
	 */
	Context context;
	/**
	 * All account names
	 */
	String[] accountMailNames = new String[10];
	/**
	 * The instance of Login class
	 */
	Login log;
	/**
	 * Instance of the MIME database
	 */
	DatabaseOperation db;
	/**
	 * The gropiId edit box
	 */
	EditText optedGroupId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		optedGroupId = (EditText) findViewById(R.id.groupIdEdittext);

		db = new DatabaseOperation(this);

		context = this;
		/*
		 * popup menu initialisation
		 */
		popUp = new PopupWindow(this);
		layout = new LinearLayout(this);
		/*
		 * to display the account details
		 */
		listViewAcountsId = new ListView(this);
		loginButton = (Button) findViewById(R.id.login);
		/*
		 * if more than one account call pop up menu
		 */
		log = new Login(context);
		accountMailNames = new String[log.getAccountCount()];
		for (int i = 0; i < log.getAccountCount(); i++) {
			accountMailNames[i] = log.acs[i].name;
		}

		loginButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (!popUp.isShowing())
					popUp.showAtLocation(layout, Gravity.CENTER, 5, 5);
				popUp.update(50, 50, LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT);
				popUp.setFocusable(true);

			}

		});
		params = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		layout.setOrientation(LinearLayout.VERTICAL);
		/*
		 * setting listview
		 */
		ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(
				getApplicationContext(), android.R.layout.simple_list_item_1,
				android.R.id.text1, accountMailNames);
		listViewAcountsId.setAdapter(modeAdapter);
		layout.addView(listViewAcountsId, params);
		/*
		 * popup menu is displayed
		 */
		popUp.setContentView(layout);

		listViewAcountsId.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View v,
					int position, long id) {
				/*
				 * Group id is set to be as selected which has been selected by
				 * user
				 */
				Login.setGroupId(optedGroupId.getText().toString());

				String selectedAccountName = (String) adapterView
						.getItemAtPosition(position);

				log.setSelectedAccountName(selectedAccountName);
				log.setLoginAccount(selectedAccountName);
				popUp.dismiss();

				DatabaseOperation.fetchingLoginDetails();
			}
		});
		/**
		 * for content provider
		 */
		// Obtain handles to UI objects
		// mContactNameEditText = (EditText)
		// findViewById(R.id.contactNameEditText);
		// mContactSaveButton = (Button) findViewById(R.id.contactSaveButton);
		// mContactsText = (TextView) findViewById(R.id.contactEntryText);
		// mContactSaveButton.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// String name = mContactNameEditText.getText().toString();
		// // insertRecord(name);
		// mContactsText.append("\n" + name);
		// }
		// });

		// displayRecords();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		// Override back button
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (popUp.isShowing()) {
				popUp.dismiss();
				// click = true;
				return false;

			}
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * Insert into the content provider
	 * 
	 * @param userName
	 */
	private void insertRecord(String userName) {
		ContentValues values = new ContentValues();
		values.put(ProviderDetails.FRIENDS_NAME, userName);
		getContentResolver().insert(ProviderDetails.CONTENT_URI, values);
	}

	/**
	 * Disply contents from the content provider
	 */
	private void displayRecords() {
		// An array specifying which columns to return.
		String columns[] = new String[] { ProviderDetails._ID,
				ProviderDetails.FRIENDS_NAME };
		Uri myUri = ProviderDetails.CONTENT_URI;
		Cursor cur = managedQuery(myUri, columns, // Which columns to return
				null, // WHERE clause; which rows to return(all rows)
				null, // WHERE clause selection arguments (none)
				null // Order-by clause (ascending by name)
		);
		if (cur.moveToFirst()) {
			String id = null;
			String userName = null;
			do {
				// Get the field values
				id = cur.getString(cur.getColumnIndex(ProviderDetails._ID));
				userName = cur.getString(cur
						.getColumnIndex(ProviderDetails.FRIENDS_NAME));
				Toast.makeText(this, id + " " + userName, Toast.LENGTH_LONG)
						.show();
			} while (cur.moveToNext());
		}
	}
}
