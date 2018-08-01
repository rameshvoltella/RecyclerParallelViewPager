package com.ramz.parallelscroll.adaptors;

import android.content.Context;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.ramz.parallelscroll.R;
import com.ramz.parallelscroll.util.Constants;

/**
 * Created by Ramesh M Nair
 */

public class PlayerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private int mScreenWidth = 0;
    public PlayerAdapter(Context context,int mScreenWidth) {
        this.context = context;
        this.mScreenWidth = mScreenWidth;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==1) {


            View view = LayoutInflater.from(context).inflate(R.layout.recyclercell_row, parent, false);
            return new ViewHolder(view);
        }
        else
        {
            View view = LayoutInflater.from(context).inflate(R.layout.row_dummy, parent, false);
            return new ViewDummyHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {

            ViewHolder mainHolder= (ViewHolder) holder;

            if (position == getCurrentIndicatorPosition()) {
                mainHolder.playerNameTextView.setTextColor(Color.BLUE);
            }
            else
            {
                mainHolder.playerNameTextView.setTextColor(Color.BLACK);

            }

            if (mScreenWidth != 0) {
                RelativeLayout.LayoutParams params =
                        new RelativeLayout.LayoutParams((mScreenWidth / 5), dp2px(context, 80));
                mainHolder.baseCellLayout.setLayoutParams(params);

            }
            mainHolder.playerNameTextView.setText(Constants.playerName[position]);
        }
        else if (holder instanceof ViewDummyHolder)
        {
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams((mScreenWidth / 5) * 4, 0);
            ((ViewDummyHolder) holder).dummyLayer.setLayoutParams(parms);
//
        }
    }


    @Override
    public int getItemViewType(int position) {
        if(position==Constants.playerName.length)
        {
            return 0;
        }
        else
        {
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return Constants.playerName.length+1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout baseCellLayout;
        TextView playerNameTextView;
        public ViewHolder(View itemView) {
            super(itemView);
            baseCellLayout=(RelativeLayout)itemView.findViewById(R.id.base_layer);
            playerNameTextView=(TextView)itemView.findViewById(R.id.player_name_tv);
        }

    }

    public class ViewDummyHolder extends RecyclerView.ViewHolder {

      LinearLayout dummyLayer;
        public ViewDummyHolder(View itemView) {
            super(itemView);
            dummyLayer=(LinearLayout)itemView.findViewById(R.id.dummy_layer);
        }


    }
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    public void setCurrentActivrPosition(int indicatorPosition) {
        mIndicatorPosition = indicatorPosition;
    }

    public int getCurrentIndicatorPosition() {
        return mIndicatorPosition;
    }
    protected int mIndicatorPosition;
}
