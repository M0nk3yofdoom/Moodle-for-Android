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
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

public class SectionListView extends ListView implements OnScrollListener {

	private View transparentView;
	
	public SectionListView(final Context context, final AttributeSet attrs, final int defStyle) {
		super(context, attrs, defStyle);
		commonInitialisation();
	}
	
	public SectionListView(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		commonInitialisation();
	}
	
	public SectionListView(final Context context) {
		super(context);
		commonInitialisation();
	}
	
	protected final void commonInitialisation() {
		setOnScrollListener(this);
		setVerticalFadingEdgeEnabled(false);
		setFadingEdgeLength(0);
	}
	
	@Override 
	public void setAdapter(final ListAdapter adapter) {
		if (!(adapter instanceof SectionListAdapter)) {
			throw new IllegalArgumentException("The adapter needs to be of type " + SectionListAdapter.class + " and is " + adapter.getClass());
		}
		super.setAdapter(adapter);
		final ViewParent parent = getParent();
		if (!(parent instanceof FrameLayout)) {
			throw new IllegalStateException("Section List should have FrameLayout as parent!");
		}
		if (transparentView != null) {
			((FrameLayout)parent).removeView(transparentView);
		}
		transparentView = ((SectionListAdapter)adapter).getTransparentSectionView();
		final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		((FrameLayout)parent).addView(transparentView, lp);
		if (adapter.isEmpty()) {
			transparentView.setVisibility(View.INVISIBLE);
		}
	}
	
	@Override 
	public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {
		final SectionListAdapter adapter = (SectionListAdapter)getAdapter();
		if (adapter != null) {
			adapter.makeSectionInvisibleIfFirstInList(firstVisibleItem);
		}
	}
	
	@Override 
	public void onScrollStateChanged(final AbsListView view, final int scrollState) {
		// do nothing
	}
}

