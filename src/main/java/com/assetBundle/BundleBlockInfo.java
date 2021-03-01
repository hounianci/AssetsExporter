package com.assetBundle;

import com.common.ArrayInputStream;
import com.common.ByteArray;
import com.common.StreamUtil;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4SafeDecompressor;

import java.io.IOException;
import java.io.InputStream;

public class BundleBlockInfo {
    private int compressSize;
    private int uncompressSize;
    private int flag;

    private byte[] compressData;
    private byte[] uncompressData;

    public BundleBlockInfo(ArrayInputStream is) throws Exception {
        uncompressSize = StreamUtil.readInt(is);
        compressSize = StreamUtil.readInt(is);
        flag = StreamUtil.readShort(is);
    }

    public void readBlock(InputStream is, ByteArray output) throws Exception {
        compressData = new byte[compressSize];
        is.read(compressData);
        LZ4Factory factory = LZ4Factory.fastestInstance();
        LZ4SafeDecompressor decompressor = factory.safeDecompressor();
        uncompressData = new byte[uncompressSize];
        decompressor.decompress(compressData, 0, compressSize, uncompressData, 0);

        output.addData(uncompressData);
    }

    @Override
    public String toString() {
        return "BundleBlockInfo{" +
                "compressSize=" + Integer.toHexString(compressSize) +
                ", uncompressSize=" + Integer.toHexString(uncompressSize) +
                ", flag=" + Integer.toHexString(flag) +
                '}';
    }

    public int getCompressSize() {
        return compressSize;
    }

    public byte[] getCompressData() {
        return compressData;
    }

    public void setCompressData(byte[] compressData) {
        this.compressData = compressData;
    }

    public byte[] getUncompressData() {
        return uncompressData;
    }

    public void setUncompressData(byte[] uncompressData) {
        this.uncompressData = uncompressData;
    }

    public void setCompressSize(int compressSize) {
        this.compressSize = compressSize;
    }

    public int getUncompressSize() {
        return uncompressSize;
    }

    public void setUncompressSize(int uncompressSize) {
        this.uncompressSize = uncompressSize;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
