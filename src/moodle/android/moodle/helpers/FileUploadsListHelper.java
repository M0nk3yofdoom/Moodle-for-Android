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

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.Environment;

public class FileUploadsListHelper {

	private static FileUploadsListHelper instance = new FileUploadsListHelper(); 
	static Context context;
	
	public static FileUploadsListHelper getInstance(Context ctx) { 
        context = ctx; 
        return instance; 
    }
	
	public ArrayList<HashMap<String, String>> populateUploadFiles(List<File> files) {
		
		ArrayList<HashMap<String, String>> fileArray = null;
		
		if (files.size() > 0) {
			fileArray = new ArrayList<HashMap<String, String>>();
			
			int elementCount = 0;
			for (File file : files) {
				String fileextension = file.getName().substring(file.getName().lastIndexOf('.') + 1);  
				String fullextension = "." + fileextension;
				String filename = file.getName().replace(fullextension, "");
				
				int thumbresourse = FileTypeThumbHelper.getInstance(context).getThumbResourse(fileextension);
				
				long time = file.lastModified();
				Date modifyDate = new Date(time);
				
				SimpleDateFormat dateformat = new SimpleDateFormat("dd\\MM\\yyyy");	
				
				String directory = file.getParent().replace(Environment.getExternalStorageDirectory().getPath(), "");
				
				String eol = System.getProperty("line.separator");   
				String directoryDateModified = "Directory: " + directory + eol + "Last Modified Date: " + dateformat.format(modifyDate);
				
				long currentfilesize = file.length();
				
				float theSize = 0;
				String filesize;
				if (currentfilesize/1024 > 1024) {
					theSize = (currentfilesize/1024)/1024;
					filesize = String.valueOf(theSize) + "Mb";
				}
				else {
					theSize = currentfilesize/1024;
					filesize = String.valueOf(theSize) + "kb";
				}
				
    			HashMap<String, String> map = new HashMap<String, String>();
    	    	
    			// adding each item to HashMap key => value		
    	    	map.put(LazyAdapter.KEY_ID, String.valueOf(elementCount));
    			map.put(LazyAdapter.KEY_HEADER, filename);
    			map.put(LazyAdapter.KEY_DESCRIPTION, directoryDateModified);
    			map.put(LazyAdapter.KEY_AVAILABLE, filesize);
    			map.put(LazyAdapter.KEY_THUMB, String.valueOf(thumbresourse));
    			map.put(LazyAdapter.KEY_OTHER, file.getPath());

    			// adding HashList to ArrayList
    			fileArray.add(map);
	    		elementCount++;
			}
		}
		
		return fileArray;		
	}
}
