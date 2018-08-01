package com.ramz.parallelscroll;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ramz.parallelscroll.util.Constants;
import com.ramz.parallelscroll.util.GlideApp;

/**
 * Created by Ramesh on 1/8/18.
 *
 * @auther Ramesh M Nair
 */
public class PlayerFragment extends Fragment {

    ImageView playerImageView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_player_view,container,false);
        playerImageView=(ImageView)v.findViewById(R.id.playerImage);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        GlideApp
                .with(this)
                .load(Constants.playerImage[getArguments().getInt("position")])
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(playerImageView);
    }

    public static PlayerFragment newInstance(int position)
    {
        PlayerFragment playerFragment=new PlayerFragment();
        Bundle b=new Bundle();
        b.putInt("position",position);
        playerFragment.setArguments(b);
        return playerFragment;

    }
}
