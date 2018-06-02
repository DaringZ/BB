package com.devster.bloodybank;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.devster.bloodybank.Database.FirebaseConn;
import com.devster.bloodybank.Fragments.SignUpPhases.ScanDonors.scanDonorsFragment;
import com.devster.bloodybank.Registeration.Registration;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout mDrawer;
    private FloatingActionButton fab;
    private ActionBarDrawerToggle mToggle;
    private FirebaseConn firebaseConn;
    private View view;
    private TextView userName,userNumber;


   @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
       getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

       setContentView(R.layout.activity_main);
       getSupportActionBar().show();

       fab=findViewById(R.id.fab);

       fab.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                       .setAction("Action", null).show();
           }
       });

        mDrawer=(DrawerLayout) findViewById(R.id.drawer);
        mToggle=new ActionBarDrawerToggle(this,mDrawer,R.string.open,R.string.close);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
       navigationView.setNavigationItemSelectedListener(this);

       view=navigationView.getHeaderView(0);
       userName=view.findViewById(R.id.tv_name);
       userNumber=view.findViewById(R.id.tv_phnno);

    }

    @Override
    protected void onResume() {
        super.onResume();
        scanDonorsFragment myScanDonorFragment=new scanDonorsFragment();
        FragmentManager manager=getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.map_container,myScanDonorFragment).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseConn=new FirebaseConn(this);
        if(firebaseConn.getCurrentUser()==null){

        }
        Toast.makeText(this,"UserId: "+firebaseConn.getUserId(),Toast.LENGTH_LONG).show();
        loadUserInfor();
    }
    private void loadUserInfor(){

        String phoneNumber = firebaseConn.getCurrentUser().getPhoneNumber();
        Toast.makeText(this,"Name: "+phoneNumber,Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

       if(mToggle.onOptionsItemSelected(item))
           return true;
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.mmHome) {
            // Handle the camera action
        } else if (id == R.id.mmreqBlood) {

        } else if (id == R.id.mmhistory) {

        } else if (id == R.id.mmSetting) {

        } else if (id == R.id.mmDonorGuide) {

        } else if (id == R.id.mmAbout) {

        }


        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        if(mDrawer.isDrawerOpen(GravityCompat.START)){
            mDrawer.closeDrawer(GravityCompat.START);
        }
        else
            super.onBackPressed();
    }
}
