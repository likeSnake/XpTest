package com.jcl.xptest.utli;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtils {
    private static final String TAG = "MyHttpUtils";

    public static void sendGetRequest(String urlString, final HttpCallback<String> callback) {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(urlString)
                .get()
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Request failed: " + e.getMessage());
                callback.onFailure(e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response!=null) {
                    try {

                        String responseString = response.body().string();
                        Log.d(TAG, "Response: " + responseString);


                        callback.onSuccess(responseString);


                    } catch (Exception e) {
                        Log.e(TAG, "JSON parse failed: " + e.getMessage());
                        callback.onFailure(e.getMessage());
                    }
                }
            }
        });

    }

    public static void sendGetRequest(String baseUrl, String param,Map<String,String> headers ,  final HttpCallback<String> callback) {

        OkHttpClient client = new OkHttpClient.Builder()
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .build();


        String urlString = baseUrl + "?" + param;

        Request.Builder requestBuilder = new Request.Builder()
                .url(urlString)
                .get();

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            MyUtil.MyLog(entry.getKey()+"--"+ entry.getValue());
            requestBuilder.addHeader(entry.getKey(), entry.getValue());
        }
        Request request = requestBuilder.build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Request failed: " + e.getMessage());
                callback.onFailure(e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response!=null) {
                    try {

                        String responseString = response.body().string();
                        Log.d(TAG, "Response: " + responseString);

                        callback.onSuccess(responseString);

                    } catch (Exception e) {
                        Log.e(TAG, "JSON parse failed: " + e.getMessage());
                        callback.onFailure(e.getMessage());
                    }
                }
            }
        });
    }

    public static void sendPostRequest(String urlString, String requestBody, Map<String,String> headers , final HttpCallback<String> callback) {

        OkHttpClient client = new OkHttpClient.Builder()
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .build();

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8");
        RequestBody body = RequestBody.create(requestBody, mediaType);
        Request.Builder requestBuilder = new Request.Builder()
                .url(urlString)
                .post(body);

        for (Map.Entry<String, String> entry : headers.entrySet()) {

            requestBuilder.addHeader(entry.getKey(), entry.getValue());
        }
        Request request = requestBuilder.build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Request failed: " + e.getMessage());
                callback.onFailure("Request failed: " + e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response!=null) {
                    try {
                        String responseString = response.body().string();

                        callback.onSuccess(responseString);
                    } catch (Exception e) {
                        Log.e(TAG, "JSON parse failed: " + e.getMessage());
                        callback.onFailure("JSON parse failed: " + e.getMessage());
                    }
                }
            }
        });
    }

    public static void sendPost( String urlString, String requestBody, Map<String, String> headers, final HttpCallback<String> callback) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // 设置请求头
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }

            // 发送请求体
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(requestBody.getBytes());
            outputStream.flush();
            outputStream.close();

            // 获取响应
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder responseBuilder = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line);
                }
                reader.close();
                inputStream.close();
                callback.onSuccess(responseBuilder.toString());
            } else {
                callback.onFailure("Request failed with response code " + responseCode);
            }

        } catch (Exception e) {
            Log.e(TAG, "Request failed: " + e.getMessage());
            callback.onFailure("Request failed: " + e.getMessage());
        }
    }

    public interface HttpCallback<T> {
        void onSuccess(T result);
        void onFailure(T failure);
    }
}
