package com.ramz.parallelscroll;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ramz.parallelscroll.adaptors.PlayerAdapter;
import com.ramz.parallelscroll.adaptors.PlayerFragmentAdapter;
import com.ramz.parallelscroll.util.Constants;
import com.ramz.parallelscroll.util.RecyclerItemClickListener;
import com.ramz.parallelscroll.util.ScreenUtil;
import com.ramz.parallelscroll.util.StartSnapHelper;
import com.ramz.parallelscroll.views.DelayedRecyclerView;

/**
 * Created by ramesh on 1/8/18.
 *
 * @auther Ramesh M Nair
 */
public class MainActivity extends AppCompatActivity {

    DelayedRecyclerView delayedRecyclerView;
    ViewPager pager;
    StartSnapHelper snapHelperStart;
    PlayerAdapter adaptor;

    int lastPosition=0;
    boolean isScrolling=false;
    protected boolean mScrollEnabled = false;
    int mItemWidth = 1;

    LinearLayoutManager layoutManagerStart;

    RelativeLayout pickerIndicatorLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        delayedRecyclerView=(DelayedRecyclerView)findViewById(R.id.delayedRecyclerView);
        pickerIndicatorLayout=(RelativeLayout)findViewById(R.id.picker_indicator_layout);
        pager=(ViewPager)findViewById(R.id.pager);

        layoutManagerStart=new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        delayedRecyclerView.setLayoutManager(layoutManagerStart);

        adaptor= new PlayerAdapter(getApplicationContext(),getWindow().getWindowManager().getDefaultDisplay().getWidth());
        delayedRecyclerView.setAdapter(adaptor);
        pager.setAdapter(new PlayerFragmentAdapter(getSupportFragmentManager()));

        /*Snap helper help you snap the selected item to the Start*/

        snapHelperStart=  new StartSnapHelper();
        snapHelperStart.attachToRecyclerView(delayedRecyclerView);

        /*Making the indicator layout same as the width of one cell*/

        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams((getWindow().getWindowManager().getDefaultDisplay().getWidth() / Constants.CELL_VISIBLITY), RelativeLayout.LayoutParams.MATCH_PARENT);
        pickerIndicatorLayout.setLayoutParams(params);


