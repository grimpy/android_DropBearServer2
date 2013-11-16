/*
 * Pawel Nadolski <http://stackoverflow.com/questions/10319471/android-is-the-groupid-of-sdcard-rw-always-1015/>
 */
package com.github.grimpy.android_sshd_manager.task;

import java.util.ArrayList;

import com.github.grimpy.android_sshd_manager.LocalPreferences;
import com.github.grimpy.android_sshd_manager.util.L;
import com.github.grimpy.android_sshd_manager.util.RootUtils;
import com.github.grimpy.android_sshd_manager.util.ServerUtils;
import com.github.grimpy.android_sshd_manager.util.ShellUtils;
import com.stericson.RootTools.RootTools;

import android.content.Context;
import android.text.TextUtils;


public class Starter extends Task {

	private static final int ID_ROOT = 0;

	public Starter(Context context, Callback<Boolean> callback, Boolean startInBackground) {
		super(Callback.TASK_START, context, callback, startInBackground);

		if (mProgressDialog != null) {
			mProgressDialog.setTitle("Starter");
		}
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		boolean enable_authentication = LocalPreferences.getBoolean(mContext, LocalPreferences.PREF_ENABLE_AUTHENTICATION, true);
		String port = LocalPreferences.getString(mContext, LocalPreferences.PREF_PORT, LocalPreferences.PREF_PORT_DEFAULT);
		ArrayList<String> config = (ArrayList<String>) ServerUtils.CONFIG.clone();
		if (!enable_authentication) {
			config.add("PermitRootLogin without-password");
			config.add("PermitEmptyPasswords yes");
		}
		config.add(String.format("Port %s", port));
		ShellUtils.echoToFile(TextUtils.join("\n", config), ServerUtils.SSHD_CONFIG);
		String command = String.format("/system/bin/sshd -f %s", ServerUtils.SSHD_CONFIG);
		if (ShellUtils.execute(command) == false) {
			return falseWithError("execute(" + command + ")");
		}

		return true;
	}
}