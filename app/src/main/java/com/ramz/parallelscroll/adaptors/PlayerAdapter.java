package com.ramz.parallelscroll.adaptors;

import android.content.Context;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
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
import com.ramz.parallelscroll.util.ScreenUtil;

/**
 * Created by Ramesh M Nair
 */

public class PlayerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private int mScreenWidth;
    private int cellSize;

    private static int TYPE_CELL=1;
    private static int TYPE_DUMMY=0;
    public PlayerAdapter(Context context,int mScreenWidth) {
        this.context = context;
        this.mScreenWidth = mScreenWidth;
        cellSize=(int) context.getResources().getDimension(R.dimen.cell_height);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==TYPE_CELL) {


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
                mainHolder.playerNameTextView.setTextColor(ContextCompat.getColor(context,R.color.colorPrimaryDark));

            }
            else
            {
                mainHolder.playerNameTextView.setTextColor(Color.BLACK);

            }

            if (mScreenWidth != 0) {
                RelativeLayout.LayoutParams params =
                        new RelativeLayout.LayoutParams((mScreenWidth / Constants.CELL_VISIBLITY), ScreenUtil.dp2px(context, cellSize));
                mainHolder.baseCellLayout.setLayoutParams(params);

            }
            mainHolder.playerNameTextView.setText(Constants.playerName[position]);
        }
        else if (holder instanceof ViewDummyHolder)
        {
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams((mScreenWidth / Constants.CELL_VISIBLITY) * 3, 0);
            ((ViewDummyHolder) holder).dummyLayer.setLayoutParams(parms);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if(position==Constants.playerName.length)
        {
            return TYPE_DUMMY;
        }
        else
        {
            return TYPE_CELL;
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


    public void setCurrentActivrPosition(int indicatorPosition) {
        mIndicatorPosition = indicatorPosition;
    }

    public int getCurrentIndicatorPosition() {
        return mIndicatorPosition;
    }
    protected int mIndicatorPosition;
}
