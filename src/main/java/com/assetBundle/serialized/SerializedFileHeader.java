package com.assetBundle.serialized;

import com.common.ArrayInputStream;
import static  com.common.StreamUtil.*;
public class SerializedFileHeader {

    private int metadataSize;
    private long fileSize;
    private int version;
    private long dataOffset;
    private byte endian;
    private byte[] reserved;
    private int targetPlatForm;//Android:13,win64:19
    private boolean enableTypeTree;
    private String unityVersion;

    public SerializedFileHeader(ArrayInputStream is) throws Exception {
        metadataSize = readInt(is);
        fileSize = readInt(is);
        version = readInt(is);
        dataOffset = readInt(is);
        if(version >= 9){
            endian = readByte(is);
            reserved = readBytes(is, 3);
        }else{
            is.setIdx((int) (fileSize-metadataSize));
            endian = readByte(is);
        }
        if(endian==0){
            is.setBigEndian(false);
        }
        if(version >= 22){
            metadataSize = readInt(is);
            fileSize = readLong(is);
            dataOffset = readLong(is);
            readLong(is);
        }
        if(version >= 7){
            unityVersion = readString(is);
        }
        if(version >= 8){
            targetPlatForm = readInt(is);
        }
        if(version >= 13){
            enableTypeTree = readBool(is);
        }
    }

    public int getMetadataSize() {
        return metadataSize;
    }

    public void setMetadataSize(int metadataSize) {
        this.metadataSize = metadataSize;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public long getDataOffset() {
        return dataOffset;
    }

    public void setDataOffset(long dataOffset) {
        this.dataOffset = dataOffset;
    }

    public byte getEndian() {
        return endian;
    }

    public void setEndian(byte endian) {
        this.endian = endian;
    }

    public byte[] getReserved() {
        return reserved;
    }

    public void setReserved(byte[] reserved) {
        this.reserved = reserved;
    }

    public int getTargetPlatForm() {
        return targetPlatForm;
    }

    public void setTargetPlatForm(int targetPlatForm) {
        this.targetPlatForm = targetPlatForm;
    }

    public boolean isEnableTypeTree() {
        return enableTypeTree;
    }

    public void setEnableTypeTree(boolean enableTypeTree) {
        this.enableTypeTree = enableTypeTree;
    }
}
