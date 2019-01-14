package com.naran.update;

/**
 * Created by Menksoft on 2018/1/8.
 */

public interface UpdateDownloadListener {
    public void onStarted();
    public void onProgressChanged(int progress, String downloadUrl);
    public void onFinished(float completeSize, String downloadUrl);
    public void onFailure();
}
