package net.shenru.safe.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.util.Log;

/**
 * Shell控制台
 * @author along
 */
public class Shell {

	private static final String TAG = Shell.class.getSimpleName();

	private Process proc;
	private boolean isError = false;
	private boolean isOpenLog = true;

	private BufferedReader in;
	private OutputStreamWriter out;
	private InputStream err;

	public Shell() {
		ProcessBuilder pb = new ProcessBuilder("echo");
		try {
			pb.redirectErrorStream(true);
			proc = pb.start();
//			in = proc.getInputStream();
//			out = proc.getOutputStream();
			//errorStream = proc.getErrorStream();
			in = new BufferedReader(new InputStreamReader(proc.getInputStream(), "UTF-8"));
			out = new OutputStreamWriter(proc.getOutputStream(), "UTF-8");
		} catch (IOException e) {
			SLog.e(TAG, e.getMessage(), e);
			isError = true;
		}
	}

	public String exec(String str) {
		if (errorCheck()) {
			return null;
		}
		try {
//			byte[] b = str.getBytes();
//			out.write(b, 0, b.length);
			out.write(str);
			out.flush();
			return log(str);
		} catch (IOException e) {
			SLog.e(TAG, e.getMessage(), e);
		}
		return null;
	}

	public void waitFor() {
		if (errorCheck()) {
			return;
		}

	}

	public void setLog(boolean isOpen) {
		this.isOpenLog = isOpen;
	}

	//关闭shell
	public void close() {
		try {
			proc.destroy();
		} catch (Exception e) {
			SLog.e(TAG, e.getMessage(), e);
		}

		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
				SLog.e(TAG, e.getMessage(), e);
			}
		}

		if (out != null) {
			try {
				out.close();
			} catch (IOException e) {
				SLog.e(TAG, e.getMessage(), e);
			}
		}

		if (err != null) {
			try {
				err.close();
			} catch (IOException e) {
				SLog.e(TAG, e.getMessage(), e);
			}
		}

	}

	private String log(String cmd) {
		try {
//			String log = null;
//			String line = null;
//			while ((line = in.readLine()) != null) {
//				if (log == null) {
//					log = "";
//				}
//				log += line;
//				line = null;
//			}

			//打印Log
			if (isOpenLog) {
				Log.i(TAG, "shell cmd:" + cmd);
//				Log.i(TAG, "shell log:" + log);
			}

//			return log;
		} catch (Exception e) {
			SLog.e(TAG, e.getMessage(), e);
		}
		return null;
	}

	private boolean errorCheck() {
		return isError;
	}
}
