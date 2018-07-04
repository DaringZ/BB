package com.devster.bloodybank.Helpers.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devster.bloodybank.R;

/**
 * Created by MOD on 7/2/2018.
 */

public class RecyclerVANotif extends RecyclerView.Adapter<RecyclerVANotif.VHNotif> {

    private String item1,item2,item3;
    public RecyclerVANotif(String item1,String item2,String item3){
     this.item1=item1;
     this.item2=item2;
     this.item3=item3;
    }

    @NonNull
    @Override
    public VHNotif onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_notif,parent,false);
        return new VHNotif(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VHNotif holder, int position) {
        holder.bind(item1,item2,item3);
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class VHNotif extends RecyclerView.ViewHolder{
        TextView tv_notif_blood,tv_title,tv_contact;
        public VHNotif(View itemView){
            super(itemView);

            tv_notif_blood=(TextView)itemView.findViewById(R.id.tv_notif_blood);
            tv_title=(TextView)itemView.findViewById(R.id.tv_notifTitle);
            tv_contact=(TextView)itemView.findViewById(R.id.tv_notifnumber);
        }


        public void bind(String reqblood,String title,String contact){
            tv_notif_blood.setText(reqblood);
            tv_title.setText(title);
            tv_contact.setText(contact);
        }
    }
}
