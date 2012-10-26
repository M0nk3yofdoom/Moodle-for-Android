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
/**
 * This database connection modified from the free Lars Vogel tutorial
 * 
 * http://www.vogella.com/articles/AndroidSQLite/article.html
 */

package moodle.android.moodle.helpers;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class CommentsDataSource {

  // Database fields
  private SQLiteDatabase database;
  private DatabaseHelper dbHelper;
  private String[] allColumns = { DatabaseHelper.COLUMN_ID,
		  DatabaseHelper.COLUMN_COMMENT };

  public CommentsDataSource(Context context) {
    dbHelper = new DatabaseHelper(context);
  }

  public void open() throws SQLException {
    database = dbHelper.getWritableDatabase();
  }

  public void close() {
    dbHelper.close();
  }

  public DBComments createComment(String comment) {
    ContentValues values = new ContentValues();
    values.put(DatabaseHelper.COLUMN_COMMENT, comment);
    long insertId = database.insert(DatabaseHelper.TABLE_COMMENTS, null,
        values);
    Cursor cursor = database.query(DatabaseHelper.TABLE_COMMENTS,
        allColumns, DatabaseHelper.COLUMN_ID + " = " + insertId, null,
        null, null, null);
    cursor.moveToFirst();
    DBComments newComment = cursorToComment(cursor);
    cursor.close();
    return newComment;
  }

  public void deleteComment(DBComments comment) {
    long id = comment.getId();
    System.out.println("Comment deleted with id: " + id);
    database.delete(DatabaseHelper.TABLE_COMMENTS, DatabaseHelper.COLUMN_ID
        + " = " + id, null);
  }

  public List<DBComments> getAllComments() {
    List<DBComments> comments = new ArrayList<DBComments>();

    Cursor cursor = database.query(DatabaseHelper.TABLE_COMMENTS,
        allColumns, null, null, null, null, null);

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      DBComments comment = cursorToComment(cursor);
      comments.add(comment);
      cursor.moveToNext();
    }
    // Make sure to close the cursor
    cursor.close();
    return comments;
  }

  private DBComments cursorToComment(Cursor cursor) {
    DBComments comment = new DBComments();
    comment.setId(cursor.getLong(0));
    comment.setComment(cursor.getString(1));
    return comment;
  }
} 
