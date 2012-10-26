/**
*  an Android implementation of REST and XML-RPC access to Moodle 2.2 servers or higher
*  Copyright (C) 2012  Justin Stevanz, Andrew Kelson and Matthias Peitsch
*
*	Contact the.omega.online@gmail.com for further information.
*
*   This program is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with this program.  If not, see <http://www.gnu.org/licenses/>
*/

package moodle.android.moodle;

//import moodleProject.AndroidMoodleApp.R;
//import moodleProject.AndroidMoodleApp.R.drawable;
//import moodleProject.AndroidMoodleApp.R.layout;
import moodle.android.moodle.model.User;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

public class Setting extends Activity {
	
	Intent intent;
	
	User user;

	/** Called when the activity is first created. */
	 @ Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		try {
			intent = getIntent();	

			user = (User) intent.getParcelableExtra("userObject");
        	
			
			
			//stretch custom dialog horizontally in android
			LayoutInflater factory = LayoutInflater.from(this);
			final View EntryView = factory.inflate(R.layout.settings, null);

			
			AlertDialog.Builder adb = new AlertDialog.Builder(this); 
			adb.setTitle("Settings");
			adb.setIcon(R.drawable.setting_icon);
			adb.setInverseBackgroundForced(true);	
			adb.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					logout();
					finish();
					return;
				}});
			adb.setNeutralButton("Save", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					//Downloading of files process started here....
					
					finish();
					return;
				}});	
			adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					finish();
					return;
				}});	
			
			
			
		    //Dialog d = adb.setView(new View(this)).create();
			Dialog d = adb.setView(EntryView).create(); 
			
		    WindowManager.LayoutParams lp = new WindowManager.LayoutParams(); 
		    lp.copyFrom(d.getWindow().getAttributes()); 
		    lp.width = WindowManager.LayoutParams.FILL_PARENT; 
		    lp.height = WindowManager.LayoutParams.FILL_PARENT; 

		    d.show(); 
		    d.getWindow().setAttributes(lp); 
		    d.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			

			
			
		    
		} catch (Exception e) {
			Log.e("Error 3", e.toString()+" Error within Settings page");
		}
	}
	 public void logout(){
		Intent nextPage;
 		nextPage = new Intent(Setting.this, Login.class);
 		nextPage.putExtra("userObject", user);
     	startActivity(nextPage);
	 }

		
}
