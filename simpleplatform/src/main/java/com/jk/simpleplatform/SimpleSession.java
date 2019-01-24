package com.jk.simpleplatform;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.jk.simpleplatform.idp.AuthClient;
import com.jk.simpleplatform.idp.IdpType;

public class SimpleSession {
    private static String TAG = "[SimpleSession]";
    private Activity activity;

    private static AuthClient currentClient;

    public SimpleSession(){
        currentClient = null;
    }

    public static void setAuthProvider(@NonNull IdpType idpType, @NonNull String serverId){
        if(idpType!=null && serverId !=null )
            SimpleAuthprovider.getInstance().addServerId(idpType,serverId);
    }

    public static void login(@NonNull Activity activity, @NonNull IdpType idpType, @NonNull final SimpleAuthResultCallback<Void> callback){
        if(activity == null){
            callback.onResult(SimpleAuthResult.<Void>getFailResult(-100,"Activity is Null"));

        }else if(idpType == null){
            callback.onResult(SimpleAuthResult.<Void>getFailResult(-101,"Idp Type is Null"));

        }else if (callback == null){
            Log.d(TAG,"Login Callback is null");

        }else{

            final AuthClient authClient = AuthClient.getAuthClientInstance(idpType);

            SimpleSession.currentClient = authClient;

            authClient.login(activity, new SimpleAuthResultCallback<Void>() {
                @Override
                public void onResult(SimpleAuthResult<Void> result) {

                    if(!result.isSuccess()){
                        SimpleSession.currentClient = null;
                    }

                   callback.onResult(result);
                }
            });
        }
    }

    //[TODO] 로그아웃 구현 해야함!
    public static void logout(){
        if(currentClient!=null){
            currentClient.logout();
        }
    }

    public static void onActivityResult(int requestCode, int resultCode, Intent data){
        if(currentClient!=null){
            currentClient.onActivityResult(requestCode,resultCode,data);
        }
    }

    public static boolean isSignedIn(@NonNull Activity activity){
        if(activity == null){
            return false;
        }else if(currentClient!=null){
            return currentClient.isSignedIn(activity);
        }
        return false;
    }

    public static IdpType getCurrentIdpType() {
        if(currentClient!=null)
            return currentClient.getIdpType();
        return null;
    }

    public static String getAccessToken(){
        if(currentClient!=null)
            return currentClient.getToken();
        return null;
    }

    public static String getEmail(){
        if(currentClient!=null)
            return currentClient.getEmail();
        return null;
    }

}
