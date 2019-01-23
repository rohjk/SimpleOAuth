package com.jk.simpleplatform.idp;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.jk.simpleplatform.SimpleAuthResultCallback;

public class FacebookAuth extends AuthClient {

    SimpleAuthResultCallback<Void> loginCallback;

    @Override
    public void login(@NonNull Activity activity, @NonNull SimpleAuthResultCallback<Void> callback) {

    }

    private void facebookInit(SimpleAuthResultCallback<Void> callback){

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
    public IdpType getIdpType() {
        return IdpType.FACEBOOK;
    }

    @Override
    public boolean isSignedIn(@NonNull Activity activity) {
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
