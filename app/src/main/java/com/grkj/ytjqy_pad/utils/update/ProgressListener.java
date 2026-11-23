package com.grkj.ytjqy_pad.utils.update;

/**
 * Created by yusam on 2018/7/20.
 */

public interface ProgressListener {
    void onProgress(long bytesRead, long contentLength, boolean done);
}
