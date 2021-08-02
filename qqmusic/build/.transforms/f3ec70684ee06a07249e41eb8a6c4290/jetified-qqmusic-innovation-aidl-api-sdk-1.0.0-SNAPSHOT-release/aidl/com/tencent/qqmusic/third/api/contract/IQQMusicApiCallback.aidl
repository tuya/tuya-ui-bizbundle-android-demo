package com.tencent.qqmusic.third.api.contract;

interface IQQMusicApiCallback {
    /**
     * @result {@link Keys}
     */
    oneway void onReturn(in Bundle result);
}
