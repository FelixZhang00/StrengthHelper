package felix.example.strengthhelper.utils;

import android.util.Log;

public class Logger {

	/*
	 * If you want to market your app ,hiding all LogCat info , just change
	 * LOGLEVEL to 0.
	 */
	private static int LOGLEVEL = 10;

	// These static vars are copied from Log class.

	/**
	 * Priority constant for the println method; use Log.v.
	 */
	public static int VERBOSE = 2;

	/**
	 * Priority constant for the println method; use Log.d.
	 */
	public static int DEBUG = 3;

	/**
	 * Priority constant for the println method; use Logger.i.
	 */
	public static int INFO = 4;

	/**
	 * Priority constant for the println method; use Log.w.
	 */
	public static int WARN = 5;

	/**
	 * Priority constant for the println method; use Log.e.
	 */
	public static int ERROR = 6;

	/**
	 * Priority constant for the println method.
	 */
	public static int ASSERT = 7;

	public static void v(String TAG, String msg) {
		if (LOGLEVEL > VERBOSE) {
			Log.v(TAG, msg);
		}
	}

	public static void i(String TAG, String msg) {
		if (LOGLEVEL > INFO) {
			Log.i(TAG, msg);
		}
	}

	public static void d(String TAG, String msg) {
		if (LOGLEVEL > DEBUG) {
			Log.d(TAG, msg);
		}
	}

	public static void e(String TAG, String msg) {
		if (LOGLEVEL > ERROR) {
			Log.e(TAG, msg);
		}
	}

	public static void w(String TAG, String msg) {
		if (LOGLEVEL > WARN) {
			Log.w(TAG, msg);
		}
	}

}
