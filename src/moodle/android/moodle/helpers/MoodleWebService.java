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

package moodle.android.moodle.helpers;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import moodle.android.moodle.R;
import moodle.android.moodle.model.Course;
import moodle.android.moodle.model.CourseContent;
import moodle.android.moodle.model.SiteInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.util.Log;

public class MoodleWebService {
	
	private Context context;
	
	public MoodleWebService (Context context) {
		this.context = context;
	}
	
	private JSONObject getWebServiceResponse(String serverurl, String functionName, String urlParameters, int xslRawId) {
		JSONObject jsonobj = null;
		
		try {
			HttpURLConnection con = (HttpURLConnection) new URL(serverurl + functionName).openConnection();		
			//HttpURLConnection con = (HttpURLConnection) new URL(serverurl + functionName + "&moodlewsrestformat=json").openConnection();
		
			con.setConnectTimeout(30000); 
			con.setReadTimeout(30000); 

			con.setRequestMethod("POST");
	        con.setRequestProperty("Content-Type", 
	           "application/x-www-form-urlencoded");
	        con.setRequestProperty("Content-Language", "en-US");
	        con.setDoOutput(true);
	        con.setUseCaches (false);
	        con.setDoInput(true);
	        DataOutputStream wr = new DataOutputStream (con.getOutputStream ());
	        
	        Log.d("URLParameters: ", urlParameters.toString());
			wr.writeBytes (urlParameters);
	        wr.flush ();
	        wr.close ();
	        
	        //Get Response
	        InputStream is =con.getInputStream();  
	        
            Source xmlSource = new StreamSource(is); 
            Source xsltSource = new StreamSource(context.getResources().openRawResource(xslRawId)); 
 
            TransformerFactory transFact = TransformerFactory.newInstance(); 
            Transformer trans = transFact.newTransformer(xsltSource); 
            StringWriter writer = new StringWriter(); 	            
            trans.transform(xmlSource, new StreamResult(writer));             
            	
        	String jsonstr = writer.toString();
        	jsonstr = jsonstr.replace("<div class=\"no-overflow\"><p>", "");
        	jsonstr = jsonstr.replace("</p></div>", "");
        	jsonstr = jsonstr.replace("<p>", "");
        	jsonstr = jsonstr.replace("</p>", "");
        	Log.d("TransformObject: ", jsonstr);
        	jsonobj = new JSONObject(jsonstr);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();				
        } catch (TransformerConfigurationException e) { 
            // TODO Auto-generated catch block 
            e.printStackTrace(); 
        } catch (TransformerFactoryConfigurationError e) { 
            // TODO Auto-generated catch block 
            e.printStackTrace(); 
        } catch (TransformerException e) { 
            // TODO Auto-generated catch block 
            e.printStackTrace(); 
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonobj;
	}

	public void getSiteinfo(String serverurl, SiteInfo siteInfo) {
		String urlParameters = ""; // moodle_webservice_get_siteinfo parameters	//core_webservice_get_site_info		
		JSONObject jsonobj = getWebServiceResponse(serverurl, "moodle_webservice_get_siteinfo", urlParameters, R.raw.siteinfoxsl);
		siteInfo.populateSiteInfo(jsonobj);
	}
	
	
	public void getUserCourses(String serverurl, int userId, ArrayList<Course> coursesArray) {
		
		String user = String.valueOf(userId);
		String urlParameters = "";
		
		try {
			urlParameters = "userid=" + URLEncoder.encode(user, "UTF-8");
						
			JSONObject jsonobj = getWebServiceResponse(serverurl, "moodle_enrol_get_users_courses", urlParameters, R.raw.coursesxsl);

			JSONArray courses = jsonobj.getJSONArray("courses");
    	    // looping through All Contacts 
    	    for(int i = 0; i < courses.length(); i++){ 
    	        JSONObject c = courses.getJSONObject(i); 
    	        Course course = new Course();
    	        course.populateCourse(c);
    	        //Toast.makeText(context.getApplicationContext(), course.getShortName(), Toast.LENGTH_LONG).show();
    	        // Storing each json item in variable 
    	        coursesArray.add(course);
    	    } 			

		} catch (JSONException e) {
			// TODO Auto-generated catch block
    	    e.printStackTrace(); 
		} catch (UnsupportedEncodingException e1) { // moodle_enrol_get_users_courses parameters
			// TODO Auto-generated catch block
			e1.printStackTrace();		 
		} 
	}
	
	public void getCourseContents(String serverurl, int courseid, ArrayList<CourseContent> courseContentsArray) {
		
		String course = String.valueOf(courseid);
		String urlParameters = "";
		
		try {
			urlParameters = "courseid=" + URLEncoder.encode(course, "UTF-8");
						
			//core_course_get_contents
			JSONObject jsonobj = getWebServiceResponse(serverurl, "core_course_get_contents", urlParameters, R.raw.contentxsl);

			JSONArray coursecontents = jsonobj.getJSONArray("coursecontents");
    	    // looping through All Course Content 
    	    for(int i = 0; i < coursecontents.length(); i++){ 
    	        JSONObject c = coursecontents.getJSONObject(i); 
    	        CourseContent coursecontent = new CourseContent();
    	        coursecontent.populateCourseContent(c);
    	        //Toast.makeText(context.getApplicationContext(), coursecontent.getName(), Toast.LENGTH_LONG).show();
    	        // Storing each json item in variable 
    	        courseContentsArray.add(coursecontent);
    	    } 			

		} catch (JSONException e) {
			// TODO Auto-generated catch block
    	    e.printStackTrace(); 
		} catch (UnsupportedEncodingException e1) { // moodle_enrol_get_users_courses parameters
			// TODO Auto-generated catch block
			e1.printStackTrace();		 
		} 
	}
	
	//public void sendUploadFiles(String serverurl, File uploadFile) {
	//	String urlParameters = ""; // moodle_webservice_get_siteinfo parameters	//core_webservice_get_site_info		
	//	JSONObject jsonobj = getWebServiceResponse(serverurl, "moodle_files_upload", urlParameters, R.raw.uploadinfoxml);
		//siteInfo.populateSiteInfo(jsonobj);
	//}
}
