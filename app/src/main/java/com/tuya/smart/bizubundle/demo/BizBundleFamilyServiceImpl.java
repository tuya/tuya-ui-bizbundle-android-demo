package com.tuya.smart.bizubundle.demo;

import com.tuya.smart.commonbiz.bizbundle.family.api.AbsBizBundleFamilyService;

public class BizBundleFamilyServiceImpl extends AbsBizBundleFamilyService {
    private long mHomeId;

    @Override
    public long getCurrentHomeId() {
        return mHomeId;
    }

    @Override
    public void setCurrentHomeId(long l) {
        mHomeId = l;
    }

    @Override
    public void shiftCurrentFamily(long l, String s) {
        mHomeId = l;
    }
}
