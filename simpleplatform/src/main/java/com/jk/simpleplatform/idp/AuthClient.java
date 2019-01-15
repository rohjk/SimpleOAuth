package com.jk.simpleplatform.idp;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.jk.simpleplatform.SimpleAuthResult;
import com.jk.simpleplatform.SimpleAuthResultCallback;

public abstract class AuthClient {

    private String serverId = "";
    protected boolean isSettedServerId = false;

    public static AuthClient getAuthClientInstance(IdpType idpType){
        switch (idpType){
            case GOOGLE :
                return new GoogleAuth();
        }

        return null;
    }

    public abstract void start(@NonNull Activity activity, @NonNull final SimpleAuthResultCallback<Void> callback);

    //public abstract void start(@NonNull Activity activity, @NonNull IdpType idp, @NonNull SimpleAuthResultCallback<Void> callback);

    public abstract void login(@NonNull Activity activity, @NonNull final SimpleAuthResultCallback<Void> callback);

    public abstract String getToken();

    public abstract String getEmail();

    public void setServerId(@NonNull String id){
        this.serverId = id;
        isSettedServerId = true;
    }

    public String getServerId(){
        return this.serverId;
    }

    public abstract boolean isSignedIn(@NonNull Activity activity);

    protected abstract void onActivityResult(Intent data);

}
