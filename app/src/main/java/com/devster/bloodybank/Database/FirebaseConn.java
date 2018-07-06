package com.devster.bloodybank.Database;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.devster.bloodybank.Helpers.EventBuses.UpdateUI;
import com.devster.bloodybank.Models.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

/**
 * Created by MOD on 5/5/2018.
 */

public class FirebaseConn{

    private static final String TAG = FirebaseConn.class.getSimpleName();
    private static FirebaseConn mInstance;
    private final static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final  DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private static FirebaseUser mfirebaseUser;
    private static String mUserId;
    private final EventBus eventBus = EventBus.getDefault();

    private Activity callingActivityIs;
    private PhoneVerify phoneVerify;
    private final SharedPreference details = SharedPreference.getInstance();
    private final int SIGNUP_SUCCESS_CODE = 200;
    private final int SIGNIP_SUCCESS_CODE = 1000;
    private final int SIGNIP_Faluir_CODE=-1000;
    private final int SIGNUP_ALREADY_EMAIL_CODE = 300;
    private final int STATE_NETWORK_LOSS=-88;

    private FirebaseConn() {}

    public static FirebaseConn getInstance() {
        if (mInstance == null)
            mInstance = new FirebaseConn();
        return mInstance;
    }

    public void Initialize(Activity activity) {
        this.callingActivityIs = activity;
    }

    public DatabaseReference getmRootRef() {
        return mRootRef;
    }

    public FirebaseUser getCurrentUser() {
        if (mAuth.getCurrentUser() != null)
            return mAuth.getCurrentUser();

        return null;
    }

    private void setFirebaseUser(FirebaseUser user) {
        mfirebaseUser = user;
    }

    public String getUserId() {
        if(mAuth.getCurrentUser()!=null)
            return mAuth.getCurrentUser().getUid();
        else return null;
    }

    private void setUserID(String uid) {
        mUserId = uid;
    }

    public void verifyPhone(String number) {
        phoneVerify = new PhoneVerify(number);
        phoneVerify.startPhoneNumberVerification();
    }

    public void verifyCode(String code) {
        if (phoneVerify != null)
            phoneVerify.verifyCode(code);
    }

