package com.assetBundle.serialized;

import com.common.ArrayInputStream;
import static com.common.StreamUtil.*;
public class ObjectInfo {
    private long byteStart;
    private int byteSize;
    private int typeID;
    private int classID;

    private long pathID;
    private SerializedType serializedType;

    public ObjectInfo(ArrayInputStream is, SerializedFile file, SerializedFileHeader header) throws Exception {
        is.alignStream();
        pathID = readLong(is);
        byteStart = readInt(is);
        byteStart += header.getDataOffset();
        byteSize = readInt(is);
        typeID = readInt(is);
        SerializedType type = file.getTypes().get(typeID);
        serializedType = type;
        classID = type.getClassID();
    }

    @Override
    public String toString() {
        return "ObjectInfo{" +
                "byteStart=" + Long.toHexString(byteStart) +
                ", byteSize=" + byteSize +
                ", typeID=" + typeID +
                ", classID=" + classID +
                ", pathID=" + pathID +
                '}';
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

    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

    public int getClassID() {
        return classID;
    }

    public void setClassID(int classID) {
        this.classID = classID;
    }

    public long getPathID() {
        return pathID;
    }

    public void setPathID(long pathID) {
        this.pathID = pathID;
    }

    public SerializedType getSerializedType() {
        return serializedType;
    }

    public void setSerializedType(SerializedType serializedType) {
        this.serializedType = serializedType;
    }
}
