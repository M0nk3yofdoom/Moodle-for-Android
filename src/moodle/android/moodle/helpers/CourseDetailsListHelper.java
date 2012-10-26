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
import moodle.android.moodle.model.Content;
import moodle.android.moodle.model.CourseContent;
import moodle.android.moodle.model.Module;

import android.content.Context;

public class CourseDetailsListHelper {

	private static CourseDetailsListHelper instance = new CourseDetailsListHelper(); 
	static Context context;
	
	public static CourseDetailsListHelper getInstance(Context ctx) { 
        context = ctx; 
        return instance; 
    }
	
	public ArrayList<HashMap<String, String>> populateCourseOverview(ArrayList<CourseContent> coursecontent) {
		
		String documentCount = "(0)";
		String assignmentCount = "(0)";
		String gradesCount = "(0)";
		String forumCount = "(0)";
		String offlineCount = "(0)";
		
		if (coursecontent.size() > 0) {
			int docCount = 0;
			int assignCount = 0;
			int gradeCount = 0;
			int frmCount = 0;
			int olCount = 0;
			
			for (CourseContent content : coursecontent) {
				for (Module module : content.getModules()) {
					if (module.getModName().equalsIgnoreCase("resource")) {
						for (Content item : module.getContents()) {
							if (item.getType().equalsIgnoreCase("file")) {
								docCount++;
							}
						}
					}
					else if (module.getModName().equalsIgnoreCase("assignment")) {
						assignCount++;
					//}
					//else if (module.getModName().equalsIgnoreCase("assignment")) {
						gradeCount++;
					}
					else if (module.getModName().equalsIgnoreCase("forum")) {
						frmCount++;
					}
					else if (module.getModName().equalsIgnoreCase("online")) {
						olCount++;
					}
				}
			}
			
			documentCount = "(" + String.valueOf(docCount) + ")";
			assignmentCount = "(" + String.valueOf(assignCount) + ")";
			gradesCount = "(" + String.valueOf(gradeCount) + ")";
			forumCount = "(" + String.valueOf(frmCount) + ")";
			offlineCount = "(" + String.valueOf(olCount) + ")";
		}
		
		ArrayList<HashMap<String, String>> courseOverviewList = new ArrayList<HashMap<String, String>>();
		
		HashMap<String, String> map = new HashMap<String, String>();
    	
		// adding each item to HashMap key => value		
    	map.put(LazyAdapter.KEY_ID, "0");
		map.put(LazyAdapter.KEY_HEADER, context.getResources().getString(R.string.documents_view));
		map.put(LazyAdapter.KEY_DESCRIPTION, context.getResources().getString(R.string.documents_desc_view));
		map.put(LazyAdapter.KEY_AVAILABLE, documentCount);
		map.put(LazyAdapter.KEY_THUMB, String.valueOf(R.drawable.documents_icon));

		// adding HashList to ArrayList
		courseOverviewList.add(map);
		
		map = new HashMap<String, String>();
		// adding each item to HashMap key => value		
    	map.put(LazyAdapter.KEY_ID, "1");
		map.put(LazyAdapter.KEY_HEADER, context.getResources().getString(R.string.assignments_view));
		map.put(LazyAdapter.KEY_DESCRIPTION, context.getResources().getString(R.string.assignments_desc_view));
		map.put(LazyAdapter.KEY_AVAILABLE, assignmentCount);
		map.put(LazyAdapter.KEY_THUMB, String.valueOf(R.drawable.assignment_icon));

		// adding HashList to ArrayList
		courseOverviewList.add(map);
    	
		map = new HashMap<String, String>();
		// adding each item to HashMap key => value		
    	map.put(LazyAdapter.KEY_ID, "2");
		map.put(LazyAdapter.KEY_HEADER, context.getResources().getString(R.string.grades_view));
		map.put(LazyAdapter.KEY_DESCRIPTION, context.getResources().getString(R.string.grades_desc_view));
		map.put(LazyAdapter.KEY_AVAILABLE, gradesCount);
		map.put(LazyAdapter.KEY_THUMB, String.valueOf(R.drawable.grade_icon));

		// adding HashList to ArrayList
		courseOverviewList.add(map);
		
		map = new HashMap<String, String>();
		// adding each item to HashMap key => value		
    	map.put(LazyAdapter.KEY_ID, "3");
		map.put(LazyAdapter.KEY_HEADER, context.getResources().getString(R.string.forum_view));
		map.put(LazyAdapter.KEY_DESCRIPTION, context.getResources().getString(R.string.forum_desc_view));
		map.put(LazyAdapter.KEY_AVAILABLE, forumCount);
		map.put(LazyAdapter.KEY_THUMB, String.valueOf(R.drawable.forum_icon));

		// adding HashList to ArrayList
		courseOverviewList.add(map);
		
		map = new HashMap<String, String>();
		// adding each item to HashMap key => value		
    	map.put(LazyAdapter.KEY_ID, "4");
		map.put(LazyAdapter.KEY_HEADER, context.getResources().getString(R.string.offline_view));
		map.put(LazyAdapter.KEY_DESCRIPTION, context.getResources().getString(R.string.offline_desc_view));
		map.put(LazyAdapter.KEY_AVAILABLE, offlineCount);
		map.put(LazyAdapter.KEY_THUMB, String.valueOf(R.drawable.offlinefolder_icon));

		// adding HashList to ArrayList
		courseOverviewList.add(map);
		
		return courseOverviewList;


	}

}
