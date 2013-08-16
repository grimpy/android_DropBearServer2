package me.shkschneider.dropbearserver2.task;

import java.io.File;

import android.content.Context;

import me.shkschneider.dropbearserver2.R;
import me.shkschneider.dropbearserver2.util.ServerUtils;
import me.shkschneider.dropbearserver2.util.ShellUtils;
import me.shkschneider.dropbearserver2.util.Utils;

public class Initialize extends Task {

	public Initialize(Context context, Callback<Boolean> callback) {
		super(Callback.TASK_INSTALL, context, callback, false);

		if (mProgressDialog != null) {
			mProgressDialog.setTitle("Installer");
		}
	}

	private Boolean copyToAppData(int resId, String path) {
		if (new File(path).exists() == true && ShellUtils.rm(path) == false) {
			return falseWithError(path);
		}
		if (Utils.copyRawFile(mContext, resId, path) == false) {
			return falseWithError(path);
		}
		if (ShellUtils.chmod(path, "755") == false) {
			return falseWithError(path);
		}
		return true;
	}

	private Boolean copyToSystemXbin(int resId, String tmp, String path) {
		if (Utils.copyRawFile(mContext, resId, tmp) == false) {
			return falseWithError(tmp);
		}
		if (ShellUtils.rm(path) == false) {
			// Ignore
		}
		if (ShellUtils.mv(tmp, path) == false) {
			return falseWithError(path);
		}
		if (ShellUtils.chmod(path, "755") == false) {
			return falseWithError(path);
		}
		return true;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		publishProgress("Dropbear binary");
		
		publishProgress("Authorized keys");
		String authorized_keys = ServerUtils.AUTHORIZED_KEYS;
		if (new File(authorized_keys).exists() == true && ShellUtils.rm(authorized_keys) == false) {
			return falseWithError(authorized_keys);
		}
		if (ServerUtils.createIfNeeded(authorized_keys) == false) {
			return falseWithError(authorized_keys);
		}

		publishProgress("Host RSA key");
		String host_rsa = ServerUtils.RSA_KEY;
		if (new File(host_rsa).exists() == true && ShellUtils.rm(host_rsa) == false) {
			return falseWithError(host_rsa);
		}
		if (ServerUtils.generateRsaPrivateKey(host_rsa) == false) {
			return falseWithError(host_rsa);
		}

		publishProgress("Host DSA key");
		String host_dss = ServerUtils.DSA_KEY;
		if (new File(host_dss).exists() == true && ShellUtils.rm(host_dss) == false) {
			return falseWithError(host_dss);
		}
		if (ServerUtils.generateDsaPrivateKey(host_dss) == false) {
			return falseWithError(host_dss);
		}
		
		publishProgress("Permissions");
		if (ShellUtils.chmod(authorized_keys, "644") == false) {
			return falseWithError(authorized_keys);
		}
		if (ShellUtils.chown(host_rsa, "0:0") == false) {
			return falseWithError(host_rsa);
		}
		if (ShellUtils.chown(host_dss, "0:0") == false) {
			return falseWithError(host_dss);
		}

		return true;
	}
}