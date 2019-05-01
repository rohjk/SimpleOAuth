package com.jk.simple.sample;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jk.simple.*;
import com.jk.simple.idp.IdpType;

public class MainActivity extends AppCompatActivity {

    TextView tokenTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tokenTextView = (TextView)findViewById(R.id.tokenView);
        Log.d("Main","Hello Git Android Project!");

        SimpleSession.setAuthProvider(IdpType.GOOGLE,"532271504370-irf3thlpul1n5cpvqnr9868g3eo5790k.apps.googleusercontent.com");
        SimpleSession.setAuthProvider(IdpType.FACEBOOK,"523512451420119");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        SimpleSession.onActivityResult(requestCode,resultCode,data);
    }

    //click Event
    public void onFacebookLoginClick(View v){
        SimpleSession.login(this, IdpType.FACEBOOK, new SimpleAuthResultCallback<Void>() {
            @Override
            public void onResult(SimpleAuthResult<Void> result) {
                onLoginCallback(result);
            }
        });

    }

    public void onGoogleLoginClick(View v){
        SimpleSession.login(this, IdpType.GOOGLE, new SimpleAuthResultCallback<Void>() {
            @Override
            public void onResult(SimpleAuthResult<Void> result) {
                onLoginCallback(result);
            }
        });

    }

    private void onLoginCallback(SimpleAuthResult<Void> result){
        StringBuilder builder = new StringBuilder();
        builder.append(result.isSuccess()? SimpleSession.getCurrentIdpType().name() + " Login is succeed" : "FAIL / " + result.getErrorCode() + " / " + result.getErrorMessaage());
        tokenTextView.setText(builder.toString());
    }

    public void onLogoutClick(View v){
        SimpleSession.logout();
        tokenTextView.setText("Logout!");
    }

    public void onlogedInClick(View v){
        StringBuilder builder = new StringBuilder();
        builder.append(SimpleSession.isSignedIn(this)? "Yes! Idp Type Is " + SimpleSession.getCurrentIdpType() : "No..");
        tokenTextView.setText(builder.toString());
    }

    public void onGetIdpType(View v){
        IdpType idpType = SimpleSession.getCurrentIdpType();
        tokenTextView.setText(idpType!= null? idpType.name() : "Null");
    }

    public void onGetAccessTokenClick(View v){
        tokenTextView.setText(SimpleSession.getAccessToken());
    }

    public void onGetEmailClick(View v){
        tokenTextView.setText(SimpleSession.getEmail());
    }


}
