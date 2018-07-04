package com.devster.bloodybank.Views.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devster.bloodybank.Helpers.Adapters.RecyclerVACardSlider;
import com.devster.bloodybank.Helpers.Interfaces.CallBackMainTo;
import com.devster.bloodybank.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AppGuideFragment extends Fragment {

    private final String TAG =AppGuideFragment.class.getSimpleName();
    private CallBackMainTo callBackMainTo;
    private View view;
    private Context context;
    private RecyclerView rv_slider;


    public AppGuideFragment() {
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
        view=inflater.inflate(R.layout.fragment_app_guide, container, false);
        callBackMainTo=(CallBackMainTo)context;
        init();
        return view;
    }

    private void init() {
        rv_slider=view.findViewById(R.id.rv_cardSlider);
        LinearLayoutManager LL=new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false);
        rv_slider.setLayoutManager(LL);
        rv_slider.setAdapter(new RecyclerVACardSlider());
    }


}
