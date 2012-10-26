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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EntityUtils;
//import org.json.JSONObject;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;

public class FileManager {
	
	private static final String TAG = "FileManager";
	
	private static FileManager instance = new FileManager(); 
    static Context context; 
 
    public static FileManager getInstance(Context ctx) { 
        context = ctx; 
        return instance; 
    }

	private final String PATH = Environment.getExternalStorageDirectory().getPath();  //put the downloaded file here
	
	public File DownloadFromUrl(String fileURL, String fileName, String courseDirectoryAndType) {  //this is the downloader method
	
		File file = null;
		try {
			URL url = new URL(fileURL); //you can write here any link
			
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
			
				// create a File object for the parent directory 
				File fileDirectory = new File(PATH + "/Moodle/" + courseDirectoryAndType); 
				// have the object build the directory structure, if needed. 
				fileDirectory.mkdirs(); 
				// create a File object for the output file 
				file = new File(fileDirectory, fileName); 
				
				long startTime = System.currentTimeMillis();			
				Log.d(TAG, "download begining");
				Log.d(TAG, "download url:" + url);
				Log.d(TAG, "downloaded file name:" + fileName);
				
				/* Open a connection to that URL. */
				URLConnection ucon = url.openConnection();
				
				/*
				 * Define InputStreams to read from the URLConnection.
				 */
				InputStream is = ucon.getInputStream();						
				BufferedInputStream bis = new BufferedInputStream(is);
				
				/*
				 * Read bytes to the Buffer until there is nothing more to read(-1).
				 */
				ByteArrayBuffer baf = new ByteArrayBuffer(50);
				int current = 0;
				while ((current = bis.read()) != -1) {
						baf.append((byte) current);
				}
				
				/* Convert the Bytes read to a String. */
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(baf.toByteArray());
				fos.close();
				
				Log.d(TAG, "download ready in"
								+ ((System.currentTimeMillis() - startTime) / 1000)
								+ " sec");	
        	}
			
		} catch (IOException e) {
				Log.d(TAG, "Error: " + e);
		}
		
		return file;
	}
	
	public void UploadToUrl(String siteUrl, String token, String filepath) {		
		
		String url = siteUrl + "/webservice/upload.php?token=" + token;
		HttpClient httpclient = new DefaultHttpClient(); 
	    httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1); 
	 
	    org.apache.http.client.methods.HttpPost httppost = new org.apache.http.client.methods.HttpPost(url); 
	    File file = new File(filepath); 
	    
	    String mimetype = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(filepath.substring(filepath.lastIndexOf("."))));
	 
	    MultipartEntity mpEntity = new MultipartEntity(); 
	    ContentBody cbFile = new FileBody(file, mimetype); 
	    mpEntity.addPart("userfile", cbFile); 
	 
	 
	    httppost.setEntity(mpEntity); 
	    Log.d(TAG, "upload executing request " + httppost.getRequestLine());
	    try {
	    	
		    HttpResponse response = httpclient.execute(httppost);
	
		    HttpEntity resEntity = response.getEntity(); 
		 
		    Log.d(TAG, "upload line status " + response.getStatusLine()); 
		    if (resEntity != null) { 
		    	Log.d(TAG, "upload " + EntityUtils.toString(resEntity)); 
		    	//JSONObject jObject = new JSONObject(EntityUtils.toString(resEntity));
		    }
		    else {
		    	Log.d(TAG, "upload error: " + EntityUtils.toString(resEntity));
		    }		    	
		 
	    }
	    catch (Exception ex) {
	    	Log.d(TAG, "Error: " + ex);
	    }
	    
	    httpclient.getConnectionManager().shutdown(); 
	}
	
	
}
