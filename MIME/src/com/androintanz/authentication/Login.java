package com.androintanz.authentication;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

public class Login {
	AccountManager manager;
	Context context;
	static Account loginAccount;
	public Account[] acs;
	public static String groupId;
	public static String accountName;

	public Login(Context context) {
		this.context = context;
		manager = AccountManager.get(context.getApplicationContext());
		acs = manager.getAccountsByType("com.google");
	}

	public int getAccountCount() {
		return acs.length;
	}

	public void setLoginAccount(String selectedEmailAccount) {
		for (int i = 0; i < acs.length; i++) {
			if (acs[i].name.equals(selectedEmailAccount)) {
				loginAccount = acs[i];
			}

		}
	}

	public static void setSelectedAccountName(String selectedAccountName) {
		accountName = selectedAccountName;
	}

	public static String getSelectedAccountName() {
		return accountName;

	}

	public static Account getLoginAccount() {
		return loginAccount;

	}

	public static void setGroupId(String selectedGroupId) {

		groupId = selectedGroupId;
	}

	public static String getGroupId() {

		return groupId;
	}
}
