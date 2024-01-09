package com.tuya.smart.bizubundle.demo;

import com.thingclips.smart.commonbiz.bizbundle.family.api.AbsBizBundleFamilyService;

public class BizBundleFamilyServiceImpl extends AbsBizBundleFamilyService {
    private long mHomeId;

    @Override
    public long getCurrentHomeId() {
        return mHomeId;
    }

    @Override
    public void shiftCurrentFamily(long familyId, String curName) {
        super.shiftCurrentFamily(familyId, curName);
        mHomeId = familyId;
    }
}
