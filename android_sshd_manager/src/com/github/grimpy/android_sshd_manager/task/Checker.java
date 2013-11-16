package com.github.grimpy.android_sshd_manager.task;

import com.github.grimpy.android_sshd_manager.util.RootUtils;
import com.github.grimpy.android_sshd_manager.util.ServerUtils;

import android.content.Context;


public class Checker extends Task {

	public Checker(Context context, Callback<Boolean> callback) {
		super(Callback.TASK_CHECK, context, callback, true);
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		RootUtils.checkRootAccess();
		RootUtils.checkSSHD();
		RootUtils.checkInitialized(mContext);

		ServerUtils.isSSHRunning();
		if (ServerUtils.sshdRunning == true) {
			ServerUtils.getIpAddresses(mContext);
		}

		return (RootUtils.hasRootAccess && RootUtils.hasSSHD && RootUtils.isInitialized && ServerUtils.sshdRunning);
	}
}