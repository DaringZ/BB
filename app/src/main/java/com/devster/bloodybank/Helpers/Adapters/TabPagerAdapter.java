package com.devster.bloodybank.Helpers.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.devster.bloodybank.Views.Fragments.TabNotifiFragment;
import com.devster.bloodybank.Views.Fragments.TabRequestFragment;

/**
 * Created by MOD on 7/2/2018.
 */

public class TabPagerAdapter extends FragmentPagerAdapter {

    private  final String TAG=TabPagerAdapter.class.getSimpleName();
    private Context context;
    private int numOftabs;
    public TabPagerAdapter(FragmentManager fm,int tabscount,Context ctxt){
        super(fm);
        this.numOftabs=tabscount;
        this.context=ctxt;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                Log.d(TAG,"TabRequest Opened.");
                return new TabRequestFragment();
            case 1:
                Log.d(TAG,"TabNotification Opened.");
                return new TabNotifiFragment();
            default:
                Log.d(TAG,"Tab Null");
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOftabs;
    }

}
