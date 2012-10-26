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

import java.util.HashMap;

import moodle.android.moodle.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class StandardArrayAdapter extends ArrayAdapter<SectionListItem> {
	
	private final SectionListItem[] items;
    public ImageLoader imageLoader; 
	private Context context;
	
	public StandardArrayAdapter(Context context, final SectionListItem[] items) {
		super(context, 0, items);
		imageLoader=new ImageLoader(context.getApplicationContext());
		this.items = items;
		this.context = context;
	}
	 
	@Override 
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			final LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = vi.inflate(R.layout.list_row, null);
		}

		TextView header = (TextView)view.findViewById(R.id.header); // header title
        TextView description = (TextView)view.findViewById(R.id.description); // description of item
        TextView availability = (TextView)view.findViewById(R.id.availability); // available
        ImageView thumb_image= (ImageView)view.findViewById(R.id.list_image); // thumb image
        ImageView traffic_image= (ImageView)view.findViewById(R.id.traffic_image); // traffic image
        
        final SectionListItem currentItem = items[position];
        
    	@SuppressWarnings("unchecked")
		HashMap<String, String> item = (HashMap<String, String>)currentItem.item;
    
        // Setting all values in listview
        header.setText(item.get("header"));
        description.setText(item.get("description"));
        availability.setText(item.get("availability"));
        imageLoader.DisplayImage(item.get("thumb"), thumb_image);
        imageLoader.DisplayImage(item.get("traffic"), traffic_image);
        
		return view;
	}
}
