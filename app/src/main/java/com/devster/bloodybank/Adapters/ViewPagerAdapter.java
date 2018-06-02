package com.devster.bloodybank.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devster.bloodybank.R;

/**
 * Created by MOD on 5/1/2018.
 */

public class ViewPagerAdapter extends PagerAdapter {

    private int[] layouts={R.layout.slider1,R.layout.slider2,R.layout.slider3,R.layout.slider4};
    private Context context;
    private LayoutInflater layoutInflater;
    private TextView[] dots;


    private String[] mtitle1,mtitle2;
    private String[] mdescription;
    private int[] imgs={R.mipmap.image1,R.mipmap.img2,R.mipmap.img3,R.mipmap.img4};
    private int[] bg={R.color.screen1,R.color.screen2,R.color.screen3,R.color.screen4};





    public ViewPagerAdapter(Context context){
        this.context=context;
        layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mtitle1=context.getResources().getStringArray(R.array.title1);
        mtitle2=context.getResources().getStringArray(R.array.title2);
        mdescription=context.getResources().getStringArray(R.array.description);



    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {


        View view=layoutInflater.inflate(layouts[position],container,false);

        ImageView img=(ImageView) view.findViewById(R.id.img_intro);
        Animation uptodown= AnimationUtils.loadAnimation(context,R.anim.uptodown);
        img.setAnimation(uptodown);

        Typeface myFont=Typeface.createFromAsset(context.getAssets(),"fonts/BLOODY.ttf");
        TextView title1=(TextView) view.findViewById(R.id.tv_title1);
        title1.setText(mtitle1[position]);
        title1.setTypeface(myFont);

        myFont=Typeface.createFromAsset(context.getAssets(),"fonts/Butler_Regular_Stencil.otf");
        TextView title2=(TextView) view.findViewById(R.id.tv_title2);
        title2.setText(mtitle2[position]);
        title2.setTypeface(myFont);

        myFont=Typeface.createFromAsset(context.getAssets(),"fonts/Roboto-Regular.ttf");
        TextView desc=(TextView) view.findViewById(R.id.tv_paragraph);
        desc.setText(mdescription[position]);
        desc.setTypeface(myFont);
        Animation righttoleft=AnimationUtils.loadAnimation(context,R.anim.righttoleft);
        desc.setAnimation(righttoleft);



        ((ViewPager)container).addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return imgs.length;
    }



    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        View view= (View) object;
        ((ViewPager)container).removeView(view);
    }

}
