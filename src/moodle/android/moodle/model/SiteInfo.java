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

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class SiteInfo implements Parcelable {
	//private static SiteInfo instance;

	public SiteInfo() {

	}

//	public static SiteInfo getInstance() {
//		if (instance == null) {
//			synchronized(SiteInfo.class) {
//				if (instance == null) {
//					instance = new SiteInfo();
//				}
//			}
//		}
//		return instance;
//	}

	private String sitename;	
	public void setSiteName(String sitename) {
       this.sitename = sitename;
    }

    public String getSiteName() {
       return sitename;
    }
    
    private String username;	
	public void setUsername(String username) {
       this.username = username;
    }

    public String getUsername() {
       return username;
    }
	    
    private String firstname;	
	public void setFirstname(String firstname) {
       this.firstname = firstname;
    }

    public String getFirstname() {
       return firstname;
    }
	    
    private String lastname;	
	public void setLastname(String lastname) {
       this.lastname = lastname;
    }

    public String getLastname() {
       return lastname;
    }
	    
    private String fullname;	
	public void setFullname(String fullname) {
       this.fullname = fullname;
    }

    public String getFullname() {
       return fullname;
    }
	    
    private int userid;	
	public void setUserid(int userid) {
       this.userid = userid;
    }

    public int getUserid() {
       return userid;
    }
	    
    private String siteurl;	
	public void setSiteUrl(String siteurl) {
       this.siteurl = siteurl;
    }

    public String getSiteUrl() {
       return siteurl;
    }
	    
    private String userpictureurl;	
	public void setUserPictureUrl(String userpictureurl) {
       this.userpictureurl = userpictureurl;
    }

    public String getUserPictureUrl() {
       return userpictureurl;
    }
    
    private boolean downloadfiles;	
	public void setDownloadFiles(boolean downloadfiles) {
       this.downloadfiles = downloadfiles;
    }

    public boolean getDownloadFiles() {
       return downloadfiles;
    }
    
    private Map<String,String> functions = Collections.synchronizedMap(new TreeMap<String,String>());

    public void addFunction(String name, String version) {
    	functions.put(name, version);
    }	 

    public boolean editFunction(String oldVersion, String newVersion, String name) {

        if(functions.containsKey(oldVersion)) {
        	functions.remove(oldVersion);
        	functions.put(newVersion, name);
            return true;
        }

        return false;
    }	
    
    public Map<String,String> getFunctions() {
    	return functions;
    }
    
    public String getFunctionByName(String name) {
    	return functions.get(name);
    }

//    public void viewFunctions() {
//
//        Set<Map.Entry<String, String>> set = functions.entrySet();
//        Iterator<Map.Entry<String, String>> it = set.iterator();
//        while(it.hasNext()) {
//            Map.Entry<String, String> entry = it.next();
//            System.out.println(entry.getKey() + ": " + entry.getValue());
//        }
//    } 
    

    public void populateSiteInfo(JSONObject jsonObject) {
    
    	    	 
    	try {  
    		if (jsonObject != null) {
    			
	    		String sitename = jsonObject.getString("sitename"); 
	    		this.setSiteName(sitename);
		        String username = jsonObject.getString("username"); 
		        this.setUsername(username);
		        String firstname = jsonObject.getString("firstname"); 
		        this.setFirstname(firstname);
		        String lastname = jsonObject.getString("lastname"); 
		        this.setLastname(lastname);
		        String fullname = jsonObject.getString("fullname"); 
		        this.setFullname(fullname);
		        String userid = jsonObject.getString("userid"); 
		        this.setUserid(Integer.valueOf(userid));
		        String siteurl = jsonObject.getString("siteurl"); 
		        this.setSiteUrl(siteurl);
		        String userpictureurl = jsonObject.getString("userpictureurl"); 
		        this.setUserPictureUrl(userpictureurl);
		        String downloadfiles = jsonObject.getString("downloadfiles"); 
		        this.setDownloadFiles((downloadfiles.equals("1")) ? Boolean.TRUE : Boolean.FALSE);
		 	    
		        JSONArray functions = jsonObject.getJSONArray("functions");
	    	    // looping through All Contacts 
	    	    for(int i = 0; i < functions.length(); i++){ 
	    	        JSONObject c = functions.getJSONObject(i); 
	    	 
	    	        // Storing each json item in variable 
	    	        String name = c.getString("name"); 
	    	        String version = c.getString("version");   
	    	        this.addFunction(name, version);
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
    public static final Parcelable.Creator<SiteInfo> CREATOR = new Parcelable.Creator<SiteInfo>() { 
        public SiteInfo createFromParcel(Parcel in) { 
            return new SiteInfo(in); 
        } 
 
        public SiteInfo[] newArray(int size) { 
            return new SiteInfo[size]; 
        } 
    }; 
 
    // write your object's data to the passed-in Parcel 
    public void writeToParcel(Parcel dest, int flags) { 
    	dest.writeString(sitename);
    	dest.writeString(username);
    	dest.writeString(firstname);
    	dest.writeString(lastname);
    	dest.writeString(fullname);
    	dest.writeInt(userid);
    	dest.writeString(siteurl);
    	dest.writeString(userpictureurl);
    	dest.writeByte((byte) (downloadfiles ? 1 : 0));  
    	
    	dest.writeInt(functions.size());
        for (String s: functions.keySet()) {
            dest.writeString(s);
            dest.writeString(functions.get(s));
        }

    }
    
    private SiteInfo(Parcel in) { 
        this.sitename = in.readString();
        this.username = in.readString();
        this.firstname = in.readString();
        this.lastname = in.readString();
        this.fullname = in.readString();
        this.userid = in.readInt();
        this.siteurl = in.readString();
        this.userpictureurl = in.readString();
        this.downloadfiles = in.readByte() == 1;

        int count = in.readInt();
        for (int i = 0; i < count; i++) {
        	this.addFunction(in.readString(), in.readString());
        }

    }  
}
