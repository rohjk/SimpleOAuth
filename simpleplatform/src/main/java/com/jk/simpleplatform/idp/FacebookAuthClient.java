package com.jk.simpleplatform.idp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.jk.simpleplatform.SimpleAuthResult;
import com.jk.simpleplatform.SimpleAuthResultCallback;
import com.jk.simpleplatform.SimpleAuthprovider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FacebookAuthClient extends AuthClient {
    private static String TAG = "[FacebookAuthClient]";

    private static int FACEBOOK_SDK_BASE_ERROR = -130;
    private static int FACEBOOK_SDK_GRAPH_ERROR = FACEBOOK_SDK_BASE_ERROR - 1;

    private static final String PERMISSIONS_PUBLIC_PROFILE = "public_profile";
    private static final String PERMISSIONS_EMAIL = "email";

    private static List<String> PERMISSIONS = new ArrayList();

    static {
        PERMISSIONS.add(PERMISSIONS_PUBLIC_PROFILE);
        PERMISSIONS.add(PERMISSIONS_EMAIL);
    }

    private CallbackManager _callbackManager;

    private SimpleAuthResultCallback<Void> loginCallback;
    private AccessToken _accessToken;
    private String _userEmail;

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
                                requestUserInfo(callback);
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

                                requestUserInfo(callback);
                            }

                            @Override
                            public void onCancel() {
                                callback.onResult(SimpleAuthResult.<Void>getFailResult(AUTH_CLIENT_USER_CANCELLED_ERROR, AUTH_CLIENT_USER_CANCELLED_ERROR_MESSAGE));
                            }

                            @Override
                            public void onError(FacebookException facebookError) {
                                facebookError.printStackTrace();
                                callback.onResult(SimpleAuthResult.<Void>getFailResult(AUTH_CLIENT_LOGIN_ERROR,"FACEBOOK_LOGIN_FAIL"));
                            }
                        });

                    }

                }else{
                    callback.onResult(facebookInitResult);
                }
            }
        });
    }

    @SuppressWarnings("deprecation")
    private void facebookInit(final Activity mActivity, final SimpleAuthResultCallback<Void> callback){
        if(SimpleAuthprovider.getInstance().getServerId(IdpType.FACEBOOK)==null){
            callback.onResult( SimpleAuthResult.<Void>getFailResult(AUTH_CLIENT_PROVIDER_ERROR,"SERVER_ID_NULL"));

        }else {
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
                callback.onResult(SimpleAuthResult.<Void>getFailResult(AUTH_CLIENT_INIT_ERROR,"FAIL_TO_INIT_FACEBOOK_CLIENT"));
            }
        }
    }

    private void requestUserInfo(final SimpleAuthResultCallback<Void> callback){
        GraphRequest graphRequest = GraphRequest.newMeRequest(_accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if(response.getError()==null){
                    //Success
                    try {
                        _userEmail = object.getString("email");
                        callback.onResult(SimpleAuthResult.<Void>getSuccessResult(null));
                    }catch (Exception e){
                        e.printStackTrace();
                        callback.onResult(SimpleAuthResult.<Void>getFailResult(FACEBOOK_SDK_BASE_ERROR,"GRAPH_REQUEST_PARSE_ERROR"));
                    }
                }else{
                    //Fail
                    callback.onResult(SimpleAuthResult.<Void>getFailResult(FACEBOOK_SDK_GRAPH_ERROR,"REQUEST_GRAPH_ERROR"));
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", PERMISSIONS_EMAIL);
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
    }

    @Override
    public void logout() {
        LoginManager.getInstance().logOut();
        clear();
    }

    private void clear(){
        _accessToken = null;
        _userEmail = null;
    }

    @Override
    public String getToken() {
        return (_accessToken != null)? _accessToken.getToken() : "";
    }

    @Override
    public String getEmail() {
        return (_userEmail != null)? _userEmail : "";
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
