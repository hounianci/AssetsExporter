package com.assetBundle.serialized;

import com.common.ArrayInputStream;
import com.common.StreamUtil;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.common.StreamUtil.*;
public class SerializedFile {

    private byte[] data;
    private String fullName;
    private SerializedFileHeader header;
    private List<SerializedType> types;
    private List<ObjectInfo> objects;

    public static boolean isSerializedFile(ArrayInputStream is) throws Exception {
        int len = is.len();
        int metadataSize = readInt(is);
        long fileSize = readInt(is);
        int version = readInt(is);
        long dataOffset = readInt(is);
        byte endian = readByte(is);
        byte[] reserved = readBytes(is,3);
        if(version>=22){
            metadataSize = readInt(is);
            fileSize = readLong(is);
            dataOffset = readLong(is);
        }
        is.setIdx(0);
        if(len != fileSize){
            return false;
        }
        if(dataOffset>len){
            return false;
        }
        return true;
    }

    public SerializedFile(String fullName, ArrayInputStream is) throws Exception {
        this.fullName = fullName;
        header = new SerializedFileHeader(is);
        int typeCount = readInt(is);
        types = new ArrayList<>();
        for(int i=0; i<typeCount; i++){
            SerializedType type = new SerializedType(is, header);
            types.add(type);
        }
        int objectCount = readInt(is);
        objects = new ArrayList<>();
        for(int i=0; i<objectCount; i++){
            ObjectInfo objectInfo = new ObjectInfo(is, this, header);
            objects.add(objectInfo);
        }
        if(header.getVersion() >= 11){
            int scriptCount = readInt(is);
        }
        int externalsCount = readInt(is);
        String userInfo = readString(is);
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public SerializedFileHeader getHeader() {
        return header;
    }

    public void setHeader(SerializedFileHeader header) {
        this.header = header;
    }

    public List<SerializedType> getTypes() {
        return types;
    }

    public void setTypes(List<SerializedType> types) {
        this.types = types;
    }
}
