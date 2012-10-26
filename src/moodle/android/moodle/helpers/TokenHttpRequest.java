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

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

public class TokenHttpRequest {

	/*
	 * Gets the token based on the username, password and url
	 * It returns the token 
	 */
	public String doHTTPRequest(String url){ 
        String responseBody = ""; 
        String token = "";
        
    	DefaultHttpClient httpClient = new DefaultHttpClient();
        
        // Creating HTTP Post 
        HttpGet httpPost = new HttpGet(url); 
  
        try { 
        	ResponseHandler<String> responseHandler=new BasicResponseHandler(); 
            responseBody = httpClient.execute(httpPost, responseHandler); 
            
            JSONObject jObject = new JSONObject(responseBody);
            token = jObject.getString("token");
        } catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();			
        } catch (ClientProtocolException e) { 
            // writing exception to log 
            e.printStackTrace(); 
        } catch (IOException e) { 
            // writing exception to log 
            e.printStackTrace();       
        } 
            
        return token; 
    } 
	
}
