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
        // Inflate the menu; this adds items to the action bar if it is present.
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
                    try {
                        Process p = Runtime.getRuntime().exec(new String[] { "su", "-c", "echo 1 > ", FBDELAY });
                        p.waitFor();
                        p = Runtime.getRuntime().exec(new String[] { "su", "-c", "echo 350 > ", FBDELAY_MS });
                        p.waitFor();
                        mountSystemRW();
                        copyAssets("03_crt",INITD,777);
                    } catch (IOException e) {
                        ShowToast("No SU Rights");
                        Log.e("harsh_debug","Failed to get SU Rights or Unsupported Kernel");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    Settings.System.putInt(getContentResolver(), CRT_ANIM, 0);
                    Log.d("harsh_debug","harsh_crt=>0");
                    try {
                        Process p = Runtime.getRuntime().exec(new String[] { "su", "-c", "echo 0 > ", FBDELAY });
                        p.waitFor();
                        p = Runtime.getRuntime().exec(new String[] { "su", "-c", "echo 0 > ", FBDELAY_MS });
                        p.waitFor();
                        mountSystemRW();
                        copyAssets("99_crtoff",INITD,777);
                    } catch (IOException e) {
                        ShowToast("No SU Rights or unsupported Kernel");
                        Log.e("harsh_debug","Failed to get SU Rights or Unsupported Kernel");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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
                    try {
                        Process p = Runtime.getRuntime().exec(new String[] {"su","-c","touch",LOGGER});
                        p.waitFor();
                        Runtime.getRuntime().exec(new String[]{"su", "-c", "chmod", "777", LOGGER});
                    } catch (Exception e) {
                        Log.e("harsh_debug","Failed to create logger", e);
                    }
                    Log.d("harsh_debug","logger enabled");
                } else {
                    try {
                        Process p = Runtime.getRuntime().exec(new String[] {"su","-c","rm",LOGGER});
                        p.waitFor();
                    } catch (Exception e) {
                        Log.e("harsh_debug","Failed to remove logger", e);
                    }
                    Log.d("harsh_debug","logger disabled");
                }
                return false;
            }
        });
    }

    public void SetSysctlListner() {
        final CheckBoxPreference sysctl_switch = (CheckBoxPreference) findPreference("sys_toggle");
        int var1=0;
        int var2=0;
        try {
            Process process = new ProcessBuilder().command("su" ,"-c" ,"ls", SYSCTL1, "|", "grep", "-q", "sysctl.conf").start();
            process.waitFor();
            var1 = process.exitValue();
            process.destroy();
            Process process2 = new ProcessBuilder().command("su" ,"-c" ,"ls", INITD, "|", "grep", "-q", "04_sysctl").start();
            process2.waitFor();
            var2 = process2.exitValue();
            process2.destroy();
        } catch (IOException e) {
            Log.e("harsh_debug", "Failed to execute process", e);
        } catch (InterruptedException e) {
            Log.e("harsh_debug", "Process Interuppted", e);
        }
        sysctl_switch.setChecked(var1 == 0 && var2 == 0);
        sysctl_switch.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
            public boolean onPreferenceClick(Preference preference) {
                if (sysctl_switch.isChecked()) {
                    mountSystemRW();
                    copyAssets("04_sysctl",INITD,777);
                    copyAssets("sysctl.conf",SYSCTL1,644);
                    try {
                        Runtime.getRuntime().exec(new String[]{"su", "-c", "sysctl", "-p"});
                    } catch (Exception e) {
                        Log.e("harsh_debug", "Failed to execute process", e);
                    }
                    Log.d("harsh_debug","sysctl tweaks enabled");
                } else {
                    mountSystemRW();
                    ClearSys();
                    copyAssets("sysctl.conf_orig",SYSCTL1,644);
                    try {
                        Runtime.getRuntime().exec(new String[]{"su", "-c", "cp", SYSCTL1+"/sysctl.conf_orig" , SYSCTL1+"/sysctl.conf"});
                        Runtime.getRuntime().exec(new String[]{"su", "-c", "rm", SYSCTL1+"/sysctl.conf_orig"});
                        Runtime.getRuntime().exec(new String[]{"su", "-c", "chmod", "644", SYSCTL1+"/sysctl.conf"});
                        Runtime.getRuntime().exec(new String[]{"su", "-c", "sysctl", "-p"});
                    } catch (IOException e) {
                        Log.e("harsh_debug", "Error in Setting permission", e);
                        e.printStackTrace();
                    }
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
            String out = new String();
            try {
                Process p = Runtime.getRuntime().exec(new String[]{"su", "-c", "system/bin/sh"});
                DataOutputStream stdin = new DataOutputStream(p.getOutputStream());
                stdin.writeBytes("head -1 /sys/kernel/fsync/mode\n");
                InputStream stdout = p.getInputStream();
                byte[] buffer = new byte[4096];
                int read;
                while(true){
                    read = stdout.read(buffer);
                    out += new String(buffer, 0, read);
                    if(read<4096){
                        break;
                    }
                }
            } catch ( Exception e) {
                ShowToast("Error Occured");
                Log.e("harsh_debug","Failed reading sysfs",e);
            }
            int val = Integer.parseInt(Character.toString(out.charAt(0)));
            cb.setChecked(val != 0);
            cb.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
                public boolean onPreferenceClick(Preference preference) {
                    if (cb.isChecked()) {
                        try {
                            Process p = Runtime.getRuntime().exec(new String[] { "su", "-c", "echo 1 > ", FSYNC });
                            p.waitFor();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("harsh_debug","Error writing sysfs");
                        }
                        mountSystemRW();
                        copyAssets("05_fsync",INITD,777);
                        Log.d("harsh_debug", "fsync=>1");
                    } else {
                        try {
                            Process p = Runtime.getRuntime().exec(new String[] { "su", "-c", "echo 0 > ", FSYNC });
                            p.waitFor();
                            mountSystemRW();
                            Runtime.getRuntime().exec(new String[] { "su", "-c", "rm", "/system/etc/init.d/05_fsync" });
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("harsh_debug","Error writing sysfs");
                        }
                        Log.d("harsh_debug", "fsync=>0");
                    }
                    return false;
                }
            });
        }else{
            cb.setSelectable(false);
            ShowToast("FSYNC:Not Supported");
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
        Process process = null;
        try {
            process = new ProcessBuilder().command("su" ,"-c" ,"rm", "/system/etc/init.d/04_sysctl").start();
            process.waitFor();
            process = new ProcessBuilder().command("su", "-c", "rm", "/system/etc/sysctl.conf").start();
            process.waitFor();
            process.destroy();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
            Process p = Runtime.getRuntime().exec(new String[] { "su", "-c", "cp", Environment.getExternalStorageDirectory().getPath()+"/"+script, path+"/"+script });
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
        try {
            Runtime.getRuntime().exec(new String[] { "su", "-c", "mount", "-o", "remount,rw", "/dev/block/mmcblk0p3", "/system" });
        } catch (IOException e) {
            Log.e("harsh_debug", "Failed to mount system as R/W", e);
        }
    }

    public void showhotbootDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(R.string.hotboot_msg);
        builder.setTitle(R.string.information);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Process process;
                try {
                    process = new ProcessBuilder().command("su" ,"-c" ,"pkill", "-f", "system_server").start();
                } catch (IOException e) {
                    Log.e("harsh_debug", "Failed to HotBoot", e);
                }
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
