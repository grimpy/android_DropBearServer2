/*
 * Pawel Nadolski <http://stackoverflow.com/questions/10319471/android-is-the-groupid-of-sdcard-rw-always-1015/>
 */
package com.github.grimpy.android_sshd_manager.task;

import com.github.grimpy.android_sshd_manager.LocalPreferences;
import com.github.grimpy.android_sshd_manager.util.L;
import com.github.grimpy.android_sshd_manager.util.ServerUtils;
import com.github.grimpy.android_sshd_manager.util.ShellUtils;

import android.content.Context;


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
		String login = "root";
		String passwd = LocalPreferences.getString(mContext, LocalPreferences.PREF_PASSWORD, LocalPreferences.PREF_PASSWORD_DEFAULT);

		String command = "/system/bin/sshd -f /system/etc/ssh/sshd_config";
		if (ShellUtils.execute(command) == false) {
			return falseWithError("execute(" + command + ")");
		}

		return true;
	}
}