package com.tdj;

import com.assetBundle.BundleHeader;
import com.common.ArrayInputStream;

public class TdjBundleHeader extends BundleHeader {

    private byte[] headUnknownData;

    @Override
    protected void afterHeaderInfo(ArrayInputStream is) throws Exception {
        headUnknownData = new byte[70];
        is.read(headUnknownData);
    }

    public byte[] getHeadUnknownData() {
        return headUnknownData;
    }

    public void setHeadUnknownData(byte[] headUnknownData) {
        this.headUnknownData = headUnknownData;
    }
}
