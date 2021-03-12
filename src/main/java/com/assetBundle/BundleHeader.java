package com.assetBundle;

import com.common.ArrayInputStream;
import com.common.ByteArray;
import com.common.StreamUtil;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4SafeDecompressor;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.common.StreamUtil.*;

public class BundleHeader {
    private long fileSize;
    private int headerCompressSize;
    private int headerUncompressSize;
    private int flag;
    private int blockInfoCount;
    private int nodeCount;

    private byte[] compressHeader;
    private byte[] uncompressHeader;

    private List<BundleBlockInfo> blockInfoList;
    private List<BundleNodeInfo> nodeList;

    public BundleHeader(ArrayInputStream is) throws Exception {
        String fileType = readString(is);
        int version = readInt(is);
        String unityVersion = readString(is);
        String unityReversion = readString(is);
        if(version>=7){
            is.alignStream(16);
        }
        fileSize = StreamUtil.readLong(is);
        headerCompressSize = StreamUtil.readInt(is);
        headerUncompressSize = StreamUtil.readInt(is);
        flag = StreamUtil.readInt(is);

        compressHeader = new byte[headerCompressSize];
        is.read(compressHeader);
        LZ4Factory factory = LZ4Factory.fastestInstance();
        LZ4SafeDecompressor decompressor = factory.safeDecompressor();
        uncompressHeader = new byte[headerUncompressSize];
        decompressor.decompress(compressHeader, 0, headerCompressSize, uncompressHeader, 0);

        ArrayInputStream headerIs = new ArrayInputStream(uncompressHeader);
        headerIs.read(new byte[16]);
        blockInfoCount = StreamUtil.readInt(headerIs);
        blockInfoList = new ArrayList<>();
        for(int i=0; i<blockInfoCount; i++){
            BundleBlockInfo blockInfo = new BundleBlockInfo(headerIs);
            blockInfoList.add(blockInfo);
        }
        nodeCount = StreamUtil.readInt(headerIs);
        nodeList = new ArrayList<>();
        for(int i=0; i<nodeCount; i++){
            BundleNodeInfo nodeInfo = new BundleNodeInfo(headerIs);
            nodeList.add(nodeInfo);
        }
    }

    public void readBlockData(InputStream is) throws Exception {
        ByteArray blockData = new ByteArray();
        for(int i=0; i<blockInfoCount; i++){
            blockInfoList.get(i).readBlock(is, blockData);
        }
        for(int i=0; i<nodeCount; i++){
            nodeList.get(i).readNode(blockData);
        }
    }

    @Override
    public String toString() {
        return "BundleHeader{" +
                "fileSize=" + Long.toHexString(fileSize) +
                ", headerCompressSize=" + Integer.toHexString(headerCompressSize) +
                ", headerUncompressSize=" + Integer.toHexString(headerUncompressSize) +
                ", flag=" + Integer.toHexString(flag) +
                ", blockInfoCount=" + Integer.toHexString(blockInfoCount) +
                ", nodeCount=" + Integer.toHexString(nodeCount) +
                '}';
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public int getHeaderCompressSize() {
        return headerCompressSize;
    }

    public int getBlockInfoCount() {
        return blockInfoCount;
    }

    public void setBlockInfoCount(int blockInfoCount) {
        this.blockInfoCount = blockInfoCount;
    }

    public int getNodeCount() {
        return nodeCount;
    }

    public void setNodeCount(int nodeCount) {
        this.nodeCount = nodeCount;
    }

    public List<BundleNodeInfo> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<BundleNodeInfo> nodeList) {
        this.nodeList = nodeList;
    }

    public void setHeaderCompressSize(int headerCompressSize) {
        this.headerCompressSize = headerCompressSize;
    }

    public int getHeaderUncompressSize() {
        return headerUncompressSize;
    }

    public void setHeaderUncompressSize(int headerUncompressSize) {
        this.headerUncompressSize = headerUncompressSize;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public byte[] getCompressHeader() {
        return compressHeader;
    }

    public void setCompressHeader(byte[] compressHeader) {
        this.compressHeader = compressHeader;
    }

    public byte[] getUncompressHeader() {
        return uncompressHeader;
    }

    public void setUncompressHeader(byte[] uncompressHeader) {
        this.uncompressHeader = uncompressHeader;
    }

    public List<BundleBlockInfo> getBlockInfoList() {
        return blockInfoList;
    }

    public void setBlockInfoList(List<BundleBlockInfo> blockInfoList) {
        this.blockInfoList = blockInfoList;
    }
}
