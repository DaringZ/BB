package com.devster.bloodybank.Helpers.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devster.bloodybank.Helpers.Interfaces.OnItemClickListener;
import com.devster.bloodybank.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by MOD on 6/29/2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{


    private ArrayList<String> searchItems=new ArrayList<>();
    private Context mContext;
    OnItemClickListener mlistener;

    public RecyclerViewAdapter(Context context,ArrayList<String> items){
        searchItems=items;
        this.mContext=context;

    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.mlistener=listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {



        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_seach_item_recyclerv,parent,false);
        return new ViewHolder(v,mlistener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(searchItems.get(position));

    }

    @Override
    public int getItemCount() {
        return searchItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView image;
        TextView tv_bloodType;
        final String TAG=ViewHolder.class.getSimpleName();

        public ViewHolder(View itemView,final OnItemClickListener listener){
            super(itemView);
            image=itemView.findViewById(R.id.img_card_circular);
            tv_bloodType=itemView.findViewById(R.id.tv_bloodtype);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        Log.d(TAG,"Item Clicked inside Adapter");
                        int position=getAdapterPosition();
                        listener.onItemClick(position);
                    }

                }
            });

        }
        public void bind(final String item){
            tv_bloodType.setText(item);
            Log.d(TAG,"Item bind inside Adapter");

        }

    }
}
