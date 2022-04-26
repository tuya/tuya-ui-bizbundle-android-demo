package com.tencent.qqmusic.third.api.contract;

import com.tencent.qqmusic.third.api.contract.IQQMusicApiCallback;
import com.tencent.qqmusic.third.api.contract.IQQMusicApiEventListener;

interface IQQMusicApi {
    /**
     * @return {@link Keys}
     */
    Bundle execute(String action, in Bundle params);

    oneway void executeAsync(String action, in Bundle params, IQQMusicApiCallback callback);

    Bundle registerEventListener(in List<String> events, IQQMusicApiEventListener listener);

    Bundle unregisterEventListener(in List<String> events, IQQMusicApiEventListener listener);
}
