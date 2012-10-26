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
/**
 * This database connection modified from the free Lars Vogel tutorial
 * 
 * http://www.vogella.com/articles/AndroidSQLite/article.html
 */

package moodle.android.moodle;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import moodle.android.moodle.helpers.CommentsDataSource;
import moodle.android.moodle.helpers.DBComments;
import moodle.android.moodle.helpers.FileManager;
import moodle.android.moodle.helpers.SectionListItem;
import moodle.android.moodle.model.User;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Database extends ListActivity implements OnClickListener {
  private CommentsDataSource datasource;
  Intent intent;
  User user;
  Button home, courseSelect, upload, setting;
  ArrayAdapter<DBComments> adapter;
	

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.database);

    intent = getIntent();
	//Bundle extras = intent.getExtras();
	user = (User) intent.getParcelableExtra("userObject"); 
    
	home = (Button) findViewById(R.id.coursework_home_view);
    courseSelect = (Button) findViewById(R.id.select_course);
	setting = (Button) findViewById(R.id.settings_view);
    upload = (Button) findViewById(R.id.upload_view);
    
    datasource = new CommentsDataSource(this);
    datasource.open();

    List<DBComments> values = datasource.getAllComments();

    // Use the SimpleCursorAdapter to show the
    // elements in a ListView
    adapter = new ArrayAdapter<DBComments>(this,
        android.R.layout.simple_list_item_1, values);
    setListAdapter(adapter);
   
    
    //fills the list with the file names.
    try{
    populateList(adapter);
    }catch(Exception e){
    	Log.e("populate issue", e.toString());
    }
    
    home.setOnClickListener(this);
	if (courseSelect.isEnabled())
		courseSelect.setOnClickListener(this);
	setting.setOnClickListener(this);
	upload.setOnClickListener(this);
    
  }
  
  public void populateList(ArrayAdapter<DBComments> adapter2){
	  
	  DBComments comment = null;
	  
	  //removes the previous document list
	  if (getListAdapter().getCount() > 0) {
	        comment = (DBComments) getListAdapter().getItem(0);
	        datasource.deleteComment(comment);
	        adapter2.remove(comment);
	      }
	  
	  //loads up the list of documents
	  if (user != null && user.getSelectedCourseId() != 99999){	
			
			File sdCard = Environment.getExternalStorageDirectory();
	    	
			String temp = user.getCourse(user.getSelectedCourseId()).getFullname();
	    	
	    	File moodle = new File(sdCard, "Moodle/" + temp + "/Documents");
	    	
	    	int i =0;
	    	for (File f : moodle.listFiles()) {
	    		if (f.isFile()){
	    	        String name = f.getName();
	    	        if(i < adapter2.getCount()){
			    	if(f.getName().toString().compareTo(adapter2.getItem(i).toString())==0){
			    		}else{
			    	        comment = datasource.createComment(name);
						    adapter2.add(comment);
					    }
			    	}else{
		    	        comment = datasource.createComment(name);
						adapter2.add(comment);
						
				    }
	    	        
	    	        
			    	i++;
			    }
	    	}
	    	
	    	
	    	
		}else{
			Toast.makeText(getApplicationContext(), "There are no files downloaded", Toast.LENGTH_LONG).show();
  		
		}
	  
  }
  
  //manages the listeners on each item.
	@SuppressLint("ShowToast")
	@SuppressWarnings("static-access")
	@Override
	public void onListItemClick(ListView parent, View view, int position, long id) {
			
		@SuppressWarnings("unchecked")
		
		File sdCard = Environment.getExternalStorageDirectory();
    	String temp = user.getCourse(user.getSelectedCourseId()).getFullname();
    	String fileName = adapter.getItem(position).getComment().toString();
    	File file = new File(sdCard, "Moodle/" + temp + "/Documents/"+ fileName);
  	
    	
    	if (file != null) {
			  MimeTypeMap myMime = MimeTypeMap.getSingleton(); 		    	                
			  Intent newIntent = new Intent(android.content.Intent.ACTION_VIEW); 

			  //Intent newIntent = new Intent(Intent.ACTION_VIEW);
			  String extension = file.toString().substring((file.toString().length()-3));
			  
			  String mimeType = myMime.getMimeTypeFromExtension(extension); 
			  newIntent.setDataAndType(Uri.fromFile(file),mimeType); 
			  newIntent.setFlags(newIntent.FLAG_ACTIVITY_NEW_TASK); 
			  try { 
			      Database.this.startActivity(newIntent); 
			  } catch (android.content.ActivityNotFoundException e) { 
					Log.e("MIME Error", e.toString()+" default program for this filetype not found");
					// Raise on activity not found  
			      Toast.makeText(Database.this, "A suitable Application to access the file " + mimeType +" not found.", 4000).show(); 
			  } 
			}
		
  }
  
  
  
  public static final int COURSE_SELECT_REQUEST_CODE = 1;

  // Will be called via the onClick attribute
  // of the buttons in main.xml
  public void onClick(View view) {
    @SuppressWarnings("unchecked")
    ArrayAdapter<DBComments> adapter = (ArrayAdapter<DBComments>) getListAdapter();
    DBComments comment = null;
    Intent nextPage;

    switch (view.getId()) {
    case R.id.add:
    	
    	
    	if (user != null && user.getSelectedCourseId() != 99999){	
			
			File sdCard = Environment.getExternalStorageDirectory();
	    	
			String temp = user.getCourse(user.getSelectedCourseId()).getFullname();
	    	
	    	File moodle = new File(sdCard, "Moodle/" + temp + "/Documents");
	    	
	    	int i =0;
	    	for (File f : moodle.listFiles()) {
	    		if (f.isFile()){
	    	        String name = f.getName();
	    	        if(i < adapter.getCount()){
			    	if(f.getName().toString().compareTo(adapter.getItem(i).toString())==0){
			    		}else{
			    	        comment = datasource.createComment(name);
						    adapter.add(comment);
					    }
			    	}else{
		    	        comment = datasource.createComment(name);
						adapter.add(comment);
				    }
			    	i++;
			    }
	    	}
		}else{
			Toast.makeText(getApplicationContext(), "There are no files downloaded", Toast.LENGTH_LONG).show();
    		
		}
    	
        
      break;
	  case R.id.delete:
	      if (getListAdapter().getCount() > 0) {
	        comment = (DBComments) getListAdapter().getItem(0);
	        datasource.deleteComment(comment);
	        adapter.remove(comment);
	      }
	      break;
	      
	      
	      
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
			
    }
    
    
    
    adapter.notifyDataSetChanged();
  }

  @Override
  protected void onResume() {
    datasource.open();
    super.onResume();
  }

  @Override
  protected void onPause() {
    datasource.close();
    super.onPause();
  }
  
  /*
  //this is the attempt at implementing the onitemclicklistener...
  listView.setOnItemClickListener(new OnItemClickListener() {

		@SuppressLint("ShowToast")
		@SuppressWarnings("static-access")
		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
		
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
		}
		*/

} 
