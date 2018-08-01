package com.ramz.parallelscroll;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;

import com.ramz.parallelscroll.adaptors.PlayerAdapter;
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

    LinearLayoutManager layoutManagerStart;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        delayedRecyclerView=(DelayedRecyclerView)findViewById(R.id.delayedRecyclerView);
        pager=(ViewPager)findViewById(R.id.pager);

        layoutManagerStart=new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        delayedRecyclerView.setLayoutManager(layoutManagerStart);

        adaptor= new PlayerAdapter(getApplicationContext(),getWindow().getWindowManager().getDefaultDisplay().getWidth());
        delayedRecyclerView.setAdapter(adaptor);

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


//                            mPager.setCurrentItem(lastPosition);

                        }
                        else
                        {

//                            mPager.setCurrentItem(lastPosition);
                        }
                    }
                    else
                    {

                        int pos = layoutManagerStart.getPosition(centerView);
                        if(lastPosition!=pos) {
                            lastPosition = pos;

                            mScrollEanbled=true;

//                            mPager.setCurrentItem(lastPosition);

                        }
                        else
                        {

//                            mPager.setCurrentItem(lastPosition);

                        }
                    }
                    adaptor.setCurrentActivrPosition(lastPosition);
                    adaptor.notifyDataSetChanged();
//                    mPager.setPagingEnabled(true);

                }
            }
        });

    }
}
