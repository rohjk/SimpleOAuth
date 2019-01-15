package com.jk.simpleplatform.idp;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.jk.simpleplatform.SimpleAuthResult;
import com.jk.simpleplatform.SimpleAuthResultCallback;

public class GoogleAuth extends AuthClient {
    public static int GOOGLE_LOGIN_REQUEST = 12121;

    GoogleSignInClient googleSignInClient;
    SimpleAuthResultCallback<Void> loginCallback;
    @Override
    public void start(@NonNull Activity activity,  @NonNull final SimpleAuthResultCallback<Void> callback) {
        SimpleAuthResult<Void> startResult = null;
        if(!isSettedServerId){
            startResult = SimpleAuthResult.getFailResult(-100,"SERVER_ID_NULL");
            callback.onResult(startResult);
        }else{
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .requestServerAuthCode(getServerId())
                    .build();

            googleSignInClient = GoogleSignIn.getClient(activity, gso);

            if(googleSignInClient == null){
                startResult = SimpleAuthResult.getFailResult(-100,"FAIL_TO_INIT_GOOGLE_CLIENT");
                callback.onResult(startResult);
            }else{
                GoogleSignInAccount lastAccount = GoogleSignIn.getLastSignedInAccount(activity);

                if(lastAccount!=null){
                    googleSignInClient.silentSignIn().addOnCompleteListener(new OnCompleteListener<GoogleSignInAccount>() {
                        @Override
                        public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                            callback.onResult(SimpleAuthResult.<Void>getSuccessResult(null));
                        }
                    });
                }else{
                    startResult = SimpleAuthResult.getSuccessResult(null);
                    callback.onResult(startResult);
                }
            }
        }
    }

    @Override
    public void login(@NonNull Activity activity, @NonNull final SimpleAuthResultCallback<Void> callback) {

        loginCallback = callback;

        Intent googleLoginIntent = googleSignInClient.getSignInIntent();
        activity.startActivityForResult(googleLoginIntent,GOOGLE_LOGIN_REQUEST);
    }

    @Override
    public String getToken() {
        return null;
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public boolean isSignedIn(@NonNull Activity activity) {
        return GoogleSignIn.getLastSignedInAccount(activity) != null;
    }

    @Override
    protected void onActivityResult(Intent data) {
        if(loginCallback!=null){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if(result.isSuccess()){
            loginCallback.onResult(SimpleAuthResult.<Void>getSuccessResult(null));
        }else{
            int errorComde = result.getStatus().getStatusCode();
            String errorMessage = result.getStatus().getStatusMessage();

            loginCallback.onResult(SimpleAuthResult.<Void>getFailResult(errorComde,errorMessage));
        }
    }
}
