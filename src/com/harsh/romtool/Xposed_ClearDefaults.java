/*******************************************************************************
 * Copyright (c) 2013 "Harsh Panchal" <panchal.harsh18@gmail.com>.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/

package com.harsh.romtool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;

public class Xposed_ClearDefaults implements IXposedHookZygoteInit {
	private XSharedPreferences pref = new XSharedPreferences("com.harsh.romtool");
	boolean enabled = pref.getBoolean("clear_defaults", false);
	@Override
    public void initZygote(StartupParam startupParam) throws Throwable {
		if(!enabled) {
            Class<?> ResolverGuideActivity = XposedHelpers.findClass("com.android.internal.app.ResolverGuideActivity", null);
            
            XposedHelpers.findAndHookMethod(ResolverGuideActivity, "onCreate", Bundle.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            Activity activity = (Activity) param.thisObject;
                            
                        Intent localIntent = (Intent) XposedHelpers.callMethod(param.thisObject, "makeMyIntent");
                        if (localIntent != null)
                                activity.startActivity(localIntent);
                            activity.finish();
                    }
            });
		}
    }
}
