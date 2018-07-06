package com.devster.bloodybank.Helpers.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.devster.bloodybank.R;

import java.util.List;

/**
 * Created by MOD on 7/1/2018.
 */

public  class  SearchResultsViewHolder extends RecyclerView.ViewHolder{


    private  TextView s_type,s_name,s_age,s_city;
    public ImageButton req_btn;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private List<String> dataSet;


    public SearchResultsViewHolder(View itemView){
        super(itemView);
        viewBinderHelper.setOpenOnlyOne(true);
        s_type=itemView.findViewById(R.id.tv_searchResult_blood);
        s_name=itemView.findViewById(R.id.tv_searchResult_name);
        s_age=itemView.findViewById(R.id.tv_searchResult_age);
        s_city=itemView.findViewById(R.id.tv_searchResult_city);
        req_btn=itemView.findViewById(R.id.req_button);


    }
    public void bind(final String name,final String bloodType,final int age,final String city){
        this.dataSet=dataSet;
        s_type.setText(bloodType);
        s_name.setText(name);
        s_age.setText(String.valueOf(age));
        s_city.setText(city);
    }

}
