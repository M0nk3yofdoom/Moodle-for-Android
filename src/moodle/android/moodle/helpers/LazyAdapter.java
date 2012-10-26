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

import java.util.ArrayList;
import java.util.HashMap;

import moodle.android.moodle.R;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyAdapter extends BaseAdapter implements Filterable {
	
	private static final String TAG = "LazyAdapter";
    
	public static final String KEY_ITEM = "item"; // parent node
	public static final String KEY_ID = "id";
	public static final String KEY_HEADER = "header";
	public static final String KEY_DESCRIPTION = "description";
	public static final String KEY_AVAILABLE = "availability";
	public static final String KEY_THUMB = "thumb";
	public static final String KEY_OTHER = "other";
	
    private Activity activity;
    private ArrayList<HashMap<String, String>> data, filtered;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
    @SuppressWarnings("unchecked")
	public LazyAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        filtered = d;
        data = (ArrayList<HashMap<String, String>>)d.clone();
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return filtered.size();
    }

    public Object getItem(int position) {
        return filtered.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_row, null);

        TextView header = (TextView)vi.findViewById(R.id.header); // header title
        TextView description = (TextView)vi.findViewById(R.id.description); // description of item
        TextView availability = (TextView)vi.findViewById(R.id.availability); // available
        ImageView thumb_image= (ImageView)vi.findViewById(R.id.list_image); // thumb image
        
        HashMap<String, String> item = new HashMap<String, String>();
        item = filtered.get(position);
        
        // Setting all values in listview
        header.setText(item.get(KEY_HEADER));
        description.setText(item.get(KEY_DESCRIPTION));
        availability.setText(item.get(KEY_AVAILABLE));
        imageLoader.DisplayImage(item.get(KEY_THUMB), thumb_image);
        return vi;
    }
     
    public void notifyDataSetChanged() {  
        super.notifyDataSetChanged();  
    }  
        
    public Filter getFilter() { 
        return new Filter() {

            @SuppressWarnings("unchecked")
			protected void publishResults(CharSequence constraint, FilterResults results) { 
                Log.d(TAG, "**** PUBLISHING RESULTS for: " + constraint); 
                
                filtered = (ArrayList<HashMap<String, String>>)results.values; 
                notifyDataSetChanged();
            } 

            protected FilterResults performFiltering(CharSequence constraint) { 
                Log.d(TAG, "**** PERFORM FILTERING for: " + constraint); 
                constraint = constraint.toString().toLowerCase();
 
                FilterResults results = new FilterResults(); 
                if(constraint != null && constraint.toString().length() > 0) {
                	
                	ArrayList<HashMap<String, String>> filt = new ArrayList<HashMap<String, String>>();
                	ArrayList<HashMap<String, String>> lData = new ArrayList<HashMap<String, String>>();
                	
                	synchronized (this) { 
                		lData.addAll(data); 
                    } 
                	
                    for(int i = 0, l = lData.size(); i < l; i++) { 
                    	HashMap<String, String> m = lData.get(i); 
                        if(m.get(KEY_HEADER).toLowerCase().contains(constraint)) 
                            filt.add(m); 
                    } 
                    
                    results.count = filt.size(); 
                    results.values = filt;                 
                }                
                else { 
                	
                    synchronized(this) 
                    { 
                    	results.count = data.size(); 
                    	results.values = data; 
                    } 
                } 
                
                return results; 
            } 
        }; 
    } 

}
