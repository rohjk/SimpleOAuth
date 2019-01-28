# Simple OAuth
An Android Library used to Sign in Google, Facebook.

A library designed to use Oauth 2.0 easy to implement.

Simple OAuth Login! 

Super simple, super easy to use!

### Specs
[![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16)
[![](https://jitpack.io/v/rohjk/SimpleOAuth.svg)](https://jitpack.io/#rohjk/SimpleOAuth)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Simple%20OAuth-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/7488)


## Quick Setup

### 1. Include library

**Using Gradle**

Step 1. Add the JitPack repository to your build file

``` gradle
allprojects {
    repositories {
    	...
        maven { url 'https://jitpack.io' }
        
    }
}
```

Step 2. Add the dependency

``` gradle
dependencies {
    implementation 'com.github.rohjk:SimpleOAuth:1.2'
}
```

### 2. Usage


* In Activity :

#### a. Set IDP Provider

set Google OAuth 2.0 'Web Client ID', Facebook 'App ID'

``` java
 SimpleSession.setAuthProvider(IdpType.GOOGLE,"<OAuth 2.0 Web Client ID>");
 SimpleSession.setAuthProvider(IdpType.FACEBOOK,"<App ID>");
```

#### b. Override onActivityResult

``` java
@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        SimpleSession.onActivityResult(requestCode,resultCode,data);
    }
```

#### C. Call Login API

``` java
 IdpType idpType; // IdpType.GOOGLE or IdpType.FACEBOOK
 
 SimpleSession.login(this, idpType, new SimpleAuthResultCallback<Void>() {
          @Override
          public void onResult(SimpleAuthResult<Void> result) {
               if(result.isSuccess()){
                    //Success to Login!
                }else{
		    //Fail to Login!
                    int errorCode = result.getErrorCode();
                    String errorMessage = result.getErrorMessaage();
                }
          }
 });
```

#### D. Done! Super Simple



### 3. Other API

#### A. Get Sign in Status

``` java
 SimpleSession.isSignedIn(<Activity>);
```

#### B. Get Signed IDP Type

``` java
 SimpleSession.getCurrentIdpType();
```

#### C. Get Access Token

``` java
 SimpleSession.getAccessToken();
```

#### D. Get Email

``` java
 SimpleSession.getEmail();
```

#### E. Logout

``` java
 SimpleSession.logout();
```
