package com.jk.simpleplatform.idp;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.jk.simpleplatform.SimpleAuthResult;
import com.jk.simpleplatform.SimpleAuthResultCallback;


public class FacebookAuth extends AuthClient {


    @Override
    public void start(@NonNull Activity activity, @NonNull SimpleAuthResultCallback<Void> callback) {

    }

    @Override
    public void login(@NonNull Activity activity, @NonNull SimpleAuthResultCallback<Void> callback) {

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
