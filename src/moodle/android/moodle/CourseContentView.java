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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import moodle.android.moodle.helpers.CourseContentsListHelper;
import moodle.android.moodle.helpers.FileManager;
import moodle.android.moodle.helpers.SectionListAdapter;
import moodle.android.moodle.helpers.SectionListItem;
import moodle.android.moodle.helpers.SectionListView;
import moodle.android.moodle.helpers.StandardArrayAdapter;
import moodle.android.moodle.model.Course;
import moodle.android.moodle.model.CourseContent;
import moodle.android.moodle.model.User;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CourseContentView extends Activity implements OnClickListener {
		
		Button home, courseSelect, upload, setting;
		TextView footerCourseHdr;
		LinearLayout emptyLayout;
		FrameLayout courseworkLayout;
		User user;
		
		SectionListItem[] documentArray;
		StandardArrayAdapter arrayAdapter;
		SectionListAdapter sectionAdapter;
		SectionListView listView;
		
	    /** Called when the activity is first created. */
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	    
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.course_material);  
	        
	        try
	        {
	        	Intent i = getIntent();	            
	        	user = (User) i.getParcelableExtra("userObject");
	        	
	        	footerCourseHdr = (TextView) findViewById(R.id.course_ftr_view);
	        	
	        	home = (Button) findViewById(R.id.coursework_home_view);
		        courseSelect = (Button) findViewById(R.id.select_course);
				setting = (Button) findViewById(R.id.settings_view);
		        upload = (Button) findViewById(R.id.upload_view);	        	
	        	
	        	if (user != null && user.getCourses().size() == 1) {
	        		user.setSelectedCourseId(user.getCourses().get(0).getId());
	        		courseSelect.setEnabled(false);
	        	}        		
	        	else
	        		courseSelect.setEnabled(true);
	        	
	        	if (user != null && user.getSelectedCourseId() == 99999) {
	        		i = new Intent(this, CourseSelect.class);
	        		i.putExtra("userObject", user);
	        		startActivityForResult(i, COURSE_SELECT_REQUEST_CODE);
	        	}
	        	
	        	if (user != null && user.getSelectedCourseId() != 99999)
	        		footerCourseHdr.setText(user.getCourse(user.getSelectedCourseId()).getShortName());
	        	
	        	getCourseDetails();
		        
		        home.setOnClickListener(this);
				if (courseSelect.isEnabled())
					courseSelect.setOnClickListener(this);
		        setting.setOnClickListener(this);
		        upload.setOnClickListener(this);
	        }
	        catch(Exception e)
	        {
	        	Log.e("Error 1", e.toString()+"Error with CourseContentView Class");
	        }
	        
	    }
	    
	    public static final int COURSE_SELECT_REQUEST_CODE = 1;

		@Override
		public void onClick(View v) {
			Intent nextPage;

			switch(v.getId())
			{	
			case R.id.coursework_home_view:
				nextPage = new Intent(this, CourseDetail.class);
				nextPage.putExtra("userObject", user);		
				startActivity(nextPage);
				break;
			case R.id.select_course:
				nextPage = new Intent(this, CourseSelect.class);
				nextPage.putExtra("userObject", user);
				startActivityForResult(nextPage, COURSE_SELECT_REQUEST_CODE);
				break;
			case R.id.settings_view:
				nextPage = new Intent(this, Setting.class);
				//nextPage.putExtra("userObject", user);
				//startActivityForResult(nextPage, COURSE_SELECT_REQUEST_CODE);
				startActivity(nextPage);
				break;
			case R.id.upload_view:
				nextPage = new Intent(this, FileUpload.class);
				nextPage.putExtra("userObject", user);
				startActivity(nextPage);
				break;
				default:
					
			}
		}

		protected void onActivityResult(int requestCode, int resultCode, Intent data) 
		{
			if (requestCode == COURSE_SELECT_REQUEST_CODE) {
				if (resultCode == RESULT_OK) {
					user = (User) data.getParcelableExtra("userObject");
					if (user != null && user.getSelectedCourseId() != 99999) {
						Course course = user.getCourse(user.getSelectedCourseId());
						footerCourseHdr.setText(course.getShortName());
						
						getCourseDetails();
					}
				}
			}
		}
		
		private void getCourseDetails() {
			emptyLayout = (LinearLayout)findViewById(R.id.coursework_item_empty);
			emptyLayout.setVisibility(View.INVISIBLE);
			courseworkLayout = (FrameLayout)findViewById(R.id.listView);
			courseworkLayout.setVisibility(View.VISIBLE);
        	
			ArrayList<CourseContent> coursecontent = new ArrayList<CourseContent>();
        	if (user != null && user.getCourse(user.getSelectedCourseId()) != null) {
        		coursecontent = user.getCourse(user.getSelectedCourseId()).getCourseContent();
        	}        	
        	
        	documentArray = CourseContentsListHelper.getInstance(this).populateCourseDocuments(coursecontent, user.getCourse(user.getSelectedCourseId()).getFullname());
    		
    		if (documentArray != null && documentArray.length > 0) {
	    		arrayAdapter = new StandardArrayAdapter(this, documentArray);
	    		sectionAdapter = new SectionListAdapter(getLayoutInflater(), arrayAdapter);
	    		listView = (SectionListView)findViewById(getResources().getIdentifier("document_section_list_view", "id", this.getClass().getPackage().getName()));
	    		listView.setAdapter(sectionAdapter);
	    		
	    		listView.setOnItemClickListener(new OnItemClickListener() {

	    			@SuppressLint("ShowToast")
					@SuppressWarnings("static-access")
					@Override
	    			public void onItemClick(AdapterView<?> parent, View view,
	    					int position, long id) {
	    							
	    				Object obj = sectionAdapter.getItem(position);
	    				if (obj instanceof SectionListItem) {				
	    				
	    					SectionListItem	selectedMap = (SectionListItem)obj;
	    					@SuppressWarnings("unchecked")
	    					HashMap<String, String> selectedItem = (HashMap<String, String>)selectedMap.item;
	    					String fileURL = selectedItem.get("url");
	    					String fileName = selectedItem.get("filename");
	    					String courseDirectoryAndType = user.getCourse(user.getSelectedCourseId()).getFullname() + "/Documents/";	    					
	    					
    						File file = FileManager.getInstance(CourseContentView.this).DownloadFromUrl(fileURL + "&token=" + user.getToken(), fileName, courseDirectoryAndType);
    						
    						if (file != null) {
		    	                MimeTypeMap myMime = MimeTypeMap.getSingleton(); 		    	                
		    	                Intent newIntent = new Intent(android.content.Intent.ACTION_VIEW); 
		    	         
		    	                //Intent newIntent = new Intent(Intent.ACTION_VIEW); 
		    	                String mimeType = myMime.getMimeTypeFromExtension(fileExt(file.toString()).substring(1)); 
		    	                newIntent.setDataAndType(Uri.fromFile(file),mimeType); 
		    	                newIntent.setFlags(newIntent.FLAG_ACTIVITY_NEW_TASK); 
		    	                try { 
		    	                    CourseContentView.this.startActivity(newIntent); 
		    	                } catch (android.content.ActivityNotFoundException e) { 
		    						Log.e("MIME Error", e.toString()+" default program for this filetype not found");
		    						// Raise on activity not found  
		    	                    Toast.makeText(CourseContentView.this, "A suitable Application to access the file " + mimeType +" not found.", 4000).show(); 
		    	                } 
    						}
    						else {
    							Toast.makeText(CourseContentView.this, "There is no SD Card installed to save the file to. Please insert to view the file.", 4000).show();
    						}
	    				}
	    			}
	    		});	
    		}
    		else {    			
    			emptyLayout.setVisibility(View.VISIBLE);
    			courseworkLayout.setVisibility(View.INVISIBLE);
    		}
		}
		
		public String fileExt(String url) { 
		    if (url.indexOf("?")>-1) { 
		        url = url.substring(0,url.indexOf("?")); 
		    } 
		    if (url.lastIndexOf(".") == -1) { 
		        return null; 
		    } else { 
		        String ext = url.substring(url.lastIndexOf(".") ); 
		        if (ext.indexOf("%")>-1) { 
		            ext = ext.substring(0,ext.indexOf("%")); 
		        } 
		        if (ext.indexOf("/")>-1) { 
		            ext = ext.substring(0,ext.indexOf("/")); 
		        } 
		        return ext.toLowerCase(); 
		 
		    } 
		} 

			

	}