package com.gm.a80066158.gmhttpclient;

/**
 * Created by 80066158 on 2017-04-11.
 */

public interface IDownloadFileResponse {
    public void onResponse(final String url, final String savePath, boolean success);
}
