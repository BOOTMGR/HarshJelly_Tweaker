/*
 *      HarshJelly Tweaker - An app to Tweak HarshJelly ROM
 *      Author : Harsh Panchal <panchal.harsh18@gmail.com, mr.harsh@xda-developers.com>
 */
package com.harsh.romtool;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class MainActivity extends PreferenceActivity {


    private static final String CRT_ANIM = "harsh_crt";
    private static final String KILLER = "harsh_killer";
    private static final String AOSP_VIBRATION = "harsh_aosp_vib";
    private static final String AOSP_ROTATION = "harsh_aosp_orient";
    private static final String ASCEND_RING = "harsh_ascend_ring";
    private static final String UNPLUG_WAKE = "harsh_unplug";
    private static final String ALL_ROTATE = "harsh_rotate";
    private static final String NAVIGATION = "harsh_navigation";
    private static final String IME = "harsh_ime";
    private static final String SCROLL = "harsh_scroll";
    private static final String FBDELAY = "/sys/module/fbearlysuspend/parameters/fbdelay";
    private static final String FBDELAY_MS = "/sys/module/fbearlysuspend/parameters/fbdelay_ms";
    private static final String LOGGER = "/data/logger";
    private static final String SYSCTL1 = "/system/etc";
    private static final String INITD = "/system/etc/init.d";
    private static final String FSYNC = "/sys/kernel/fsync/mode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.main);
        Log.i("harsh_debug", "===========HarshJelly Tweaker Launched===========");
        SetCRTListner();
        SetKillerListner();
        SetAOSPVibListner();
        SetAOSPOrientListner();
        SetRingerListner();
        SetLoggerListner();
        SetSysctlListner();
        SetSysctlListner();
        SetUnplugListener();
        SetAllRotateListener();
        SetNavListener();
        SetIMEListener();
        SetFSYNCListener();
        SetScrollListener();
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void SetCRTListner() {
        final CheckBoxPreference crt_toggle = (CheckBoxPreference) findPreference("crt_toggle");
        int crt = Settings.System.getInt(getContentResolver(),CRT_ANIM, 0);
        crt_toggle.setChecked(crt != 0);
        crt_toggle.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
            public boolean onPreferenceClick(Preference preference) {
                if (crt_toggle.isChecked()) {
                    Settings.System.putInt(getContentResolver(), CRT_ANIM, 1);
                    Log.d("harsh_debug","harsh_crt=>1");
                    new SU().execute("echo 1 > "+FBDELAY,"echo 350 > "+FBDELAY_MS);
                    mountSystemRW();
                    copyAssets("03_crt",INITD,777);
                } else {
                    Settings.System.putInt(getContentResolver(), CRT_ANIM, 0);
                    Log.d("harsh_debug","harsh_crt=>0");
                    new SU().execute("echo 0 > "+FBDELAY,"echo 0 > "+FBDELAY_MS);
                    mountSystemRW();
                    copyAssets("99_crtoff",INITD,777);
                }
                return false;
            }
        });
    }

    public void SetKillerListner() {
        final CheckBoxPreference killer = (CheckBoxPreference) findPreference("killer_toggle");
        int Killer = Settings.System.getInt(getContentResolver(),KILLER, 0);
        killer.setChecked(Killer != 0);
        killer.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
            public boolean onPreferenceClick(Preference preference) {
                if (killer.isChecked()) {
                    Settings.System.putInt(getContentResolver(), KILLER,1);
                    Log.d("harsh_debug","harsh_killer=>1");
                } else {
                    Settings.System.putInt(getContentResolver(), KILLER,0);
                    Log.d("harsh_debug","harsh_killer=>0");
                }
                return false;
            }
        });
    }

    public void SetAOSPVibListner() {
        final CheckBoxPreference aosp_vib = (CheckBoxPreference) findPreference("vib_toggle");
        int AOSP_VIB = Settings.System.getInt(getContentResolver(),AOSP_VIBRATION, 0);
        aosp_vib.setChecked(AOSP_VIB != 0);
        aosp_vib.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
            public boolean onPreferenceClick(Preference preference) {
                if (aosp_vib.isChecked()) {
                    Settings.System.putInt(getContentResolver(), AOSP_VIBRATION,1);
                    Log.d("harsh_debug","harsh_aosp_vib=>1");
                } else {
                    Settings.System.putInt(getContentResolver(), AOSP_VIBRATION,0);
                    Log.d("harsh_debug","harsh_aosp_vib=>0");
                }
                return false;
            }
        });
    }

    public void SetAOSPOrientListner() {
        final CheckBoxPreference aosp_oriet = (CheckBoxPreference) findPreference("rot_toggle");
        int AOSP_ROT = Settings.System.getInt(getContentResolver(),AOSP_ROTATION, 0);
        aosp_oriet.setChecked(AOSP_ROT != 0);
        aosp_oriet.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
            public boolean onPreferenceClick(Preference preference) {
                if (aosp_oriet.isChecked()) {
                    Settings.System.putInt(getContentResolver(), AOSP_ROTATION,1);
                    Log.d("harsh_debug","harsh_aosp_orient=>1");
                } else {
                    Settings.System.putInt(getContentResolver(), AOSP_ROTATION,0);
                    Log.d("harsh_debug","harsh_aosp_orient=>0");
                }
                return false;
            }
        });
    }

    public void SetRingerListner() {
        final CheckBoxPreference ascend_ring = (CheckBoxPreference) findPreference("ascending_toggle");
        int ringer = Settings.System.getInt(getContentResolver(),ASCEND_RING, 0);
        ascend_ring.setChecked(ringer != 0);
        ascend_ring.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
            public boolean onPreferenceClick(Preference preference) {
                if (ascend_ring.isChecked()) {
                    Settings.System.putInt(getContentResolver(), ASCEND_RING,1);
                    Log.d("harsh_debug","harsh_ascend_ring=>1");
                } else {
                    Settings.System.putInt(getContentResolver(), ASCEND_RING,0);
                    Log.d("harsh_debug","harsh_ascend_ring=>0");
                }
                return false;
            }
        });
    }

    public void SetLoggerListner() {
        final CheckBoxPreference logger = (CheckBoxPreference) findPreference("log_toggle");
        final File log_enable = new File(LOGGER);
        logger.setChecked(log_enable.exists());
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
    }

    public void SetSysctlListner() {
        final CheckBoxPreference sysctl_switch = (CheckBoxPreference) findPreference("sys_toggle");
        int var1 = new Utils().SU_retVal("ls "+SYSCTL1+" | grep -q sysctl.conf");
        int var2 = new Utils().SU_retVal("ls "+INITD+" | grep -q 04_sysctl");
        sysctl_switch.setChecked(var1 == 0 && var2 == 0);
        sysctl_switch.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
            public boolean onPreferenceClick(Preference preference) {
                if (sysctl_switch.isChecked()) {
                    mountSystemRW();
                    copyAssets("04_sysctl",INITD,777);
                    copyAssets("sysctl.conf",SYSCTL1,644);
                    new SU().execute("sysctl -p");
                    Log.d("harsh_debug","sysctl tweaks enabled");
                } else {
                    mountSystemRW();
                    ClearSys();
                    copyAssets("sysctl.conf_orig",SYSCTL1,644);
                    new SU().execute("cp -f /system/etc/sysctl.conf_orig /system/etc/sysctl.conf","rm /system/etc/sysctl.conf_orig","sysctl -p");
                    ClearSys();
                    Log.d("harsh_debug","sysctl tweaks disabled");
                }
                return false;
            }
        });
    }

    public void SetUnplugListener() {
        final CheckBoxPreference unplug_wake = (CheckBoxPreference) findPreference("unplug_wake");
        int uwake = Settings.System.getInt(getContentResolver(),UNPLUG_WAKE, 0);
        unplug_wake.setChecked(uwake != 0);
        unplug_wake.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
            public boolean onPreferenceClick(Preference preference) {
                if (unplug_wake.isChecked()) {
                    Settings.System.putInt(getContentResolver(), UNPLUG_WAKE,1);
                    Log.d("harsh_debug","harsh_unplug=>1");
                    ShowToast("Reboot is Required");
                } else {
                    Settings.System.putInt(getContentResolver(), UNPLUG_WAKE,0);
                    Log.d("harsh_debug","harsh_unplug=>0");
                    ShowToast("Reboot is Required");
                }
                return false;
            }
        });
    }

    public void SetAllRotateListener() {
        final CheckBoxPreference all_rotate = (CheckBoxPreference) findPreference("allrot_toggle");
        int val = Settings.System.getInt(getContentResolver(),ALL_ROTATE, 0);
        all_rotate.setChecked(val != 0);
        all_rotate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
            public boolean onPreferenceClick(Preference preference) {
                if (all_rotate.isChecked()) {
                    Settings.System.putInt(getContentResolver(), ALL_ROTATE,1);
                    Log.d("harsh_debug","harsh_rotate=>1");
                    ShowToast("Reboot is Required");
                } else {
                    Settings.System.putInt(getContentResolver(), ALL_ROTATE,0);
                    Log.d("harsh_debug","harsh_rotate=>0");
                    ShowToast("Reboot is Required");
                }
                return false;
            }
        });
    }

    public void SetNavListener() {
        final CheckBoxPreference cb = (CheckBoxPreference) findPreference("nav_toggle");
        int val = Settings.System.getInt(getContentResolver(),NAVIGATION, 0);
        cb.setChecked(val != 0);
        cb.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
            public boolean onPreferenceClick(Preference preference) {
                if (cb.isChecked()) {
                    Settings.System.putInt(getContentResolver(), NAVIGATION,1);
                    Log.d("harsh_debug","harsh_navigation=>1");
                    ShowToast("Reboot is Required");
                } else {
                    Settings.System.putInt(getContentResolver(), NAVIGATION,0);
                    Log.d("harsh_debug","harsh_navigation=>0");
                    ShowToast("Reboot is Required");
                }
                return false;
            }
        });
    }

    public void SetIMEListener() {
        final CheckBoxPreference cb = (CheckBoxPreference) findPreference("ime_toggle");
        int val = Settings.System.getInt(getContentResolver(),IME, 0);
        cb.setChecked(val != 0);
        cb.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
            public boolean onPreferenceClick(Preference preference) {
                if (cb.isChecked()) {
                    Settings.System.putInt(getContentResolver(), IME,1);
                    Log.d("harsh_debug","harsh_ime=>1");
                    ShowToast("Reboot is Required");
                } else {
                    Settings.System.putInt(getContentResolver(), IME,0);
                    Log.d("harsh_debug","harsh_ime=>0");
                    ShowToast("Reboot is Required");
                }
                return false;
            }
        });
    }

    public void SetFSYNCListener() {
        final CheckBoxPreference cb = (CheckBoxPreference) findPreference("fsync_toggle");
        final File f = new File(FSYNC);
        if(f.exists()) {
            String out = new Utils().SU_wop("head -1 /sys/kernel/fsync/mode");
            int val = Integer.parseInt(Character.toString(out.charAt(0)));
            cb.setChecked(val != 0);
            cb.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
                public boolean onPreferenceClick(Preference preference) {
                    if (cb.isChecked()) {
                        new SU().execute("echo 1 > "+FSYNC);
                        mountSystemRW();
                        copyAssets("02_fsync",INITD,777);
                        Log.d("harsh_debug", "fsync=>1");
                    } else {
                        new SU().execute("echo 0 > "+FSYNC,"rm /system/etc/init.d/02_fsync");
                        Log.d("harsh_debug", "fsync=>0");
                    }
                    return false;
                }
            });
        }else{
            cb.setEnabled(false);
            cb.setSummary("Unsupported kernel");
            Log.e("harsh_debug","FSYNC:Unsupported Kernel");
        }
    }

    public void SetScrollListener() {
        final CheckBoxPreference cb = (CheckBoxPreference) findPreference("scroll_toggle");
        int val = Settings.System.getInt(getContentResolver(),SCROLL, 0);
        cb.setChecked(val != 0);
        cb.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
            public boolean onPreferenceClick(Preference preference) {
                if (cb.isChecked()) {
                    Settings.System.putInt(getContentResolver(), SCROLL,1);
                    Log.d("harsh_debug","harsh_scroll=>1");
                    ShowToast("Reboot is Required");
                } else {
                    Settings.System.putInt(getContentResolver(), SCROLL,0);
                    Log.d("harsh_debug","harsh_scroll=>0");
                    ShowToast("Reboot is Required");
                }
                return false;
            }
        });
    }

    public void ClearSys() {
        mountSystemRW();
        new SU().execute("rm /system/etc/init.d/04_sysctl", "rm /system/etc/sysctl.conf");
    }

    public void copyAssets(String script,String path,int mode) {
        AssetManager assetManager = getAssets();
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(script);
            File outFile = new File(Environment.getExternalStorageDirectory().getPath(), script);
            out = new FileOutputStream(outFile);
            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch(IOException e) {
            Log.e("harsh_debug", "Failed to handle: " + script, e);
        }
        try {
            Process p = Runtime.getRuntime().exec(new String[] { "su", "-c", "cp", "-f", Environment.getExternalStorageDirectory().getPath()+"/"+script, path+"/"+script });
            p.waitFor();
            p = Runtime.getRuntime().exec(new String[] { "su", "-c", "rm", Environment.getExternalStorageDirectory().getPath()+"/"+script });
            p = Runtime.getRuntime().exec(new String[] { "su", "-c", "chmod", Integer.toString(mode), path+"/"+script });
            p.waitFor();
        } catch (IOException e) {
            Log.e("harsh_debug", "Failed to move: " + script, e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(script.equals("03_crt") || script.equals("99_crtoff")){
            if(script.equals("03_crt")) {
                try {
                    Process p = Runtime.getRuntime().exec(new String[] { "su", "-c", "rm", "/system/etc/init.d/99_crtoff" });
                    p.waitFor();
                } catch (IOException e) {
                    Log.e("harsh_debug", "Failed to remove: " + script, e);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else {
                try {
                    Process p = Runtime.getRuntime().exec(new String[] { "su", "-c", "rm", "/system/etc/init.d/03_crt" });
                    p.waitFor();
                } catch (IOException e) {
                    Log.e("harsh_debug", "Failed to remove: " + script, e);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

    public void mountSystemRW() {
        new SU().execute("mount -o remount,rw /dev/block/mmcblk0p3 /system");
    }

    public void showhotbootDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(R.string.hotboot_msg);
        builder.setTitle(R.string.information);
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

    public void ShowToast(String msg) {
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }
}
