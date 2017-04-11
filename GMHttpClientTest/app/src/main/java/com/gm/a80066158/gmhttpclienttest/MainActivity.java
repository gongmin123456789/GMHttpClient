package com.gm.a80066158.gmhttpclienttest;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.gm.a80066158.gmhttpclient.GMHttpClient;
import com.gm.a80066158.gmhttpclient.IDownloadFileResponse;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initContent();
    }

    private void initContent() {
        Button button = (Button) findViewById(R.id.okButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOkButtonClicked();
            }
        });
    }

    private void onOkButtonClicked() {
        Log.i(TAG, "<onOkButtonClicked> start");

        testDownloadFile();
    }

    private void testGet() {
        Log.i(TAG, "<testGet> start");

        String url = "http://www.baidu.com";
        GMHttpClient.get(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "<onResponse> " + response.body().string());
            }
        });
    }

    private void testPost() {
        Log.i(TAG, "<testPost> start");

        String url = "http://192.168.1.112:28888/";
        String buf = "hello world";

        GMHttpClient.post(url, buf, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "<onResponse> " + response.body().string());
            }
        });
    }

    private void testDownloadFile() {
        Log.i(TAG, "<testDownloadFile> start");

        String url = "http://blog.csdn.net/tycoon1988/article/details/40080691";
        String savePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                                + "/baidu.html";
        GMHttpClient.downloadFile(url, savePath, new IDownloadFileResponse() {
            @Override
            public void onResponse(String url, String savePath, boolean success) {
                Log.i(TAG, "<testDownloadFile:onResponse> success = " + success);
            }
        });
    }
}
