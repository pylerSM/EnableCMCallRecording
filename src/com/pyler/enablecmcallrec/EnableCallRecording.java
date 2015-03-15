package com.pyler.enablecmcallrec;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import android.content.Context;
import android.media.MediaRecorder;
import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class EnableCallRecording implements IXposedHookLoadPackage,
		IXposedHookInitPackageResources {
	public static final String DIALER = "com.android.dialer";
	public static final String CALL_RECORDING_SERVICE = "com.android.services.callrecorder.CallRecorderService";

	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
		if (!DIALER.equals(lpparam.packageName)) {
			return;
		}
		findAndHookMethod(CALL_RECORDING_SERVICE, lpparam.classLoader,
				"isEnabled", Context.class,
				XC_MethodReplacement.returnConstant(true));
		findAndHookMethod(CALL_RECORDING_SERVICE, lpparam.classLoader,
				"getAudioSource",
				XC_MethodReplacement
						.returnConstant(MediaRecorder.AudioSource.VOICE_CALL));
	}

	@Override
	public void handleInitPackageResources(InitPackageResourcesParam resparam)
			throws Throwable {
		if (!DIALER.equals(resparam.packageName)) {
			return;
		}
		resparam.res.setReplacement(DIALER, "bool", "call_recording_enabled",
				true);
	}

}