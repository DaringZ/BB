package com.devster.bloodybank.Views.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devster.bloodybank.Helpers.Adapters.TabPagerAdapter;
import com.devster.bloodybank.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequestsFragment extends Fragment {


    private  final String TAG=RequestsFragment.class.getSimpleName();
    private Context context;
    private View v;
    private ViewPager viewPager;
    private TabItem req,notification;
    public RequestsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_requests, container, false);
        return v;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        TabLayout tabLayout=(TabLayout)v.findViewById(R.id.tabLayout);
        viewPager=(ViewPager) v.findViewById(R.id.vp_secondory);
        req=(TabItem) v.findViewById(R.id.tab_req);
        notification=(TabItem)v.findViewById(R.id.tab_notification);

        TabPagerAdapter adapter=new TabPagerAdapter(getActivity().getSupportFragmentManager(),2,context);
        viewPager.setAdapter(adapter);
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
