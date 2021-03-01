package com.assetBundle.serialized;

import com.common.ArrayInputStream;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import  static  com.common.StreamUtil.*;

public class SerializedType {
    private int classID;
    private boolean isStrippedType;
    private short scriptTypeIndex = -1;
    private List<TypeTreeNode> nodes;
    private byte[] scriptID; //Hash128
    private byte[] oldTypeHash; //Hash128
    private int[] typeDependencies;


    public SerializedType(ArrayInputStream is, SerializedFileHeader header) throws Exception {
        classID = readInt(is);
        if(header.getVersion() >= 16){
            isStrippedType = readBool(is);
        }
        if(header.getVersion() >= 17){
            scriptTypeIndex = readShort(is);
        }
        if(header.getVersion() >= 13){
            if((header.getVersion()<16&&classID<0) || (header.getVersion() >= 16 && classID == 114)){
                scriptID = readBytes(is, 16);
            }
            oldTypeHash = readBytes(is, 16);
        }
        if (header.isEnableTypeTree()){
            nodes = new ArrayList<>();
            if(header.getVersion()>=12 || header.getVersion()==10){
                typeTreeBlobRead(is, header);
            }else{
                //TODO readTypeTree
            }
            if(header.getVersion() >= 21){
                typeDependencies = readIntArray(is);
            }
        }
    }
    private void typeTreeBlobRead(ArrayInputStream is, SerializedFileHeader header) throws Exception {
        int numberOfNodes = readInt(is);
        int stringBufferSize = readInt(is);
        for(int i=0; i<numberOfNodes; i++){
            TypeTreeNode typeTreeNode = new TypeTreeNode(is, header);
            nodes.add(typeTreeNode);
        }
        byte[] stringBuffer = readBytes(is, stringBufferSize);
        ArrayInputStream stringIs = new ArrayInputStream(stringBuffer);
        for(int i=0; i<numberOfNodes; i++){
            TypeTreeNode node = nodes.get(i);
            node.setType(readTypeString(stringIs, node.getTypeStrOffset()));
            node.setName(readTypeString(stringIs, node.getNameStrOffset()));
        }

    }
    private String readTypeString(ArrayInputStream stringIs, int val) throws Exception {
        boolean isOffset = (val&0x80000000)==0;
        if(isOffset){
            stringIs.setIdx(val);
            return readString(stringIs);
        }
        int offset = val&0x7FFFFFFF;

        return  String.valueOf(offset);
    }

    public int getClassID() {
        return classID;
    }

    public void setClassID(int classID) {
        this.classID = classID;
    }

    public boolean isStrippedType() {
        return isStrippedType;
    }

    public void setStrippedType(boolean strippedType) {
        isStrippedType = strippedType;
    }

    public short getScriptTypeIndex() {
        return scriptTypeIndex;
    }

    public void setScriptTypeIndex(short scriptTypeIndex) {
        this.scriptTypeIndex = scriptTypeIndex;
    }

    public List<TypeTreeNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<TypeTreeNode> nodes) {
        this.nodes = nodes;
    }

    public byte[] getScriptID() {
        return scriptID;
    }

    public void setScriptID(byte[] scriptID) {
        this.scriptID = scriptID;
    }

    public byte[] getOldTypeHash() {
        return oldTypeHash;
    }

    public void setOldTypeHash(byte[] oldTypeHash) {
        this.oldTypeHash = oldTypeHash;
    }

    public int[] getTypeDependencies() {
        return typeDependencies;
    }

    public void setTypeDependencies(int[] typeDependencies) {
        this.typeDependencies = typeDependencies;
    }
}
