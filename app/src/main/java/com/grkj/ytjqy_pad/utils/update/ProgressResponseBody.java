package com.grkj.ytjqy_pad.utils.update;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by yusam on 2018/7/20.
 */

public class ProgressResponseBody extends ResponseBody {

    private final ResponseBody responseBody;

    private final ProgressListener progressListener;

    private BufferedSource bufferedSource;

    public ProgressResponseBody(ResponseBody responseBody, ProgressListener progressListener) {
        this.responseBody = responseBody;
        //传入回调
        this.progressListener = progressListener;
    }

    //返回响应内容的类型，比如image/jpeg
    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    //返回响应内容的长度
    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    //BufferedSource可以理解为一个带有缓冲区的响应体
    @Override
    public BufferedSource source() {
        if (bufferedSource == null){
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }


    private Source source(Source source){
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;
            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink,byteCount);
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;   //不断统计当前下载好的数据
                //接口回调
                progressListener.onProgress(totalBytesRead,responseBody.contentLength(),bytesRead == -1);
                return bytesRead;
            }
        };
    }

}
