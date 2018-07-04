package com.devster.bloodybank.Helpers.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.devster.bloodybank.R;

/**
 * Created by MOD on 7/1/2018.
 */

public class SearchResultsViewHolder extends RecyclerView.ViewHolder{


    private final TextView s_type,s_name,s_age,s_city;


    public SearchResultsViewHolder(View itemView){
        super(itemView);
        s_type=itemView.findViewById(R.id.tv_searchResult_blood);
        s_name=itemView.findViewById(R.id.tv_searchResult_name);
        s_age=itemView.findViewById(R.id.tv_searchResult_age);
        s_city=itemView.findViewById(R.id.tv_searchResult_city);


    }
    public void bind(final String name,final String bloodType,final int age,final String city){
        s_type.setText(bloodType);
        s_name.setText(name);
        s_age.setText(String.valueOf(age));
        s_city.setText(city);
    }

}
