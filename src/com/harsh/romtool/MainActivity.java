package com.harsh.romtool;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends Activity {
	
	
	private static final String CRT_ANIM = "harsh_crt";
	private static final String KILLER = "harsh_killer";
	private static final String AOSP_VIBRATION = "harsh_aosp_vib";
	private static final String AOSP_ROTATION = "harsh_aosp_orient";
	private static final String ASCEND_RING = "harsh_ascend_ring";
	private static final String FBDELAY = "/sys/module/fbearlysuspend/parameters/fbdelay";
	private static final String FBDELAY_MS = "/sys/module/fbearlysuspend/parameters/fbdelay_ms";
	private static final String LOGGER = "/data/logger";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SetCRTListner();
        SetKillerListner();
        SetAOSPVibListner();
        SetAOSPOrientListner();
        SetRingerListner();
        SetLoggerListner();
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
        default:
        return super.onOptionsItemSelected(item);
    }
    }

    public void SetCRTListner() {
        Switch crt_anim = (Switch) findViewById(R.id.s_crt);
        int crt = Settings.System.getInt(getContentResolver(),CRT_ANIM, 0);
        crt_anim.setChecked(crt != 0);
        crt_anim.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    Settings.System.putInt(getContentResolver(), CRT_ANIM,1);
                    Log.d("harsh_debug","harsh_crt=>1");
                    try {
                        Process p = Runtime.getRuntime().exec(new String[] { "su", "-c", "echo 1 > ", FBDELAY });
                        p.waitFor();
                        p = Runtime.getRuntime().exec(new String[] { "su", "-c", "echo 350 > ", FBDELAY_MS });
                        p.waitFor();
                        mountSystemRW();
                        copyAssets("03_crt");
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), "No SU Rights", Toast.LENGTH_SHORT).show();
                        Log.e("harsh_debug","Failed to get SU Rights or Unsupported Kernel");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    Settings.System.putInt(getContentResolver(), CRT_ANIM,0);
                    Log.d("harsh_debug","harsh_crt=>0");
                    try {
                        Process p = Runtime.getRuntime().exec(new String[] { "su", "-c", "echo 0 > ", FBDELAY });
                        p.waitFor();
                        p = Runtime.getRuntime().exec(new String[] { "su", "-c", "echo 0 > ", FBDELAY_MS });
                        p.waitFor();
                        mountSystemRW();
                        copyAssets("99_crtoff");
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), "No SU Rights or Unsupported Kernel", Toast.LENGTH_SHORT).show();
                        Log.e("harsh_debug","Failed to get SU Rights or Unsupported Kernel");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void SetKillerListner() {
        Switch killer = (Switch) findViewById(R.id.s_killer);
        int Killer = Settings.System.getInt(getContentResolver(),KILLER, 0);
        killer.setChecked(Killer != 0);
        killer.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    Settings.System.putInt(getContentResolver(), KILLER,1);
                    Log.d("harsh_debug","harsh_killer=>1");
                } else {
                    Settings.System.putInt(getContentResolver(), KILLER,0);
                    Log.d("harsh_debug","harsh_killer=>0");
                }
            }
        });
    }

    public void SetAOSPVibListner() {
        Switch aosp_vib = (Switch) findViewById(R.id.s_vib);
        int AOSP_VIB = Settings.System.getInt(getContentResolver(),AOSP_VIBRATION, 0);
        aosp_vib.setChecked(AOSP_VIB != 0);
        aosp_vib.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    Settings.System.putInt(getContentResolver(), AOSP_VIBRATION,1);
                    Log.d("harsh_debug","harsh_aosp_vib=>1");
                } else {
                    Settings.System.putInt(getContentResolver(), AOSP_VIBRATION,0);
                    Log.d("harsh_debug","harsh_aosp_vib=>0");
                }
            }
        });
    }

    public void SetAOSPOrientListner() {
        Switch aosp_oriet = (Switch) findViewById(R.id.s_oriet);
        int AOSP_ROT = Settings.System.getInt(getContentResolver(),AOSP_ROTATION, 0);
        aosp_oriet.setChecked(AOSP_ROT != 0);
        aosp_oriet.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    Settings.System.putInt(getContentResolver(), AOSP_ROTATION,1);
                    Log.d("harsh_debug","harsh_aosp_orient=>1");
                } else {
                    Settings.System.putInt(getContentResolver(), AOSP_ROTATION,0);
                    Log.d("harsh_debug","harsh_aosp_orient=>0");
                }
            }
        });
    }

    public void SetRingerListner() {
        Switch ascend_ring = (Switch) findViewById(R.id.s_ascendring);
        int ringer = Settings.System.getInt(getContentResolver(),ASCEND_RING, 0);
        ascend_ring.setChecked(ringer != 0);
        ascend_ring.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    Settings.System.putInt(getContentResolver(), ASCEND_RING,1);
                    Log.d("harsh_debug","harsh_ascend_ring=>1");
                } else {
                    Settings.System.putInt(getContentResolver(), ASCEND_RING,0);
                    Log.d("harsh_debug","harsh_ascend_ring=>0");
                }
            }
        });
    }

    public void SetLoggerListner() {
        Switch logger = (Switch) findViewById(R.id.s_logger);
        final File log_enable = new File(LOGGER);
        logger.setChecked(log_enable.exists());
        logger.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
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
            }
        });
    }
    
    public void copyAssets(String script) {
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
            	Process p = Runtime.getRuntime().exec(new String[] { "su", "-c", "cp", Environment.getExternalStorageDirectory().getPath()+"/"+script, "/system/etc/init.d/"+script });
            	p.waitFor();
				p = Runtime.getRuntime().exec(new String[] { "su", "-c", "rm", Environment.getExternalStorageDirectory().getPath()+"/"+script });
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
}
