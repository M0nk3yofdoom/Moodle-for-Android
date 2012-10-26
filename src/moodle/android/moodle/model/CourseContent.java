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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class CourseContent implements Parcelable {
	//private static CourseContent instance;

	public CourseContent() {

	}

//	public static CourseContent getInstance() {
//		if (instance == null) {
//			synchronized(CourseContent.class) {
//				if (instance == null) {
//					instance = new CourseContent();
//				}
//			}
//		}
//		return instance;
//	}
	
	private int id;	
	public void setId(int id) {
       this.id = id;
    }

    public int getId() {
       return id;
    }
        
    private String name;	
	public void setName(String name) {
       this.name = name;
    }

    public String getName() {
       return name;
    }
	    
    private int visible;	
	public void setVisible(int visible) {
       this.visible = visible;
    }

    public int getVisible() {
       return visible;
    }
        
	private String summary;	
	public void setSummary(String summary) {
	   this.summary = summary;
	}
	
	public String getSummary() {
	   return summary;
	}
    
    private ArrayList<Module> modules = new ArrayList<Module>();	
	public void setModules(ArrayList<Module> modules) {
       this.modules = modules;
    }

    public ArrayList<Module> getModules() {
       return modules;
    }

    public void populateCourseContent(JSONObject jsonObject) {
    
    	    	 
    	try {  
    		if (jsonObject != null) {
    			
	    		String id = jsonObject.getString("id"); 
	    		this.setId(Integer.valueOf(id));
		        String name = jsonObject.optString("name");  
		        if (name != null && name.trim().length() > 0)
		        	this.setName(name);
		        String visible = jsonObject.optString("visible");  
		        if (visible != null && visible.trim().length() > 0)
		        	this.setVisible(Integer.valueOf(visible));
		        String summary = jsonObject.optString("summary");  
		        if (summary != null && summary.trim().length() > 0)
		        	this.setSummary(summary);
		        
		        JSONArray modules = jsonObject.getJSONArray("modules");
		        ArrayList<Module> modulesArray = new ArrayList<Module>();
	    	    // looping through all Modules 
	    	    for(int i = 0; i < modules.length(); i++){ 
	    	        JSONObject c = modules.getJSONObject(i); 
	    	        Module module = new Module();
	    	        module.populateModule(c);
	    	        //Toast.makeText(context.getApplicationContext(), course.getShortName(), Toast.LENGTH_LONG).show();
	    	        // Storing each json item in variable 
	    	        modulesArray.add(module);
	    	    } 	
	    	    
	    	    if (modulesArray.size() > 0) {
	    	    	this.setModules(modulesArray);
	    	    }
    		}
    	} catch (JSONException e) { 
    	    e.printStackTrace(); 
    	}
    }
    
    /* everything below here is for implementing Parcelable */ 
	 
    // 99.9% of the time you can just ignore this 
    public int describeContents() { 
        return 0; 
    } 
    
 // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods 
    public static final Parcelable.Creator<CourseContent> CREATOR = new Parcelable.Creator<CourseContent>() { 
        public CourseContent createFromParcel(Parcel in) { 
            return new CourseContent(in); 
        } 
 
        public CourseContent[] newArray(int size) { 
            return new CourseContent[size]; 
        } 
    }; 
 
    // write your object's data to the passed-in Parcel 
    public void writeToParcel(Parcel dest, int flags) { 
    	dest.writeInt(id);
    	dest.writeString(name);
    	dest.writeInt(visible);
    	dest.writeString(summary);
    	dest.writeTypedList(modules); 
    }
    
    private CourseContent(Parcel in) { 
        this.id = in.readInt();
        this.name = in.readString();
        this.visible = in.readInt();
        this.summary = in.readString();
        in.readTypedList(this.modules, Module.CREATOR); 
    }
}
