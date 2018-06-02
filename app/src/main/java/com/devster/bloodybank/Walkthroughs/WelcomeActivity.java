package com.devster.bloodybank.Walkthroughs;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devster.bloodybank.Adapters.ViewPagerAdapter;
import com.devster.bloodybank.Database.SharedPreference;
import com.devster.bloodybank.R;
import com.devster.bloodybank.Registeration.Registration;

public class WelcomeActivity extends AppCompatActivity {

    private final int TOTAL_SLIDER=4;
    private ViewPager viewPager;
    private SharedPreference sliderManager;
    private ViewPagerAdapter viewPagerAdapter;

    private LinearLayout dots_layout;
    private TextView[] tv_dots;
    private TextView Slider_desc;
    private Button btn_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sliderManager=new SharedPreference(getApplicationContext());
        if(sliderManager.isAlreadyOpen()){
            startRegisterActivity();
        }

        setContentView(R.layout.activity_welcome);
        getSupportActionBar().hide();

        dots_layout=(LinearLayout) findViewById(R.id.LL_dots);
        btn_next=(Button) findViewById(R.id.btn_next);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int current=getItem(+1);
                if(current<TOTAL_SLIDER)
                    viewPager.setCurrentItem(current);
                else
                    startRegisterActivity();
            }
        });
        viewPager=(ViewPager) findViewById(R.id.viewPager);
        viewPagerAdapter=new ViewPagerAdapter(getApplicationContext());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {


                if(position==TOTAL_SLIDER-1){
                    btn_next.setText(getResources().getString(R.string.btn_fnish));
                    sliderManager.setRan(true);
                }
                else
                    btn_next.setText(getResources().getString(R.string.btn_next));

                addDots(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        addDots(0);

    }

    private void addDots(int page){
        dots_layout.removeAllViews();
        tv_dots=new TextView[TOTAL_SLIDER];
        int[] activeDOTS=getResources().getIntArray(R.array.dot_active);
        int[] inActiveDOTS=getResources().getIntArray(R.array.dot_Inactive);

        for (int i=0;i<tv_dots.length;i++){
            tv_dots[i]=new TextView(this);
            tv_dots[i].setText(Html.fromHtml("&#8226;"));
            tv_dots[i].setTextSize(55);
            tv_dots[i].setTextColor(inActiveDOTS[page]);
            dots_layout.addView(tv_dots[i]);
        }
        if(tv_dots.length>0)
            tv_dots[page].setTextColor(activeDOTS[page]);
    }


    private int getItem(int i){
        return viewPager.getCurrentItem()+i;
    }

    private void startRegisterActivity(){
        Intent intent=new Intent(WelcomeActivity.this, Registration.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

}
