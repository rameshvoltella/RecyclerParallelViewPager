package com.ramz.parallelscroll.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 *  @auther Ramesh M Nair
 *  Its  made just to make a delay on scroll of recylerview
 */
public class DelayedRecyclerView extends RecyclerView {


    public DelayedRecyclerView(Context context) {
        super(context);
    }

    public DelayedRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DelayedRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {

        velocityX *= 0.01;

        return super.fling(velocityX, velocityY);
    }


}