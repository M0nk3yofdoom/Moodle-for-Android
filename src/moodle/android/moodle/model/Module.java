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

public class Module implements Parcelable {
	//private static Module instance;

	public Module() {

	}

//	public static Module getInstance() {
//		if (instance == null) {
//			synchronized(Module.class) {
//				if (instance == null) {
//					instance = new Module();
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

	private String url;	
	public void setUrl(String url) {
       this.url = url;
    }

    public String getUrl() {
       return url;
    }
        
    private String name;	
	public void setName(String name) {
       this.name = name;
    }

    public String getName() {
       return name;
    }
    
    private String description;	
	public void setDescription(String description) {
       this.description = description;
    }

    public String getDescription() {
       return description;
    }
	    
    private int visible;	
	public void setVisible(int visible) {
       this.visible = visible;
    }

    public int getVisible() {
       return visible;
    }
        
	private String modicon;	
	public void setModIcon(String modicon) {
	   this.modicon = modicon;
	}
	
	public String getModIcon() {
	   return modicon;
	}
    
    private String modname;	
	public void setModName(String modname) {
       this.modname = modname;
    }

    public String getModName() {
       return modname;
    }
    
    private String modplural;	
	public void setModPlural(String modplural) {
       this.modplural = modplural;
    }

    public String getModPlural() {
       return modplural;
    }
    
    private int availablefrom;	
	public void setAvailableFrom(int availablefrom) {
       this.availablefrom = availablefrom;
    }

    public int getAvailableFrom() {
       return availablefrom;
    }
    
    private int availableuntil;	
	public void setAvailableUntil(int availableuntil) {
       this.availableuntil = availableuntil;
    }

    public int getAvailableUntil() {
       return availableuntil;
    }
    
    private int indent;	
	public void setIndent(int indent) {
       this.indent = indent;
    }

    public int getIndent() {
       return indent;
    }
    
    private ArrayList<Content> contents = new ArrayList<Content>();	
	public void setContents(ArrayList<Content> contents) {
       this.contents = contents;
    }

    public ArrayList<Content> getContents() {
       return contents;
    }

    public void populateModule(JSONObject jsonObject) {
    
    	    	 
    	try {  
    		if (jsonObject != null) {
    			
	    		String id = jsonObject.getString("id"); 
	    		this.setId(Integer.valueOf(id));
		        String url = jsonObject.optString("url"); 
		        if (url != null && url.trim().length() > 0)
		        	this.setUrl(url);
		        String name = jsonObject.optString("name");  
		        if (name != null && name.trim().length() > 0)
		        	this.setName(name);
		        String description = jsonObject.optString("description"); 
		        if (description != null && description.trim().length() > 0) 
		        	this.setDescription(description);
		        String visible = jsonObject.optString("visible");  
		        if (visible != null && visible.trim().length() > 0)
		        	this.setVisible(Integer.valueOf(visible));
		        String modicon = jsonObject.optString("modicon");  
		        if (modicon != null && modicon.trim().length() > 0)
		        	this.setModIcon(modicon);
		        String modname = jsonObject.optString("modname"); 
		        if (modname != null && modname.trim().length() > 0)
		        	this.setModName(modname);
		        String modplural = jsonObject.optString("modplural");  
		        if (modplural != null && modplural.trim().length() > 0)
		        	this.setModPlural(modplural);	
		        String availablefrom = jsonObject.optString("availablefrom");  
		        if (availablefrom != null && availablefrom.trim().length() > 0)
		        	this.setAvailableFrom(Integer.valueOf(availablefrom));
		        String availableuntil = jsonObject.optString("availableuntil");  
		        if (availableuntil != null && availableuntil.trim().length() > 0)
		        	this.setAvailableUntil(Integer.valueOf(availableuntil));
		        String indent = jsonObject.optString("indent");  
		        if (indent != null && indent.trim().length() > 0)
		        	this.setIndent(Integer.valueOf(indent));
		        
		        JSONArray contents = jsonObject.getJSONArray("contents");
		        ArrayList<Content> contentsArray = new ArrayList<Content>();
	    	    // looping through all Contents 
	    	    for(int i = 0; i < contents.length(); i++){ 
	    	        JSONObject c = contents.getJSONObject(i); 
	    	        Content content = new Content();
	    	        content.populateContent(c);
	    	        //Toast.makeText(context.getApplicationContext(), course.getShortName(), Toast.LENGTH_LONG).show();
	    	        // Storing each json item in variable 
	    	        contentsArray.add(content);
	    	    } 	
	    	    
	    	    if (contentsArray.size() > 0) {
	    	    	this.setContents(contentsArray);
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
    public static final Parcelable.Creator<Module> CREATOR = new Parcelable.Creator<Module>() { 
        public Module createFromParcel(Parcel in) { 
            return new Module(in); 
        } 
 
        public Module[] newArray(int size) { 
            return new Module[size]; 
        } 
    }; 
 
    // write your object's data to the passed-in Parcel 
    public void writeToParcel(Parcel dest, int flags) { 
    	dest.writeInt(id); 
    	dest.writeString(url);
    	dest.writeString(name);
    	dest.writeString(description);
    	dest.writeInt(visible);
    	dest.writeString(modicon);
    	dest.writeString(modname);
    	dest.writeString(modplural);
    	dest.writeInt(availablefrom);
    	dest.writeInt(availableuntil);
    	dest.writeInt(indent);
    	dest.writeTypedList(contents); 
    }
    
    private Module(Parcel in) { 
        this.id = in.readInt(); 
        this.url = in.readString();
        this.name = in.readString();
        this.description = in.readString();
        this.visible = in.readInt();
        this.modicon = in.readString();
        this.modname = in.readString();
        this.modplural = in.readString();
        this.availablefrom = in.readInt();
        this.availableuntil = in.readInt();
        this.indent = in.readInt();
        in.readTypedList(this.contents, Content.CREATOR); 
    } 
}
