package com.devster.bloodybank.Helpers.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.devster.bloodybank.Models.RequestDetails;
import com.devster.bloodybank.R;

/**
 * Created by MOD on 7/5/2018.
 */

public class RequestedVH extends RecyclerView.ViewHolder {

    private TextView tv_bloodT,tv_name,tv_time,tv_hospital,tv_status;
    private View itemView;

    public RequestedVH(View itemView){
        super(itemView);
        this.itemView=itemView;
        tv_bloodT=itemView.findViewById(R.id.tv_req_blood);
        tv_name=itemView.findViewById(R.id.tv_donor_name);
        tv_time=itemView.findViewById(R.id.tv_sentTime);
        tv_hospital=itemView.findViewById(R.id.tv_atHopital);
        tv_status=itemView.findViewById(R.id.tv_reqStatus);

    }

    public void bind(RequestDetails req){

        tv_bloodT.setText(req.getBlood());
        tv_name.setText(req.getRequestedTo());
        tv_hospital.setText(req.getAtHospital());

//        if(req.getStatus().isAccepted()){
//            tv_status.setText(R.string.accepted);
//            tv_status.setTextColor(itemView.getContext().getResources().getColor(R.color.verify_green));
//        }
//        else{
//            tv_status.setText(R.string.pending);
//        }

    }





}