        /*Click listener*/
        delayedRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), delayedRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        mScrollEnabled=true;
                        lastPosition=position;

                        delayedRecyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                View view = layoutManagerStart.findViewByPosition(position);


                                if (view == null) {
                                    Log.e("error", "Cant find target View for initial Snap");
                                    return;
                                }

                                int[] snapDistance = snapHelperStart.calculateDistanceToFinalSnap(layoutManagerStart, view);
                                if (snapDistance[0] != 0 || snapDistance[1] != 0) {

                                    /*will scroll to selected postion and snap it on the 1st item*/
                                    delayedRecyclerView.scrollBy(snapDistance[0], snapDistance[1]);
                                    pager.setCurrentItem(position);
                                    adaptor.setCurrentActivrPosition(lastPosition);
                                    adaptor.notifyDataSetChanged();

                                }



                            }
                        });

                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        Toast.makeText(getApplicationContext(),Constants.playerName[position],Toast.LENGTH_LONG).show();
                    }
                })
        );


        delayedRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(newState==RecyclerView.SCROLL_STATE_DRAGGING)
                {
                    adaptor.setCurrentActivrPosition(lastPosition);
                    adaptor.notifyDataSetChanged();
                }


                /*When scroll is about to stop we find the snap position and set current viewpager based on the position*/
                if(newState == RecyclerView.SCROLL_STATE_IDLE) {

                    View centerView = snapHelperStart.findSnapView(layoutManagerStart);
                    if(centerView==null)
                    {
                        if(lastPosition!=(Constants.playerName.length-1)) {
                            lastPosition = Constants.playerName.length - 1;
                            mScrollEnabled =true;


                            pager.setCurrentItem(lastPosition);

                        }
                        else
                        {

                            pager.setCurrentItem(lastPosition);
                        }
                    }
                    else
                    {

                        int pos = layoutManagerStart.getPosition(centerView);
                        if(lastPosition!=pos) {
                            lastPosition = pos;

                            mScrollEnabled =true;

                            pager.setCurrentItem(lastPosition);

                        }
                        else
                        {

                            pager.setCurrentItem(lastPosition);

                        }
                    }
                    adaptor.setCurrentActivrPosition(lastPosition);
                    adaptor.notifyDataSetChanged();

                }
            }
        });

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (!mScrollEnabled) {

                    scrollToTab(position, positionOffset);
                }


            }

            @Override
            public void onPageSelected(final int position) {


            }

            @Override
            public void onPageScrollStateChanged(int state) {

                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    mScrollEnabled =false;
                    isScrolling=false;





                }
                else if(state==ViewPager.SCROLL_STATE_DRAGGING)
                {
                    isScrolling=true;
                    mScrollEnabled =false;


                }
            }
        });




    }


    /*Core part of the scrolling*/
    int mOldPosition;
    int mOldScrollOffset;
    protected float mOldPositionOffset;
    protected int mIndicatorPosition;

    protected void scrollToTab(int position, float positionOffset) {
        int scrollOffset = 0;

        if (mItemWidth == 1 && delayedRecyclerView != null) {
            View child = delayedRecyclerView.getChildAt(0);
            if (child != null) {
                mItemWidth = child.getMeasuredWidth() * 2;
            }
        }

        View selectedView = layoutManagerStart.findViewByPosition(position);
        View nextView = layoutManagerStart.findViewByPosition(position + 1);


        if (nextView != null) {
            if (selectedView != null) {
                int width = delayedRecyclerView.getMeasuredWidth();
                float sLeft = (position == 0) ? 24 : width / mItemWidth - selectedView.getMeasuredWidth() / mItemWidth; // left edge of selected tab
                float sRight = sLeft + selectedView.getMeasuredWidth(); // right edge of selected tab
                float nLeft = width / mItemWidth - nextView.getMeasuredWidth() / mItemWidth; // left edge of next tab
                float distance = sRight - nLeft; // total distance that is needed to distance to next tab
                float dx = distance * positionOffset;
                scrollOffset = (int) (sLeft - dx);
            } else {
                scrollOffset = (mItemWidth/2) * (-1);
            }

            mIndicatorPosition = position;
            delayedRecyclerView.stopScroll();

            if ((position != mOldPosition || scrollOffset != mOldScrollOffset)) {

                layoutManagerStart.scrollToPositionWithOffset(position, scrollOffset);

            } else {

                Log.d("TAG_KKB_RV_POS", "ELSE mOldScrollOffset: " + mOldScrollOffset +", scrollOffset: " + scrollOffset
                        + ", mOldPosition:" + mOldPosition);
                Log.d("Szzz", scrollOffset + "outside ifffff" + position);

            }
            updateCurrentPosition(position, positionOffset - mOldPositionOffset, positionOffset);

        }
        else
        {
            delayedRecyclerView.scrollToPosition(position);
            pager.setCurrentItem(position);
        }

        mOldPosition = position;
        mOldScrollOffset = scrollOffset;
        mOldPositionOffset=positionOffset;

    }

    protected void updateCurrentPosition(int position, float dx, float positionOffset) {
        if (adaptor == null) {
            return;
        }

        int activePosition = -1;
        if (dx > 0 && positionOffset >= 0.59900004) {
            activePosition = position + 1;
        } else if (dx < 0 && positionOffset <= 0.40099996) {
            activePosition = position;
        }
        if (activePosition >= 0 && activePosition != adaptor.getCurrentIndicatorPosition()) {
            adaptor.setCurrentActivrPosition(activePosition);
            adaptor.notifyDataSetChanged();
        }
    }
}
