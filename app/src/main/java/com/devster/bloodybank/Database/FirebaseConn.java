package com.devster.bloodybank.Database;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;


import com.devster.bloodybank.AppUser.User;
import com.devster.bloodybank.EventBuses.UpdateUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

/**
 * Created by MOD on 5/5/2018.
 */

public class FirebaseConn {

    private static final String TAG = FirebaseConn.class.getSimpleName();
    private static FirebaseAuth mAuth=FirebaseAuth.getInstance();;
    private static DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference("Users");
    private static FirebaseUser firebaseUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private final int SIGNUP_SUCCESS_CODE = 200;
    private final int SIGNUP_ALREADY_CODE = 300;
    private EventBus eventBus = EventBus.getDefault();
    private static String UserId;
    public Activity callBackactivity;
    PhoneVerify phoneVerify;

    public FirebaseConn(Activity activity) {
        this.callBackactivity=activity;

    }
    public FirebaseAuth getInstance() {
        return mAuth;
    }
    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }
    public String getUserId() {
        return mAuth.getCurrentUser().getUid();
    }
    public void setFirebaseUser(FirebaseUser user){
        this.firebaseUser = user;
        setUserID();
    }
    public void setUserID(){
        if (firebaseUser != null) {
            UserId = firebaseUser.getUid();
        }
    }
    public void verifyPhone(String number,String country){
        phoneVerify=new PhoneVerify(number,country);
        phoneVerify.startPhoneNumberVerification();
    }
    public void verifyCode(String code){
        if(phoneVerify!=null)
            phoneVerify.verifyCode(code);
    }

    public void SignUp(final User user) {
        mRootRef.child(getUserId()).setValue(user).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                            eventBus.post(new UpdateUI(SIGNUP_SUCCESS_CODE,"Register"));
                        else if(task.getException() instanceof FirebaseAuthUserCollisionException){
                            eventBus.post(new UpdateUI(SIGNUP_ALREADY_CODE,"Already Register"));
                        }
                    }
                });
    }

    public void signOut(){
        mAuth.signOut();
    }

    public  class PhoneVerify {

        private final String TAG = PhoneVerify.class.getSimpleName();
        private static final int STATE_CODE_SENT = 2;
        private static final int STATE_VERIFY_FAILED = 3;
        private static final int STATE_VERIFY_SUCCESS = 4;
        private static final int STATE_SIGNIN_FAILED = 5;
        private static final int STATE_SIGNIN_SUCCESS = 6;

        private PhoneAuthProvider.ForceResendingToken mResendToken;
        private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

        private boolean mVerificationInProgress = false;
        private String mVerificationId;
        private String phoneNumber,countrycode;
        private EventBus eventBus = EventBus.getDefault();


        public PhoneVerify(String phoneNumber,String countrycode){

            InitializePhoneAuth();
            this.phoneNumber=phoneNumber;
            this.countrycode=countrycode;
            if (mVerificationInProgress) {
                startPhoneNumberVerification();
            }
        }

        private void InitializePhoneAuth() {
            mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(PhoneAuthCredential credential) {
                    Log.d(TAG, "onVerificationCompleted:" + credential);
                    mVerificationInProgress = false;
                    eventBus.post(new UpdateUI(STATE_VERIFY_SUCCESS,"succes"));
                }
                @Override
                public void onVerificationFailed(FirebaseException e) {
                    mVerificationInProgress = false;
                    eventBus.post(new UpdateUI(STATE_VERIFY_FAILED,e.getMessage()));

                }

                @Override
                public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                    super.onCodeSent(verificationId, token);
                    Log.d(TAG, "onCodeSent:" + verificationId);
                    mVerificationId = verificationId;
                    mResendToken = token;
                    eventBus.post(new UpdateUI(STATE_CODE_SENT,verificationId));
                }
            };

        }



        public void startPhoneNumberVerification() {
            // [START start_phone_auth]
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    this.phoneNumber,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    callBackactivity,               // Activity (for callback binding)
                    mCallbacks);        // OnVerificationStateChangedCallbacks
            // [END start_phone_auth]

            mVerificationInProgress = true;

        }
        public void verifyCode(String code){
            verifyPhoneNumberWithCode(mVerificationId,code);
        }
        private void verifyPhoneNumberWithCode(String verificationId, String code) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

            signInWithPhoneAuthCredential(credential);
        }

        private void resendVerificationCode(String phoneNumber,
                                            PhoneAuthProvider.ForceResendingToken token) {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phoneNumber,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    callBackactivity,               // Activity (for callback binding)
                    mCallbacks,         // OnVerificationStateChangedCallbacks
                    token);             // ForceResendingToken from callbacks
        }

        private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

            getInstance().signInWithCredential(credential).addOnCompleteListener(callBackactivity, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = task.getResult().getUser();
                        mRootRef.child(user.getUid()).child("PhoneNumberWCode").setValue(phoneNumber);
                        mRootRef.child(user.getUid()).child("CountryCode").setValue(countrycode);
                        eventBus.post(new UpdateUI(STATE_SIGNIN_SUCCESS,"signin"));
                        // [START_EXCLUDE]
                    }
                    else {
                        eventBus.post(new UpdateUI(STATE_SIGNIN_FAILED,"fail"));
                    }
                }
            });

        }


    }

}
