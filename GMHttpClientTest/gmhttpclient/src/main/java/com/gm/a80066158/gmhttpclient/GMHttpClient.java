package com.gm.a80066158.gmhttpclient;

import android.renderscript.ScriptGroup;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 80066158 on 2017-04-10.
 */

public class GMHttpClient {
    private static final String TAG = "GMHttpClient";
    private static final int CONNECT_TIMEOUT = 60;  // second
    private static final int READ_TIMEOUT = 100;    // second
    private static final int WRITE_TIMEOUT = 60;    // second

    private static final int DOWNLOAD_FILE_BUF_LEN = 1024;

    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .build();

    public static void get(final String url, Callback callback) {
        Log.i(TAG, "<get> url = " + url);
        if (null == url) {
            return;
        }

        Request request = new Request.Builder().url(url).build();
        Call call  = okHttpClient.newCall(request);
        call.enqueue(callback);
    }

    public static void post(final String url, final String body, Callback callback) {
        Log.i(TAG, "<post> url = " + url + ", body = " + body);
        if (null == url) {
            return;
        }

        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, body);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
    }

    public static void downloadFile(final String url, final String savePath,
                                    final IDownloadFileResponse downloadFileResponse) {
        Log.i(TAG, "<downloadFile> url = " + url + ", savePath = " + savePath);
        if (null == url ||
                null == savePath) {
            return;
        }

        final Request request = new Request.Builder().url(url).build();
        Call call  = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                if (null != downloadFileResponse) {
                    downloadFileResponse.onResponse(url, savePath, false);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "<onResponse> url = " + url + ", savePath = " + savePath);
                InputStream inputStream = response.body().byteStream();
                OutputStream outputStream = new FileOutputStream(savePath);
                byte[] buffer = new byte[DOWNLOAD_FILE_BUF_LEN];
                int readLen = 0;
                int totalReadLen = 0;
                do {
                    readLen = inputStream.read(buffer, totalReadLen, DOWNLOAD_FILE_BUF_LEN);
                    if (readLen > 0) {
                        totalReadLen += readLen;
                        outputStream.write(buffer, 0, readLen);
                    } else {
                        break;
                    }
                } while (true);
                inputStream.close();
                outputStream.close();

                if (null != downloadFileResponse) {
                    downloadFileResponse.onResponse(url, savePath, true);
                }
            }
        });
    }
}
