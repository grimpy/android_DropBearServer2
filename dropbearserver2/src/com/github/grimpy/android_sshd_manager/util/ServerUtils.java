/*
 * Sherif elKhatib <http://stackoverflow.com/questions/6896618/read-command-output-inside-su-process>
 * Martin <http://www.droidnova.com/get-the-ip-address-of-your-device,304.html>
 * javadb <http://www.javadb.com/remove-a-line-from-a-text-file>
 * external-ip <http://code.google.com/p/external-ip/>
 */
package com.github.grimpy.android_sshd_manager.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.http.conn.util.InetAddressUtils;

import com.stericson.RootTools.RootTools;

import android.content.Context;
import android.text.TextUtils;

public abstract class ServerUtils {

	public static String localDir = null;
	public static Boolean sshdRunning = false;
	public static String dropbearVersion = null;
	public static List<String> ipAddresses = null;
	public static String AUTHORIZED_KEYS = "/data/.ssh/authorized_keys";
	public static String RSA_KEY = "/data/ssh/ssh_host_rsa_key";
	public static String RSA_PUB_KEY = "/data/ssh/ssh_host_rsa_key.pub";
	public static String DSA_KEY = "/data/ssh/ssh_host_dsa_key";
	public static String DSA_PUB_KEY = "/data/ssh/ssh_host_dsa_key.pub";
	
	

	// WARNING: this is not threaded
	public static final List<String> getIpAddresses(Context context) {
		if (ipAddresses == null) {
			ipAddresses = new ArrayList<String>();
			try {
				for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
					NetworkInterface intf = en.nextElement();
					for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
						InetAddress inetAddress = enumIpAddr.nextElement();
						String ipAddress = inetAddress.getHostAddress().toString();
						if (InetAddressUtils.isIPv4Address(ipAddress) && !ipAddresses.contains(ipAddress)) {
							ipAddresses.add(ipAddress);
						}
					}
				}
			}
			catch (SocketException e) {
				L.e("SocketException: " + e.getMessage());
			}
			return ipAddresses;
		}
		return ipAddresses;
	}

	// WARNING: this is not threaded
	public static final Boolean isSSHRunning() {
		sshdRunning = false;
		try {
			Process suProcess = Runtime.getRuntime().exec("su");

			// stdin
			DataOutputStream stdin = new DataOutputStream(suProcess.getOutputStream());
			L.d("# ps sshd");
			stdin.writeBytes("ps sshd\n");
			stdin.flush();
			stdin.writeBytes("exit\n");
			stdin.flush();

			// stdout
			BufferedReader reader = new BufferedReader(new InputStreamReader(suProcess.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.endsWith("sshd") == true) {
					sshdRunning = true;
					L.d(String.format("%s", sshdRunning));
					return sshdRunning;
				}
			}
		}
		catch (IOException e) {
			L.e("IOException: " + e.getMessage());
		}
		L.d(String.format("%s", sshdRunning));
		
		return sshdRunning;
	}

	// WARNING: this is not threaded
	public static final Boolean generateRsaPrivateKey(String path) {
		String cmd = String.format("/system/bin/ssh-keygen -t rsa -f %s -N \"\"", path);
		return ShellUtils.execute(cmd);
	}

	// WARNING: this is not threaded
	public static final Boolean generateDsaPrivateKey(String path) {
		String cmd = String.format("/system/bin/ssh-keygen -t dsa -f %s -N \"\"", path);
		return ShellUtils.execute(cmd);
	}

	// WARNING: this is not threaded
	public static List<String> getPublicKeys(String path) {
		List<String> publicKeys = new ArrayList<String>();
		publicKeys = ShellUtils.readFile(path);
		return publicKeys;
	}

	// WARNING: this is not threaded
	public static final Boolean addPublicKey(String publicKey, String path) {
		return ShellUtils.echoAppendToFile(publicKey, path);
	}

	// WARNING: this is not threaded
	public static final Boolean removePublicKey(String publicKey, String path) {
		List publicKeys = getPublicKeys(path);
		if (publicKeys.contains(publicKey)) {
			publicKeys.remove(publicKey);
			String contents = TextUtils.join("\n", publicKeys);
			return ShellUtils.echoToFile(contents, path);
		}
		return false;
	}

	public static final Boolean createIfNeeded(String path) {
		if (! RootTools.exists(path)){
			return ShellUtils.touch(path);
		}
		return true;
	}

}
