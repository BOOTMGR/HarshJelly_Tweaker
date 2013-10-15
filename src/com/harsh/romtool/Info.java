/*
 *      HarshJelly Tweaker - An app to Tweak HarshJelly ROM
 *      Author : Harsh Panchal <panchal.harsh18@gmail.com, mr.harsh@xda-developers.com>
 */
package com.harsh.romtool;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Info extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);
        TextView hjt = (TextView) findViewById(R.id.hjt_version);
        hjt.setText("HarshJelly Tweaker Version : "+getString(R.string.info_2));
        TextView hj = (TextView) findViewById(R.id.hj_version);
        hj.setText("HarshJelly Version : "+ Utils.SU_wop("getprop ro.harshjelly.version"));
        TextView kernel = (TextView) findViewById(R.id.kernel);
        kernel.setText("Kernel : "+Utils.SU_wop("uname -r | cut -b 8-25"));
    }

}
