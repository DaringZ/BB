package com.devster.bloodybank.Views.Main;

import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.devster.bloodybank.Database.FirebaseConn;
import com.devster.bloodybank.Database.FirebaseStorage;
import com.devster.bloodybank.Helpers.Interfaces.CallBackMainTo;
import com.devster.bloodybank.R;
import com.devster.bloodybank.Views.Fragments.AppGuideFragment;
import com.devster.bloodybank.Views.Fragments.HistoryFragment;
import com.devster.bloodybank.Views.Fragments.HomeFragment;
import com.devster.bloodybank.Views.Fragments.MyRequestsFragment;
import com.devster.bloodybank.Views.Fragments.SearchDonorsFragment;
import com.devster.bloodybank.Views.Fragments.scanDonorsFragment;
import com.google.firebase.iid.FirebaseInstanceId;
import com.tapadoo.alerter.Alerter;

import net.steamcrafted.loadtoast.LoadToast;

import java.util.ArrayList;

import devlight.io.library.ntb.NavigationTabBar;


public class MainActivity extends AppCompatActivity implements CallBackMainTo {
    private static final String TAG = MainActivity.class.getSimpleName();

    private LoadToast mytoast;
    public boolean isNetworkAvailable(){
        ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            Log.d(TAG,"Network Connected");
            return true;
        } else {
            Log.d(TAG,"No Network");
            showAlerter();
            return false;}

    }

    public void loadToasty(String text){
        mytoast=new LoadToast(this)
                .setText(text)
                .setProgressColor(Color.GREEN)
                .setTranslationY(1500)
                .setBorderColor(Color.WHITE);
        mytoast.show();
    }

    @Override
    public void stopToasty(int stateCode,String txt) {
        if(stateCode==5555)
            mytoast.success();
        else if(stateCode==-5555) {
            //mytoast.hide();
            mytoast=new LoadToast(this)
                    .setText(txt)
                    .setTranslationY(1500)
                    .setTextColor(getResources().getColor(R.color.colorPrimaryDark))
                    .setBorderColor(Color.GREEN).show();
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            mytoast.hide();
                        }
                    }, 1100);

        }
    }

    private FirebaseConn firebaseConn;
    private FirebaseStorage storage;
    private ViewPager mPager;
    private FragmentPagerAdapter mAdapter;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
       getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
       setContentView(R.layout.activity_main);
       Log.d(TAG,"Created ");
       init();

       Log.d(TAG,"curent Token: "+ FirebaseInstanceId.getInstance().getToken());
    }

    public void init(){
       firebaseConn=FirebaseConn.getInstance();
       firebaseConn.Initialize(this);
       storage=FirebaseStorage.getInstance();
       storage.Initialize(this,firebaseConn);

       mPager = (ViewPager) findViewById(R.id.vp_veritcal_ntb);
        mAdapter=new FragmentPagerAdapter(getSupportFragmentManager()){
           private final Fragment[] mFragments=new Fragment[]{
                   new HomeFragment(),
                   new scanDonorsFragment(),
                   new SearchDonorsFragment(),
                   new MyRequestsFragment(),
                   new HistoryFragment(),new AppGuideFragment(),

           };

           @Override
           public Fragment getItem(int position) {
               return mFragments[position];
           }

           @Override
           public int getCount() {
               return mFragments.length;
           }
       };

        mPager.setAdapter(mAdapter);

        final String[] colors = getResources().getStringArray(R.array.ntb_color);

        final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_vertical);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.mipmap.home),
                        Color.parseColor(colors[0]))
                        .title("Home")
                        .selectedIcon(getResources().getDrawable(R.mipmap.home))
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.mipmap.img2),
                        Color.parseColor(colors[1]))
                        .selectedIcon(getResources().getDrawable(R.mipmap.img2))
                        .title("ic_sixth")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.places_ic_search),
                        Color.parseColor(colors[2]))
                        .selectedIcon(getResources().getDrawable(R.drawable.places_ic_search))
                        .title("Scan")
                        .build()
        );
        models.add(

                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.mipmap.request),
                        Color.parseColor(colors[3]))
                        .selectedIcon(getResources().getDrawable(R.mipmap.request))
                        .title("Requests")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.mipmap.history),
                        Color.parseColor(colors[4]))
                        .selectedIcon(getResources().getDrawable(R.mipmap.history))
                        .title("History")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.mipmap.about),
                        Color.parseColor(colors[5]))
                        .selectedIcon(getResources().getDrawable(R.mipmap.about))
                        .title("About/Guide")
                        .build()
        );


        navigationTabBar.setModels(models);
        navigationTabBar.setBadgeBgColor(R.color.colorPrimary);
        navigationTabBar.setViewPager(mPager, 0);


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseConn.getCurrentUser()==null){}

    }



    private void showAlerter() {
        Alerter.create(this)
                .setText("No Internet Connectivity")
                .setBackgroundColorRes(R.color.color_error)
                .setDuration(1500)
                .setIcon(R.mipmap.wifi_alert)
                .show();
    }


    @Override
    public void onBackPressed() {
            super.onBackPressed();
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();
    }


}


