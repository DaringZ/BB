package com.devster.bloodybank.Helpers.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.devster.bloodybank.R;

/**
 * Created by MOD on 7/1/2018.
 */

public class RecyclerVACardSlider extends RecyclerView.Adapter<RecyclerVACardSlider.VHCardSlider> {


    private int[] imgs={R.drawable.a_neg,R.drawable.a_pos,R.drawable.b_neg,R.drawable.b_pos,R.drawable.o_neg,R.drawable.o_pos,R.drawable.ab_neg,R.drawable.ab_pos};
    public RecyclerVACardSlider(){

    }

    @NonNull
    @Override
    public RecyclerVACardSlider.VHCardSlider onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_blood_forwho_slider,parent,false);
        return new VHCardSlider(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VHCardSlider holder, int position) {
        holder.bind(imgs[position]);
    }

    @Override
    public int getItemCount() {
        return imgs.length;
    }

    public class VHCardSlider extends RecyclerView.ViewHolder{

        ImageView imgV;
        public VHCardSlider(View itemView){
            super(itemView);
            imgV=itemView.findViewById(R.id.img_cardSlider);
        }
        public void bind(int img){
            imgV.setImageResource(img);
        }


    }
}
