package com.jk.simpleplatform.idp;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.jk.simpleplatform.SimpleAuthResult;
import com.jk.simpleplatform.SimpleAuthResultCallback;

public abstract class AuthClient {

    private String serverId = "";
    protected boolean isSettedServerId = false;

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

}
