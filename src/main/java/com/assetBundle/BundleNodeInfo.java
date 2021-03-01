package com.assetBundle;

import com.common.ArrayInputStream;
import com.common.ByteArray;
import com.common.StreamUtil;

import java.io.InputStream;
import java.util.Arrays;

public class BundleNodeInfo {
    private long offset;
    private long size;
    private int flag;
    private String path;

    private byte[] nodeData;

    public BundleNodeInfo(ArrayInputStream is) throws Exception {
        offset = StreamUtil.readLong(is);
        size = StreamUtil.readLong(is);
        flag = StreamUtil.readInt(is);
        path = StreamUtil.readString(is);
    }

    public void readNode(ByteArray is){
        nodeData = new byte[(int) size];
        is.getByteData((int) offset, (int) size, nodeData);
    }

    @Override
    public String toString() {
        return "BundleNodeInfo{" +
                "offset=" + Long.toHexString(offset) +
                ", size=" + Long.toHexString(size) +
                ", flag=" + Integer.toHexString(flag) +
                ", path='" + path + '\''+
                '}';
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public byte[] getNodeData() {
        return nodeData;
    }

    public void setNodeData(byte[] nodeData) {
        this.nodeData = nodeData;
    }
}
