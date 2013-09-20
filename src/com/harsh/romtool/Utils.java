/*
 *      HarshJelly Tweaker - An app to Tweak HarshJelly ROM
 *      Author : Harsh Panchal <panchal.harsh18@gmail.com, mr.harsh@xda-developers.com>
 *          This file Provides core function definitions for MainActivity.
 */
package com.harsh.romtool;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

class Utils {
    // SU_wop : Executes shell command as superuser and return the output as a String
    // Usage  : new Utils().SU_wop("command")
    public String SU_wop(String cmds) {
//     FLAG:0x2
        String out = null;
        try {
            out = new String();
            Process p = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(p.getOutputStream());
            os.writeBytes(cmds+"\n");
            os.writeBytes("exit\n");
            os.flush();
            InputStream stdout = p.getInputStream();
            byte[] buffer = new byte[4096];
            int read;
            while (true) {
                read = stdout.read(buffer);
                out += new String(buffer, 0, read);
                if (read < 4096) {
                    break;
                }
            }
        } catch (Exception e) {
            final Activity activity = new MainActivity();
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(activity, "Error Occured...!", Toast.LENGTH_SHORT).show();
                }
            });
            Log.e("harsh_debug", "Error executing SU command, flag:0x2");
        }
        return out;
    }
    // SU_retVal : Executes shell command as superuser and return the value of command executed
    //             It'll return either 0 or 1
    // Usage  : new Utils().SU_retVal("command")
    public int SU_retVal(String cmd) {
//     FLAG:0x3
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            final Activity activity = new MainActivity();
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(activity, "Error Occured...!", Toast.LENGTH_SHORT).show();
                }
            });
            Log.e("harsh_debug", "Error executing SU command, flag:0x3");
        }
        return process.exitValue();
    }
}
// SU : This Asynctask class Executes shell commands as Superuser in background
// Usage  : new SU().execute("command1","command2", ... )
class SU extends AsyncTask<String, Void, Void> {
   @Override
   protected Void doInBackground(String... cmds) {
//     FLAG:0x1
       try {
           Process process = Runtime.getRuntime().exec("su");
           DataOutputStream os = new DataOutputStream(process.getOutputStream());
           for(int i=0;i<cmds.length;i++)
               os.writeBytes(cmds[i]+"\n");
           os.writeBytes("exit\n");
           os.flush();
           process.waitFor();
       } catch (Exception e) {
           final Activity activity= new MainActivity();
           activity.runOnUiThread(new Runnable() {
               public void run() {
                   Toast.makeText(activity, "Error Occured...!", Toast.LENGTH_SHORT).show();
               }
           });
           Log.e("harsh_debug","Error executing SU command, flag:0x1");
       }
       return null;
   }
}


