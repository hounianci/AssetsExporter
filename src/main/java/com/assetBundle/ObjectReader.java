package com.assetBundle;

import com.assetBundle.serialized.ObjectInfo;
import com.assetBundle.serialized.SerializedFile;
import com.common.ArrayInputStream;

public class ObjectReader {

    private SerializedFile assetFile;
    private long pathId;
    private long byteStart;
    private int byteSize;

    ArrayInputStream is;

    public ObjectReader(ArrayInputStream is, SerializedFile assetsFile, ObjectInfo objectInfo){
        this.is = is;
        is.setBigEndian(false);
        this.assetFile = assetsFile;
        pathId = objectInfo.getPathID();
        byteStart = objectInfo.getByteStart();
        byteSize = objectInfo.getByteSize();
    }
    public void reset(){
        is.setIdx((int) byteStart);
    }

    public SerializedFile getAssetFile() {
        return assetFile;
    }

    public void setAssetFile(SerializedFile assetFile) {
        this.assetFile = assetFile;
    }

    public long getPathId() {
        return pathId;
    }

    public void setPathId(long pathId) {
        this.pathId = pathId;
    }

    public long getByteStart() {
        return byteStart;
    }

    public void setByteStart(long byteStart) {
        this.byteStart = byteStart;
    }

    public int getByteSize() {
        return byteSize;
    }

    public void setByteSize(int byteSize) {
        this.byteSize = byteSize;
    }

    public ArrayInputStream getIs() {
        return is;
    }

    public void setIs(ArrayInputStream is) {
        this.is = is;
    }
}
