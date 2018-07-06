package com.devster.bloodybank.Views.Fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.devster.bloodybank.Database.FirebaseConn;
import com.devster.bloodybank.Helpers.Adapters.RequestedVH;
import com.devster.bloodybank.Models.RequestDetails;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * Created by MOD on 7/5/2018.
 */

public class BaseFragment extends Fragment {

    final String TAG = BaseFragment.class.getSimpleName();
    Context context;

    private FirebaseRecyclerAdapter<RequestDetails,RequestedVH> mAdapter;
    private RecyclerView rv_req;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;

    }

    public LinearLayoutManager getLinearLManager(){
        LinearLayoutManager manager=new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.setReverseLayout(false);

        return manager;
    }

    public Query getQuery(DatabaseReference ref,String tabname,int number,String filter) {
        Query myRequesQ=null;
        final String uid= FirebaseConn.getInstance().getCurrentUser().getUid();
        if(tabname.equals("tab1")){
            myRequesQ=ref.child("User-requests").child(uid);
        }
        else if(tabname.equals("tab2")){
            if(number==1){
                myRequesQ=ref.child("Status").child(uid).orderByChild("isrequested");
            }
            else if(number==2){
                myRequesQ=ref.child("User-requests").child(filter).orderByChild(filter);
            }
        }

        return myRequesQ;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

