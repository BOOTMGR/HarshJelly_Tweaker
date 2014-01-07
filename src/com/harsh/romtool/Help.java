/*******************************************************************************
 * Copyright (c) 2013 "Harsh Panchal" <panchal.harsh18@gmail.com>.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/

package com.harsh.romtool;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class Help extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
        TextView tv = (TextView) findViewById(R.id.textView1);
        tv.setText(Html.fromHtml("<b><u>CRT Animation</u></b><br>"+"This will Enable CRT effect while turning screen off.Beware that there are some bugs which wake up device sometime after turning screen off.In that case just wait for 1-2 second and then try to turn it off<br>"+"<b><u>Long Press Back to kill app</u></b><br>"+"This will simply kill the foreground activity on Holding back key.<br>"+"<b><u>Ascending Ringtone</u></b><br>"+"When enabled,Ringtone volume will be increase in Ascending manner<br>"+"<b><u>Wake on Unplug</u></b><br>"+"Turns screen on upon disconnecting device from USB cable, Charger etc<br>"+"<b><u>Allow all Rotations</u></b><br>"+"Allows Screen to rotate in any direction (90,180,270,360)<br>"+"<b><u>Show Navigationbar</u></b><br>"+"Shows on screen virtual Hardware Keys.This is only for Fun.Some stock apps will be messed up after enabling this option<br>"+"<b><u>IME Switcher Notification</u></b><br>"+"Shows notification in Status bar while typing to switch IME (keyboard).Useful while using Multiple keyboards<br>"+"<b><u>Lockscreen Vibration</u></b><br>"+"Enables Haptic Feedback in AOSP Lockscreen<br>"+"<b><u>Lockscreen Rotation</u></b><br>"+"Allows rotation in lockscreen.<br>"+"<b><u>Logger</u></b><br>"+"Enables Android Logger.This will reduce system performance.But if you're going to report a bug, enable this option then take log and post it.This Option works only for cocore Kernel<br>"+"<b><u>Sysctl Tweaks</u></b><br>"+"This Tweaks are EXPERIMENTAL, so their effects are unknown.Enable/disable them according to whatever you feel.By Default:DISABLED<br>"+"<b><u>Safe Volume Warning</u></b><br>"+"Shows/hides warning Toast when listening to High level sound.It is better to keep it disabled while watching Video.<br>CAUTION: Listening to loud music for longer periods can damage your ear.<br>"+"<b><u>WiFi Connection Toast</u></b><br>"+"Shows Toast when device is connected to AP<br>"+"<b><u>Quickpanel auto Scrolling</u></b><br>"+"Automatically Scrolls quickpanel toggles when statusbar is expanded"));
    }
}
