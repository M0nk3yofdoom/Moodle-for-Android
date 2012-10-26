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

import moodle.android.moodle.R;
import android.content.Context;

public class FileTypeThumbHelper {

	private static FileTypeThumbHelper instance = new FileTypeThumbHelper(); 
	static Context context;
	
	public static FileTypeThumbHelper getInstance(Context ctx) { 
        context = ctx; 
        return instance; 
    }
	
	public int getThumbResourse(String fileextension) {
		
		int thumbresourse = R.drawable.no_image;
		if (fileextension.contains("do"))
			thumbresourse = R.drawable.filetype_doc;
		else if (fileextension.equalsIgnoreCase("pdf"))
			thumbresourse = R.drawable.filetype_pdf;
		else if (fileextension.contains("xls") || fileextension.equalsIgnoreCase("csv"))
			thumbresourse = R.drawable.filetype_xlsx;
		else if (fileextension.equalsIgnoreCase("avi"))
			thumbresourse = R.drawable.filetype_avi;
		else if (fileextension.equalsIgnoreCase("bmp"))
			thumbresourse = R.drawable.filetype_bmp;
		else if (fileextension.equalsIgnoreCase("fla") || fileextension.equalsIgnoreCase("swl"))
			thumbresourse = R.drawable.filetype_fla;
		else if (fileextension.equalsIgnoreCase("gif"))
			thumbresourse = R.drawable.filetype_gif;
		else if (fileextension.equalsIgnoreCase("jpeg") || fileextension.equalsIgnoreCase("jpg"))
			thumbresourse = R.drawable.filetype_jpeg;
		else if (fileextension.equalsIgnoreCase("mov"))
			thumbresourse = R.drawable.filetype_mov;
		else if (fileextension.equalsIgnoreCase("mpeg"))
			thumbresourse = R.drawable.filetype_mpeg;
		else if (fileextension.contains("mp"))
			thumbresourse = R.drawable.filetype_mp;
		else if (fileextension.equalsIgnoreCase("png"))
			thumbresourse = R.drawable.filetype_png;
		else if (fileextension.contains("pp"))
			thumbresourse = R.drawable.filetype_pptx;
		else if (fileextension.equalsIgnoreCase("rar"))
			thumbresourse = R.drawable.filetype_rar;
		else if (fileextension.equalsIgnoreCase("text") || fileextension.equalsIgnoreCase("txt"))
			thumbresourse = R.drawable.filetype_text;
		else if (fileextension.equalsIgnoreCase("url"))
			thumbresourse = R.drawable.filetype_url;
		else if (fileextension.equalsIgnoreCase("wav"))
			thumbresourse = R.drawable.filetype_wav;
		else if (fileextension.equalsIgnoreCase("wmv"))
			thumbresourse = R.drawable.filetype_wmv;
		else if (fileextension.equalsIgnoreCase("zip"))
			thumbresourse = R.drawable.filetype_zip;
		
		return thumbresourse;
	}

}
