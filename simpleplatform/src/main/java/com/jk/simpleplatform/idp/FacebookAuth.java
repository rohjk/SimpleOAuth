package com.jk.simpleplatform.idp;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.jk.simpleplatform.SimpleAuthResult;
import com.jk.simpleplatform.SimpleAuthResultCallback;
import com.jk.simpleplatform.SimpleAuthprovider;

import java.util.ArrayList;
import java.util.List;


public class FacebookAuth extends AuthClient {
    private static String TAG = "[FacebookAuth]";
    private static final String PERMISSIONS_PUBLIC_PROFILE = "public_profile";
    private static final String PERMISSIONS_EMAIL = "email";

    private static List<String> PERMISSIONS = new ArrayList();

    static {
        PERMISSIONS.add(PERMISSIONS_PUBLIC_PROFILE);
        PERMISSIONS.add(PERMISSIONS_EMAIL);
    }

    CallbackManager _callbackManager;

    SimpleAuthResultCallback<Void> loginCallback;
    AccessToken _accessToken;

    @Override
    @SuppressWarnings("deprecation")
    public void login(@NonNull final Activity activity, @NonNull final SimpleAuthResultCallback<Void> callback) {

        facebookInit(activity, new SimpleAuthResultCallback<Void>() {
            @Override
            public void onResult(SimpleAuthResult<Void> facebookInitResult) {
                if(facebookInitResult.isSuccess()){

                    _accessToken = AccessToken.getCurrentAccessToken();

                    if(_accessToken!=null){
                        // 앱실행 후, Facebook SDK 초기화를 처음 실행하면 Working Thread에서 작업이 수행됨
                        // Login API를 호출한 곳에서 UI 변경을 할 수 없음
                        // UI 변경이 가능하도록 하기위해 메인 쓰레드에서 callback을 invoke 한다
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callback.onResult(SimpleAuthResult.<Void>getSuccessResult(null));
                            }
                        });

                    }else{
                        loginCallback = callback;

                        _callbackManager = CallbackManager.Factory.create();

                        LoginManager.getInstance().logInWithReadPermissions(activity, PERMISSIONS);
                        LoginManager.getInstance().registerCallback(_callbackManager, new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                _accessToken = loginResult.getAccessToken();
                                callback.onResult(SimpleAuthResult.<Void>getSuccessResult(null));
                            }

                            @Override
                            public void onCancel() {
                                callback.onResult(SimpleAuthResult.<Void>getFailResult(-101, "User canceled log in."));
                            }

                            @Override
                            public void onError(FacebookException error) {
                                callback.onResult(SimpleAuthResult.<Void>getFailResult(-100, error.getMessage()));
                            }
                        });

                    }

                }else{
                    callback.onResult(facebookInitResult);
                }
            }
        });
    }

    private void facebookInit(final Activity mActivity, final SimpleAuthResultCallback<Void> callback){
        if(SimpleAuthprovider.getInstance().getServerId(IdpType.FACEBOOK)==null){
            callback.onResult( SimpleAuthResult.<Void>getFailResult(-100,"SERVER_ID_NULL"));

        }else {
            SimpleAuthResult<Void> initResult = null;

            FacebookSdk.setApplicationId(SimpleAuthprovider.getInstance().getServerId(IdpType.FACEBOOK));
            try {
                FacebookSdk.sdkInitialize(mActivity, new FacebookSdk.InitializeCallback() {
                    @Override
                    public void onInitialized() {
                        callback.onResult(SimpleAuthResult.<Void>getSuccessResult(null));
                    }
                });
            } catch (Exception ex) {
                Log.i(TAG, "Failed to auto initialize the Facebook SDK", ex);
                callback.onResult(SimpleAuthResult.<Void>getFailResult(-100,"FAIL_TO_INIT_FACEBOOK_CLIENT"));
            }
        }
    }

    @Override
    public String getToken() {
        return (_accessToken != null)? _accessToken.getToken() : "";
    }

    @Override
    public String getEmail() {
        return (_accessToken != null)? _accessToken.getUserId() : "";
    }

    @Override
    public IdpType getIdpType() {
        return IdpType.FACEBOOK;
    }

    @Override
    public boolean isSignedIn(@NonNull Activity activity) {
        return _accessToken != null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(_callbackManager!=null)
            _callbackManager.onActivityResult(requestCode,resultCode,data);
    }
}
