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

import java.util.ArrayList;

import moodle.android.moodle.helpers.AppStatus;
import moodle.android.moodle.helpers.MoodleWebService;
import moodle.android.moodle.helpers.TokenHttpRequest;
import moodle.android.moodle.model.Course;
import moodle.android.moodle.model.CourseContent;
import moodle.android.moodle.model.SiteInfo;
import moodle.android.moodle.model.User;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Login extends Activity implements OnClickListener {
    Button login;
	EditText siteUrl, username, password;
	User user;
	SharedPreferences saved;
	String loginDetails;
	
	ProgressDialog dialog;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);   
        
        try {
        	siteUrl = (EditText) findViewById(R.id.moodle_url);
        	username = (EditText) findViewById(R.id.username);
        	password = (EditText) findViewById(R.id.password);
        	login = (Button) findViewById(R.id.login_button);

        	try {
//        		
        		siteUrl.setHint(R.string.login_url_hint);
        		username.setHint(R.string.login_username_hint);
        		password.setHint(R.string.login_password_hint);
        		
        		//siteUrl.setText(saved.getString("siteUrlVal", "http://moodletest.shaftedartist.com"));       		
        		//username.setText(saved.getString("usr", "guest@guest"));
        		//password.setText(saved.getString("pwd","Gu3$t%000"));
        		
        		siteUrl.setText("http://moodletest.shaftedartist.com");
        		username.setText("guest@guest");
        		password.setText("Gu3$t%000");
        		
        	}
        	catch(Exception e) {
            	Log.e("NoPreferences", e.toString());
            }       	
	        
	        login.setOnClickListener(this);
	        
        }
        catch(Exception e) {
        	Log.e("Error With Login", e.toString());
        }
        
        
    }

	@Override
	public void onClick(View v) {
		
		
		switch(v.getId())
		{
			case R.id.login_button:
				
				//this dialog box needs to be run separately....
				
				dialog = ProgressDialog.show(this, "", 
		                "Logging in, please wait", true);
				
				/*new Thread(new Runnable(){
        		public void run(){
			*/
				
				if (AppStatus.getInstance(Login.this).isOnline(Login.this)) {  
					String conType = AppStatus.getInstance(Login.this).getConnectionType(Login.this);
					conType = conType == null ? "Unknown" : conType;
					Toast.makeText(getApplicationContext(), "You are online (" + conType + ")!!!!", Toast.LENGTH_LONG).show(); 
	        	 	
					
	        	} else {     
	        		messageHandler.sendEmptyMessage(0);
	        		
	        		Toast.makeText(getApplicationContext(), "You are not online!!!!", Toast.LENGTH_LONG).show();
	        		// enter details for offline access to some files. Restore the database.
	        		
	        	} 
				String siteUrlVal = siteUrl.getText().toString();
				
				//checks for http:// entry
				/*
				if(!(siteUrlVal.substring(0, 7).toLowerCase().compareTo("http://")==0)) {
					siteUrlVal = "http://"+siteUrlVal;
					siteUrl.setText(siteUrlVal);
				}
				*/
				
				String usr = username.getText().toString();
				String pwd = password.getText().toString();
				
				
				String usrUri = Uri.encode(usr);
				String pwdUri = Uri.encode(pwd);
				
				
				saved = getSharedPreferences(loginDetails, MODE_PRIVATE);
				
				SharedPreferences.Editor e = saved.edit();
				e.putString("siteUrlVal", siteUrlVal);
				e.putString("usr", usr);
				e.putString("pwd", pwd);
				e.commit();
				
				String url = siteUrlVal + "/login/token.php?username=" + usrUri + "&password=" + pwdUri + "&service=moodle_mobile_app";
				
				TokenHttpRequest tokenRequest = new TokenHttpRequest();
				String token = tokenRequest.doHTTPRequest(url); 
				
		        //Toast.makeText(getApplicationContext(), token, Toast.LENGTH_LONG).show(); 
		        
				
		        if (token != null && token != "") {
		        	
		        	
		        	String serverurl = siteUrlVal + "/webservice/rest/server.php" + "?wstoken=" + token + "&wsfunction=";
			        
			        user = new User();
			        user.setUsername(usr);
			        user.setPassword(pwd);
			        user.setToken(token);
			        user.setTokenCreateDate();
			        user.setUrl(url);
			        
			        MoodleWebService webService = new MoodleWebService(Login.this);
			        SiteInfo siteInfo = new SiteInfo();
			        webService.getSiteinfo(serverurl, siteInfo);
			        user.setSiteInfo(siteInfo);
			        ArrayList<Course> courses = new ArrayList<Course>();
			        webService.getUserCourses(serverurl, siteInfo.getUserid(), courses);
			        
			        	
			        if (courses.size() > 0) {		        	
			        	for(int i = 0; i < courses.size(); i++) { 
			    	        Course c = courses.get(i); 
			    	        ArrayList<CourseContent> coursecontents = new ArrayList<CourseContent>();
			    	        webService.getCourseContents(serverurl, c.getId(), coursecontents);
			    	        
			    	        if (coursecontents.size() > 0) {
			    	        	c.setCourseContent(coursecontents);
			    	        }
			    	    } 
			        	user.setCourses(courses);
			        	
			        	Intent nextPage;
	        	
			        	nextPage = new Intent(Login.this, CourseDetail.class);
						nextPage.putExtra("userObject", user); 

						startActivity(nextPage);
			        }
			        else {
			        	messageHandler.sendEmptyMessage(0);
		        		
			        	Log.e("Course Error", "User is not enrolled in any courses");
			        	Toast.makeText(getApplicationContext(), "This User is not Enrolled in any Courses, please contact your Lecturer", Toast.LENGTH_LONG).show();
			        	
			        }
		        
				
		        } 
		        else {
		        	messageHandler.sendEmptyMessage(0);
	        		
		        	Toast.makeText(getApplicationContext(), "The username and password are incorrect. Please try again!", Toast.LENGTH_LONG).show();
		        	Toast.makeText(getApplicationContext(), "If details are correct contact Site Admin to enable REST protocol", Toast.LENGTH_LONG).show();
		        }
		        
	        		/*}
	        	}).start();
	            */
		        
			break;
			default:
					
				
		}
	}
	
	private Handler messageHandler = new Handler() {

	    public void handleMessage(Message msg) {
	        super.handleMessage(msg);
	        dialog.dismiss();

	    }
	};
	

}