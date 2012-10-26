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

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class AppStatus { 
	 
    private static AppStatus instance = new AppStatus(); 
    static Context context; 
    ConnectivityManager connectivityManager; 
    NetworkInfo wifiInfo, mobileInfo; 
    boolean connected = false; 
 
    public static AppStatus getInstance(Context ctx) { 
        context = ctx; 
        return instance; 
    }
    
    public String getConnectionType(Context con) { 
    	String haveConnectedWifi = null; 
    	String haveConnectedMobile = null; 
	 
	    ConnectivityManager cm = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE); 
	    NetworkInfo[] netInfo = cm.getAllNetworkInfo(); 
	    for (NetworkInfo networkInfo : netInfo) { 
	        if (networkInfo.getTypeName().equalsIgnoreCase("WIFI")) 
	            if (networkInfo.isAvailable() && networkInfo.isConnected()) 
	                haveConnectedWifi = "WIFI"; 
	        if (networkInfo.getTypeName().equalsIgnoreCase("MOBILE")) 
	            if (networkInfo.isAvailable() && networkInfo.isConnected()) 
	                haveConnectedMobile = "MOBILE"; 
	    } 
	    return (haveConnectedWifi != null) ? haveConnectedWifi : haveConnectedMobile; 
	}
 
    public boolean isOnline(Context con) { 
    	boolean haveConnectedWifi = false; 
	    boolean haveConnectedMobile = false;
	    
		connectivityManager = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE); 
		NetworkInfo[] netInfo = connectivityManager.getAllNetworkInfo(); 
	    for (NetworkInfo networkInfo : netInfo) { 
	        if (networkInfo.getTypeName().equalsIgnoreCase("WIFI")) 
	            if (networkInfo.isAvailable() && networkInfo.isConnected()) 
	                haveConnectedWifi = true; 
	        if (networkInfo.getTypeName().equalsIgnoreCase("MOBILE")) 
	            if (networkInfo.isAvailable() && networkInfo.isConnected()) 
	                haveConnectedMobile = true; 
	    } 
	    return haveConnectedWifi || haveConnectedMobile; 
    } 
} 

