package com.devster.bloodybank.Helpers.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.devster.bloodybank.Models.RequestDetails;
import com.devster.bloodybank.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MOD on 7/6/2018.
 */

public class RVNotif  extends RecyclerView.Adapter<RVNotif.NotifVH>{

    List<RequestDetails> notif=new ArrayList<RequestDetails>();


    public RVNotif(Context ctxt,List<RequestDetails> notif){
        this.notif=notif;
    }
    @NonNull
    @Override
    public NotifVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_tab_notifi,parent,false);
        return new NotifVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifVH holder, int position) {
        holder.bind(notif);
    }

    @Override
    public int getItemCount() {
        return notif.size();
    }

    public static class NotifVH extends RecyclerView.ViewHolder {


        TextView header,requester,sentTime;
        ImageButton acpt_btn;

        public NotifVH(View itemView) {
            super(itemView);
        }

        public void bind(List<RequestDetails> notif){

        }
    }
}
