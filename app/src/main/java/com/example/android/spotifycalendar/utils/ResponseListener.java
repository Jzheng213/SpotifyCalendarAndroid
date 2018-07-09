package com.example.android.spotifycalendar.utils;

public interface ResponseListener {
    void onResponse(Object response);
    void onError(String message);
}
