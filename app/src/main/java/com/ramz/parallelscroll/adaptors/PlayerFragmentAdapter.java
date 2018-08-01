package com.ramz.parallelscroll.adaptors;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.ramz.parallelscroll.PlayerFragment;
import com.ramz.parallelscroll.util.Constants;
/**
 * Created by ramesh on 1/8/18.
 *
 * @auther Ramesh M Nair
 */

public class PlayerFragmentAdapter extends FragmentStatePagerAdapter {

    public PlayerFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return PlayerFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return  Constants.playerImage.length;
    }



}
