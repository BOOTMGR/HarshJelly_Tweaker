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
import android.widget.TextView;

public class Info extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);
        TextView hjt = (TextView) findViewById(R.id.hjt_version);
        TextView hj = (TextView) findViewById(R.id.hj_version);
        TextView kernel = (TextView) findViewById(R.id.kernel);
		hjt.setText("HarshJelly Tweaker Version : "+getString(R.string.info_2));
		hj.setText("HarshJelly Version : "+ Utils.SU_wop("getprop ro.harshjelly.version"));
        kernel.setText("Kernel : "+Utils.SU_wop("uname -r | cut -b 8-50"));
    }

}
