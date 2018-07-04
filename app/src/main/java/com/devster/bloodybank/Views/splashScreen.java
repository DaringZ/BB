package com.devster.bloodybank.Views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.devster.bloodybank.Database.FirebaseConn;
import com.devster.bloodybank.R;
import com.devster.bloodybank.Utils.ShimmerLibrary.Shimmer;
import com.devster.bloodybank.Utils.ShimmerLibrary.ShimmerTextView;
import com.devster.bloodybank.Views.Main.MainActivity;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class splashScreen extends AppCompatActivity {

    ShimmerTextView tv;
    Shimmer shimmer;
    private static final String TAG = FirebaseConn.class.getSimpleName();
    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        tv = (ShimmerTextView) findViewById(R.id.tv_shimmer);
        if (shimmer != null && shimmer.isAnimating()) {

            shimmer.cancel();
            startMainActivity();
        } else {
            Log.v(TAG,"zain");
            shimmer = new Shimmer();
            shimmer.setRepeatCount(3);
            shimmer.start(tv);
        }


    }
    private void startMainActivity() {
        finish();
        startActivity(new Intent(splashScreen.this,MainActivity.class));

    }
}
