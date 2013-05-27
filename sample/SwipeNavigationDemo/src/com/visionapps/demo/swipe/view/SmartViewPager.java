package com.visionapps.demo.swipe.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

/**
 * Custom {@link ViewPager} implementation that will handle horizontal scroll events by himself. <br>
 * Default ViewPager becomes hardly usable when it's nested into ScrollView based containers (such as ScrollView,
 * ListView, etc.). It is due to the fact that ScrollView based views will intercept any motion event with minimal (even
 * 1px) vertical shift. So to change page by scrolling with a default {@link ViewPager} user will need to move his
 * finger horizontally with zero vertical shift, which is obvious quite irritating.<br>
 * {@link SmartViewPager} has a much much better behavior at resolving scrolling directions.
 */
public class SmartViewPager extends ViewPager {

	// -----------------------------------------------------------------------
	//
	// Constructor
	//
	// -----------------------------------------------------------------------
	public SmartViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		mGestureDetector = new GestureDetector(context, new XScrollDetector());
	}

	// -----------------------------------------------------------------------
	//
	// Fields
	//
	// -----------------------------------------------------------------------
	private GestureDetector mGestureDetector;
	private boolean mIsLockOnHorizontalAxis = false;

	// -----------------------------------------------------------------------
	//
	// Methods
	//
	// -----------------------------------------------------------------------
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// decide is horizontal axis is locked already, or do we need to check the scrolling direction
		if (!mIsLockOnHorizontalAxis)
			mIsLockOnHorizontalAxis = mGestureDetector.onTouchEvent(event);

		// release the lock at finger up
		if (event.getAction() == MotionEvent.ACTION_UP)
			mIsLockOnHorizontalAxis = false;

		getParent().requestDisallowInterceptTouchEvent(mIsLockOnHorizontalAxis);
		return super.onTouchEvent(event);
	}

	// -----------------------------------------------------------------------
	//
	// Inner Classes
	//
	// -----------------------------------------------------------------------
	private class XScrollDetector extends SimpleOnGestureListener {

		// -----------------------------------------------------------------------
		//
		// Methods
		//
		// -----------------------------------------------------------------------
		/**
		 * @return true - if we're scrolling in X direction, false - in Y direction.
		 */
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			return Math.abs(distanceX) > Math.abs(distanceY);
		}

	}
}
