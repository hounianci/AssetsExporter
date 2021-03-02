package com.assetBundle.asset;

import com.assetBundle.ObjectReader;
import com.common.ArrayInputStream;
import static com.common.StreamUtil.*;

public class StreamingInfo {
    private long offset;
    private int size;
    private String path;

    public StreamingInfo(ObjectReader reader) throws Exception {
        int[] version = reader.getAssetFile().getHeader().getVersionArray();
        ArrayInputStream is = reader.getIs();
        if(version[0] >= 2020){
            offset = readLong(is);
        }else{
            offset = readInt(is);
        }
        size = readInt(is);
        path = readAlignedString(is);
    }

    @Override
    public String
    toString() {
        return "StreamingInfo{" +
                "offset=" + offset +
                ", size=" + size +
                ", path='" + path + '\'' +
                '}';
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
