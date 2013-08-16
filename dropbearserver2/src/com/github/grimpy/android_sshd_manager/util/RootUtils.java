package com.github.grimpy.android_sshd_manager.util;

import java.io.File;

import android.content.Context;

import com.stericson.RootTools.RootTools;

public abstract class RootUtils {

	public static Boolean hasRootAccess = false;
	public static Boolean hasSSHD = false;
	public static Boolean isInitialized = false;

	public static final Boolean checkRootAccess() {
		hasRootAccess = false;
		if (RootTools.isRootAvailable()) {
			if (RootTools.isAccessGiven()) {
				hasRootAccess = true;
			}
			else {
				L.w("access rejected");
			}
		}
		else {
			L.w("su not found");
		}
		return hasRootAccess;
	}

	public static final Boolean checkSSHD() {
		hasSSHD = false;
		if (RootTools.checkUtil("sshd") && RootTools.checkUtil("ssh-keygen")) {
			hasSSHD = true;
		}
		else {
			L.w("SSHD not found");
		}
		return hasSSHD;
	}

	public static final Boolean checkInitialized(Context context) {
		isInitialized = false;
		String[] filestocheck = {ServerUtils.DSA_KEY, ServerUtils.RSA_KEY };
		for (String filepath : filestocheck) {
			if (!RootTools.exists(filepath)) {
				L.w(filepath);
				return false;
			}	
		}		
		isInitialized = true;

		return isInitialized;
	}
}
