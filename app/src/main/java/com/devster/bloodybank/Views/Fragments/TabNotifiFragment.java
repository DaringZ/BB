package com.devster.bloodybank.Views.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devster.bloodybank.Helpers.Adapters.RecyclerVANotif;
import com.devster.bloodybank.Helpers.EventBuses.AddNotification;
import com.devster.bloodybank.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabNotifiFragment extends Fragment {

    private final String TAG=TabNotifiFragment.class.getSimpleName();
    Context context;
    View v;
    RecyclerView rv_notif;
    TextView tv_hint_notif;

    public TabNotifiFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_tab_notifi, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {

        rv_notif=(RecyclerView)v.findViewById(R.id.rv_notif);
        tv_hint_notif=(TextView) v.findViewById(R.id.tv_hint_notif);
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AddNotification details){
        if(details.isRecieve()){
            rv_notif.setLayoutManager(new LinearLayoutManager(context));
            RecyclerVANotif adapter=new RecyclerVANotif(details.getReqBlood(),details.getTitle(),details.getContact());
            rv_notif.setAdapter(adapter);
        }
    }

    @Override
    public void onDetach() {
        EventBus.getDefault().unregister(this);
        super.onDetach();
    }
}
