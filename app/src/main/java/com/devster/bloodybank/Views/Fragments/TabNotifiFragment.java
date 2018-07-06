package com.devster.bloodybank.Views.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devster.bloodybank.Database.FirebaseConn;
import com.devster.bloodybank.Helpers.Adapters.NotifVH;
import com.devster.bloodybank.Helpers.Adapters.RVNotif;
import com.devster.bloodybank.Models.RequestDetails;
import com.devster.bloodybank.Models.Status;
import com.devster.bloodybank.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabNotifiFragment extends BaseFragment {

    private final String TAG="tab2";
    protected  static long totalNotifs=0;
    private FirebaseRecyclerAdapter<RequestDetails,NotifVH> mAdapter;
    Context context;
    View v;
    RecyclerView rv_notif;
    TextView tv_hint_notif;
    List<RequestDetails> notif=new ArrayList<RequestDetails>();

    public TabNotifiFragment() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_tab_notifi,container,false);
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
        rv_notif.setLayoutManager(getLinearLManager());
        RVNotif mAdapter=new RVNotif(getContext(),notif);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseConn.getInstance().getmRootRef().child("User-requests").keepSynced(true);
        checkIncomingnotification();
        rv_notif.setAdapter(mAdapter);
        if(mAdapter!=null){
            mAdapter.startListening();}
    }

    @Override
    public void onResume() {
        FirebaseConn.getInstance().getmRootRef().child("User-requests").keepSynced(true);
        super.onResume();
        if(mAdapter!=null){
            mAdapter.startListening();}
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mAdapter!=null){
            mAdapter.stopListening();}
    }

    private void checkIncomingnotification() {

        Log.d(TAG,"Checking");
        final DatabaseReference mref=FirebaseConn.getInstance().getmRootRef();
        final String uid= FirebaseConn.getInstance().getCurrentUser().getUid();
        mref.child("Status").child(uid).orderByChild("isrequested").equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG,"onDataChange");
                Log.d(TAG,"Total Requests-> "+dataSnapshot.getChildrenCount());
                long count=dataSnapshot.getChildrenCount();
                if(count==0){
                    setNotifStatus(false);
                    return;
                }
                setTotalNotifs(count);
                for(DataSnapshot task:dataSnapshot.getChildren()){
                    Log.d(TAG,"Found->");
                    Status status=task.getValue(Status.class);
                    if(status!=null){

                        if(status.getRequesterByID()!=null){
                            setNotifStatus(true);
                            getQuery(mref,TAG,2,status.getRequesterByID()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    RequestDetails notifDetails=dataSnapshot.getValue(RequestDetails.class);
                                    notif.add(notifDetails);
                                    mAdapter.notifyItemRangeChanged(0,notif.size());

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void setNotifStatus(boolean status) {
        if(status)
            tv_hint_notif.setVisibility(View.VISIBLE);
        else
            tv_hint_notif.setVisibility(View.GONE);
    }

    public static void setTotalNotifs(long count) {
        TabNotifiFragment.totalNotifs = totalNotifs+count;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
