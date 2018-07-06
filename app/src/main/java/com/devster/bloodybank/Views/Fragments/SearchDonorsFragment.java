package com.devster.bloodybank.Views.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.devster.bloodybank.Database.FirebaseStorage;
import com.devster.bloodybank.Helpers.Adapters.RecyclerViewAdapter;
import com.devster.bloodybank.Helpers.EventBuses.UpdateUI;
import com.devster.bloodybank.Helpers.Interfaces.CallBackMainTo;
import com.devster.bloodybank.Helpers.Interfaces.OnItemClickListener;
import com.devster.bloodybank.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchDonorsFragment extends Fragment {

    final String TAG = SearchDonorsFragment.class.getSimpleName();
    CallBackMainTo callBackMainTo;
    private View view;
    private Context context;
    private final ArrayList<String> textItems = new ArrayList<>();
    private String toSearch = "";
    private EditText et_searchField;
    private TextView lbl;
    private RecyclerView rv, rv_search_result;
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;


    public SearchDonorsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        EventBus.getDefault().register(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search_donors, container, false);
        callBackMainTo = (CallBackMainTo) context;

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        setupRecycler1();
    }

    private void init() {

        lbl=view.findViewById(R.id.lblnoResults);
        toolbar = view.findViewById(R.id.mToolbar);
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar);
        et_searchField = view.findViewById(R.id.et_searchField);
        rv = view.findViewById(R.id.rv_search_list);
        rv_search_result = view.findViewById(R.id.rv_searchR);
        rv_search_result = view.findViewById(R.id.rv_searchR);
        rv_search_result.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false));
        setupRecycler1();


    }


    private void setupRecycler1() {
        addSearchItems();
        LinearLayoutManager LL = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        rv.setLayoutManager(LL);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(context, textItems);
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                toSearch = textItems.get(position);
                Log.d(TAG, "toSearch Item Clicked: " + toSearch);
                if (callBackMainTo.isNetworkAvailable()) {
                    callBackMainTo.loadToasty("Searching");
                    Log.d(TAG, "toSearch Item is searched: " + toSearch);
                    displaySearchResults();
                }
            }
        });

    }
    private void displaySearchResults(){
        FirebaseStorage.getInstance().searchUser(rv_search_result, toSearch);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnEvent(UpdateUI state) {
        final int SEARCH_SUCCESS_CODE = 5555;
        final int SEARCH_NOFOUND_CODE = -5555;
        switch (state.getState()) {
            case SEARCH_SUCCESS_CODE:
                callBackMainTo.stopToasty(SEARCH_SUCCESS_CODE,"");
                break;
            case SEARCH_NOFOUND_CODE:
                callBackMainTo.stopToasty(SEARCH_NOFOUND_CODE,"No Results");
                break;
        }
    }
    private void addSearchItems() {
        String[] bloodGroups = getResources().getStringArray(R.array.bloodType);
        for (int i = 0; i < bloodGroups.length; i++) {
            textItems.add(0, bloodGroups[i]);
        }

    }
    @Override
    public void onDetach() {
        EventBus.getDefault().unregister(this);
        super.onDetach();
    }
}
