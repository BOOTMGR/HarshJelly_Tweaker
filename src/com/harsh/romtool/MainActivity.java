/*******************************************************************************
 * Copyright (c) 2013 "Harsh Panchal" <panchal.harsh18@gmail.com>.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/

package com.harsh.romtool;


import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends Activity {

    private static final String CRT_ANIM = "harsh_crt";
    private static final String KILLER = "harsh_killer";
    private static final String AOSP_VIBRATION = "harsh_aosp_vib";
    private static final String AOSP_ROTATION = "harsh_aosp_orient";
    private static final String ASCEND_RING = "harsh_ascend_ring";
    private static final String UNPLUG_WAKE = "harsh_unplug";
    private static final String ALL_ROTATE = "harsh_rotate";
    private static final String NAVIGATION = "harsh_navigation";
    private static final String IME = "harsh_ime";
    private static final String HEADSET = "harsh_volume";
    private static final String WIFI_NOTIF = "harsh_wifi_notif";
    private static final String TW_SCROLL = "harsh_tw_scroll";
    private static final String QUICK_SCROLL = "harsh_quick_scroll";
    private static final String STATUSBAR_TRANS = "harsh_statusbar_trans";
    private static final String USERNAME = "user_name";
    private static final String FBDELAY = "/sys/module/fbearlysuspend/parameters/fbdelay";
    private static final String FBDELAY_MS = "/sys/module/fbearlysuspend/parameters/fbdelay_ms";
    private static final String LOGGER = "/data/logger";
    private static final String SYSCTL1 = "/system/etc";
    private static final String INITD = "/system/etc/init.d";
    
    private static ContentResolver cr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager mFragmentManager = getFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager
                                .beginTransaction();
        PrefsFragment mPrefsFragment = new PrefsFragment();
        mFragmentTransaction.replace(android.R.id.content, mPrefsFragment);
        mFragmentTransaction.commit();
        Log.i("harsh_debug", "===========HarshJelly Tweaker Launched===========");
    }
    
    public static class PrefsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
    	
        @SuppressWarnings("deprecation")
		@Override
        public void onCreate(Bundle savedInstanceState) {
                    super.onCreate(savedInstanceState);
                    getPreferenceManager().setSharedPreferencesMode(MODE_WORLD_READABLE);
                    addPreferencesFromResource(R.xml.main);
                    SharedPreferences sharedPref = getPreferenceScreen().getSharedPreferences();
                    cr = getActivity().getContentResolver();
                    onSharedPreferenceChanged(sharedPref,"crt_toggle");
                    onSharedPreferenceChanged(sharedPref,"killer_toggle");
                    onSharedPreferenceChanged(sharedPref,"vib_toggle");
                    onSharedPreferenceChanged(sharedPref,"rot_toggle");
                    onSharedPreferenceChanged(sharedPref,"ascending_toggle");
                    onSharedPreferenceChanged(sharedPref,"log_toggle");
                    onSharedPreferenceChanged(sharedPref,"sys_toggle");
                    onSharedPreferenceChanged(sharedPref,"unplug_wake");
                    onSharedPreferenceChanged(sharedPref,"allrot_toggle");
                    onSharedPreferenceChanged(sharedPref,"nav_toggle");
                    onSharedPreferenceChanged(sharedPref,"ime_toggle");
                    onSharedPreferenceChanged(sharedPref,"hs_toggle");
                    onSharedPreferenceChanged(sharedPref,"wifi_notif_toggle");
                    onSharedPreferenceChanged(sharedPref,"tw_pg_toggle");
                    onSharedPreferenceChanged(sharedPref,"font");
                    onSharedPreferenceChanged(sharedPref,"quickpanel_scroll");
                    onSharedPreferenceChanged(sharedPref,"statusbar_trans");
                    onSharedPreferenceChanged(sharedPref,"user_name");
                    handleUserTile();
        }

		@Override
		public void onSharedPreferenceChanged(SharedPreferences sp,
				String key) {
			if(key.equals("crt_toggle")) handleCRT();
			if(key.equals("killer_toggle")) handleKiller();
			if(key.equals("vib_toggle")) handleAOSPVib();
			if(key.equals("rot_toggle")) handleAOSPOrient();
			if(key.equals("ascending_toggle")) handleRinger();
			if(key.equals("log_toggle")) handleLogger();
			if(key.equals("sys_toggle")) handleSysctl();
			if(key.equals("unplug_wake")) handleUnplug();
			if(key.equals("allrot_toggle")) handleAllRotation();
			if(key.equals("nav_toggle")) handleNavigation();
			if(key.equals("ime_toggle")) handleIME();
			if(key.equals("hs_toggle")) handleHeadsetWarning();
			if(key.equals("wifi_notif_toggle")) handleWiFiNotification();
			if(key.equals("tw_pg_toggle")) handleTWScroll();
			if(key.equals("font")) handleFont();
			if(key.equals("quickpanel_scroll")) handleQuickPanelScroll();
			if(key.equals("statusbar_trans")) handleStatusbarTransparancy();
			if(key.equals("user_name")) updateUserName();
		}
		
		public void handleCRT() {
			final CheckBoxPreference crt_toggle = (CheckBoxPreference) findPreference("crt_toggle");
	        final File f = new File(FBDELAY);
	        int crt = getInt(CRT_ANIM, 0);
	        crt_toggle.setChecked(crt != 0);
	        crt_toggle.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
	                public boolean onPreferenceClick(Preference preference) {
	                    if (crt_toggle.isChecked()) {
	                        putInt(CRT_ANIM, 1);
	                        Log.d("harsh_debug","harsh_crt=>1");
	                        new SU().execute("echo 1 > "+FBDELAY,"echo 350 > "+FBDELAY_MS);
	                        Utils.mountSystemRW();
	                        Utils.copyAssets("03_crt",INITD,777,getActivity().getApplicationContext());
	                    } else {
	                    	putInt(CRT_ANIM, 0);
	                        Log.d("harsh_debug","harsh_crt=>0");
	                        new SU().execute("echo 0 > "+FBDELAY,"echo 0 > "+FBDELAY_MS);
	                        Utils.mountSystemRW();
	                        Utils.copyAssets("99_crtoff",INITD,777,getActivity().getApplicationContext());
	                    }
	                    return false;
	                }
	        });
	        if(!f.exists()) {
	            crt_toggle.setSummary("Unsupported kernel");
	            crt_toggle.setEnabled(false);
				Log.d("harsh_debug","CRT Animation not supported due to unsupported Kernel");
				putInt(CRT_ANIM, 0);
	        }
		}
		
		public void handleKiller() {
	        final CheckBoxPreference killer = (CheckBoxPreference) findPreference("killer_toggle");
	        int Killer = getInt(KILLER, 1);
	        killer.setChecked(Killer != 0);
	        killer.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
	            public boolean onPreferenceClick(Preference preference) {
	                if (killer.isChecked()) {
	                	putInt(KILLER,1);
	                    Log.d("harsh_debug","harsh_killer=>1");
	                } else {
	                	putInt(KILLER,0);
	                    Log.d("harsh_debug","harsh_killer=>0");
	                }
	                return false;
	            }
	        });
	    }
		
		public void handleAOSPVib() {
	        final CheckBoxPreference aosp_vib = (CheckBoxPreference) findPreference("vib_toggle");
	        int AOSP_VIB = getInt(AOSP_VIBRATION, 0);
	        aosp_vib.setChecked(AOSP_VIB != 0);
	        aosp_vib.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
	            public boolean onPreferenceClick(Preference preference) {
	                if (aosp_vib.isChecked()) {
	                	putInt(AOSP_VIBRATION,1);
	                    Log.d("harsh_debug","harsh_aosp_vib=>1");
	                } else {
	                	putInt(AOSP_VIBRATION,0);
	                    Log.d("harsh_debug","harsh_aosp_vib=>0");
	                }
	                return false;
	            }
	        });
	    }
		
		public void handleAOSPOrient() {
	        final CheckBoxPreference aosp_oriet = (CheckBoxPreference) findPreference("rot_toggle");
	        int AOSP_ROT = getInt(AOSP_ROTATION, 0);
	        aosp_oriet.setChecked(AOSP_ROT != 0);
	        aosp_oriet.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
	            public boolean onPreferenceClick(Preference preference) {
	                if (aosp_oriet.isChecked()) {
	                	putInt(AOSP_ROTATION,1);
	                    Log.d("harsh_debug","harsh_aosp_orient=>1");
	                } else {
	                	putInt(AOSP_ROTATION,0);
	                    Log.d("harsh_debug","harsh_aosp_orient=>0");
	                }
	                return false;
	            }
	        });
	    }
		
		public void handleRinger() {
	        final CheckBoxPreference ascend_ring = (CheckBoxPreference) findPreference("ascending_toggle");
	        int ringer = getInt(ASCEND_RING, 0);
	        ascend_ring.setChecked(ringer != 0);
	        ascend_ring.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
	            public boolean onPreferenceClick(Preference preference) {
	                if (ascend_ring.isChecked()) {
	                	putInt(ASCEND_RING,1);
	                    Log.d("harsh_debug","harsh_ascend_ring=>1");
	                } else {
	                	putInt(ASCEND_RING,0);
	                    Log.d("harsh_debug","harsh_ascend_ring=>0");
	                }
	                return false;
	            }
	        });
	    }
		
		public void handleLogger() {
	        final CheckBoxPreference logger = (CheckBoxPreference) findPreference("log_toggle");
	        int cocore = Utils.SU_retVal("echo $(uname -r) | grep -i -q cocore");
	        final File log_enable = new File(LOGGER);
	        logger.setChecked(log_enable.exists());
	        if (cocore == 0)
	            logger.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
	                public boolean onPreferenceClick(Preference preference) {
	                    if (logger.isChecked()) {
	                        new SU().execute("touch "+LOGGER,"chmod 777 "+LOGGER);
	                        Log.d("harsh_debug","logger enabled");
	                    } else {
	                        new SU().execute("rm "+LOGGER);
	                        Log.d("harsh_debug","logger disabled");
	                    }
	                    return false;
	                }
	            });
	        else {
	            logger.setEnabled(false);
	            logger.setSummary("Unsupported kernel");
	            Log.e("harsh_debug","Logger:not supported");
	        }
	    }
		
		public void handleSysctl() {
	        final CheckBoxPreference sysctl_switch = (CheckBoxPreference) findPreference("sys_toggle");
	        int var1 = Utils.SU_retVal("ls "+SYSCTL1+" | grep -q sysctl.conf");
	        int var2 = Utils.SU_retVal("ls "+INITD+" | grep -q 04_sysctl");
	        sysctl_switch.setChecked(var1 == 0 && var2 == 0);
	        sysctl_switch.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
	            public boolean onPreferenceClick(Preference preference) {
	                if (sysctl_switch.isChecked()) {
	                    Utils.mountSystemRW();
	                    showDialog("Warning","This tweaks are EXPERIMENTAL and their effects are unknown.They may or may not affect system performance.");
	                    Utils.copyAssets("04_sysctl",INITD,777,getActivity().getApplicationContext());
	                    Utils.copyAssets("sysctl.conf",SYSCTL1,644,getActivity().getApplicationContext());
	                    new SU().execute("sysctl -p");
	                    Log.d("harsh_debug","sysctl tweaks enabled");
	                } else {
	                    Utils.mountSystemRW();
	                    ClearSys();
	                    Utils.copyAssets("sysctl.conf_orig",SYSCTL1,644,getActivity().getApplicationContext());
	                    new SU().execute("cp -f /system/etc/sysctl.conf_orig /system/etc/sysctl.conf","rm /system/etc/sysctl.conf_orig","sysctl -p");
	                    ClearSys();
	                    Log.d("harsh_debug","sysctl tweaks disabled");
	                }
	                return false;
	            }
	        });
	    }
		
		public void handleUnplug() {
	        final CheckBoxPreference unplug_wake = (CheckBoxPreference) findPreference("unplug_wake");
	        int uwake = getInt(UNPLUG_WAKE, 0);
	        unplug_wake.setChecked(uwake != 0);
	        unplug_wake.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
	            public boolean onPreferenceClick(Preference preference) {
	                if (unplug_wake.isChecked()) {
	                	putInt(UNPLUG_WAKE,1);
	                    Log.d("harsh_debug","harsh_unplug=>1");
	                    ShowToast("Reboot is Required");
	                } else {
	                	putInt(UNPLUG_WAKE,0);
	                    Log.d("harsh_debug","harsh_unplug=>0");
	                    ShowToast("Reboot is Required");
	                }
	                return false;
	            }
	        });
	    }
		
		public void handleAllRotation() {
	        final CheckBoxPreference all_rotate = (CheckBoxPreference) findPreference("allrot_toggle");
	        int val = getInt(ALL_ROTATE, 0);
	        all_rotate.setChecked(val != 0);
	        all_rotate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
	            public boolean onPreferenceClick(Preference preference) {
	                if (all_rotate.isChecked()) {
	                	putInt(ALL_ROTATE,1);
	                    Log.d("harsh_debug","harsh_rotate=>1");
	                    ShowToast("Reboot is Required");
	                } else {
	                	putInt(ALL_ROTATE,0);
	                    Log.d("harsh_debug","harsh_rotate=>0");
	                    ShowToast("Reboot is Required");
	                }
	                return false;
	            }
	        });
	    }
		
		public void handleNavigation() {
	        final CheckBoxPreference cb = (CheckBoxPreference) findPreference("nav_toggle");
	        int val = getInt(NAVIGATION, 0);
	        cb.setChecked(val != 0);
	        cb.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
	            public boolean onPreferenceClick(Preference preference) {
	                if (cb.isChecked()) {
	                	putInt(NAVIGATION,1);
	                    Log.d("harsh_debug","harsh_navigation=>1");
	                    ShowToast("Reboot is Required");
	                } else {
	                	putInt(NAVIGATION,0);
	                    Log.d("harsh_debug","harsh_navigation=>0");
	                    ShowToast("Reboot is Required");
	                }
	                return false;
	            }
	        });
	    }
		
		public void handleIME() {
	        final CheckBoxPreference cb = (CheckBoxPreference) findPreference("ime_toggle");
	        int val = getInt(IME, 0);
	        cb.setChecked(val != 0);
	        cb.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
	            public boolean onPreferenceClick(Preference preference) {
	                if (cb.isChecked()) {
	                	putInt(IME,1);
	                    Log.d("harsh_debug","harsh_ime=>1");
	                    ShowToast("Reboot is Required");
	                } else {
	                	putInt(IME,0);
	                    Log.d("harsh_debug","harsh_ime=>0");
	                    ShowToast("Reboot is Required");
	                }
	                return false;
	            }
	        });
	    }
		
		public void handleHeadsetWarning() {
	        final CheckBoxPreference cb = (CheckBoxPreference) findPreference("hs_toggle");
	        int val = getInt(HEADSET, 1);
	        cb.setChecked(val != 0);
	        cb.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
	            public boolean onPreferenceClick(Preference preference) {
	                if (cb.isChecked()) {
	                	putInt(HEADSET,1);
	                    Log.d("harsh_debug","harsh_volume=>1");
	                } else {
	                	putInt(HEADSET,0);
	                    showDialog("Warning...","Listening to Loud music for longer time can damage your ear and lead to hear loss.");
	                    Log.d("harsh_debug","harsh_volume=>0");
	                }
	                return false;
	            }
	        });
	    }
		
		public void handleWiFiNotification() {
	        final CheckBoxPreference cb = (CheckBoxPreference) findPreference("wifi_notif_toggle");
	        int val = getInt(WIFI_NOTIF, 0);
	        cb.setChecked(val != 0);
	        cb.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
	            public boolean onPreferenceClick(Preference preference) {
	                if (cb.isChecked()) {
	                	putInt(WIFI_NOTIF,1);
	                    Log.d("harsh_debug","harsh_wifi_notif=>1");
	                } else {
	                	putInt(WIFI_NOTIF,0);
	                    Log.d("harsh_debug","harsh_wifi_notif=>0");
	                }
	                return false;
	            }
	        });
	    }
		
		public void handleTWScroll() {
	        final CheckBoxPreference cb = (CheckBoxPreference) findPreference("tw_pg_toggle");
	        int val = getInt(TW_SCROLL, 0);
	        cb.setChecked(val != 0);
	        cb.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
	            public boolean onPreferenceClick(Preference preference) {
	                if (cb.isChecked()) {
	                	putInt(TW_SCROLL,1);
	                    Log.d("harsh_debug","harsh_tw_scroll=>1");
	                } else {
	                	putInt(TW_SCROLL,0);
	                    Log.d("harsh_debug","harsh_tw_scroll=>0");
	                }
	                return false;
	            }
	        });
	    }
		
		public void handleFont() {
	        PreferenceScreen prefs = getPreferenceScreen();
	        ListPreference mFontrpef = (ListPreference) prefs.findPreference("font");
	        mFontrpef.setOnPreferenceChangeListener(
	                new Preference.OnPreferenceChangeListener() {
	                    @Override
	                    public boolean onPreferenceChange(Preference preference,Object newValue) {
	                        if (Integer.valueOf((String) newValue) == 0) {
	                            Utils.copyAssets("Roboto-Regular.ttf","/system/fonts",644,getActivity().getApplicationContext());
	                            showhotbootDialog();
	                            Log.i("harsh_debug","Changed font to Roboto");
	                        } else {
	                            Utils.copyAssets("Ubuntu.ttf","/system/fonts",644,getActivity().getApplicationContext());
	                            new SU().execute("mv /system/fonts/Ubuntu.ttf /system/fonts/Roboto-Regular.ttf");
	                            showhotbootDialog();
	                            Log.i("harsh_debug","Changed font to Ubuntu");
	                        }
	                        return true;
	                    }
	                });
	    }

	    public void handleQuickPanelScroll() {
	        final CheckBoxPreference cb = (CheckBoxPreference) findPreference("quickpanel_scroll");
	        int val = getInt(QUICK_SCROLL, 0);
	        cb.setChecked(val != 0);
	        cb.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
	            public boolean onPreferenceClick(Preference preference) {
	                if (cb.isChecked()) {
	                	putInt(QUICK_SCROLL,1);
	                    Log.d("harsh_debug","harsh_quick_scroll=>1");
	                } else {
	                	putInt(QUICK_SCROLL,0);
	                    ShowToast("Reboot is Required");
	                    Log.d("harsh_debug","harsh_quick_scroll=>0");
	                }
	                return false;
	            }
	        });
	    }
	    
	    public void handleStatusbarTransparancy() {
	    	PreferenceScreen prefs = getPreferenceScreen();
	        ListPreference mListPref = (ListPreference) prefs.findPreference("statusbar_trans");
	        mListPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
				
				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue) {
					putInt(STATUSBAR_TRANS, Integer.parseInt((String) newValue));
					return true;
				}
			});
	    }
	    
	    public void handleUserTile() {
	    	final PreferenceScreen ps = (PreferenceScreen) findPreference("usertile");
	    	ps.setOnPreferenceClickListener(new OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference arg0) {
					showDialog("Usage", "Put two images namely my_prof_normal.png and my_prof_pressed.png into /data/HarshJelly folder (create if not created) and set permission to 777 for both images.");
					return false;
				}
			});
	    }
	    
	    public void updateUserName() {
	    	PreferenceScreen prefs = getPreferenceScreen();
	    	EditTextPreference mETPref = (EditTextPreference) prefs.findPreference("user_name");
	    	mETPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener(){
	    	    public boolean onPreferenceChange(Preference preference, Object newValue) {
	    	    	String val = (String) newValue;
	    	    	Settings.System.putString(cr, USERNAME, val.toUpperCase());
	    	        return true;
	    	    }
	    	});
	    }
	    
	    public void showDialog(String title, String msg) {
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setTitle(title);
	        builder.setMessage(msg);
	        builder.setCancelable(false);
	        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int id) {}
	        });
	        AlertDialog dialog = builder.create();
	        dialog.show();
	    }
	    
	    public void ShowToast(String msg) {
	        Toast.makeText(getActivity().getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
	    }
	    
	    public void showhotbootDialog() {
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setMessage(R.string.hotboot_msg);
	        builder.setTitle(R.string.warning);
	        builder.setCancelable(false);
	        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int id) {
	                new SU().execute("pkill -f system_server");
	            }
	        });
	        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int id) {}
	        });
	        AlertDialog dialog = builder.create();
	        dialog.show();
	    }
	    
	    public int getInt(String key, int defValue) {
	    	return Settings.System.getInt(cr, key, defValue);
	    }
	    
	    public void putInt(String key, int val) {
	    	Settings.System.putInt(cr, key, val);
	    }
	}
    
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                startActivity(new Intent(this, About.class));
                return true;
            case R.id.hotboot:
                showhotbootDialog();
                return true;
            case R.id.help:
                startActivity(new Intent(this, Help.class));
                return true;
            case R.id.reset:
                ResetToDefaults();
                return true;
            case R.id.info:
                startActivity(new Intent(this, Info.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    public static void ClearSys() {
        Utils.mountSystemRW();
        new SU().execute("rm /system/etc/init.d/04_sysctl", "rm /system/etc/sysctl.conf");
    }

    public void showhotbootDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(R.string.hotboot_msg);
        builder.setTitle(R.string.warning);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                new SU().execute("pkill -f system_server");
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {}
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    
    public void ResetToDefaults() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(R.string.rest_to_def);
        builder.setTitle(R.string.warning);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.d("harsh_debug","resetting all settings...");
                Settings.System.putInt(getContentResolver(), CRT_ANIM, 1);
                new SU().execute("echo 1 > "+FBDELAY,"echo 350 > "+FBDELAY_MS);
                Utils.mountSystemRW();
                Utils.copyAssets("03_crt",INITD,777,getApplicationContext());
                Settings.System.putInt(getContentResolver(), KILLER,1);
                Settings.System.putInt(getContentResolver(), ASCEND_RING,0);
                Settings.System.putInt(getContentResolver(), UNPLUG_WAKE,0);
                Settings.System.putInt(getContentResolver(), ALL_ROTATE,0);
                Settings.System.putInt(getContentResolver(), NAVIGATION,0);
                Settings.System.putInt(getContentResolver(), IME,0);
                Settings.System.putInt(getContentResolver(), HEADSET,1);
                Settings.System.putInt(getContentResolver(), AOSP_VIBRATION,0);
                Settings.System.putInt(getContentResolver(), AOSP_ROTATION,0);
                Settings.System.putInt(getContentResolver(), WIFI_NOTIF,0);
                Settings.System.putInt(getContentResolver(), TW_SCROLL,0);
                Settings.System.putInt(getContentResolver(), QUICK_SCROLL,1);
                new SU().execute("rm "+LOGGER);
                Utils.mountSystemRW();
                ClearSys();
                Utils.copyAssets("sysctl.conf_orig",SYSCTL1,644,getApplicationContext());
                new SU().execute("cp -f /system/etc/sysctl.conf_orig /system/etc/sysctl.conf","rm /system/etc/sysctl.conf_orig","sysctl -p");
                ClearSys();
                finish();
                startActivity(getIntent());
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {}
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
