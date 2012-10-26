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

package moodle.android.moodle.model;

import java.util.ArrayList;
import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;




public class User implements Parcelable {
	
	public User() {

	}

	private String username;	
	public void setUsername(String username) {
       this.username = username;
    }

    public String getUsername() {
       return username;
    }
    
    private String password;	
	public void setPassword(String password) {
       this.password = password;
    }

    public String getPassword() {
       return password;
    }
    
    private String url;	
	public void setUrl(String url) {
       this.url = url;
    }

    public String getUrl() {
       return url;
    }
    
    private String token;	
	public void setToken(String token) {
       this.token = token;
    }

    public String getToken() {
       return token;
    }
    
    private Date tokencreatedate;	
	public void setTokenCreateDate() {
       this.tokencreatedate = new Date(); 
    }

    public Date getTokenCreateDate() {
       return tokencreatedate;
    }
    
    private int selectedcourseid = 99999;	
   	public void setSelectedCourseId(int selectedcourseid) {
   	  this.selectedcourseid = selectedcourseid;
   	}

   	public int getSelectedCourseId() {
      return selectedcourseid;
   	}
    
    private SiteInfo siteinfo;	
    public void setSiteInfo(SiteInfo siteinfo) {
	   this.siteinfo = siteinfo; 
	}
    
    public SiteInfo getSiteInfo() {
       return siteinfo;
    }
    
    private ArrayList<Course> courses = new ArrayList<Course>();	
    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses; 
     }
    
    public ArrayList<Course> getCourses() {
       return courses;
    }
    
    public Course getCourse(int id) {
    	for (Course course : courses) { 
		  if (course.getId() == id) { 
		    return course;  
		  } 
		} 
		return null; // course not found. 
    }
    
    /* everything below here is for implementing Parcelable */ 
	 
    // 99.9% of the time you can just ignore this 
    public int describeContents() { 
        return 0; 
    } 
    
 // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods 
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() { 
        public User createFromParcel(Parcel in) { 
            return new User(in); 
        } 
 
        public User[] newArray(int size) { 
            return new User[size]; 
        } 
    }; 
 
    // write your object's data to the passed-in Parcel 
    public void writeToParcel(Parcel dest, int flags) { 
    	dest.writeString(username); 
    	dest.writeString(password);
    	dest.writeString(url);
    	dest.writeString(token);
    	dest.writeLong(tokencreatedate.getTime());
    	dest.writeInt(selectedcourseid);
    	dest.writeParcelable(siteinfo, flags);
    	dest.writeTypedList(courses); 
    }
    
    private User(Parcel in) { 
        this.username = in.readString(); 
        this.password = in.readString();
        this.url = in.readString();
        this.token = in.readString();
        this.tokencreatedate = new Date(in.readLong());
        this.selectedcourseid = in.readInt();
        this.siteinfo = in.readParcelable(SiteInfo.class.getClassLoader());
        in.readTypedList(this.courses, Course.CREATOR); 
    }
}
