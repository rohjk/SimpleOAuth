package com.jk.simpleplatform.idp;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.jk.simpleplatform.SimpleAuthResult;
import com.jk.simpleplatform.SimpleAuthResultCallback;
import com.jk.simpleplatform.SimpleAuthprovider;

public class GoogleAuthClient extends AuthClient {
    private static String TAG = "[GoogleAuthClient]";
    public static int GOOGLE_LOGIN_REQUEST = 12121;

    GoogleSignInClient googleSignInClient;
    GoogleSignInAccount googleSignInAccount;

    SimpleAuthResultCallback<Void> loginCallback;

    @Override
    public void login(@NonNull final Activity activity, @NonNull final SimpleAuthResultCallback<Void> callback) {
        googleInit(activity, new SimpleAuthResultCallback<Void>() {
            @Override
            public void onResult(SimpleAuthResult<Void> googleInitResult) {
                if(googleInitResult.isSuccess()){
                    final GoogleSignInAccount lastAccount = GoogleSignIn.getLastSignedInAccount(activity);

                    if(lastAccount!=null){
                        googleSignInClient.silentSignIn().addOnCompleteListener(new OnCompleteListener<GoogleSignInAccount>() {
                            @Override
                            public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                                handleGoogleSignInAccount(task, callback);
                            }
                        });

                    }else{
                        loginCallback = callback;

                        Intent googleLoginIntent = googleSignInClient.getSignInIntent();
                        activity.startActivityForResult(googleLoginIntent,GOOGLE_LOGIN_REQUEST);
                    }
                }else{
                    callback.onResult(googleInitResult);
                }
            }
        });
    }

    private void googleInit(final Activity mActivity, SimpleAuthResultCallback<Void> callback){
        if(SimpleAuthprovider.getInstance().getServerId(IdpType.GOOGLE)==null){
            callback.onResult( SimpleAuthResult.<Void>getFailResult(-100,"SERVER_ID_NULL"));
        }else{
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .requestIdToken(SimpleAuthprovider.getInstance().getServerId(IdpType.GOOGLE))
                    .requestServerAuthCode(SimpleAuthprovider.getInstance().getServerId(IdpType.GOOGLE))
                    .build();

            googleSignInClient = GoogleSignIn.getClient(mActivity, gso);

            if(googleSignInClient == null){
                callback.onResult(SimpleAuthResult.<Void>getFailResult(-100,"FAIL_TO_INIT_GOOGLE_CLIENT"));
            }else{
                callback.onResult(SimpleAuthResult.<Void>getSuccessResult(null));
            }
        }
    }


    @Override
    public IdpType getIdpType() {
        return IdpType.GOOGLE;
    }

    @Override
    public String getToken() {
       return googleSignInAccount!=null? googleSignInAccount.getIdToken() : "";
    }

    @Override
    public String getEmail() {
        return googleSignInAccount!=null? googleSignInAccount.getEmail() : "";
    }

    @Override
    public boolean isSignedIn(@NonNull Activity activity) {
        return googleSignInAccount!=null? (GoogleSignIn.getLastSignedInAccount(activity) != null) : false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == GOOGLE_LOGIN_REQUEST){
            if(loginCallback!=null){
                handleGoogleSignInAccount(GoogleSignIn.getSignedInAccountFromIntent(data),loginCallback);
            }
        }
    }

    private void handleGoogleSignInAccount(Task<GoogleSignInAccount> task, SimpleAuthResultCallback<Void> callback) {
        if(callback==null){

        }else{
            try{
                googleSignInAccount = task.getResult(ApiException.class);
                callback.onResult(SimpleAuthResult.<Void>getSuccessResult(null));
            } catch (ApiException e){
                googleSignInAccount = null;
                int errorResponse = e.getStatusCode();
                String errorMsg = "Google Auth Error (" + e.getStatusCode() +")";
                if(e.getStatusCode() == GoogleSignInStatusCodes.SIGN_IN_CANCELLED) {
                    errorResponse = 1;
                    errorMsg = "User canceled log in.";
                }

                callback.onResult(SimpleAuthResult.<Void>getFailResult(errorResponse, errorMsg));
            }
        }
    }

}
