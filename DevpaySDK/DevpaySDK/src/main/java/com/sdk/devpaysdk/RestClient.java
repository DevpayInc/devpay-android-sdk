package com.sdk.devpaysdk;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RestClient {

    Context context;
    String baseURL;
    HashMap<String, String> headers = new HashMap<>();
    public Boolean debug = false;

    RestClient(Context context, HashMap<String, String> headers, String baseURL) {
        this.context = context;
        this.headers = headers;
        this.baseURL = baseURL;
    }

    interface RestClientCallback {
        public void completion(Error err, JSONObject response);
    }

    public void get(String path, final RestClientCallback callback) {

        RequestQueue queue = Volley.newRequestQueue(context);

        String url = this.baseURL + path;
        logRequest("Post",url, null);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        logResponse(null, response.toString());
                        callback.completion(null, response);
                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        byte[] bytes = error.networkResponse.data;
                        String responseString = new String(bytes, StandardCharsets.UTF_8);
                        logResponse(buildError(error), responseString);
                        callback.completion(buildError(error), null);
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = super.getHeaders();

                if (headers == null
                        || headers.equals(Collections.emptyMap())) {
                    headers = new HashMap<String, String>();
                }

                headers.putAll(RestClient.this.headers);
                return headers;
            }
        };

        queue.add(jsonObjectRequest);

    }

    public void post(String path, JSONObject data,
                     final HashMap<String, String> localHeaders,
                     final RestClientCallback callback) {

        RequestQueue queue = Volley.newRequestQueue(context);

        String url = this.baseURL + path;
        logRequest("Get",url, data);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, data, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        logResponse(null, response.toString());
                        callback.completion(null, response);
                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        byte[] bytes = error.networkResponse.data;
                        String responseString = new String(bytes, StandardCharsets.UTF_8);
                        logResponse(buildError(error), responseString);
                        callback.completion(buildError(error), null);
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = super.getHeaders();

                if (headers == null
                        || headers.equals(Collections.emptyMap())) {
                    headers = new HashMap<String, String>();
                }

                headers.putAll(RestClient.this.headers);
                headers.putAll(localHeaders);

                return headers;
            }
        };

        queue.add(jsonObjectRequest);
    }

    Error buildError(VolleyError error) {

        if (error != null) {
            if (error.getMessage() != null) {
                return new Error(error.getMessage());
            }
        }

        byte[] bytes = error.networkResponse.data;
        String responseString = new String(bytes, StandardCharsets.UTF_8);
        return new Error(responseString);
    }

    void logRequest(String method,String url, JSONObject data) {

        if (debug) {
            Log.d("DevPaySDK: ","Request method - "+method);

            if (url != null) {
                Log.d("DevPaySDK: ","URL - "+ url);
            }
            if (data != null) {
                Log.d("DevPaySDK: ","Request data - "+ data.toString());
            }
        }
    }

    void logResponse(Error err, String response) {

        if (debug) {
            if (err != null) {
                if (err.getMessage() != null) {
                    Log.d("DevPaySDK: ","Response error"+ err.getMessage());
                } else {
                    Log.d("DevPaySDK: Response", "Failed with unknown error");
                }
            }
            if (response != null) {
                Log.d("DevPaySDK: ", "Response "+response.toString());
            }
        }
    }
}
