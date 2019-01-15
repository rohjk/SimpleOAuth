package com.jk.simpleplatform;

import android.support.annotation.NonNull;

public class SimpleAuthResult<T> {
    private boolean isSuccess;
    private T contents;

    private String errorMessaage;
    private int errorCode;

    public SimpleAuthResult(@NonNull boolean isSuccess, @NonNull T contents){
        this.isSuccess = isSuccess;
        this.contents = contents;
    }

    public SimpleAuthResult(@NonNull int errorCode, @NonNull String errorMessaage){
        this.isSuccess = false;
        this.errorCode = errorCode;
        this.errorMessaage = errorMessaage;
    }

    public static <T> SimpleAuthResult<T> getSuccessResult(T contents){
        return new SimpleAuthResult<T>( true, contents);
    }

    public static <T> SimpleAuthResult<T> getFailResult(@NonNull int errorCode, @NonNull String errorMessaage){
        return new SimpleAuthResult<T>(errorCode, errorMessaage);
    }

    public T getContents(){
        return contents;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getErrorMessaage() {
        return errorMessaage;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
