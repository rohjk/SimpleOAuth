package com.jk.simpleplatform.idp;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.jk.simpleplatform.SimpleAuthResult;
import com.jk.simpleplatform.SimpleAuthResultCallback;

public class GoogleAuth extends AuthClient {
    private static int RC_SIGN_IN = 12121;

    GoogleSignInClient googleSignInClient;
    GoogleSignInAccount googleAccount;

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
            GoogleSignInAccount lastAccount = GoogleSignIn.getLastSignedInAccount(activity);

            if(lastAccount != null){
                googleSignInClient.silentSignIn().addOnCompleteListener(new OnCompleteListener<GoogleSignInAccount>() {
                    @Override
                    public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                        if (task.isSuccessful()) {
                            // The signed in account is stored in the task's result.
                            GoogleSignInAccount signedInAccount = task.getResult();
                        } else {
                            // Player will need to sign-in explicitly using via UI
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                       callback.onResult( new SimpleAuthResult<Void>(e.hashCode(),e.getMessage()) );
                    }
                });

            }else{
                startResult = SimpleAuthResult.getFailResult(-100,"FAIL_TO_INIT_GOOGLE_CLIENT");
                callback.onResult(startResult);
            }
        }
    }

    @Override
    public void login(@NonNull Activity activity, @NonNull final SimpleAuthResultCallback<Void> callback) {

    }

    @Override
    public String getToken() {
        return null;
    }

    @Override
    public String getEmail() {
        return null;
    }
}
