package com.example.android.spotifycalendar.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.Locale;

public class APIEventUtil {


    public static void deleteEvent(int id, final Context context){
        String url = "https://spotify-calendar-backend.herokuapp.com/api/events";
        url = String.format(Locale.US, "%s/%d", url, id);
        StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Toast.makeText(context, response, Toast.LENGTH_LONG).show();
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
