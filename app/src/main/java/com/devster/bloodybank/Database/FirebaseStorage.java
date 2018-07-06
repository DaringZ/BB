package com.devster.bloodybank.Database;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.devster.bloodybank.Helpers.Adapters.SearchResultsViewHolder;
import com.devster.bloodybank.Helpers.EventBuses.UpdateUI;
import com.devster.bloodybank.Models.RequestDetails;
import com.devster.bloodybank.Models.Status;
import com.devster.bloodybank.Models.UserDetails;
import com.devster.bloodybank.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by MOD on 7/1/2018.
 */

public class FirebaseStorage {
    private static final String TAG = FirebaseConn.class.getSimpleName();
    private static FirebaseStorage mInstance;
    private FirebaseConn conn;
    private Activity callingActivity;

    FirebaseRecyclerOptions<UserDetails> user;
    FirebaseRecyclerAdapter<UserDetails, SearchResultsViewHolder> adapter;


    private FirebaseStorage() {
    }

    public static FirebaseStorage getInstance() {
        if (mInstance == null)
            mInstance = new FirebaseStorage();

        return mInstance;
    }

    public void Initialize(Activity activity, FirebaseConn instance) {
        this.callingActivity = activity;
        conn = instance;
    }

    public void searchUser(final RecyclerView rv, final String toSearch) {
        Log.d(TAG, "In SearchUser Adapter");
        final Query query = conn.getmRootRef().child("Users").orderByChild("bloodType").startAt(toSearch).endAt(toSearch + "\uf8ff");
        query.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists()){
                            final int SEARCH_NOFOUND_CODE = -5555;
                            EventBus.getDefault().post(new UpdateUI(SEARCH_NOFOUND_CODE, "SearchComplete-> No Results"));
                            if(adapter!=null)
                                adapter.stopListening();
                        }else{
                            UserDetails result=dataSnapshot.getValue(UserDetails.class);
                            if(result==null) {
                                Log.d(TAG, "Unexpected No Results");
                            }
                            else {
                                setAdapter(query, rv);
                            }
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }


        );







    }


    private void setAdapter(final Query q, final RecyclerView rv) {
        user = new FirebaseRecyclerOptions.Builder<UserDetails>()
                .setQuery(q, UserDetails.class)
                .build();
        conn.getmRootRef().keepSynced(true);

        adapter = new FirebaseRecyclerAdapter<UserDetails, SearchResultsViewHolder>(user) {
            @NonNull
            @Override
            public SearchResultsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                Log.d(TAG, "OnCreateViewHolder -> SarchUser");
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_search_results, parent, false);
                return new SearchResultsViewHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull SearchResultsViewHolder holder, int position, @NonNull final UserDetails model) {
                Log.d(TAG, "OBindViewHolder -> SarchUser");
                holder.bind(model.getName(), model.getBloodType(), model.getAge(),model.getCity()+", "+model.getCountry());
                SearchResultsViewHolder s=(SearchResultsViewHolder)holder;
                s.req_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(callingActivity,"Requested",Toast.LENGTH_LONG).show();
                        final String senderId=conn.getCurrentUser().getUid();
                        final String requestedToID=model.getId();
                        RequestDetails details=new RequestDetails(senderId,model.getName(),model.getBloodType(),"Sheikh Zaid",model.getAge());
                        Status status=new Status(senderId,true);
                        sendRequest(senderId,requestedToID,details,status);

                    }
                });

            }

            @Override
            public void onDataChanged() {
                final int SEARCH_SUCCESS_CODE = 5555;
                Log.d(TAG, "SearchResultsChanged");
                EventBus.getDefault().post(new UpdateUI(SEARCH_SUCCESS_CODE, "Search Complete"));

            }
            @Override
            public void onError(DatabaseError e) {

            }
        };

        rv.setAdapter(adapter);
        adapter.startListening();
    }

    private void sendRequest(final String userID,final String toID,final RequestDetails req,final Status status){

        final String key1=conn.getmRootRef().child("Requests").push().getKey();
        final String key2=conn.getmRootRef().child("Status").push().getKey();
        final Map<String, Object> childupdate = new HashMap<>();




        if(key1!=null && key2!=null) {

            //if(conn.getmRootRef().child("Status").child(toID).child(key2)
            conn.getmRootRef().child("Status").child(toID).orderByChild(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        Toast.makeText(callingActivity,"Already Requested",Toast.LENGTH_LONG).show();
                    }
                    else{
                        childupdate.put("/Requests/"+key1,req);
                        childupdate.put("/User-requests/"+userID+"/"+key1,req);
                        childupdate.put("/Status/"+toID+"/"+key2,status);


                        conn.getmRootRef().updateChildren(childupdate);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


//
        }
        else{
            Log.d("TAG","Send Request failed");
        }
        Log.d("TAG","RequestValues at Request/"+key1);

    }
}
