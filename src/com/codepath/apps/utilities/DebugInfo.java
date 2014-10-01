package com.codepath.apps.utilities;

import android.util.Log;

public class DebugInfo {
	public static final int currentMethod = 3;
	public static final int depth = 0;

	public static void stackTrace() {
		StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		for (int i = currentMethod; i < currentMethod + depth; i++) {
			Log.d("Twitter",
					ste[i].getClassName() + " # " + ste[i].getMethodName());
		}
		Log.d("Twitter", "\n");
	}
}
