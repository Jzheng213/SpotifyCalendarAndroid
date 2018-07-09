package com.example.android.spotifycalendar.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.android.spotifycalendar.contracts.MonthlyContract;
import com.koushikdutta.async.http.body.JSONObjectBody;

import org.json.JSONObject;

import java.util.Locale;
import java.util.concurrent.Callable;

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
}
