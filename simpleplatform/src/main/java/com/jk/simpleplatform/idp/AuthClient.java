package com.jk.simpleplatform.idp;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.jk.simpleplatform.SimpleAuthResultCallback;

public abstract class AuthClient {

    protected static int AUTH_CLIENT_BASE_ERROR = -100;
    protected static int AUTH_CLIENT_USER_CANCELLED_ERROR = -101;
    protected static int AUTH_CLIENT_LOGIN_ERROR = -102;
    protected static int AUTH_CLIENT_PROVIDER_ERROR = -110;
    protected static int AUTH_CLIENT_INIT_ERROR = -120;

    protected static String AUTH_CLIENT_USER_CANCELLED_ERROR_MESSAGE = "USER_CANCELLED";

    public static AuthClient getAuthClientInstance(IdpType idpType){
        switch (idpType){
            case GOOGLE :
                return new GoogleAuthClient();
            case FACEBOOK :
                return new FacebookAuthClient();
        }

        return null;
    }

    public abstract void login(@NonNull Activity activity, @NonNull final SimpleAuthResultCallback<Void> callback);

    public abstract void logout();

    public abstract String getToken();

    public abstract String getEmail();

    public abstract IdpType getIdpType();

    public abstract boolean isSignedIn(@NonNull Activity activity);

    public abstract void onActivityResult(int requestCode, int resultCode, Intent data);

}
