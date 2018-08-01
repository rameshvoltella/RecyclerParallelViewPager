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

import com.ramz.parallelscroll.adaptors.PlayerAdapter;
import com.ramz.parallelscroll.adaptors.PlayerFragmentAdapter;
import com.ramz.parallelscroll.util.Constants;
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
    protected boolean mScrollEanbled = false;
    int mItemWidth = 1;

    LinearLayoutManager layoutManagerStart;

    RelativeLayout pickerIndicator;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        delayedRecyclerView=(DelayedRecyclerView)findViewById(R.id.delayedRecyclerView);
        pickerIndicator=(RelativeLayout)findViewById(R.id.picker_indicator_layout);
        pager=(ViewPager)findViewById(R.id.pager);

        layoutManagerStart=new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        delayedRecyclerView.setLayoutManager(layoutManagerStart);

        adaptor= new PlayerAdapter(getApplicationContext(),getWindow().getWindowManager().getDefaultDisplay().getWidth());
        delayedRecyclerView.setAdapter(adaptor);
        pager.setAdapter(new PlayerFragmentAdapter(getSupportFragmentManager()));
        pager.setClipToPadding(false);
        // set padding manually, the more you set the padding the more you see of prev & next page
        pager.setPadding(60, 0, 60, 0);
        // sets a margin b/w individual pages to ensure that there is a gap b/w them
        pager.setPageMargin(20);

        snapHelperStart=  new StartSnapHelper();
        snapHelperStart.attachToRecyclerView(delayedRecyclerView);


        delayedRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==RecyclerView.SCROLL_STATE_DRAGGING)
                {
                    adaptor.setCurrentActivrPosition(lastPosition);
                    adaptor.notifyDataSetChanged();
                }


                if(newState == RecyclerView.SCROLL_STATE_IDLE) {

                    View centerView = snapHelperStart.findSnapView(layoutManagerStart);
                    if(centerView==null)
                    {
                        if(lastPosition!=(Constants.playerName.length-1)) {
                            lastPosition = Constants.playerName.length - 1;
                            mScrollEanbled=true;


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

                            mScrollEanbled=true;

                            pager.setCurrentItem(lastPosition);

                        }
                        else
                        {

                            pager.setCurrentItem(lastPosition);

                        }
                    }
                    adaptor.setCurrentActivrPosition(lastPosition);
                    adaptor.notifyDataSetChanged();
//                    mPager.setPagingEnabled(true);

                }
            }
        });

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (!mScrollEanbled) {

                    scrollToTab(position, positionOffset);
                }


            }

            @Override
            public void onPageSelected(final int position) {


            }

            @Override
            public void onPageScrollStateChanged(int state) {

                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    mScrollEanbled=false;
                    isScrolling=false;





                }
                else if(state==ViewPager.SCROLL_STATE_DRAGGING)
                {
                    isScrolling=true;
                    mScrollEanbled=false;


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

        // if(position dummy.size())


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
                //mRequestScrollToTab = true;
            }

            if (position != 0) {
                // scrollOffset=scrollOffset-10;
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
