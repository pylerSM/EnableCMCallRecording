package com.pyler.enablecmcallrec;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import android.content.Context;
import android.os.Build;
import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class EnableCallRecording implements IXposedHookLoadPackage,
		IXposedHookInitPackageResources {
	public static final String DIALER = "com.android.dialer";

	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
		if (!DIALER.equals(lpparam.packageName)) {
			return;
		}
		findAndHookMethod(
				"com.android.services.callrecorder.CallRecorderService",
				lpparam.classLoader, "isEnabled", Context.class,
				XC_MethodReplacement.returnConstant(true));

	}

	@Override
	public void handleInitPackageResources(InitPackageResourcesParam resparam)
			throws Throwable {
		if (!DIALER.equals(resparam.packageName)) {
			return;
		}
		resparam.res.setReplacement(DIALER, "bool", "call_recording_enabled",
				true);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			resparam.res.setReplacement(DIALER, "integer",
					"call_recording_audio_source", 4);
		}
	}

}