package com.devster.bloodybank.Database;

import android.app.Activity;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devster.bloodybank.Helpers.Adapters.SearchResultsViewHolder;
import com.devster.bloodybank.Helpers.EventBuses.UpdateUI;
import com.devster.bloodybank.Helpers.Interfaces.SwipeControllerActions;
import com.devster.bloodybank.Models.RequestDetails;
import com.devster.bloodybank.Models.UserDetails;
import com.devster.bloodybank.R;
import com.devster.bloodybank.Utils.SwipeController;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;

import org.greenrobot.eventbus.EventBus;


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

    public void searchUser(RecyclerView rv, String toSearch) {
        Log.d(TAG, "In SearchUser Adapter");
        Query query = conn.getmRootRef().child("Users").orderByChild("bloodType").startAt(toSearch).endAt(toSearch + "\uf8ff");
        setAdapter(query);
        serRecyler(rv);

    }

    private void serRecyler(final RecyclerView rv) {
        rv.setAdapter(adapter);
        final SwipeController swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                super.onRightClicked(position);
            }
        });
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(rv);
        rv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);

            }
        });
    }


    private void setAdapter(final Query q) {
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

        adapter.startListening();
    }

    private void sendRequest(final String userID,final RequestDetails details){


        String key1=conn.getmRootRef().child("Requests").push().getKey();
        if(key1!=null) {
            conn.getmRootRef().child("Requests").child(key1).setValue(details);
            conn.getmRootRef().child("User-requests").child(userID).setValue(details);
        }
        else{
            Log.d("TAG","Send Request failed");
        }
        Log.d("TAG","RequestValues at Request/"+key1);

    }
}
