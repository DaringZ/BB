package com.devster.bloodybank.Helpers.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.devster.bloodybank.Models.RequestDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MOD on 7/6/2018.
 */

public class NotifVH extends RecyclerView.ViewHolder {


    TextView header,requester,sentTime;
    ImageButton acpt_btn;
    List<RequestDetails> notif=new ArrayList<RequestDetails>();
    public NotifVH(View itemView,List<RequestDetails> notif) {
        super(itemView);
        this.notif=notif;
    }

    public void bind(){

    }
}
