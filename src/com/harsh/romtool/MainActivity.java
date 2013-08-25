package com.harsh.romtool;

import java.io.IOException;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	
	private static final String CRT_ANIM = "harsh_crt";
	private static final String KILLER = "harsh_killer";
	private static final String AOSP_VIBRATION = "harsh_aosp_vib";
	private static final String AOSP_ROTATION = "harsh_aosp_orient";
	private static final String ASCEND_RING = "harsh_ascend_ring";
	private static final String FBDELAY = "/sys/module/fbearlysuspend/parameters/fbdelay";
	private static final String FBDELAY_MS = "/sys/module/fbearlysuspend/parameters/fbdelay_ms";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Switch crt_anim = (Switch) findViewById(R.id.s_crt);
        Switch killer = (Switch) findViewById(R.id.s_killer);
        Switch aosp_vib = (Switch) findViewById(R.id.s_vib);
        Switch aosp_oriet = (Switch) findViewById(R.id.s_oriet);
        Switch ascend_ring = (Switch) findViewById(R.id.s_ascendring);
        int crt = Settings.System.getInt(getContentResolver(),CRT_ANIM, 0);
        int Killer = Settings.System.getInt(getContentResolver(),KILLER, 0);
        int AOSP_VIB = Settings.System.getInt(getContentResolver(),AOSP_VIBRATION, 0);
        int AOSP_ROT = Settings.System.getInt(getContentResolver(),AOSP_ROTATION, 0);
        int ringer = Settings.System.getInt(getContentResolver(),ASCEND_RING, 0);
        if(crt==0){
        	crt_anim.setChecked(false);
        }else{
        	crt_anim.setChecked(true);
        }
        if(Killer==0){
        	killer.setChecked(false);
        }else{
        	killer.setChecked(true);
        }
        if(AOSP_VIB==0){
        	aosp_vib.setChecked(false);
        }else{
        	aosp_vib.setChecked(true);
        }
        if(AOSP_ROT==0){
        	aosp_oriet.setChecked(false);
        }else{
        	aosp_oriet.setChecked(true);
        }
        if(ringer==0){
        	ascend_ring.setChecked(false);
        }else{
        	ascend_ring.setChecked(true);
        }
	        crt_anim.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		        @Override
		        public void onCheckedChanged(CompoundButton buttonView,
		        boolean isChecked) {
			        	if (isChecked) {
			        		Settings.System.putInt(getContentResolver(), CRT_ANIM,1);
			        		Log.d("harsh_debug","harsh_crt=>1");
			        		try {
								Runtime.getRuntime().exec(new String[] { "su", "-c", "echo 1 > ", FBDELAY });
								Runtime.getRuntime().exec(new String[] { "su", "-c", "echo 350 > ", FBDELAY_MS });
							} catch (IOException e) {
								Toast.makeText(getApplicationContext(), "No SU Rights", Toast.LENGTH_SHORT).show();
							} 
			        	} else {
			        		Settings.System.putInt(getContentResolver(), CRT_ANIM,0);
			        		Log.d("harsh_debug","harsh_crt=>0");
			        		try {
			        			Runtime.getRuntime().exec(new String[] { "su", "-c", "echo 0 > ", FBDELAY });
			        			Runtime.getRuntime().exec(new String[] { "su", "-c", "echo 0 > ", FBDELAY_MS });
							} catch (IOException e) {
								Toast.makeText(getApplicationContext(), "No SU Rights or Unsupported Kernel", Toast.LENGTH_SHORT).show();
								Log.e("harsh_debug","Failed to get SU Rights or Unsupported Kernel");
							} 
			        	}
		        }
	        });
	        
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
    
}
