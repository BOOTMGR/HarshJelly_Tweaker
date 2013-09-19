package com.harsh.romtool;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.DataOutputStream;

class Utils {

}

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


