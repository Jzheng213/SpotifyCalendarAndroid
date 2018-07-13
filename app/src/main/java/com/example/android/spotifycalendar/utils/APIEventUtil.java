package com.example.android.spotifycalendar.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.android.spotifycalendar.models.Event;
import com.example.android.spotifycalendar.views.EventForm.EventFormActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class APIEventUtil {

    public static void getEvents(Context context, final ResponseListener listener){
        String url = "https://spotify-calendar-backend.herokuapp.com/api/events";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response){
                        listener.onResponse(response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(String.format("api request fail: %s", error));
                Log.e("Volley","Error");
            }
        });

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public static void deleteEvent(int id, final Context context){
        String url = "https://spotify-calendar-backend.herokuapp.com/api/events";

        url = String.format(Locale.US, "%s/%d", url, id);
        StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.d("delete_request", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("delete_request", "delete failed" + error);
                    }
                }
        );

        VolleySingleton.getInstance(context).addToRequestQueue(deleteRequest);
    }

    public static void postEvent(Context context, JSONObject jsonObject, ResponseListener listener){
        String url = "https://spotify-calendar-backend.herokuapp.com/api/events";
        sendEvent(context, Request.Method.POST, url, jsonObject, listener);
    }

    public static void putEvent(Context context, JSONObject jsonObject, int id, ResponseListener listener){
        String url = "https://spotify-calendar-backend.herokuapp.com/api/events";
        url = String.format(Locale.US, "%s/%d", url, id);
        sendEvent(context, Request.Method.PUT, url, jsonObject, listener);
    }



    private static void sendEvent(
            final Context context,
            int requestMethod,
            String url,
            JSONObject jsonObject,
            final ResponseListener listener){

    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
            requestMethod,
            url,
            jsonObject,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    listener.onResponse(response);
                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            listener.onError(String.format("api request fail: %s", error));
            Log.e("post_request", "failed request");

        }
    }){
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return super.getParams();
        }

        @Override
        public Map<String, String> getHeaders() {
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json; charset=utf-8");
            return headers;
        }
    };

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }
}
