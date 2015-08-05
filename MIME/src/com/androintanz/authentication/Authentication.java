package com.androintanz.authentication;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.pras.auth.Authenticator;

public class Authentication implements Authenticator {
	private final String TAG = "AndroidAuthenticator";
	Activity activity;
	AccountManager manager;
	private String service = null;
	private String authToken = "";

	public Authentication(Activity activity) {
		this.activity = activity;
		manager = AccountManager.get(activity.getApplicationContext());
	}

	@Override
	public String getAuthToken(String service) {
		if (service == null) {
			throw new IllegalAccessError(
					"No Service name defined, Can't create Auth Token...");
		}

		if (service != null && !service.equals(service)) {
			// Reset previous Token
			manager.invalidateAuthToken("com.google", authToken);

		}
		Account[] acs = manager.getAccountsByType("com.google");
		Log.i(TAG, "Num of Matching account: " + acs.length);

		if (acs == null || acs.length == 0) {
			Toast.makeText(this.activity.getApplicationContext(),
					"No Google Account Added...", Toast.LENGTH_LONG).show();
			return "";
		} else {
			AccountManagerFuture result = (AccountManagerFuture) (manager
					.getAuthToken(Login.getLoginAccount(), service, null,
							activity, null, null));
			try {
				Bundle b = (Bundle) result.getResult();
				authToken = b.getString(AccountManager.KEY_AUTHTOKEN);
				Log.i(TAG, "Auth_Token: " + authToken);
				return authToken;
			} catch (Exception ex) {
				Log.i(TAG, "Error: " + ex.toString());
			}
		}
		Log.i(TAG, "Problem in getting Auth Token...");
		return "";
	}
}