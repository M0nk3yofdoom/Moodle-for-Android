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
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import moodle.android.moodle.helpers.FileManager;
import moodle.android.moodle.helpers.FileUploadsListHelper;
import moodle.android.moodle.helpers.LazyAdapter;
import moodle.android.moodle.model.Course;
import moodle.android.moodle.model.User;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment; 
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class FileUpload extends Activity implements OnClickListener {		

		public static final List<String> supportedFileExtensions = getSupportedExtensions(); 
		
		Button home, courseSelect, upload, setting;
		TextView footerCourseHdr, emptyText;
		EditText inputSearch;
		LinearLayout emptyLayout;
		FrameLayout courseworkLayout;
		User user;
		
		ArrayList<HashMap<String, String>> fileArray = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> selectedMap;
	    LazyAdapter adapter;
	    ListView list;
		
		List<String> fileStringList, pathStringList;
		static List<File> availableFileList;
		String root;

	    /** Called when the activity is first created. */
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	    
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.upload_layout);  
	        	        
	        try
	        {
	        	final ProgressDialog dialog = ProgressDialog.show(FileUpload.this,
	        			"",
	        			"Fetching Files, this may take a few minutes",
	        			true, false);
	        	
	        	
	        	//new Thread(new Runnable(){
	        	//	public void run(){
	        			
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
	        		i = new Intent(FileUpload.this, CourseSelect.class);
	        		i.putExtra("userObject", user);
	        		startActivityForResult(i, COURSE_SELECT_REQUEST_CODE);
	        	}
	        	
	        	if (user != null && user.getSelectedCourseId() != 99999)
	        		footerCourseHdr.setText(user.getCourse(user.getSelectedCourseId()).getShortName());
	        	
	        	getFileUpload();
	        	
		        home.setOnClickListener(FileUpload.this);
				if (courseSelect.isEnabled())
					courseSelect.setOnClickListener(FileUpload.this);
		        setting.setOnClickListener(FileUpload.this);
		        upload.setOnClickListener(FileUpload.this);
		        
		        dialog.dismiss();
	        	
	        	//	}
	        	//}).start();
		        
	        }
	        catch(Exception e)
	        {
	        	Log.e("Error 1", e.toString());
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
				//loading the initial view after changing course from within FileUpload
				nextPage = new Intent(this, CourseDetail.class);
				nextPage.putExtra("userObject", user);
				startActivity(nextPage);
				break;
			case R.id.settings_view:
				nextPage = new Intent(this, Setting.class);
				nextPage.putExtra("userObject", user);
				startActivityForResult(nextPage, COURSE_SELECT_REQUEST_CODE);
				//startActivity(nextPage);
				break;
			case R.id.upload_view:
				/*nextPage = new Intent(this, FileUpload.class);
				nextPage.putExtra("userObject", user);
				startActivity(nextPage);*/
				break;
				default:
					
			}
		}
		
		private void getFileUpload() {

			emptyLayout = (LinearLayout)findViewById(R.id.upload_item_empty);
			emptyLayout.setVisibility(View.INVISIBLE);
			emptyText = (TextView)findViewById(R.id.upload_empty_text);
			emptyText.setText(R.string.empty_upload);
			list = (ListView)findViewById(R.id.upload_file_list);
			list.setVisibility(View.VISIBLE);
			
			inputSearch = (EditText) findViewById(R.id.uploadInputSearch); 
			
	    	//attempt at opening a file for upload
	        try{

	        	
	        			
	        	boolean mExternalStorageAvailable = false;
	        	boolean mExternalStorageWriteable = false;
	        	String state = Environment.getExternalStorageState();

	        	if (Environment.MEDIA_MOUNTED.equals(state)) {
	        	    // We can read and write the media
	        	    mExternalStorageAvailable = mExternalStorageWriteable = true;
	        	} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        	    // We can only read the media
	        	    mExternalStorageAvailable = true;
	        	    mExternalStorageWriteable = false;
	        	} else {
	        	    // Something else is wrong. It may be one of many other states, but all we need
	        	    //  to know is we can neither read nor write
	        	    mExternalStorageAvailable = mExternalStorageWriteable = false;
	        	}
	        	
	        	
	        	if (mExternalStorageAvailable || mExternalStorageWriteable) {	        		
	        		visitAllDirs(Environment.getExternalStorageDirectory());
	        		
	        		//Sort the files alphabetically.
	        		Collections.sort(availableFileList, new Comparator<File>() { 
	        	        @Override 
	        	        public int compare(File s1, File s2) { 
	        	            return s1.getName().compareToIgnoreCase(s2.getName()); 
	        	        } 
	        	    });
	        		
	        	}
	    		else {    			
	    			
	    			
	    			emptyLayout.setVisibility(View.VISIBLE);
	    			emptyText.setText("No Media Mounted (SD Card Missing)");
	    			list.setVisibility(View.INVISIBLE);
	    		}
	        	
	        	
	        	
	        	
	        }
	        catch(Exception e)
	        {
	        	Log.e("Error 1", e.toString());
	        }
	    	
	    	
	    	if (availableFileList != null && availableFileList.size() > 0) {
	    		// Populate the files for the list
	    		fileArray = FileUploadsListHelper.getInstance(this).populateUploadFiles(availableFileList);
	    		
	    		//list=(ListView)findViewById(R.id.upload_file_list);
	    		
	    		adapter=new LazyAdapter(this, fileArray);        
		        list.setAdapter(adapter);
		        
		        /** 
		         * Enabling Search Filter 
		         * */
		        inputSearch.addTextChangedListener(new TextWatcher() { 
		  
		            @Override
		            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) { 
		                // When user changed the Text 
		            	FileUpload.this.adapter.getFilter().filter(cs); 
		            } 
		  
		            @Override
		            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, 
		                    int arg3) { 
		                // TODO Auto-generated method stub 
		  
		            } 
		  
		            @Override
		            public void afterTextChanged(Editable arg0) { 
		                // TODO Auto-generated method stub 
		            } 
		        }); 

		        
		        // Click event for single list row
		        list.setOnItemClickListener(new OnItemClickListener() {

					@SuppressWarnings("unchecked")
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						
						selectedMap = (HashMap<String, String>)adapter.getItem(position);
						String value = selectedMap.get(LazyAdapter.KEY_HEADER);
						
						new AlertDialog.Builder(FileUpload.this) 
						  .setIcon(R.drawable.upload_icon_default)
						  .setTitle("Upload File")
						  .setMessage("Upload the file: " + System.getProperty("line.separator") + value)
						  .setPositiveButton("Yes", new DialogInterface.OnClickListener() {								 
							    public void onClick(DialogInterface dialog, int which) { 							    	
							      //String value = selectedMap.get(LazyAdapter.KEY_HEADER);	      
							      String selectedFile = selectedMap.get(LazyAdapter.KEY_OTHER);
							      
							      FileManager.getInstance(FileUpload.this).UploadToUrl(user.getSiteInfo().getSiteUrl(), user.getToken(), selectedFile);
							      
							      dialog.dismiss(); 
							    } 
							  })
						  .setNegativeButton("No", new DialogInterface.OnClickListener() {
							    public void onClick(DialogInterface dialog, int which) {
							      dialog.dismiss();
							    } 
							  }).create().show();	
					}
				});	
	    	}
    		else {    			
    			emptyLayout.setVisibility(View.VISIBLE);
    			list.setVisibility(View.INVISIBLE);
    		}
	        
	    	
		}
		
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() { 
		    @Override 
		    public void onClick(DialogInterface dialog, int which) { 
		        switch (which){ 
		        case DialogInterface.BUTTON_POSITIVE: 
		            //Yes button clicked 
		            break; 
		 
		        case DialogInterface.BUTTON_NEGATIVE: 
		            //No button clicked 
		            break; 
		        } 
		    } 
		}; 

		
		// Process only directories under dir
		private static void visitAllDirs(File dir) {
		    if (dir.isDirectory()) {
		    	getFiles(dir);

		        String[] children = dir.list();
		        if (children != null) {
			        for (int i=0; i<children.length; i++) {
			            visitAllDirs(new File(dir, children[i]));
			        }
		        }
		    }
		}
		
		private static void getFiles(File dir) {
			if (availableFileList == null)
				availableFileList = new ArrayList<File>();
			
			File[] files = dir.listFiles(new FileFilter() {

				 public boolean accept(File pathname) {
					  //If a file or directory is hidden, or unreadable, don't show it in the list.
					  if(pathname.isHidden())
					   return false;
					  if(!pathname.canRead())
					   return false;
	
					  //Filter all directories in the list.
					  if(pathname.isDirectory())
					   return false;
	
					  //Check if there is a supported file type that we can read.
					  String fileName = pathname.getName();
					  String fileExtension;
					  int mid= fileName.lastIndexOf(".");
					  fileExtension = fileName.substring(mid+1,fileName.length());
					  for(String s : supportedFileExtensions) {
						   if(s.contentEquals(fileExtension)) 
							   return true;
					  }
					  
					  return false; 
					 }
				   
				});
			 
			if (files != null) {
				for(int h=0; h < files.length; h++) {
					File file = files[h];
					availableFileList.add(file);					
				}
			}

			//ArrayAdapter<String> fileList = new ArrayAdapter<String>(this, R.layout.row, item);
			//setListAdapter(fileList); 
	    }

	 

		protected void onActivityResult(int requestCode, int resultCode, Intent data) 
		{
			if (requestCode == COURSE_SELECT_REQUEST_CODE) {
				if (resultCode == RESULT_OK) {
					user = (User) data.getParcelableExtra("userObject");
					if (user != null && user.getSelectedCourseId() != 99999) {
						Course course = user.getCourse(user.getSelectedCourseId());
						footerCourseHdr.setText(course.getShortName());
						
						getFileUpload();
					}
				}
			}
		}
		
		public static List<String> getSupportedExtensions() {
			List<String> extensionList = new ArrayList<String>();
			
			extensionList.add("dot");
			extensionList.add("doc");
			extensionList.add("docx");
			extensionList.add("pdf");
			extensionList.add("xls");
			extensionList.add("xlsx");
			extensionList.add("csv");
			extensionList.add("avi");
			extensionList.add("bmp");
			extensionList.add("fla");
			extensionList.add("swl");
			extensionList.add("gif");
			extensionList.add("jpeg");
			extensionList.add("jpg");
			extensionList.add("mov");
			extensionList.add("mpeg");
			extensionList.add("mp3");
			extensionList.add("mp4");
			extensionList.add("png");
			extensionList.add("ppt");
			extensionList.add("pptx");
			extensionList.add("pps");
			extensionList.add("rar");
			extensionList.add("text");
			extensionList.add("txt");
			extensionList.add("url");
			extensionList.add("wav");
			extensionList.add("wmv");
			extensionList.add("zip");
			
			return extensionList;
		}
		
		
	}