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

import moodle.android.moodle.model.Course;
import moodle.android.moodle.model.User;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class CourseSelect extends Activity {
	
	String[] courseItems;
	Intent intent;
	User user;
	
	/** Called when the activity is first created. */
	 @ Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		try {
			intent = getIntent();
			//Bundle extras = intent.getExtras();
			user = (User) intent.getParcelableExtra("userObject"); 
			
			Course selectedCourse = new Course();
			if(user == null || (user != null && user.getSelectedCourseId() == 99999)) { 
				 
		    } else { 
		    	selectedCourse= user.getCourse(user.getSelectedCourseId()); 
		    }
			
			courseItems = new String[ user.getCourses().size() ];
			int a = 0;
			for (Course course : user.getCourses()) {
				courseItems[a] = course.getShortName();
				a++;
			} 		
			
			int checkedItem = 0;			
			for (int i = 0; i < courseItems.length; i++) { 
				if (courseItems[i].equals(selectedCourse.getShortName()))
					checkedItem = i; 
		    }			
			
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, courseItems);
			
			new AlertDialog.Builder(this) 
			  .setTitle("Select a Course")
			  .setSingleChoiceItems(adapter, checkedItem,  new DialogInterface.OnClickListener() {
			 
			    public void onClick(DialogInterface dialog, int which) { 
			 		      
			    	String item = courseItems[which].toString();
			    	
			    	for (Course course : user.getCourses()) {
						if (item.equalsIgnoreCase(course.getShortName()))
							user.setSelectedCourseId(course.getId());
					}
			    	
			    	intent.putExtra("userObject", user);
			    	setResult(RESULT_OK, intent);
			      dialog.dismiss(); 
			      finish();
			    } 
			  }).create().show();			
						
		} catch (Exception e) {
			Log.e("Error 3", e.toString());
			Toast.makeText(getApplicationContext(), "You are not enrolled in any courses", Toast.LENGTH_LONG).show();
		}
	}	
	
}