    public void SignUp(final UserDetails user) {


        mRootRef.child("Users").orderByChild("email").equalTo(user.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    eventBus.post(new UpdateUI(SIGNUP_ALREADY_EMAIL_CODE, "Already Register"));
                } else {
                    user.setId(mUserId);
                    mRootRef.child("Users").child(getCurrentUser().getUid()).setValue(user).
                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        details.saveUserDetails(user);
                                        eventBus.post(new UpdateUI(SIGNUP_SUCCESS_CODE, "Succes Register"));

                                    } else if (task.getException() instanceof FirebaseNetworkException) {
                                        eventBus.post(new UpdateUI(STATE_NETWORK_LOSS, "Already Register"));
                                    }
                                }
                            });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void signIn(String Fullnum){
        Log.d(TAG, "SigningIn");
        mRootRef.child("Users").orderByChild("phoneNumberWCode").equalTo(Fullnum).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    eventBus.post(new UpdateUI(SIGNIP_SUCCESS_CODE, "Sign In Success"));
                    UserDetails user=dataSnapshot.getValue(UserDetails.class);
                    details.saveUserDetails(user);
                    details.setLogin(true);

                }
                else{
                    eventBus.post(new UpdateUI(SIGNIP_Faluir_CODE, "Sign In Faluire"));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("SignIn Error ",databaseError.getMessage());
            }
        });
    }
    public void signOut() {
        details.setLogin(false);
        mAuth.signOut();
    }

    public class PhoneVerify {

        private final String TAG = PhoneVerify.class.getSimpleName();
        private PhoneAuthProvider.ForceResendingToken mResendToken;
        private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

        private boolean mVerificationInProgress = false;
        private String mVerificationId;
        private String fullphoneNumber;
        private EventBus eventBus = EventBus.getDefault();

        public PhoneVerify(){}
        public PhoneVerify(String fullphoneNumber) {

            final int STATE_ALREADY_VERIFIED = -4;
            this.fullphoneNumber = fullphoneNumber;
            InitializePhoneAuth();
            mRootRef.child("Users").orderByChild("phoneNumberWCode").equalTo(fullphoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        //Phone is Already verified.
                        Log.d(TAG, "Already Verified");
                        eventBus.post(new UpdateUI(STATE_ALREADY_VERIFIED, "PhoneVerify Already"));
                    } else {
                        // New Phone Number to be verified

                        if (mVerificationInProgress) {
                            Log.d(TAG, "New Number");

                            startPhoneNumberVerification();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        private void InitializePhoneAuth() {
            final int STATE_CODE_SENT = 2;
            final int STATE_NUMBER_INVALID = 3;
            final int STATE_VERIFY_SUCCESS = 4;
            final int STATE_NETWORK_LOSS=-88;
            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(PhoneAuthCredential credential) {
                    Log.d(TAG, "onVerificationCompleted:" + credential);
                    mVerificationInProgress = false;
                    eventBus.post(new UpdateUI(STATE_VERIFY_SUCCESS, "PhoneVerify succes"));
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    mVerificationInProgress = false;
                    if (e instanceof FirebaseTooManyRequestsException) {
                        // The SMS quota for the project has been exceeded
                        // ...
                        Log.d(TAG, "SMS quota exceeded:");
                    }
                    else if(e instanceof FirebaseNetworkException){
                        Log.d(TAG, "No Network Connectivity");
                        eventBus.post(new UpdateUI(STATE_NETWORK_LOSS, "No Network Connectivity"));
                    }
                    else
                        eventBus.post(new UpdateUI(STATE_NUMBER_INVALID, "PhoneVerify Failed"));

                }

                @Override
                public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                    super.onCodeSent(verificationId, token);
                    Log.d(TAG, "CodeSent:");
                    mVerificationId = verificationId;
                    mResendToken = token;
                    eventBus.post(new UpdateUI(STATE_CODE_SENT, verificationId));
                }
            };

        }


        public void startPhoneNumberVerification() {
            // [START start_phone_auth]
            Log.d(TAG, "Number: "+fullphoneNumber);
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    this.fullphoneNumber,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    callingActivityIs,               // Activity (for callback binding)
                    mCallbacks);        // OnVerificationStateChangedCallbacks
            // [END start_phone_auth]

            mVerificationInProgress = true;

        }

        public void verifyCode(String code) {
            verifyPhoneNumberWithCode(mVerificationId, code);
        }

        public void verifyPhoneNumberWithCode(String verificationId, String code) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithPhoneAuthCredential(credential);
        }

        private void resendVerificationCode(String phoneNumber,
                                            PhoneAuthProvider.ForceResendingToken token) {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phoneNumber,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    callingActivityIs,               // Activity (for callback binding)
                    mCallbacks,         // OnVerificationStateChangedCallbacks
                    token);             // ForceResendingToken from callbacks
        }

        private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
            final int STATE_SIGNIN_FAILED = 5;
            final int STATE_SIGNIN_SUCCESS = 6;
            mAuth.signInWithCredential(credential).addOnCompleteListener(callingActivityIs, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        Log.d(TAG, "Phone signInWithCredential:success");
                        FirebaseUser fireUser = task.getResult().getUser();
                        setFirebaseUser(fireUser);
                        setUserID(fireUser.getUid());
                        details.savePhoneDetails(fullphoneNumber,fireUser.getUid());
                        eventBus.post(new UpdateUI(STATE_SIGNIN_SUCCESS, "Phone signInWithCredential:success"));
                        // [START_EXCLUDE]
                    } else {
                        eventBus.post(new UpdateUI(STATE_SIGNIN_FAILED, "Phone signInfail"));
                    }
                }
            });
        }
    }

}
