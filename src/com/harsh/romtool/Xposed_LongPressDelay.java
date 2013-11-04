/*
 *      HarshJelly Tweaker - An app to Tweak HarshJelly ROM
 *      Author : Harsh Panchal <panchal.harsh18@gmail.com, mr.harsh@xda-developers.com>
 */
package com.harsh.romtool;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Xposed_LongPressDelay implements IXposedHookLoadPackage {
	private XSharedPreferences pref;
	@Override
	public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
		pref = new XSharedPreferences("com.harsh.romtool");
		if (lpparam.packageName.equals("android")) {
	            ClassLoader classLoader = lpparam.classLoader;
	            XC_MethodReplacement methodreplacer = new XC_MethodReplacement() {
	                protected Object replaceHookedMethod(
	                        XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam)
	                        throws Throwable {
	                	int val = Integer.parseInt(pref.getString("longpress_delay", Integer.toString(500)));
	                    return val;
	                }
	            };
	            XposedHelpers.findAndHookMethod("android.view.ViewConfiguration",
	                    classLoader, "getGlobalActionKeyTimeout", methodreplacer);
	    }
    }
}
