package com.devster.bloodybank.Views.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devster.bloodybank.R;



/**
 * A simple {@link Fragment} subclass.
 */
public class MyRequestsFragment extends Fragment {


    private final String TAG = MyRequestsFragment.class.getSimpleName();
    private View v;
    private FragmentPagerAdapter mAdapter;
    private ViewPager viewPager;
    private TabItem req, notification;



    public MyRequestsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_my_requests, container, false);
        return v;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.tabLayout);
        viewPager = (ViewPager) v.findViewById(R.id.vp_secondory);
        req = (TabItem) v.findViewById(R.id.tab_req);
        notification = (TabItem) v.findViewById(R.id.tab_notification);

        mAdapter = new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {
            private Fragment[] mFragment = new Fragment[]{
                    new TabRequestFragment(),
                    new TabNotifiFragment(),
            };

            @Override
            public Fragment getItem(int position) {
                return mFragment[position];
            }

            @Override
            public int getCount() {
                return mFragment.length;
            }
        };
        viewPager.setAdapter(mAdapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }
}
