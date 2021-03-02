package com.assetBundle.asset;

import com.assetBundle.ObjectReader;
import com.assetBundle.serialized.ObjectInfo;

import java.util.Arrays;

public class AsObject {
    private int[] version;
    protected StreamingInfo streamingInfo;
    public AsObject(ObjectReader reader){
        reader.reset();
        version = reader.getAssetFile().getHeader().getVersionArray();
    }

    public int[] getVersion() {
        return version;
    }

    public void setVersion(int[] version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "AsObject{" +
                ", streamingInfo=" + streamingInfo.toString() +
                '}';
    }
}
