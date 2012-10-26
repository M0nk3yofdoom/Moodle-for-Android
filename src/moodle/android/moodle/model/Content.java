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

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Content implements Parcelable  {
		
	//private static Content instance;

	public Content() {

	}

//	public static Content getInstance() {
//		if (instance == null) {
//			synchronized(Content.class) {
//				if (instance == null) {
//					instance = new Content();
//				}
//			}
//		}
//		return instance;
//	}
	
	private String type;	
	public void setType(String type) {
       this.type = type;
    }

    public String getType() {
       return type;
    }
    
    private String filename;	
	public void setFileName(String filename) {
       this.filename = filename;
    }

    public String getFileName() {
       return filename;
    }
    
    private String filepath;	
	public void setFilePath(String filepath) {
       this.filepath = filepath;
    }

    public String getFilePath() {
       return filepath;
    }
	
	private int filesize;	
	public void setFileSize(int filesize) {
       this.filesize = filesize;
    }

    public int getFileSize() {
       return filesize;
    }

	private String fileurl;	
	public void setFileUrl(String fileurl) {
       this.fileurl = fileurl;
    }

    public String getFileUrl() {
       return fileurl;
    }
    
    private String content;	
	public void setContent(String content) {
       this.content = content;
    }

    public String getContent() {
       return content;
    }
    
    private long timecreated;	
	public void setTimeCreated(long timecreated) {
       this.timecreated = timecreated;
    }

    public long getTimeCreated() {
       return timecreated;
    }
    
    private long timemodified;	
	public void setTimeModified(long timemodified) {
       this.timemodified = timemodified;
    }

    public long getTimeModified() {
       return timemodified;
    }
    
    private int sortorder;	
	public void setSortOrder(int sortorder) {
       this.sortorder = sortorder;
    }

    public int getSortOrder() {
       return sortorder;
    }
    
    private int userid;	
	public void setUserId(int userid) {
       this.userid = userid;
    }

    public int getUserId() {
       return userid;
    }
	    
    private String author;	
	public void setAuthor(String author) {
       this.author = author;
    }

    public String getAuthor() {
       return author;
    }
    
    private String license;	
	public void setLicense(String license) {
       this.license = license;
    }

    public String getLicense() {
       return license;
    }

    public void populateContent(JSONObject jsonObject) {
     
		if (jsonObject != null) {    			
	        String type = jsonObject.optString("type"); 
	        if (type != null && type.trim().length() > 0)
	        	this.setType(type);
	        String filename = jsonObject.optString("filename"); 
	        if (filename != null && filename.trim().length() > 0)
	        	this.setFileName(filename);
	        String filepath = jsonObject.optString("filepath"); 
	        if (filepath != null && filepath.trim().length() > 0)
	        	this.setFilePath(filepath);
	        String filesize = jsonObject.optString("filesize"); 
	        if (filesize != null && filesize.trim().length() > 0)
	        	this.setFileSize(Integer.valueOf(filesize));
	        String fileurl = jsonObject.optString("fileurl"); 
	        if (fileurl != null && fileurl.trim().length() > 0)
	        	this.setFileUrl(fileurl);
	        String content = jsonObject.optString("content"); 
	        if (content != null && content.trim().length() > 0) 
	        	this.setContent(content);		        
	        String timecreated = jsonObject.optString("timecreated");  
	        if (timecreated != null && timecreated.trim().length() > 0)
	        	this.setTimeCreated(Long.valueOf(timecreated));
	        String timemodified = jsonObject.optString("timemodified");  
	        if (timemodified != null && timemodified.trim().length() > 0)
	        	this.setTimeModified(Long.valueOf(timemodified));		        
	        String sortorder = jsonObject.optString("sortorder");  
	        if (sortorder != null && sortorder.trim().length() > 0)
	        	this.setSortOrder(Integer.valueOf(sortorder));
	        String userid = jsonObject.optString("userid");  
	        if (userid != null && userid.trim().length() > 0)
	        	this.setUserId(Integer.valueOf(userid));
	        String author = jsonObject.optString("author");  
	        if (author != null && author.trim().length() > 0)
	        	this.setAuthor(author);
	        String license = jsonObject.optString("license");  
	        if (license != null && license.trim().length() > 0)
	        	this.setLicense(license);		        
		}
    }

    /* everything below here is for implementing Parcelable */ 
	 
    // 99.9% of the time you can just ignore this 
    public int describeContents() { 
        return 0; 
    } 
    
 // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods 
    public static final Parcelable.Creator<Content> CREATOR = new Parcelable.Creator<Content>() { 
        public Content createFromParcel(Parcel in) { 
            return new Content(in); 
        } 
 
        public Content[] newArray(int size) { 
            return new Content[size]; 
        } 
    }; 
 
    // write your object's data to the passed-in Parcel 
    public void writeToParcel(Parcel dest, int flags) { 
    	dest.writeString(type); 
    	dest.writeString(filename);
    	dest.writeString(filepath);
    	dest.writeInt(filesize);
    	dest.writeString(fileurl);
    	dest.writeString(content);
    	dest.writeLong(timecreated);
    	dest.writeLong(timemodified);
    	dest.writeInt(sortorder);
    	dest.writeInt(userid);
    	dest.writeString(author);
    	dest.writeString(license);
    }
    
    private Content(Parcel in) { 
        this.type = in.readString(); 
        this.filename = in.readString();
        this.filepath = in.readString();
        this.filesize = in.readInt();
        this.fileurl = in.readString();
        this.content = in.readString();
        this.timecreated = in.readLong();
        this.timemodified = in.readLong();
        this.sortorder = in.readInt();
        this.userid = in.readInt();
        this.author = in.readString();
        this.license = in.readString();
    } 
}
