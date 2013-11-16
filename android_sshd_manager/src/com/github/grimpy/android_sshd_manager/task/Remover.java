package com.github.grimpy.android_sshd_manager.task;

import com.github.grimpy.android_sshd_manager.util.ServerUtils;
import com.github.grimpy.android_sshd_manager.util.ShellUtils;

import android.content.Context;


public class Remover extends Task {

	public Remover(Context context, Callback<Boolean> callback) {
		super(Callback.TASK_REMOVE, context, callback, false);

		if (mProgressDialog != null) {
			mProgressDialog.setTitle("Remover");
		}
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		publishProgress("Authorized keys");
		ShellUtils.rm(ServerUtils.AUTHORIZED_KEYS);

		publishProgress("Host RSA key");
		ShellUtils.rm(ServerUtils.RSA_KEY);

		publishProgress("Host DSA key");
		ShellUtils.rm(ServerUtils.DSA_KEY);

		return true;
	}
}