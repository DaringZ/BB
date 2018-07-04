package com.devster.bloodybank.Helpers.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devster.bloodybank.R;

/**
 * Created by MOD on 5/2/2018.
 */

public class SpinnerAdapter extends BaseAdapter {
    Context context;
    private String[] spinnerItems;
    private LayoutInflater layoutInflater;

    public SpinnerAdapter(Context context,String[] spinnerItems) {
        this.context=context;
        this.spinnerItems = spinnerItems;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return spinnerItems.length;
    }

    @Override
    public Object getItem(int position) {
        return spinnerItems[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=convertView;

        if(convertView==null){
            view=layoutInflater.inflate(R.layout.spinner_item,null);
        }

        TextView item=(TextView) view.findViewById(R.id.tv_spinnerItem);

        item.setText(spinnerItems[position].toString());

        return view;
    }

    @Override
    public boolean isEnabled(int position) {
        if(position==0){
            return false;
        }else

        return super.isEnabled(position);
    }



    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view=super.getDropDownView(position, convertView, parent);
        LinearLayout lay=(LinearLayout)view;
        TextView tv_item=lay.findViewById(R.id.tv_spinnerItem);
        tv_item.setTypeface(null, Typeface.BOLD);
        if(position==0){
            tv_item.setEnabled(false);
            tv_item.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            tv_item.setTypeface(null, Typeface.BOLD);
        }
        return view;

    }


}
