package com.github.grimpy.android_sshd_manager.task;

import com.github.grimpy.android_sshd_manager.util.L;
import com.github.grimpy.android_sshd_manager.util.ServerUtils;
import com.github.grimpy.android_sshd_manager.util.ShellUtils;

import android.content.Context;


public class Stopper extends Task {

	public Stopper(Context context, Callback<Boolean> callback, boolean startInBackground) {
		super(Callback.TASK_STOP, context, callback, startInBackground);

		if (mProgressDialog != null) {
			mProgressDialog.setTitle("Stopper");
		}
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		//ShellUtils.rm(ServerUtils.getLocalDir(mContext) + "/pid");

		L.i("Killing processes");
		if (ShellUtils.killall("sshd") == false) {
			return falseWithError("killall(sshd)");
		}

		return true;
	}
}