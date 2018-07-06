package com.devster.bloodybank.Views.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devster.bloodybank.Database.FirebaseConn;
import com.devster.bloodybank.Helpers.Adapters.RequestedVH;
import com.devster.bloodybank.Models.RequestDetails;
import com.devster.bloodybank.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabRequestFragment extends BaseFragment {

    final String TAG="tab1";
    private FirebaseRecyclerAdapter<RequestDetails,RequestedVH> mAdapter;
    private RecyclerView rv_req;
    View v;

    public TabRequestFragment() {}



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_tab_request, container, false);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rv_req=v.findViewById(R.id.rv_req);
        rv_req.setLayoutManager(getLinearLManager());
        rv_req=v.findViewById(R.id.rv_req);
        setAdapter();

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseConn.getInstance().getmRootRef().child("User-requests").keepSynced(true);
        if(mAdapter!=null){
            mAdapter.startListening();}

    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseConn.getInstance().getmRootRef().child("User-requests").keepSynced(true);
    }

    @Override
    public void onStop() {

        super.onStop();
        if(mAdapter!=null)
            mAdapter.stopListening();
    }

    private void setAdapter() {

        Query reQ=getQuery(FirebaseConn.getInstance().getmRootRef(),TAG,-1,"");
        final FirebaseRecyclerOptions options=new FirebaseRecyclerOptions.Builder<RequestDetails>()
                .setQuery(reQ,RequestDetails.class)
                .build();
        mAdapter=new FirebaseRecyclerAdapter<RequestDetails, RequestedVH>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RequestedVH holder, int position, @NonNull RequestDetails model) {
                holder.bind(model);
            }

            @NonNull
            @Override
            public RequestedVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_request,parent,false);
                return new RequestedVH(v);
            }


            @Override
            public int getItemCount() {
                return super.getItemCount();
            }
        };

        rv_req.setAdapter(mAdapter);
    }
    @Override
    public LinearLayoutManager getLinearLManager() {
        LinearLayoutManager manager=new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.setReverseLayout(false);

        return manager;
    }

}
