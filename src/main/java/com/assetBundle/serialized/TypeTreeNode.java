package com.assetBundle.serialized;

import com.common.ArrayInputStream;
import static  com.common.StreamUtil.*;
public class TypeTreeNode {
    private String type;
    private String name;
    private int byteSize;
    private int index;
    private boolean isArray; //m_TypeFlags
    private int version;
    private int metaFlag;
    private int level;
    private int typeStrOffset;
    private int nameStrOffset;
    private long refTypeHash;
    public TypeTreeNode(ArrayInputStream is, SerializedFileHeader header) throws Exception {
        version = readShort(is);
        level = readByte(is);
        isArray = readBool(is);
        typeStrOffset = readInt(is);
        nameStrOffset = readInt(is);
        byteSize = readInt(is);
        index = readInt(is);
        metaFlag = readInt(is);
        if(header.getVersion() >= 19){
            refTypeHash = readLong(is);
        }
    }

    public TypeTreeNode(String type, String name, int level, boolean align)
    {
        type = type;
        name = name;
        level = level;
        metaFlag = align ? 0x4000 : 0;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getByteSize() {
        return byteSize;
    }

    public void setByteSize(int byteSize) {
        this.byteSize = byteSize;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean getIsArray() {
        return isArray;
    }

    public void setIsArray(boolean isArray) {
        this.isArray = isArray;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getMetaFlag() {
        return metaFlag;
    }

    public void setMetaFlag(int metaFlag) {
        this.metaFlag = metaFlag;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getTypeStrOffset() {
        return typeStrOffset;
    }

    public void setTypeStrOffset(int typeStrOffset) {
        this.typeStrOffset = typeStrOffset;
    }

    public int getNameStrOffset() {
        return nameStrOffset;
    }

    public void setNameStrOffset(int nameStrOffset) {
        this.nameStrOffset = nameStrOffset;
    }

    public long getRefTypeHash() {
        return refTypeHash;
    }

    public void setRefTypeHash(long refTypeHash) {
        this.refTypeHash = refTypeHash;
    }
}
