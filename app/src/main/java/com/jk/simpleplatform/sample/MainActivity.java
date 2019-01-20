package com.jk.simpleplatform.sample;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jk.simpleplatform.*;
import com.jk.simpleplatform.idp.IdpType;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("Main","Hello Git Android Project!");
    }

    public void onLoginClick(View v){
        SimpleSession.setAuthProvider(IdpType.GOOGLE,"858478456785-geu04dmqo24q2dtet58ni1sbm67un54j.apps.googleusercontent.com");

        SimpleSession.login(this, IdpType.GOOGLE, new SimpleAuthResultCallback<Void>() {
            @Override
            public void onResult(SimpleAuthResult<Void> result) {
                StringBuilder builder = new StringBuilder();
                if(result.isSuccess()){
                    builder.append("SUCCESS ! / "+SimpleSession.getCurrentIdpType().name() + " / ");
                    builder.append(SimpleSession.getAccessToken());

                }else{
                    builder.append("FAIL ! / "+result.getErrorCode() + " / ");
                    builder.append(result.getErrorMessaage());
                }
                Toast.makeText(MainActivity.this, builder.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        SimpleSession.onActivityResult(requestCode,resultCode,data);

    }
}
