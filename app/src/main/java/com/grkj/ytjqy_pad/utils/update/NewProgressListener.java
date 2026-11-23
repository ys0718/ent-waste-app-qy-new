package com.grkj.ytjqy_pad.utils.update;

/**
 * Created by yusam on 2018/7/20.
 */

public interface NewProgressListener {
    void onStart(long totalSize);

    void onProgress(int currentLength);

    void onFinish();

    void onFailure();
}
