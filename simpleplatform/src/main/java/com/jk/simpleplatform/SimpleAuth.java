package com.jk.simpleplatform;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.jk.simpleplatform.idp.AuthClient;
import com.jk.simpleplatform.idp.IdpType;

public class SimpleAuth {
    private static String TAG = "[SimpleAuth]";
    private Activity activity;

    private static SimpleAuthSession currentSession;

    public static void start(@NonNull Activity activity, @NonNull IdpType idpType, @NonNull final SimpleAuthResultCallback<Void> callback){
        if(activity == null){
            callback.onResult(SimpleAuthResult.<Void>getFailResult(-100,"Activity is Null"));

        }else if(idpType == null){
            callback.onResult(SimpleAuthResult.<Void>getFailResult(-101,"Idp Type is Null"));

        }else if (callback == null){
            Log.d(TAG,"Login Callback is null");

        }else{
            AuthClient authClient = AuthClient.getAuthClientInstance(idpType);

            authClient.start(activity, new SimpleAuthResultCallback<Void>() {
                @Override
                public void onResult(SimpleAuthResult<Void> result) {
                   callback.onResult(result);
                }
            });
        }
    }

}
